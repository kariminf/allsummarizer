package aak.as.confs.multiling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import aak.as.process.extraction.bayes.Feature;
import aak.as.process.extraction.bayes.PLeng;
import aak.as.process.extraction.bayes.Pos;
import aak.as.process.extraction.bayes.RLeng;
import aak.as.process.extraction.bayes.TFB;
import aak.as.process.extraction.bayes.TFU;
import aak.as.tools.Calculus;

public class MssTraining {
	
	private static final String mssFolder = 
			"/home/kariminf/Data/ATS/multilingMss2015Training/";
	
	private static final String [] langs = 
		{"en"};
	
	private static Feature [] features = {
		new TFU(),
		new TFB(),
		new Pos(),
		new RLeng(),
		new PLeng(),
	};
	
	private static String[] th_name = new String[6];
	private static double[] th_value = new double[6];


	private static HashMap<String, Integer> readSizes(String lang){
		HashMap<String, Integer> sizes =
				new HashMap<String, Integer>();

		File sizefile = new File(mssFolder + "target-length/" + lang + ".txt");

		if (! sizefile.exists()) return null;

		try {
			BufferedReader in = new BufferedReader(new FileReader(sizefile));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.trim().length() < 1) continue;
				
				String[] values = line.split(",");
				if (values.length<2) continue;
				
				sizes.put(values[0], new Integer(values[1]));
			}
			in.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return sizes;
		}
		
		return sizes;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		for (String lang: langs){
			MSS mss = new MSS(lang);
			HashMap<String, Integer> sizes = readSizes(lang);
			
			//System.out.println(sizes);
			
			File folder = new File(mssFolder + "body/text/" +  lang);
			if (! folder.exists()) continue;
			if (! folder.isDirectory()) continue;
			
			File[] files = folder.listFiles();
			for (File file: files){
				String fileName = file.getName();
				if (! fileName.endsWith("_body.txt")) continue;
				
				mss.preprocess(file);
				
				//This is used to store statistics
				{

					List<Double> sim = Calculus.delMultiple(mss.getSimilarity(), 0.0);

					th_name[0] = "mean"; th_value[0] = Calculus.mean(sim);
					th_name[1] = "median"; th_value[1] = Calculus.median(sim);
					th_name[2] = "variance"; th_value[2] = Calculus.variance(sim);
					th_name[3] = "Hmode"; th_value[1] = Calculus.modeHigh(sim);
					th_name[4] = "Lmode"; th_value[4] = Calculus.modeLow(sim);
					th_name[5] = "skewness"; th_value[5] = Calculus.skewness(sim);

				}
				
				for(int th=0;th<10;th++){//TODO modify this to specify thresholds
					
					
					
				} //thresholds
			
			} //files
			
		}//languages
		
		
	}

}
