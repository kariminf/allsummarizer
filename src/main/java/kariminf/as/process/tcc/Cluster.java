/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013 Abdelkrime Aries <kariminfo0@gmail.com>
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

package kariminf.as.process.tcc;

import kariminf.as.preProcess.DynamicPreProcessor;
import kariminf.as.tools.Data;

/**
 * This class is used for sentences' clustering. 
 * 
 * Given a set of sentences, we want to cluster identical ones into the same group.
 * 
 * @author Abdelkrime Aries
 *
 */
public abstract class Cluster {
	
	protected Data data;
	
	/**
	 * Constructor of the class. We have to afford preprocessed data 
	 * (See {@link DynamicPreProcessor}).
	 * 
	 * @param data data container
	 */
	public Cluster(Data data){
		this.data = data;
	}
	
	
	/**
	 * Creates the different groups of topics, and put the result into the data container
	 */
	public abstract void createClasses();

}
