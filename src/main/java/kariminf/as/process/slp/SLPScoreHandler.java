package kariminf.as.process.slp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.process.ScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public abstract class SLPScoreHandler implements ScoreHandler {
	
	private double thSimilarity = 0.0;
	
	protected List<Double> sentSimDoc = new ArrayList<Double>();
	
	// sentence ID ==> most similar sentence ID
	protected HashMap<Integer, Integer> simSentID =  new HashMap<>();
	
	public void setThresholdSimilarity(double thSimilarity){
		this.thSimilarity = thSimilarity;
	}

	protected double getSLPScore(Data data, int unitID){
		
		calculateSimilarity(data);
		
		double score = Math.log(sentSimDoc.get(unitID));
		score -= Math.log(data.getSentWords().size());
		
		score -= Math.log(data.getRealPos().get(simSentID.get(unitID)));
		return score;
	}
	
	private void calculateSimilarity(Data data){
		List<List<String>> sentWords = data.getSentWords();
		
		for(int i = 0; i < sentWords.size(); i++){
			List<String> otherWords = new ArrayList<>();
			double maxSim = 0.0;
			for(int j = 0; j< sentWords.size(); j++){
				if (i == j) continue;
				double sim = data.getSimilarity(i, j);
				if (sim < thSimilarity) continue;
				otherWords.addAll(sentWords.get(j));
				if (maxSim < sim){
					simSentID.put(i, j);
					maxSim = sim;
				}
			}
			double sim = Tools.calcSimilarity(sentWords.get(i), otherWords);
			sentSimDoc.add(sim);
		}
	}

}
