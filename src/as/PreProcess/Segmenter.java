package as.PreProcess;

import java.util.List;

public interface Segmenter {
	public List<String> splitToSentences(String text);
	public List<String> segmentWords(String text);
}
