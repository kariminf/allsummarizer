package kariminf.as.process.slp;

import kariminf.as.tools.Data;

public class SLP2ScoreHandler extends SLPScoreHandler {
	
	
	public SLP2ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Double scoreUnit(int unitID) {
		
		double score = getSLPScore(unitID);
		
		int nbrRelatives = (relatives.containsKey(unitID))?
				relatives.get(unitID).size():
					0;
		
		for(int otherUnitID: candidates){
			if (otherUnitID == unitID) continue;
			score += nbrRelatives *  getSLPScore(otherUnitID);
		}
		
		return score;
	}
	
	
	
}
