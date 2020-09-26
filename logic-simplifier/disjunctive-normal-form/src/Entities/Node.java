package Entities;

import Sample.Samples;

/**
 * Each Node represents a character. It can be either an operand, variable, or
 * not. If it is an operand it will have two children. If it is a variable it
 * will not have any children. If it is a not, it will have one child.
 * 
 * @author mavz1
 *
 */
public class Node {
	private EquationEntity nodeVar;
	private Node leftNode;
	private Node rightNode;
	private Samples sample;

	public EquationEntity getNodeVar() {
		return nodeVar;
	}

	public void setNodeVar(EquationEntity nodeVar) {
		this.nodeVar = nodeVar;
	}

	public Samples getSample() {
		return sample;
	}

	public void setSample(Samples sample) {
		this.sample = sample;
	}

	/**
	 * Creates the Equation using the different Nodes
	 * 
	 * @return
	 */
	public String createEquation() {
		String toReturn = "";
		if (nodeVar.isSymbol(EntitySymbols.VAR)) {
			toReturn += this.sample.getVariableName();
		} else if (nodeVar.isSymbol(EntitySymbols.NOT)) {
			toReturn += this.getLeftNode().createEquation() + "'";
		} else if (nodeVar.isSymbol(EntitySymbols.AND)) {
			toReturn += this.getLeftNode().createEquation() + " AND " + this.getRightNode().createEquation();
		} else if (nodeVar.isSymbol(EntitySymbols.OR)) {
			toReturn += "( " + this.getLeftNode().createEquation() + ") OR (" + this.getRightNode().createEquation()
					+ " )";
		}
		return toReturn;
	}

	/**
	 * Creates node that has two child nodes. Will be an operation
	 * 
	 * @param c
	 * @param leftNode
	 * @param rightNode
	 */
	public Node(EquationEntity character, Node leftNode, Node rightNode) {
		nodeVar = character;
		this.leftNode = leftNode;
		this.rightNode = rightNode;
	}

	/**
	 * Creates a node that has one child node. Will be a "not"
	 * 
	 * @param c
	 * @param node
	 */
	public Node(EquationEntity charcter, Node node) {
		nodeVar = charcter;
		leftNode = node;
		rightNode = null;
	}

	/**
	 * Creates a node that has zero child nodes. Will be a term/letter
	 * 
	 * @param c
	 */
	public Node(EquationEntity character) {
		nodeVar = character;
		leftNode = null;
		rightNode = null;
		sample = new Samples();
		sample.setVariableName(character.getTermName());
	}

	public Node() {
	}

	public EquationEntity getNodeChar() {
		return nodeVar;
	}

	public void setNodeChar(EquationEntity nodeChar) {
		this.nodeVar = nodeChar;
	}

	public Node getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(Node leftNode) {
		this.leftNode = leftNode;
	}

	public Node getRightNode() {
		return rightNode;
	}

	public void setRightNode(Node rightNode) {
		this.rightNode = rightNode;
	}

	/**
	 * Creates a written equation of the node
	 * 
	 * @return
	 */
	public String getEquation() {
		String toReturn = " (";
		if (nodeVar.isSymbol(EntitySymbols.VAR)) {
			toReturn += nodeVar;
		} else if (nodeVar.toString().equals("NOT")) {
			toReturn += nodeVar;
			toReturn += leftNode.getEquation();
		} else {
			toReturn += leftNode.getEquation();
			toReturn += nodeVar;
			toReturn += rightNode.getEquation();
		}
		toReturn += ") ";
		return toReturn;
	}

	@Override
	public String toString() {
		String toReturn = "";
		if (leftNode != null) {
			toReturn += "Left: " + leftNode.getNodeChar();
		} else {
			toReturn += "Left: null";
		}
		if (nodeVar != null) {
			toReturn += "    Mid: " + nodeVar.getTermName();
		} else {
			toReturn += "    Mid: null";
		}

		if (rightNode != null) {
			toReturn += "    Right: " + rightNode.getNodeChar() + "\n";
		} else {
			toReturn += "    right: null\n";
		}

		return toReturn;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftNode == null) ? 0 : leftNode.hashCode());
		result = prime * result + ((nodeVar == null) ? 0 : nodeVar.hashCode());
		result = prime * result + ((rightNode == null) ? 0 : rightNode.hashCode());
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
		Node other = (Node) obj;
		if (leftNode == null) {
			if (other.leftNode != null)
				return false;
		} else if (!leftNode.equals(other.leftNode))
			return false;
		if (nodeVar == null) {
			if (other.nodeVar != null)
				return false;
		} else if (!nodeVar.equals(other.nodeVar))
			return false;
		if (rightNode == null) {
			if (other.rightNode != null)
				return false;
		} else if (!rightNode.equals(other.rightNode))
			return false;
		return true;
	}

}
