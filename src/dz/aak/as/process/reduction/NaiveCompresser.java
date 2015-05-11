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

package dz.aak.as.process.reduction;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NaiveCompresser implements Compresser{
	
	private final Pattern del1 = Pattern.compile("(.*), which [^,]*,(.*)");
	private final Pattern del2 = Pattern.compile("([^,]*), who [^,]*,(.*)");
	private final Pattern del3 = Pattern.compile("([^_]*)_[^_]*_(.*)");
	
	private Matcher m;
	
	public List<String> compressChoices(String inStr) {
		
		return null;
	}
	
	public String compress(String inStr) {
		
		String outStr = inStr;
		boolean matched = false;
		
		m = del1.matcher(outStr);
		if (m.find()) {
			outStr =  m.group(1) + m.group(2);
			matched=true;
        }
		
		m = del2.matcher(outStr);
		if (m.find()) {
			outStr =  m.group(1) + m.group(2);
			matched=true;
        }
		
		m = del3.matcher(outStr);
		if (m.find()) {
			outStr =  m.group(1) + m.group(2);
			matched=true;
        }
		
		if (matched){
			//replace all spaces more than one, with only one space
		outStr = outStr.replaceAll(" +", " ");
		
		return outStr;
		}
		
		return null;
	}
	
	public static void main(String args[]){
		NaiveCompresser n = new NaiveCompresser();
		
		String out = n.compress("I am going to the park _ near home _ to peak flowers.");
		System.out.println(out);
	}

}
