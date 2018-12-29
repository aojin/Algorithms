package Searching.BSTGenetics;
import Searching.RedBlackBST;
import stdlib.*;
import java.util.*;

public class A4GeneticSequences {

	/*  Your A4GeneticSequences class should only contain the methods specified in the API. It should declare an instance variable whose type is one of the symbol tables data structures we've seen, for example, an AVL tree or a red-black tree. 
	 * Do NOT copy the methods in those classes into your class. The API methods you write will use the symbol table instance variable and call various methods in the symbol table class, for example, to add the key-value pair of a species name 
	 * and its genetic sequence. Also note that the input file is formatted as lines where each line contains a species name (which might contain spaces), a tab, and a genetic sequence. This means that you will split a line read from this file 
	 * using a call to split specifying that the split character is a single tab. */
	
	public A4GeneticSequences() {} // upon use of the constructor, an empty Red-Black BST instance is instantiated
		
	private RedBlackBST<String, String> collection = new RedBlackBST<>();

	
	public void addSpecies(String species, String sequence) {
		
		collection.put(species, sequence);
		StdOut.printf("\n%s added to collection. \n",species);
		StdOut.printf("Height is now: %d \n", collection.height());
	}
	
	public void removeSpecies(String species) {
		
		collection.delete(species);
		StdOut.printf("\n%s removed from collection. \n", species);
		//StdOut.printf("Height is now: %d \n", collection.height());
	}

	public ArrayList<String> speciesList() {
		
		ArrayList<String> arrList = new ArrayList<String>(collection.size());
		
		Iterable<String> speciesKeys = collection.keys();
		
		for (String species : speciesKeys) {
			arrList.add(species);
		}
		
		Collections.sort(arrList);
		
		return arrList;
	}
	
	public String sequence(String species) {
		
		// StdOut.printf("\nThe first 20 of the sequence for %s is: %.20s \n", species, collection.get(species));
		return collection.get(species);
		
	}
	
}
