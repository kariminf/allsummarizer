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

package dz.aak.as.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {

	private List<String> sentences = new ArrayList<String>();
	private List<List<String>> sentWords;
	private HashMap<Integer, List<Integer>> classes;
	private HashMap<Integer, Integer> sentPos;
	private List<Double> sentSim;
	private List<Integer> nbrWords;
	
	private HashMap<String, Integer> trainType = new HashMap<String, Integer>();
	private HashMap<String, Integer> scoreType = new HashMap<String, Integer>();
		
	public Data(){
		trainType.put("classes", 0); //HashMap<Integer, List<Integer>>
		trainType.put("sentWords", 1); //List<List<String>> 
		trainType.put("sentPos", 2); //HashMap<Integer, Integer> 
		trainType.put("sentRLeng", 4); // List<Integer> 
		
		scoreType.put("sentWords", 1);// List<String>
		scoreType.put("sentPos", 2);// Integer
		scoreType.put("sentLeng", 3);// Integer
		scoreType.put("sentRLeng", 4);// Integer
		
	}
	
	
	public void setSentences(List<String> sentences){
		this.sentences = sentences;
	}
	
	public void setSentWords(List<List<String>> sentWords){
		this.sentWords = sentWords;
		calculateSimilarity();
	}
	
	
	public void setClasses(HashMap<Integer, List<Integer>> classes){
		this.classes = classes;
	}
	
	public void setNbrWords(List<Integer> nbrWords){
		this.nbrWords = nbrWords;
	}
	
	public void setSentPos (HashMap<Integer, Integer> sentPos){
		this.sentPos = sentPos;
	}
	
	
	public Object makeTrainParam(String type){
		
		if (! trainType.containsKey(type))
			return null;
		
		switch (trainType.get(type)){
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
	
	public Object makeScoreParam(String type, int sentID){
		
		if (! scoreType.containsKey(type))
			return null;
		
		switch (scoreType.get(type)){
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
	
	public List<Object> makeTrainParams(String trainParam){
		
		List<Object> result = new ArrayList<Object>();
		
		for (String param: trainParam.split(","))
			result.add(makeTrainParam(param));
		return result;
	}
	
	public List<Object> makeScoreParams(String scoreParam, int sentID){
		
		List<Object> result = new ArrayList<Object>();
		
		for (String param: scoreParam.split(","))
			result.add(makeScoreParam(param, sentID));
		return result;
	}
	
	
	public int getSentNumber(){
		return sentWords.size();
	}
	
	public int getClassesNumber(){
		return classes.size();
	}
	
	public List<String> getSentences (){
		return sentences;
	}
	
	public String getSentence (int index){
		return sentences.get(index);
	}
	
	public List<List<String>> getSentWords (){
		return sentWords;
	}
	
	public List<Integer> getNbrWords (){
		return nbrWords;
	}
	
	public HashMap<Integer, Integer> getRealPos (){
		return sentPos;
	}
	
	
	private void calculateSimilarity(){
		
		sentSim = new ArrayList<Double>();
		for(int i=0; i< sentWords.size(); i++)
			for(int j=i+1; j< sentWords.size(); j++){
				Double sim = Tools.calcSimilarity(sentWords.get(i), sentWords.get(j));
				sentSim.add(sim);
			}
				
				
	}
	
	public Double getSimilarity(int xPos, int yPos){
		
		if (xPos >= sentWords.size() || yPos >= sentWords.size()) return 0.0;
		if (xPos == yPos) return 1.0;
		if (xPos > yPos){
			int tmp = yPos;
			yPos = xPos;
			xPos = tmp;
		}
		
		// Suppose S is the number of sentences, x is the less position of a sentence
		// Sum from (S-x) to (S-1) = x * (2S - x - 1) / 2
		// This represents the base element for the xth line in the matrix
		int realPos = xPos * (2* sentWords.size()- xPos -1)/2;
		// We add the position of the column in the matrix
		realPos += (yPos - xPos - 1);
		
		return sentSim.get(realPos);
	}
	
	public List<Double> getSimList(){
		return sentSim;
	}

	public static void main(String[] args) {
		
		/*Data d = new Data();
		
		List<Object> l = d.makeTrainParam("classes,sentWords,classes,sentPos");
		
		for (Object o: l)
			System.out.println(o.getClass().toString());*/
	}

}
