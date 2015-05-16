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

package dz.aak.as.process.extraction.bayes;

import java.util.List;

/**
 * The feature to be used to score a sentence in the text.
 * 
 * @author Abdelkrime Aries
 *
 */
public interface Feature {

	/**
	 * Gets the names of parameters separated by a comma.
	 * 
	 * @return training parameters' names
	 */
	public String getTrainParam();
	
	
	/**
	 * Trains the classifier using to this feature
	 * 
	 * @param trainParam a list of objects which are the parameters needed by this feature
	 * 		  for training.
	 */
	public void train(List<Object> trainParam);
	
	
	/**
	 * Gets the names of parameters separated by a comma
	 * 
	 * @return score parameters' names
	 */
	public String getScoreParam();
	
	
	/**
	 * Scores a sentence according to a topic. 
	 * 
	 * The sentence is scored using the score parameters, for example, the term frequency.
	 * 
	 * @param classID the ID of the class (topic) wich is used to score the sentence
	 * @param scoreParam a list of Objects which are the parameters needed
	 *        to score the sentence
	 * @return the score of the sentence in the class "classID" using this feature
	 */
	public Double score(int classID, List<Object> scoreParam);
	
}
