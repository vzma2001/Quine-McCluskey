package Processes;

import java.util.ArrayList;
import Visualization.*;
import quinemccluskey.*;
import Entities.*;
import Sample.Samples;

public class DNFDriver {
	public static void main(String[] args) {
		GetUserInput keyBoard = new GetUserInput();
		String input = keyBoard.getUserInput();
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
		NodeToTerm nodeConvertor = new NodeToTerm();
		ArrayList<String> allLetters = nodeConvertor.setAllLetters(node);
		TreePrinter.print(node);
		ArrayList<String> letters = nodeConvertor.setAllLetters(node);
		ArrayList<Term> termList = nodeConvertor.convertNodeToTerm(node,
				nodeConvertor.getAllSamples(new ArrayList<Samples>(), node), letters);
		QuineMcCluskey simplifier = new QuineMcCluskey();
		termList = simplifier.simplify(termList, allLetters);
		simplifier.display(termList);
	}
}
