/*
 * AllSumarizer v2
 * This file is part of AllSummarizer project; an implementation of the method
 * described in this paper:
 * http://dx.doi.org/10.1117/12.2004001
 * 
 * Copyright (C) 2014  Abdelkrime Aries <kariminfo0@gmail.com>
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

package aak.as.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * 
 * @author kariminf
 * This class is used for file 
 *
 */
public class FileManager {

	
	public static boolean saveFile(String filePath, String content) throws IOException{
		Writer out = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
	    try {
	      out.write(content);
	    }
	    finally {
	      out.close();
	    }
	    
	    return true;
	}
	
	public static String readFile (File f) {
	    try {
	      String contents = "";

	      BufferedReader input = new BufferedReader(new FileReader(f));
	      
	      for(String line = input.readLine(); line != null; line = input.readLine()) {
	    	  contents += " " + line + System.lineSeparator();
	      }
	      input.close();

	      return contents;
	      
	    } catch(IOException e) {
	      e.printStackTrace();
	      System.exit(1);
	      return null;
	    } 
	  }
	
	public static boolean createFolder(String dirName){
		
		File dir = new File(dirName);

		if (dir.exists()){
			//System.out.println("The directory already exists");
			return false;
		}

		try{
			dir.mkdir();
		} catch(SecurityException se){
			//System.out.println("Can't create the directory!!");
			return false;
		}        
		 
		return true;
	}
	
	
	public static void main(String[] args) {
		createFolder("/home/kariminf/Data/ATS/cmplg/peer/stat_th/kkk");
	}
	
	
}
