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

package aak.as.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	
	public HashMap<String, Integer> getWordsFreq (List<String> sentences){
		HashMap<String, Integer> wordsFreq = new HashMap<String, Integer>();
		
		for(String sentence: sentences){
			for (String word : segmentWords(sentence)){
				//if (isNotStopWord(word)){
					int value = (wordsFreq.containsKey(word))?wordsFreq.get(word)+1:1;
					wordsFreq.put(word, value);
				//}
			}
		}
		
		return wordsFreq;
	} 
	

		

	


/*
		public static List<String> normalization (List<String> sentences){
		  		  
		  for(int i=0; i< sentences.size();i++){
			  String sentence = sentences.get(i);
			  
		  }
		  
		  return sentences;
	  }
	 */

	 /*public static Boolean isNotStopWord(String word) {
		    
		  if (!stopList.contains(word) && !word.matches(".*\\W+.*")) //
		    	  return true;
		      
		    return false;
	  }*/
	
	public static List<String> readFile(File f) {
	    try {
	      StringBuilder contents = new StringBuilder();

	      BufferedReader input = new BufferedReader(new FileReader(f));
	      for(String line = input.readLine(); line != null; line = input.readLine()) {
	        contents.append(line);
	        contents.append(", ");
	      }
	      input.close();

	      return segmentWords(contents.toString());
	      
	    } catch(IOException e) {
	      e.printStackTrace();
	      System.exit(1);
	      return null;
	    } 
	  }
	
	public static List<String> segmentWords(String s) {
	    List<String> ret = new ArrayList<String>();
	    for(String word:  s.split("[\\.,;:\"\']?\\s+|\\.$")) {
	      if(word.length() > 0) {
	        ret.add(word);
	      }
	    }
	    return ret;
	  }
}
