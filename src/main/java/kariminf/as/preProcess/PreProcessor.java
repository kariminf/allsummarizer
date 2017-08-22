package kariminf.as.preProcess;

import java.util.ArrayList;
import java.util.List;

import kariminf.as.tools.Data;

public abstract class PreProcessor {
	
	protected String lang;
	protected Data data;
	
	// In multi-document summarization, we have many texts
	protected List<String> texts = new ArrayList<String>();
	
	public PreProcessor(String lang){
		this.lang = lang;
	}
	
	public void setData(Data data){
		this.data = data;
	}
	
	public Data getData(){
		return data;
	}
	
	/**
	 * Clears the texts in multi-document preprocessing
	 */
	public void clearText(){
		texts.clear();
	}

	/**
	 * Adds a text to the texts to be preprocessed
	 * 
	 * @param text the text to add
	 */
	public void addText(String text){
		texts.add(text);	
	}
	
	public abstract void preProcess();

}
