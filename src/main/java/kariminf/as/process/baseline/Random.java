package kariminf.as.process.baseline;

import java.util.ArrayList;
import java.util.HashMap;

import kariminf.as.process.ScoreHandler;
import kariminf.as.tools.Data;

public class Random implements ScoreHandler {
	
	private HashMap<Integer, Double> scores = new HashMap<>();
	
	@Override
	public void setData(Data data) {
		int size = data.getSentAmount();
		ArrayList<Double> values = new ArrayList<>();
		for(int i=0; i<size; i++) values.add((double) i);
		for(int i=0; i<size; i++) {
			int idx = (int) (Math.random() * values.size());
			scores.put(i, values.get(idx));
		}
	}

	@Override
	public Double scoreUnit(int unitID) throws UnitNotIncluded {
		return scores.get(unitID);
	}

}
