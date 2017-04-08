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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.preProcess.DynamicPreProcessor;
import kariminf.as.process.Scorer;
import kariminf.as.process.topicclassif.BayesScoreHandler;
import kariminf.as.process.topicclassif.bayes.Feature;
import kariminf.as.process.topicclassif.bayes.PLeng;
import kariminf.as.process.topicclassif.bayes.Pos;
import kariminf.as.process.topicclassif.bayes.RLeng;
import kariminf.as.process.topicclassif.bayes.TFB;
import kariminf.as.process.topicclassif.bayes.TFU;
import kariminf.as.process.topicclassif.cluster.Cluster;
import kariminf.as.process.topicclassif.cluster.NaiveCluster;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;


public class MMS {

	
	
	private List<Feature> features = new ArrayList<Feature>();
	private String lang = "en";
	
	private Data data;
	private boolean clustered = false;
	private DynamicPreProcessor preprocessor;
	private final static int summarySize = 250; //words
	private final static int zhSummarySize = 750; //bytes
	
	
	public MMS (String lang){
		this.lang = (lang.length()==2)?lang:"en";
		clustered = false;
		data = new Data();
		preprocessor = new DynamicPreProcessor(this.lang, this.data);
		
	}
	
	public void addDocument(String file){
		String text = readFile(file);
		preprocessor.addText(text);
	}
	
	public void preprocess(){
		preprocessor.preProcess();
		clustered = false;
	}
	
	public void cluster(double threshold){
		Cluster cluster = new NaiveCluster(threshold, data);
		cluster.createClasses();
		clustered = true;
		//System.out.println("#sent=" + data.getSentNumber());
	}
	

	/**
	 * Use a feature for scoring
	 * @param feature
	 */
	public void addFeature(Feature feature){
		if (!features.contains(feature))
			features.add(feature);
	}
	
	public String summarize (double simTH) throws Exception{
		
		if(features.size() <1 ) throw new Exception("add at least one feature");
		if (! clustered ) throw new Exception("Use cluster before summarize");
		
		BayesScoreHandler bc = new BayesScoreHandler();
		Scorer scorer = Scorer.create(bc);
		
		for (Feature feature: features)
			bc.addFeature(feature);
		
		scorer.setData(data);
		scorer.scoreUnits();
		
		return getSummary(data, scorer.getOrdered(), simTH);
	}
	
	/**
	 * Clear the used features if there is any
	 */
	public void clear(){		preprocessor.clearText();
		features.clear();
		preprocessor.clearText();
	}
	
	/**
	 * Clear the used features if there is any
	 */
	public void clearFeatures(){
		features.clear();
	}
	
	public List<Double> getSimilarity(){
		return data.getSimList();
	}
	
	public int sentNum (){
		return data.getSentNumber();
	}
	
	public double getTermDistribution(){
		List<List<String>> sentWords = data.getSentWords();
		return Tools.termsDistribution(sentWords);
	}


	private String readFile(String file){
		StringBuffer content = new StringBuffer();

		int i = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.length() < 1)
					continue;
				i++;
				if (i>2) //don't take the title and the date
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
	
	
	private String getSummary(Data data, List<Integer> order, double simTH){

		
		if (lang.startsWith("zh"))
			return getChineseSummary (data, order, simTH);
		
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();
		List<Integer> nbrWords = data.getNbrWords();

		String summary = "";
		int numChars = 0;
		int numOrder = 0;

		while(true){

			if (numOrder >=  order.size())
				break;

			int index = order.get(numOrder);

			if (numOrder > 0){
				List<String> prevWords = sentWords.get(order.get(numOrder-1));
				List<String> actWords = sentWords.get(index);
				if (Tools.similar(prevWords, actWords, simTH)){
					numOrder ++;
					if (numOrder < order.size())
						index = order.get(numOrder);
				}
			}
			
			numChars += nbrWords.get(index);
			
			if (numChars > summarySize)
				break;
			
			summary += sentences.get(index) + "\n";

			numOrder ++;
		}
		
		if(summary.length() < 1)
			summary = sentences.get(order.get(0)).substring(0, summarySize);

		return summary;
	}
	
	private String getChineseSummary (Data data, List<Integer> order, double simTH){
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();

		String summary = "";
		int numChars = 0;
		int numOrder = 0;

		while(true){

			if (numOrder >=  order.size())
				break;

			int index = order.get(numOrder);

			if (numOrder > 0){
				List<String> prevWords = sentWords.get(order.get(numOrder-1));
				List<String> actWords = sentWords.get(index);
				if (Tools.similar(prevWords, actWords, simTH)){
					numOrder ++;
					if (numOrder < order.size())
						index = order.get(numOrder);
				}
			}

			try {
				final byte[] utf8Bytes = sentences.get(index).getBytes("UTF-8");
				numChars += utf8Bytes.length;
			} catch (UnsupportedEncodingException e) {
				numChars += sentences.get(index).length();
			}

			if (numChars > zhSummarySize)
				break;
			
			summary += sentences.get(index) + "\n";

			numOrder ++;
		}

		return summary;
	}


}
