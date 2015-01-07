package aak.as.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculus {

	public static int factorial(int num){
		if (num<=1)
			return 1;
		int result = num * factorial(num-1);
		
		return result;
	}
	
	public static int combination(int total, int size){
		
		if (total < size)
			return 0;
		
		if (total == size)
			return 1;
		
		int result = total * combination(total-1, size) / (total-size);
		
		return result;
	}
	
	
	public static List<Double> addMultiple(List<Double> list, double element, int freq){
		List<Double> result = new ArrayList<Double>();
		result.addAll(list);
		for (int i=0; i< freq; i++)
			result.add(element);
		
		return result;
	}
	
	public static List<Double> delMultiple(List<Double> list, double element){
		List<Double> result = new ArrayList<Double>();
		for (Double x: list)
			if (x != element)
				result.add(x);
		
		return result;
	}
	
	public static double median (List<Double> list){
		
		List<Double> clist = new ArrayList<Double>();
		clist.addAll(list);
		Collections.sort(clist);

		if (clist.size() % 2 == 1){
			int index = (clist.size() - 1)/2;
			return clist.get(index);
		}
		
		int index = clist.size() /2;
		
		return (double)((clist.get(index) + clist.get(index-1))/2);
	}
	
	public static double mean (List<Double> list){

		double sum = 0.0;
		for (int i=0; i< list.size(); i++)
			sum += list.get(i);
		
		return (double)(sum/list.size());
	}
	
	public static double modeHigh (List<Double> list){

		List<Double> clist = new ArrayList<Double>();
		
		for(Double element: list)
			if (! clist.contains(element))
				clist.add(element);
		Collections.sort(clist);
		
		int freqmax = 0;
		Double modeElement = 0.0;
		
		for (Double element: clist)
			if (freqmax <= Collections.frequency(list, element)){
				freqmax = Collections.frequency(list, element);
				modeElement = element;
			}

		return (double) modeElement;
	}
	
	public static double modeLow (List<Double> list){

		List<Double> clist = new ArrayList<Double>();
		
		for(Double element: list)
			if (! clist.contains(element))
				clist.add(element);
		Collections.sort(clist);
		
		int freqmax = 0;
		Double modeElement = 0.0;
		
		for (int i = clist.size()-1; i>=0; i--)
			if (freqmax <= Collections.frequency(list, clist.get(i))){
				freqmax = Collections.frequency(list, clist.get(i));
				modeElement = clist.get(i);
			}

		return (double) modeElement;
	}


	public static double variance (List<Double> list){

		double m = mean(list);
		double var = 0.0;
		
		for (Double x: list)
			var += Math.pow((x-m), 2.0);
		
		var = var/list.size();

		return var;
	}
	
	
	public static double skewness (List<Double> list){

		double m = mean(list);
		double dev = Math.sqrt(variance(list));
		double result = 0.0;
		for (Double x: list)
			result += Math.pow(((x-m)/dev), 3.0);
		
		result = result / list.size();

		return result;
	}
	
	/**
	 *  Ret
	 * @param size : The number of elements in our set (always start with 0 and ends
	 *  with (size - 1) )
	 * @param nbr : The number of combinations, for example 2 elements, etc.
	 * @return A list containing the list of elements
	 */
	public static List<List<Integer>> getCombinations(int size, int nbr){
		
		if(nbr < 1 ) return null;

		return addToComb(size, 0, nbr);
	}
	
	private static List<List<Integer>> addToComb(int size, int start, int nbr){
		
		nbr--;
		
		if(nbr == 0 ) {
			List<List<Integer>> combList = new ArrayList<List<Integer>>();
			for (int i= start; i< size; i++){
				List<Integer> oneComb = new ArrayList<Integer>();
				oneComb.add(i);
				combList.add(oneComb);
			}
			
			return combList;
		}
		
		List<List<Integer>> combTList = new ArrayList<List<Integer>>();
		
		for (int i= start; i<size; i++){
			List<List<Integer>> combList = addToComb(size, i+1, nbr);
			for (List<Integer> comb: combList)
				comb.add(0, i);
			combTList.addAll(combList);
		}

		return combTList;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*int i = Calculus.combination(10,3);
		System.out.println(i);
		
		System.out.println(Calculus.getCombinations(5, 5));*/
		
		List<Double> k = new ArrayList<Double>();
		k.add(1.0);k.add(2.0);k.add(2.0);k.add(3.0);
		k.add(4.0);k.add(5.0);k.add(6.0);k.add(6.0);
		
		System.out.println("mean: " + Calculus.mean(k));
		System.out.println("variance: " + Calculus.variance(k));
		//System.out.println("deviation: " + Math.sqrt(Calculus.variance(k)));
		System.out.println("median: " + Calculus.median(k));
		System.out.println("higher mode: " + Calculus.modeHigh(k));
		System.out.println("lower mode: " + Calculus.modeLow(k));
		System.out.println("skewness: " + Calculus.skewness(k));
		
		System.out.println(Calculus.delMultiple(k, 2.0));

	}

}
