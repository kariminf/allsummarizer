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

package aak.UI.Eval;

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
