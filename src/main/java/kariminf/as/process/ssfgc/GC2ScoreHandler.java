package kariminf.as.process.ssfgc;

import java.util.ArrayList;
import java.util.List;

import kariminf.as.tools.Data;

public class GC2ScoreHandler extends SSFScoreHandler {
	
	
	public GC2ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
	}

	/*
	 * CG = SSF + sum(SSF(sj); (si, sj) in V) - sum(SSF(sj); (si, sj) not in V)
	 * 
	 * (non-Javadoc)
	 * @see kariminf.as.process.ScoreHandler#scoreUnit(int)
	 */
	@Override
	public Double scoreUnit(int unitID) throws UnitNotIncluded {
		
		if (! candidates.contains(unitID)) throw new UnitNotIncluded();
		
		List<Integer> rels = relatives.get(unitID);
		
		if (rels == null) rels = new ArrayList<>();
		
		double score = getSSFScore(unitID);
		
		for(int otherUnitID: candidates){
			if (otherUnitID == unitID) continue;
			
			if (rels.contains(otherUnitID))
				score += getSSFScore(otherUnitID);
			else 
				score -= getSSFScore(otherUnitID);
		}
		
		return score;

	}
	
	
	
}
