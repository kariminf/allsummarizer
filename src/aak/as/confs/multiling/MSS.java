package aak.as.confs.multiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aak.as.preProcess.PreProcessor;
import aak.as.process.extraction.Summarizer;
import aak.as.process.extraction.bayes.Feature;
import aak.as.process.extraction.cluster.Cluster;
import aak.as.process.extraction.cluster.NaiveCluster;
import aak.as.tools.Data;


public class MSS {
	
	private List<Feature> features = new ArrayList<Feature>();
	private String lang = "en";
	
	private Data data;
	private boolean clustered = false;
	private PreProcessor preprocessor;
	
	public MSS (String lang){
		this.lang = (lang.length()==2)?lang:"en";
		clustered = false;
		data = new Data();
		preprocessor = new PreProcessor(this.lang, this.data);
	}

	public void preprocess(File file){
		String text = readFile(file);
		preprocessor.preProcess(text);
	}
	
	public List<Double> getSimilarity(){
		return data.getSimList();
	}
	
	public void cluster(double threshold){
		Cluster cluster = new NaiveCluster(threshold, data);
		cluster.createClasses();
		clustered = true;
	}
	
	
	/**
	 * Use a feature for scoring
	 * @param feature
	 */
	public void addFeature(Feature feature){
		if (!features.contains(feature))
			features.add(feature);
	}
	
	public String summarize(int summarySize) throws Exception{
		
		if(features.size() <1 ) throw new Exception("add at least one feature");
		if (! clustered ) throw new Exception("Use cluster before summarize");
		
		Summarizer summarizer = new Summarizer();
		
		for (Feature feature: features)
			summarizer.addFeature(feature);
		
		return "";
	}
	
	/**
	 * Clear the used features if there is any
	 */
	public void clearFeatures(){
		features.clear();
	}


	private String readFile(File file){
		StringBuffer content = new StringBuffer();

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.startsWith("["))
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

}
