package kariminf.as.process.ssfgc;

import java.util.List;

import kariminf.as.tools.Data;

public class GC3ScoreHandler extends SSFScoreHandler {
	
	public GC3ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
	}
	

	@Override
	public Double scoreUnit(int unitID) throws UnitNotIncluded {
		
		if (! candidates.contains(unitID)) throw new UnitNotIncluded();

		List<Integer> rels = relatives.get(unitID);
		
		if (rels == null || rels.size() < 1)
			return getSSFScore(unitID);
		
		int nbrRelatives = rels.size() + 1;
		
		return getSSFScore(unitID) * nbrRelatives;
	}
	
	
	
}
