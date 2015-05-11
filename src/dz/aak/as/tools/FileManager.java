/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2014-2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

package dz.aak.as.tools;

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
