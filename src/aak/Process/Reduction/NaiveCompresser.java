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

package aak.Process.Reduction;

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
