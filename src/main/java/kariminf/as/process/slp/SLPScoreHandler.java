package kariminf.as.process.slp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.process.ScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public abstract class SLPScoreHandler implements ScoreHandler {
	
	private double thSimilarity = 0.0;
	
	
	protected List<Integer> candidates = new ArrayList<>();
	
	protected List<Double> sentSimDoc = new ArrayList<Double>();
	
	//protected HashMap<Integer, Integer> connections = new HashMap<>();
	
	// sentence ID ==> most similar sentence ID
	protected HashMap<Integer, Integer> simMaxSentID =  new HashMap<>();
	
	protected HashMap<Integer, List<Integer>> relatives =  new HashMap<>();
	
	public SLPScoreHandler setThresholdSimilarity(double thSimilarity){
		this.thSimilarity = thSimilarity;
		return this;
	}
	
	public double getThresholdSimilarity(){
		return thSimilarity;
	}
	
	public HashMap<Integer, List<Integer>> getRelatives(int sentID){
		return relatives;
	}
	
	public List<Integer> getCandidates(){
		return candidates;
	}

	public double getSLPScore(Data data, int unitID){
		
		calculateSimilarity(data);
		
		
		double score = Math.log(sentSimDoc.get(unitID));
		
		int docID = data.getSentDoc(unitID);
		
		//Normalize the size 
		//=================
		{
			//calculate the maximum size of the same document
			double maxSize = data.getSentWords().get(
					data.getMaxLenSentID(docID)
					).size();
			
			double minSize = data.getSentWords().get(
					data.getMinLenSentID(docID)
					).size();
			
			if (maxSize != minSize){
				double size = data.getSentWords().get(unitID).size();
				
				//Add a very low amount to not have infinnity score
				double sizeNorm = ((maxSize - size) + 0.0001)/(maxSize - minSize);
						
				score -= Math.log(sizeNorm);
				
				//System.out.printf("min=%d, max=%d, size=%d, norm=%f", (int)minSize, (int)maxSize, (int)size, sizeNorm);
			}
			
		}
		
		
		//Normalize the position
		//=======================
		{
			double posMiddle = data.getDocLength(docID);
			posMiddle = posMiddle/2;
			
			double pos = data.getRealPos(unitID);
			
			//The normalized position goes to zero every time we approach the middle 
			//We can't have a zero score
			//So, the score of the middle
			double posNorm = (posMiddle == pos)?
					1/ posMiddle:
						Math.abs(posMiddle - pos)/ posMiddle;
			
			score -= Math.log(posNorm);
		}
		
		return score; 
	}
	
	public void calculateSimilarity(Data data){
		
		// Clear data
		sentSimDoc = new ArrayList<Double>();
		simMaxSentID =  new HashMap<>();
		relatives =  new HashMap<>();
		candidates = new ArrayList<Integer>();
		
		List<List<String>> sentWords = data.getSentWords();
		
		// Search for candidate units
		//===========================
		
		for(int i = 0; i < sentWords.size(); i++){
			
			double maxSim = - 1.0;
			boolean isCandidate = false;
			
			for(int j = 0; j< sentWords.size(); j++){
				if (i == j) continue;
				double sim = data.getSimilarity(i, j);
				
				if (sim < thSimilarity) continue;
				
				
				
				isCandidate = true;
				
				if (maxSim < sim){
					simMaxSentID.put(i, j);
					maxSim = sim;
				}
			}
			
			if (isCandidate) candidates.add(i);
			
		}
		
		
		// claculate the new similarities
		//The max similar sentence to each one
		//The list of similar sentences
		//================================
		
		for(int i = 0; i < sentWords.size(); i++){
			
			if (!candidates.contains(i)){
				sentSimDoc.add(0.0);
				continue;
			}
			
			List<Integer> simSentIDs = new ArrayList<>();
			relatives.put(i, simSentIDs);
			
			List<String> otherWords = new ArrayList<>();
			
			for(int j: candidates){
				if (i == j) continue;
				otherWords.addAll(sentWords.get(j));
				
				if (data.getSimilarity(i, j) > thSimilarity){
					simSentIDs.add(j);
				}
					
			}
			
			//System.out.printf(">>%d => %d\n", i, otherWords.size());
			double sim = Tools.calcSimilarity(sentWords.get(i), otherWords);
			sentSimDoc.add(sim);
		}//
		
		
	}
	

}
