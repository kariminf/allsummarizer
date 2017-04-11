package kariminf.as.process.slp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import kariminf.as.tools.Data;

public class SLPScoreHandlerTest extends SLPScoreHandler {
	
	private static Data data;

	@BeforeClass
	public static void setUp() throws Exception {
		data = new Data();
		List<String> sentences = new ArrayList<String>();
		sentences.add("aa aa bb cc");
		sentences.add("aa bb bb bb dd");
		sentences.add("dd ee ee");
		sentences.add("bb cc cc cc ee ff");
		data.addSentences(sentences);
		
		List<List<String>> sentWords = new ArrayList<>();
		
		for(String sentence: sentences){
			List<String> wordsList = Arrays.asList(sentence.split(" "));
			sentWords.add(wordsList);
		}
		
		data.setSentWords(sentWords);
		
		for (int i = 0; i < 4; i++)
			for (int j = i+1; j < 4; j++)
				System.out.printf("sim(%d,%d)= %f\n", i, j,data.getSimilarity(i, j));
		
	}

	@Test
	public void setThresholdSimilarityTest() {
		double threshold = 0.25;
		SLPScoreHandler slpSH = new SLPScoreHandlerTest();
		slpSH.setThresholdSimilarity(threshold);
		assertEquals(threshold, slpSH.getThresholdSimilarity(), 0.0001);
	}
	
	@Test
	public void calculateSimilarityTest(){
		
		SLPScoreHandler slpSH = new SLPScoreHandlerTest();
		slpSH.calculateSimilarity(data);
		
		HashMap<Integer, Integer> _simSentID =  new HashMap<>();
		
		_simSentID.put(0, 1);
		_simSentID.put(1, 0);
		_simSentID.put(2, 3);
		_simSentID.put(3, 0);
		
		assertEquals(_simSentID, slpSH.simSentID);
		
		double[] _sentSimDoc = new double[]{
				0.580947,
				0.458682,
				0.184288,
				0.445566
		};
		
		assertArrayEquals(_sentSimDoc, 
				slpSH.sentSimDoc.stream().mapToDouble(Double::doubleValue).toArray(), 0.0001);
		
		
		//assertEquals(_sentSimDoc, slpSH.sentSimDoc);
		
		slpSH.setThresholdSimilarity(0.4);
		slpSH.calculateSimilarity(data);
		
		_simSentID = new HashMap<>();
		_simSentID.put(0, 1);
		_simSentID.put(1, 0);
		_simSentID.put(3, 0);
		
		assertEquals(_simSentID, slpSH.simSentID);
		
		_sentSimDoc = new double[]{
				0.682288,
				0.473049,
				0.0,
				0.388888
		};
		
		//System.out.println(slpSH.sentSimDoc);
		
		assertArrayEquals(_sentSimDoc, 
				slpSH.sentSimDoc.stream().mapToDouble(Double::doubleValue).toArray(), 0.0001);
		
	}
	
	
	@Test
	public void getSLPScoreTest(){
		SLPScoreHandler slpSH = new SLPScoreHandlerTest();
		
		double[] _expectedScores = new double[]{
				0.486701,
				0.0,
				0.0,
				0.0
		};
		
		double[] _realScores = new double[data.getSentNumber()];
		
		for (int unitID = 0; unitID < data.getSentNumber(); unitID++){
			_realScores[unitID] = slpSH.getSLPScore(data, unitID);
		}
		
		assertArrayEquals(_expectedScores, _realScores, 0.0001);
		
	}
	
	
	@Override
	public Double scoreUnit(Data data, int unitID) {
		return 0.0;
	}

}
