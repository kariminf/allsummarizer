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
import java.util.List;

import kariminf.as.confs.multiling.MSS;
import kariminf.ktoolja.file.FileManager;


public class MssStats {

	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/Mss15Train/";

	private static final String outFolder =
			"/home/kariminf/Data/ATS/Mss15Train/tests/Th2015/";

	private static final String [] langs = 
		{"af", "ar"};
	//"af", "eo", "hr", "ms", "sh", "sl", "sr", "vi"


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		for (String lang: langs){//langs

			System.out.println("Language '" + lang + "' ...");
			
			String statBuffer  = "Doc,#sent,#termsD,#termsS,#HTF\n";
			
			MSS mss = new MSS(lang);
			//System.out.println(sizes);

			File folder = new File(mssFolder + "body/text/" +  lang + "/");
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;

			File[] files = folder.listFiles();
			for (File file: files){

				String fileName = file.getName();
				if (! fileName.endsWith("_body.txt")) continue;
					
				fileName = fileName.substring(0, fileName.length()-9);

				mss.preprocess(file);
				
				// ID of the document
				statBuffer += fileName + ",";
				
				// number of sentences in the document
				statBuffer += mss.sentNum() + ",";
				
				List<Integer> termsStat = mss.getTermsStats();
				
				// Number of different terms in the document
				statBuffer += termsStat.get(0) + ",";
				
				// Sum of different terms in each sentence
				statBuffer += termsStat.get(1) + ",";
				
				// Highest Term Frequency
				statBuffer += mss.getMaxFrequency() + "\n";
				
			}//Files

			try {

				FileManager.saveFile(outFolder + lang + "_stats.csv", statBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}//languages


	}

}
