/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013-2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import kariminf.as.ui.gui.GUI;
import kariminf.ktoolja.file.FileManager;


public class AS {

	public static void main(String[] args) {
		//TODO add other options
		if (args==null || args.length < 4){
			startGUI();
			return;
		}
			
		
		int i= 0;
		
		String inputDocURL = "";
		String outputDocURL = "";
		
		//If two are specified, we prefer the first one
		int sentNbr = 0;
		/*
		int wordsNbr = 0;
		int charsNbr = 0;
		*/
		
		double th = 0.02;
		
		while (i < args.length){
			if (args[i].charAt(0) != '-'){
				System.out.println("Wrong parameters structure:");
				help();
				return;
			}
			
			if (args[i].length() != 2){
				System.out.println("No such option:" + args[i]);
				help();
				return;
			}
			
			char option = args[i].charAt(1);
			
			switch(option){
			
			case 'i': 
				i++;
				if(i > args.length){
					System.out.println("You must specify input document:");
					help();
					return;
				}
				inputDocURL = args[i];
				break;
				
			case 'o':
				i++;
				if(i > args.length){
					System.out.println("You must specify output document:");
					help();
					return;
				}
				outputDocURL = args[i];
				break;
				
			case 's':
				i++;
				if(i > args.length){
					System.out.println("You must specify number of lines:");
					help();
					return;
				}
				
				try {
					sentNbr = Integer.parseInt(args[i]);
				}
				catch (NumberFormatException e){
					System.out.println("You have to enter a number");
					help();
					return;
				}
				break;
				
			case 't':
				i++;
				if(i > args.length){
					System.out.println("You must specify the threshold: real between 0 and 1");
					help();
					return;
				}
				
				try {
					th = Double.parseDouble(args[i]);
				}
				catch (NumberFormatException e){
					System.out.println("You have to enter a real number between 0 and 1");
					help();
					return;
				}
				
				if (th < 0 || th > 1)
					th = 0.02;
				break;
			
			/*
			case 'w':
				i++;
				if(i > args.length){
					System.out.println("You must specify number of words in summary");
					help();
					return;
				}
				
				try {
					wordsNbr = Integer.parseInt(args[i]);
				}
				catch (NumberFormatException e){
					System.out.println("You have to enter a number");
					help();
					return;
				}
				break;
				
			case 'c':
				i++;
				if(i > args.length){
					System.out.println("You must specify number of words in summary");
					help();
					return;
				}
				
				try {
					charsNbr = Integer.parseInt(args[i]);
				}
				catch (NumberFormatException e){
					System.out.println("You have to enter a number");
					help();
					return;
				}
				break;
			*/
			default:
				System.out.println("No such option: " + args[i]);
				help();
				return;
			}
			
			
			i++;
		}
		
		MonoDoc mono = new MonoDoc();
		mono.setThreshold(th);
		File f = new File(inputDocURL);
		
		if (! f.exists()){
			System.out.println("The file that you've choosen doesn't exist");
			return;
		}
		
		String text = FileManager.readFile(f);
		mono.Summarize(text);
		String result = mono.getSummaryNum(sentNbr);
		
		try {
			FileManager.saveFile(outputDocURL, result);
		} catch (IOException e) {
			System.out.println("Couldn't write the result in your specified destination");
			return;
		}
		
		System.out.println("Your summary has been generated.");
		
	}
	
	
	
	public static void help(){
		String help = "AllSummarizer usage:\n";
		help += "Jar file: java -jar <jar_name> options\n";
		help += "Class: java kariminf.as.ui.MonoDoc options\n";
		help += "\nOptions: -i <input_file> -o <output_file> -s <number_of_sent> -t <threshold>";
		
		System.out.println(help);
	}
	
	public static void startGUI(){
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					System.out.println(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
	
		new GUI();
	}

}

/*
My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree.
My research in ESI is about ATS, it is the intersection between IR and NLP.
In This research, the main idea is to find relevant sentences using IR technics.
The statistical features are the power of IR to find relevance.
AI technics are used, such as learning algorithms to create models for each topic in the input text.
	   
 */
