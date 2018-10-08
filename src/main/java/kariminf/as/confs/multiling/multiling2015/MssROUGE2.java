/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

package kariminf.as.confs.multiling.multiling2015;

import java.io.File;
import java.io.IOException;

import kariminf.ktoolja.file.FileManager;


public class MssROUGE2 {

	private static final String peerFolder = 
			"/home/kariminf/Data/ATS/Mss15Test/tests/baselines/random/";

	private static final String summaryFolder =
			"/home/kariminf/Data/ATS/Mss15Test/model";
	
	private static final String [] langs = 
		{
		"af", "ar", "bg", "ca", "cs", "de", "el", "en", "eo", 
		"es", "eu", "fa", "fi", "fr", "he", "hr", "hu", "id", 
		"it", 
		"ja", "ms", "nl", "no", "pt", "ro", "ru", "sh", 
		"sl", "sr", "sv", "th", "tr", "vi", "zh",
		"ka", "ko", "pl", "sk"
		};

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (String lang: langs){//langs
		    
			File folder = new File(summaryFolder + "/" + lang + "");
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;
			
		    
		    String xmlcontent = "<ROUGE-EVAL version=\"1.0\">\n";

			File[] files = folder.listFiles();
			for (File file: files){

				String fileName = file.getName();
				if (! fileName.endsWith(".txt")) continue;

				fileName = fileName.substring(0, fileName.length()-4);//4//12
				//String newfolderName = outFolder + lang + "/";
				//newfolderName += fileName.substring(0, fileName.length()-9) + "/";
				
				//int pid = 0;
				xmlcontent += "<EVAL ID=\""+fileName+"\">\n";
				
				xmlcontent += "<PEER-ROOT>\n";
				xmlcontent += peerFolder + lang + "\n";
				xmlcontent += "</PEER-ROOT>\n";
				xmlcontent += "<MODEL-ROOT>\n";
				xmlcontent += summaryFolder + "/" + lang + "\n";
				xmlcontent += "</MODEL-ROOT>\n";
				xmlcontent += "<INPUT-FORMAT TYPE=\"SPL\">\n";
				xmlcontent += "</INPUT-FORMAT>\n";
				xmlcontent += "<PEERS>\n";
				xmlcontent += "<P ID=\"Random\">" + fileName + ".txt</P>\n";
				xmlcontent += "</PEERS>\n";
				xmlcontent += "<MODELS>\n";
				xmlcontent += "<M ID=\"M"+fileName+"\">" + fileName + ".txt</M>\n";
				xmlcontent += "</MODELS>\n";
				xmlcontent += "</EVAL>\n";

			} //files
			xmlcontent += "</ROUGE-EVAL>";
			
			try {

				FileManager.saveFile(peerFolder + "/" + lang + "-random2018.xml", xmlcontent);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}//languages


	}


}
