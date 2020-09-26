package quinemccluskey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import Sample.Samples;

/**
 * Implements the Quine McCluskey Algorithm. Checks for Tautology then if it is
 * not a tautology, it will find combine the terms, create a map with the
 * remaining terms, find essential prime implicants. and use row and column
 * dominance
 * 
 * @author mavz1
 *
 */
public class QuineMcCluskey {
	private String finalEquation = "";
	private Map<Integer, ArrayList<Term>> implicantTable = new HashMap<>();

	/**
	 * Displays the final equation and a description of it
	 * 
	 * @param termList
	 */
	public void display(ArrayList<Term> termList) {
		if (finalEquation.equals("")) {
			for (Term t : termList) {
				addAnswer(t.getLetterCombo());
			}
		}
		System.out.println("\nThe below equation is the simplified version of all the original minterms.");
		System.out.println("Key: \n\' = Not\n+ = Or");
		System.out.println("Simplified Equation: " + finalEquation);
	}

	/**
	 * Simplifies the original list of terms. First checks for tautology. Then if it
	 * is not, it creates a map, and find essential prime implicants and uses row
	 * and column dominance until no terms remain in the map. A list of essential
	 * prime implicants is returned
	 * 
	 * @param termList
	 * @return most simplified list of terms
	 */
	public ArrayList<Term> simplify(ArrayList<Term> termList, ArrayList<String> allLetters) {
		boolean taut = isTautology(termList, allLetters);
		if (taut) {
			setFinalEquation("1");
			return new ArrayList<Term>();
		} else {
			ArrayList<Term> simplified = new ArrayList<Term>();
			Collections.sort(termList);
			setTermInfo(termList, allLetters);

			termList = combineAll(termList, allLetters);
			makeMap(termList);

			ArrayList<Integer> primes = findPrimes();
			ArrayList<Integer> needToRemove = new ArrayList<Integer>();
			findAllEssentialPrimeImplicants(simplified, primes, needToRemove, allLetters);

			return simplified;
		}
	}

	/**
	 * Changes the user input into a list of terms that are all the same size and
	 * have no dashes.
	 * 
	 * @param input
	 * @return list of terms made from the string
	 */
	public ArrayList<Term> parseTerms(String input, ArrayList<String> allLetters) {
		String[] seperatedTerms = input.split(" ");

		ArrayList<Term> toReturn = new ArrayList<Term>();
		matchComboLength(input, seperatedTerms);

		for (int i = 0; i < seperatedTerms.length; i++) {
			toReturn.add(new Term(seperatedTerms[i], new ArrayList<Samples>(), allLetters));
		}
		// Finds any dashes in the minterms, and replaces it with a 1 and a 0.
		Boolean hasDashes = false;
		do {
			for (int i = 0; i < toReturn.size(); i++) {
				replaceDash(toReturn.get(i), toReturn, i, allLetters);
			}
			hasDashes = true;
			for (int i = 0; i < toReturn.size(); i++) {
				if (toReturn.get(i).getCombo().contains("-")) {
					hasDashes = false;
				}
			}
		} while (!hasDashes);
		// Organize based on group
		return (toReturn);
	}

