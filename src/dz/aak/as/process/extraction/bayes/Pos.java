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

package dz.aak.as.process.extraction.bayes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * This feature is used to score each sentence using its position in the text.
 * p-avg = 
 */
public class Pos implements Feature {

	/*
	List<List<String>> sentences;
	HashMap<Integer, List<Integer>> classes;
	HashMap<Integer, Integer> sentPos;*/
	
	private float posPart;
	
	private HashMap<Integer, List<Integer>> classPosFreq 
	= new HashMap<Integer, List<Integer>>();

	@Override
	public String getTrainParam() {
		return "classes,sentPos";
	}
	
	@Override
	public void train(List<Object> trainParam) {
	
		@SuppressWarnings("unchecked")
		HashMap<Integer, List<Integer>> classes = 
				(HashMap<Integer, List<Integer>>) trainParam.get(0);
		@SuppressWarnings("unchecked")
		HashMap<Integer, Integer> sentPos = 
				(HashMap<Integer, Integer>) trainParam.get(1);
		int maxpos = 0;
		
		for (int sentID : sentPos.keySet()){
			if (maxpos < sentPos.get(sentID))
				maxpos = sentPos.get(sentID);
			
		}
				
		posPart = (float) maxpos/ 10 ; 
		
		//System.out.println("posPart=" + posPart);
		
		//Reset the classPosFreq, when training this feature another time
		classPosFreq = new HashMap<Integer, List<Integer>>();
		
		for (int classID=0; classID< classes.size(); classID++){
			
			List<Integer> posFreq = new ArrayList<Integer>();
			
			for (int lengCat = 0; lengCat < 10; lengCat++)
				posFreq.add(0);
			
			for (int sentID: classes.get(classID)){
				int pos = sentPos.get(sentID);
				
				for (int posCat = 0; posCat < 10; posCat++)
					if (pos <= posPart *(posCat +1)){
						posFreq.set(posCat, (posFreq.get(posCat)+1));
						break;
					}
			}
			
			classPosFreq.put(classID, posFreq);
		}
			/*
		this.sentences = sentences;
		this.classes = classes;
		this.sentPos = sentPos;*/
	}

	@Override
	public String getScoreParam() {
		return "sentPos";
	}
	
	/**
	 * Score a sentence using the equation:
	 * score(s_i in C_j / pos) = cos_max (s_i,s)*score(s); s in C_j
	 */
	@Override
	public Double score(int classID, List<Object> scoreParam) {
		
		int sentPos = (int) scoreParam.get(0);
		
		for (int posCat = 0; posCat < 10; posCat++)
			if (sentPos <= posPart *(posCat +1))
				return (double) classPosFreq.get(classID).get(posCat);
		
		return 0.0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HashMap<Integer, List<Integer>> classes = new HashMap<Integer, List<Integer>>() ;
		List<List<String>> sentences = new ArrayList<List<String>>();
		HashMap<Integer, Integer> sentPos = new HashMap<Integer, Integer>();
		
		List<String> sent0 = new ArrayList<String>();
		sent0.add("1"); sent0.add("2");sent0.add("3"); sent0.add("4");sent0.add("5");
		
		List<String> sent1 = new ArrayList<String>();
		sent1.add("1"); sent1.add("1");sent1.add("6"); sent1.add("4");
		
		List<String> sent2 = new ArrayList<String>();
		sent2.add("2"); sent2.add("5");sent2.add("6"); 
		
		List<String> sent3 = new ArrayList<String>();
		sent3.add("2"); sent3.add("5");sent3.add("6"); sent3.add("6"); sent3.add("6"); 
		
		sentences.add(sent0);sentences.add(sent1);sentences.add(sent2);sentences.add(sent3);
		sentPos.put(0, 1);sentPos.put(1, 2);sentPos.put(2, 3);sentPos.put(3, 4);
		
		List<Integer> class0 = new ArrayList<Integer>();
		class0.add(0);class0.add(1);
		
		List<Integer> class1 = new ArrayList<Integer>();
		class1.add(1);class1.add(2);
		
		List<Integer> class2 = new ArrayList<Integer>();
		class2.add(0);class2.add(2);class2.add(3);
		
		classes.put(0, class0);classes.put(1, class1);classes.put(2, class2);

		
		Pos pc = new Pos();
		
		List<Object> trainparam = new ArrayList<Object>();
		trainparam.add(classes);
		trainparam.add(sentPos);
		
		pc.train(trainparam);
		
		List<Object> scoreparam = new ArrayList<Object>();
		scoreparam.add(4);
		
		double s = pc.score(2, scoreparam);
		
		System.out.println("score=" + s);
	}


}
