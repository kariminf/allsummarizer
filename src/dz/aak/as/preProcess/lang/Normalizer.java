package aak.as.preProcess.lang;

import java.util.HashMap;

public interface Normalizer {
	/**
	 * 
	 * @param text
	 * @return
	 */
	public String normalize (String text);
	/**
	 * 
	 * @param text
	 * @param param the function we want to apply on the text \n
	 * For example: 
	 * @return
	 */
	public String normalize (String text, String param);
	/**
	 * 
	 * @return name of each parameter with its description
	 */
	public HashMap <String, String> getParameters();
}
