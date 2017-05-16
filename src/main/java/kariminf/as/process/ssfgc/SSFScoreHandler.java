package kariminf.as.process.ssfgc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.process.ScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public abstract class SSFScoreHandler implements ScoreHandler {

	private double thSimilarity = 0.0;

	protected Data data;

	protected List<Integer> candidates = new ArrayList<>();

	protected List<Double> sentSimDoc = new ArrayList<Double>();

	protected List<Double> slpScores = new ArrayList<Double>();

	//protected HashMap<Integer, Integer> connections = new HashMap<>();

	// sentence ID ==> most similar sentence ID
	protected HashMap<Integer, Integer> simMaxSentID =  new HashMap<>();

	protected HashMap<Integer, List<Integer>> relatives =  new HashMap<>();
	
	private double maxSSF = Double.NEGATIVE_INFINITY;
	private double minSSF = Double.POSITIVE_INFINITY;

	public SSFScoreHandler(Data data, double thSimilarity){
		this.data = data;
		setThresholdSimilarity(thSimilarity);
	}

	public SSFScoreHandler setThresholdSimilarity(double thSimilarity){
		this.thSimilarity = thSimilarity;
		calculateSimilarity();
		calculateSSFScores();
		//System.out.println("candidates=" + candidates);
		//System.out.println("nbr sentences=" + data.getSentWords().size());
		return this;
	}

	public double getThresholdSimilarity(){
		return thSimilarity;
	}

	public HashMap<Integer, List<Integer>> getRelatives(){
		return relatives;
	}

	public List<Integer> getCandidates(){
		return candidates;
	}

	public double getSLPScore(int unitID){
		if (slpScores.size() <= unitID)
			return Double.NEGATIVE_INFINITY;
		return slpScores.get(unitID);
	}


	private void calculateSSFScores(){
		slpScores = new ArrayList<Double>();
		for(int i = 0; i < data.getSentNumber(); i++){
			double score = calculateSSFScore(i);
			slpScores.add(score);
			
			if (candidates.contains(i)){
				if (score > maxSSF) maxSSF = score;
				if (score < minSSF) minSSF = score;
			}
			
			//System.out.println(">>>> score= " + slpScores.get(i));
		}
	}

	private double calculateSSFScore(int unitID){

		if (! candidates.contains(unitID))
			return Double.NEGATIVE_INFINITY;


		double score = Math.log(sentSimDoc.get(unitID));

		int docID = data.getSentDoc(unitID);

		//Normalize the size 
		//=================
		score -= Math.log(scoreSize(unitID, docID, true));


		//the position
		//=======================
		score -= Math.log(scorePosition(unitID, docID, true));

		return score; 
	}

	private double scoreSize(int unitID, int docID, boolean normalized){

		double size = data.getSentWords().get(unitID).size();

		if (!normalized) return size;

		//calculate the maximum size of the same document
		double maxSize = data.getSentWords().get(
				data.getMaxLenSentID(docID)
				).size();

		double minSize = data.getSentWords().get(
				data.getMinLenSentID(docID)
				).size();

		if (maxSize == minSize) return 1.0;

		//Add a very low amount to not have infinity score
		return  ((maxSize - size) + 0.0001)/(maxSize - minSize);

	}

	private double scorePosition(int unitID, int docID, boolean normalized){
		double posMiddle = data.getDocLength(docID);
		posMiddle = posMiddle/2;

		double pos = data.getRealPos(unitID);
		if (simMaxSentID.containsKey(unitID)){
			double pos2 = data.getRealPos(simMaxSentID.get(unitID));

			if (Math.abs(pos-posMiddle) < Math.abs(pos2-posMiddle)) 
				pos = pos2;
		}

		if (! normalized) return pos;

		//The normalized position goes to zero every time we approach the middle 
		//We can't have a zero score
		//So, the score of the middle
		double posNorm = (posMiddle == pos)?
				1/ posMiddle:
					Math.abs(posMiddle - pos)/ posMiddle;

		return posNorm;
	}


	private void calculateSimilarity(){

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

	public double getMinSSF() {
		return minSSF;
	}

	public double getMaxSSF() {
		return maxSSF;
	}


}
