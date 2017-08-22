package kariminf.as.process.ssfgc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import kariminf.as.process.ScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public abstract class SSFScoreHandler implements ScoreHandler {

	private double thSimilarity = 0.0;

	/**
	 * @return the thSimilarity
	 */
	public double getThSimilarity() {
		return thSimilarity;
	}

	protected Data data;

	protected List<Integer> candidates = new ArrayList<>();

	private List<Double> sentSimDoc = new ArrayList<Double>();

	private HashMap<Integer,Double> ssfScores = new HashMap<>();

	//protected HashMap<Integer, Integer> connections = new HashMap<>();

	// sentence ID ==> most similar sentence ID
	private HashMap<Integer, Integer> simMaxSentID =  new HashMap<>();

	protected HashMap<Integer, List<Integer>> relatives =  new HashMap<>();
	
	private HashMap<String, Integer> tfu = new HashMap<>();
	private HashMap<String, Integer> sfu = new HashMap<>();
	
	private double maxSSF = Double.NEGATIVE_INFINITY;
	private double minSSF = Double.POSITIVE_INFINITY;
	
	private boolean isPosNorm = false;
	
	private boolean isSizeNorm = false;
	
	//private int scoreRegulator = 1000;

	public SSFScoreHandler(double thSimilarity){
		
		setThresholdSimilarity(thSimilarity);
	}
	
	public void setData(Data data){
		this.data = data;
		calculateSimilarity();
		calculateTF();
		//calculateSSFScores();
		//System.out.println("candidates=" + candidates);
		//System.out.println("nbr sentences=" + data.getSentWords().size());
	}

	public SSFScoreHandler setThresholdSimilarity(double thSimilarity){
		this.thSimilarity = thSimilarity;
		return this;
	}
	
	public SSFScoreHandler setNormalization(boolean isPosNorm, boolean isSizeNorm){
		this.isPosNorm = isPosNorm;
		this.isSizeNorm = isSizeNorm;
		return this;
	}

	public HashMap<Integer, List<Integer>> getRelatives(){
		return relatives;
	}

	public List<Integer> getCandidates(){
		return candidates;
	}

	public double getSSFScore(int unitID){
		if (ssfScores.size() <= unitID)
			return Double.NEGATIVE_INFINITY;
		return ssfScores.get(unitID);
	}


	public void calculateSSFScores(){
		ssfScores = new HashMap<>();
		for(int i = 0; i < data.getSentNumber(); i++){
			double score = calculateSSFScore(i);
			ssfScores.put(i,score);
			
			if (candidates.contains(i)){
				if (score > maxSSF) maxSSF = score;
				if (score < minSSF) minSSF = score;
			}
			
			//System.out.println(">>>> score= " + ssfScores.get(i));
		}
	}
	
	
	public double scoreTfIsf(int unitID){
		double tfIsfSums = 0.0;
		double logTotalSize = Math.log(candidates.size());
		for (String word: data.getSentWords().get(unitID))
			//tfIsfSums += tfu.get(word) * Math.pow(logTotalSize - Math.log(sfu.get(word)), 2.0);
			tfIsfSums += Math.pow(tfu.get(word) * (logTotalSize - Math.log(sfu.get(word))), 2.0);
		return Math.sqrt(tfIsfSums);
	}

	/*
	 * Score(si) = log (1 + (sim(si, C)/(size(si)*pos(si))))
	 */
	private double calculateSSFScore(int unitID){

		if (! candidates.contains(unitID))
			return Double.NEGATIVE_INFINITY;

		
		int docID = data.getSentDoc(unitID);
		

		double score = sentSimDoc.get(unitID);
		
		score *= scoreTfIsf(unitID);
		score *= scoreSize(unitID, docID);
		score *= scorePosition(unitID, docID);

		return score; 
	}
	
	public void converge(){
		double prevSumScores = 0.001;
		double precision = 1.0;
		int i = 0;
		while (precision > 0.01){
			double sumScores = 0.0;
			for (int unitID: candidates){
				try {
					double score = scoreUnit(unitID);
					sumScores += score;
					ssfScores.put(unitID, score);
				} catch (UnitNotIncluded e) {
				}
			}
			precision = (sumScores - prevSumScores)/prevSumScores;
			prevSumScores = sumScores;
			i++;
			if (i >= 1000) break;
		}
		
		
		
	}

	private double scoreSize(int unitID, int docID){

		double size = data.getSentWords().get(unitID).size();
		
		if (size == 0) return 0;
		
		return 1/size;

	}
	

	private double scorePosition(int unitID, int docID){
		
		int len = data.getDocLength(docID);
		
		double posMiddle = (double) len / 2.0;

		double pos = data.getRealPos(unitID);
		
		if (pos <= posMiddle) return 1.0/ (pos + 1.0);
		
		return 1.0/(len - pos + 1.0) ;
		
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
		
		HashMap<Integer, Integer> nbrImpNeighbors = new HashMap<>();

		for(int i = 0; i < sentWords.size(); i++){

			double maxSim = - 1.0;
			//boolean isCandidate = false;
			double simSum = 0.0;
			
			int nbrImportant = 0;

			for(int j = 0; j< sentWords.size(); j++){
				if (i == j) continue;
				double sim = data.getSimilarity(i, j);

				if (sim >= thSimilarity) nbrImportant++;
				
				simSum += sim;



				//isCandidate = true;

				if (maxSim < sim){
					simMaxSentID.put(i, j);
					maxSim = sim;
				}
			}

			//if (isCandidate) candidates.add(i);
			double vertexCut = (nbrImportant> 0)? 1.0/(double) nbrImportant: 1000;
			
			nbrImpNeighbors.put(i, nbrImportant);
			
			//System.out.println("cutting vertex for sentence " + i + " is " + vertexCut);
			//System.out.println("sim sum for sentence " + i + " is " + simSum);
			if (simSum >= vertexCut) candidates.add(i);

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
			
			double edgeCut = thSimilarity/(double) nbrImpNeighbors.get(i);
			
			//System.out.println("cutting edge for sentence " + i + " is " + edgeCut);

			for(int j: candidates){
				if (i == j) continue;
				//compare with all other sentences
				otherWords.addAll(sentWords.get(j));
				if (data.getSimilarity(i, j) >= edgeCut){
					//System.out.println("edge( " + i + ", " + j + ")");
					simSentIDs.add(j);
				}

			}

			//System.out.printf(">>%d => %d\n", i, otherWords.size());
			double sim = Tools.calcSimilarity(sentWords.get(i), otherWords);
			sentSimDoc.add(sim);
		}//


	}
	
	public void calculateTF(){
		tfu = new HashMap<>();
		List<List<String>> sentWords = data.getSentWords();
		for (int unitID : candidates){
			HashSet<String> foundWords = new HashSet<>();
			for (String word : sentWords.get(unitID)) {
				int value = (tfu.containsKey(word)) ? tfu.get(word) + 1 : 1;
				tfu.put(word, value);
				foundWords.add(word);
			}
			
			for (String word : foundWords) {
				int value = (sfu.containsKey(word)) ? sfu.get(word) + 1 : 1;
				sfu.put(word, value);
			}
		}
			
	}

	public double getMinSSF() {
		return minSSF;
	}

	public double getMaxSSF() {
		return maxSSF;
	}

	/*
	public int getScoreRegulator() {
		return scoreRegulator;
	}

	public void setScoreRegulator(int scoreRegulator) {
		this.scoreRegulator = scoreRegulator;
	}*/
	
	public double getSentCandidateSim(int unitID){
		if (! candidates.contains(unitID))
			return 0.0;
		return sentSimDoc.get(unitID);
	}
	
	public int getMostSimilar(int unitID){
		if (! candidates.contains(unitID))
			return -1;
		return simMaxSentID.get(unitID);
	}
	
	//=============================================================
	// These getters are usually used in the test 
	// The variables are treated internally 
	// The returned lists, etc. are clones and not the actual ones
	//=============================================================
	
	public double getThresholdSimilarity(){
		return thSimilarity;
	}
	
	public List<Double> getSentSimDoc(){
		List<Double> result = new ArrayList<>();
		result.addAll(sentSimDoc);
		return result;
	}
	
	public HashMap<Integer, Integer> getSimMaxSentID(){
		HashMap<Integer, Integer> result = new HashMap<>();
		for (Integer k: simMaxSentID.keySet())
			result.put(k, simMaxSentID.get(k));
		return result;
	}
	


}
