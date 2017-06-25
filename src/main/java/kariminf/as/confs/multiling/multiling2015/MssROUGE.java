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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kariminf.ktoolja.file.FileManager;


public class MssROUGE {


	private static final String peerFolder = 
			"/home/kariminf/Data/ATS/Mss15Test/tests";
	/*private static final String peerFolder = 
			"/home/kariminf/Data/ATS/Mss15Test/tests";*/

	private static final String summaryFolder =
			"/home/kariminf/Data/ATS/Mss15Test/model";
	/*private static final String summaryFolder =
			"/home/kariminf/Data/ATS/Mss15Test/model";*/
	
	public static final Map<String, String> langs = new HashMap<String, String>() {{
		put("af", "D_sn/TFU-TFB-Pos-RLeng-PLeng");
		put("ar", "D_s/TFB-Pos-PLeng");
		put("bg", "Lmode/TFU-PLeng");
		put("ca", "Hmode/TFB-Pos-PLeng");
		put("cs", "Hmode/TFU-TFB-Pos-PLeng");
		put("de", "Lmode/TFU-Pos-PLeng");
		put("el", "median/TFU-TFB-Pos-RLeng-PLeng");
		put("en", "median/TFU-Pos-RLeng-PLeng");
		put("eo", "D_sn/TFU-TFB-RLeng-PLeng");
		put("es", "s_Dn/TFB-PLeng");
		put("eu", "Lmode/TFU-PLeng");
		put("fa", "variance/TFB-Pos-PLeng");
		put("fi", "mean/TFU-TFB");
		put("fr", "median/TFB-Pos-RLeng");
		put("he", "D_s/TFB-PLeng");
		put("hr", "variance/TFU-TFB-RLeng");
		put("hu", "s_Dn/TFU-PLeng");
		put("id", "median/TFU-Pos-RLeng");
		put("it", "median/TFB-Pos-RLeng-PLeng");
		put("ja", "variance/TFU-PLeng");
		put("ka", "variance/TFU-TFB-Pos-RLeng-PLeng");
		put("ko", "s_Dn/TFU-Pos-RLeng-PLeng");
		put("ms", "mean/TFB-RLeng-PLeng");
		put("nl", "Hmode/TFU-TFB-PLeng");
		put("no", "Lmode/TFU-TFB-Pos-RLeng-PLeng");
		put("pl", "mean/TFU-TFB-Pos-RLeng-PLeng");
		put("pt", "Lmode/TFU-TFB-Pos-RLeng-PLeng");
		put("ro", "Hmode/TFB-RLeng-PLeng");
		put("ru", "median/TFB-RLeng");
		put("sh", "s_Dn/TFB-Pos-RLeng-PLeng");
		put("sk", "median/TFB-RLeng");
		put("sl", "D_sn/TFB-RLeng-PLeng");
		put("sr", "Lmode/TFB-Pos-RLeng");
		put("sv", "Lmode/TFB-Pos-RLeng-PLeng");
		put("th", "D_s/TFB-RLeng-PLeng");
		put("tr", "median/TFB-Pos-PLeng");
		put("vi", "D_s/TFB-Pos-RLeng-PLeng");
		put("zh", "Hmode/TFB-RLeng-PLeng");
		//put("en", "en");
	}};

	/*private static final String [] langs = 
		{"ar"};*/

	private static List<String> features = new ArrayList<>();
	
	private static String[] methods = {
			//"SSF-GC0", 
			"SSF-GC1",
			"SSF-GC2",
			"SSF-GC3",
			"SSF-GC4",
			"SSF-GC5"
	};
	
	private static String[] ths = {
			//"00", 
			//"hmode",
			"mean",
			//"median"
	};
	
	private static String[] exts = {
			"e0", 
			"e1",
			"e2",
			"e3",
			"e4",
			"e5"
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for(String method: methods)
			for (String th: ths)
				for (String ext: exts)
					features.add(method + "_" + th + "_" + ext);

		for (String lang: langs.keySet()){//langs
		    
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
				xmlcontent += peerFolder + "\n";
				xmlcontent += "</PEER-ROOT>\n";
				xmlcontent += "<MODEL-ROOT>\n";
				xmlcontent += summaryFolder + "/" + lang + "\n";
				xmlcontent += "</MODEL-ROOT>\n";
				xmlcontent += "<INPUT-FORMAT TYPE=\"SPL\">\n";
				xmlcontent += "</INPUT-FORMAT>\n";
				xmlcontent += "<PEERS>\n";
				
				/*

				for(int th=0;th<th_name.length;th++){//threshold 40
					
					for (List<Integer> oneComb : comb){//features combinations

						String featused = "";
						for (int index: oneComb)
							featused += features[index]  + "-";
						
						featused = featused.substring(0, featused.length()-1);
						
						xmlcontent += "<P ID=\"" + featused + "\">" + th_name[th] + "/" + featused + ".asz</P>\n";
						
						//pid++;
						
					} //features combinations
					

				} //thresholds
				
				*/
				
				/*
				xmlcontent += "<P ID=\"TCC\">training2015/" + lang + "/" + fileName;
				xmlcontent += "/" + langs.get(lang) + ".asz</P>\n";
				*/
				
				xmlcontent += "<P ID=\"TCC\">testing2015/" + lang + "/";
				xmlcontent += fileName + ".txt</P>\n";
				
				
				for (String feat: features){
					xmlcontent += "<P ID=\"" + feat + "\">testing2017/" + lang ;
					xmlcontent += "/" + fileName + "/" + feat + ".asz</P>\n";
				}
				
				xmlcontent += "</PEERS>\n";
				xmlcontent += "<MODELS>\n";
				//xmlcontent += "<M ID=\"M"+fileName+"\">" + fileName + "_summary.txt</M>\n";
				xmlcontent += "<M ID=\"M"+fileName+"\">" + fileName + ".txt</M>\n";
				xmlcontent += "</MODELS>\n";
				xmlcontent += "</EVAL>\n";

			} //files
			xmlcontent += "</ROUGE-EVAL>";
			
			try {

				FileManager.saveFile(peerFolder + "/" + lang + "-2017.xml", xmlcontent);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}//languages


	}


}
