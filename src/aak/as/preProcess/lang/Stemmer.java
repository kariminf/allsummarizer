package aak.as.preProcess.lang;

import java.util.List;

public interface Stemmer {

	/**
	 * 
	 * @param listWords
	 * @return
	 */
	public List<String> stemListWords(List<String> listWords);
}
