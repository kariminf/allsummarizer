package aak.as.preProcess.def;

import java.util.ArrayList;
import java.util.List;

import aak.as.preProcess.lang.Segmenter;

public class DefSegmenter implements Segmenter {

	@Override
	public List<String> splitToSentences(String text) {

		List<String> sentences = new ArrayList<String>();

		for(String sentence:  text.split("[\\.؟\\!?。][\\s$]")) 
			if(sentence.trim().length() > 0) 
				sentences.add(sentence.trim());

		return sentences;
	}

	@Override
	public List<String> segmentWords(String text) {
		// TODO Auto-generated method stub
		return null;
	}

}
