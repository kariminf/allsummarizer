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

package dz.aak.as.process.extraction;


import java.util.ArrayList;
import java.util.List;

import dz.aak.as.process.extraction.bayes.Feature;
import dz.aak.as.tools.Data;


/*
 * @author Abdelkrime Aries
 * @use this class is used to score each sentence in the different topics
 */
public class BayesClassifier {
	
	private List<Feature> features = new ArrayList<Feature>();
	
	private boolean normalized = false;
	
	/**
	 * This function is used to set the features vector 
	 */
	public void setFeatures(List<Feature> features){
		this.features = features;
	}
	
	/**
	 * This function is used to add new features
	 */
	public void addFeature(Feature feature){
		features.add(feature);
	}
	
	public void normalize(boolean normalized){
		this.normalized = normalized;
	}
	
	/**
	 * This function is served to classify our sentences, 
	 * and order them from the more important to the less one
	 */
	public List<Integer> classify(Data data) {
		//HashMap<Integer, List<Integer>> classes, List<List<String>> sentences
		
		List<Integer> orderedSentences= new ArrayList<Integer>();
		List<Double> orderedScores= new ArrayList<Double>();
		
		for(int sentID=0; sentID < data.getSentNumber();sentID++){
			Double currentScore = scoreSentence(data, sentID);
			
			//System.out.println("Sentence:" + sentID + " - Score:" + currentScore);
			int j = 0;
			while (j< orderedScores.size() && currentScore <= orderedScores.get(j))
				j++;

			orderedSentences.add(j, sentID);
			orderedScores.add(j, currentScore);
			
		}
		
		return orderedSentences;
	}

	/*
	 * This function calculate the score of a sentence in a class
	 * Score(Si,Cj,F1...Fk) = PROD_k Score (Si,Cj,Fk)
	 * 
	 */
	private Double scoreSentInClass (Data data, int sentID, int classID){
		
		Double scoreSent = 0.0;
		
		for (Feature feature: features){
			String scoreParam = feature.getScoreParam();
			scoreSent += Math.log(feature.score(classID, data.makeScoreParams(scoreParam, sentID))+1.0);
		}
			

		return scoreSent;
	}
	

	/**
	 * This function calculate the apartness of a sentences to all classes
	 * So: P(Si -> C1^C2^....^Cn / D, features) == P(Si -> C1/D, features) * .......* P(Sn -> C1/D, features)
	 * with normalization on sentence length+1
	 */
	public Double scoreSentence(Data data, int sentID){
		
		Double score = 0.0;
		
		
		
		for (int classID = 0; classID < data.getClassesNumber(); classID++){
			Double scoreclass= scoreSentInClass(data, sentID, classID);
			score += scoreclass;
		}
		
		if (normalized){
			int sentLeng = (int) data.makeScoreParam("sentRLeng", sentID);
			return score - Math.log(sentLeng);
		}
			
		return score;// - Math.log(sentlength);
	}

	public void train(Data data)
	{
		for (Feature feature: features){
			String trainParam = feature.getTrainParam();
			feature.train(data.makeTrainParams(trainParam));
		}
	}

}
