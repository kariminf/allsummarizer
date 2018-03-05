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

package kariminf.as.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Contains methods used by more than one class (shared methods)
 * 
 * @author Abdelkrime Aries
 *
 */
public class Tools {
	
	/**
	 * Decides whether two sentences are similar based on a similarity threshold.
	 * 
	 * @param sentence1 list of words in sentence 1
	 * @param sentence2 list of words in sentence 2
	 * @param threshold the threshold used in decision
	 * @return true if they are similar, false otherwise
	 */
	public static boolean similar (List<String> sentence1, List<String> sentence2, Double threshold){
		HashMap<String, Integer> v1 = getSentWordsFreq (sentence1);
		HashMap<String, Integer> v2 = getSentWordsFreq (sentence2);
		
		if (cosineSimilarity(v1, v2) >= threshold)
			return true;
		
		return false;
	}
	
	
	/**
	 * Calculates the similarity between two sentences.
	 * 
	 * @param sentence1 list of words in sentence 1
	 * @param sentence2 list of words in sentence 2
	 * @return similarity between 0 and 1
	 */
	public static double calcSimilarity (List<String> sentence1, List<String> sentence2){
		
		HashMap<String, Integer> v1 = getSentWordsFreq (sentence1);
		HashMap<String, Integer> v2 = getSentWordsFreq (sentence2);
		return cosineSimilarity(v1, v2);
	}
	
	
	/**
	 * Calculates the cosine similarity between two sentences.
	 * 
	 * @param v1 each word in sentence 1 with its frequency
	 * @param v2 each word in sentence 2 with its frequency
	 * @return cosine similarity
	 */
	public static double cosineSimilarity(HashMap<String, Integer> v1, HashMap<String, Integer> v2) {
        Set<String> v1ANDv2 = new HashSet<String>(v1.keySet());
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

	
	/**
	 * calculates the words' frequencies in each sentence
	 * 
	 * @param sentence list of words in a sentence
	 * @return each word and its frequency
	 */
	public static HashMap<String, Integer> getSentWordsFreq (List<String> sentence){
		HashMap<String, Integer> wordsFreq = new HashMap<String, Integer>();
		
		for (String stem : sentence){
				int value = (wordsFreq.containsKey(stem))?wordsFreq.get(stem)+1:1;
				wordsFreq.put(stem, value);
		}
		
		return wordsFreq;
	}
	
	/**
	 * calculates the words' frequencies in the document
	 * 
	 * @param list of words in each sentence of the documennt
	 * @return each word and its frequency
	 */
	public static HashMap<String, Integer> getDocWordsFreq(List<List<String>> sentences){
		
		ArrayList<String> allTerms = new ArrayList<String>();
		
		for (List<String> sentence : sentences){
			allTerms.addAll(sentence);
		}
		
		return getSentWordsFreq(allTerms);
	}
	
	public static List<Integer> getSent2DocWordsFreq(
			List<String> sentence,
			HashMap<String, Integer> docWordsFreq) {
		
		List<Integer> result = new ArrayList<>();
		
		for (String word: sentence) {
			Integer freq = (docWordsFreq.containsKey(word))?
					docWordsFreq.get(word): 0;
			result.add(freq);
		}
		
		return result;
		
	}
	
	/**
	 * Gives some statistics about the different  terms in a text.
	 * 
	 * @param sentWords list of sentence where a sentence is a list of words
	 * @return a list of integers: 
	 * <ul>
	 * <li>the first element (index 0) is the number of different terms 
	 * in the document </li>
	 * <li>the second element is the sum of numbers of different terms in each sentence</li>
	 * <li>the third element is the frequency of the most repeated term</li>
	 * </ul>
	 */
	public static List<Integer> termsStats(List<List<String>> sentWords){

		List<Integer> stats = new ArrayList<Integer>();
		
		if (sentWords == null || sentWords.size() < 1){
			stats.add(0);
			stats.add(0);
			return stats;
		}
		
		int sentTermsSum = 0;

		Set<String> docTerms = new HashSet<String>();
		
		for (List<String> words: sentWords){
			Set<String> sentTerms = new HashSet<String>();
			sentTerms.addAll(words);
			sentTermsSum += sentTerms.size();
			docTerms.addAll(sentTerms);
		}
		
		stats.add(docTerms.size());
		stats.add(sentTermsSum);
		return stats;
	}
	
	/**
	 * Calculates the terms distribution in a text.
	 * 
	 * The terms distribution in a document D is the sum of the numbers of different 
	 * terms in each sentence divided by the number of different terms in D
	 * <p><code>
	 * TD = (&#8721;<sub>i</sub> |s<sub>i</sub>|)/|D|
	 * </code><br/>
	 * where:
	 * <ul>
	 * <li>|D| is number of different terms in the source document</li>
	 * <li>|s<sub>i</sub>| is number of different terms in the sentence s<sub>i</sub></li>
	 * </ul>
	 * </p>
	 * @param sentWords list of sentence where a sentence is a list of words
	 * @return terms' distribution
	 */
	public static double termsDistribution(List<List<String>> sentWords){

		if (sentWords == null || sentWords.size() < 1)
			return 0.0;
		
		List<Integer> stats = termsStats(sentWords);
		
		int sentTermsSum = stats.get(1);
		int docTerms = stats.get(0);
		
		return ((double) sentTermsSum/docTerms);
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
