package Processes;

import java.util.*;
import Entities.*;
import Sample.Samples;
import quinemccluskey.Term;

/**
 * Converts a node into an array of terms
 * 
 * @author mavz1
 *
 */
public class NodeToTerm {
	private final static String[] ALPHABET = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * Checks the current Node and creates a list of all variable names used
	 * 
	 * @param node
	 * @return
	 */
	public ArrayList<String> setAllLetters(Node node) {
		NodeToTerm n = new NodeToTerm();
		ArrayList<Samples> allSamples = n.getSamples(node);
		ArrayList<String> allLetters = new ArrayList<String>();
		for (Samples s : allSamples) {
			String toAdd = s.getVariableName();
			if (!allLetters.contains(toAdd)) {
				allLetters.add(toAdd);
			}
		}
		Collections.sort(allLetters);
		if (allLetters.isEmpty()) {
			allLetters = new ArrayList<String>(Arrays.asList(ALPHABET));
		}
		return allLetters;
	}

	/**
	 * Looks at a node and creates a list of all samples being tracked
	 * 
	 * @param allSamples
	 * @param topNode
	 * @return
	 */
	public ArrayList<Samples> getAllSamples(ArrayList<Samples> allSamples, Node topNode) {
		NodeToTerm converter = new NodeToTerm();
		ArrayList<Node> nodesToConvert = converter.findToConvert(topNode);
		allSamples = new ArrayList<Samples>();
		for (Node n : nodesToConvert) {
			ArrayList<Samples> toAdd = converter.getSamples(n);
			allSamples.addAll(toAdd);
			HashSet<Samples> set = new HashSet<Samples>(allSamples);
			allSamples = new ArrayList<Samples>(set);
		}
		Collections.sort(allSamples);
		return allSamples;
	}

	/**
	 * Converts a Node to a List of Terms. Terms are Variables and'ed together
	 * 
	 * @param topNode
	 * @return
	 */
	public ArrayList<Term> convertNodeToTerm(Node topNode, ArrayList<Samples> allSamples, ArrayList<String> letters) {
		ArrayList<Node> nodesToConvert = findToConvert(topNode);
		Term toAdd;
		ArrayList<Term> toReturn = new ArrayList<Term>();
		for (Node n : nodesToConvert) {
			toAdd = new Term(n, allSamples, letters);
			toReturn.add(toAdd);
		}
		return toReturn;
	}

	/**
	 * Gets all the samples contained in a single node
	 * 
	 * @param node
	 * @return
	 */
	public ArrayList<Samples> getSamples(Node node) {
		ArrayList<Samples> toReturn = new ArrayList<>();
		Node leftNode = node.getLeftNode();
		Node rightNode = node.getRightNode();
		if (node.getNodeChar().isSymbol(EntitySymbols.AND)) {
			toReturn.addAll(getSamples(leftNode));
			toReturn.addAll(getSamples(rightNode));
		} else if (node.getNodeChar().isSymbol(EntitySymbols.OR)) {
			ArrayList<Samples> x = getSamples(leftNode);
			toReturn.addAll(x);
			ArrayList<Samples> y = getSamples(rightNode);
			toReturn.addAll(y);
		} else if (node.getNodeChar().isSymbol(EntitySymbols.NOT)) {
			Samples toAdd = new Samples(node.getLeftNode().getSample());
			toAdd.setWant(false);
			toReturn.add(toAdd);
		} else if (node.getNodeChar().isSymbol(EntitySymbols.VAR)) {
			Samples toAdd = new Samples(node.getSample());
			toAdd.setWant(true);
			toReturn.add(toAdd);
		}
		return toReturn;
	}

	/**
	 * Finds nodes that should be converted such as lone variables and groups of
	 * variables and'ed together
	 * 
	 * @param topNode
	 * @return
	 */
	public ArrayList<Node> findToConvert(Node topNode) {
		ArrayList<Node> toReturn = new ArrayList<Node>();
		Node leftNode = topNode.getLeftNode();
		Node rightNode = topNode.getRightNode();

		if (topNode.getNodeChar().isSymbol(EntitySymbols.AND)) {
			toReturn.add(topNode);
		} else if (topNode.getNodeChar().isSymbol(EntitySymbols.OR)) {
			toReturn.addAll(findToConvert(leftNode));
			toReturn.addAll(findToConvert(rightNode));
		} else if (topNode.getNodeChar().isSymbol(EntitySymbols.VAR)) {
			toReturn.add(topNode);
		} else if (topNode.getNodeChar().isSymbol(EntitySymbols.NOT)) {
			toReturn.add(topNode);
		}
		return toReturn;
	}

}
