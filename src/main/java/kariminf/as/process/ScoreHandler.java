package kariminf.as.process;

import kariminf.as.tools.Data;

public interface ScoreHandler {
	
	public static class UnitNotIncluded extends Exception {}
	
	public abstract Double scoreUnit(int unitID) throws UnitNotIncluded;

}
