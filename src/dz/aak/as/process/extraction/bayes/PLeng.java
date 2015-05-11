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

package dz.aak.as.process.extraction.bayes;

import java.util.ArrayList;
import java.util.List;

/**
 * @class Pleng: processed length (after stop-words' elimination)
 * @author Abdelkrime Aries
 * @use This feature is to score a sentence using its length after stop-words' elimination
 * 
 */

public class PLeng extends RLeng {
	
	public PLeng() {
		super();
	}
	
	public PLeng(int catNbr) {
		super(catNbr);
	}

	@Override
	public String getTrainParam() {
		return "classes,sentWords";
	}
	
	@Override
	public void train(List<Object> trainParam) {
		
		@SuppressWarnings("unchecked")
		List<List<String>> sentWords = 
				(List<List<String>>) trainParam.get(1);
		
		List<Integer>  nbrWords = new ArrayList<Integer>();
		
		for (List<String> sent : sentWords)
			nbrWords.add(sent.size());
		
		List<Object> trainParam2 = new ArrayList<Object>();
		trainParam2.add(trainParam.get(0));
		trainParam2.add(nbrWords);
		
		super.train(trainParam2);
		
	}
	
	@Override
	public String getScoreParam() {
		return "sentLeng";
	}

	
	public static void main(String[] args) {
		
	}

}
