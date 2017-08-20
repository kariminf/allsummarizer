package kariminf.as.postProcess.extraction;

import java.util.ArrayList;
import java.util.List;

import kariminf.as.postProcess.ReOrderer;
import kariminf.as.process.Scorer;
import kariminf.as.process.ssfgc.SSFScoreHandler;
import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public class ReOrderer1 implements ReOrderer {

	private Scorer scorer;
	private List<Integer> order = new ArrayList<>();
	
	double simTH; 
	
	public ReOrderer1(Scorer scorer, double simTH){
		this.scorer = scorer;
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

	@Override
	public List<Integer> getOrder() {
		return order;
	}

}
