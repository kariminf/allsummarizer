package aak.as.preProcess.def;

import java.util.List;

import aak.as.preProcess.lang.Stemmer;

public class DefStemmer implements Stemmer {

	@Override
	public List<String> stemListWords(List<String> listWords) {
		return listWords;
	}

}
