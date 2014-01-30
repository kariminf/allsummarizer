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
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;


public class PreProcessor {
	
	private List<String> Sentences = new ArrayList<String>();
	private List<List<String>> SentWords = new ArrayList<List<String>>();
	
	public void preProcess(String inTxt){
		URLClassLoader ucl = null;
		try {
			URL[] jars= {new File("preProcess/en.jar").toURI().toURL()};
			ucl = new URLClassLoader(jars);
		} catch (MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		Normalizer normalizer = null;
		//{
			ServiceLoader<Normalizer> nsl = ServiceLoader.load(Normalizer.class, ucl);
			Iterator<Normalizer> nit = nsl.iterator();
			if (nit.hasNext())
				normalizer = nit.next();
		//}
		
		Segmenter segmenter = null;
		//{
			ServiceLoader<Segmenter> ssl = ServiceLoader.load(Segmenter.class, ucl);
			Iterator<Segmenter> sit = ssl.iterator();
			if (sit.hasNext())
				segmenter = sit.next();
		//}

		inTxt = normalizer.normalize(inTxt);
		
		//inTxt = inTxt.toLowerCase();
		
		Sentences = segmenter.splitToSentences(inTxt);
				
		
		SentWords = PreProcess(Sentences);
	}
	
	public static List<List<String>> PreProcess (List<String> Sents){
		
		URLClassLoader ucl =null;
		try {
			URL[] jars= {new File("preProcess/en.jar").toURI().toURL()};
			ucl = new URLClassLoader(jars);
		} catch (MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			
		Segmenter segmenter = null;
		{
			ServiceLoader<Segmenter> sl = ServiceLoader.load(Segmenter.class, ucl);
			Iterator<Segmenter> it = sl.iterator();
			if (it.hasNext())
				segmenter = it.next();
		}
		
		Stemmer stemmer = null;
		{
			ServiceLoader<Stemmer> sl = ServiceLoader.load(Stemmer.class, ucl);
			Iterator<Stemmer> it = sl.iterator();
			if (it.hasNext())
				stemmer = it.next();
		}
		
		SWEliminator swEliminator = null;
		{
			ServiceLoader<SWEliminator> sl = ServiceLoader.load(SWEliminator.class, ucl);
			Iterator<SWEliminator> it = sl.iterator();
			if (it.hasNext())
				swEliminator = it.next();
		}
		
		List<List<String>> sentWords = new ArrayList<List<String>>();
		
		
		for (String sent: Sents){
			List<String> segSent = segmenter.segmentWords(sent);
			
			swEliminator.deleteSW(segSent);
			
			segSent = stemmer.stemListWords(segSent);
			
			sentWords.add(segSent);
		}
		
		return sentWords;
	}
	
	public List<String> getSentences (){
		return Sentences;
	}
	
	public List<List<String>> getSentWords (){
		return SentWords;
	}
	
	public static void main(String[] args) {
		
		String input = "";
		input += "My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree. ";
		input += "My research in ESI is about ATS, it is the intersection between IR and NLP. ";
		input += "In this research, the main idea is to find relevant sentences using IR technics. ";
		input += "The statistical features are the power of IR to find relevancy. ";
		input += "AI technics are used, such as learning algorithms to create models for each topic in the input text. ";
		
		PreProcessor pp = new PreProcessor();
		
		pp.preProcess(input);
		
		System.out.println(pp.getSentWords().toString());
		
	}
	
}
