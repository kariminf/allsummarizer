package kariminf.as.process.ssfgc;

import kariminf.as.tools.Data;

public class GC0ScoreHandler extends SSFScoreHandler {
	
	
	public GC0ScoreHandler(Data data, double thSimilarity) {
		super(data, thSimilarity);
	}

	@Override
	public Double scoreUnit(int unitID) throws UnitNotIncluded {
		
		if (! candidates.contains(unitID)) throw new UnitNotIncluded();
		
		return getSSFScore(unitID);
	}
	
	
	
}
