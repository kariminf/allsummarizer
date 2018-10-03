package kariminf.as.process.baseline;

import kariminf.as.process.ScoreHandler;
import kariminf.as.tools.Data;

public class Firsts implements ScoreHandler {
	
	protected Data data;

	@Override
	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public Double scoreUnit(int unitID) throws UnitNotIncluded {
		return 1/(unitID + 0.1);
	}

}
