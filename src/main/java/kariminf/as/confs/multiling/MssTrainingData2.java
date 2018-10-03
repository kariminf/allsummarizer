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
public class MssTrainingData2 {

	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/Mss15Test/";

	private static final String [] langs = 
		{"af", "ar", "bg", "ca", "cs", "de", "el", "eo", "es"};
		//{"eu", "fa", "fi", "fr", "he", "hr", "hu", "id", "it", "ja"};
		//{"ka", "ko", "ml", "ms", "nl", "nn", "no", "pl", "pt", "ro"};
		//{"ru", "sh", "sk", "sl", "sr", "sv", "th", "tr", "vi", "zh"};
		//{"en"};


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
			//FileManager.createFolder(new File(statsFolder));

			File[] files = folder.listFiles();
			for (File file: files){
				
				String fileName = file.getName();
				if (! fileName.endsWith("_body.txt")) continue;
				
				//verify if the file have been already processed
				fileName = fileName.substring(0, fileName.length()-9) ;
				
				System.out.println("processing file: " + file);
				String newfolderName = statsFolder + fileName + "/";
				
				Data data = new Data();
				StaticPreProcessor preProcessor = new StaticPreProcessor(lang);
				preProcessor.setData(data);
				preProcessor.preProcess(readFile(file));
				
				List<String> sentences = data.getSentences();
				List<List<Double>> sentSim = data.getSentSimilarities();
				
				String sSimOut = "{\n";
				
				for (int sID = 0; sID < sentences.size(); sID++) {
					
					String comma = (sID + 1 < sentences.size())? ",": "";
					String sIDstr = "\"" + sID + "\": ";
					List<Double> sims = sentSim.get(sID);
					sSimOut += sIDstr + sims + comma + "\n";
				}
				sSimOut += "}";
				
				try {
					FileManager.saveFile(newfolderName + "sentSimZ.json", sSimOut);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} //files
		}//languages
	}
}
