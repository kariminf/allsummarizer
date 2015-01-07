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

package as.Process.Extraction.Bayes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @class Rleng: real length (without stop-words' elimination)
 * @author Abdelkrime Aries
 * @use This feature is to score a sentence using its full length,
 */

public class RLeng implements Feature {

	/*private HashMap<Integer, List<Integer>> classLengFreq 
	= new HashMap<Integer, List<Integer>>();*/
	
	private HashMap<Integer, HashMap<Integer, Integer>> classLengFreq 
	= new HashMap<Integer, HashMap<Integer, Integer>>();
	
	private int catNbr = 0; // Number of categories; by default: infinite
	private double catStep = 0.0; // Step between a category and another
	
	public RLeng() {
		this.catNbr = 0;
	}
	
	public RLeng(int catNbr) {
		if (catNbr < 0)
			catNbr = - catNbr;
		
		this.catNbr = catNbr;
	}

	@Override
	public String getTrainParam() {
		return "classes,sentRLeng";
	}
	
	/**
	 * 
	 */
	@Override
	public void train(List<Object> trainParam) {
		
		if (catNbr > 0){
			trainCat(trainParam);
			return;
		}
		
		trainInf(trainParam);
	}

	
	/**
	 * We call this function when there is an infinite number of categories:
	 * It means, each length is a category
	 */
	private void trainInf(List<Object> trainParam) {
		
		@SuppressWarnings("unchecked")
		HashMap<Integer, List<Integer>> classes = 
				(HashMap<Integer, List<Integer>>) trainParam.get(0);
		
		@SuppressWarnings("unchecked")
		List<Integer>  nbrWords = 
				(List<Integer>) trainParam.get(1);
		
		
		for (int classID = 0; classID < classes.size(); classID++)
		{
			// each length is a category (we calculate the frequency of each length
			// If the length doesn't exist for a class, it's frequency is 0
			HashMap<Integer, Integer> lengFreq = new HashMap<Integer, Integer>();

			for (int sentID: classes.get(classID)){
				
				int leng = nbrWords.get(sentID);
				
				//If the length exist, we add 1 to its frequency
				if (lengFreq.containsKey(leng)){
					int freq = lengFreq.get(leng) + 1;
					lengFreq.put(leng, freq);
					continue;
				}
				
				//If the length doesn't exist, we add it to the frequency table
				lengFreq.put(leng, 1);
			}

			//for each class, we add it's length frequency table
			classLengFreq.put(classID, lengFreq);
		}
	}
	
	/**
	 * We call this function when there is a defined number of categories:
	 * It means, the lengths are divided to intervals
	 */
	private void trainCat(List<Object> trainParam) {
		
		@SuppressWarnings("unchecked")
		HashMap<Integer, List<Integer>> classes = 
				(HashMap<Integer, List<Integer>>) trainParam.get(0);
		@SuppressWarnings("unchecked")
		List<Integer>  nbrWords = 
				(List<Integer>) trainParam.get(1);
		
		{
			List<Integer> sentRLeng = new ArrayList<Integer>();
			sentRLeng.addAll(nbrWords);
			Collections.sort(sentRLeng);
			catStep = (double)(sentRLeng.get(sentRLeng.size()-1))/catNbr;
		}
		
		for (int classID = 0; classID < classes.size(); classID++)
		{
			// Here, the key is the Ceiling of the length divided by the step
			// It means, if we convert the lengths to "catNbr" of lengths
			HashMap<Integer, Integer> lengFreq = new HashMap<Integer, Integer>();
			
			for (int sentID: classes.get(classID)){
				
				double leng = (double) nbrWords.get(sentID);
				int newLeng = new Double(Math.ceil(leng/catStep)).intValue();
				
				//If the new length exist, we add 1 to its frequency
				if (lengFreq.containsKey(newLeng)){
					int freq = lengFreq.get(newLeng) + 1;
					lengFreq.put(newLeng, freq);
					continue;
				}
				
				//If the length doesn't exist, we add it to the frequency table
				lengFreq.put(newLeng, 1);
			}

			classLengFreq.put(classID, lengFreq);
		}
	}
	
	@Override
	public String getScoreParam() {
		return "sentRLeng";
	}
	
	/**
	 * 
	 * @param classID : the ID of the class
	 * @param scoreParam : the score Params
	 */
	public Double score(int classID, List<Object> scoreParam) {
		
		int sentLeng = (int) scoreParam.get(0);
		
		if (catNbr > 0){
			sentLeng = new Double(Math.ceil((double)sentLeng/catStep)).intValue();
			
		}
		
		if(classLengFreq.get(classID).containsKey(sentLeng))
				return (double) classLengFreq.get(classID).get(sentLeng);
		return 0.0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		HashMap<Integer, List<Integer>> classes = new HashMap<Integer, List<Integer>>() ;
		
		List<Integer> class0 = new ArrayList<Integer>();
		class0.add(0);class0.add(1);class0.add(4);
		
		List<Integer> class1 = new ArrayList<Integer>();
		class1.add(1);class1.add(2);
		
		List<Integer> class2 = new ArrayList<Integer>();
		class2.add(0); class2.add(2); class2.add(3); class2.add(5);
		
		List<Integer> class3 = new ArrayList<Integer>();
		class3.add(1); class3.add(5); class3.add(6); class3.add(7);
		
		classes.put(0, class0);
		classes.put(1, class1);
		classes.put(2, class2);
		classes.put(3, class3);
		
		
		List<Integer>  nbrWords = new ArrayList<Integer>();
		nbrWords.add(2); // s0
		nbrWords.add(5); // s1
		nbrWords.add(8); // s2
		nbrWords.add(7); // s3
		nbrWords.add(6); // s4
		nbrWords.add(2); // s5
		nbrWords.add(1); // s6
		nbrWords.add(5); // s7
		
		RLeng l = new RLeng(5);
		{
			List<Object> trainParams = new ArrayList<Object>();
			trainParams.add(classes);
			trainParams.add(nbrWords);
			l.train(trainParams);
		}
		
		{
			List<Object> scoreParams = new ArrayList<Object>();
			scoreParams.add(8);
			for (int classID = 0; classID < 4; classID++)
				System.out.println(l.score(classID, scoreParams));
		}

	}

}
