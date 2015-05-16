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

package dz.aak.as.preProcess.lang;

import java.util.List;

/**
 * Sentence segmentation and word tokenization for a given language.
 * 
 * @author Abdelkrime Aries
 *
 */
public interface Segmenter {
	
	
	/**
	 * Segments a text in a given language to many sentences.
	 * 
	 * @param text input text
	 * @return a list of sentences
	 */
	public List<String> splitToSentences(String text);
	
	
	/**
	 * Tokenizes a text: split it to many words.
	 * 
	 * @param text input text (a sentence usually)
	 * @return a list of words
	 */
	public List<String> segmentWords(String text);
	
}
