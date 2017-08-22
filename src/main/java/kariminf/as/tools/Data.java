/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2015 Abdelkrime Aries <kariminfo0@gmail.com>
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
import java.util.List;

/**
 * Data container.
 * 
 * It is used to store data from preprocessing and clustering tasks.
 * 
 * @author Abdelkrime Aries
 * 
 */
public class Data {

	private List<String> sentences = new ArrayList<String>();
	private List<List<String>> sentWords = new ArrayList<>();
	private HashMap<Integer, List<Integer>> classes;
	
	//sentID ==> real position in a document
	private HashMap<Integer, Integer> sentPos = new HashMap<>();
	
	private List<Double> sentSim;
	
	private List<Integer> nbrWords;
	
	private int docAmount = 0;
	
	//sentID ==> docID
	private HashMap<Integer, Integer> sentInDoc = new HashMap<Integer, Integer>();
	//docID ==> length (number of sentences)
	private HashMap<Integer, Integer> docLen = new HashMap<Integer, Integer>();
	
	//statistics on docs (int)
	//"min" ==> {docID ==> minLengthSentID}
	//"max" ==> {docID ==> minLengthSentID}
	private HashMap<String, HashMap<Integer, Integer>> docStats = new HashMap<>();
	

	private HashMap<String, Integer> trainType = new HashMap<String, Integer>();
	private HashMap<String, Integer> scoreType = new HashMap<String, Integer>();
	
	private List<Integer> finalSentences = new ArrayList<>();

	/**
	 * Creates a new data container
	 */
	public Data() {
		trainType.put("classes", 0); // HashMap<Integer, List<Integer>>
		trainType.put("sentWords", 1); // List<List<String>>
		trainType.put("sentPos", 2); // HashMap<Integer, Integer>
		trainType.put("sentRLeng", 4); // List<Integer>

		scoreType.put("sentWords", 1);// List<String>
		scoreType.put("sentPos", 2);// Integer
		scoreType.put("sentLeng", 3);// Integer
		scoreType.put("sentRLeng", 4);// Integer
		
		
		docStats.put("min", new HashMap<>());
		docStats.put("max", new HashMap<>());

	}

	public void clear(){
		sentences = new ArrayList<String>();
		sentWords = null;
		classes = null;
		sentPos = null;
		sentSim = null;
		//nbrWords = null;
	}

	/**
	 * add the sentences of the container
	 * 
	 * @param sentences
	 *            the sentences we want to add
	 */
	public void addSentences(List<String> sentences) {
		int base = this.sentences.size();
		for (int i = 0; i < sentences.size(); i++){
			int ID = base + i;
			sentPos.put(ID, i+1);
			this.sentences.add(sentences.get(i));
			sentInDoc.put(ID, docAmount);
		}
		
		docLen.put(docAmount, sentences.size());
		
		docAmount++;
	}

	/**
	 * Sets the sentences' words and calculates the similarity between each two
	 * sentences
	 * 
	 * @param sentWords
	 *            a list of sentences where each sentence is a list of words
	 */
	public void setSentWords(List<List<String>> sentWords) {
		
		int currentDoc = -1;
		int minSize = Integer.MAX_VALUE;
		int maxSize = 0;
		for (int sentID = 0; sentID < sentWords.size(); sentID++){
			List<String> words = sentWords.get(sentID);
			this.sentWords.add(words);
			int doc = sentInDoc.get(sentID);
			
			if (currentDoc != doc ){
				minSize = Integer.MAX_VALUE;
				maxSize = 0;
				currentDoc = doc;
			}
			
			int size = words.size();
			
			if (minSize >  size){
				minSize = size;
				docStats.get("min").put(currentDoc, sentID);
			}
			
			if (maxSize <  size){
				maxSize = size;
				docStats.get("max").put(currentDoc, sentID);
			}
		}
		
		calculateSimilarity();
	}

	/**
	 * Sets the classes of topics and their sentences.
	 * 
	 * @param classes
	 *            each class and it's list of sentences
	 */
	public void setClasses(HashMap<Integer, List<Integer>> classes) {
		// This is unprotected way to add classes
		this.classes = classes;
	}

	/**
	 * Sets the real number of words in each sentence
	 * 
	 * @param nbrWords
	 *            the real number of words in each sentence
	 */
	public void setNbrWords(List<Integer> nbrWords) {
		// This is unprotected way
		this.nbrWords = nbrWords;
	}

	/**
	 * Returns an object containing a given training parameter.
	 * 
	 * @param type
	 *            the name of the training parameter
	 * @return the training parameter
	 */
	public Object makeTrainParam(String type) {

		if (!trainType.containsKey(type))
			return null;

		switch (trainType.get(type)) {
		case 0:
			return classes;
		case 1:
			return sentWords;
		case 2:
			return sentPos;
		case 4:
			return nbrWords;
		}

		return null;
	}

