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

package aak.PreProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SWEliminator {

	private List<String> stopList = getStopList(new File("english.stop"));
	
	public Boolean isNotStopWord(String word) {
		word = word.toLowerCase().trim();
		if (!stopList.contains(word) && !word.matches(".*\\W+.*")) //
			return true;
		 
		return false;
	}
	
	private Boolean isStopWord(String word) {
	    word = word.toLowerCase().trim();
	    if (stopList.contains(word) || word.matches(".*\\W+.*")) //a non-word character
			return true;
		
		return false;
	}
	
	public void delSW(List<String> wordsList){
		for(int i=wordsList.size()-1; i >=0; i--)
			if (isStopWord(wordsList.get(i)))
				wordsList.remove(i);
	}
	
	private static List<String> getStopList(File f) {
		List<String> stopwords = new ArrayList<String>();
	    try {

	      BufferedReader input = new BufferedReader(new FileReader(f));
	      for(String line = input.readLine(); line != null; line = input.readLine()) {
	        stopwords.add(line);
	      }
	      input.close();

	      return stopwords;
	      
	    } catch(IOException e) {
	      e.printStackTrace();
	      System.exit(1);
	      return null;
	    } 
	  }
	
	
	public static void main(String[] args) {
		List<String> tstList = new ArrayList<String>();
		tstList.add("the");
		tstList.add("new");
		tstList.add("theory");
		tstList.add("said");
		tstList.add("to");
		tstList.add("be");
		tstList.add("or");
		tstList.add("not");
		tstList.add("the");
		tstList.add("problem");
		tstList.add("not");
		tstList.add("mine");
		
		SWEliminator eliminator = new SWEliminator();
		eliminator.delSW(tstList);
		
		System.out.println(tstList.toString());
		

	}

}
