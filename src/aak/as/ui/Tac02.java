package aak.as.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import aak.as.preProcess.PreProcessor;
import aak.as.process.extraction.Summarizer;
import aak.as.process.extraction.bayes.Pos;
import aak.as.process.extraction.bayes.RLeng;
import aak.as.process.extraction.bayes.TFB;
import aak.as.process.extraction.cluster.Cluster;
import aak.as.process.extraction.cluster.NaiveCluster;
import aak.as.tools.Data;
import aak.as.tools.FileManager;
import aak.as.tools.Tools;

public class Tac02 {

	private static String tacfolder = "tac11/src/";
	private static String peerfolder = "tac11/peer/";
	private static Double Threshold = 0.12;
	
	private static String [][] language = {
		{"Arabic", "ar"}, {"Czech", "cz"},
		{"English", "en"}, {"French", "fr"}
		};
	private static String readFile(File f) {
	    try {
	      String contents = "";
	      int index = 0;

	      BufferedReader input = new BufferedReader(new FileReader(f));
	      for(String line = input.readLine(); line != null; line = input.readLine()) {
	        if (line.trim().length() > 5)
	        	index++;
	        else
	        	continue;
	        
	        if (index > 2)
	        	contents += " " + line + "\n";
	      }
	      input.close();

	      return contents;
	      
	    } catch(IOException e) {
	      e.printStackTrace();
	      System.exit(1);
	      return null;
	    } 
	  }
	
	public static String getSummary(List<List<String>> sentWords, List<Integer> nbrWords,
			List<String> sentences, List<Integer> order){
		String summary = "";
		int numWords = 0;
		int numOrder = 0;
		
		while(true){
			
			if (numOrder >=  order.size())
				break;
			
			int index = order.get(numOrder);
			
			if (numOrder > 0){
				List<String> prevWords = sentWords.get(order.get(numOrder-1));
				List<String> actWords = sentWords.get(index);
				if (Tools.similar(prevWords, actWords, 0.5)){
					numOrder ++;
					if (numOrder < order.size())
						index = order.get(numOrder);
				}
			}
			
			if (nbrWords.get(index) ==0){
				numOrder ++;
				continue;
			}
				
				
			numWords += nbrWords.get(index);
			
			
			if (numWords > 240){
				if (numWords <= 250){
					summary += sentences.get(index) + "\n";
					break;
				}
				else{
					numOrder ++;
					continue;
				}
			}
				
			summary += sentences.get(index) + "\n";
			
			numOrder ++;
		}
		
		/*for(int i=0; i< nbrSent; i++)
			summary += sentences.get(order.get(i));*/
		return summary;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		/*File[] flist = tacfolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
            	return file.getPath().toLowerCase().endsWith(".jar");
            	//return file.getPath().matches(tacfolder.getPath() + "/M000[01].*");
            	}
        });

		for (int i=0; i < flist.length; i++){
			System.out.println("File[" + i + "] = " + flist[i].getPath());
			System.out.println(readFile(flist[i]));
		}*/
	for (int count =0; count < language.length; count++){	
		
		String lang =  language[count][0];
		String accro = language[count][1];
		
		Data data= new Data();
		PreProcessor pre = new PreProcessor(accro, data);
		
		for (int i =0; i< 10; i++){ //10 topics
			
			/*File[] en = tacfolder.listFiles(new FileFilter() {
	            public boolean accept(File file) {
	            	return file.getPath().matches(".*M00"+ topicCount +".\\.english");
	            	}
	        });*/
			
			//System.out.println("-------------");
			for (int j=0; j< 10; j++)
				pre.addText(readFile(new File(tacfolder + "M00" + i + "" + j + "." + lang.toLowerCase())));
			
			
			pre.preProcess(); //Here data is filled with sentences and their features

			Cluster cluster = new NaiveCluster(Threshold, data);
			
			cluster.createClasses();
			
			Summarizer summarizer = new Summarizer();
			summarizer.addFeature(new TFB());
			summarizer.addFeature(new RLeng());
			summarizer.addFeature(new Pos());
			summarizer.summarize(data);
			
			//String //240-250 words
			String summary = getSummary(data.getSentWords(), data.getNbrWords(),
					data.getSentences(), summarizer.getOrdered());
			
			try {
				FileManager.saveFile(peerfolder + "AS12/"+ lang + "/M00" + i, summary);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}//language iteration
	}

}
