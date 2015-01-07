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

package as.Process.Extraction;


import java.util.ArrayList;
import java.util.List;

import as.Process.Extraction.Bayes.Feature;
import as.Tools.Data;


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
