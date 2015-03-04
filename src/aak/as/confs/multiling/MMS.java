package aak.as.confs.multiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aak.as.preProcess.PreProcessor;
import aak.as.process.extraction.Summarizer;
import aak.as.process.extraction.bayes.Feature;
import aak.as.process.extraction.bayes.PLeng;
import aak.as.process.extraction.bayes.Pos;
import aak.as.process.extraction.bayes.RLeng;
import aak.as.process.extraction.bayes.TFB;
import aak.as.process.extraction.bayes.TFU;
import aak.as.process.extraction.cluster.Cluster;
import aak.as.process.extraction.cluster.NaiveCluster;
import aak.as.tools.Data;
import aak.as.tools.Tools;

public class MMS {

	
	
	private List<Feature> features = new ArrayList<Feature>();
	private String lang = "en";
	
	private Data data;
	private boolean clustered = false;
	private PreProcessor preprocessor;
	private final static int summarySize = 250; //words
	private final static int zhSummarySize = 750; //bytes
	
	
	public MMS (String lang){
		this.lang = (lang.length()==2)?lang:"en";
		clustered = false;
		data = new Data();
		preprocessor = new PreProcessor(this.lang, this.data);
		
	}
	
	public void addDocument(String file){
		String text = readFile(file);
		preprocessor.addText(text);
	}
	
	public void preprocess(){
		preprocessor.preProcess();
		clustered = false;
	}
	
	public void cluster(double threshold){
		Cluster cluster = new NaiveCluster(threshold, data);
		cluster.createClasses();
		clustered = true;
		//System.out.println("#sent=" + data.getSentNumber());
	}
	

	/**
	 * Use a feature for scoring
	 * @param feature
	 */
	public void addFeature(Feature feature){
		if (!features.contains(feature))
			features.add(feature);
	}
	
	public String summarize (double simTH) throws Exception{
		
		if(features.size() <1 ) throw new Exception("add at least one feature");
		if (! clustered ) throw new Exception("Use cluster before summarize");
		
		Summarizer summarizer = new Summarizer();
		
		for (Feature feature: features)
			summarizer.addFeature(feature);
		
		summarizer.summarize(data);
		
		return getSummary(data, summarizer.getOrdered(), simTH);
	}
	
	/**
	 * Clear the used features if there is any
	 */
	public void clear(){
		features.clear();
		preprocessor.clearText();
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


	private String readFile(String file){
		StringBuffer content = new StringBuffer();

		int i = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.length() < 1)
					continue;
				i++;
				if (i>2) //don't take the title and the date
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
	
	
	private String getSummary(Data data, List<Integer> order, double simTH){

		
		if (lang.startsWith("zh"))
			return getChineseSummary (data, order, simTH);
		
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();
		List<Integer> nbrWords = data.getNbrWords();

		String summary = "";
		int numChars = 0;
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
			
			numChars += nbrWords.get(index);
			
			if (numChars > summarySize)
				break;
			
			summary += sentences.get(index) + "\n";

			numOrder ++;
		}
		
		if(summary.length() < 1)
			summary = sentences.get(order.get(0)).substring(0, summarySize);

		return summary;
	}
	
	private String getChineseSummary (Data data, List<Integer> order, double simTH){
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();

		String summary = "";
		int numChars = 0;
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

			try {
				final byte[] utf8Bytes = sentences.get(index).getBytes("UTF-8");
				numChars += utf8Bytes.length;
			} catch (UnsupportedEncodingException e) {
				numChars += sentences.get(index).length();
			}

			if (numChars > zhSummarySize)
				break;
			
			summary += sentences.get(index) + "\n";

			numOrder ++;
		}

		return summary;
	}


}
