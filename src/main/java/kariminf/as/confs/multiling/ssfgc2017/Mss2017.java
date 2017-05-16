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

public class Mss2017 {

	//private String lang = "en";

	private Data data;
	private PreProcessor preprocessor;
	private int summarySize;
	
	private Scorer scorer;

	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/Mss15Train/";

	private static final String outFolder =
			"/home/kariminf/Data/ATS/Mss15Train/tests/training2017/";

	private static final String [] langs = 
		{/*"af", 
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
				"zh"*/
				"en"};
	
	private static final String[] types = {
			"SSF-GC0", "SSF-GC2", "SSF-GC4"
	};
	
	private static final double [] ths = 
		{0.0, 0.0, 0.0, 0.0};
	private static final String [] thNames = 
		{"00", "mean", "median", "hmode"};
	/*private static final String [] thNames = 
		{"median"};*/
	
	//fa

	public Mss2017 (String lang){
		//this.lang = (lang.length()==2)?lang:"en";
		data = new Data();
		preprocessor = new StaticPreProcessor(lang, data);
	}
	
	
	public void setSummarySize(int summarySize){
		this.summarySize = summarySize;
	}
	
	
	public Data getData(){
		return data;
	}


	public void preprocess(File file){
		String text = readFile(file);
		preprocessor.preProcess(text);
	}

	public List<Double> getSimilarity(){
		return data.getSimList();
	}

	public int sentNum (){
		return data.getSentNumber();
	}

	public double getTermDistribution(){
		List<List<String>> sentWords = data.getSentWords();
		return Tools.termsDistribution(sentWords);
	}

	public List<Integer> getTermsStats(){
		List<List<String>> sentWords = data.getSentWords();
		return Tools.termsStats(sentWords);
	}

	public int getMaxFrequency(){
		List<List<String>> sentWords = data.getSentWords();
		HashMap<String, Integer> termsFreqs = Tools.getDocWordsFreq(sentWords);

		int maxFreq = 0;

		for (int freq : termsFreqs.values()){
			maxFreq = Math.max(maxFreq, freq);
		}
		return maxFreq;
	}


