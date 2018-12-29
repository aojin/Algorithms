package Sorting;

import stdlib.*;

public class SimpleLinearSort {

	public static void main(String[] args) {
		
		// Algorithms Exercise 1.1.26
		
		int a = 4, b = 1, c = 2;
		
		StdOut.println(String.format("Test Values: %d %d %d", a,b, c));
		
		int[] result = threesortint(a,b,c);
		
			for (int i = 0; i < result.length; i++) {
				StdOut.print(result[i]+" ");
			}
		
		double d = 5.6, e = 12.2315, f = 0.9999;
		
		StdOut.println(String.format("\n\nDouble Test Values: %f %f %f", d,e, f));
		
		double[] result1 = threesortdouble(d,e,f);
		
			for (int i = 0; i < result1.length; i++) {
				StdOut.print(result1[i]+" ");
			}
		
		char g = 'c', h = 'a', i = 'b'; // If you use 'B' instead of 'b', the order will print B a c because capitals are lower order in ASCII
		
		StdOut.println(String.format("\n\nChar Test Values: %c %c %c", g,h, i));
		
		char[] result2 = threesortchar(g,h,i);
		
			for (int i2 = 0; i2 < result2.length; i2++) {
				StdOut.print(result2[i2]+" ");
			}
			
	}

	// Below, I write three test functions on integer primitives, double primitives and character primitives
	
	public static int[] threesortint(int a, int b, int c) {
		int t;
		if (a > b) { t = a; a = b; b = t; }
		if (a > c) { t = a; a = c; c = t; }
		if (b > c) { t = b; b = c; c = t; }
		int[] arr = {a, b, c};
		return arr;
	}
	
	public static double[] threesortdouble(double a, double b, double c) {
		double t;
		if (a > b) { t = a; a = b; b = t; }
		if (a > c) { t = a; a = c; c = t; }
		if (b > c) { t = b; b = c; c = t; }
		double[] arr = {a, b, c};
		return arr;
	}
	
	public static char[] threesortchar(char a, char b, char c) { //characters of the same case will be compared in their sequential ordering
		char t;
		if (a > b) { t = a; a = b; b = t; }
		if (a > c) { t = a; a = c; c = t; }
		if (b > c) { t = b; b = c; c = t; }
		char[] arr = {a, b, c};
		return arr;
	}
	
}

