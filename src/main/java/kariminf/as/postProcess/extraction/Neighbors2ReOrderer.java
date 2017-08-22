package kariminf.as.postProcess.extraction;

import java.util.HashMap;
import java.util.List;

import kariminf.as.process.Scorer;
import kariminf.as.process.ssfgc.SSFScoreHandler;

public class Neighbors2ReOrderer extends Extractor {

	HashMap<Integer, List<Integer>> relatives;
	
	public Neighbors2ReOrderer(Scorer scorer){
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
			
			
			double max = Double.NEGATIVE_INFINITY;
			int prevIndex = index;
			index = -1;
			
			for(int relID: relIDs){
				
				if (order.contains(relID)) continue;
				
				int relNbr = 0;
				List<Integer> relIDs2 = relatives.get(relID);
				
				for(int relID2: relIDs2)
					if (!order.contains(relID2))
						relNbr++;
				
				if(relNbr> 0){
					double score = 
							((double) relNbr) / ((double)(order0.indexOf(relID)+1));
					System.out.println("E5_" + prevIndex + "(" + relID + ")= " + score);
					if (score > max){
						max = score;
						index = relID;
					}
				}
				
			}
			
			//When all relatives are included into the summary
			if (index < 0) break;
			
			order.add(index);

		}
		
	}

	@Override
	public List<Integer> getOrder() {
		return order;
	}

}
