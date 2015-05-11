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


public class ReOrderer {

	
	public static List<Integer> reorder(List<List<String>> sentWords){
		
		if (sentWords.size() > 200)
			return null;
	
		int size = sentWords.size();
		
		double matrix[][] = new double[size][size];
		
		for (int i=0; i<size; i++)
			for(int j=0; j< size; j++){
				double sim = 0.0;
				if (i!=j)
					sim = - Tools.calcSimilarity(sentWords.get(i), sentWords.get(j));
				matrix[i][j] = sim;
			}
		
		int firstNode = getFirst(matrix);

		return nearestNeighbour(matrix, firstNode);
		
	}
	
	public static List<Integer> nearestNeighbour(double[][] matrix, int startNode){
		
		int nodesSize = matrix.length;
		
		List<Integer> result = new ArrayList<Integer>();
		
		Set<Integer> remaining = new HashSet<Integer>();
		
		for (int i=0; i<nodesSize; i++)
			if(i!=startNode) remaining.add(i);
		
		int current = startNode;
		result.add(startNode);
		
		
		while (! remaining.isEmpty()){
			double min = Double.MAX_VALUE;
			int index = 0;
			
			for(int j: remaining)
				if (min > matrix[current][j]){
					min = matrix[current][j];
					index = j;
				}
			
			current = index;
			result.add(index);
			remaining.remove(index);
			
		}
		
		return result;
	}
	

	public static int getFirst(double matrix[][]){
		
		int nodesSize = matrix.length;
		
		if (nodesSize < 2)
			return 0;
		
		double min[] = new  double [nodesSize];
		int minIdx[] = new  int [nodesSize];
		
		for(int i=0; i<nodesSize; i++){
			min[i] = Double.MAX_VALUE;
			minIdx[i]= 0;
			for(int j=i+1;j<nodesSize; j++)
				if(min[i] > matrix[i][j]){
					min[i] = matrix[i][j];
					minIdx[i] = j;
				}	
		}
		
		double minVal = Double.MAX_VALUE;
		int firstIdx = 0;
		
		for (int i= 0; i< nodesSize; i++)
			if (minVal > min[i]){
				minVal = min[i];
				firstIdx = i;
			}
		
		int secondIdx = minIdx[firstIdx];
		
		double minFirst = Double.MAX_VALUE;
		double minSecond = Double.MAX_VALUE;
		
		for (int j=0; j< nodesSize; j++){
			if(j>firstIdx && minFirst>matrix[firstIdx][j])
				minFirst=matrix[firstIdx][j];
			
			if(j>secondIdx && minSecond>matrix[secondIdx][j])
				minSecond=matrix[secondIdx][j];
				
		}
		
		if (minSecond>minFirst)
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
