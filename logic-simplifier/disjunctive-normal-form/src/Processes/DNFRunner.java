package Processes;

import java.util.ArrayList;

import Entities.EquationEntity;
import Entities.Node;
import Visualization.TreePrinter;

/**
 * Runs the disjunctive form converter and returns a node
 * 
 * @author mavz1
 *
 */
public class DNFRunner {
	public Node run(String input) {
		Parser parser = new Parser();
		ArrayList<EquationEntity> inputs = parser.parse(input);
		Node node = parser.createTree(inputs);
		TreePrinter.print(node);
		node = parser.simplifyOperations(node);
		node = parser.pushDownNots(node);
		TreePrinter.print(node);
		boolean madeChange = false;
		do {
			madeChange = parser.pushAllOrDown(node);
		} while (madeChange);
		return node;
	}
}
