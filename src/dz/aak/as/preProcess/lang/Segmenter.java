package aak.as.preProcess.lang;

import java.util.List;

public interface Segmenter {
	/**
	 * 
	 * @param text
	 * @return
	 */
	public List<String> splitToSentences(String text);
	/**
	 * 
	 * @param text
	 * @return
	 */
	public List<String> segmentWords(String text);
}
