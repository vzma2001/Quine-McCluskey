package quinemccluskey;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class QuineMcCluskey {
	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) {
		String finalEquation = "";
		String listOfNums = "";
		String[] seperatedTerms;
		ArrayList<Term> termList = new ArrayList<Term>();
		ArrayList<Integer> needToRemove = new ArrayList<Integer>();
		ArrayList<Term> primeList = new ArrayList<>();
		Term hold = null;
		Boolean validInput = false;
		Boolean flag = false;
		int longestCombo = 0;
		Map<Integer, ArrayList<Term>> implicantTable = new HashMap<>();
		Map<Term, ArrayList<String>> columnMap = new HashMap<>();
		Term prime;

		// User inputs minterms. Checks if the minterms are only made of 1,0,-," ".
		listOfNums = getUserInput.getString();
		seperatedTerms = listOfNums.split(" ");
		makeCombo(listOfNums, seperatedTerms, longestCombo);
		for (int i = 0; i < seperatedTerms.length; i++) {
			do {
				try {
					hold = new Term(seperatedTerms[i]);
					validInput = true;
				} catch (InvalidParameterException e) {
					System.out.println(e.getMessage());
					validInput = false;
					listOfNums = getUserInput.getString();
					seperatedTerms = listOfNums.split(" ");
					makeCombo(listOfNums, seperatedTerms, longestCombo);
					termList = new ArrayList<Term>();
				}
			} while (validInput == false);
			termList.add(hold);
		}
		// Finds any dashes in the minterms, and replaces it with a 1 and a 0.
		do {
			for (int i = 0; i < termList.size(); i++) {
				replaceDash(termList.get(i), termList, i);
			}
			flag = true;
			for (int i = 0; i < termList.size(); i++) {
				if (termList.get(i).getCombo().contains("-")) {
					flag = false;
				}
			}
		} while (flag == false);

		// Organize based on group
		termList = removeDuplicatesTerms(termList);
		Collections.sort(termList);
		Boolean wasCombined;
		for (int i = 0; i < termList.size(); i++) {
			termList.set(i, new Term(termList.get(i).combo, termList.get(i).group, termList.get(i).letterCombo));
		}

		do {
			ArrayList<Term> holder = Simplify.combine(termList);
			wasCombined = false;
			if (holder != termList) {
				wasCombined = true;
			}
			termList = removeDuplicatesTerms(termList);
			Collections.sort(termList);
		} while (wasCombined == true);

		// Find Prime Implicants and make map.
		do {
			for (int i = 0; i < termList.size(); i++) {
				String[] columnArray = termList.get(i).columnArray;
				for (int j = 0; j < columnArray.length; j++) {
					implicantTable.putIfAbsent(Integer.parseInt(columnArray[j]), new ArrayList<Term>());
					implicantTable.get(Integer.parseInt(columnArray[j])).add(termList.get(i));
				}
			}

			// Find the prime terms, and uses that to find other terms that need to be
			// removed.
			for (Integer key : implicantTable.keySet()) {
				if (implicantTable.get(key).size() == 1) {
					prime = implicantTable.get(key).get(0);
					if (finalEquation == "") {
						finalEquation += prime.getLetterCombo();
					} else {
						finalEquation += " + " + prime.getLetterCombo();
					}
					for (int j = 0; j < prime.getColumnsArray().length; j++) {
						flag = true;
						for (int k = 0; k < needToRemove.size(); k++) {
							if (needToRemove.get(k) == Integer.parseInt(prime.getColumnsArray()[j])) {
								flag = false;
							}
						}
						if (flag == true) {
							needToRemove.add(Integer.parseInt(prime.getColumnsArray()[j]));
						}
						flag = true;
					}
					primeList.add(prime);
				}
			}
			// Removes the rowss that were marked
			ArrayList<Integer> implicantTableRemoval = new ArrayList<>();
			for (Integer j : implicantTable.keySet()) {
				for (int k = 0; k < needToRemove.size(); k++) {
					if (j == needToRemove.get(k)) {
						implicantTableRemoval.add(j);
					}
				}
			}
			for (int i = 0; i < implicantTableRemoval.size(); i++) {
				implicantTable.remove(i);
			}
			if (!implicantTable.isEmpty()) {
				// Finds which terms remain
				ArrayList<Term> currentNums = new ArrayList<>();
				for (Integer j : implicantTable.keySet()) {
					for (int k = 0; k < implicantTable.get(j).size(); k++) {
						currentNums.add(implicantTable.get(j).get(k));
					}
				}
				currentNums = removeDuplicatesTerms(currentNums);

				// Finds and removes Row Dominance (When all terms have a shared row)
				ArrayList<Integer> keysToRemove = new ArrayList<>();
				for (Integer j : implicantTable.keySet()) {
					if (implicantTable.get(j).size() == currentNums.size()) {
						keysToRemove.add(j);
					}
				}
				for (int k = 0; k < keysToRemove.size(); k++) {
					implicantTable.remove(keysToRemove.get(k));
				}
				// Finds and removes Column Dominance (When one term contains all hte values of
				// another term)
				for (int i = 0; i < currentNums.size(); i++) {
					ArrayList<String> columnListForMap = new ArrayList<String>(
							Arrays.asList(currentNums.get(i).columnArray));
					columnMap.put(currentNums.get(i), columnListForMap);
				}
				ArrayList<Term> columnDominancerRemoval = new ArrayList<>();
				for (Term i : columnMap.keySet()) {
					for (Term j : columnMap.keySet()) {
						if (columnMap.get(i).contains(columnMap.get(j)) && i != j
								&& !columnDominancerRemoval.contains(columnMap.get(j))) {
							columnDominancerRemoval.add(j);
						}
					}
				}
				for (int i = 0; i < columnDominancerRemoval.size(); i++) {
					columnMap.remove(columnDominancerRemoval.get(i));
				}
			}
		} while (!implicantTable.isEmpty());
		System.out.println(finalEquation);
	}

	/**
	 * This finds the longest combination and then makes all the other combos that
	 * size
	 * 
	 * @param listOfNums
	 * @param seperatedTerms
	 * @param longestCombo
	 */
	public static void makeCombo(String listOfNums, String[] seperatedTerms, int longestCombo) {

		// Find the longest combo
		for (int i = 0; i < seperatedTerms.length; i++) {
			if (seperatedTerms[i].length() > longestCombo) {
				longestCombo = seperatedTerms[i].length();
			}
		}
		// Set all combos to be as long as the longest combo
		for (int i = 0; i < seperatedTerms.length; i++) {
			while (seperatedTerms[i].length() < longestCombo) {
				seperatedTerms[i] = seperatedTerms[i] + "-";
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
	public static void replaceDash(Term term, ArrayList<Term> termList, int pos) {
		if (term.getCombo().contains("-")) {
			Term branch1 = new Term(term.getCombo().replaceFirst("-", "1"));
			Term branch0 = new Term(term.getCombo().replaceFirst("-", "0"));
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
	public static ArrayList<Term> removeDuplicatesTerms(ArrayList<Term> termList) {
		ArrayList<Term> hold = new ArrayList<>();
		HashSet<Term> termSet = new HashSet<>(termList);
		for (Term x : termSet) {
			hold.add(x);
		}
		return hold;
	}

	/**
	 * Checks for duplicate strings by putting it in a HashSet
	 * 
	 * @param currentNums
	 * @return
	 */
	public static ArrayList<Integer> removeDuplicateString(ArrayList<Integer> currentNums) {
		HashSet<Integer> stringSet = new HashSet<>(currentNums);
		currentNums.clear();
		currentNums.addAll(stringSet);

		return currentNums;

	}
}
