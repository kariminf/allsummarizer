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

import java.util.List;

import dz.aak.as.preProcess.lang.Stemmer;

/**
 * Default Stemmer; it returns the same list as input. 
 * This class is used as a fallback when we can't find a certain language's stemmer.
 * 
 * @author Abdelkrime Aries
 *
 */
public class DefStemmer implements Stemmer {

	@Override
	public List<String> stemListWords(List<String> listWords) {
		return listWords;
	}

}
