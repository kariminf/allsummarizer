/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2013-2015 Abdelkrime Aries <kariminfo0@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kariminf.as.preProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import kariminf.langpi.basic.arabic.*;
import kariminf.langpi.basic.bulgarian.*;
import kariminf.langpi.basic.catalan.*;
import kariminf.langpi.basic.czech.*;
import kariminf.langpi.basic.german.*;
import kariminf.langpi.basic.greek.*;
import kariminf.langpi.basic.english.*;
import kariminf.langpi.basic.spanish.*;
import kariminf.langpi.basic.basque.*;
import kariminf.langpi.basic.persian.*;
import kariminf.langpi.basic.finnish.*;
import kariminf.langpi.basic.french.*;
import kariminf.langpi.basic.hebrew.*;
import kariminf.langpi.basic.hindi.*;
import kariminf.langpi.basic.hungarian.*;
import kariminf.langpi.basic.indonesian.*;
import kariminf.langpi.basic.italian.*;
import kariminf.langpi.basic.japanese.*;
import kariminf.langpi.basic.dutch.*;
import kariminf.langpi.basic.nynorsk.*;
import kariminf.langpi.basic.norwegian.*;
import kariminf.langpi.basic.portuguese.*;
import kariminf.langpi.basic.romanian.*;
import kariminf.langpi.basic.russian.*;
import kariminf.langpi.basic.swedish.*;
import kariminf.langpi.basic.thai.*;
import kariminf.langpi.basic.turkish.*;
import kariminf.langpi.basic.chinese.*;
import kariminf.langpi.basic.def.*;
import kariminf.langpi.basic.BasicInfo;
import kariminf.langpi.basic.Normalizer;
import kariminf.langpi.basic.SWEliminator;
import kariminf.langpi.basic.Segmenter;
import kariminf.langpi.basic.Stemmer;
import kariminf.as.tools.Data;
import kariminf.ktoolja.plugins.JarLoader;



/**
 * Preprocess task
 * 
 * <p>Given a text in a certain language, this class is used to look for adequate 
 * methods to preprocess it.</p>
 * <p>PreProcessing consists of normalization (See {@link Normalizer}), sentences 
 * and word segmentation (See {@link Segmenter}), stemming (See {@link Stemmer})
 *  and stop words removing (See {@link SWEliminator}).
 * 
 * @author Abdelkrime Aries
 *
 */
public class StaticPreProcessor extends PreProcessor {
	
	// In multi-document summarization, we have many texts
	private List<String> texts = new ArrayList<String>();

	//Different preprocessing tasks
	private BasicInfo info = null;
	private Normalizer normalizer = null;
	private Segmenter segmenter = null;
	private SWEliminator sweliminator = null;
	private Stemmer stemmer = null;


	/**
	 * Creates a PreProcessor for a specific language
	 * 
	 * @param lang the language in ISO 639-1 code, eg. "en", "fr", "ar", etc.
	 * @param data The data container
	 */
	public StaticPreProcessor(String lang){
		super(lang);
		setLanguage(lang);
		
	}


