package kariminf.as.preProcess;

import kariminf.as.tools.Data;

public abstract class PreProcessor {
	
	protected String lang;
	protected Data data;
	
	public PreProcessor(String lang){
		this.lang = lang;
	}
	
	public void setData(Data data){
		this.data = data;
	}
	
	public Data getData(){
		return data;
	}
	
	public abstract void preProcess(String inTxt);

}
