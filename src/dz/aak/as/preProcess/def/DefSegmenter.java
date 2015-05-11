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

package dz.aak.as.preProcess.def;

import java.util.ArrayList;
import java.util.List;

import dz.aak.as.preProcess.lang.Segmenter;

public class DefSegmenter implements Segmenter {

	@Override
	public List<String> splitToSentences(String text) {

		List<String> sentences = new ArrayList<String>();
		
		text = text.replaceAll("([\\.\\!\\?؟。]+)[\\s$]", "$1#&#");
		for(String sentence:  text.split("#&#")) 
			if(sentence.trim().length() > 0) 
				sentences.add(sentence.trim());
		
		//System.out.println("sent # = " + sentences.size());

		return sentences;
	}

	@Override
	public List<String> segmentWords(String text) {

		List<String> words = new ArrayList<String>();
	    for(String word:  text.split("[\\.,;؛،、:\"\']?\\s+|[\\.\\!\\?؟。]+$")) {
	      if(word.trim().length() > 0) {
	    	  words.add(word.toLowerCase().trim());
	      }
	    }
	    return words;
	    
	}

}
