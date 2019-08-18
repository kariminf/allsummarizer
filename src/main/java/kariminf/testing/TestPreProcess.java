package kariminf.testing;

import java.util.List;

import kariminf.as.preProcess.PreProcessor;
import kariminf.as.preProcess.StaticPreProcessor;
import kariminf.as.tools.Data;

public class TestPreProcess {

	public static void main(String[] args) {
		
		String text = "";
		text += "My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree. ";
		text += "My research in ESI is about ATS, it is the intersection between IR and NLP. ";
		text += "In this research, the main idea is to find relevant sentences using IR technics. ";
		text += "The statistical features are the power of IR to find relevancy. ";
		text += "AI technics are used, such as learning algorithms to create models for each topic in the input text. ";
		
		Data data = new Data();
		PreProcessor prep = new StaticPreProcessor("en");
		prep.setData(data);
		prep.addText(text);
		prep.preProcess();
		
		
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();
		List<List<Double>> sim = data.getSentSimilarities();
		
		System.out.println(sim);
		

	}
	
	public static Data pp() {
		String text = "";
		text += "My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree. ";
		text += "My research in ESI is about ATS, it is the intersection between IR and NLP. ";
		text += "In this research, the main idea is to find relevant sentences using IR technics. ";
		text += "The statistical features are the power of IR to find relevancy. ";
		text += "AI technics are used, such as learning algorithms to create models for each topic in the input text. ";
		
		Data data = new Data();
		PreProcessor prep = new StaticPreProcessor("en");
		prep.setData(data);
		prep.addText(text);
		prep.preProcess();
		
		return data;
	}

}
