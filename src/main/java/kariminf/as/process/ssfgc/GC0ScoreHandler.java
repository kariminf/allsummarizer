package kariminf.as.process.ssfgc;

public class GC0ScoreHandler extends SSFScoreHandler {
	
	
	public GC0ScoreHandler(double thSimilarity) {
		super(thSimilarity);
	}

	@Override
	public Double scoreUnit(int unitID) throws UnitNotIncluded {
		
		if (! candidates.contains(unitID)) throw new UnitNotIncluded();
		
		return getSSFScore(unitID);
	}
	
	
	
}
