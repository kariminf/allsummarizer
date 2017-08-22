package kariminf.as.postProcess;

import kariminf.as.tools.Data;

public abstract class PostProcessor {
	
	private Data data;
	
	public void setData(Data data){
		this.data = data;
	}
	
	public abstract void postProcess();
	

}
