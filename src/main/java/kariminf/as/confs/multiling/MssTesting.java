/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2015 Abdelkrime Aries <kariminfo0@gmail.com>
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
import java.util.HashMap;
import java.util.List;

import kariminf.ktoolja.math.Calculus;
import kariminf.as.process.topicclassif.Feature;
import kariminf.as.process.topicclassif.PLeng;
import kariminf.as.process.topicclassif.Pos;
import kariminf.as.process.topicclassif.RLeng;
import kariminf.as.process.topicclassif.TFB;
import kariminf.as.process.topicclassif.TFU;
import kariminf.ktoolja.file.FileManager;


public class MssTesting {

	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/multilingMss2015Testing/";

	private static final String outFolder =
			"/home/kariminf/Data/ATS/multilingMss2015Testing/AllSummarizer/";
	
	private static final String parFile =
			"/home/kariminf/Data/ATS/multilingMss2015Testing/parameters.info";

	private static final String [] langs = 
		{
		//"af", "ar", "bg", "ca", "cs", "de", "el", "en", "eo", 
		//"es", "eu", "fa", "fi", "fr", "he", "hr", "hu", "id", 
		"it"//, 
		//"ja", "ms", "nl", "no", "pt", "ro", "ru", "sh", 
		//"sl", "sr", "sv", "th", "tr", "vi", "zh"
		//"ka", "ko", "pl", "sk"
		};
	
	private static double getThreshold(String thName, MSS mss){
		
		List<Double> sim = Calculus.delMultiple(mss.getSimilarity(), 0.0);
		
		if(thName.startsWith("median"))
			return Calculus.median(sim);
		
		if(thName.startsWith("mean"))
			return Calculus.mean(sim);
		
		if(thName.startsWith("variance"))
			return Calculus.variance(sim);
		
		if(thName.startsWith("hmode"))
			return Calculus.modeHigh(sim);
		
		if(thName.startsWith("lmode"))
			return Calculus.modeLow(sim);
		
		double dist = mss.getTermDistribution();
		
		if(thName.startsWith("d/s"))
			return 1/dist;
		
		int sentNum = mss.sentNum();
		
		if(thName.startsWith("s/dn"))			
			return dist/sentNum;
		
		if(thName.startsWith("d/sn"))
			return 1/(dist * sentNum);
		
		return 0.0;
	}
	
	private static HashMap<String, List<String>> getParameters(){
		HashMap<String, List<String>> parameters =
				new HashMap<String, List<String>>();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(parFile));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.length() < 1)
					continue;
				
				String [] pars = line.split(",");
				if (pars.length < 3)
					continue;
				
				List<String> parsLst = new ArrayList<String>();
				parsLst.add(pars[1]);
				parsLst.add(pars[2]);
				parameters.put(pars[0], parsLst);
				
			}
			in.close();
			return parameters;
		}
		catch (IOException ex) {
			return parameters;
		}

	}
	
	private static void setFeatures(String featNames, MSS mss){
		
		featNames = featNames.toLowerCase();
		
		if (featNames.contains("pleng"))
			mss.addFeature(new PLeng());
		
		if (featNames.contains("pos"))
			mss.addFeature(new Pos());
		
		if (featNames.contains("rleng"))
			mss.addFeature(new RLeng());
		
		if (featNames.contains("tfb"))
			mss.addFeature(new TFB());
		
		if (featNames.contains("tfu"))
			mss.addFeature(new TFU());

	}

	private static HashMap<String, Integer> readSizes(String lang){
		HashMap<String, Integer> sizes =
				new HashMap<String, Integer>();

		File sizefile = new File(mssFolder + "target-length/" + lang + ".txt");

		if (! sizefile.exists()) return null;

		try {
			BufferedReader in = new BufferedReader(new FileReader(sizefile));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.trim().length() < 1) continue;

				String[] values = line.split(",");
				if (values.length<2) continue;

				sizes.put(values[0], new Integer(values[1]));
			}
			in.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return sizes;
		}

		return sizes;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HashMap<String, List<String>> parameters = getParameters();

		for (String lang: langs){//langs

			if(! parameters.containsKey(lang)){
				System.out.println("No parameters for this language");
				continue;
			}
			
			MSS mss = new MSS(lang);
			mss.noBrackets();
			HashMap<String, Integer> sizes = readSizes(lang);
			List<String> param = parameters.get(lang);
			

			File folder = new File(mssFolder + "body/text/" +  lang + "/");
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;

			File[] files = folder.listFiles();
			int count = 0;
			for (File file: files){

				String fileName = file.getName();
				if (! fileName.endsWith("_body.txt")) continue;
				
				int summarySize = sizes.get(fileName);
				
				count++;
				
				System.out.print(lang + count + ": ");
				
				//verify if the file have been already processed
				fileName = fileName.substring(0, fileName.length()-9) + ".txt";
				if (new File(outFolder + lang + "/" + fileName).exists()){
					System.out.println(fileName  + " already processsed");
					continue; 
				}
				
				FileManager.createFolder(new File(outFolder + lang + "/"));
				

				mss.preprocess(file);
				
				System.out.print("summary size= " + summarySize);
				
				double th = getThreshold(param.get(0), mss);
				System.out.println(", th= " + th);
				
				mss.cluster(th);
				
				mss.clearFeatures();
				setFeatures(param.get(1), mss);
				
				String summary="";
				try {
					summary = mss.summarize(summarySize, th);
				} catch (Exception e1) {
					e1.printStackTrace();
					summary="";
				}
				
				try {

					FileManager.saveFile(outFolder + lang + "/" + fileName, summary);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			} //files

		}//languages


	}

}
