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

/**
 * @author kariminf
 * 
 * This feature is to score a sentence using its length, with the equation
 * 
 * score(s_i) 	= 0 					if L_i >= L_min
 * 				= (L_i - L_min)/ L_min	otherwise
 * 
 * L_min = AVG L in C
 */
public class Leng implements Feature {

	HashMap<Integer, Integer> minLeng = new HashMap<Integer, Integer>();
	
	@Override
	public void train(HashMap<Integer, List<Integer>> classes,
			List<List<String>> sentences) {
		
		for (int classID = 0; classID < classes.size(); classID++)
		{
			int minCLeng = 0;
			for (int sentenceID : classes.get(classID))
				minCLeng += sentences.get(sentenceID).size();
			
			minCLeng /= classes.get(classID).size();
			System.out.println("minCLeng = " + minCLeng);
			
			minLeng.put(classID, minCLeng);
		}

	}

	/**
	 * 
	 * @param classID : the ID of the class
	 * @param entry: List of String
	 */
	public Double score(int classID, Object entry) {
		
		@SuppressWarnings("unchecked")
		List<String> sentence = (List<String>) entry;
		
		int sentLen = sentence.size();
		int minSentLen = minLeng.get(classID);
		
		if (sentLen < minSentLen)
			return (double) ((minSentLen - sentLen)/ minSentLen);
		
		return 0.0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
