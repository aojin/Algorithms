package Searching.BSTGenetics;

import stdlib.*;

import java.util.ArrayList;

public class A4TestGeneticSequences {

	public static void main(String[] args) {

		readAndPrintGeneticSequence();
				
	}
	
	public static void readAndPrintGeneticSequence() {
		A4GeneticSequences ds = new A4GeneticSequences();
		StdIn.fromFile("src/Searching/BSTGenetics/sequences.txt");
		
		while (!StdIn.isEmpty()) {
			String genome = StdIn.readLine();
			String[] split = genome.split("\t");
			String species = split[0];
			String sequence = split[1];
			ds.addSpecies(species, sequence);
		}
		
		ArrayList<String> list = ds.speciesList();
		
		StdOut.printf("\nThere are %d species in the table\n\n", list.size());
		
		for (String species : list) {
			StdOut.printf("%s : %.20s\n", species, ds.sequence(species));
		}
		
		for (String species : list) {
			ds.removeSpecies(species);
		}
		
		list = ds.speciesList();
		
		StdOut.printf("\nAfter deletion, there are %d species in the table\n", list.size());

	}
}

