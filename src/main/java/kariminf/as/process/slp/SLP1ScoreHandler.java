package kariminf.as.process.slp;

import kariminf.as.tools.Data;

public class SLP1ScoreHandler extends SLPScoreHandler {
	
	
	public SLP1ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
	}

	@Override
	public Double scoreUnit(int unitID) {
		
		double score = getSLPScore(unitID);
		
		for(int otherUnitID: candidates){
			if (otherUnitID == unitID) continue;
			score += data.getSimilarity(unitID, otherUnitID) * 
					getSLPScore(otherUnitID);
		}
		
		return score;
	}
	
	
	
}
