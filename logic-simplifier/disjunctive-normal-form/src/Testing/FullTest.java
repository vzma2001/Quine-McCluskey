package Testing;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Entities.EquationEntity;
import Entities.Node;
import Processes.NodeToTerm;
import Processes.Parser;
import Sample.Samples;
import Visualization.TreePrinter;
import quinemccluskey.QuineMcCluskey;
import quinemccluskey.Term;

class FullTest {

	/**
	 * Tests a full run of an equation. Goes through disjunctive normal form, node
	 * to term conversion, and Quine McCluskey
	 */
	@Test
	public void testFullRun() {
		String input = "A@B";
		Parser parser = new Parser();
		ArrayList<EquationEntity> inputs = parser.parse(input);
		Node node = parser.createTree(inputs);
		TreePrinter.print(node);

		node = parser.simplifyOperations(node);
		node = parser.pushDownNots(node);
		boolean madeChange = false;
		do {
			madeChange = parser.pushAllOrDown(node);
		} while (madeChange);
		TreePrinter.print(node);
		NodeToTerm nodeConvertor = new NodeToTerm();
		ArrayList<String> allLetters = nodeConvertor.setAllLetters(node);
		ArrayList<Term> termList = nodeConvertor.convertNodeToTerm(node,
				nodeConvertor.getAllSamples(new ArrayList<Samples>(), node), allLetters);
		QuineMcCluskey simplifier = new QuineMcCluskey();
		termList = simplifier.simplify(termList, allLetters);
		ArrayList<Term> expected = new ArrayList<Term>();
		ArrayList<Samples> sampleToAdd = new ArrayList<Samples>();
		sampleToAdd.add(new Samples("A", false));
		sampleToAdd.add(new Samples("B", true));
		Term toAdd = new Term("01", 1, "A' AND B", sampleToAdd, allLetters);
		expected.add(toAdd);
		sampleToAdd = new ArrayList<Samples>();
		sampleToAdd.add(new Samples("A", true));
		sampleToAdd.add(new Samples("B", false));
		toAdd = new Term("10", 1, "A AND B'", sampleToAdd, allLetters);
		expected.add(toAdd);
		assertTrue(termList.equals(expected));
	}

}
