package kariminf.as;

import java.util.ArrayList;

import kariminf.as.postProcess.PostProcessor;
import kariminf.as.preProcess.PreProcessor;
import kariminf.as.process.Scorer;
import kariminf.as.tools.Data;

public class AllSummarizer {
	
	private ArrayList<PreProcessor> preProcessors = new ArrayList<>();
	private ArrayList<Scorer> scorers = new ArrayList<>();
	private ArrayList<PostProcessor> postProcessors = new ArrayList<>();
	private boolean lock = false;
	private Data data;
	
	
	/**
	 * 
	 * @param preProcessor
	 */
	public void addPreProcessor(PreProcessor preProcessor){
		if(lock) return;
		if (preProcessor.getData() != null) return;
		
		if (data == null) data = preProcessor.getData();
		
		//All preprocessors must work on the same data file
		if(data != preProcessor.getData()) return;
		
		preProcessors.add(preProcessor);
	}
	
	/**
	 * 
	 * @param scorer
	 */
	public void addScorer(Scorer scorer){
		if(lock) return;
		scorer.setData(data);
		scorers.add(scorer);
	}
	
	/**
	 * 
	 * @param postProcessor
	 */
	public void addPostProcessor(PostProcessor postProcessor){
		if(lock) return;
		postProcessor.setData(data);
		postProcessors.add(postProcessor);
	}
	
	/**
	 * 
	 */
	public void lock(){
		lock = true;
	}
	
	/**
	 * 
	 */
	public void unlock(){
		lock = false;
	}
	
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public boolean summarize(String text){
		
		if (preProcessors.isEmpty()) return false;
		
		for(PreProcessor preProcessor: preProcessors){
			preProcessor.preProcess(text);
		}
		
		for(Scorer scorer: scorers){
			scorer.scoreUnits();
		}
		
		for(PostProcessor postPressor: postProcessors){
			// Add 
		}
		
		return true;
	}

}
