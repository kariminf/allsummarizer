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

package kariminf.as.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.process.ScoreHandler.UnitNotIncluded;
import kariminf.as.tools.Data;


/**
 * This class is used to summarize a text or a set of texts.
 * 
 * @author Abdelkrime Aries
 *
 */
public class Scorer {

	private List<Integer> orderNumSent = new ArrayList<Integer>();
	private HashMap<Integer,Double> scores = new HashMap<Integer,Double>();
	private ScoreHandler scoreHandler;
	private Data data;
	
	
	protected Scorer(ScoreHandler scoreHandler){
		this.scoreHandler = scoreHandler;
	}
	
	
	public static Scorer create(ScoreHandler scoreHandler){
		if (scoreHandler == null) return null;
		
		return new Scorer(scoreHandler);
	}
	
	
	public boolean setData(Data data){
		if (data == null) return false;
		orderNumSent = new ArrayList<Integer>();
		scores = new HashMap<Integer,Double>();
		this.data = data;
		scoreHandler.setData(data);
		return true;
	}
	
	public Data getData(){
		return data;
	}
	
	
	public ScoreHandler getScoreHandler(){
		return scoreHandler;
	}

	
	public void scoreUnits(){
		if (data == null) return;
		List<Integer> orderedSentences = new ArrayList<Integer>();
		List<Double> orderedScores = new ArrayList<Double>();
		
		for(int unitID = 0; unitID < data.getSentNumber(); unitID++){
			Double currentScore;
			try {
				currentScore = scoreHandler.scoreUnit(unitID);
			} catch (UnitNotIncluded e) {
				continue;
			}
			
			int j = 0;
			while (j < orderedScores.size() && currentScore <= orderedScores.get(j))
				j++;

			orderedSentences.add(j, unitID);
			orderedScores.add(j, currentScore);
			
			scores.put(unitID, currentScore);
		}
		
		orderNumSent = orderedSentences;
		
	}
	
	/**
	 * Gets the first important sentences in the text.
	 * 
	 * @param nbrSent number of sentences to be extracted
	 * @return a list of first important sentences
	 */
	public List<Integer> getAmount(int amount) {

		if (amount < 1)
			return null;

		return orderNumSent.subList(0, amount);
	}


	/**
	 * Gets a percentage of the first important sentences
	 * 
	 * @param percent percentage to be extracted
	 * @return a list of first important sentences
	 */
	public List<Integer> getPercent(int percent) {

		if (percent < 1)
			return null;
		int nbrSent = (int) Math.ceil(percent * orderNumSent.size() / 100);

		if (nbrSent < 1)
			nbrSent = 1;

		return orderNumSent.subList(0, nbrSent - 1);
	}


	/**
	 * Gets the list of indexes of sentences ordered by their importance in the text.
	 * 
	 * @return a list of indexes of sentences
	 */
	public List<Integer> getOrdered(){

		return orderNumSent;
	}
	
	
	public Double getScore(int unitID){
		return scores.get(unitID);
	}

}
