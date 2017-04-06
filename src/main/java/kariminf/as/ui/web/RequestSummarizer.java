package kariminf.as.ui.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kariminf.as.preProcess.StaticPreProcessor;
import kariminf.as.process.Scorer;
import kariminf.as.process.topicclassif.BayesClassifier;
import kariminf.as.process.topicclassif.bayes.PLeng;
import kariminf.as.process.topicclassif.bayes.Pos;
import kariminf.as.process.topicclassif.bayes.RLeng;
import kariminf.as.process.topicclassif.bayes.TFB;
import kariminf.as.process.topicclassif.bayes.TFU;
import kariminf.as.process.topicclassif.cluster.Cluster;
import kariminf.as.process.topicclassif.cluster.NaiveCluster;
import kariminf.as.tools.Data;

public class RequestSummarizer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1257142632752308233L;
	
	private List<String> sentences;
	private List<Integer> orderNumSent = new ArrayList<Integer>();
	private List<Double> orderedScores = new ArrayList<Double>();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException {
		double th = 0.2;
		int nbrSent = 1;
		String featNames = "tfu, pos";
		String lang = "en";
		String text = "I am going to the market. The market is near the big red building.";
		
		Summarize(text, lang, th, featNames);
		
		String result = getSummaryNum(nbrSent);
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(result);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String text = request.getParameter("text");
		String lang = request.getParameter("lang");
		String thStr = request.getParameter("th");
		//String nbrSentStr = request.getParameter("nbs");
		//String[] features = request.getParameterValues("feat");
		String featNames = request.getParameter("feat");
		

		if (text == null || text.length() < 5){
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("Text not set");
			return;
		}
		
		if (lang == null || lang.length() < 2 ){
			lang = "en";
		}
		
		if (thStr == null || thStr.length() < 1 ){
			thStr = "0.2";
		}
		
		/*
		if (nbrSentStr == null || nbrSentStr.length() < 1 ){
			thStr = "2";
		}
		*/
		
		/*
		if (features == null || features.length < 1 ){
			features = new String[1];
			features[0] = "tfu";
		}
		*/
		
		if (featNames == null || featNames.length() < 3 ){
			featNames = "tfu";
		}
		
		double th = Double.valueOf(thStr);
		//int nbrSent = Integer.valueOf(nbrSentStr);
		//String featNames = Arrays.toString(features);
		
		Summarize(text, lang, th, featNames);
		
		//String result = getSummaryNum(nbrSent);
		String result = getOrderedSentences();
		
		result += "features=" + featNames + "& lang=" + lang + "& th=" + th + "<br>\n";
				
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(result);
		
	}
	
	
	public void Summarize(String text, String lang, double th, String featNames) throws IOException{
		
		Data data = new Data();
		
		// Pre-Processing
		{
			StaticPreProcessor preprocess = new StaticPreProcessor(lang, data);
			preprocess.preProcess(text);
		}
		
		/*
		if (data != null)
			throw new IOException("The text" + data.getSentences());
		*/
		
		// Processing: Clustering
		{
			Cluster cluster = new NaiveCluster(th, data);
			cluster.createClasses();
		}
		
		/*
		 if (data != null)
			throw new IOException("The classes nbr" + data.getClassesNumber());
		 */
		
		// Processing: Notation & Ordering
		{
			BayesClassifier bc = new BayesClassifier();
			Scorer summarizer = new Scorer(bc);
			addfeatures(bc, featNames);
			/*
			if (data != null)
				throw new IOException("features: " + summarizer.getNbrFeatures());
				*/
			summarizer.summarize(data);
			orderNumSent = summarizer.getOrdered();
			/*
			if (data != null)
				throw new IOException("ordered" + orderNumSent);
				*/
			sentences = data.getSentences();
			//sentences.add("");
			
			for (int order: orderNumSent)
				orderedScores.add(summarizer.getScore(order));
			
		}
		
	}
	
	public void addfeatures(BayesClassifier bc, String featNames){
		featNames = featNames.toLowerCase();
		
		if (featNames.contains("pleng"))
			bc.addFeature(new PLeng());
		
		if (featNames.contains("pos"))
			bc.addFeature(new Pos());
		
		if (featNames.contains("rleng"))
			bc.addFeature(new RLeng());
		
		if (featNames.contains("tfb"))
			bc.addFeature(new TFB());
		
		if (featNames.contains("tfu"))
			bc.addFeature(new TFU());
	}
	
	public String getSummaryNum(int nbrSent){
		String summary = "";
		int len = sentences.size();
		for(int i=0; i< nbrSent; i++){
			if (i >= len)
				return summary;
			summary += orderedScores.get(i) + ": ";
			summary += sentences.get(orderNumSent.get(i)) + "<br>";
		}
			
		return summary;
	}
	
	public String getOrderedSentences(){
		String summary = "";
		int len = sentences.size();
		for(int i=0; i< len; i++){
			summary += orderedScores.get(i) + ": ";
			summary += sentences.get(orderNumSent.get(i)) + "<br>\n";
		}
			
		return summary;
	}
	
	

}
