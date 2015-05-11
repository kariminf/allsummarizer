/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013 Abdelkrime Aries <kariminfo0@gmail.com>
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

package dz.aak.as.process.extraction.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dz.aak.as.tools.Data;

public class NaiveCluster extends Cluster {

	private Double Threshold = - 1.0;
	
	public NaiveCluster(Double threshold, Data data) {
		super(data);
		Threshold = threshold;
	}

	@Override
	public void createClasses() {
		
		List<List<String>> sentWords = data.getSentWords();
		
		if (Threshold<0.0)
			Threshold = (double) (1/sentWords.size());

		HashMap<Integer, List<Integer>> tmpClasses = new  HashMap<Integer, List<Integer>>();
		
		//create for each sentence, the class of similar words
		for(int i=0; i< sentWords.size();i++){
			List<Integer> classI = new ArrayList<Integer>();
			for(int j=0; j<sentWords.size();j++){
				//System.out.print("cos("+i+","+j+")=");
				if (data.getSimilarity(i, j) >= Threshold){
					classI.add(j);
				}
			}
			
			tmpClasses.put(i, classI);
			//System.out.println(tmpClasses.get(i).toString());	
		}
		
		
		//delete the redundant classes
		int numMaxClass=tmpClasses.size();
		for (int i=0; i<numMaxClass; i++){
			
			if (tmpClasses.get(i).size() ==0){
				tmpClasses.remove(i);
				//System.out.print("class(" +i+") IS an ampty class\n");
				continue;
			}
			
			int j=0;
			while(j<numMaxClass){
				if (tmpClasses.containsKey(j) && i !=j){
					if (tmpClasses.get(j).containsAll(tmpClasses.get(i))){
						tmpClasses.remove(i);
						//System.out.print("class(" +i+") IN class("+j+")\n");	
						break;
					}
				}
				j++;
			}

		}
		
		//re-enumerate the obtained classes
		HashMap<Integer, List<Integer>> classes = new  HashMap<Integer, List<Integer>>();
		int j=0;
		for (int i=0; i<numMaxClass; i++){
			if (tmpClasses.containsKey(i)){
				classes.put(j, tmpClasses.get(i));
				j++;
			}
		}
		
		data.setClasses(classes);

	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
