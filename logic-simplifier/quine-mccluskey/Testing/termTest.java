package Testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import Sample.Samples;
import quinemccluskey.Term;

public class termTest {

	/**
	 * Tests constructor that only takes in string
	 */
	@Test
	public void testTermConstructorWithOnlyString() {
		Term term = new Term("1000", new ArrayList<Samples>(), new ArrayList<String>());
		assertEquals(1, term.getGroup());
		assertTrue(term.getRowArray().isEmpty());
		assertEquals("AB'C'D'", term.getLetterCombo());
		assertEquals("1000", term.getCombo());
	}

	/**
	 * Tests if isDominating method returns 1 when equal terms are used
	 */
	@Test
	public void testTermIsDominatingSameTerm() {
		Term term1 = new Term("1000", 1, "AB'C'D'", new ArrayList<Samples>(), new ArrayList<String>());
		assertEquals(1, term1.isDominating(term1));
	}

	/**
	 * Checks if isDominating returns the correct term when the calling object
	 * dominates the other object
	 */
	@Test
	public void testTermIsDominatingOtherTerm() {
		Term term1 = new Term("1000", new ArrayList<Samples>(), new ArrayList<String>());
		ArrayList<Integer> toSet = new ArrayList<>();
		toSet.add(1);
		toSet.add(8);
		term1.setRowArray(toSet);
		toSet.remove(1);
		Term term2 = new Term("1010", new ArrayList<Samples>(), new ArrayList<String>());
		term2.setRowArray(toSet);
		assertEquals(1, term1.isDominating(term2));
	}

	/**
	 * Checks if isDominating returns the correct term when the calling object is
	 * dominated by the other term.
	 */
	@Test
	public void testTermIsDominatedByOtherTerm() {
		Term term1 = new Term("1000", new ArrayList<Samples>(), new ArrayList<String>());
		ArrayList<Integer> toSet = new ArrayList<>();
		toSet.add(1);
		term1.setRowArray(toSet);
		toSet = new ArrayList<Integer>();
		toSet.add(1);
		toSet.add(8);
		Term term2 = new Term("1010", new ArrayList<Samples>(), new ArrayList<String>());
		term2.setRowArray(toSet);
		assertEquals(-1, term1.isDominating(term2));
	}

	/**
	 * Checks if isDominating returns the correct term when there is no dominacne
	 * between the two terms
	 */
	@Test
	public void testTermIsDominatingNoDominance() {
		Term term1 = new Term("1000", new ArrayList<Samples>(), new ArrayList<String>());
		ArrayList<Integer> toSet = new ArrayList<>();
		toSet.add(1);
		term1.setRowArray(toSet);
		ArrayList<Integer> toSet2 = new ArrayList<>();
		toSet2.add(8);
		Term term2 = new Term("1010", new ArrayList<Samples>(), new ArrayList<String>());
		term2.setRowArray(toSet2);
		assertEquals(0, term1.isDominating(term2));
	}

}
