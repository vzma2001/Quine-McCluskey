package quinemccluskey;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class Term implements Comparable<Term> {
	public int group = 0; // Depends on the number of ones
	public String combo; // the binary version made up of 0, 1, and '-'. ie: 0101-1
	public String letterCombo=""; // The letter version using letters and apostrophes. ie: A'BC'DF
	public String columns; // Keeps track of which groups have been combined together
	public String[] columnArray;
	char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	// private ArrayList<Integer> currentColumnNums;
	public Term(String num) {
		combo = num;
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				letterCombo = letterCombo + (alphabet[i]);
			} else if (combo.charAt(i) == '0') {
				letterCombo = letterCombo + (alphabet[i]) + "'";
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

//	public static void setColumns(ArrayList<Term>termList) {
//		for(int i=0;i<termList.size();i++) {
//			termList.get(i).columns = String.valueOf(Integer.parseInt(termList.get(i).combo, 2));
//			termList.get(i).columnArray = termList.get(i).getColumns().split(" ");
//			organizeArray(termList.get(i).columnArray);
//		}
//		
//	}
	public Term(String num, int group, String letterCombo) {
		combo = num;
		this.group = group;
		this.letterCombo = letterCombo;
		columns = String.valueOf(Integer.parseInt(combo, 2));
		columnArray = columns.split(" ");
		organizeArray(columnArray);
		
	}

	public Term(String num, String col) {
		combo = num;
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				letterCombo = letterCombo + (alphabet[i]);
			} else if (combo.charAt(i) == '0') {
				letterCombo = letterCombo + (alphabet[i]) + "'";
			} else if (combo.charAt(i) != '-' && combo.charAt(i) != ' ') {
				throw new InvalidParameterException("The character " + combo.charAt(i) + " is not acceptable.");
			}
		}
		for (int i = 0; i < combo.length(); i++) {
			if (combo.charAt(i) == '1') {
				group++;
			}
		}
		columns = col;
		columnArray = columns.split(" ");
		for (int i = 0; i < columnArray.length; i++) {
			if (columnArray[i] == "" || columnArray[i] == " ") {
				ArrayList<String> listToDeleteFromArray = new ArrayList<String>(Arrays.asList(columnArray));
				listToDeleteFromArray.remove(i);
				columnArray = (String[]) listToDeleteFromArray.toArray();
				i = 0;
			}
		}
		organizeArray(columnArray);
	}

	public static void organizeArray(String[] columns) {
		for (int i = 0; i < columns.length; i++) {
			for (int j = columns.length - 1; j >= 0; j--) {
				if (Integer.parseInt(columns[j]) < Integer.parseInt(columns[i])) {
					String hold = columns[j];
					columns[j] = columns[i];
					columns[i] = hold;
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

	public String getColumns() {
		return columns;
	}

	public String[] getColumnsArray() {
		return columnArray;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(columnArray);
		result = prime * result + ((columns == null) ? 0 : columns.hashCode());
		result = prime * result + ((combo == null) ? 0 : combo.hashCode());
		result = prime * result + group;
		result = prime * result + ((letterCombo == null) ? 0 : letterCombo.hashCode());
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
		if (!Arrays.equals(columnArray, other.columnArray))
			return false;
		if (columns == null) {
			if (other.columns != null)
				return false;
		} else if (!columns.equals(other.columns))
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
		return true;
	}

	@Override
	public int compareTo(Term that) {
		return Integer.compare(this.getGroup(), that.getGroup());
	}
}
