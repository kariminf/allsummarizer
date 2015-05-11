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

package dz.aak.as.confs.multiling;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dz.aak.as.tools.Calculus;
import dz.aak.as.tools.FileManager;

public class MssROUGE {


	private static final String peerFolder = 
			"/home/kariminf/Data/ATS/multilingMss2015Training/training2015/";

	private static final String summaryFolder =
			"/home/kariminf/Data/ATS/multilingMss2015Training/summary/";

	private static final String [] langs = 
		{"ru"};

	private static String [] features = {
		"TFU",
		"TFB",
		"Pos",
		"RLeng",
		"PLeng",
	};

	//add static thresholds let say 31 from 0.00 to 0.30
	private static String[] th_name = 
		{"mean", "median", "variance", "Hmode", "Lmode", 
		"s_Dn", "D_sn", "D_s",
		"00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
		"10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
		"20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"
		};

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<List<Integer>> comb = new ArrayList<List<Integer>>();
		for (int combNbr=1; combNbr <= features.length; combNbr++)
			comb.addAll(Calculus.getCombinations(features.length, combNbr));

		for (String lang: langs){//langs
		    
			File folder = new File(summaryFolder + lang);
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;
			
			String perlscript = "#!/usr/bin/perl -w \n";
		    perlscript += "$ROUGE=\"perl KROUGE.pl\";\n";
		    
		    String xmlcontent = "<ROUGE-EVAL version=\"1.0\">\n";

			File[] files = folder.listFiles();
			for (File file: files){

				String fileName = file.getName();
				if (! fileName.endsWith("_summary.txt")) continue;

				fileName = fileName.substring(0, fileName.length()-12);
				//String newfolderName = outFolder + lang + "/";
				//newfolderName += fileName.substring(0, fileName.length()-9) + "/";
				
				//int pid = 0;
				xmlcontent += "<EVAL ID=\""+fileName+"\">\n";
				
				xmlcontent += "<PEER-ROOT>\n";
				xmlcontent += peerFolder + lang + "/" + fileName + "\n";
				xmlcontent += "</PEER-ROOT>\n";
				xmlcontent += "<MODEL-ROOT>\n";
				xmlcontent += summaryFolder + lang + "\n";
				xmlcontent += "</MODEL-ROOT>\n";
				xmlcontent += "<INPUT-FORMAT TYPE=\"SPL\">\n";
				xmlcontent += "</INPUT-FORMAT>\n";
				xmlcontent += "<PEERS>\n";

				for(int th=0;th<th_name.length;th++){//threshold 40
					
					for (List<Integer> oneComb : comb){//features combinations

						String featused = "";
						for (int index: oneComb)
							featused += features[index]  + "-";
						
						featused = featused.substring(0, featused.length()-1);
						
						xmlcontent += "<P ID=\""+ th_name[th] + "#" + featused + "\">" + th_name[th] + "/" + featused + ".asz</P>\n";
						
						//pid++;
						
					} //features combinations
					

				} //thresholds
				
				xmlcontent += "</PEERS>\n";
				xmlcontent += "<MODELS>\n";
				xmlcontent += "<M ID=\"M"+fileName+"\">" + fileName + "_summary.txt</M>\n";
				xmlcontent += "</MODELS>\n";
				xmlcontent += "</EVAL>\n";

			} //files
			xmlcontent += "</ROUGE-EVAL>";
			
			perlscript += "$cmd=\"$ROUGE -e data ";
			perlscript += " -a -x -c 95 -r 1000 -n 2 -f A -p 0.5 -t 0 -d ";
			perlscript += peerFolder + "ROUGE/xml/" + lang + ".xml > " ;
			perlscript += peerFolder + "ROUGE/out/" + lang + ".out" + "\";\n";
			perlscript += "print $cmd,\"\\n\";\n";
			perlscript += "system($cmd);\n\n";
			
			try {

				FileManager.saveFile(peerFolder + "ROUGE/xml/" + lang + ".xml", xmlcontent);
				FileManager.saveFile(peerFolder + "ROUGE/script/" + lang + ".pl", perlscript);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}//languages


	}


}
