package aak.as.confs.multiling;

import java.util.ArrayList;
import java.util.List;

import aak.as.preProcess.PreProcessor;
import aak.as.process.extraction.bayes.Feature;
import aak.as.tools.Data;


public class MSS {
	
	private List<Feature> features = new ArrayList<Feature>();
	private String lang = "en";
	private PreProcessor preprocessor;
	private Data data = new Data();
	
	public MSS (String lang){
		this.lang = (lang.length()==2)?lang:"en";
		preprocessor = new PreProcessor(this.lang, this.data);
	}
	
	/**
	 * Use a feature for scoring
	 * @param feature
	 */
	public void addFeature(Feature feature){
		if (!features.contains(feature))
			features.add(feature);
	}
	
	/**
	 * Clear the used features if there is any
	 */
	public void clearFeatures(){
		features.clear();
	}
	
	

}
