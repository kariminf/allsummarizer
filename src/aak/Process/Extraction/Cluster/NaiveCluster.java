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

package aak.Process.Extraction.Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aak.Tools.Tools;

public class NaiveCluster implements Cluster {

	private Double Threshold = - 1.0;
	private HashMap<Integer, List<Integer>> classes = new  HashMap<Integer, List<Integer>>();
	
	public NaiveCluster(Double threshold) {
		Threshold = threshold;
	}

	@Override
	public void createClasses(List<List<String>> sentences) {
		if (Threshold<0.0)
			Threshold = (double) (1/sentences.size());

		HashMap<Integer, List<Integer>> tmpClasses = new  HashMap<Integer, List<Integer>>();
		
		//create for each sentence, the class of similar words
		for(int i=0; i< sentences.size();i++){
			List<Integer> classI = new ArrayList<Integer>();
			for(int j=0; j<sentences.size();j++){
				System.out.print("cos("+i+","+j+")=");
				if (Tools.similar(sentences.get(i), sentences.get(j), Threshold)){
					classI.add(j);
				}
			}
			tmpClasses.put(i, classI);
			System.out.println(tmpClasses.get(i).toString());	
		}
		
		
		//delete the redundant classes
		int numMaxClass=tmpClasses.size();
		for (int i=0; i<numMaxClass; i++){
			
			if (tmpClasses.get(i).size() ==0){
				tmpClasses.remove(i);
				System.out.print("class(" +i+") IS an ampty class\n");
				continue;
			}
			
			int j=0;
			while(j<numMaxClass){
				if (tmpClasses.containsKey(j) && i !=j){
					if (tmpClasses.get(j).containsAll(tmpClasses.get(i))){
						tmpClasses.remove(i);
						System.out.print("class(" +i+") IN class("+j+")\n");	
						break;
					}
				}
				j++;
			}

		}
		
		//re-enumerate the obtained classes
		int j=0;
		for (int i=0; i<numMaxClass; i++){
			if (tmpClasses.containsKey(i)){
				classes.put(j, tmpClasses.get(i));
				j++;
			}
		}

	}

	@Override
	public HashMap<Integer, List<Integer>> getClasses() {
		return classes;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
