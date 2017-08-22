package kariminf.as.process.slp;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import kariminf.as.process.ssfgc.SSFScoreHandler;
import kariminf.as.tools.Data;

public class SSFScoreHandlerTest  {
	
	private static class TstSSF extends SSFScoreHandler {
		public TstSSF(Data data, double thSimilarity) {
			super(thSimilarity);
			this.setData(data);
		}
		
		@Override
		public Double scoreUnit(int unitID) {
			return 0.0;
		}
	}
	


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
		SSFScoreHandler ssfSH = new TstSSF(data, threshold);
		assertEquals(threshold, ssfSH.getThresholdSimilarity(), 0.0001);
	}
	
	@Test
	public void calculateSimilarityTest(){
		
		SSFScoreHandler ssfSH = new TstSSF(data, 0.0);
		
		HashMap<Integer, Integer> _simSentID =  new HashMap<>();
		
		_simSentID.put(0, 1);
		_simSentID.put(1, 0);
		_simSentID.put(2, 3);
		_simSentID.put(3, 0);
		
		
		assertEquals(_simSentID, ssfSH.getSimMaxSentID());
		
		double[] _sentSimDoc = new double[]{
				0.580947,
				0.458682,
				0.184288,
				0.445566
		};
		
		assertArrayEquals(_sentSimDoc, 
				ssfSH.getSentSimDoc().stream().mapToDouble(Double::doubleValue).toArray(), 0.0001);
		
		
		//assertEquals(_sentSimDoc, slpSH.sentSimDoc);
		
		ssfSH.setThresholdSimilarity(0.4);
		
		_simSentID = new HashMap<>();
		_simSentID.put(0, 1);
		_simSentID.put(1, 0);
		_simSentID.put(3, 0);
		//Not a candidate sentence, but its max is calculated anyway
		_simSentID.put(2, 3);
		
		assertEquals(_simSentID, ssfSH.getSimMaxSentID());
		
		_sentSimDoc = new double[]{
				0.6154574548966636,
				0.6154574548966636,
				0.0, //Not a candidate sentence
				0.0 //Not a candidate sentence
		};
		
		//System.out.println(ssfSH.getSentSimDoc());
		
		assertArrayEquals(_sentSimDoc, 
				ssfSH.getSentSimDoc().stream().mapToDouble(Double::doubleValue).toArray(), 0.0001);
		
	}
	
	
	@Test
	public void getSLPScoreTest(){
		SSFScoreHandler ssfSH = new TstSSF(data, 0.0);
		
		double[] _expectedScores = new double[]{
				0.31154,
				0.10791,
				0.09986,
				0.41597
		};
		
		
		
		
		double[] _realScores = new double[data.getSentNumber()];
		
		
		ssfSH.calculateSSFScores();
		
		for (int unitID = 0; unitID < data.getSentNumber(); unitID++){
			_realScores[unitID] = ssfSH.getSSFScore(unitID);
		}
		
		assertArrayEquals(_expectedScores, _realScores, 0.0001);
		
		
		_expectedScores = new double[]{
				0.36286,
				0.27460,
				Double.NEGATIVE_INFINITY, //not candidate
				Double.NEGATIVE_INFINITY, //not candidate
		};
		
		ssfSH.setThresholdSimilarity(0.4);
		ssfSH.calculateSSFScores();
		for (int unitID = 0; unitID < data.getSentNumber(); unitID++){
			_realScores[unitID] = ssfSH.getSSFScore(unitID);
			System.out.println("ssf" + ssfSH.getSSFScore(unitID));
			
		}
		
		assertArrayEquals(_expectedScores, _realScores, 0.0001);
		
		
	}

}
