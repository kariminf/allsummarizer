package kariminf.as.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kariminf.as.tools.Calculus;

public class Calculus_T {

	public static void factorial_T() {
		long start = System.nanoTime();
		int fact = Calculus.factorial(5);
		long end = System.nanoTime();
		System.out.println();
		System.out.println("factorial(5): 120 = " + fact);
		System.out.println("==> took " + (end - start) + " nanoseconds");
	}
	
	public static void combination_T() {
		long start = System.nanoTime();
		int comb = Calculus.combination(5, 3);
		long end = System.nanoTime();
		System.out.println();
		System.out.println("combination(5,3): 10 = " + comb);
		System.out.println("==> took " + (end - start) + " nanoseconds");
	}

	public static void addMultiple_T() {
		List<Double> l = testList();
		System.out.println();
		System.out.println("list before = " + l);
		
		long start = System.nanoTime();
		List<Double> l2 = Calculus.addMultiple(l, 0.0, 3);
		long end = System.nanoTime();
		
		System.out.println("after adding 3 elements of 0.0 = " + l2);
		System.out.println("==> took " + (end - start) + " nanoseconds");
		
	}

	
	public static void delMultiple_T() {
		List<Double> l = testList();
		System.out.println();
		System.out.println("list before = " + l);
		
		long start = System.nanoTime();
		List<Double> l2 = Calculus.delMultiple(l, 6.0);
		long end = System.nanoTime();
		
		System.out.println("after deleting elements of 6.0 = " + l2);
		System.out.println("==> took " + (end - start) + " nanoseconds");
		
	}

	public static void median_T() {
		List<Double> l = testList();
		System.out.println();
		System.out.println("list = " + l);
		
		long start = System.nanoTime();
		double mediane = Calculus.median(l);
		long end = System.nanoTime();
		
		System.out.println("mediane: 3.5 = " + mediane);
		System.out.println("==> took " + (end - start) + " nanoseconds");
		
	}

	public static void mean_T() {
		List<Double> l = testList();
		System.out.println();
		System.out.println("list = " + l);
		
		long start = System.nanoTime();
		double mean = Calculus.mean(l);
		long end = System.nanoTime();
		
		System.out.println("mean: 3.625 = " + mean);
		System.out.println("==> took " + (end - start) + " nanoseconds");
	}

	public static void modeHigh_T() {
		List<Double> l = testList();
		System.out.println();
		System.out.println("list = " + l);
		
		long start = System.nanoTime();
		double hmode = Calculus.modeHigh(l);
		long end = System.nanoTime();
		
		System.out.println("higher mode: 6.0 = " + hmode);
		System.out.println("==> took " + (end - start) + " nanoseconds");
	}

	public static void modeLow_T() {
		List<Double> l = testList();
		System.out.println();
		System.out.println("list = " + l);
		
		long start = System.nanoTime();
		double lmode = Calculus.modeLow(l);
		long end = System.nanoTime();
		
		System.out.println("Lower mode: 2.0 = " + lmode);
		System.out.println("==> took " + (end - start) + " nanoseconds");
	}

	public static void variance_T() {
		//TODO complete
	}

	public static void skewness_T() {
		//TODO complete
	}

	
	public static void getCombinations_T() {
		//TODO complete
	}
	
	private static List<Double> testList(){
		List<Double> k = new ArrayList<Double>();
		k.add(5.0);
		k.add(1.0);
		k.add(6.0);
		k.add(2.0);
		k.add(6.0);
		k.add(4.0);
		k.add(2.0);
		k.add(3.0);
		return k;
	}

	public static void main(String[] args) {
		
		factorial_T();
		combination_T();
		addMultiple_T();
		delMultiple_T();
		median_T();
		mean_T();
		modeHigh_T();
		modeLow_T();
		variance_T();
		skewness_T();
		getCombinations_T();

	}

}
