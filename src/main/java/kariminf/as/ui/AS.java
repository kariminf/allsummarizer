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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import kariminf.as.confs.multiling.MMS;
import kariminf.as.preProcess.DynamicPreProcessor;
import kariminf.as.process.Scorer;
import kariminf.as.process.tcc.BayesScoreHandler;
import kariminf.as.process.tcc.Clusterer;
import kariminf.as.process.tcc.Feature;
import kariminf.as.process.tcc.NaiveCluster;
import kariminf.as.process.tcc.PLeng;
import kariminf.as.process.tcc.Pos;
import kariminf.as.process.tcc.RLeng;
import kariminf.as.process.tcc.TFB;
import kariminf.as.process.tcc.TFU;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;
import kariminf.as.ui.gui.GUI;
import kariminf.ktoolja.file.FileManager;


public class AS {
	
	private static String inputDocURL = "";
	private static String outputDocURL = "";
	private static String language = "";
	private static boolean singleDoc = true;
	private static String threshold = "";
	private static String features = "";
	private static char summaryUnit = 'b';
	private static String summarySize = "";
	private static boolean sizeFix = true;
	private static boolean variantInputs = false;
	
	
	public static void parseOptions(String[] args){
		int i= 0;
		
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
			
			//language
			case 'l': 
				i++;
				if(i > args.length){
					System.out.println("Error: You must specify the language (ISO 639-1)\n\n");
					help();
					return;
				}
				
				if(args[i].length() != 2){
					System.out.println("Error: Language code must be 2 characters (ISO 639-1)\n\n");
					help();
					return;
				}
				
				language = args[i];
				break;
				
			// source size: multi-document or single-document (by default)
			case 'm': 
				singleDoc = false;
				break;
			
			//Input/ output
			case 'i': 
				i++;
				if(i > args.length){
					System.out.println("Error: You must specify input document\n\n");
					help();
					return;
				}
				inputDocURL = args[i];
				break;
				
			case 'o':
				i++;
				if(i > args.length){
					System.out.println("Error: You must specify output document\n\n");
					help();
					return;
				}
				outputDocURL = args[i];
				break;
			case 'v': 
				variantInputs = true;
				break;
			
			//summary length 
			case 'n':
				i++;
				if(i > args.length){
					System.out.println("Error: You must specify output size\n\n");
					help();
					return;
				}
				
				summarySize = args[i];
				break;
				
			case 'r':
				i++;
				if(i > args.length){
					System.out.println("Error: You must specify output size ratio\n\n");
					help();
					return;
				}
				sizeFix = false;
				summarySize = args[i];
				break;
			
			//summary unit
			case 'b':
			case 'c':
			case 'w':
			case 's':
				summaryUnit = option;
				break;
				
			case 't':
				i++;
				if(i > args.length){
					System.out.println("Error: You must specify the threshold from 0 to 100\n\n");
					help();
					System.exit(0);
				}
				
				threshold = args[i];
				break;
				
			case 'f':
				i++;
				if(i > args.length){
					System.out.println("Error: You must specify the feature combination(s)\n\n");
					help();
					System.exit(0);
				}
				
				features = args[i];
				break;
			
			default:
				System.out.println("Error: No such option: " + args[i] + "\n\n");
				help();
			}
			
