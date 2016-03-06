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

package kariminf.as.preProcess.lang;

import java.util.List;

/**
 * Word steming: delete the prefixes, infixes and suffixes.
 * 
 * @author Abdelkrime aries
 *
 */
public interface Stemmer {

	/**
	 * Stems a list of words
	 * 
	 * @param listWords a list of input words
	 * @return a list of stemmed words
	 */
	public List<String> stemListWords(List<String> listWords);
	
}
