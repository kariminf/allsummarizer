package aak.as.preProcess.def;

import java.util.HashMap;

import aak.as.preProcess.lang.Normalizer;

public class DefNormalizer implements Normalizer {

	@Override
	public String normalize(String text) {
		return text;
	}

	@Override
	public String normalize(String text, String param) {
		return text;
	}

	@Override
	public HashMap<String, String> getParameters() {
		return null;
	}

}
