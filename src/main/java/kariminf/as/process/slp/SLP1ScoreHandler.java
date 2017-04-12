package kariminf.as.process.slp;

import kariminf.as.tools.Data;

public class SLP1ScoreHandler extends SLPScoreHandler {
	
	
	@Override
	public Double scoreUnit(Data data, int unitID) {
		
		double score = getSLPScore(data, unitID);
		
		for(int otherUnitID = 0; otherUnitID < data.getSentNumber(); otherUnitID++){
			if (otherUnitID == unitID) continue;
			score += data.getSimilarity(unitID, otherUnitID) * 
					getSLPScore(data, otherUnitID);
		}
		
		return score;
	}
	
	
	
}