package kariminf.as.process.ssfgc;

import java.util.List;

import kariminf.as.tools.Data;

public class GC4ScoreHandler extends SSFScoreHandler {
	
	private int a;
	
	public GC4ScoreHandler(Data data, double thSimilarity, boolean keepMyScore){
		super(data, thSimilarity);
		a = (keepMyScore)? 1: 0;
	}

	@Override
	public Double scoreUnit(int unitID) throws UnitNotIncluded {
		
		if (! candidates.contains(unitID)) throw new UnitNotIncluded();
		
		List<Integer> rels = relatives.get(unitID);
		
		if (rels == null || rels.size() < 1)
			return getSSFScore(unitID);
		
		//System.out.println(unitID + "==>" + rels);
		
		double sumSims = 0.0;
		
		for(int otherUnitID: rels){
			if (otherUnitID == unitID) continue;
			sumSims += data.getSimilarity(unitID, otherUnitID);
		}
		
		return getSSFScore(unitID) * (a + sumSims) ;
	}
	
	
	
}
