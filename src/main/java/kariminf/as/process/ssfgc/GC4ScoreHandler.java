package kariminf.as.process.ssfgc;

import java.util.List;

import kariminf.as.tools.Data;

public class GC4ScoreHandler extends SSFScoreHandler {
	
	
	public GC4ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
	}

	@Override
	public Double scoreUnit(int unitID) {
		
		List<Integer> rels = relatives.get(unitID);
		
		if (rels == null || rels.size() < 1)
			return getSLPScore(unitID);
		
		//System.out.println(unitID + "==>" + rels);
		
		double sumSims = 0.0;
		
		for(int otherUnitID: rels){
			if (otherUnitID == unitID) continue;
			sumSims += data.getSimilarity(unitID, otherUnitID);
		}
		
		
		double normScore = (getSLPScore(unitID) - getMinSSF());
		normScore = normScore / (getMaxSSF() - getMinSSF());
		
		
		return normScore * (1.0 + sumSims) ;
	}
	
	
	
}
