package kariminf.as.process;

import kariminf.as.tools.Data;

public interface ScoreHandler {
	
	public static class UnitNotIncluded extends Exception {}
	
	public abstract void setData(Data data);
	public abstract Double scoreUnit(int unitID) throws UnitNotIncluded;

}
