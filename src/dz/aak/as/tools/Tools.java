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

package dz.aak.as.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


public class Tools {
	
	/*
	 * Tools
	 * 
	 * 
	 */
	//private static List<String> stopList = readFile(new File("english.stop"));
	
	/*
	 * This function help to calculate words frequencies in a sentence
	 * 
	 */
	public static boolean similar (List<String> sentence1, List<String> sentence2, Double threshold){
		
		//String stemSent1 = new PorterStemmer().stemText(sentence1);
		//String stemSent2 = new PorterStemmer().stemText(sentence2);
		
		HashMap<String, Integer> v1 = getSentWordsFreq (sentence1);
		HashMap<String, Integer> v2 = getSentWordsFreq (sentence2);
		
		//System.out.println(cosineSimilarity(v1, v2));
		if (cosineSimilarity(v1, v2) >= threshold) //  threshold
			return true;
		
		/*if (cosineSimilarity(v1, v2)> 0.71) // Theta > 45
			return true;*/
		
		return false;
	}
	
	public static double calcSimilarity (List<String> sentence1, List<String> sentence2){
		
		HashMap<String, Integer> v1 = getSentWordsFreq (sentence1);
		HashMap<String, Integer> v2 = getSentWordsFreq (sentence2);
		return cosineSimilarity(v1, v2);
	}
	
	public static double cosineSimilarity(HashMap<String, Integer> v1, HashMap<String, Integer> v2) {
        Set<String> v1ANDv2 = new HashSet<String>(v1.keySet());
		//Set<String> both = v1.keySet();
        v1ANDv2.retainAll(v2.keySet());
        int v1Xv2 = 0;
        int v1Xv1 = 0;
        int v2Xv2 = 0;
        
        for (String word : v1ANDv2) v1Xv2 += v1.get(word) * v2.get(word);
        for (String word : v1.keySet()) v1Xv1 += v1.get(word) * v1.get(word);
        for (String word : v2.keySet()) v2Xv2 += v2.get(word) * v2.get(word);
        
        if (v1Xv1 * v2Xv2 ==0)
        	return 0;
        
        return v1Xv2 / Math.sqrt(v1Xv1 * v2Xv2);
	}

	
	public static HashMap<String, Integer> getSentWordsFreq (List<String> sentence){
		HashMap<String, Integer> wordsFreq = new HashMap<String, Integer>();
		
		for (String stem : sentence){
				int value = (wordsFreq.containsKey(stem))?wordsFreq.get(stem)+1:1;
				wordsFreq.put(stem, value);
		}
		
		return wordsFreq;
	}
	
	
	public static double termsDistribution(List<List<String>> sentWords){

		if (sentWords == null || sentWords.size() < 1)
			return 0.0;
		
		int sentTermsSum = 0;
		Set<String> docTerms = new HashSet<String>();
		for (List<String> words: sentWords){
			Set<String> sentTerms = new HashSet<String>();
			sentTerms.addAll(words);
			sentTermsSum += sentTerms.size();
			docTerms.addAll(sentTerms);
		}
		
		return ((double) sentTermsSum/docTerms.size());
		
	}
	
	public static void main(String[] args){
		
		List<List<String>> doc = new ArrayList<List<String>>();
		
		List<String> sent1 = new  ArrayList<String>();
		sent1.add("A");sent1.add("A");sent1.add("B");sent1.add("C");
		
		List<String> sent2 = new  ArrayList<String>();
		sent2.add("A");sent2.add("D");sent2.add("E");
		
		List<String> sent3 = new  ArrayList<String>();
		sent3.add("E");sent3.add("F");
		
		doc.add(sent1);doc.add(sent2);doc.add(sent3);
		
		System.out.println("sim(1,2) = " + calcSimilarity(sent1, sent2));
		System.out.println("sim(1,3) = " + calcSimilarity(sent1, sent3));
		System.out.println("sim(2,3) = " + calcSimilarity(sent3, sent2));

		System.out.println("term distribution = " + termsDistribution(doc));
		
		
		
	}
	
}
