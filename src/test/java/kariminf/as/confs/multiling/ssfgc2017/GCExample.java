package kariminf.as.confs.multiling.ssfgc2017;

import java.util.List;

import kariminf.as.postProcess.extraction.SimpleReOrderer;
import kariminf.as.postProcess.extraction.ThresholdReOrderer;
import kariminf.as.postProcess.extraction.GraphReOrderer;
import kariminf.as.postProcess.extraction.SimNeighborReOrderer;
import kariminf.as.postProcess.extraction.DiffNeighborReOrderer;
import kariminf.as.postProcess.extraction.Extractor;
import kariminf.as.postProcess.extraction.Neighbors2ReOrderer;
import kariminf.as.preProcess.StaticPreProcessor;
import kariminf.as.process.Scorer;
import kariminf.as.process.ssfgc.GC1ScoreHandler;
import kariminf.as.process.ssfgc.GC2ScoreHandler;
import kariminf.as.process.ssfgc.GC3ScoreHandler;
import kariminf.as.process.ssfgc.GC4ScoreHandler;
import kariminf.as.process.ssfgc.SSFScoreHandler;
import kariminf.as.tools.Data;
import kariminf.ktoolja.math.Calculus;

public class GCExample {

	public static void main(String[] args) {
		String text = "Ōkami has the player controlling the main character, Amaterasu, in a woodcut, watercolor style, cel-shaded environment, which looks like an animated Japanese ink-illustration (known as sumi-e) with other styles of art. ";

		text += "The gameplay style is a mix of action, platform, and puzzle gaming genres, and has been noted by many reviewers to have numerous similarities in overall gameplay style to The Legend of Zelda series, an inspiration that director Hideki Kamiya, a self-proclaimed Zelda-fan, has admitted has influenced his general game design. ";

		text += "The main story is primarily linear, directed through by Amaterasu's guide Issun, though numerous side quests and optional activities allow for players to explore the game world and take the story at their own pace. ";

		text += "By completing quests, side quests and small additional activities (such as making trees bloom into life or feeding wild animals), Amaterasu earns Praise, which can then be spent to increase various statistics of the character, such as the amount of health and number of ink wells for Celestial Brush techniques. ";

		text += "Combat is staged in a ghostly virtual arena, and Amaterasu can fight enemies using a combination of weapons, fighting techniques and Brush methods to dispatch the foes. ";
		
		text += "At the end of combat, money (as yen) is rewarded to Amaterasu, with bonuses for completing a battle quickly and without taking damage. ";

		text += "The money can be spent on numerous items from merchants across the land, including healing goods, better weapons, tools and key items for completing quests. ";

		text += "The money can also be used to buy new combat techniques at dojos throughout the land. ";

		text += "Additionally, rare Demon Fangs can be earned through combat which can be traded for additional, unique items that are beneficial in gameplay but not required to complete the game. ";

		text += "Weapons inspired by the Imperial Regalia of Japan, the Reflector, the Rosaries and the Glaive can be equipped on Amaterasu as either a main or sub-weapon, and used in addition to other melee attacks that the player can have Amaterasu learn through the course of the game.";

		
		Data data = new Data();
		StaticPreProcessor preprocessor = new StaticPreProcessor("en");
		preprocessor.setData(data);
		preprocessor.preProcess(text);
		
		System.out.println(data.getSentWords());
		
		System.exit(0);
		
		
		/*for(int i = 0; i < 10; i++){
			for(int j = i+1; j < 10; j++){
				System.out.println("sim(s" + i + ", s" + j + ") = " + data.getSimilarity(i, j));
			}
		}*/
		
		
		
		List<Double> sim = Calculus.delMultiple(data.getSimList(), 0.0);
		
		//System.out.println("mean = " + Calculus.mean(sim));
		
		//System.exit(0);
		
		//SSFScoreHandler ssh = new GC4ScoreHandler(data, Calculus.mean(sim), true);
		double th = Calculus.mean(sim);
		SSFScoreHandler ssh = new GC1ScoreHandler(th);
		ssh.setData(data);
		
		//ssh.setNormalization(false, false);
		ssh.calculateSSFScores();
		Scorer scorer = Scorer.create(ssh);
		scorer.setData(data);
		scorer.scoreUnits();
		
		Extractor reorder;
		//reorder = new ReOrderer0(scorer);
		//reorder = new ReOrderer1(scorer, th);
		//reorder = new ReOrderer2(scorer);
		//reorder = new ReOrderer3(scorer);
		//reorder = new ReOrderer4(scorer);
		reorder = new Neighbors2ReOrderer(scorer);
		
		reorder.reOrder();
		
		System.out.println("Order:" + reorder.getOrder());
		
		//ssh.converge();
		//System.exit(0);
		/*
		for(int i = 0; i < 10; i++){
			try {
				//System.out.println("SSF-GC5(" + i + ") = " + ssh.scoreUnit(i));
				//System.out.println("tf*isf(" + i + ") = " + ssh.scoreTfIsf(i));
				//System.out.println("sim(" + i + ", C) = " + ssh.getSentCandidateSim(i));
				//System.out.println("size(" + i + ") = " + data.getSentWords().get(i).size());
				//System.out.println("maxSim(" + i + ") = " + ssh.getMostSimilar(i));
				//System.out.println("SSF(" + i + ") = " + ssh.getSSFScore(i));
				
			} catch (Exception e) {
				System.out.println("Sentence " + i + " not a candidate");
			}
			
		}*/
		
	}

}

