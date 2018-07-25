package quinemccluskey;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class QuineMcCluskey {
	public static void main(String[] args) {
		String finalEquation = "";
		String listOfNums = "";
		String[] seperatedTerms;
		ArrayList<Term> termList = new ArrayList<Term>();

		ArrayList<Integer> needToRemove = new ArrayList<Integer>();

		ArrayList<Integer> primeList = new ArrayList<>();

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
		// termList = removeDuplicatesTerms(termList);

		// Find Prime Implicants

		do {
			for (int i = 0; i < termList.size(); i++) {
				String[] columnArray = termList.get(i).columnArray;
				// THIS IS A HOLDER. CHANGE THE 3. SUPPOSED TO BE LIKE THIS:
				// termList.get(i).getColumnsArray().length
				for (int j = 0; j < columnArray.length; j++) {
//					implicantTable.putIfAbsent(Integer.parseInt(columnArray[j]), new ArrayList<Integer>());
//					ArrayList<Integer> holder = imsplicantTable.get(j);
//					holder.add(i);
//					implicantTable.put(j, holder);
					implicantTable.putIfAbsent(j, new ArrayList<Integer>());
					implicantTable.get(j).add(i);
				}
			}

			// Find stuff that needs to be removed and puts into needToRemove
			for (Integer key : implicantTable.keySet()) {
				if (implicantTable.get(key).size() == 1) {
					prime = implicantTable.get(key).get(0);
					if (finalEquation == "") {
						finalEquation += termList.get(prime).getLetterCombo();
					} else {
						finalEquation += " + " + termList.get(prime).getLetterCombo();
					}
					for (int j = 0; j < termList.get(prime).getColumnsArray().length; j++) {
						flag = true;
						for (int k = 0; k < needToRemove.size(); k++) {
							if (needToRemove.get(k) == Integer.parseInt(termList.get(prime).getColumnsArray()[j])) {
								flag = false;
							}
						}
						if (flag == true) {
							needToRemove.add(Integer.parseInt(termList.get(prime).getColumnsArray()[j]));
						}
						flag = true;
					}
					primeList.add(prime);
				}
			}
			// needToRemove contains the numbers in the prime's column

			// Remove rows from the implicantTable that contain a number from the prime
			// implicant set.
			for (Integer j : implicantTable.keySet()) {
				for (int k = 0; k < needToRemove.size(); k++) {
					if (j == needToRemove.get(k)) {
						implicantTable.remove(j);
					}
				}
			}
			// Find Current numbers left to find rowDominance
			ArrayList<Integer> currentNums = new ArrayList<>();
			for (Integer j : implicantTable.keySet()) {
				for (int k = 0; k < implicantTable.get(j).size(); k++) {
					currentNums.add(implicantTable.get(j).get(k));
				}
			}
			currentNums = removeDuplicateString(currentNums);

			// Row Dominance
			ArrayList<Integer> keysToRemove = new ArrayList<>();
			for (Integer j : implicantTable.keySet()) {
				if (implicantTable.get(j).size() == currentNums.size()) {
					keysToRemove.add(j);
				}
			}
			for (int k = 0; k < keysToRemove.size(); k++) {
				implicantTable.remove(keysToRemove.get(k));
			}
			// Column Dominance
			// Make new hashmap, get rid of prime from combined list. use combinedList and put in map.columnMap

		} while (!implicantTable.isEmpty());
		System.out.println(finalEquation);
		System.out.println("HERE");
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

	public static ArrayList<Term> removeDuplicatesTerms(ArrayList<Term> termList) {
//		ArrayList<String> comboList = new ArrayList<>();
		ArrayList<Term> hold = new ArrayList<>();
//		for (int x = 0; x < termList.size(); x++) {
//			comboList.add(termList.get(x).getCombo());
//		}
		HashSet<Term> termSet = new HashSet<>(termList);
		for (Term x : termSet) {
			hold.add(x);
		}
		return hold;
	}

	public static ArrayList<Integer> removeDuplicateString(ArrayList<Integer> currentNums) {
		HashSet<Integer> stringSet = new HashSet<>(currentNums);
		currentNums.clear();
		currentNums.addAll(stringSet);

		return currentNums;

	}
}