	/**
	 * Changes or sets the language of the preprocessor
	 * 
	 * @param lang the language in ISO 639-1 code, eg. "en", "fr", "ar", etc.
	 */
	public void setLanguage(String lang){
		
		lang = lang.toUpperCase();
		
		ISO639_1 iso;
		
		try{
			iso = ISO639_1.valueOf(lang);
		} catch (IllegalArgumentException e){
			iso = ISO639_1.EN;
		}
		
		switch (iso){
		case AR:
			info = new ArInfo();
			System.out.println("arabic");
			normalizer = new ArNormalizer();
			segmenter = new ArSegmenter();
			sweliminator = new ArSWEliminator();
			stemmer = new ArStemmer();
			break;


		case BG:
			info = new BgInfo();
			normalizer = new BgNormalizer();
			segmenter = new BgSegmenter();
			sweliminator = new BgSWEliminator();
			stemmer = new BgStemmer();
			break;


		case CA:
			info = new CaInfo();
			normalizer = new CaNormalizer();
			segmenter = new CaSegmenter();
			sweliminator = new CaSWEliminator();
			stemmer = new CaStemmer();
			break;


		case CS:
			info = new CsInfo();
			normalizer = new CsNormalizer();
			segmenter = new CsSegmenter();
			sweliminator = new CsSWEliminator();
			stemmer = new CsStemmer();
			break;


		case DE:
			info = new DeInfo();
			normalizer = new DeNormalizer();
			segmenter = new DeSegmenter();
			sweliminator = new DeSWEliminator();
			stemmer = new DeStemmer();
			break;


		case EL:
			info = new ElInfo();
			normalizer = new ElNormalizer();
			segmenter = new ElSegmenter();
			sweliminator = new ElSWEliminator();
			stemmer = new ElStemmer();
			break;


		case EN:
			info = new EnInfo();
			normalizer = new EnNormalizer();
			segmenter = new EnSegmenter();
			sweliminator = new EnSWEliminator();
			stemmer = new EnStemmer();
			break;


		case ES:
			info = new EsInfo();
			normalizer = new EsNormalizer();
			segmenter = new EsSegmenter();
			sweliminator = new EsSWEliminator();
			stemmer = new EsStemmer();
			break;


		case EU:
			info = new EuInfo();
			normalizer = new EuNormalizer();
			segmenter = new EuSegmenter();
			sweliminator = new EuSWEliminator();
			stemmer = new EuStemmer();
			break;


		case FA:
			info = new FaInfo();
			normalizer = new FaNormalizer();
			segmenter = new FaSegmenter();
			sweliminator = new FaSWEliminator();
			stemmer = new FaStemmer();
			break;


		case FI:
			info = new FiInfo();
			normalizer = new FiNormalizer();
			segmenter = new FiSegmenter();
			sweliminator = new FiSWEliminator();
			stemmer = new FiStemmer();
			break;


		case FR:
			info = new FrInfo();
			normalizer = new FrNormalizer();
			segmenter = new FrSegmenter();
			sweliminator = new FrSWEliminator();
			stemmer = new FrStemmer();
			break;


		case HE:
			info = new HeInfo();
			normalizer = new HeNormalizer();
			segmenter = new HeSegmenter();
			sweliminator = new HeSWEliminator();
			stemmer = new HeStemmer();
			break;


		case HI:
			info = new HiInfo();
			normalizer = new HiNormalizer();
			segmenter = new HiSegmenter();
			sweliminator = new HiSWEliminator();
			stemmer = new HiStemmer();
			break;


		case HU:
			info = new HuInfo();
			normalizer = new HuNormalizer();
			segmenter = new HuSegmenter();
			sweliminator = new HuSWEliminator();
			stemmer = new HuStemmer();
			break;


		case ID:
			info = new IdInfo();
			normalizer = new IdNormalizer();
			segmenter = new IdSegmenter();
			sweliminator = new IdSWEliminator();
			stemmer = new IdStemmer();
			break;


		case IT:
			info = new ItInfo();
			normalizer = new ItNormalizer();
			segmenter = new ItSegmenter();
			sweliminator = new ItSWEliminator();
			stemmer = new ItStemmer();
			break;


		case JA:
			info = new JaInfo();
			normalizer = new JaNormalizer();
			segmenter = new JaSegmenter();
			sweliminator = new JaSWEliminator();
			stemmer = new JaStemmer();
			break;


		case NL:
			info = new NlInfo();
			normalizer = new NlNormalizer();
			segmenter = new NlSegmenter();
			sweliminator = new NlSWEliminator();
			stemmer = new NlStemmer();
			break;


		case NN:
			info = new NnInfo();
			normalizer = new NnNormalizer();
			segmenter = new NnSegmenter();
			sweliminator = new NnSWEliminator();
			stemmer = new NnStemmer();
			break;


		case NO:
			info = new NoInfo();
			normalizer = new NoNormalizer();
			segmenter = new NoSegmenter();
			sweliminator = new NoSWEliminator();
			stemmer = new NoStemmer();
			break;


		case PT:
			info = new PtInfo();
			normalizer = new PtNormalizer();
			segmenter = new PtSegmenter();
			sweliminator = new PtSWEliminator();
			stemmer = new PtStemmer();
			break;


		case RO:
			info = new RoInfo();
			normalizer = new RoNormalizer();
			segmenter = new RoSegmenter();
			sweliminator = new RoSWEliminator();
			stemmer = new RoStemmer();
			break;


		case RU:
			info = new RuInfo();
			normalizer = new RuNormalizer();
			segmenter = new RuSegmenter();
			sweliminator = new RuSWEliminator();
			stemmer = new RuStemmer();
			break;


		case SV:
			info = new SvInfo();
			normalizer = new SvNormalizer();
			segmenter = new SvSegmenter();
			sweliminator = new SvSWEliminator();
			stemmer = new SvStemmer();
			break;


		case TH:
			info = new ThInfo();
			normalizer = new ThNormalizer();
			segmenter = new ThSegmenter();
			sweliminator = new ThSWEliminator();
			stemmer = new ThStemmer();
			break;


		case TR:
			info = new TrInfo();
			normalizer = new TrNormalizer();
			segmenter = new TrSegmenter();
			sweliminator = new TrSWEliminator();
			stemmer = new TrStemmer();
			break;


		case ZH:
			info = new ZhInfo();
			normalizer = new ZhNormalizer();
			segmenter = new ZhSegmenter();
			sweliminator = new ZhSWEliminator();
			stemmer = new ZhStemmer();
			break;


		default:
			//info = new DefInfo();
			normalizer = new DefNormalizer();
			segmenter = new DefSegmenter();
			sweliminator = new DefSWEliminator();
			stemmer = new DefStemmer();
			break;

		}


	}