	/**
	 * Returns an object containing a given scoring parameter for a specific
	 * sentence.
	 * 
	 * @param type
	 *            the name of scoring parameter
	 * @param sentID
	 *            the ID of the sentence to be scored
	 * @return scoring parameter
	 */
	public Object makeScoreParam(String type, int sentID) {

		if (!scoreType.containsKey(type))
			return null;

		switch (scoreType.get(type)) {
		case 1:
			return sentWords.get(sentID);
		case 2:
			return sentPos.get(sentID);
		case 3:
			return sentWords.get(sentID).size();
		case 4:
			return nbrWords.get(sentID);
		}

		return null;
	}

	/**
	 * Returns a set of objects containing training parameters.
	 * 
	 * @param trainParam
	 *            the names of training parameters separated by a comma
	 * @return a set of training parameters
	 */
	public List<Object> makeTrainParams(String trainParam) {

		List<Object> result = new ArrayList<Object>();

		for (String param : trainParam.split(","))
			result.add(makeTrainParam(param));
		return result;
	}

	/**
	 * Returns a set of objects containing scoring parameter for a specific
	 * sentence.
	 * 
	 * @param scoreParam
	 *            the names of scoring parameters separated by a comma
	 * @param sentID
	 *            the ID of the sentence to be scored
	 * @return a set of scoring parameters
	 */
	public List<Object> makeScoreParams(String scoreParam, int sentID) {

		List<Object> result = new ArrayList<Object>();

		for (String param : scoreParam.split(","))
			result.add(makeScoreParam(param, sentID));
		return result;
	}

	/**
	 * Gets the number of sentences.
	 * 
	 * @return the number of sentences
	 */
	public int getSentNumber() {
		return sentWords.size();
	}

	/**
	 * Gets the number of classes (topics) in the text(s).
	 * 
	 * @return number of classes
	 */
	public int getClassesNumber() {
		return classes.size();
	}

	/**
	 * Gets the list of sentences.
	 * 
	 * @return list of sentences
	 */
	public List<String> getSentences() {
		// unprotected way
		return sentences;
	}

	/**
	 * Gets a specific sentence.
	 * 
	 * @param index
	 *            the index (ID) of the sentence
	 * @return the sentence
	 */
	public String getSentence(int index) {
		return sentences.get(index);
	}

	/**
	 * Gets the sentences' words.
	 * 
	 * @return a list of sentences where each sentence is a list of words
	 */
	public List<List<String>> getSentWords() {
		// unprotected way
		return sentWords;
	}

	/**
	 * Gets the real number of words in each sentence
	 * 
	 * @return a list of words' numbers in each sentence
	 */
	public int getNbrWords(int sentID) {
		return nbrWords.get(sentID);
	}
	
	
	public int getSentAmount(){
		return sentences.size();
	}

	/**
	 * Gets the real position of each sentence.
	 * 
	 * @return the sentence index (ID) and its position in the source text
	 */
	public int getRealPos(int sentID) {
		return sentPos.get(sentID);
	}

	/**
	 * Calculates similarities between sentences and store them
	 */
	private void calculateSimilarity() {

		sentSim = new ArrayList<Double>();
		for (int i = 0; i < sentWords.size(); i++){
			for (int j = i+1; j < sentWords.size(); j++) {
				Double sim = Tools.calcSimilarity(sentWords.get(i),
						sentWords.get(j));
				sentSim.add(sim);

			}
		}
	}


	/**
	 * Gets the similarity between two sentences.
	 * 
	 * @param xPos the ID (index) of the first sentence
	 * @param yPos the ID (index) of the second sentence
	 * @return the similarity
	 */
	public Double getSimilarity(int xPos, int yPos) {
		
		if (xPos == yPos)
			return 1.0;
		
		int realPos = getMatrixPos(xPos, yPos);
		
		if(realPos < 0) return 0.0;

		return sentSim.get(realPos);
	}
	
	public int getMatrixPos(int xPos, int yPos){
		
		if (xPos >= sentWords.size() || yPos >= sentWords.size())
			return -1;
		if (xPos > yPos) {
			int tmp = yPos;
			yPos = xPos;
			xPos = tmp;
		}

		//The matrix is transformed to a list, so we have to search for the position
		//of the similarity of these two sentences in this list

		// Suppose S is the number of sentences and x < y 
		//The starting point is the number of elements from (S-x) to (S-1)
		int realPos = xPos * (2 * sentWords.size() - xPos - 1) / 2;

		// We add the position of the column in the matrix
		realPos += (yPos - xPos - 1);
		
		return realPos;
	}


	/**
	 * Gets the list of similarities.
	 * 
	 * @return list of similarities
	 */
	public List<Double> getSimList() {
		//unprotected way
		return sentSim;
	}
	
	public int getDocLength(int docID){
		return docLen.get(docID);
	}
	
	public int getSentDoc(int sentID){
		return sentInDoc.get(sentID);
	}
	
	public boolean isSentInDoc(int sentID, int docID){
		
		int doc = sentInDoc.get(sentID);
		return (docID == doc);
	}
	
	public int getSentSize(int sentID){
		return sentWords.get(sentID).size();
	}
	
	public int getMaxLenSentID(int docID){
		return docStats.get("max").get(docID);
	}
	
	public int getMinLenSentID(int docID){
		return docStats.get("min").get(docID);
	}

}
