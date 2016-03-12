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

package kariminf.as.ui;

import java.util.ArrayList;
import java.util.List;

import kariminf.as.preProcess.PreProcessor;
import kariminf.as.process.extraction.Summarizer;
import kariminf.as.process.extraction.bayes.Pos;
import kariminf.as.process.extraction.bayes.RLeng;
import kariminf.as.process.extraction.bayes.TFB;
import kariminf.as.process.extraction.cluster.Cluster;
import kariminf.as.process.extraction.cluster.NaiveCluster;
import kariminf.as.tools.Data;


public class MonoDoc {

	private List<String> sentences;
	private List<Integer> orderNumSent = new ArrayList<Integer>();
	private List<Double> orderedScores = new ArrayList<Double>();
	private Double Threshold = -1.0;
	
	
	public void setThreshold (Double threshold){
		Threshold = threshold;
	}
	
	public void Summarize(String text){
		
		Data data = new Data();
		
		// Pre-Processing
		{
			PreProcessor preprocess = new PreProcessor("fr", data);
			preprocess.preProcess(text);
		}
		
		// Processing: Clustering
		{
			Cluster cluster = new NaiveCluster(Threshold, data);
			cluster.createClasses();
		}
		
		// Processing: Notation & Ordering
		{
			Summarizer summarizer = new Summarizer();
			summarizer.addFeature(new TFB());
			summarizer.addFeature(new RLeng());
			summarizer.addFeature(new Pos());
			summarizer.summarize(data);
			orderNumSent = summarizer.getOrdered();
			sentences = data.getSentences();
			for (int order: orderNumSent)
				orderedScores.add(summarizer.getScore(order));
		}
		
	}
	
	public String getSummaryPercent(int percent){
	
		int nbrSent = (int) Math.ceil(percent * orderNumSent.size() / 100);
		if (nbrSent<1)
			nbrSent = 1;
		
		return getSummaryNum(nbrSent);
	}
	
	public String getSummaryNum(int nbrSent){
		String summary = "";
		
		for(int i=0; i< nbrSent; i++)
			summary += sentences.get(orderNumSent.get(i));
		return summary;
	}
	
	
	public List<String> getOrdered(){
		List<String> ordered = new ArrayList<String>();
		for (int order: orderNumSent)
			ordered.add(sentences.get(order));
		
		return ordered;
	}
	
	public List<Double> getOrderedScores(){
		//encapsulation breach (we gave the address of the arrayList
		return orderedScores;
	}
	
	/*
	public List<Integer> getSentPercent(int percent) {
		
		if (percent < 1)
			return null;
		int nbrSent = (int) Math.ceil(percent * orderNumSent.size() / 100);
		
		if (nbrSent<1)
			nbrSent = 1;
		
		return orderNumSent.subList(0, nbrSent-1);
	}
		
	public List<Integer> getOrdered(){

		return orderNumSent;
	}
	*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
