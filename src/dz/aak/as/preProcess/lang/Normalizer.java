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

public interface Normalizer {
	/**
	 * 
	 * @param text
	 * @return
	 */
	public String normalize (String text);
	/**
	 * 
	 * @param text
	 * @param param the function we want to apply on the text \n
	 * For example: 
	 * @return
	 */
	public String normalize (String text, String param);
	/**
	 * 
	 * @return name of each parameter with its description
	 */
	public HashMap <String, String> getParameters();
}
