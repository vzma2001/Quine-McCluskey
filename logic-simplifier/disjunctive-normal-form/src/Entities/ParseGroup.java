package Entities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javafx.util.Pair;

/**
 * Tracks the different way operands can be written as
 * 
 * @author mavz1
 *
 */
public class ParseGroup {
	static HashMap parseGroups;
	public static final String sample = "sample", and = "and", or = "or", not = "not", xor = "xor", equiv = "equiv",
			implies = "implies", leftParend = "leftParend", rightParend = "rightParend";
	private static String samplePattern = "\\w+";
	public static String andPattern = "AND|\\&\\&|\\*";
	public static String orPattern = "OR|\\|\\||\\+";
	public static String notPattern = "NOT|\\!|\\~";
	public static String xorPattern = "XOR|\\^\\^|\\@|\\^";
	public static String equivPattern = "EQUIV|\\-\\-|\\=|\\-";
	public static String impliesPattern = "IMPLIES|\\>";
	public static String leftParendPattern = "\\(";
	public static String rightParendPattern = "\\)";

	@SuppressWarnings("unchecked")
	private static final List<Pair<String, String>> groupPairs = Arrays.asList(new Pair<>(and, andPattern),
			new Pair<>(or, orPattern), new Pair<>(not, notPattern), new Pair(xor, xorPattern),
			new Pair(equiv, equivPattern), new Pair<>(implies, impliesPattern), new Pair<>(sample, samplePattern),
			new Pair<>(leftParend, leftParendPattern), new Pair<>(rightParend, rightParendPattern));

	public ParseGroup() {
//		Pair toAdd = new Pair(Key k,);
	}

	public static String getNameFromIndex(int index) {
		return groupPairs.get(index).getKey();
	}

	public static Pattern getPattern() {

		StringBuilder sb = new StringBuilder();
		sb.append("(?i)");
		for (Pair<String, String> pair : groupPairs) {
			sb.append("(?<");
			sb.append(pair.getKey());
			sb.append(">");
			sb.append(pair.getValue());
			sb.append(")|");
		}

		return Pattern.compile(sb.substring(0, sb.length() - 1));

	}

}
