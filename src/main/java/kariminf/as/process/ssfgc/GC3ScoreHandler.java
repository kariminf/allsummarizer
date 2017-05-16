package kariminf.as.process.ssfgc;

import java.util.ArrayList;
import java.util.List;

import kariminf.as.tools.Data;

public class GC3ScoreHandler extends SSFScoreHandler {
	
	
	public GC3ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Double scoreUnit(int unitID) {
		
		List<Integer> rels = relatives.get(unitID);
		
		
		
		if (rels == null) rels = new ArrayList<>();
		
		double sumSimScores = 0.0;
		double sumUnsimScores = 0.0;
		
		for(int otherUnitID: candidates){
			if (otherUnitID == unitID) continue;
			
			if (rels.contains(otherUnitID))
				sumSimScores += getSLPScore(otherUnitID);
			else 
				sumUnsimScores += getSLPScore(otherUnitID);
		}
		
		double score = getSLPScore(unitID);
		
		return score + sumSimScores - sumUnsimScores;

	}
	
	
	
}
