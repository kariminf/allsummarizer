package kariminf.as.confs.multiling.slp2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import kariminf.as.confs.multiling.MSS;
import kariminf.as.preProcess.StaticPreProcessor;
import kariminf.as.process.ScoreHandler;
import kariminf.as.process.Scorer;
import kariminf.as.process.slp.SLP1ScoreHandler;
import kariminf.as.process.slp.SLP2ScoreHandler;
import kariminf.as.process.slp.SLPScoreHandler;
import kariminf.as.process.topicclassif.BayesScoreHandler;
import kariminf.as.process.topicclassif.Cluster;
import kariminf.as.process.topicclassif.Feature;
import kariminf.as.process.topicclassif.NaiveCluster;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;
import kariminf.ktoolja.file.FileManager;
import kariminf.ktoolja.math.Calculus;

public class MssTraining {

	private String lang = "en";

	private Data data;
	private StaticPreProcessor preprocessor;
	private int summarySize;

	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/Mss15Train/";

	private static final String outFolder =
			"/home/kariminf/Data/ATS/Mss15Train/tests/training2017/";

	private static final String [] langs = 
		{"en"};
	//"af", "eo", "hr", "ms", "sh", "sl", "sr", "vi"

	public MssTraining (String lang){
		this.lang = (lang.length()==2)?lang:"en";
		data = new Data();
		preprocessor = new StaticPreProcessor(this.lang, this.data);
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


	public Scorer summarize(int summarySize, ScoreHandler sh){

		Scorer scorer = Scorer.create(sh);
		scorer.setData(data);
		scorer.scoreUnits();

		return scorer;
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


	
	private String getSummary1(List<Integer> order, double simTH){

		String summary = "";
		int numChars = 0;
		int numOrder = 0;
		
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
	
	
	private String getSummary2(List<Integer> order, SLPScoreHandler slp){

		String summary = "";
		int numChars = 0;
		int numOrder = 0;
		
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

			MssTraining mss = new MssTraining(lang);
			HashMap<String, Integer> sizes = readSizes(lang);

			File folder = new File(mssFolder + "body/text/" +  lang + "/");
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;

			File[] files = folder.listFiles();
			
			for (File file: files){//files

				double min = 0.0;
				double max = 0.0;

				String fileName = file.getName();
				if (! fileName.endsWith("_body.txt")) continue;

				int summarySize = sizes.get(fileName);

				//verify if the file have been already processed
				fileName = fileName.substring(0, fileName.length()-9) + "/";
				if (new File(outFolder + lang + "/" + fileName).exists()){
					System.out.println(fileName  + " already processsed");
					continue;
				}



				String newfolderName = outFolder + lang + "/";
				FileManager.createFolder(new File(newfolderName));
				newfolderName += fileName;
				FileManager.createFolder(new File(newfolderName));

				System.out.println(file.getName()  + " preprocessing...");
				mss.preprocess(file);

				System.out.println("summary size= " + summarySize);
				
				List<Double> sim = Calculus.delMultiple(mss.getSimilarity(), 0.0);

				double mean = Calculus.mean(sim);
				
				SLPScoreHandler s1t0 = 
						new SLP1ScoreHandler().setThresholdSimilarity(0.0);
				SLPScoreHandler s2t0 = 
						new SLP2ScoreHandler().setThresholdSimilarity(0.0);
				SLPScoreHandler s1tm = 
						new SLP1ScoreHandler().setThresholdSimilarity(mean);
				SLPScoreHandler s2tm = 
						new SLP2ScoreHandler().setThresholdSimilarity(mean);

				try {
	
					Scorer scorer;
					String summary;
					
					mss.setSummarySize(summarySize);
					
					//SLP1 with sim=0.0
					//==================
					scorer = mss.summarize(summarySize, s1t0);
					
					summary = mss.getSummary1(scorer.getOrdered(), mean);
					FileManager.saveFile(newfolderName + "SLP1_0_e1.asz", summary);
					
					
					//SLP1 with sim=mean
					//==================
					scorer = mss.summarize(summarySize, s1tm);
					
					summary = mss.getSummary1(scorer.getOrdered(), mean);
					FileManager.saveFile(newfolderName + "SLP2_m_e1.asz", summary);
					
					
					//SLP2 with sim=0.0
					//==================
					scorer = mss.summarize(summarySize, s2t0);
					
					summary = mss.getSummary1(scorer.getOrdered(), mean);
					FileManager.saveFile(newfolderName + "SLP2_0_e1.asz", summary);
					
					
					//SLP2 with sim=mean
					//==================
					scorer = mss.summarize(summarySize, s2tm);
					
					summary = mss.getSummary1(scorer.getOrdered(), mean);
					FileManager.saveFile(newfolderName + "SLP2_m_e1.asz", summary);

					
					
				} catch (IOException e) {
					e.printStackTrace();
				}

			} //files

		}//languages
	}//main


	}
