/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013-2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

package dz.aak.as.process.extraction;

import java.util.ArrayList;
import java.util.List;
import dz.aak.as.process.extraction.bayes.Feature;
import dz.aak.as.tools.Data;


public class Summarizer {
	//public final static TypeClassifier feature
	
	private List<Integer> orderNumSent = new ArrayList<Integer>();
	private List<Feature> features = new ArrayList<Feature>();
	
	public void addFeature(Feature feature){
		features.add(feature);
	}
	
	public void summarize(Data data) 
	{

		if (features.size() == 0) System.out.println("No feature is defined");
		BayesClassifier classifier = new BayesClassifier();
		classifier.setFeatures(features);
		
		//classifier.normalize(true);
		classifier.train(data);
		orderNumSent = classifier.classify(data);
	}
	
	
	public List<Integer> getSentNumber(int nbrSent) {
		
		if (nbrSent < 1)
			return null;

		return orderNumSent.subList(0, nbrSent);
	}
	
	
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
	
}
