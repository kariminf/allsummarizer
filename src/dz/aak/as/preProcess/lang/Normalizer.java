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

import java.util.HashMap;

/**
 * This interface is used for text Normalization
 * 
 * To normalize a text is to replace or delete some characters, 
 * For example, in Arabic we delete diacritics (Tashkiil),
 * in French we replace diacritics (énervé --> enerve)
 *  
 * @author Abdelkrime Aries
 *
 */
public interface Normalizer {
	
	
	/**
	 *  Normalizes a text. 
	 *  
	 *  Here, all types of normalization are used for a given language.
	 *  
	 * @param text the text to be normalized
	 * @return a normalized text
	 */
	public String normalize (String text);
	
	
	/**
	 * Normalizes a text, given a parameter
	 * 
	 * Sometimes there are many types of normalization, and using this method
	 * one can choose which normalization function to use 
	 * 
	 * @param text the text to be normalized
	 * @param param the function we want to apply on the text
	 * @return a normalized text
	 */
	public String normalize (String text, String param);
	
	
	/**
	 * Gives the type (function) of normalization with its description.
	 * 
	 * @return name of each parameter with its description
	 */
	public HashMap <String, String> getParameters();
	
}
