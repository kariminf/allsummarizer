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

package kariminf.as.ui.eval;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class TrecFile {
	
	private String Abstract ="";
	private String Title ="";
	private String Body ="";
	private String BodyP ="";
	
	private final String erase = "<IMAGE TYPE=\"FIGURE\"\\/>|<REF/>|<CREF/>|<FOOTNOTE/>|<EQN/>|<ITEMIZE>|</ITEMIZE>|</ITEM>";
	private final String toline = "<ITEM>";
	//private final String comment = "<!--.*-->";
	
	
	public TrecFile(){
		
	}
	
	public boolean openXMLFile(String xmlFilePath){
		StringBuffer content = new StringBuffer();
		
		try {
		      BufferedReader in = new BufferedReader(new FileReader(xmlFilePath));
		      String line;
		      while ( (line = in.readLine()) != null) {
		        content.append(line);
		        //sb.append("/n"); to make it more platform independent (Log July 12, 2004)
		        content.append("&&&&&&&&&&&&");
		      }
		      in.close();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		      return false;
		    }
		
		setXMLContent(content.toString());
		    
		return true;
	}
	
	public void setXMLContent(String xmlContent){
		
		//------------------------------------------------------------------
	    //
	    //------------------------------------------------------------------
		
		xmlContent = xmlContent.replaceAll(erase, "");
		xmlContent = xmlContent.replaceAll(toline, "&&&&&&&&&&&&");
		xmlContent = xmlContent.replaceAll("-->", "空");
		xmlContent = xmlContent.replaceAll("<!--[^空]*空", "");
		//boolean loop = true;
		
		//System.out.print(xmlContent + "\n-------------------------\n");
		//------------------------------------------------------------------
	    //
	    //------------------------------------------------------------------
		Matcher match= Pattern.compile("<TITLE>(.*)</TITLE>").matcher(xmlContent);
	    if(match.find())
	    	Title = match.group(1);
	    //------------------------------------------------------------------
	    //
	    //------------------------------------------------------------------
	    match= Pattern.compile("<ABSTRACT>(.*)</ABSTRACT>").matcher(xmlContent);
	    if(match.find()){
	    	//Abstract = match2.group(1);
	    	String[] phrases = match.group(1).split("<P>|</P>");
	    	
	    	String abs = "";
	    	for(int i = 0;i<phrases.length; i++)
	    		abs += phrases[i]+ " ";
	    	
	    	abs = abs.replaceAll("[\\r\\n]|&&&&&&&&&&&&", " ");
	    	List<String> spltS = null ;//= Tools.SplitToSentences(abs);
	    	
	    	for(String sent: spltS)
	    		Abstract += sent + "\n";
	    	
	    	
	    }
	    
	    //------------------------------------------------------------------
	    //
	    //------------------------------------------------------------------
	    match= Pattern.compile("<BODY>(.*)</BODY>").matcher(xmlContent);
	    if(match.find()){
	    	//Abstract = match2.group(1);
	    	String[] phrases = match.group(1).split("<P>|<HEADER>");
	    	for (int i = 0; i < phrases.length; i++) {
	    	      if (phrases[i].indexOf("</P>") > 0) {
	    	         Body += phrases[i].substring(0,phrases[i].lastIndexOf("</P>"));//+System.getProperty("line.separator");
	    	         //BodyP += "<p>\n" + phrases[i].substring(0,phrases[i].lastIndexOf("</P>"))+"\n</p>\n";
	    	         BodyP += phrases[i].substring(0,phrases[i].lastIndexOf("</P>"))+"\n";
	    	       }/*else if (phrases[i].indexOf("</HEADER>") > 0) {
		    	         //Body += phrases[i].substring(0,phrases[i].lastIndexOf("</HEADER>"))+System.getProperty("line.separator");
		    	         BodyP += "<h1>\n" + phrases[i].substring(0,phrases[i].lastIndexOf("</HEADER>"))+"\n</h1>\n";
		    	   }*/
	    	    }
	    }
	    
	    //Abstract = Abstract.replaceAll("&&&&&&&&&&&&", " ");
	    Abstract = Abstract.replaceAll(" +", " ");
	    Body = Body.replaceAll("&&&&&&&&&&&&", " ");
	    BodyP = BodyP.replaceAll("&&&&&&&&&&&&", " ");
	}
	
	public String getAbstract(){
		return Abstract;
	}
	
	public String getTitle(){
		return Title;
	}
	
	public String getBody(){
		return Body;
	}
	
	
	public String getPBody(){
		return BodyP;
	}
	
	
	
	
	public static void main(String[] args) {
		//9404004.xml
		TrecFile f = new TrecFile();
		f.openXMLFile("9404004.xml");
		
		System.out.print("title=" + f.getTitle() + "\n");
		System.out.print("------------------------------------\n");
		System.out.print("abstract=" + f.getAbstract() + "\n");
		System.out.print("------------------------------------\n");
		System.out.print("Body=" + f.getBody() + "\n");
	}

}
