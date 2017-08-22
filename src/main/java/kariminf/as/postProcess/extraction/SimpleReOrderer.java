package kariminf.as.postProcess.extraction;

import kariminf.as.process.Scorer;

public class SimpleReOrderer extends Extractor {

	public SimpleReOrderer(Scorer scorer){
		super(scorer);
	}

	@Override
	public void reOrder() {
		
		order.addAll(scorer.getOrdered());
		
	}



}
