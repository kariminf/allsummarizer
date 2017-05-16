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

package kariminf.as.confs.multiling.multiling2015;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.ktoolja.math.Calculus;
import kariminf.as.confs.multiling.MMS;
import kariminf.as.process.tcc.Feature;
import kariminf.as.process.tcc.PLeng;
import kariminf.as.process.tcc.Pos;
import kariminf.as.process.tcc.RLeng;
import kariminf.as.process.tcc.TFB;
import kariminf.as.process.tcc.TFU;
import kariminf.ktoolja.file.FileManager;


public class MmsTraining {

	private static final String mmsFolder = 
			"/home/kariminf/Data/ATS/multilingMms2015Training/";

	private static final String outFolder =
			"/home/kariminf/Data/ATS/multilingMms2015Training/training2015/";

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

	private static Feature [] features = {
		new TFU(),
		new TFB(),
		new Pos(),
		new RLeng(),
		new PLeng(),
	};

	//add static thresholds let say 31 from 0.00 to 0.30
	private static String[] th_name = 
		{"mean", "median", "variance", "Hmode", "Lmode", 
		"s_Dn", "D_sn", "D_s"/*,
		"00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
		"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"*/
		};
	private static double[] th_value = 
		{.0, .0, .0, .0, .0, 
		.0, .0, .0/*,
		.00, .01, .02, .03, .04, .05, .06, .07, .08, .09,
		.10, .11, .12, .13, .14, .15, .16, .17, .18, .19,
		.20, .21, .22, .23, .24, .25, .26, .27, .28, .29, .30*/
		};

	
	public static HashMap<String, List<String>> getFiles(String lng){
		HashMap<String, List<String>> result = new HashMap<String, List<String>>();
		
		File folder = new File(mmsFolder + lng + "/sources/");
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

		
		List<List<Integer>> comb = new ArrayList<List<Integer>>();
		for (int combNbr=1; combNbr <= features.length; combNbr++)
			comb.addAll(Calculus.getCombinations(features.length, combNbr));
		
		for (String[] lang: langs){//langs
			
				
			MMS mms = new MMS(lang[1]);

			HashMap<String, List<String>> topics =  getFiles(lang[0]);
			
			System.out.println("topics #:" + topics.size());
			
			String newfolderName = outFolder + lang[0] + "/";
				FileManager.createFolder(new File(newfolderName));
			
			for(String topic: topics.keySet()){
				
				mms.clear();
				newfolderName = outFolder + lang[0] + "/";
				newfolderName += topic + "/";
				FileManager.createFolder(new File(newfolderName));
				
				System.out.println(lang[0] + ", topic" + topic);
				
				List<String> files = topics.get(topic);
				for(String file: files)
					mms.addDocument(file);
				
				mms.preprocess();
				
				{

					List<Double> sim = Calculus.delMultiple(mms.getSimilarity(), 0.0);

					th_value[0] = Calculus.mean(sim);
					th_value[1] = Calculus.median(sim);
					th_value[2] = Calculus.variance(sim);
					th_value[3] = Calculus.modeHigh(sim);
					th_value[4] = Calculus.modeLow(sim);

					double dist = mms.getTermDistribution();
					int sentNum = mms.sentNum();
					th_value[5] = dist/sentNum;
					th_value[6] = 1/(dist * sentNum);
					th_value[7] = 1/dist;

				}
				
				
				for(int th=0;th<th_value.length;th++){//threshold 40

					System.out.println("threshold = " + th_value[th]);
					
					String newfolderName2 = newfolderName + th_name[th] + "/";
					FileManager.createFolder(new File(newfolderName2));
					
					System.out.println("====> th= " + th);
					mms.cluster(th_value[th]);
					
					for (List<Integer> oneComb : comb){//features combinations
						mms.clearFeatures();

						String featused = "";
						for (int index: oneComb){
							mms.addFeature(features[index]);
							featused += features[index].getClass().getSimpleName() + "-";
						}
						featused = featused.substring(0, featused.length()-1);


						String summary="";
						try {
							summary = mms.summarize(th_value[th]);
						} catch (Exception e1) {
							e1.printStackTrace();
							summary="";
						}

						try {

							FileManager.saveFile(newfolderName2 + featused + ".asz", summary);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} //features combinations
					
					
				} // thresholds
			}//topics
			

		}//languages


	}

}
