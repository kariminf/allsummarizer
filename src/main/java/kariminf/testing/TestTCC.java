package kariminf.testing;

import java.util.List;
import kariminf.as.process.Scorer;
import kariminf.as.process.tcc.BayesScoreHandler;
import kariminf.as.process.tcc.NaiveCluster;
import kariminf.as.process.tcc.Pos;
import kariminf.as.process.tcc.TFB;
import kariminf.as.tools.Data;

public class TestTCC {

	public static void main(String[] args) {
		Data data = TestPreProcess.pp();
		
		NaiveCluster nc = new NaiveCluster(0.25);
		BayesScoreHandler bsh = new BayesScoreHandler(nc);
		Scorer s = Scorer.create(bsh);
		s.setData(data);//calls bsh.setData(data)
		
		bsh.addFeature(new TFB());//calls TFB.setData(data);
		bsh.addFeature(new Pos());//calls Pos.setData(data)
		
		
		bsh.train();//must train before scoring
		s.scoreUnits();
		
		List<Integer> order = s.getOrdered();
		double sent1score = s.getScore(1);//sentence 1 score
		
		System.out.println(sent1score);
		

	}

}
