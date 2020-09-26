package Testing;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Entities.EntitySymbols;
import Entities.EquationEntity;
import Entities.Node;
import Processes.NodeToTerm;
import Sample.Samples;
import quinemccluskey.Term;

class TransitionTests {

	/**
	 * Builds a Node Tree and checks if it properly converts it to a list of terms
	 */
	@Test
	public void testNodeToTerm() {
		ArrayList<Term> expected = new ArrayList<Term>();
		Node left = new Node(new EquationEntity(EntitySymbols.VAR, "A"));
		Node right = new Node(new EquationEntity(EntitySymbols.VAR, "B"));
		Node input = new Node(new EquationEntity(EntitySymbols.OR), left, right);
		NodeToTerm nodeToTerm = new NodeToTerm();
		ArrayList<String> allLetters = nodeToTerm.setAllLetters(input);
		ArrayList<Samples> allSamples = nodeToTerm.getAllSamples(new ArrayList<Samples>(), input);
		ArrayList<Term> actual = nodeToTerm.convertNodeToTerm(input, allSamples, allLetters);
		ArrayList<Samples> toAdd = new ArrayList<>();
		Samples sampleToAdd = new Samples();
		sampleToAdd.setVariableName("A");
		sampleToAdd.setWant(true);
		toAdd.add(sampleToAdd);
		Term term = new Term("1-", toAdd, allLetters);
		expected.add(term);

		toAdd = new ArrayList<>();
		sampleToAdd = new Samples();
		sampleToAdd.setVariableName("B");
		sampleToAdd.setWant(true);
		toAdd.add(sampleToAdd);
		term = new Term("-1", toAdd, allLetters);
		expected.add(term);

		assertTrue(expected.equals(actual));
	}

	/**
	 * Creates a node tree and checks if the getSamples retrieves accurate samples
	 * from the tree
	 */
	@Test
	public void testGetSamples() {
		ArrayList<Samples> expected = new ArrayList<Samples>();
		Node left = new Node(new EquationEntity(EntitySymbols.VAR, "A"));
		Node right = new Node(new EquationEntity(EntitySymbols.VAR, "B"));
		Node input = new Node(new EquationEntity(EntitySymbols.OR), left, right);
		NodeToTerm nodeToTerm = new NodeToTerm();
		ArrayList<Samples> actual = nodeToTerm.getSamples(input);
		Samples sample = new Samples();
		sample.setVariableName("A");
		sample.setWant(true);
		expected.add(sample);
		sample = new Samples();
		sample.setVariableName("B");
		sample.setWant(true);
		expected.add(sample);
		assertTrue(expected.equals(actual));
	}

}
