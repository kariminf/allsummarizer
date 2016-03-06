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
 * Stop word eliminator. 
 * 
 * Stop words are the words having high redundancy in the text with no meaning, 
 * such as "to be", "the", etc.
 * 
 * @author Abdelkrime Aries
 *
 */
public interface SWEliminator {
	
	/**
	 * Deletes stop words from a list of words
	 * 
	 * @param wordsList the list of words which have to be filtered
	 */
	public void deleteSW (List<String> wordsList);
	
}
