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

import as.Tools.Tools;

/*
 * This feature is used to score each sentence using its position in the text.
 * score(s_i)= 1/i
 * 
 */
public class Pos1 implements Feature {

	List<List<String>> sentences;
	HashMap<Integer, List<Integer>> classes;
	
	@Override
	public void train(HashMap<Integer, List<Integer>> classes, List<List<String>> sentences) {
		this.sentences = sentences;
		this.classes = classes;
	}

	/**
	 * Score a sentence using the equation:
	 * score(s_i in C_j / pos) = cos_max (s_i,s)*score(s); s in C_j
	 */
	@Override
	public Double score(int classID, Object entry) {
		Double score = 0.0;
		int simSentPos = 1;
		
		@SuppressWarnings("unchecked")
		List<String> sentence = (List<String>) entry;
		
		Double simMax = 0.0;
		for(int sentID: classes.get(classID))
		{
			Double sim = Tools.calcSimilarity(sentence, sentences.get(sentID));
			if (sim> simMax)
			{
				simMax = sim;
				simSentPos = sentID + 1; //the index starts from 0
			}
		}
		
		//System.out.println("simMax:" + simMax + ", simSentPos:" + simSentPos);
		
		score = simMax/simSentPos;
		return score;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