			i++;
		}
	}
	
	
	public static ArrayList<String> getOutputFiles(ArrayList<ArrayList<File>> input){
		ArrayList<String> result = new ArrayList<String>();
		
		if (! outputDocURL.endsWith("/"))
			outputDocURL = outputDocURL + "/";
		
		//If not variant, the input is just one
		if (! variantInputs){
			String outName = "";
			if (singleDoc)
				outName = outputDocURL + input.get(0).get(0).getName();
			else
				outName = outputDocURL + input.get(0).get(0).getParentFile().getName();
			
			result.add(outName);
			return result;
		}
		
		//if variant 
		for(ArrayList<File> files: input){
			if (files.isEmpty()){
				System.out.println("Error: check the folder if it has proper files");
				System.exit(0);
			}

			String outName = "";
			if (singleDoc)
				outName = outputDocURL + files.get(0).getName();
			else
				outName = outputDocURL + files.get(0).getParentFile().getName();
			result.add(outName);
		}
		
		return result;
	}
	
	public static ArrayList<ArrayList<File>> getFiles(){
		
		File inputFile = new File(inputDocURL);
		
		//The input document must exist
		if (! inputFile.exists()){
			System.out.print("Error: Input file doesn't exist");
			System.exit(0);
		}
		
		//The input must be a folder if multi-documents or variant inputs
		if ((variantInputs || !singleDoc) && inputFile.isFile()){
			System.out.print("Error: The input must be a folder if multi-documents or variant inputs");
			System.exit(0);
		}
		
		ArrayList<ArrayList<File>> folders = new ArrayList<ArrayList<File>>();
		//If there are many tests
		if (variantInputs){
			if (singleDoc){
				
				for (File file: inputFile.listFiles())
					if (file.isFile()){
						ArrayList<File> files = new ArrayList<File>();
						files.add(file);
						folders.add(files);
					}
				
			} else{
				for (File folder: inputFile.listFiles()){
					if (folder.isDirectory()){
						ArrayList<File> files = new ArrayList<File>();
						for (File file: folder.listFiles())
							if (file.isFile())
								files.add(file);
						folders.add(files);
					}
				}
			}
			
		} else {//If there is one test
			ArrayList<File> files = new ArrayList<File>();
			if (singleDoc)
				files.add(inputFile);
			else{
				for (File file: inputFile.listFiles())
					if (file.isFile())
						files.add(file);
			}
			folders.add(files);
		}
		
		return folders;
	}
	
	public static ArrayList<Double> getThresholds(){
		threshold = threshold.trim();
		Pattern p = Pattern.compile("(\\d+)\\-(\\d+)\\:(\\d+)");
		Matcher m = p.matcher(threshold);
		ArrayList<Double> result = new ArrayList<Double>();
		if (m.find()){
			
			try{
				
				
				double from = Integer.parseInt(m.group(1))/100.;
				double to = Integer.parseInt(m.group(2))/100.;
				double step = Integer.parseInt(m.group(3))/100.;
				//System.out.println("from: " + from + ", to: " + to + ", step: " + step);
				int nb = 1 + (int)((to - from)/step + 0.01);
				
				for (int i=0; i< nb; i++){
					result.add(Math.round(from * 100)/100.);
					from += step;
				}
				
				return result;
				
			} catch (NumberFormatException e){
				System.out.println("Numbers must be integers");
				System.exit(0);
				
			}
		}
		
		//If not (from-to:step), then it is value1,...
		String thStrs[] = threshold.split(";");
		for (int i=0; i< thStrs.length; i++){
			try{
				result.add(Integer.parseInt(thStrs[i])/100.);
			} catch (NumberFormatException e){
				System.out.println("One threshold is wrong: " + thStrs[i]);
				System.exit(0);
			}
		}
		
		
		return result;
	}
	
	
	public static ArrayList<Integer> getSizes(){
		summarySize = summarySize.trim();
		
		Pattern p = Pattern.compile("(\\d+)\\-(\\d+)\\:(\\d+)");
		Matcher m = p.matcher(summarySize);
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (m.find()){
			
			try{
				
				int from = Integer.parseInt(m.group(1));
				int to = Integer.parseInt(m.group(2));
				if(!sizeFix && to > 100)
					to = 100;
				int step = Integer.parseInt(m.group(3));
				int nb = 1 + (int)((to - from)/step + 0.01);
				
				for (int i=0; i< nb; i++){
					result.add(from);
					from += step;
				}
				
				return result;
				
			} catch (NumberFormatException e){
				System.out.println("Numbers must be integers");
				System.exit(0);
				
			}
		}
		
		//If not (from-to:step), then it is value1,...
		String sizeStrs[] = summarySize.split(";");
		for (int i=0; i < sizeStrs.length; i++){
			try{
				result.add(Integer.parseInt(sizeStrs[i]));
			} catch (NumberFormatException e){
				System.out.println("One size is wrong: " + sizeStrs[i]);
				System.exit(0);
			}
		}
		
		
		return result;
	}
	

	public static void main(String[] args) {
		
		if (args != null && args.length > 0 && args[0].equals("-h")){
			help();
		}
		if (args==null || args.length < 4){
			startGUI();
			return;
		}
			
		parseOptions(args);
		
		ArrayList<Double> ths = getThresholds();
		System.out.println("thresholds:" + ths);
		ArrayList<Integer> sizes = getSizes();
		System.out.println("sizes:" + sizes);
		ArrayList<ArrayList<File>> tests = getFiles();
		System.out.println("inputs:" + tests);
		ArrayList<String> outputs = getOutputFiles(tests);
		//System.out.println("outputs:" + outputs);
		ArrayList<ArrayList<Feature>> featuresSets = getFeatures();
		
		Data data = new Data();
		DynamicPreProcessor preprocessor = new DynamicPreProcessor(language);
		preprocessor.setData(data);
		
		//The output is always a folder
		FileManager.createFolder(new File(outputDocURL));
		
		int testCount = -1;
		System.out.println("outputs:");
		//Looping over different tests
		for(ArrayList<File> files: tests){
			testCount ++;
			
			//PreProcessing
			preprocessor.clearText();
			data.clear();
			for(File file: files){
				String text = FileManager.readFile(file);
				preprocessor.addText(text);
			}
			preprocessor.preProcess();
			
			//totalSize: we need it just when there is a perencentage
			int totalSize = 0;
			if (! sizeFix){
				String allText = "";
				for (String sent: data.getSentences())
					allText += " " + sent;
				switch (summaryUnit) {
				case 'b':
					try {
						//number of bytes
						totalSize = allText.getBytes("UTF-8").length;
					} catch (UnsupportedEncodingException e) {
						//if the encoding not supported, we take the number of chars
						totalSize = allText.length();
					}
					break;
				case 'c':
					totalSize = allText.length(); //number of chars
					break;
				case 'w':
					for (int id=0; id < data.getSentAmount(); id++)//number of words
						totalSize += data.getNbrWords(id);
					break;
				case 's':
					totalSize = data.getSentences().size(); //number of sentences
					break;
				default:
					break;
				}
				
			}
			
			
			//loop over different thresholds
			for(double th: ths){

				
				//Loop over feature combinations
				for(ArrayList<Feature> fs: featuresSets){
					
					BayesScoreHandler bc = new BayesScoreHandler(new NaiveCluster(th));
					//add the feature combination
					String featused = "";
					for (Feature f: fs){
						bc.addFeature(f);
						featused += f.getClass().getSimpleName() + "-";
					}
					
					Scorer scorer = Scorer.create(bc);
					
					featused = featused.substring(0, featused.length()-1);
						
					scorer.setData(data);
					scorer.scoreUnits();
					
					//loop over summary size
					for (int size: sizes){
						
						String result = getSummary(data, scorer.getOrdered(), th, size, totalSize);
						String filename = outputs.get(testCount);
						filename += "_" + th + "_" + featused + "_" + size ;
						filename += sizeFix? "": "%";
						filename += summaryUnit + ".txt";
						
						try {
							FileManager.saveFile(filename, result);
							System.out.println("Summary created: " + filename);
						} catch (IOException e) {
							System.out.println("Couldn't write the result in your specified destination");
							return;
						}
					}
					
				}
				
			}
			
		}

		
	}
	
	
	private static String getSummary(Data data, List<Integer> order, double simTH, int size, int totalSize){
		
		int sumSize = size;
		
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();
		
		
		//percentage
		if (! sizeFix){
			sumSize = (int) Math.round(size * totalSize / 100);
		}

		String summary = "";
		int currentSize = 0;
		int numOrder = 0;

		while(true){

			if (numOrder >=  order.size())
				break;

			int index = order.get(numOrder);

			if (numOrder > 0){
				List<String> prevWords = sentWords.get(order.get(numOrder-1));
				List<String> actWords = sentWords.get(index);
				if (Tools.similar(prevWords, actWords, simTH)){
					numOrder ++;
					if (numOrder < order.size())
						index = order.get(numOrder);
				}
			}
			
			//
			switch (summaryUnit) {
			case 'b':
				try {
					//number of bytes
					currentSize += sentences.get(index).getBytes("UTF-8").length;
				} catch (UnsupportedEncodingException e) {
					//if the encoding not supported, we take the number of chars
					currentSize += sentences.get(index).length();
				}
				break;
			case 'c':
				currentSize += sentences.get(index).length(); //number of chars
				break;
			case 'w':
				currentSize += data.getNbrWords(index); //number of words
				break;
			case 's':
				currentSize ++; //number of sentences
				break;
			default:
				break;
			}
			
			
			if (currentSize > sumSize)
				break;
			
			summary += sentences.get(index) + "\n";

			numOrder ++;
		}
		
		//if all sentences are larger than the size, no sentences is selected
		if(summary.length() < 1){
			//we take the most relevant sentence
			summary = sentences.get(order.get(0));
			//Then we truncate it
			//summary = sentences.get(order.get(0)).substring(0, sumSize);
		}
			

		return summary;
	}
	
	private static ArrayList<ArrayList<Feature>> getFeatures(){
		ArrayList<ArrayList<Feature>> featuresSets = new ArrayList<ArrayList<Feature>>();
		
		features = features.toLowerCase();
		
		for(String featNames: features.split("\\;")){
			ArrayList<Feature> featComb = new ArrayList<Feature>();
			
			if (featNames.contains("pleng"))
				featComb.add(new PLeng());
			
			if (featNames.contains("pos"))
				featComb.add(new Pos());
			
			if (featNames.contains("rleng"))
				featComb.add(new RLeng());
			
			if (featNames.contains("tfb"))
				featComb.add(new TFB());
			
			if (featNames.contains("tfu"))
				featComb.add(new TFU());
			
			featuresSets.add(featComb);
		}
		
		return featuresSets;
		
	}
	
	
	
	public static void help(){
		String help = "AllSummarizer usage:\n";
		help += "Jar file: java -jar <jar_name> options\n";
		help += "Class: java kariminf.as.ui.MonoDoc options\n";
		help += "\n\nOptions: \n";
		help += "=============\n";
		
		help += "\ninput/output options: \n";
		help += "----------------------\n";
		
		help += "-i <input_file>: it must be a file or a folder if it is multidocument or variant inputs\n";
		
		help += "-o <output_file>: it must be a file or a folder if it is multidocument ";
		help += "or there is multiple output lengths, feature combinations or thresholds\n";
		
		help += "-v: variant inputs; a folder that contains files or folders to be summarized.\n";
		
		help += "\nsummary options: \n";
		help += "-----------------\n";
		help += "sumary unit: \n";
		help += "-b: we use Bytes to specify the summary size.\n";
		help += "-c: we use characters to specify the summary size.\n";
		help += "-w: we use words to specify the summary size.\n";
		help += "-s: we use sentences to specify the summary size.\n";
		
		help += "\nsumary length: \n";
		help += "-n <number>: defines the number of units to be extracted.\n";
		help += "-r <ratio>: ratio from 1 to 100% defines the percentage of units to be extracted.\n";
		help += "you can specify more than one length, by separating the lengths with semicolons\n";
		help += "or you can use <min-max:step> to specify min and max length and the step \n";
		
		help += "\nsummarizer options: \n";
		help += "--------------------\n";
		help += "-f <features>: the features used to score the sentences.\n";
		help += "the features are separated by a comma; for example: tfu,pos \n";
		help += "for multiple combinations, we use semicolons; for example: tfu,pos;tfb,len\n";
		help += "\n-t <threshold>: a number from 0 to 100 to specify the threshold of clustering.\n";
		help += "for multiple thresholds, we use semicolons; for example: 5;50\n";
		help += "or you can use <min-max:step> to specify min and max threshold and the step \n";
		
		help += "\n\nTo get this help, use -h";
		
		System.out.println(help);
		System.exit(0);
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
