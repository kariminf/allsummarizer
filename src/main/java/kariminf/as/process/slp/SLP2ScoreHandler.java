package kariminf.as.process.slp;

import kariminf.as.tools.Data;

public class SLP2ScoreHandler extends SLPScoreHandler {
	
	
	@Override
	public Double scoreUnit(Data data, int unitID) {
		
		double score = getSLPScore(data, unitID);
		
		int nbrRelatives = (relatives.containsKey(unitID))?
				relatives.get(unitID).size():
					0;
		
		for(int otherUnitID: candidates){
			if (otherUnitID == unitID) continue;
			score += nbrRelatives *  getSLPScore(data, otherUnitID);
		}
		
		return score;
	}
	
	
	
}
