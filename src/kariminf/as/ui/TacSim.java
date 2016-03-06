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

package kariminf.as.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import kariminf.as.preProcess.PreProcessor;
import kariminf.as.tools.Calculus;
import kariminf.as.tools.Data;
import kariminf.as.tools.FileManager;


public class TacSim {

	private static String tacfolder = "tac11/src/";
	
	private static String [][] language = {
		{"Arabic", "ar"}
		, 
		{"Czech", "cz"}
		, 
		{"English", "en"}
		, 
		{"French", "fr"}
		};
	
	private static String readFile(File f) {
	    try {
	      String contents = "";
	      int index = 0;

	      BufferedReader input = new BufferedReader(new FileReader(f));
	      for(String line = input.readLine(); line != null; line = input.readLine()) {
	        if (line.trim().length() > 5)
	        	index++;
	        else
	        	continue;
	        
	        if (index > 2)
	        	contents += " " + line + "\n";
	      }
	      input.close();

	      return contents;
	      
	    } catch(IOException e) {
	      e.printStackTrace();
	      System.exit(1);
	      return null;
	    } 
	  }
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String result = "";
		
		for (int count =0; count < language.length; count++){	
			
			String lang =  language[count][0];
			String accro = language[count][1];
			
			System.out.println("language: " + lang);
			
			result += lang + "\n";

			Data data = new Data();
			PreProcessor pre = new PreProcessor(accro, data);

			result += ",,,with 0,,,,,,,, no 0\n";
			result += "topic,#sent,,mean,median1,median2,Hmode,Lmode,var,skewness,,";
			result += "mean,median1,median2,Hmode,Lmode,var,skewness\n";
			for (int i =0; i< 10; i++){ //10 topics
				System.out.println("topic: " + i);
				pre.clearText();
				for (int j=0; j< 10; j++)
					pre.addText(readFile(new File(tacfolder + "M00" + i + "" + j + "." + lang.toLowerCase())));
					
				pre.preProcess();
				
				result += "M00" + i + ","; //topic
				result += data.getSentences().size() + ",,";//#sent
				
				result += Calculus.mean(data.getSimList()) + ",";//mean
				result += Calculus.median(data.getSimList()) + ",";//median1
				{
					List<Double> newSim = 
							Calculus.addMultiple(data.getSimList(), 1.0, data.getSentences().size());
					result += Calculus.median(newSim) + ",";//median2
				}
				result += Calculus.modeHigh(data.getSimList()) + ","; //Hmode
				result += Calculus.modeLow(data.getSimList()) + ","; //Lmode
				result += Calculus.variance(data.getSimList()) + ",";//variance
				result += Calculus.skewness(data.getSimList()) + ",,";//skewness
				
				List<Double> simZ = Calculus.delMultiple(data.getSimList(), 0.0);
				if(simZ.size() < 1){
					result += "\n";
					continue;
				}
				result += Calculus.mean(simZ) + ",";//mean
				result += Calculus.median(simZ) + ",";//median1
				{
					List<Double> newSim = 
							Calculus.addMultiple(simZ, 1.0, data.getSentences().size());
					result += Calculus.median(newSim) + ",";//median2
				}
				result += Calculus.modeHigh(simZ) + ","; //Hmode
				result += Calculus.modeLow(simZ) + ","; //Lmode
				result += Calculus.variance(simZ) + ",";//variance
				result += Calculus.skewness(simZ) + "\n";//skewness
				
			} //topics
			
			result += "\n";

		}//language iteration
		
		try {
			FileManager.saveFile("tac2011.csv", result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//main

}
