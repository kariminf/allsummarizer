package aak.thEstim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import aak.as.preProcess.PreProcessor;
import aak.as.process.extraction.Summarizer;
import aak.as.process.extraction.bayes.*;
import aak.as.process.extraction.cluster.*;
import aak.as.tools.*;

public class Duc04 {

	private static String duc04folder = "/home/kariminf/Data/ATS/duc2004/";
	private static String peerfolder = duc04folder + "peer/";
	private static Feature [] features = {
		new TFU(),
		new TFB(),
		new Pos(),
		new RLeng(),
		new PLeng(),
	};
	
	public static void main(String[] args) {
		
		File dir = new File(duc04folder + "source/");
		File [] topics  = dir.listFiles();
		
		for(File topic : topics){
			
			if (! topic.isDirectory())
				continue;
			
			System.out.println("topic: " + topic.getName());
			
			Data data = new Data();
			{
				PreProcessor preprocess = new PreProcessor("en", data);
				File [] files  = topic.listFiles();
				for (File file: files){
					String text = openFile(file.getAbsolutePath());
					preprocess.addText(text);
				}
				preprocess.preProcess();
			}
			
			String topicname = topic.getName();
				
			String[] th_name = new String[6];
				double[] th_value = new double[6];
			//This is used to store statistics
			{
	
				List<Double> sim = Calculus.delMultiple(data.getSimList(), 0.0);
				
				th_name[0] = "mean"; th_value[0] = Calculus.mean(sim);
				th_name[1] = "median"; th_value[1] = Calculus.median(sim);
				th_name[2] = "variance"; th_value[2] = Calculus.variance(sim);
				th_name[3] = "Hmode"; th_value[1] = Calculus.modeHigh(sim);
				th_name[4] = "Lmode"; th_value[4] = Calculus.modeLow(sim);
				th_name[5] = "skewness"; th_value[5] = Calculus.skewness(sim);
				
			}
			
			for(int th = 0; th < 6; th++){
				
				{
					System.out.println("threshold: " + th_value[th]);
					Cluster cluster = new NaiveCluster(th_value[th], data);
					cluster.createClasses();
				}
				
				for (int combNbr=1; combNbr <= features.length; combNbr++){
					List<List<Integer>> comb = 
							Calculus.getCombinations(features.length, combNbr);
					
					for (List<Integer> oneComb : comb){
						Summarizer summarizer = new Summarizer();
						String combStr = "";
						for (int index: oneComb){
							summarizer.addFeature(features[index]);
							combStr += features[index].getClass().getSimpleName() + "-";
						}
						combStr = combStr.substring(0, combStr.length()-1);
						
						FileManager.createFolder(peerfolder + combStr);
						
						
						//System.out.println("features: " + combStr);
						
						summarizer.summarize(data);
						List<Integer> order = summarizer.getSentNumber(5);
						String summary = "";
						
						
						int OrderIDX = 1;
						List<List<String>> sentWords = data.getSentWords();
						summary += 	data.getSentence(order.get(0)) + "\n";
						List<String> prevWords = sentWords.get(order.get(0));
						
						while (true){
							
							List<String> actWords = sentWords.get(order.get(OrderIDX));
							
							do {
								if(OrderIDX >= order.size()) break;
								actWords = sentWords.get(order.get(OrderIDX));
								OrderIDX++;
							} while (Tools.similar(prevWords, actWords, 0.5));
							
							if (OrderIDX >= order.size()) break;
							
							prevWords = actWords;
							summary += 	data.getSentence(order.get(OrderIDX-1)) + "\n";
							
							if (summary.length() >= 665) break;
						}
						
						if (summary.length() > 665)
							summary = summary.substring(0, 666);
						
						combStr += "/" + topicname ;
						FileManager.createFolder(peerfolder + combStr);
						combStr += "/" + th_name[th] + ".asz";
						try {
							FileManager.saveFile(peerfolder + combStr, summary);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
								
					}
				}// combinations
			}//threshold
			System.out.println("......................");
		}//files

	}
	
	public static String openFile(String filePath){
		StringBuffer content = new StringBuffer();
		
		try {
		      BufferedReader in = new BufferedReader(new FileReader(filePath));
		      String line;
		      boolean getit = false;
		      while ( (line = in.readLine()) != null) {
		    	  if (line.contains("<TEXT>")){
		    		  getit=true;
		    		  continue;
		    	  }
		    	  
		    	  if (line.contains("</TEXT>"))
		    		  break;
		    	  
		    	  if (getit)
		    		  content.append(line + " ");
		        
		        //sb.append("/n"); to make it more platform independent (Log July 12, 2004)
		        //content.append("&&&&&&&&&&&&");
		      }
		      in.close();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		      return "";
		    }
		    
		return content.toString();
	}

}
