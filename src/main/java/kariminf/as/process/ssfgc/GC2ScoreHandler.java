package kariminf.as.process.ssfgc;

import java.util.List;

import kariminf.as.tools.Data;

public class GC2ScoreHandler extends SSFScoreHandler {
	
	
	public GC2ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Double scoreUnit(int unitID) {

		List<Integer> rels = relatives.get(unitID);
		
		if (rels == null || rels.size() < 1)
			return getSLPScore(unitID);
		
		double normScore = (getSLPScore(unitID) - getMinSSF());
		normScore = normScore / (getMaxSSF() - getMinSSF());
		
		int nbrRelatives = rels.size() + 1;
		
		return normScore * nbrRelatives;
	}
	
	
	
}
