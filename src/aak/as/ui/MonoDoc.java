/*
 * AllSumarizer v2
 * This file is part of AllSummarizer project; an implementation of the method
 * described in this paper:
 * http://dx.doi.org/10.1117/12.2004001
 * 
 * Copyright (C) 2014  Abdelkrime Aries <kariminfo0@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package aak.as.ui;

import java.util.ArrayList;
import java.util.List;

import aak.as.preProcess.PreProcessor;
import aak.as.process.extraction.Summarizer;
import aak.as.process.extraction.bayes.Pos;
import aak.as.process.extraction.bayes.RLeng;
import aak.as.process.extraction.bayes.TFB;
import aak.as.process.extraction.cluster.Cluster;
import aak.as.process.extraction.cluster.NaiveCluster;
import aak.as.tools.Data;

public class MonoDoc {

	private List<String> sentences;
	private List<Integer> orderNumSent = new ArrayList<Integer>();
	private Double Threshold = -1.0;
	
	
	public void setThreshold (Double threshold){
		Threshold = threshold;
	}
	
	public void Summarize(String text){
		
		Data data = new Data();
		
		// Pre-Processing
		{
			PreProcessor preprocess = new PreProcessor("en", data);
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
