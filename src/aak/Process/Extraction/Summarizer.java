/*
 * AllSumarizer v2
 * This file is part of AllSummarizer project; an implementation of the method
 * described in this paper:
 * http://dx.doi.org/10.1117/12.2004001
 * 
 * Copyright (C) 2013  Abdelkrime Aries <kariminfo0@gmail.com>
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

package aak.Process.Extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aak.Process.Extraction.Bayes.FreqTF;
import aak.Process.Extraction.Bayes.FreqTFBi;


public class Summarizer {
	//public final static TypeClassifier feature
	
	private List<Integer> orderNumSent = new ArrayList<Integer>();
	
	public void Summarize(HashMap<Integer, List<Integer>> classes, List<List<String>> sentWords) 
	{

		BayesClassifier classifier = new BayesClassifier();
			
		classifier.addFeature(new FreqTF());
		classifier.addFeature(new FreqTFBi());
			
		classifier.train(classes, sentWords);
		orderNumSent = classifier.classify(classes, sentWords);

	}
	
	
	
	public List<Integer> getSentNumber(int nbrSent) {
		
		if (nbrSent < 1)
			return null;

		return orderNumSent.subList(0, nbrSent-1);
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