	public void summarize(int summarySize, String type, double thSimilarity){
		
		SSFScoreHandler ssh;
		
		int typenum = Integer.parseInt(type.substring(type.length()-1));
		
		System.out.println(typenum);
		
		switch (typenum) {
		case 1:
			ssh = new GC1ScoreHandler(data, thSimilarity);
			break;
		case 2:
			ssh = new GC2ScoreHandler(data, thSimilarity);
			break;
		case 3:
			ssh = new GC3ScoreHandler(data, thSimilarity);
			break;
		case 4:
			ssh = new GC4ScoreHandler(data, thSimilarity);
			break;
		default:
			ssh = new GC0ScoreHandler(data, thSimilarity);
		}
		
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


	
	private String getSummary1(){

		String summary = "";
		int numChars = 0;
		int numOrder = 0;
		
		List<Integer> order = scorer.getOrdered();
		
		double simTH = ((SSFScoreHandler)scorer.getScoreHandler())
				.getThresholdSimilarity();
		
		List<List<String>> sentWords = data.getSentWords();
		List<String> sentences = data.getSentences();

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

			numChars += sentences.get(index).length();


			if (numChars > summarySize)
				break;

			summary += sentences.get(index) + "\n";

			numOrder ++;
		}

		if (summary.length() < 1)
			summary = sentences.get(order.get(0));


		return summary;
	}
	
	
	private String getSummary2(){

		String summary = "";
		int numChars = 0;
		
		List<Integer> order = scorer.getOrdered();
		HashMap<Integer, List<Integer>> relatives = 
				((SSFScoreHandler)scorer.getScoreHandler()).getRelatives();
		
		List<String> sentences = data.getSentences();
		
		
		List<Integer> included = new ArrayList<>();
		
		//The first scored sentence is held as summary
		int index = order.get(0);
		included.add(index);
		summary += sentences.get(index) + "\n";
		numChars += sentences.get(index).length();
		
		while(true){
			List<Integer> relIDs = relatives.get(index);
			index = -1;
			int minOrder = Integer.MAX_VALUE;
			if (relIDs == null) break;
			for(int relID: relIDs){
				int thisOrder = order.indexOf(relID);
				if (thisOrder >= 0 && minOrder > thisOrder)
					if(! included.contains(relID)){
						index = relID;
						minOrder = thisOrder;
					}
			}
			if (index < 0) break;
			
			included.add(index);
			summary += sentences.get(index) + "\n";
			numChars += sentences.get(index).length();
			
			if (numChars >= summarySize) break;
		}


		return summary;
	}
	
	
	private String getSummary3(){

		String summary = "";
		int numChars = 0;
		
		List<Integer> order = scorer.getOrdered();
		HashMap<Integer, List<Integer>> relatives = 
				((SSFScoreHandler)scorer.getScoreHandler()).getRelatives();
		
		List<String> sentences = data.getSentences();
		
		List<Integer> included = new ArrayList<>();
		
		//The first scored sentence is held as summary
		int index = order.get(0);
		included.add(index);
		summary += sentences.get(index) + "\n";
		numChars += sentences.get(index).length();
		
		while(true){
			List<Integer> relIDs = relatives.get(index);
			int prevIndex = index;
			index = -1;
			double max = Double.MIN_VALUE;
			if(relIDs == null) break;
			for(int relID: relIDs){
				double score = order.indexOf(relID);
				score = score/(order.size() * data.getSimilarity(prevIndex, relID));
				if (score > max)
					if(! included.contains(relID)){
						index = relID;
						max = score;
					}
			}
			
			if (index < 0) break;
			
			included.add(index);
			summary += sentences.get(index) + "\n";
			numChars += sentences.get(index).length();
			
			if (numChars >= summarySize) break;
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
				fileName = fileName.substring(0, fileName.length()-9) + "/";
				/*if (new File(outFolder + lang + "/" + fileName).exists()){
					System.out.println(fileName  + " already processsed");
					continue;
				}*/

				newfolderName = outFolder + lang + "_norm/" + fileName;
				FileManager.createFolder(new File(newfolderName));

				System.out.println(file.getName()  + " preprocessing...");
				Mss2017 mss = new Mss2017(lang);
				mss.preprocess(file);
				
				//Affecting similarity thresholds
				{
					List<Double> sim = Calculus.delMultiple(mss.getSimilarity(), 0.0);
					ths[1] = Calculus.mean(sim);
					ths[2] = Calculus.median(sim);
					ths[3] = Calculus.modeHigh(sim);
				}
				
				for (int thIdx=0; thIdx < ths.length; thIdx++){
					
					for (int typeIdx = 0; typeIdx < types.length; typeIdx++){
						try {
							String summary;
							
							System.out.println("setting summary size to " + summarySize);
							
							mss.setSummarySize(summarySize);

							System.out.println(types[typeIdx] + ".threshold=" + thNames[thIdx]);
							mss.summarize(summarySize, types[typeIdx], ths[thIdx]);
							
							String name = newfolderName + types[typeIdx];
							name += "_" + thNames[thIdx] + "_";
							
							System.out.println("\t extraction method 1");
							summary = mss.getSummary1();
							FileManager.saveFile(name + "e1.asz", summary);
							
							System.out.println("\t extraction method 2");
							summary = mss.getSummary2();
							FileManager.saveFile(name + "e2.asz", summary);
							
							System.out.println("\t extraction method 3");
							summary = mss.getSummary3();
							FileManager.saveFile(name + "e3.asz", summary);
					
						} catch (Exception e) {
							e.printStackTrace();
						}
					}//Types
					
				}//Thresholds

				

			} //files

		}//languages
	}//main


	}
