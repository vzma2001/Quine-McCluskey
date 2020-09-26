package Testing;

import org.junit.Test;
import quinemccluskey.QuineMcCluskey;
import quinemccluskey.Term;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class QuineMcCluskeyTest {
	private final static String[] ALPHABET = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * Tests isTautology Method to see if it catches the tautology
	 */
	@Test
	public void testTaut() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "111 110 101 100 011 010 001 000";
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
		assertTrue(test.isTautology(termList, new ArrayList<String>()));
	}

	/**
	 * Tests to see if it simplifies a specific case correctly
	 */
	@Test
	public void testSimplify() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "0000 0010 1000 0101 0110 1010 1100 0111 1101 1110 1111";
		String expectedAnswer = "B'D' BD BC AD'";
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
		termList = test.simplify(termList, new ArrayList<String>(Arrays.asList(ALPHABET)));
		String actualAnswer = test.getFinalEquation();
		for (Term t : termList) {
			actualAnswer += t.getLetterCombo() + " ";
		}
		actualAnswer = actualAnswer.trim();
		assertEquals(expectedAnswer, actualAnswer);
	}

	/**
	 * Tests to see if it simplifies another specific case correctly
	 */
	@Test
	public void testSimplify2() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "0000 0001 0010 1000 0101 0110 1001 1010 0111 1110";
		String expectedAnswer = "B'C' CD' A'BD";
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
		termList = test.simplify(termList, new ArrayList<String>(Arrays.asList(ALPHABET)));
		String actualAnswer = test.getFinalEquation();
		for (Term t : termList) {
			actualAnswer += t.getLetterCombo() + " ";
		}
		actualAnswer = actualAnswer.trim();
		assertEquals(expectedAnswer, actualAnswer);
	}

	/**
	 * Tests to see if it simplifies another specific case correctly
	 */
	@Test
	public void testSimplify3() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "0010 0011 1001 0111 1011 1101";
		String expectedAnswer = "A'B'C A'CD AC'D B'CD";
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
		termList = test.simplify(termList, new ArrayList<String>(Arrays.asList(ALPHABET)));
		String actualAnswer = test.getFinalEquation();
		for (Term t : termList) {
			actualAnswer += t.getLetterCombo() + " ";
		}
		actualAnswer = actualAnswer.trim();
		assertEquals(expectedAnswer, actualAnswer);
	}

	/**
	 * Tests if it will it will simplify correctly when it is one term off of being
	 * a tautology. Originally solved returned a form of the answer that was not the
	 * most simplified. 5 terms rather than the expected 4 terms
	 */
	@Test
	public void testOneOffOfTautology() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "1 10 11 100 101 110 1000";
		String expectedAnswer = "AB'C'D' A'B'D A'CD' A'BC'";
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
		termList = test.simplify(termList, new ArrayList<String>(Arrays.asList(ALPHABET)));
		String actualAnswer = test.getFinalEquation();
		for (Term t : termList) {
			actualAnswer += t.getLetterCombo() + " ";
		}
		actualAnswer = actualAnswer.trim();
		assertEquals(expectedAnswer, actualAnswer);
	}

	/**
	 * Originally broke because of lack of hashcode method in the Term class
	 */
	@Test
	public void testBreakingBecauseHashCode() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "0000 0010 1000 0101 0110 1010 1100 0111 1101 1110";
		String expectedAnswer = "B'D' A'BD CD' ABC'";
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
		termList = test.simplify(termList, new ArrayList<String>(Arrays.asList(ALPHABET)));
		String actualAnswer = test.getFinalEquation();
		for (Term t : termList) {
			actualAnswer += t.getLetterCombo() + " ";
		}
		actualAnswer = actualAnswer.trim();
		assertEquals(expectedAnswer, actualAnswer);
	}

	/**
	 * Expected to throw due to invalid character ("A") in the input
	 */
	@Test(expected = InvalidParameterException.class)
	public void testSimplifyLetterInInputThrowException() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "0000 0010 1000 0101 0110 1010 11A00 0111 1101 1110";
		@SuppressWarnings("unused")
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
	}

	/**
	 * Tests if it will still process the string correctly if there are multiple
	 * spaces before and after the terms
	 */
	@Test
	public void testSimplifyAndParseMultipleSpaces() {
		QuineMcCluskey test = new QuineMcCluskey();
		String input = "        0000            0010 1000 0101 0110              1010 1100 0111 1101 1110             ";
		String expectedAnswer = "B'D' A'BD CD' ABC'";
		ArrayList<Term> termList = test.parseTerms(input, new ArrayList<String>());
		termList = test.simplify(termList, new ArrayList<String>(Arrays.asList(ALPHABET)));
		String actualAnswer = test.getFinalEquation();
		for (Term t : termList) {
			actualAnswer += t.getLetterCombo() + " ";
		}
		actualAnswer = actualAnswer.trim();
		assertEquals(expectedAnswer, actualAnswer);
	}

}