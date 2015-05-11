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

package dz.aak.as.ui.eval;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class EvalFileMulti {

	/**
	 * @param args
	 */
	
	private final static String folderSRC="duc04/data/";//
	private final static String folderMSRC="duc04/models/";//
	private final static String folderPEER="AAK_SENTNODUP";//peer
	private final static String folderMODEL="MODEL";//model
	private final static String folder="duc04/";//trec/
	
	//private final static String[] pexts = {"aak_UB01NC","aak_UB03NC","aak_UB04NC","aak_UB10NC"};//AAK_CMPRS
	//private final static String[] pexts = {"aak_UB01N","aak_UB03N","aak_UB04N","aak_UB10N"};//AAK_NODUPNORM
	private final static String[] pexts = {"aak_U02s","aak_U04s","aak_U09s","aak_B04s","aak_UB01s","aak_UB02s","aak_UB03s","aak_UB04s","aak_UB05s","aak_UB06s","aak_UB07s","aak_UB08s","aak_UB09s","aak_UB10s"}; //AAK_SENTNODUP
	//private final static String[] pexts = {"aak_U01","aak_U02","aak_U03","aak_U04","aak_U05","aak_U06","aak_U07","aak_U08","aak_U09","aak_U10"}; //AAK_SENT
	//private final static String[] pexts = {"aak_U","aak_B","aak_UB"}; //AAK_DOC
	
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
	
	public static String openBFile(String filePath){
		StringBuffer content = new StringBuffer();
		
		try {
		      BufferedReader in = new BufferedReader(new FileReader(filePath));
		      String line;
		      while ( (line = in.readLine()) != null) {
		        content.append(line + " ");
		        //sb.append("/n"); to make it more platform independent (Log July 12, 2004)
		        //content.append("&&&&&&&&&&&&");
		      }
		      in.close();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		      return "";
		    }
		    
		return content.toString();
	}
	

	
	public static void main(String[] args) {
		
		String[] files = new File(folderSRC).list();
		String[] mfiles = new File(folderMSRC).list();
		
		String content ="<ROUGE-EVAL version=\"1.0\">\n";
		int id=1;
		
		for(String file: files)
			//if (file.matches(".*\\.SRC")){
		{		
				String name = file.substring(0, file.length()-1).toUpperCase();
				
				content += "<EVAL ID=\""+id+"\">\n";
				content += "<PEER-ROOT>\n";
				content += folderPEER + "\n";
				content += "</PEER-ROOT>\n";
				content += "<MODEL-ROOT>\n";
				content += folderMODEL + "\n";
				content += "</MODEL-ROOT>\n";
				content += "<INPUT-FORMAT TYPE=\"SPL\">\n";
				content += "</INPUT-FORMAT>\n";
				content += "<PEERS>\n";
				
				for(String ext: pexts)
					content += "<P ID=\""+ ext +"\">"+ name + "T." + ext +"</P>\n";
				
				content += "</PEERS>\n";
				content += "<MODELS>\n";
				
				
				int mdlnum = 1;
				for(String mfile: mfiles)
					if (mfile.startsWith(name)){
						content += "<M ID=\"M"+mdlnum+"\">" + mfile + "</M>\n";
						mdlnum++;
					}
				
				content += "</MODELS>\n";
				content += "</EVAL>\n";


				id++;
		}
		content += "</ROUGE-EVAL>";

		try {
			saveFile(folder+folderPEER+".xml", content);
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}

}
