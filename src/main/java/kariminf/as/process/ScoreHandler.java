package kariminf.as.process;

import java.util.List;

import kariminf.as.tools.Data;

public interface ScoreHandler {
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public List<Integer> reorderUnits(Data data);
	
	public Double getScore(int unitID);

}
