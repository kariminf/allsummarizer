package kariminf.as.postProcess.extraction;

import java.util.ArrayList;
import java.util.List;

import kariminf.as.process.Scorer;
import kariminf.as.process.ssfgc.SSFScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public class ThresholdReOrderer extends Extractor {
	
	double simTH; 
	
	public ThresholdReOrderer(Scorer scorer, double simTH){
		super(scorer);
		this.simTH = simTH;
	}

	@Override
	public void reOrder() {
		
		List<Integer> order0 = scorer.getOrdered();
		List<List<String>> sentWords = scorer.getData().getSentWords();
		
		List<String> prevWords = new ArrayList<>();
		
		for(int index: order0){
			List<String> actWords = sentWords.get(index);
			if (!Tools.similar(prevWords, actWords, simTH)){
				order.add(index);
				prevWords = actWords;
			}
		}
		
	}

}
