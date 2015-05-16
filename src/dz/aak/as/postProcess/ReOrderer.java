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

package dz.aak.as.postProcess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dz.aak.as.tools.Tools;


/**
 * Sentences reordering
 * 
 * After summarization, extracted sentence have to be reordered
 * 
 * @author Abdelkrime Aries
 *
 */
public class ReOrderer {

	/**
	 * Reorders sentences based on a certain relationship between them.
	 * 
	 * @param sentWords a list contains sentences, where a sentence is a list of words
	 * @return a list of indexes representing the order of these sentences
	 */
	public static List<Integer> reorder(List<List<String>> sentWords){

		if (sentWords.size() > 200)
			return null;

		//Begin: Creating the matrix of similarities
		int size = sentWords.size();

		double matrix[][] = new double[size][size];

		for (int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				double sim = 0.0;
				if (i != j)
					sim = - Tools.calcSimilarity(sentWords.get(i), sentWords.get(j));
				matrix[i][j] = sim;
			}
		//End: Creating the matrix of similarities
		
		//Calculating the first sentence
		int firstNode = getFirst(matrix);

		//Using nearest neighbors to find the order for other sentences
		return nearestNeighbour(matrix, firstNode);

	}
	

	/**
	 * Reorders a list of elements based on the nearest neighbor
	 * 
	 * This method uses a matrix of similarities to reorder the elements starting from 
	 * an index; It returns a list of reordered indexes
	 * 
	 * @param matrix a matrix of similarities between each two elements
	 * @param startNode start point (index)
	 * @return a list of indexes representing the order of these elements
	 */
	public static List<Integer> nearestNeighbour(double[][] matrix, int startNode){

		int nodesSize = matrix.length;

		List<Integer> result = new ArrayList<Integer>();

		//Begin: the remaining elements
		Set<Integer> remaining = new HashSet<Integer>();
		for (int i = 0; i < nodesSize; i++)
			if (i != startNode) remaining.add(i);
		//End: the remaining elements

		int current = startNode;
		result.add(startNode);


		while (! remaining.isEmpty()){
			double min = Double.MAX_VALUE;
			int index = 0;

			//Begin: min similarity between the current element and the remaining
			for (int j: remaining){
				if (min > matrix[current][j]){
					min = matrix[current][j];
					index = j;
				}
			}
			//End: min similarity between the current element and the remaining

			//The nearest neighbor will be the current element
			current = index;
			result.add(index);
			remaining.remove(index);

		}

		return result;
	}


	/**
	 * Search for the first element based on a similarity matrix.
	 * 
	 * @param matrix a matrix of similarities between each two elements
	 * @return the index of the element that should be the starting point
	 */
	public static int getFirst(double matrix[][]){

		int nodesSize = matrix.length;

		if (nodesSize < 2)
			return 0;

		double minVal = Double.MAX_VALUE;
		int firstIdx = 0;
		int secondIdx = 0;
		
		//Begin: Look for the least distance between all pairs of elements
		for (int i = 0; i < nodesSize; i++){
			for (int j = i + 1; j < nodesSize; j++){
				if (minVal > matrix[i][j]){
					minVal = matrix[i][j];
					firstIdx = i;
					secondIdx = j;
				}
			}
			
		}
		//End: Look for the least distance between all pairs of elements

		double minFirst = Double.MAX_VALUE;
		double minSecond = Double.MAX_VALUE;

		//Begin: Look for min distance of firstIdx and secondIdx with other elements
		for (int j = 0; j < nodesSize; j++){
			
			if (j == firstIdx || j == secondIdx) continue;
			
			if((j > firstIdx) && (minFirst > matrix[firstIdx][j]))
				minFirst = matrix[firstIdx][j];

			if((j > secondIdx) && (minSecond > matrix[secondIdx][j]))
				minSecond = matrix[secondIdx][j];
		}
		//Begin: Look for min distance of firstIdx and secondIdx with other elements

		//If the second element has a less distance with others than the first,
		//we take it as the first element
		if (minSecond > minFirst)
			firstIdx = secondIdx;

		return firstIdx;
	}

	
	/**
	 * @param args
	 */
	 public static void main(String[] args) {
		 List<List<String>> sentWords= new ArrayList<List<String>>();

		 List<String> words = new ArrayList<String>();	
		 words.add("a");words.add("b");words.add("c");//words.add("d");
		 sentWords.add(words);

		 words = new ArrayList<String>();	
		 words.add("c");words.add("g");words.add("e");
		 sentWords.add(words);

		 words = new ArrayList<String>();	
		 words.add("e");words.add("f");
		 sentWords.add(words);

		 words = new ArrayList<String>();	
		 words.add("a");words.add("e");words.add("g");
		 sentWords.add(words);

		 //System.out.println(ReOrderer.reorder(sentWords));

		 double mat[][]={
				 {0.0, -0.57, 0.0, -0.33, -0.2, -0.4},//0
				 {-0.57, 0.0, 0.0, 0.0, 0.0, 0.0},//1
				 {0.0, 0.0, 0.0, -0.4, 0.0, 0.0},//2
				 {-0.33, 0.0, -0.4, 0.0, 0.0, 0.0},//3
				 {-0.2, 0.0, 0.0, 0.0, 0.0, 0.0},//4
				 {-0.4, 0.0, 0.0, 0.0, 0.0, 0.0},//5
		 };

		 System.out.println(ReOrderer.nearestNeighbour(mat, 1));

		 System.out.println(ReOrderer.getFirst(mat));/**/

	 }


}
