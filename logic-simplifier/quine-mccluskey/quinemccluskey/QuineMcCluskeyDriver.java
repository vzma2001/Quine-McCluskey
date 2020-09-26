package quinemccluskey;

import java.util.ArrayList;
import java.util.Arrays;

public class QuineMcCluskeyDriver {

	public static void main(String[] args) {
		String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		String listOfNums = "";
		ArrayList<Term> termList = new ArrayList<Term>();
		QuineMcCluskey simplifier = new QuineMcCluskey();

		listOfNums = simplifier.getUserInput();

		termList = simplifier.parseTerms(listOfNums, new ArrayList<String>());

		boolean taut = simplifier.isTautology(termList, new ArrayList<String>(Arrays.asList(alphabet)));
		termList = simplifier.simplify(termList, new ArrayList<String>());

		simplifier.display(termList);
	}
}
