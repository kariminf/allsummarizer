/*
 * AllSumarizer v2
 * This file is part of AllSummarizer project; an implementation of the method
 * described in this paper:
 * http://dx.doi.org/10.1117/12.2004001
 * 
 * Copyright (C) 2013  Abdelkrime Aries <kariminfo0@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package as.Process.Extraction.Bayes;

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
