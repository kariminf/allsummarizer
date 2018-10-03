/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2018 Abdelkrime Aries <kariminfo0@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kariminf.as.confs.multiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kariminf.ktoolja.math.Calculus;
import kariminf.langpi.eval.ats.KRouge;
import kariminf.as.preProcess.StaticPreProcessor;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;
import kariminf.ktoolja.file.FileManager;


/**
 * Extracts information from Multiling Mss dataset
 * @author kariminf
 *
 */
public class MssTrainingData {

	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/Mss15Test/";

	private static final String [] langs = 
		//{"af", "ar", "bg", "ca", "cs", "de", "el", "eo", "es"};
		//{"eu", "fa", "fi", "fr", "he", "hr", "hu", "id", "it", "ja"};
		//{"ka", "ko", "ml", "ms", "nl", "nn", "no", "pl", "pt", "ro"};
		//{"ru", "sh", "sk", "sl", "sr", "sv", "th", "tr", "vi", "zh"};
		{"en"};


	public static String readFile(File file){
		StringBuffer content = new StringBuffer();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.startsWith("[") || line.length() < 1)
					continue;
				if(line.length() < 50)
					if (! line.matches(".*[\\.\\!\\?؟。]$"))
						continue;
				content.append(line + " ");
			}
			in.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return "";
		}

		return content.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (String lang: langs) {//langs
			System.out.println("processing language: " + lang);
			File folder = new File(mssFolder + "src/body/text/" +  lang + "/");
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;
			
			String statsFolder = mssFolder  + "stats/" + lang + "/";
			
			String modelFolder = mssFolder  + "model/" + lang + "/";
			FileManager.createFolder(new File(statsFolder));

			int maxSentTFLength = 0;
			int maxDocTFLength = 0;
			int maxSentSimLength = 0;
			int maxDocSimLength = 0;
			int maxDocSizesLength = 0;
			File[] files = folder.listFiles();
			for (File file: files){
				
				String fileName = file.getName();
				if (! fileName.endsWith("_body.txt")) continue;
				
				//verify if the file have been already processed
				fileName = fileName.substring(0, fileName.length()-9) ;
				if (new File(mssFolder  + "features/" + lang + "/" + fileName).exists()){
					System.out.println(fileName  + " already processsed");
					continue;
				}
				
				System.out.println("processing file: " + file);
				String newfolderName = statsFolder + fileName + "/";
				FileManager.createFolder(new File(newfolderName));
				
				KRouge rouge1 = new KRouge(KRouge.GramType.UNI);
				//Processing the model
				{
					Data data = new Data();
					StaticPreProcessor preProcessor = new StaticPreProcessor(lang);
					preProcessor.setData(data);
					//For MSS Train data
					//File model = new File(modelFolder + fileName + "_summary.txt");
					//For MSS Test Data
					File model = new File(modelFolder + fileName + ".txt");
					preProcessor.preProcess(readFile(model));
					
					rouge1.newModel();
					for (List<String> sentence: data.getSentWords()) {
						rouge1.addModelSentence(sentence);
					}
				}
				
				Data data = new Data();
				StaticPreProcessor preProcessor = new StaticPreProcessor(lang);
				preProcessor.setData(data);
				preProcessor.preProcess(readFile(file));
				
				List<String> sentences = data.getSentences();
				List<List<String>> sentWords = data.getSentWords();
				List<List<Double>> sentSim = data.getSentSimilarities();
				
				String sOut = "{\n", 
						sLenOut = "{\n",
						sSimOut = "{\n",
						sFreqOut = "{\n",
						rouge1Out = "{\n";
				
				List<Integer> docSentSizes = new ArrayList<>();
				
				int maxSentSimLength2 = 0;
				int maxSentTFLength2 = 0;
				
				HashMap<String, Integer> docTFs = Tools.getDocWordsFreq(sentWords);
				
				for (int sID = 0; sID < sentences.size(); sID++) {
					
					String comma = (sID + 1 < sentences.size())? ",": "";
					String sIDstr = "\"" + sID + "\": ";
					
					sOut += sIDstr + "\"" + 
					sentences.get(sID).replaceAll("\"", "\\\\\"") + "\"" + comma + "\n";
					
					int size = sentWords.get(sID).size();
					sLenOut += sIDstr + size + comma + "\n";
					docSentSizes.add(size);
					List<Double> sims = Calculus.delMultiple(sentSim.get(sID), 0.0);
					sSimOut += sIDstr + sims + comma + "\n";
					if(sims.size() > maxSentSimLength) 
						maxSentSimLength = sims.size();
					if(sims.size() > maxSentSimLength2) 
						maxSentSimLength2 = sims.size();
					List<Integer> freqs = 
							Tools.getSent2DocWordsFreq(sentWords.get(sID), docTFs);
					sFreqOut += sIDstr + freqs + comma + "\n";
					if(freqs.size() > maxSentTFLength) 
						maxSentTFLength = freqs.size();
					if(freqs.size() > maxSentTFLength2) 
						maxSentTFLength2 = freqs.size();
					
					rouge1.resetPeer();
					rouge1.addPeerSentence(sentWords.get(sID));
					rouge1.calculate();
					
					rouge1Out += sIDstr + rouge1.getRecall() + comma + "\n";
				}
				
				sOut += "}";
				sLenOut += "}";
				sSimOut += "}";
				sFreqOut += "}";
				rouge1Out += "}";
				
				String docInfo = "{\n";
				
				docInfo += "\"size\": " + sentences.size() + ",\n";
				docInfo += "\"sizes\": " + docSentSizes + ",\n";
				if (docSentSizes.size() > maxDocSizesLength)
					maxDocSizesLength = docSentSizes.size();
				List<Double> docSims = Calculus.delMultiple(data.getSimList(), 0.0);
				docInfo += "\"sims\": " + docSims + ",\n";
				if (docSims.size() > maxDocSimLength)
					maxDocSimLength = docSims.size();
				Collection<Integer> docFreqs = docTFs.values();
				docInfo += "\"freqs\": " + docFreqs + ",\n";
				if (docFreqs.size() > maxDocTFLength)
					maxDocTFLength = docFreqs.size();
				docInfo += "\"maxSentSimLength\": " + maxSentSimLength2 + ",\n";
				docInfo += "\"maxSentTFLength\": " + maxSentTFLength2 + "\n";
				docInfo += "}";
				
				try {
					FileManager.saveFile(newfolderName + "sents.json", sOut);
					FileManager.saveFile(newfolderName + "sentLen.json", sLenOut);
					FileManager.saveFile(newfolderName + "sentSim.json", sSimOut);
					FileManager.saveFile(newfolderName + "sentTF.json", sFreqOut);
					FileManager.saveFile(newfolderName + "sentRouge1.json", rouge1Out);
					FileManager.saveFile(newfolderName + "docInfo.json", docInfo);
				} catch (IOException e) {
					e.printStackTrace();
				}


			} //files
			
			String langOut = "{\n";
			langOut += "\"maxSentTFLength\": " + maxSentTFLength + ",\n";
			langOut += "\"maxDocTFLength\": " + maxDocTFLength + ",\n";
			langOut += "\"maxSentSimLength\": " + maxSentSimLength + ",\n";
			langOut += "\"maxDocSimLength\": " + maxDocSimLength + ",\n";
			langOut += "\"maxDocSizesLength\": " + maxDocSizesLength + "\n";
			langOut += "}";
			
			try {
				FileManager.saveFile(statsFolder + "lang.json", langOut);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//languages
	}
}
