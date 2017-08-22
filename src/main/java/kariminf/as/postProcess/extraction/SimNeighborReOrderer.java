package kariminf.as.postProcess.extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kariminf.as.process.Scorer;
import kariminf.as.process.ssfgc.SSFScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public class SimNeighborReOrderer extends Extractor {
	
	HashMap<Integer, List<Integer>> relatives;
	
	public SimNeighborReOrderer(Scorer scorer){
		super(scorer);
		if (scorer.getScoreHandler() instanceof SSFScoreHandler){
			relatives = ((SSFScoreHandler)scorer.getScoreHandler()).getRelatives();
		}
	}

	@Override
	public void reOrder() {
		
		if (relatives == null) return;
		
		List<Integer> order0 = scorer.getOrdered();
		
		int index = order0.get(0);
		order.add(index);
		
		while(true){
			List<Integer> relIDs = relatives.get(index);
			
			if(relIDs == null || relIDs.isEmpty()) break;
			
			//Calculate similarity order
			List<Integer> relsSimOrder = new ArrayList<>();
			{
				List<Double> orderedSims = new ArrayList<>();
				
				for(int relID: relIDs){
					int j = 0;
					double currentSim = scorer.getData().getSimilarity(index, relID);
					
					//The bigger similarity comes first
					while (j < orderedSims.size() && currentSim <= orderedSims.get(j))
						j++;

					relsSimOrder.add(j, relID);
					orderedSims.add(j, currentSim);
				}
			}
			//===========================
			
			
			int prevIndex = index;
			index = -1;
			int min = Integer.MAX_VALUE;
			
			for(int relID: relIDs){
				int scoreOrder = order0.indexOf(relID);
				int simOrder = relsSimOrder.indexOf(relID);
				
				int score = scoreOrder + simOrder;
				
				System.out.println("E3_" + prevIndex + "(" + relID + ")= " + score);
				
				if (score < min)
					if(! order.contains(relID)){
						index = relID;
						min = score;
					}
			}
			
			//When all relatives are included into the summary
			if (index < 0) break;
			
			order.add(index);
		}
		
	}

}
