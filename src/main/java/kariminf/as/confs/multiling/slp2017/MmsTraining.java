package kariminf.as.confs.multiling.slp2017;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import kariminf.as.tools.Data;
import kariminf.as.tools.Tools;

public class MmsTraining {
	
	
	
	
	
	private String readFile(String file){
		StringBuffer content = new StringBuffer();

		int i = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ( (line = in.readLine()) != null) {
				if (line.length() < 1)
					continue;
				i++;
				if (i>2) //don't take the title and the date
					content.append(line + " ");
			}
			in.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return "";
		}

		return content.toString();
	}
	
	
private String getSummary(Data data, List<Integer> order, double simTH){

		
		if (lang.startsWith("zh"))
			return getChineseSummary (data, order, simTH);
		
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();

		String summary = "";
		int numChars = 0;
		int numOrder = 0;

		while(true){

			if (numOrder >=  order.size())
				break;

			int index = order.get(numOrder);

			if (numOrder > 0){
				List<String> prevWords = sentWords.get(order.get(numOrder-1));
				List<String> actWords = sentWords.get(index);
				if (Tools.similar(prevWords, actWords, simTH)){
					numOrder ++;
					if (numOrder < order.size())
						index = order.get(numOrder);
				}
			}
			
			numChars += data.getNbrWords(index);
			
			if (numChars > summarySize)
				break;
			
			summary += sentences.get(index) + "\n";

			numOrder ++;
		}
		
		if(summary.length() < 1)
			summary = sentences.get(order.get(0)).substring(0, summarySize);

		return summary;
	}
	
	private String getChineseSummary (Data data, List<Integer> order, double simTH){
		List<String> sentences = data.getSentences();
		List<List<String>> sentWords = data.getSentWords();

		String summary = "";
		int numChars = 0;
		int numOrder = 0;

		while(true){

			if (numOrder >=  order.size())
				break;

			int index = order.get(numOrder);

			if (numOrder > 0){
				List<String> prevWords = sentWords.get(order.get(numOrder-1));
				List<String> actWords = sentWords.get(index);
				if (Tools.similar(prevWords, actWords, simTH)){
					numOrder ++;
					if (numOrder < order.size())
						index = order.get(numOrder);
				}
			}

			try {
				final byte[] utf8Bytes = sentences.get(index).getBytes("UTF-8");
				numChars += utf8Bytes.length;
			} catch (UnsupportedEncodingException e) {
				numChars += sentences.get(index).length();
			}

			if (numChars > zhSummarySize)
				break;
			
			summary += sentences.get(index) + "\n";

			numOrder ++;
		}

		return summary;
	}


}
