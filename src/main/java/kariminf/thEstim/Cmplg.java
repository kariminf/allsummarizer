/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2014-2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

package kariminf.thEstim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kariminf.as.preProcess.DynamicPreProcessor;
import kariminf.as.process.Scorer;
import kariminf.as.process.tcc.BayesScoreHandler;
import kariminf.as.process.tcc.Cluster;
import kariminf.as.process.tcc.Feature;
import kariminf.as.process.tcc.NaiveCluster;
import kariminf.as.process.tcc.PLeng;
import kariminf.as.process.tcc.Pos;
import kariminf.as.process.tcc.RLeng;
import kariminf.as.process.tcc.TFB;
import kariminf.as.process.tcc.TFU;
import kariminf.as.tools.*;
import kariminf.ktoolja.file.FileManager;
import kariminf.ktoolja.math.Calculus;



public class Cmplg {

	private static String cmplgfolder = "/home/kariminf/Data/ATS/cmplg/";
	private static String expfolder = cmplgfolder + "20141222_StatTh/";
	private static String peerfolder = expfolder + "peer/";

	private static Feature [] features = {
		new TFU(),
		new TFB(),
		new Pos(),
		new RLeng(),
		new PLeng(),
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File dir = new File(cmplgfolder + "source/");
		File [] files  = dir.listFiles();

		List<List<Integer>> comb = new ArrayList<List<Integer>>();
		for (int combNbr=1; combNbr <= features.length; combNbr++)
			comb.addAll(Calculus.getCombinations(features.length, combNbr));




		for(File file : files){

			System.out.println("file: " + file.getName());

			Data data = new Data();
			{
				DynamicPreProcessor preprocess = new DynamicPreProcessor("en");
				preprocess.setData(data);
				String text = FileManager.readFile(file);
				preprocess.preProcess(text);
			}

			String[] th_name = new String[6];
			double[] th_value = new double[6];
			//This is used to store statistics
			{

				List<Double> sim = Calculus.delMultiple(data.getSimList(), 0.0);

				th_name[0] = "mean"; th_value[0] = Calculus.mean(sim);
				th_name[1] = "median"; th_value[1] = Calculus.median(sim);
				th_name[2] = "variance"; th_value[2] = Calculus.variance(sim);
				th_name[3] = "Hmode"; th_value[1] = Calculus.modeHigh(sim);
				th_name[4] = "Lmode"; th_value[4] = Calculus.modeLow(sim);
				th_name[5] = "skewness"; th_value[5] = Calculus.skewness(sim);

			}

			String filename ="";
			{
				int dot = file.getName().lastIndexOf('.');
				filename = file.getName().substring(0, dot);
			}


			String combStr = peerfolder + filename + "/";

			FileManager.createFolder(new File(combStr));

			String info = "";
			for(int th = 0; th < 6; th++){

				{
					System.out.println(th_name[th] + ": " + th_value[th]);
					Cluster cluster = new NaiveCluster(th_value[th], data);
					cluster.createClasses();
				}

				info += th_name[th] + ": " + th_value[th] + "\n";

				for (List<Integer> oneComb : comb){
					
					combStr = peerfolder + filename + "/";
					BayesScoreHandler bc = new BayesScoreHandler();
					String featused = "";
					for (int index: oneComb){
						bc.addFeature(features[index]);
						featused += features[index].getClass().getSimpleName() + "-";
					}
					
					Scorer scorer = Scorer.create(bc);

					featused = featused.substring(0, featused.length()-1) + "/";

					combStr += featused;

					{
						FileManager.createFolder(new File(combStr));
						//System.out.println("features: " + featused);
						scorer.setData(data);
						scorer.scoreUnits();
						String summary = getSummary(data, scorer.getOrdered());

						combStr +=  th_name[th] + ".asz";
						try {
							FileManager.saveFile(combStr, summary);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}


				}// combinations

			}//threshold*/

			try {
				FileManager.saveFile(peerfolder + filename + "/info.txt", info);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("......................");
		}//files

		//System.out.println(comb);
	}


	public static String getSummary(Data data, List<Integer> order){

		List<List<String>> sentWords = data.getSentWords();
		List<String> sentences = data.getSentences();

		String summary = "";
		int numWords = 0;
		int numOrder = 0;

		while(true){

			if (numOrder >=  order.size())
				break;

			int index = order.get(numOrder);

			if (numOrder > 0){
				List<String> prevWords = sentWords.get(order.get(numOrder-1));
				List<String> actWords = sentWords.get(index);
				if (Tools.similar(prevWords, actWords, 0.5)){
					numOrder ++;
					if (numOrder < order.size())
						index = order.get(numOrder);
				}
			}

			if (data.getNbrWords(index) ==0){
				numOrder ++;
				continue;
			}


			numWords += data.getNbrWords(index);


			if (numWords > 240){
				if (numWords <= 250){
					summary += sentences.get(index) + "\n";
					break;
				}
				else{
					numOrder ++;
					continue;
				}
			}

			summary += sentences.get(index) + "\n";

			numOrder ++;
		}

		/*for(int i=0; i< nbrSent; i++)
			summary += sentences.get(order.get(i));*/
		return summary;
	}


}
