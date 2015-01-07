/*
 * AllSumarizer v2
 * This file is part of AllSummarizer project; an implementation of the method
 * described in this paper:
 * http://dx.doi.org/10.1117/12.2004001
 * 
 * Copyright (C) 2013  Abdelkrime Aries <kariminfo0@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package as.PreProcess;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import as.Tools.Data;


public class PreProcessor {
	
	// In multi-documents summarization, we have many texts
	private List<String> Texts = new ArrayList<String>();
	/*private List<String> Sentences = new ArrayList<String>();
	private List<List<String>> SentWords = new ArrayList<List<String>>();
	private List<Integer> nbrWords = new ArrayList<Integer>();
	private HashMap<Integer, Integer> SentPos = new HashMap<Integer, Integer>();*/
	Data data;
	
	private Normalizer normalizer = null;
	private Segmenter segmenter = null;
	private SWEliminator sweliminator = null;
	private Stemmer stemmer = null;
	
	
	public PreProcessor(String lang, Data data){
		setLanguage(lang);
		this.data = data;
	}
	
	private URLClassLoader loadLanguage(File file){
		URLClassLoader ucl = null;
		try {
			URL[] jars = {file.toURI().toURL()};
			ucl = new URLClassLoader(jars);
		} catch (MalformedURLException mue){
			mue.printStackTrace();
			System.exit(1);
		}
		
		return ucl;
	}
	
	public void setLanguage(String lang){
		
		URLClassLoader ucl = null;
		File plugin = new File("preProcess/" + lang + ".jar");
		
		if (!plugin.exists()){
			plugin = new File("preProcess/en.jar");
			System.out.println("Pre-procssing plugin of [" + lang + "], not found ... trying [en]");
		}
		
		ucl = loadLanguage(plugin);	
		
		{
			ServiceLoader<Normalizer> sl = ServiceLoader.load(Normalizer.class, ucl);
			Iterator<Normalizer> it = sl.iterator();
			it = sl.iterator();
			if (it.hasNext())
				normalizer = it.next();
		}
		

		{
			ServiceLoader<Segmenter> sl = ServiceLoader.load(Segmenter.class, ucl);
			Iterator<Segmenter> it = sl.iterator();
			if (it.hasNext())
				segmenter = it.next();
		}
		
		
		{
			ServiceLoader<SWEliminator> sl = ServiceLoader.load(SWEliminator.class, ucl);
			Iterator<SWEliminator> it = sl.iterator();
			if (it.hasNext())
				sweliminator = it.next();
		}
		
		{
			ServiceLoader<Stemmer> sl = ServiceLoader.load(Stemmer.class, ucl);
			Iterator<Stemmer> it = sl.iterator();
			if (it.hasNext())
				stemmer = it.next();
		}

	}
	
	public void clearText(){
		Texts.clear();
	}
	
	public void addText(String text){
		Texts.add(text);	
	}
	
	public void preProcess(){
		
		List<String> sentences = new ArrayList<String>();
		HashMap<Integer, Integer> sentPos = new HashMap<Integer, Integer>();
		
		int sentMergePos = 0;
		for (String text: Texts){
			text = normalizer.normalize(text);
			List<String> sentInText = segmenter.splitToSentences(text);
			int textPos = - 1 - sentMergePos;
			sentPos.put(textPos, sentInText.size());
			for (int i=1; i<= sentInText.size(); i++){
				sentPos.put(sentMergePos, i);
				sentMergePos++;
			}
			sentences.addAll(sentInText);
		}
		
		
		List<List<String>> sentWords = PreProcess(sentences);
		
		//System.out.println("====>" + sentPos.size());
		data.setSentPos(sentPos);
		data.setSentences(sentences);
		data.setSentWords(sentWords);
		
	}
	
	
	public List<List<String>> PreProcess (List<String> Sents){

		List<List<String>> sentWords = new ArrayList<List<String>>();
		List<Integer> nbrWords = new ArrayList<Integer>(); //number of words in the sentences including stop-words
		
		for (String sent: Sents){
			List<String> segSent = segmenter.segmentWords(sent);
			
			nbrWords.add(segSent.size());
			
			sweliminator.deleteSW(segSent);
			
			segSent = stemmer.stemListWords(segSent);
			
			sentWords.add(segSent);
		}
		
		data.setNbrWords(nbrWords);
		
		return sentWords;
	}
	

	public void preProcess(String inTxt){

		inTxt = normalizer.normalize(inTxt);
		
		List<String> sentences = segmenter.splitToSentences(inTxt);
		HashMap<Integer, Integer> sentPos = new HashMap<Integer, Integer>();
		List<List<String>> sentWords = PreProcess(sentences);

		sentPos.put(-1, sentences.size());
		for (int i=0; i< sentences.size(); i++)
			sentPos.put(i, i+1);
		
		data.setSentences(sentences);
		data.setSentPos(sentPos);
		data.setSentWords(sentWords);
	}
	
	
	
	public static void main(String[] args) {
		
		/**/String input = "";
		input += "My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree. ";
		input += "My research in ESI is about ATS, it is the intersection between IR and NLP. ";
		input += "In this research, the main idea is to find relevant sentences using IR technics. ";
		input += "The statistical features are the power of IR to find relevancy. ";
		input += "AI technics are used, such as learning algorithms to create models for each topic in the input text. ";
		/**/
		
		/*
		String input = "";
		input += " اسمي هو كريم، وأنا أدرس المعلوماتية في المدرسة العليا للإعلام الآلي، التي تقع في الجزائر العاصمة، لأتحصل على شهادة الماجستير. ";
		input += " بحثي في ESI عن التلخيص الآلي، والذي يشكل التقاطع بين IR و NLP . ";
		input += " في هذا البحث، الفكرة الرئيسية هي إيجاد الجمل المناسبة باستعمال تقنيات IR. ";
		input += " الميزات الإحصائية تمثل قوة IR في إيجاد مدى التناسب. ";
		input += " تقنيات AI تستعمل أيضا، مثل لوغارتمات التعلم لإنشاء نماذج لكل موضوع في النص المدخل.";
		*/
		
		/*String input = "";
		input += "Mon nom est Karim, et j'étude l'informatique à l'ESI, qui se situe à Alger, pour obtenir le diplôme de Magister. ";
		input += "Mon recherche à l'ESI est sur le RAT, qui est l'intersection entre la RI et le TALN. ";
		input += "Dans ce recherche, l'idée principale est de trouver les phrses pertinentes en utilisant les techniques de RI. ";
		input += "Les caractéristiques statistiques sont le point fort de RI pour trouver la pertinence. ";
		input += "Les techniques de l'IA sont utilisées, comme les algorithmes d'apprentissage afin de créer des models pour chaque thème dans le texte d'entrée. ";
		*/
		
		/*String input = "";
		input += "Mé jméno je Karim, a studuji informatiku v ESI, která je v Alžíru, získat magisterský stupeň. ";
		input += "Můj výzkum ve ESI je o ATS, to je křižovatka mezi IR a NLP. ";
		input += "V tomto výzkumu, hlavní myšlenkou je najít relevantní věty pomocí infračervené techniky. ";
		input += "Statistické vlastnosti jsou síla IR najít relevantní. ";
		input += "AI techniky jsou používány, jako je učení algoritmů vytvořit modely pro každé téma ve vstupním textu.";
		*/
		
		//String input = "I am studying at home.";
		Data data = new Data();
		
		PreProcessor pp = new PreProcessor("en", data);
		
		//pp.preProcess(input);
		
		pp.addText(input);
		pp.preProcess();
		
		List<List<String>> g = new ArrayList<List<String>>();
		
		Object ll = (Object) g;
		
		System.out.println("class=" + ll.getClass().getName());
		
		
		
		//System.out.println(pp.getSentWords().toString());
		
		List<List<String>> l = data.getSentWords();
		List<Integer> n = data.getNbrWords();
		
		int i=0;
		for (List<String> s: l){
			System.out.println(n.get(i) + "=" + s.toString());
			i++;
		}
			
		
	}
	
}
