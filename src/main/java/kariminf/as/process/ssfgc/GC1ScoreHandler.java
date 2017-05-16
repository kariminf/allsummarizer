package kariminf.as.process.ssfgc;

import java.util.List;

import kariminf.as.tools.Data;

public class GC1ScoreHandler extends SSFScoreHandler {
	
	
	public GC1ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
	}

	@Override
	public Double scoreUnit(int unitID) {
		
		List<Integer> rels = relatives.get(unitID);
		
		double score = getSLPScore(unitID);
		
		if (rels == null || rels.size() < 1)
			return score;
		
		for(int otherUnitID: rels){
			if (otherUnitID == unitID) continue;
			score += data.getSimilarity(unitID, otherUnitID) * 
					getSLPScore(otherUnitID);
		}
		
		return score;
	}
	
	
	
}
