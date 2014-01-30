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

import java.util.ArrayList;
import java.util.List;

import aak.Tools.PorterStemmer;

public class PreProcessor {
	
	private List<String> Sentences = new ArrayList<String>();
	private List<List<String>> SentWords = new ArrayList<List<String>>();
	
	public void preProcess(String inTxt){
		
		inTxt = Normalizer.deleteNewLine(inTxt);
		
		//inTxt = inTxt.toLowerCase();
		
		Sentences = Segmenter.SplitToSentences(inTxt);
		
		SentWords = PreProcess(Sentences);
	}
	
	public static List<List<String>> PreProcess (List<String> Sents){
		
		List<List<String>> sentWords = new ArrayList<List<String>>();
		
		SWEliminator eliminator = new SWEliminator();
		PorterStemmer ps = new PorterStemmer();
		
		for (String sent: Sents){
			List<String> segSent = Segmenter.segmentWords(sent);
			
			eliminator.delSW(segSent);
			
			segSent = ps.stemListWords(segSent);
			
			sentWords.add(segSent);
		}
		
		return sentWords;
	}
	
	public List<String> getSentences (){
		return Sentences;
	}
	
	public List<List<String>> getSentWords (){
		return SentWords;
	}
	
	public static void main(String[] args) {
		
		String input = "";
		input += "My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree. ";
		input += "My research in ESI is about ATS, it is the intersection between IR and NLP. ";
		input += "In this research, the main idea is to find relevant sentences using IR technics. ";
		input += "The statistical features are the power of IR to find relevancy. ";
		input += "AI technics are used, such as learning algorithms to create models for each topic in the input text. ";
		
		PreProcessor pp = new PreProcessor();
		
		pp.preProcess(input);
		
		System.out.println(pp.getSentWords().toString());
		
	}
	
}
