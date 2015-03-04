package aak.as.preProcess.def;

import java.util.ArrayList;
import java.util.List;

import aak.as.preProcess.lang.Segmenter;

public class DefSegmenter implements Segmenter {

	@Override
	public List<String> splitToSentences(String text) {

		List<String> sentences = new ArrayList<String>();
		
		text = text.replaceAll("([\\.\\!\\?؟。]+)[\\s$]", "$1#&#");
		for(String sentence:  text.split("#&#")) 
			if(sentence.trim().length() > 0) 
				sentences.add(sentence.trim());
		
		//System.out.println("sent # = " + sentences.size());

		return sentences;
	}

	@Override
	public List<String> segmentWords(String text) {

		List<String> words = new ArrayList<String>();
	    for(String word:  text.split("[\\.,;؛،、:\"\']?\\s+|[\\.\\!\\?؟。]+$")) {
	      if(word.trim().length() > 0) {
	    	  words.add(word.toLowerCase().trim());
	      }
	    }
	    return words;
	    
	}

}
