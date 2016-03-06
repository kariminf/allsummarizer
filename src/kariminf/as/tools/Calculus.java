/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2015 Abdelkrime Aries <kariminfo0@gmail.com>
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

package kariminf.as.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kariminf.as.tools.Calculus_T;


/**
 * This class contains static methods used for calculus, such as combination and median.
 * 
 * @author Abdelkrime Aries
 *
 */
public class Calculus {

	/**
	 * Calculates the factorial of a natural number. 
	 * If the number is less or equal to 1, it returns 1.
	 * 
	 * @param num a natural number
	 * @return the factorial of this number
	 */
	public static int factorial(int num) {
		
		if (num <= 1) return 1;
		
		int result = num * factorial(num - 1);

		return result;
	}

	
	/**
	 * Calculates the combination of a given size from a total number of elements.
	 * 
	 * @param total total number of elements in a set
	 * @param size size of combination
	 * @return number of possible combinations
	 */
	public static int combination(int total, int size) {

		if (total < size)
			return 0;

		if (total == size)
			return 1;

		int result = total * combination(total - 1, size) / (total - size);

		return result;
	}

	
	/**
	 * Extends a list of Double with a number of the same element. 
	 * 
	 * @param list the list to be extended
	 * @param element the element we want to add 
	 * @param freq number of times we add the same element
	 * @return the new extended list
	 */
	public static List<Double> addMultiple(List<Double> list, double element, int freq) {
		
		List<Double> result = new ArrayList<Double>();
		result.addAll(list);
		for (int i = 0; i < freq; i++)
			result.add(element);

		return result;
	}

	
	/**
	 * Searches all occurrences of an element in a list and deletes them.
	 * 
	 * @param list the list to be truncated
	 * @param element the element to be deleted
	 * @return a list without the element in question.
	 */
	public static List<Double> delMultiple(List<Double> list, double element) {
		
		List<Double> result = new ArrayList<Double>();
		
		for (Double x : list)
			if (x != element)
				result.add(x);

		return result;
	}

	
	/**
	 * Calculates the median of a list of doubles.
	 * 
	 * @param list the list of doubles
	 * @return the median
	 */
	public static double median(List<Double> list) {

		List<Double> clist = new ArrayList<Double>();
		clist.addAll(list);
		Collections.sort(clist);

		if (clist.size() % 2 == 1) {
			int index = (clist.size() - 1) / 2;
			return clist.get(index);
		}

		int index = clist.size() / 2;

		return (double) ((clist.get(index) + clist.get(index - 1)) / 2);
	}

	
	/**
	 * Calculates the average in a list of doubles.
	 * 
	 * @param list the list of doubles
	 * @return the average (mean)
	 */
	public static double mean(List<Double> list) {

		double sum = 0.0;
		for (int i = 0; i < list.size(); i++)
			sum += list.get(i);

		return (double) (sum / list.size());
	}

	
	/**
	 * Calculates the higher mode in a list of doubles.
	 * 
	 * @param list the list of doubles
	 * @return the higher mode
	 */
	public static double modeHigh(List<Double> list) {

		List<Double> clist = new ArrayList<Double>();

		for (Double element : list)
			if (!clist.contains(element))
				clist.add(element);
		Collections.sort(clist);

		int freqmax = 0;
		Double modeElement = 0.0;

		for (Double element : clist)
			if (freqmax <= Collections.frequency(list, element)) {
				freqmax = Collections.frequency(list, element);
				modeElement = element;
			}

		return (double) modeElement;
	}

	
	/**
	 * Calculates the lower mode in a list of doubles.
	 * 
	 * @param list the list of doubles
	 * @return the lower mode
	 */
	public static double modeLow(List<Double> list) {

		List<Double> clist = new ArrayList<Double>();

		for (Double element : list)
			if (!clist.contains(element))
				clist.add(element);
		
		Collections.sort(clist);

		int freqmax = 0;
		Double modeElement = 0.0;

		for (int i = clist.size() - 1; i >= 0; i--){
			if (freqmax <= Collections.frequency(list, clist.get(i))) {
				freqmax = Collections.frequency(list, clist.get(i));
				modeElement = clist.get(i);
			}
		}

		return (double) modeElement;
	}

	
	/**
	 * Calculates the variance of a list of doubles.
	 * 
	 * @param list the list of doubles
	 * @return the variance
	 */
	public static double variance(List<Double> list) {

		double m = mean(list);
		double var = 0.0;

		for (Double x : list)
			var += Math.pow((x - m), 2.0);

		var = var / list.size();

		return var;
	}

	
	/**
	 * Calculates the skewness of a list of doubles.
	 * 
	 * @param list the list of doubles
	 * @return the skewness
	 */
	public static double skewness(List<Double> list) {

		double m = mean(list);
		double dev = Math.sqrt(variance(list));
		double result = 0.0;
		for (Double x : list)
			result += Math.pow(((x - m) / dev), 3.0);

		result = result / list.size();

		return result;
	}

	/**
	 * Returns a list of list of possible combinations.
	 * 
	 * <p>
	 * It takes the size of our set and the max number of combinations, 
	 * then it gives as all possible combinations from 1 to the max number.
	 * For a size n, the elements are 0,1,...,n-1.
	 * </p>
	 * <p>
	 * For example: <br/>
	 * getCombinations(4,2) = {{0}, {1}, {2}, {3}, {0,1}, {0,2}, {0,3}, 
	 * {1,2}, {1,3}, {2,3}}
	 * </p>
	 * 
	 * @param size: The number of elements in our set
	 * @param nbr: The number of maximum combinations, for example 2 elements, etc.
	 * @return a list containing lists of combinations
	 */
	public static List<List<Integer>> getCombinations(int size, int nbr) {

		if (nbr < 1)
			return null;

		return addToComb(size, 0, nbr);
	}

	
	/**
	 * Recursive method used to search different combinations of a set 
	 * of elements starting from an element with a maximum number of combinations.
	 *  
	 * This method is used by {@link Calculus_T#getCombinations(int, int)}.
	 * 
	 * @param size the size of the elements set
	 * @param start starting point
	 * @param nbr maximum number of combinations
	 * @return a list containing lists of combinations
	 */
	private static List<List<Integer>> addToComb(int size, int start, int nbr) {

		nbr--;

		if (nbr == 0) {
			List<List<Integer>> combList = new ArrayList<List<Integer>>();
			for (int i = start; i < size; i++) {
				List<Integer> oneComb = new ArrayList<Integer>();
				oneComb.add(i);
				combList.add(oneComb);
			}

			return combList;
		}

		List<List<Integer>> combTList = new ArrayList<List<Integer>>();

		for (int i = start; i < size; i++) {
			List<List<Integer>> combList = addToComb(size, i + 1, nbr);
			for (List<Integer> comb : combList)
				comb.add(0, i);
			combTList.addAll(combList);
		}

		return combTList;
	}

}