	/**
	 * Checks if the user input string contains only valid characters: {"1", " ",
	 * "0", "-"}
	 * 
	 * @param input
	 * @return if the string is valid
	 */
	private boolean isValid(String input) {
		if (input.isEmpty()) {
			return false;
		}
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != ' ' && input.charAt(i) != '0' && input.charAt(i) != '1' && input.charAt(i) != '-') {
				System.out.println(input.charAt(i) + " is not a valid character");
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if there is tautology in the initial set of terms. Tautology is if
	 * every combination is achieved at that length. There would be tautology if
	 * after removing all duplicates and expanding all the dashes into both 0 and 1,
	 * if the number of terms was equal to 2^length of the combination. ex: For a
	 * combo with a length of 4 like 1000, it would be tautology if the there was a
	 * total of 16 terms (2^4)
	 * 
	 * @param termList
	 * @return if there is tautology
	 */
	public boolean isTautology(ArrayList<Term> termList, ArrayList<String> allLetters) {

		for (int i = 0; i < termList.size(); i++) {
			if (termList.get(i).getCombo().contains("-")) {
				Term termTrue = new Term(termList.get(i).getCombo().replaceFirst("-", "1"), new ArrayList<Samples>(),
						new ArrayList<String>());
				Term termFalse = new Term(termList.get(i).getCombo().replaceFirst("-", "0"), new ArrayList<Samples>(),
						new ArrayList<String>());
				termTrue.setSamples(getSampleToSet(termTrue, allLetters));
				termFalse.setSamples(getSampleToSet(termFalse, allLetters));
				termList.add(termTrue);
				termList.set(i, termFalse);
				i--;
			}
		}
		HashSet<Term> set = new HashSet<Term>();

		for (Term t : termList) {
			set.add(t);
		}
		termList.clear();
		for (Term term : set) {
			termList.add(term);
		}
		return set.size() == Math.pow(2, termList.get(0).getCombo().length());
	}

	/**
	 * This finds the longest combination and then makes all the other combos that
	 * size
	 * 
	 * @param listOfNums
	 * @param seperatedTerms
	 * @param longestCombo
	 */
	public void matchComboLength(String listOfNums, String[] seperatedTerms) {
		int longestCombo = 0;
		// Find the longest combo
		for (int i = 0; i < seperatedTerms.length; i++) {
			if (seperatedTerms[i].length() > longestCombo) {
				longestCombo = seperatedTerms[i].length();
			}
		}
		// Set all combos to be as long as the longest combo
		for (int i = 0; i < seperatedTerms.length; i++) {
			while (seperatedTerms[i].length() < longestCombo) {
				seperatedTerms[i] = 0 + seperatedTerms[i];
			}
		}
	}

	/**
	 * Changes the dashes to 1 and 0. Adds those to the termList and removes the
	 * original
	 * 
	 * @param term
	 * @param termList
	 * @param pos
	 */
	public void replaceDash(Term term, ArrayList<Term> termList, int pos, ArrayList<String> allLetters) {
		if (term.getCombo().contains("-")) {
			Term branch1 = new Term(term.getCombo().replaceFirst("-", "1"), term.getSample(), allLetters);
			Term branch0 = new Term(term.getCombo().replaceFirst("-", "0"), term.getSample(), allLetters);
			termList.set(pos, branch0);
			termList.add(branch1);
		}
	}

	/**
	 * Checks for duplicate terms by putting it in a HashSet
	 * 
	 * @param termList
	 * @return
	 */
	public ArrayList<Term> removeDuplicatesTerms(ArrayList<Term> termList) {
		HashSet<Term> a = new HashSet<>(termList);
		ArrayList<Term> hold = new ArrayList<Term>(a);
		return hold;
	}

	/**
	 * Checks for duplicate strings by putting it in a HashSet
	 * 
	 * @param currentNums
	 * @return
	 */
	public ArrayList<Integer> removeDuplicateString(ArrayList<Integer> currentNums) {
		HashSet<Integer> stringSet = new HashSet<>(currentNums);
		currentNums.clear();
		currentNums.addAll(stringSet);

		return currentNums;

	}

	/**
	 * Asks for user to input their minterms. Format of input should be in terms of
	 * binary numbers, separated by spaces. 0 represents false, 1 represents true, -
	 * represents either 1 or 0.
	 * 
	 * @return the user input
	 */
	public String getUserInput() {
		Scanner keyboard = new Scanner(System.in);
		String input;
		do {
			System.out.println("Input the terms that want to be simplified");
			System.out.println("Input should be made up of '1', '0', '-'. '-' represents both 1 and 0");
			System.out.println("Ex: \"11-\" = ABC & ABC'");
			System.out.print("Enter binary forms of minterms: ");

			input = keyboard.nextLine();
			input.trim();
		} while (!isValid(input));
		keyboard.close();
		return input;
	}

	/**
	 * Takes two groups and combines terms between each group if the terms only have
	 * one difference. Combining will alter all aspects of the term
	 * 
	 * @param termList
	 * @return
	 */
	public ArrayList<Term> combine(ArrayList<Term> termList, ArrayList<String> allLetters) {
		int differences = 0;
		int position = 0;
		String column;
		ArrayList<Term> combinedList = new ArrayList<Term>();
		ArrayList<Term> termList1 = new ArrayList<Term>();
		ArrayList<Term> termList2 = new ArrayList<Term>();
		ArrayList<Term> usedTerms = new ArrayList<>();
		for (Term term : termList) {

			// Create the two groups to compare. Dependent on the number of 1's in their
			// binary combinations
			termList1.clear();
			termList2.clear();
			int groupNum = term.getGroup();
			for (int i = 0; i < termList.size(); i++) {
				if (termList.get(i).getGroup() == groupNum) {
					termList1.add(termList.get(i));
				}

				if (termList.get(i).getGroup() == groupNum + 1) {
					termList2.add(termList.get(i));
				}
			}

			// Compares the two chosen groups and combines if the two terms have only 1
			// difference. Replaces the difference with a dash and marks the two terms as
			// need to remove
			for (int i = 0; i < termList1.size(); i++) {
				for (int j = 0; j < termList2.size(); j++) {
					for (int k = 0; k < termList.get(0).getCombo().length(); k++) {
						if (termList1.get(i).getCombo().charAt(k) != termList2.get(j).getCombo().charAt(k)) {
							differences++;
							position = k;
						}
					}

					if (differences == 1) {
						String newCombo = termList1.get(i).getCombo().substring(0, position) + '-'
								+ termList1.get(i).getCombo().substring(position + 1);
						column = termList1.get(i).getRows() + " " + termList2.get(j).getRows();
						ArrayList<Samples> toAdd = new ArrayList<>();
						toAdd.addAll(termList1.get(i).getSample());
						toAdd.addAll(termList2.get(j).getSample());
						HashSet<Samples> set = new HashSet<Samples>(toAdd);
						toAdd = new ArrayList<Samples>(set);
						Term termToAdd = new Term(newCombo, column, toAdd, allLetters);
						termToAdd.setSamples(getSampleToSet(termToAdd, allLetters));
						if (!combinedList.contains(termToAdd)) {
							combinedList.add(termToAdd);
						}
						usedTerms.add(termList1.get(i));
						usedTerms.add(termList2.get(j));
					}
					differences = 0;
				}
			}
		}
		termList.removeAll(usedTerms);
		combinedList.removeAll(usedTerms);
		termList.addAll(combinedList);
		return termList;

	}

	/**
	 * Adds an answer to the final equation
	 * 
	 * @param toAdd
	 */
	public void addAnswer(String toAdd) {
		if (finalEquation.length() != 0) {
			finalEquation += " OR " + toAdd;
		} else {
			finalEquation = toAdd;
		}

	}

	public String getFinalEquation() {
		return finalEquation;
	}

	public void setFinalEquation(String toSet) {
		finalEquation = toSet;
	}

	/**
	 * Takes in a term and looks at it's binary combo and creates an arrayList of
	 * Samples based on it This is allowed because allLetters is kept constant among
	 * all variables so the order is always the same
	 * 
	 * @param term
	 * @param allLetters
	 * @return
	 */
	private ArrayList<Samples> getSampleToSet(Term term, ArrayList<String> allLetters) {
		ArrayList<Samples> toReturn = new ArrayList<Samples>();
		String combo = term.getCombo();
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				toReturn.add(new Samples(allLetters.get(i), true));
			} else if (combo.charAt(i) == '0') {
				toReturn.add(new Samples(allLetters.get(i), false));
			}
		}
		return toReturn;
	}

	/**
	 * Removes column and row dominance
	 */
	private void removeDominance() {
		// Row Dominance
		Object[] keySet = implicantTable.keySet().toArray();
		HashSet<Integer> rowRemove = new HashSet<Integer>();
		// If a row contains all the terms of another, it will remove itself
		for (int i = 0; i < keySet.length; i++) {
			for (int j = 0; j < keySet.length; j++) {
				if (i != j) {
					if (implicantTable.get(keySet[i]).containsAll((implicantTable).get(keySet[j]))) {
						rowRemove.add((Integer) keySet[i]);
					} else if (implicantTable.get(keySet[j]).containsAll((implicantTable).get(keySet[i]))) {
						rowRemove.add((Integer) keySet[j]);
					}
				}
			}
		}
		for (Integer i : rowRemove) {
			implicantTable.remove(i);
		}
		// Finds and removes Column Dominance. If the column contains another column,
		// remove the column that is contained.
		HashSet<Term> toRemove = new HashSet<Term>();
		keySet = implicantTable.keySet().toArray();
		for (int i = 0; i < keySet.length; i++) {
			ArrayList<Term> toParse = implicantTable.get(keySet[i]);
			for (int j = 0; j < toParse.size(); j++) {
				for (int k = j + 1; k < toParse.size(); k++) {
					int dominance = toParse.get(j).isDominating(toParse.get(k));
					if (dominance == -1) {
						toRemove.add(toParse.get(j));
					} else if (dominance == 1) {
						toRemove.add(toParse.get(k));
					}
				}
			}
		}

		// Removes the term from the table
		for (Term t : toRemove) {
			for (int i = 0; i < keySet.length; i++) {
				if (implicantTable.get(keySet[i]).contains(t)) {
					ArrayList<Term> temp = implicantTable.get(keySet[i]);
					temp.remove(t);
					implicantTable.put((Integer) keySet[i], temp);
				}
			}
		}
	}

	/**
	 * Finds the Term that would affect the most terms
	 * 
	 * @return the term that has the greatest influence
	 */
	private Term mostInfluence() {
		Object[] keyset = implicantTable.keySet().toArray();
		Term most = implicantTable.get(keyset[0]).get(implicantTable.get(keyset[0]).size() - 1);

		for (Object i : keyset) {
			ArrayList<Term> compare = implicantTable.get(i);
			for (Term t : compare) {

				if (getInfluence(t) > getInfluence(most)) {
					most = t;
				}
			}
		}

		return most;
	}

	/**
	 * Gets the number of rows the term affects Is used to determine which term has
	 * the most influence when there are no essential prime implicants
	 * 
	 * @param term
	 * @return
	 */
	private int getInfluence(Term term) {
		ArrayList<Integer> rowsArray = term.getRowArray();
		int influence = 0;
		for (Integer row : rowsArray) {
			if (implicantTable.get(row) != null) {
				influence += implicantTable.get(row).size();
			}
		}

		return influence;
	}

	/**
	 * Finds the primes in the current set of numbers
	 * 
	 * @return list of primes
	 */
	private ArrayList<Integer> findPrimes() {
		ArrayList<Integer> primes = new ArrayList<Integer>();

		for (Integer key : implicantTable.keySet()) {
			if (implicantTable.get(key).size() == 1) {
				primes.add(key);
			}
		}
		return primes;
	}

	/**
	 * Sets all term info to ensure that all terms are formatted the same
	 * 
	 * @param termList
	 * @param allLetters
	 */
	private void setTermInfo(ArrayList<Term> termList, ArrayList<String> allLetters) {
		for (int i = 0; i < termList.size(); i++) {
			termList.set(i, new Term(termList.get(i).getCombo(), termList.get(i).getGroup(),
					termList.get(i).getLetterCombo(), termList.get(i).getSample(), allLetters));
		}
	}

	/**
	 * Constantly looks for prime implicants and uses row and column dominance until
	 * no more terms remain in the table. If there are no essential prime
	 * implicants, the ,most influential term will be picked and used as the
	 * essential prime implicant
	 * 
	 * @param simplified
	 * @param primes
	 * @param needToRemove
	 * @param allLetters
	 */
	private void findAllEssentialPrimeImplicants(ArrayList<Term> simplified, ArrayList<Integer> primes,
			ArrayList<Integer> needToRemove, ArrayList<String> allLetters) {
		do {
			// If there are no primes, the most influential term is chosen
			if (primes.isEmpty()) {
				chooseMostInfluenceToRemove(simplified, needToRemove, allLetters);
				removeMarked(needToRemove, allLetters);
				needToRemove.clear();
			} else {
				findPrimes(simplified, primes, needToRemove);
				ArrayList<Integer> implicantTableRemoval = removeRow(needToRemove);
				removeColumn(simplified, needToRemove, implicantTableRemoval, allLetters);
				needToRemove.clear();
			}

			primes = findPrimes();
		} while (!implicantTable.isEmpty());
	}

	/**
	 * Used if there are no primes. It will find the term that would remove the most
	 * number of terms and choose that term to act as an essential prime implicant
	 * 
	 * @param simplified
	 * @param needToRemove
	 * @param allLetters
	 */
	private void chooseMostInfluenceToRemove(ArrayList<Term> simplified, ArrayList<Integer> needToRemove,
			ArrayList<String> allLetters) {
		Term term = mostInfluence();
		needToRemove.clear();
		for (int j = 0; j < term.getRowArray().size(); j++) {
			if (!needToRemove.contains(term.getRowArray().get(j))) {
				needToRemove.add(term.getRowArray().get(j));
			}
		}
		if (!simplified.contains(term)) {
			simplified.add(term);
		}
	}

	/**
	 * Keeps combining terms until they cannot combine anymore
	 * 
	 * @param termList
	 * @param allLetters
	 * @return
	 */
	private ArrayList<Term> combineAll(ArrayList<Term> termList, ArrayList<String> allLetters) {
		Boolean wasCombined;
		do {

			ArrayList<Term> combinedList = combine(termList, allLetters);

			Collections.sort(termList);
			termList = combinedList;
			combinedList = combine(termList, allLetters);
			wasCombined = false;
			if (combinedList != termList) {
				wasCombined = true;
			}
			termList = removeDuplicatesTerms(termList);
		} while (wasCombined == true);
		return termList;
	}

	/**
	 * Takes all terms and makes the map
	 * 
	 * @param termList
	 */
	private void makeMap(ArrayList<Term> termList) {
		for (int i = 0; i < termList.size(); i++) {
			ArrayList<Integer> columnArray = termList.get(i).getRowArray();
			for (int j = 0; j < columnArray.size(); j++) {
				implicantTable.putIfAbsent(columnArray.get(j), new ArrayList<Term>());
				implicantTable.get(columnArray.get(j)).add(termList.get(i));
			}
		}
	}

	/**
	 * Removes a column from the table and removes rows that it contains. Those
	 * removed rows would be removed from other terms and columns that contain it
	 * 
	 * @param needToRemove
	 * @param allLetters
	 */
	private void removeMarked(ArrayList<Integer> needToRemove, ArrayList<String> allLetters) {
		ArrayList<Integer> implicantTableRemoval = new ArrayList<>();
		for (Integer j : implicantTable.keySet()) {
			for (int k = 0; k < needToRemove.size(); k++) {
				if (j == needToRemove.get(k)) {
					implicantTableRemoval.add(j);
				}
			}
		}
		for (int i = 0; i < implicantTableRemoval.size(); i++) {
			implicantTable.remove(implicantTableRemoval.get(i));
		}

		// Removes Row that have been used from remaining terms
		for (Integer i : implicantTable.keySet()) {
			ArrayList<Term> row = implicantTable.get(i);
			for (int k = 0; k < row.size(); k++) {
				Term t = row.get(k);
				ArrayList<Integer> columnArray = t.getRowArray();

				// Looks at other rows and sees if they contain the removed column
				for (Integer j : implicantTableRemoval) {
					if (columnArray.contains(j)) {
						String rows = t.getRows();
						String target = j + "";
						if (rows.indexOf(" " + j + " ") != -1) {
							rows = rows.replaceFirst(" " + j + " ", " ");
						} else if (rows.indexOf(j + " ") == 0) {
							rows = rows.replaceFirst(j + " ", "");
						} else if (rows.indexOf(" " + j) == rows.length() - 1 - target.length()) {
							rows = rows.substring(0, rows.indexOf(" " + j));
						}
						Term toChange = new Term(t.getCombo(), rows, t.getSample(), allLetters);
						row.set(row.indexOf(t), toChange);
						implicantTable.replace(i, row);
						t = row.get(k);
					}
				}

			}
		}

		// Removes the rows that were marked
		implicantTableRemoval = new ArrayList<>();
		for (Integer j : implicantTable.keySet()) {
			for (int k = 0; k < needToRemove.size(); k++) {
				if (j == needToRemove.get(k)) {
					implicantTableRemoval.add(k);
				}
			}
		}

		for (int i = 0; i < implicantTableRemoval.size(); i++) {
			implicantTable.remove(implicantTableRemoval.get(i));
		}
	}

	/**
	 * Looks for primes. Prime is found if the row has only one
	 * 
	 * @param simplified
	 * @param primes
	 * @param needToRemove
	 */
	private void findPrimes(ArrayList<Term> simplified, ArrayList<Integer> primes, ArrayList<Integer> needToRemove) {
		for (Integer key : primes) {
			Term prime = implicantTable.get(key).get(0);
			for (int j = 0; j < prime.getRowArray().size(); j++) {
				if (!needToRemove.contains(prime.getRowArray().get(j))) {
					needToRemove.add(prime.getRowArray().get(j));
				}
			}
			if (!simplified.contains(prime)) {
				simplified.add(prime);
			}
		}
	}

	/**
	 * Removes an entire row
	 * 
	 * @param needToRemove
	 * @return
	 */
	private ArrayList<Integer> removeRow(ArrayList<Integer> needToRemove) {
		ArrayList<Integer> implicantTableRemoval = new ArrayList<>();
		for (Integer j : implicantTable.keySet()) {
			for (int k = 0; k < needToRemove.size(); k++) {
				if (j == needToRemove.get(k)) {
					implicantTableRemoval.add(j);
				}
			}
		}
		for (int i = 0; i < implicantTableRemoval.size(); i++) {
			implicantTable.remove(implicantTableRemoval.get(i));
		}
		return implicantTableRemoval;
	}

	/**
	 * Removes a column from the implicantTable and removes the rows that are
	 * affected from the other terms. Column will contain terms in different rows.
	 * Those rows would be removed, so they will be removed from any term that
	 * contains term currently.
	 * 
	 * @param simplified
	 * @param needToRemove
	 * @param implicantTableRemoval
	 * @param allLetters
	 */
	private void removeColumn(ArrayList<Term> simplified, ArrayList<Integer> needToRemove,
			ArrayList<Integer> implicantTableRemoval, ArrayList<String> allLetters) {
		for (Integer i : implicantTable.keySet()) {
			ArrayList<Term> row = implicantTable.get(i);
			for (int k = 0; k < row.size(); k++) {
				Term t = row.get(k);
				ArrayList<Integer> columnArray = t.getRowArray();

				// Checks other rows to see if it contains that column and removes the removed
				// column from the rows
				for (Integer j : implicantTableRemoval) {
					if (columnArray.contains(j)) {
						String rows = t.getRows();
						String target = j + "";
						if (rows.indexOf(" " + j + " ") != -1) {
							rows = rows.replaceFirst(" " + j + " ", " ");
						} else if (rows.indexOf(j + " ") == 0) {
							rows = rows.replaceFirst(j + " ", "");
						} else if (rows.indexOf(" " + j) == rows.length() - 1 - target.length()) {
							rows = rows.substring(0, rows.indexOf(" " + j));
						}
						Term toChange = new Term(t.getCombo(), rows, t.getSample(), allLetters);
						row.set(row.indexOf(t), toChange);
						implicantTable.replace(i, row);
						t = row.get(k);
					}
				}

			}
		}

		// Looks through the new implicant Table to see if there are any new essential
		// prime implicants and marks them if there are
		for (Integer key : implicantTable.keySet()) {
			if (implicantTable.get(key).size() == 1) {
				Term prime = implicantTable.get(key).get(0);
				for (int j = 0; j < prime.getRowArray().size(); j++) {
					if (!needToRemove.contains(prime.getRowArray().get(j))) {
						needToRemove.add(prime.getRowArray().get(j));
					}
				}
				if (!simplified.contains(prime)) {
					simplified.add(prime);
				}

			}
		}

		removeDominance();
	}

}
