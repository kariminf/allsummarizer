package kariminf.as.postProcess.extraction;

import java.util.ArrayList;
import java.util.List;

import kariminf.as.postProcess.ReOrderer;
import kariminf.as.process.Scorer;

public class ReOrderer0 implements ReOrderer {

	private Scorer scorer;
	private List<Integer> order = new ArrayList<>();
	
	public ReOrderer0(Scorer scorer){
		this.scorer = scorer;
	}

	@Override
	public void reOrder() {
		
		order.addAll(scorer.getOrdered());
		
	}

	@Override
	public List<Integer> getOrder() {
		return order;
	}

}
