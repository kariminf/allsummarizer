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

import java.util.HashMap;
import java.util.List;

public class TFU implements Feature {

	private HashMap<Integer, HashMap<String, Integer>> classWordsFreq 
					= new HashMap<Integer, HashMap<String, Integer>>();
	

	@Override
	public String getTrainParam() {
		return "classes,sentWords";
	}

	@Override
	public void train(List<Object> trainParam) {
		@SuppressWarnings("unchecked")
		HashMap<Integer, List<Integer>> classes = 
				(HashMap<Integer, List<Integer>>) trainParam.get(0);
		@SuppressWarnings("unchecked")
		List<List<String>> sentWords = 
				(List<List<String>>) trainParam.get(1);
		
		for (int classID = 0; classID < classes.size(); classID++)
		{
			HashMap<String, Integer> classIWordsFreq = new HashMap<String, Integer>();
			for (int sentID : classes.get(classID))
				for (String word: sentWords.get(sentID))
				{
					int value = (classIWordsFreq.containsKey(word))?classIWordsFreq.get(word)+1:1;
					classIWordsFreq.put(word, value);
				}
			classWordsFreq.put(classID, classIWordsFreq);
		}
		
	}

	@Override
	public String getScoreParam() {
		return "sentWords";
	}

	@Override
	public Double score(int classID, List<Object> scoreParam) {
		Double score = 0.0;

		@SuppressWarnings("unchecked")
		List<String> sentence = (List<String>) scoreParam.get(0);
		
		for (String word: sentence)
			if (classWordsFreq.get(classID).containsKey(word))
				score += classWordsFreq.get(classID).get(word);
		
		return score;
	}
	
	/*
	public void train(HashMap<Integer, List<Integer>> classes, 
			List<List<String>> sentences, HashMap<Integer, Integer> sentPos){
	
		for (int classID = 0; classID < classes.size(); classID++)
		{
			HashMap<String, Integer> classIWordsFreq = new HashMap<String, Integer>();
			for (int sentenceID : classes.get(classID))
				for (String word: sentences.get(sentenceID))
				{
					int value = (classIWordsFreq.containsKey(word))?classIWordsFreq.get(word)+1:1;
					classIWordsFreq.put(word, value);
				}
			classWordsFreq.put(classID, classIWordsFreq);
		}

	}


	public Double score(int classID, Object entry) {
		
		Double score = 0.0;

		@SuppressWarnings("unchecked")
		List<String> sentence = (List<String>) entry;
		
		for (String word: sentence)
			if (classWordsFreq.get(classID).containsKey(word))
				score += classWordsFreq.get(classID).get(word);
		
		return score;
	}*/

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
