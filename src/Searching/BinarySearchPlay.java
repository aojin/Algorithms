package Searching;

import stdlib.*;
import java.util.Arrays;

public class BinarySearchPlay {

	// This is a simple rank function
	// It works fine for small arrays
	public static int rank0(int key, int[] a) {
		for (int i = 0; i < a.length; i++)
			if (a[i] == key) return i;
		return -1;
	}

	// Here is more efficient version, coded three ways
	// precondition: array a[] is sorted
	public static int ranker(int key, int[] a) { // interesting encapsulation which ensures that the two parameter mode still functions

		return rankHelper2 (key, a, 0, a.length - 1);
	}

	public static int rank(int key, int[] a) {
		int numLessThan = 0;
		for(int i = 0; i < a.length; i++) {
			if(a[i] < key) {
				numLessThan +=1 ;
			}
		}
		return numLessThan;
	}
	
	public static int count(int key, int[] a) {
		int numEqualsTo = 0;
		for(int i = 0; i < a.length; i++) {
			if(a[i] == key) {
				numEqualsTo +=1 ;
			}
		}
		return numEqualsTo;
	}
	
	// Here is  a standard
	public static int rankHelper1(int key, int[] a, int lo, int hi) {
		// key is in a[lo..hi] or not present.
		if (lo > hi) return -1; // this catches exceptions and returns error flag
		int mid = lo + (hi - lo) / 2; // mid will represent split value. Remember integer values are always truncated if they process a float. Meaning they round down.
		if      (key < a[mid]) return rankHelper1 (key, a, lo, mid - 1);
		else if (key > a[mid]) return rankHelper1 (key, a, mid + 1, hi);
		else return mid;
	}

	// This is the same, but uses a loop
	public static int rankHelper2(int key, int[] a, int lo, int hi) {
		while (lo <= hi) {
			// key is in a[lo..hi] or not present.
			int mid = lo + (hi - lo) / 2;
			if      (key < a[mid]) hi = mid - 1;
			else if (key > a[mid]) lo = mid + 1;
			else return mid; // returns the index where you first found the number
		}
		return -1; // is not return
	}

	// This is the same, but written with explicit variables
	public static int rankHelper3(int key, int[] a, int lo, int hi) { // marginally slower because it assigns variables in recursion
		if (lo > hi) return -1;
		int result;
		int mid = lo + (hi - lo) / 2;
		if (key < a[mid])
			result = rankHelper3 (key, a, lo, mid - 1);
		else if (key > a[mid])
			result = rankHelper3 (key, a, mid + 1, hi);
		else
			result = mid;
		return result;
	}

	public static void testTrace(String whitelistFile, int key) {
		// get whitelist from file and sort
		int[] whitelist = new In(whitelistFile).readAllInts();
		Arrays.sort(whitelist);
		StdOut.println(Arrays.toString(whitelist));

		int rank = ranker(key, whitelist);
		if (rank >= 0) {
			StdOut.println(key + " is in the list");
		} else {
			StdOut.println(key + " is NOT in the list");
		}
	}

	public static void testInteractive(String whitelistFile) {
		// get whitelist from file and sort
		int[] whitelist = new In(whitelistFile).readAllInts();
		Arrays.sort(whitelist);
		StdOut.println(Arrays.toString(whitelist));

		// read from the console; type Control-D or Control-Z at the beginning of a blank line to stop.
		StdOut.print("Enter a number: ");
		while (!StdIn.isEmpty()) {
			int key = StdIn.readInt();
			int rank = ranker(key, whitelist);
			int numLessThan = rank(key, whitelist);
			int numEqualsTo = count(key,whitelist);
			System.out.format("%d %s in the list\n", key, (rank >= 0) ? "is" : "is not");
			if (rank >= 0) {
				System.out.format("There are %s elements less than %d in the list\n", numLessThan, key);
				System.out.format("There %s %s %s equal to %d in the list\n", (numEqualsTo > 1) ? "are" : "is",
						numEqualsTo, (numEqualsTo > 1) ? "elements" : "element", key);
				return;
			}
			else
				StdOut.print("Enter a number: ");
		}
	}

	public static void testPerformance(String whitelistFile, String keyFile) {
		// get whitelist from file and sort
		int[] whitelist = new In(whitelistFile).readAllInts();
		Arrays.sort(whitelist);

		// open key file and start timer
		int[] keys = new In(keyFile).readAllInts();
		Stopwatch sw = new Stopwatch();

		// count number of data entries in the whitelist
		int count = 0;
		for (int key : keys) {
			if (ranker(key, whitelist) == -1) {
				//StdOut.println(key); // print to see the data, comment out for performance
				count = count + 1;
			}
		}

		System.out.format ("%d misses\n%f seconds\n", count, sw.elapsedTime ());
	}

	public static void main(String[] args) {
		testInteractive("data/tinyW.txt");
		//testTrace("data/tinyW.txt", 28);
		//testPerformance("data/tinyW.txt", "data/tinyT.txt");
		//testPerformance("data/largeW.txt", "data/largeT.txt");
	}
}

