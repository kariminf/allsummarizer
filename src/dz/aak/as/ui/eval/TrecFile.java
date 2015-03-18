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

package aak.as.ui.eval;


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
