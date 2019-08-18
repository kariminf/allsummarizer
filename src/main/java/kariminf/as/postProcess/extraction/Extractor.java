/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013-2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

package kariminf.as.postProcess.extraction;

import java.util.ArrayList;
import java.util.List;
import kariminf.as.postProcess.PostProcessor;
import kariminf.as.process.Scorer;



/**
 * Sentences reordering
 * 
 * After summarization, extracted sentence have to be reordered
 * 
 * @author Abdelkrime Aries
 *
 */
public abstract class Extractor extends PostProcessor {
	
	protected Scorer scorer;
	
	protected List<Integer> order = new ArrayList<>();
	
	protected Extractor(Scorer scorer){
		this.scorer = scorer;
	}
	
	@Override
	public void postProcess() {
		// TODO Auto-generated method stub
		
	}
	
	
	public List<Integer> getOrder() {
		return order;
	}
	
	public abstract void reOrder();


}
