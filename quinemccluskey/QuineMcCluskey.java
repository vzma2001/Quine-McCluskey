package quinemccluskey;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class QuineMcCluskey {
	public static void main(String[] args) {
		String finalEquation = "";
		String listOfNums = "";
		String[] seperatedTerms;
		String[] columnsArray;
		ArrayList<Term> termList = new ArrayList<Term>();
		ArrayList<Term> holdList = new ArrayList<Term>();
		ArrayList<Integer> needToRemove = new ArrayList<Integer>();
		ArrayList<Integer> removed = new ArrayList<Integer>();
		ArrayList<Integer> primeList = new ArrayList<>();
		ArrayList<Term> primeTermList = new ArrayList<>();
		Term hold = null;
		Boolean validInput = false;
		Boolean flag = false;
		int longestCombo = 0;
		Map<Integer, ArrayList<Integer>> implicantTable = new HashMap<>();
		Map<Term, ArrayList<Integer>> columnMap = new HashMap<>();
		int prime;

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
					termList = null;
				}
			} while (validInput == false);
			termList.add(hold);

		}
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
		termList = removeDuplicates(termList);
		Collections.sort(termList);
		Boolean wasCombined;
		for (int x = 0; x < termList.size(); x++) {
			termList.get(x).setColumns();
		}

		do {
			wasCombined = Simplify.combine(termList, longestCombo);
			termList = removeDuplicates(termList);
			Collections.sort(termList);
		} while (wasCombined == true);
		for (int x = 0; x < termList.size(); x++) {
			System.out.println(termList.get(x).getCombo());
		}

		/*
		 * // Find Prime Implicants do { for (int i = 0; i < termList.size(); i++) {
		 * columnsArray = termList.get(i).getColumnsArray(); for (int j = 0; j <
		 * columnsArray.length; j++) { implicantTable.putIfAbsent(j, new
		 * ArrayList<Integer>()); implicantTable.get(j).add(i); } }
		 * 
		 * // Find stuff that needs to be removed and puts into needToRemove for
		 * (Integer key : implicantTable.keySet()) { if (implicantTable.get(key).size()
		 * == 1) { prime = implicantTable.get(key).get(0); if (finalEquation == "") {
		 * finalEquation += termList.get(prime).getLetterCombo(); } else { finalEquation
		 * += " + " + termList.get(prime).getLetterCombo(); } for (int j = 0; j <
		 * termList.get(prime).getColumnsArray().length; j++) { flag = true; for (int k
		 * = 0; k < needToRemove.size(); k++) { if (needToRemove.get(k) ==
		 * Integer.parseInt(termList.get(prime).getColumnsArray()[j])) { flag = false; }
		 * } if (flag == true) {
		 * needToRemove.add(Integer.parseInt(termList.get(prime).getColumnsArray()[j]));
		 * } flag = true; } primeList.add(prime); } } // needToRemove contains the
		 * numbers in the prime's column
		 * 
		 * // Remove rows from the implicantTable that contain a number from the prime
		 * // implicant set. for (Integer j : implicantTable.keySet()) { for (int k = 0;
		 * k < needToRemove.size(); k++) { if (j == needToRemove.get(k)) {
		 * implicantTable.remove(j); } } } // Find Current numbers left to find
		 * rowDominance ArrayList<Integer> currentNums = new ArrayList<>(); for (Integer
		 * j : implicantTable.keySet()) { for (int k = 0; k <
		 * implicantTable.get(j).size(); k++) { for (int l = 0; l < currentNums.size();
		 * l++) { flag = true; if (currentNums.get(l) == implicantTable.get(k).get(j)) {
		 * flag = false; } } if (flag == true) {
		 * currentNums.add(implicantTable.get(k).get(j)); } flag = true; } }
		 * 
		 * // Row Dominance for (Integer j : implicantTable.keySet()) { if
		 * (implicantTable.get(j).size() == currentNums.size()) {
		 * implicantTable.remove(j); } }
		 * 
		 * // Column Dominance // Make new hashmap, get rid of prime from combined list.
		 * usesd combined lsit // and put in map. for (int j = 0; j < primeList.size();
		 * j++) { termList.remove(j); System.out.println("Here"); } } while
		 * (!implicantTable.isEmpty());
		 */
	}

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

	public static void replaceDash(Term term, ArrayList<Term> termList, int pos) {
		if (term.getCombo().contains("-")) {
			Term branch1 = new Term(term.getCombo().replaceFirst("-", "1"));
			Term branch0 = new Term(term.getCombo().replaceFirst("-", "0"));
			termList.set(pos, branch0);
			termList.add(branch1);
		}
	}

	public static ArrayList<Term> removeDuplicates(ArrayList<Term> termList) {
		HashSet<Term> termSet = new HashSet<>(termList);
		return new ArrayList<Term>(termSet);
	}
}
