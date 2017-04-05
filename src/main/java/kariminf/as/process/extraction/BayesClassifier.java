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

package kariminf.as.process.extraction;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.preProcess.DynamicPreProcessor;
import kariminf.as.process.extraction.bayes.Feature;
import kariminf.as.tools.Data;



/**
 * This class is used to score each sentence in a text.
 * 
 * Given a list of sentences, a list of features and a list of topics, this class
 * is used to: 
 * <ul>
 * <li>Add scoring features.</li>
 * <li>Calculate the probability that a sentence belongs to or represents a topic.</li>
 * <li>Calculate the probability that a sentence represents all the topics.</li>
 * </ul>
 * 
 * @author Abdelkrime Aries
 *
 */
public class BayesClassifier {
	
	private List<Feature> features = new ArrayList<Feature>();
	private HashMap<Integer,Double> scores = new HashMap<Integer,Double>();
	
	private boolean normalized = false;
	
	/**
	 * Sets the list of features to be used to score a sentence
	 * 
	 * @param features list of features (See {@link Feature})
	 */
	public void setFeatures(List<Feature> features){
		for (Feature feature: features)
			this.features.add(feature);
	}
	
	
	/**
	 * Add a new feature to the list 
	 * @param feature the feature to be add
	 */
	public void addFeature(Feature feature){
		features.add(feature);
	}
	
	
	/**
	 * Sets whether normalizing the score or not. 
	 * Normalizing is to divide the score by the length of the sentence
	 * 
	 * @param normalized Normalize the score or not
	 */
	public void normalize(boolean normalized){
		this.normalized = normalized;
	}
	
	
	/**
	 * Scores the sentences and reorder them from the more important to the less one.
	 * 
	 * In order to use this method, the data container must contain preprocessed text. 
	 * To do this, we must call the preprocessor first (See {@link DynamicPreProcessor}).
	 * 
	 * @param data the data container
	 * @return a list of sentences' indexes ordered by importance
	 */
	public List<Integer> classify(Data data) {
		//HashMap<Integer, List<Integer>> classes, List<List<String>> sentences
		
		List<Integer> orderedSentences = new ArrayList<Integer>();
		List<Double> orderedScores = new ArrayList<Double>();
		
		for(int sentID = 0; sentID < data.getSentNumber(); sentID++){
			Double currentScore = scoreSentence(data, sentID);
			
			//System.out.println("Sentence:" + sentID + " - Score:" + currentScore);
			int j = 0;
			while (j < orderedScores.size() && currentScore <= orderedScores.get(j))
				j++;

			orderedSentences.add(j, sentID);
			orderedScores.add(j, currentScore);
			
		}
		
		return orderedSentences;
	}

	
	/**
	 * Calculates the score of a sentence in a class of topics
	 * 
	 * The score of a sentence s<sub>i</sub> in a class of topics c<sub>j</sub> 
	 * having a set of features F is the production of scores 
	 * of s<sub>i</sub> in c<sub>j</sub> knowing each feature.
	 * <p><code>
	 * P(s<sub>i</sub> &#8712; c<sub>j</sub> | F) = 
	 * &#8719;<sub>k</sub> P(s<sub>i</sub> &#8712; c<sub>j</sub> | f<sub>k</sub>)
	 * </code></p>
	 * 
	 * @param data the data container
	 * @param sentID sentence index (ID)
	 * @param classID index (ID) of the class (topic).
	 * @return the score of a sentence in a class with a set of features.
	 */
	private Double scoreSentInClass (Data data, int sentID, int classID){
		
		Double scoreSent = 0.0;
		
		for (Feature feature: features){
			String scoreParam = feature.getScoreParam();
			//Using log will protect from overflow
			scoreSent += Math.log(
					feature.score(classID, data.makeScoreParams(scoreParam, sentID)) + 1.0
					);
		}
		
		return scoreSent;
	}
	

	
	/**
	 * Scores a sentence
	 * 
	 * The score is the probability that a sentence s<sub>i</sub> represents 
	 * all the topics c<sub>j</sub> ∈ C knowing a set of features F.
	 * <p><code>
	 * P(s<sub>i</sub> &#8712; &#8898;<sub>j</sub> c<sub>j</sub> | F) = 
	 * &#8719;<sub>j</sub> P(s<sub>i</sub> &#8712; c<sub>j</sub> | F)
	 * </code></p>
	 * 
	 * @param data the data container
	 * @param sentID The index (ID) of the sentence to be scored
	 * @return the score of the sentence
	 */
	public Double scoreSentence(Data data, int sentID){
		
		Double score = 0.0;
		
		//Multiplication is addition in log space
		for (int classID = 0; classID < data.getClassesNumber(); classID++){
			Double scoreclass = scoreSentInClass(data, sentID, classID);
			score += scoreclass;
		}
		
		//If the normalization of the score is activated
		if (normalized){
			int sentLeng = (int) data.makeScoreParam("sentRLeng", sentID);
			return score - Math.log(sentLeng);
		}
		
		scores.put(sentID, score);
		
		return score;
	}

	
	/**
	 * Trains Bayes classifier on the data, using a set of features
	 * 
	 * @param data data container
	 */
	public void train(Data data)
	{
		for (Feature feature: features){
			String trainParam = feature.getTrainParam();
			feature.train(data.makeTrainParams(trainParam));
		}
	}
	
	public Double getScore(int sentID){
		if (! scores.containsKey(sentID)) return -1.0;
		
		return scores.get(sentID);
	}

}
