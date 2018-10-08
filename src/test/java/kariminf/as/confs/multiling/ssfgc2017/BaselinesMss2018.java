package kariminf.as.confs.multiling.ssfgc2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.preProcess.PreProcessor;
import kariminf.as.preProcess.StaticPreProcessor;
import kariminf.as.process.Scorer;
import kariminf.as.process.baseline.Firsts;
import kariminf.as.process.baseline.Random;
import kariminf.as.process.ssfgc.GC0ScoreHandler;
import kariminf.as.process.ssfgc.GC1ScoreHandler;
import kariminf.as.process.ssfgc.GC2ScoreHandler;
import kariminf.as.process.ssfgc.GC3ScoreHandler;
import kariminf.as.process.ssfgc.GC4ScoreHandler;
import kariminf.as.process.ssfgc.SSFScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;
import kariminf.ktoolja.file.FileManager;
import kariminf.ktoolja.math.Calculus;

public class BaselinesMss2018 {

	//private String lang = "en";

	private Data data;
	private PreProcessor preprocessor;
	private int summarySize;
	
	private Scorer scorer;

	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/Mss15Test/src/";

	private static final String outFolder =
			"/home/kariminf/Data/ATS/Mss15Test/tests/baselines/random/";

	private static final String [] langs = 
		{		"af", 
				"ar", 
				"bg", 
				"ca", 
				"cs", 
				"de", 
				"el", 
				"en", 
				"eo", 
				"es", 
				"eu", 
				"fa", 
				"fi", 
				"fr", 
				"he", 
				"hr", 
				"hu", 
				"id", 
				"it", 
				"ja", 
				"ka", 
				"ko", 
				"ms", 
				"nl", 
				"no", 
				"pl", 
				"pt", 
				"ro", 
				"ru", 
				"sh", 
				"sk", 
				"sl", 
				"sr", 
				"sv", 
				"th", 
				"tr", 
				"vi", 
				"zh"
				};


	public BaselinesMss2018 (String lang){
		//this.lang = (lang.length()==2)?lang:"en";
		data = new Data();
		preprocessor = new StaticPreProcessor(lang);
		preprocessor.setData(data);
	}
	
	
	public void setSummarySize(int summarySize){
		this.summarySize = summarySize;
	}
	
	
	public Data getData(){
		return data;
	}


	public void preprocess(File file){
		String text = readFile(file);
		preprocessor.addText(text);
		preprocessor.preProcess();
	}

	public List<Double> getSimilarity(){
		return data.getSimList();
	}

	public int sentNum (){
		return data.getSentNumber();
	}


	public void summarize(){
		
		Random ssh = new Random();
		
		ssh.setData(data);
		scorer = Scorer.create(ssh);
		scorer.setData(data);
		scorer.scoreUnits();
	}


	public String readFile(File file){
		StringBuffer content = new StringBuffer();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.startsWith("[") || line.length() < 1)
					continue;
				content.append(line + " ");
			}
			in.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return "";
		}

		return content.toString();
	}

	/*
	 * First most scored ones 
	 */
	private String getSummary0(){

		String summary = "";
		int numChars = 0;
		
		List<Integer> order = scorer.getOrdered();
		
		List<String> sentences = data.getSentences();
		numChars += sentences.get(0).length();
		summary += sentences.get(0) + "\n";
		
		int numOrder = 1;
		
		while(numOrder < order.size()){
			int index = order.get(numOrder);
			String sentence = sentences.get(index);
			numChars += sentence.length();

			if (numChars > summarySize)
				break;

			summary += sentence + "\n";

			numOrder ++;
		}
		
		return summary;
	}



	private static HashMap<String, Integer> readSizes(String lang){
		HashMap<String, Integer> sizes =
				new HashMap<String, Integer>();

		File sizefile = new File(mssFolder + "target-length/" + lang + ".txt");

		if (! sizefile.exists()) return null;

		try {
			BufferedReader in = new BufferedReader(new FileReader(sizefile));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.trim().length() < 1) continue;

				String[] values = line.split(",");
				if (values.length<2) continue;

				sizes.put(values[0], new Integer(values[1]));
			}
			in.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return sizes;
		}

		return sizes;
	}



	public static void main(String[] args) {


		for (String lang: langs){//langs

			
			HashMap<String, Integer> sizes = readSizes(lang);

			File folder = new File(mssFolder + "body/text/" +  lang + "/");
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;

			File[] files = folder.listFiles();
			
			String newfolderName = outFolder + lang + "/";
			FileManager.createFolder(new File(newfolderName));
			
			for (File file: files){//files

				String fileName = file.getName();
				if (! fileName.endsWith("_body.txt")) continue;

				int summarySize = sizes.get(fileName);

				//verify if the file have been already processed
				fileName = fileName.substring(0, fileName.length()-9);
				/*if (new File(outFolder + lang + "/" + fileName).exists()){
					System.out.println(fileName  + " already processsed");
					continue;
				}*/

				newfolderName = outFolder + lang + "/" ;
				FileManager.createFolder(new File(newfolderName));
				newfolderName = newfolderName + fileName;

				System.out.println(file.getName()  + " preprocessing...");
				BaselinesMss2018 mss = new BaselinesMss2018(lang);
				mss.preprocess(file);
				try {
					String summary;

					mss.setSummarySize(summarySize);
					mss.summarize();
					summary = mss.getSummary0();
					FileManager.saveFile(newfolderName + ".txt", summary);
			
				} catch (Exception e) {
					e.printStackTrace();
				}

			} //files

		}//languages
	}//main


	}
