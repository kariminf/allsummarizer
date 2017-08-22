/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013 Abdelkrime Aries <kariminfo0@gmail.com>
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

package kariminf.as.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import kariminf.ktoolja.math.Calculus;
import kariminf.as.tools.Data;
import kariminf.ktoolja.file.FileManager;
import kariminf.as.tools.Tools;



public class Duc04 {

	private static String cmplgfolder = "duc04/src/";
	private static String peerfolder = "duc04/peer/";
	private static String linefolder = "duc04/line/";
	private static String statsfolder = "duc04/stats/";
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
		
		File dir = new File(cmplgfolder);
		File [] topics  = dir.listFiles();
		String statContent = "doc,#sent,mean,median,variance,hmode,skewness\n";
		for(File topic : topics){
			
			if (! topic.isDirectory())
				continue;
			
			System.out.println("topic: " + topic.getName());
			
			Data data = new Data();
			{
				DynamicPreProcessor preprocess = new DynamicPreProcessor("en");
				preprocess.setData(data);
				File [] files  = topic.listFiles();
				for (File file: files){
					String text = openFile(file.getAbsolutePath());
					preprocess.addText(text);
				}
				preprocess.preProcess();
			}
			
			String topicname = topic.getName();
				
			//This is used to store statistics
			{
				
				statContent += topicname + ",";
				statContent += data.getSentNumber() + ",";
				List<Double> sim = Calculus.delMultiple(data.getSimList(), 0.0);
				statContent += Calculus.mean(sim) + ",";//mean
				statContent += Calculus.median(sim) + ",";//median
				statContent += Calculus.variance(sim) + ",";//variance
				statContent += Calculus.modeHigh(sim) + ","; //Hmode
				statContent += Calculus.skewness(sim) + "\n";//skewness
				
			}
			
			for(int th = 0; th <= 25; th++){
				
				{
					double Threshold = (double) th / 100;
					System.out.println("threshold: " + Threshold);
					Cluster cluster = new NaiveCluster(Threshold, data);
					cluster.createClasses();
				}
				
				for (int combNbr=1; combNbr <= features.length; combNbr++){
					List<List<Integer>> comb = 
							Calculus.getCombinations(features.length, combNbr);
					
					for (List<Integer> oneComb : comb){
						BayesScoreHandler bc = new BayesScoreHandler();
						String combStr = "";
						for (int index: oneComb){
							bc.addFeature(features[index]);
							combStr += features[index].getClass().getSimpleName() + "-";
						}
						Scorer scorer = Scorer.create(bc);
						combStr = combStr.substring(0, combStr.length()-1);
						
						FileManager.createFolder(new File(peerfolder + combStr));
						
						
						//System.out.println("features: " + combStr);
						
						scorer.setData(data);
						scorer.scoreUnits();
						List<Integer> order = scorer.getAmount(5);
						String summary = "";
						
						
						int OrderIDX = 1;
						List<List<String>> sentWords = data.getSentWords();
						summary += 	data.getSentence(order.get(0)) + "\n";
						List<String> prevWords = sentWords.get(order.get(0));
						
						while (true){
							
							List<String> actWords = sentWords.get(order.get(OrderIDX));
							
							do {
								if(OrderIDX >= order.size()) break;
								actWords = sentWords.get(order.get(OrderIDX));
								OrderIDX++;
							} while (Tools.similar(prevWords, actWords, 0.5));
							
							if (OrderIDX >= order.size()) break;
							
							prevWords = actWords;
							summary += 	data.getSentence(order.get(OrderIDX-1)) + "\n";
							
							if (summary.length() >= 665) break;
						}
						
						if (summary.length() > 665)
							summary = summary.substring(0, 666);
						
						combStr += "/" + topicname ;
						FileManager.createFolder(new File(peerfolder + combStr));
						combStr += "/" + th + ".asz";
						try {
							FileManager.saveFile(peerfolder + combStr, summary);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
								
					}
				}// combinations
			}//threshold
			System.out.println("......................");
		}//files
		
		
		
		//save statistics
		try {
			FileManager.saveFile(statsfolder + "stats.csv", statContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String openFile(String filePath){
		StringBuffer content = new StringBuffer();
		
		try {
		      BufferedReader in = new BufferedReader(new FileReader(filePath));
		      String line;
		      boolean getit = false;
		      while ( (line = in.readLine()) != null) {
		    	  if (line.contains("<TEXT>")){
		    		  getit=true;
		    		  continue;
		    	  }
		    	  
		    	  if (line.contains("</TEXT>"))
		    		  break;
		    	  
		    	  if (getit)
		    		  content.append(line + " ");
		        
		        //sb.append("/n"); to make it more platform independent (Log July 12, 2004)
		        //content.append("&&&&&&&&&&&&");
		      }
		      in.close();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		      return "";
		    }
		    
		return content.toString();
	}

}
