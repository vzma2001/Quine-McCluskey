package quinemccluskey;

import java.security.InvalidParameterException;
import java.util.*;
import Processes.*;
import Entities.*;
import Sample.*;

/**
 * Terms are used in Quine McCluskey. Each Term has a bianry combo that
 * determines its group (Number of 1's), letterCombo (Decimal Form)
 * 
 * @author mavz1
 *
 */
public class Term implements Comparable<Term> {
	private int group = 0; // Depends on the number of ones
	private String combo; // the binary version made up of 0, 1, and '-'. ie: 0101-1
	private String letterCombo = ""; // The letter version using letters and apostrophes. ie: A'BC'DF
	private ArrayList<Integer> rowArray = new ArrayList<Integer>();
	private ArrayList<Samples> samples = new ArrayList<Samples>();
	private List<String> allLetters = new ArrayList<String>();
	private final static String[] ALPHABET = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * Constructor to convert node into term
	 * 
	 * @param node
	 * @param allSamples
	 */
	public Term(Node node, ArrayList<Samples> allSamples, ArrayList<String> letters) {
		allLetters = letters;
		NodeToTerm n = new NodeToTerm();
		this.samples = n.getSamples(node);

		int count = 0;
		String equation = "";
		HashSet<Samples> set = new HashSet<Samples>(samples);
		this.samples = new ArrayList<Samples>(set);

		Collections.sort(samples);

		// Creates the Binary Combo
		for (String s : allLetters) {
			if (count < samples.size() && s.equals(samples.get(count).getVariableName())) {
				if (samples.get(count).isWanted()) {
					equation += "1";
				} else {
					equation += "0";
				}
				count++;
			} else {
				equation += "-";
			}
		}
		combo = equation;
		letterCombo = node.createEquation();
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				group++;
			}
		}
	}

	/**
	 * Constructor when only given the combination
	 * 
	 * @param num
	 */
	public Term(String num, ArrayList<Samples> samples, ArrayList<String> letters) {
		allLetters = letters;
		if (allLetters.isEmpty()) {
			allLetters = new ArrayList<String>(Arrays.asList(ALPHABET));
		}
		this.samples = samples;
		HashSet<Samples> set = new HashSet<Samples>(samples);
		samples = new ArrayList<Samples>(set);
		Collections.sort(this.samples);
		combo = num;
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				letterCombo = letterCombo + allLetters.get(i);
			} else if (combo.charAt(i) == '0') {
				letterCombo = letterCombo + allLetters.get(i) + "'";
			} else if (combo.charAt(i) != '-' && combo.charAt(i) != ' ') {
				throw new InvalidParameterException("The character " + combo.charAt(i) + " is not acceptable.");
			}
		}
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				group++;
			}
		}

	}

	/**
	 * Constructor used to make term if all other info is known
	 * 
	 * @param num
	 * @param group
	 * @param letterCombo
	 */
	public Term(String num, int group, String letterCombo, ArrayList<Samples> samples, ArrayList<String> letters) {
		allLetters = letters;
		if (allLetters.isEmpty()) {
			allLetters = new ArrayList<String>(Arrays.asList(ALPHABET));
		}
		combo = num;
		this.samples = samples;
		HashSet<Samples> set = new HashSet<Samples>(samples);
		samples = new ArrayList<Samples>(set);
		Collections.sort(this.samples);
		this.group = group;
		this.letterCombo = letterCombo;
		String rows = String.valueOf(Integer.parseInt(combo, 2));
		String[] temp = rows.split(" ");
		rowArray = new ArrayList<Integer>();
		for (String s : temp) {
			rowArray.add(Integer.parseInt(s));
		}
		organizeArray(rowArray);

	}

	/**
	 * If the binary combination and the column is known. Used when combining terms
	 * 
	 * @param num
	 * @param col
	 * @param samples
	 * @param letters
	 */
	public Term(String num, String col, ArrayList<Samples> samples, ArrayList<String> letters) {
		allLetters = letters;
		if (allLetters.isEmpty()) {
			allLetters = new ArrayList<String>(Arrays.asList(ALPHABET));
		}
		combo = num;
		this.samples = samples;
		HashSet<Samples> set = new HashSet<Samples>(samples);
		samples = new ArrayList<Samples>(set);
		Collections.sort(this.samples);
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				letterCombo = letterCombo + allLetters.get(i);
			} else if (combo.charAt(i) == '0') {
				letterCombo = letterCombo + allLetters.get(i) + "'";
			} else if (combo.charAt(i) != '-' && combo.charAt(i) != ' ') {
				throw new InvalidParameterException("The character " + combo.charAt(i) + " is not acceptable.");
			}
		}
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				group++;
			}
		}
		String rows = col;

		String[] temp = rows.split(" ");
		rowArray = new ArrayList<Integer>();
		for (String s : temp) {
			rowArray.add(Integer.parseInt(s));
		}
		for (int i = 0; i < rowArray.size(); i++) {
			if (rowArray.get(i) == null) {
				rowArray.remove(i);
				i--;
			}
		}
		organizeArray(rowArray);
	}

	/**
	 * Organizes the columnsArray in increasing order
	 * 
	 * @param rows
	 */
	private void organizeArray(ArrayList<Integer> rows) {
		for (int i = 0; i < rows.size(); i++) {
			for (int j = rows.size() - 1; j >= 0; j--) {
				if (rows.get(j) < rows.get(i)) {
					Integer hold = rows.get(j);
					rows.set(j, rows.get(i));
					rows.set(i, hold);
				}
			}
		}
	}

	public int getGroup() {
		return group;
	}

	public String getCombo() {
		return combo;
	}

	public String getLetterCombo() {
		return letterCombo;
	}

	public ArrayList<Integer> getRowArray() {
		return rowArray;
	}

	public String getRows() {
		String toReturn = "";
		for (int i = 0; i < rowArray.size(); i++) {
			toReturn += " " + rowArray.get(i);
		}
		return toReturn.substring(1);
	}

	@Override
	public int compareTo(Term that) {
		return Integer.compare(this.getGroup(), that.getGroup());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(ALPHABET);
		result = prime * result + ((combo == null) ? 0 : combo.hashCode());
		result = prime * result + group;
		result = prime * result + ((letterCombo == null) ? 0 : letterCombo.hashCode());
		result = prime * result + ((rowArray == null) ? 0 : rowArray.hashCode());
		result = prime * result + ((samples == null) ? 0 : samples.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		if (!Arrays.equals(ALPHABET, other.ALPHABET))
			return false;
		if (combo == null) {
			if (other.combo != null)
				return false;
		} else if (!combo.equals(other.combo))
			return false;
		if (group != other.group)
			return false;
		if (letterCombo == null) {
			if (other.letterCombo != null)
				return false;
		} else if (!letterCombo.equals(other.letterCombo))
			return false;
		if (rowArray == null) {
			if (other.rowArray != null)
				return false;
		} else if (!rowArray.equals(other.rowArray))
			return false;
		if (samples == null) {
			if (other.samples != null)
				return false;
		} else if (!samples.equals(other.samples))
			return false;
		return true;
	}

	/**
	 * Sees if one term dominates the other
	 * 
	 * @param other
	 * @return
	 */
	public int isDominating(Term other) {
		if (this.rowArray.equals(other.rowArray)) {
			return 1;
		} else if (this.rowArray.containsAll(other.rowArray)) {
			return 1;
		} else if (other.rowArray.containsAll(this.rowArray)) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * Sees if one term dominates the other
	 * 
	 * @param term1
	 * @param term2
	 * @return
	 */
	public int isDominating(Term term1, Term term2) {
		if (term1.rowArray.equals(term2.rowArray)) {
			return 1;
		} else if (term1.rowArray.containsAll(term2.rowArray)) {
			return 1;
		} else if (term2.rowArray.containsAll(term1.rowArray)) {
			return -1;
		} else {
			return 0;
		}
	}

	public String toString() {
		return " " + combo;
	}

	public ArrayList<Samples> getSample() {
		return samples;
	}

	public ArrayList<Samples> getSamples() {
		return samples;
	}

	public void setSamples(ArrayList<Samples> samples) {
		this.samples = samples;
	}

	public List<String> getAllLetters() {
		return allLetters;
	}

	public void setAllLetters(List<String> allLetters) {
		this.allLetters = allLetters;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public void setCombo(String combo) {
		this.combo = combo;
	}

	public void setLetterCombo(String letterCombo) {
		this.letterCombo = letterCombo;
	}

	public void setRowArray(ArrayList<Integer> rowArray) {
		this.rowArray = rowArray;
	}

}