	/**
	 * Clears the texts in multi-document preprocessing
	 */
	public void clearText(){
		texts.clear();
	}

	/**
	 * Adds a text to the texts to be preprocessed
	 * 
	 * @param text the text to add
	 */
	public void addText(String text){
		texts.add(text);	
	}

	/**
	 * Preprocess all the entered texts using the method 
	 * {@link StaticPreProcessor#addText(String)}
	 */
	public void preProcess(){

		for (String text: texts){
			text = normalizer.normalize(text);
			List<String> sentInText = segmenter.splitToSentences(text);
			data.addSentences(sentInText);
		}

		List<List<String>> sentWords = PreProcess(data.getSentences());
		data.setSentWords(sentWords);

	}
	
	/*
	public void preProcess(){

		List<String> sentences = new ArrayList<String>();
		HashMap<Integer, Integer> sentPos = new HashMap<Integer, Integer>();

		int sentMergePos = 0;
		for (String text: texts){
			text = normalizer.normalize(text);
			List<String> sentInText = segmenter.splitToSentences(text);
			int textPos = - 1 - sentMergePos;
			sentPos.put(textPos, sentInText.size());
			for (int i = 1; i <= sentInText.size(); i++){
				sentPos.put(sentMergePos, i);
				sentMergePos++;
			}
			sentences.addAll(sentInText);
		}


		List<List<String>> sentWords = PreProcess(sentences);

		data.setSentPos(sentPos);
		data.setSentences(sentences);
		data.setSentWords(sentWords);

	}*/


	/**
	 * Preprocess a list of sentences; here the sentence segmentation task is not used
	 * 
	 * @param Sents list of sentences
	 * @return list of sentences where each sentence is a list of preprocessed words
	 */
	public List<List<String>> PreProcess (List<String> Sents){

		List<List<String>> sentWords = new ArrayList<List<String>>();
		//number of words in the sentences including stop-words
		List<Integer> nbrWords = new ArrayList<Integer>(); 

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


	/**
	 * Preprocess a given text
	 * 
	 * @param inTxt text to be preprocessed; the result can be found in the 
	 * {@link Data} object given in the constructor 
	 * (See {@link StaticPreProcessor#PreProcessor(String, Data)}
	 */
	public void preProcess(String inTxt){

		inTxt = normalizer.normalize(inTxt);

		List<String> sentences = segmenter.splitToSentences(inTxt);
		List<List<String>> sentWords = PreProcess(sentences);

		data.addSentences(sentences);
		data.setSentWords(sentWords);


	}
	
}
