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
import kariminf.as.process.topicclassif.PLeng;
import kariminf.as.process.topicclassif.Pos;
import kariminf.as.process.topicclassif.RLeng;
import kariminf.as.process.topicclassif.TFB;
import kariminf.as.process.topicclassif.TFU;
import kariminf.ktoolja.file.FileManager;


public class MmsTesting {

	private static final String mmsFolder = 
			"/home/kariminf/Data/ATS/multilingMms2015Testing/body/";

	private static final String outFolder =
			"/home/kariminf/Data/ATS/multilingMms2015Testing/AllSummarizer_MMS/";
	
	private static final String parFile =
			"/home/kariminf/Data/ATS/multilingMms2015Testing/parameters.info";

	private static final String [][] langs = 
		{
		{"arabic","ar"},
		{"chinese","zh"},
		{"czech","cs"},
		{"english","en"},
		{"french","fr"},
		{"greek","el"},
		{"hebrew","he"},
		{"hindi","hi"},
		{"romanian","ro"},
		{"spanish","es"}
		};

	
	private static double getThreshold(String thName, MMS mms){
		
		List<Double> sim = Calculus.delMultiple(mms.getSimilarity(), 0.0);
		
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
		
		double dist = mms.getTermDistribution();
		
		if(thName.startsWith("d/s"))
			return 1/dist;
		
		int sentNum = mms.sentNum();
		
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
	
	private static void setFeatures(String featNames, MMS mms){
		
		featNames = featNames.toLowerCase();
		
		if (featNames.contains("pleng"))
			mms.addFeature(new PLeng());
		
		if (featNames.contains("pos"))
			mms.addFeature(new Pos());
		
		if (featNames.contains("rleng"))
			mms.addFeature(new RLeng());
		
		if (featNames.contains("tfb"))
			mms.addFeature(new TFB());
		
		if (featNames.contains("tfu"))
			mms.addFeature(new TFU());

	}
	
	public static HashMap<String, List<String>> getFiles(String lang){
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		
		File folder = new File(mmsFolder + lang);
		if (! folder.isDirectory()){
			System.out.println(folder.getAbsolutePath() + ": is not a folder");
			return result;
		}
		
		File[] files = folder.listFiles();
		
		for (File file: files){
			String filename = file.getName();
			String topic = filename.substring(0, 4);
			if (! result.containsKey(topic)){
				List<String> fileList = new ArrayList<String>();
				result.put(topic, fileList);
			}
			result.get(topic).add(file.getAbsolutePath());
		}
		
		return result;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HashMap<String, List<String>> parameters = getParameters();
		
		for (String[] lang: langs){//langs
			
			if(! parameters.containsKey(lang[1])){
				System.out.println("No parameters for this language");
				continue;
			}
			
			/*String newfolderName = outFolder + lang[0] + "/";
				FileManager.createFolder(newfolderName);*/
				
			MMS mms = new MMS(lang[1]);
			List<String> param = parameters.get(lang[1]);

			HashMap<String, List<String>> topics =  getFiles(lang[0]);
			
			for(String topic: topics.keySet()){
				
				mms.clear();
				setFeatures(param.get(1), mms);
				
				System.out.println(lang[0] + ", topic" + topic);
				
				List<String> files = topics.get(topic);
				for(String file: files)
					mms.addDocument(file);
				
				mms.preprocess();
				
				double th = getThreshold(param.get(0), mms);
				System.out.println("====> th= " + th);
				
				mms.cluster(th);
				
				String summary="";
				try {
					summary = mms.summarize(th);
				} catch (Exception e1) {
					e1.printStackTrace();
					summary="";
				}
				
				try {

					FileManager.saveFile(outFolder + topic + "." + lang[0], summary);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			

		}//languages


	}

}
