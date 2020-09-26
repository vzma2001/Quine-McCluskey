package Processes;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.EntityCategories;
import Entities.EntitySymbols;
import Entities.EquationEntity;
import Entities.Node;
import Entities.ParseGroup;

public class Parser {
	private int index = 0;

	/**
	 * Creates a tree made of operations, variables, and nots
	 * 
	 * @param entities
	 * @return Tree of nodes
	 */
	public Node createTree(ArrayList<EquationEntity> entities) {
		Node toReturn = null;
		Node leftChildNode = null;
		Node rightChildNode = null;
		Boolean leftIsNot = false;
		Boolean rightIsNot = false;
		EquationEntity parent = null;

		for (int i = index; i < entities.size(); i++) {
			if (entities.get(i).isSymbol(EntitySymbols.LEFT_PAREND)) {
				if (leftChildNode == null) {
					index++;
					leftChildNode = createTree(entities);
					i = index - 1;
				} else {
					index++;
					rightChildNode = createTree(entities);
					i = index - 1;
				}
			} else if (entities.get(i).isSymbol(EntitySymbols.RIGHT_PAREND)) {
				index++;
				if (parent == null) {
					return leftChildNode;
				} else {
					if (leftIsNot) {
						leftChildNode = new Node(new EquationEntity(EntitySymbols.NOT), leftChildNode);
					}
					if (rightIsNot) {
						rightChildNode = new Node(new EquationEntity(EntitySymbols.NOT), rightChildNode);
					}
					return new Node(parent, leftChildNode, rightChildNode);
				}
			} else if (entities.get(i).isSymbol(EntitySymbols.OR)) {
				parent = entities.get(i);
				index++;
			} else if (entities.get(i).isSymbol(EntitySymbols.AND)) {
				parent = entities.get(i);
				index++;
			} else if (entities.get(i).isSymbol(EntitySymbols.XOR)) {
				parent = entities.get(i);
				index++;
			} else if (entities.get(i).isSymbol(EntitySymbols.IMPLIES)) {
				parent = entities.get(i);
				index++;
			} else if (entities.get(i).isSymbol(EntitySymbols.EQUIV)) {
				parent = entities.get(i);
				index++;
			} else if (entities.get(i).isSymbol(EntitySymbols.NOT)) {
				if (leftChildNode == null) {
					leftIsNot = !leftIsNot;
				} else {
					rightIsNot = !rightIsNot;
				}
				index++;
			} else {
				if (leftChildNode == null) {
					leftChildNode = new Node(entities.get(i));
				} else {
					rightChildNode = new Node(entities.get(i));
				}
				index++;
			}
		}
		if (parent == null) {
			if (leftIsNot) {
				leftChildNode = new Node(new EquationEntity(EntitySymbols.NOT), leftChildNode);
			}
			toReturn = leftChildNode;
		} else {
			if (leftIsNot) {
				leftChildNode = new Node(new EquationEntity(EntitySymbols.NOT), leftChildNode);
			}
			if (rightIsNot) {
				rightChildNode = new Node(new EquationEntity(EntitySymbols.NOT), rightChildNode);
			}
			toReturn = new Node(parent, leftChildNode, rightChildNode);
		}

		return toReturn;
	}

	/**
	 * Finds all the or's and pushes them below any ands.
	 * 
	 * @param node
	 * @return
	 */
	public boolean pushAllOrDown(Node node) {
		Node temp = node;
		boolean madeChange = pushOrDown(temp);
		if (madeChange) {
			return true;
		}
		if (node != null && !isBottomOfBranch(node.getLeftNode())) {
			madeChange = pushAllOrDown(temp.getLeftNode());
		}
		if (node != null && !madeChange && !isBottomOfBranch(node.getRightNode())) {
			madeChange = pushAllOrDown(temp.getRightNode());
		}
		return madeChange;
	}

	/**
	 * Returns if the node is the end of a branch, either a variable or a notted
	 * variable
	 * 
	 * @param node
	 * @return
	 */
	private boolean isBottomOfBranch(Node node) {
		boolean isBottom = false;
		if (node != null
				&& (node.getNodeChar().isSymbol(EntitySymbols.VAR) || node.getNodeChar().isSymbol(EntitySymbols.NOT))) {
			isBottom = true;
		}
		return isBottom;
	}

	/**
	 * Pushes down a singular 'or' below an 'and'
	 * 
	 * @param node
	 * @return
	 */
	public boolean pushOrDown(Node node) {
		if (node != null) {
			Node leftNode = node.getLeftNode();
			Node rightNode = node.getRightNode();
			if (leftNode != null && node.getNodeChar().isSymbol(EntitySymbols.AND)
					&& leftNode.getNodeChar().isSymbol(EntitySymbols.OR)) {
				Node newLeft = new Node(new EquationEntity(EntitySymbols.AND), leftNode.getLeftNode(), rightNode);
				Node newRight = new Node(new EquationEntity(EntitySymbols.AND), leftNode.getRightNode(), rightNode);
				node.setLeftNode(newLeft);
				node.setRightNode(newRight);
				node.setNodeChar(new EquationEntity(EntitySymbols.OR));
				return true;
			} else if (rightNode != null && node.getNodeChar().isSymbol(EntitySymbols.AND)
					&& rightNode.getNodeChar().isSymbol(EntitySymbols.OR)) {
				Node newLeft = new Node(new EquationEntity(EntitySymbols.AND), leftNode, rightNode.getLeftNode());
				Node newRight = new Node(new EquationEntity(EntitySymbols.AND), leftNode, rightNode.getRightNode());
				node.setLeftNode(newLeft);
				node.setRightNode(newRight);
				node.setNodeChar(new EquationEntity(EntitySymbols.OR));
				return true;
			}
		}
		return false;
	}

	/**
	 * Pushes down so the nots are associated with variables rather than operations
	 * 
	 * @param node
	 * @return
	 */
	public Node pushDownNots(Node node) {

		do {
			if (node.getNodeChar().isSymbol(EntitySymbols.NOT)) {
				Node leftNode = node.getLeftNode();
				EquationEntity leftNodeChar = leftNode.getNodeChar();
				if (leftNodeChar.isSymbol(EntitySymbols.AND)) {
					node = new Node(new EquationEntity(EntitySymbols.OR),
							new Node(new EquationEntity(EntitySymbols.NOT), leftNode.getLeftNode()),
							new Node(new EquationEntity(EntitySymbols.NOT), leftNode.getRightNode()));
				} else if (leftNodeChar.isSymbol(EntitySymbols.OR)) {
					node = new Node(new EquationEntity(EntitySymbols.AND),
							new Node(new EquationEntity(EntitySymbols.NOT), leftNode.getLeftNode()),
							new Node(new EquationEntity(EntitySymbols.NOT), leftNode.getRightNode()));
				} else if (leftNodeChar.isSymbol(EntitySymbols.NOT)) {
					node = leftNode.getLeftNode();
				}
			}
		} while (node.getNodeChar().getType() != null && node.getNodeChar().isType(EntityCategories.NOT));

		if (node.getLeftNode() != null && !node.getLeftNode().getNodeChar().isSymbol(EntitySymbols.VAR)) {
			node.setLeftNode(pushDownNots(node.getLeftNode()));
		}

		if (node.getRightNode() != null && !node.getRightNode().getNodeChar().isSymbol(EntitySymbols.VAR)) {
			node.setRightNode(pushDownNots(node.getRightNode()));
		}
		return node;
	}

	/**
	 * Changes Equiv, implies, and Xor into ands and ors
	 * 
	 * @param node
	 * @return
	 */
	public Node simplifyOperations(Node node) {
		Node leftNode = node.getLeftNode();
		Node rightNode = node.getRightNode();
		if (leftNode != null) {
			EquationEntity leftNodeChar = leftNode.getNodeChar();
			if (!leftNodeChar.isSymbol(EntitySymbols.VAR)) {
				leftNode = simplifyOperations(leftNode);
			}
		}

		if (rightNode != null) {
			EquationEntity rightNodeChar = rightNode.getNodeChar();
			if (!rightNodeChar.isSymbol(EntitySymbols.VAR)) {
				rightNode = simplifyOperations(rightNode);
			}
		}

		node.setLeftNode(leftNode);
		node.setRightNode(rightNode);

		if (node.getNodeChar().isSymbol(EntitySymbols.EQUIV)) {
			node = removeEquiv(node);
		} else if (node.getNodeChar().isSymbol(EntitySymbols.IMPLIES)) {
			node = removeImplies(node);
		} else if (node.getNodeChar().isSymbol(EntitySymbols.XOR)) {
			node = removeXOr(node);
		}

		return node;
	}

	/**
	 * Replaces A implies B with !A OR B
	 * 
	 * @param node
	 * @return
	 */
	private Node removeImplies(Node node) {
		Node newLeftNode = new Node(new EquationEntity(EntitySymbols.NOT), node.getLeftNode());
		Node newRightNode = node.getRightNode();
		return new Node(new EquationEntity(EntitySymbols.OR), newLeftNode, newRightNode);
	}

	/**
	 * Replaces A Equiv B with (A AND B) OR (!A AND !B)
	 * 
	 * @param node
	 * @return
	 */
	private Node removeEquiv(Node node) {
		Node newLeftNode = new Node(new EquationEntity(EntitySymbols.AND), node.getLeftNode(), node.getRightNode());
		Node newRightNode = new Node(new EquationEntity(EntitySymbols.AND),
				new Node(new EquationEntity(EntitySymbols.NOT), node.getLeftNode()),
				new Node(new EquationEntity(EntitySymbols.NOT), node.getRightNode()));
		return new Node(new EquationEntity(EntitySymbols.OR), newLeftNode, newRightNode);
	}

	/**
	 * Replaces A XOr B with (A AND !B) OR (!A AND B)
	 * 
	 * @param node
	 * @return
	 */
	private Node removeXOr(Node node) {
		Node newLeftNode = new Node(new EquationEntity(EntitySymbols.AND), node.getLeftNode(),
				new Node(new EquationEntity(EntitySymbols.NOT), node.getRightNode()));
		Node newRightNode = new Node(new EquationEntity(EntitySymbols.AND),
				new Node(new EquationEntity(EntitySymbols.NOT), node.getLeftNode()), node.getRightNode());
		return new Node(new EquationEntity(EntitySymbols.OR), newLeftNode, newRightNode);
	}

	/**
	 * Takes in a equation and makes each term into an Equation Entity
	 * 
	 * @param input
	 * @return
	 */
	public ArrayList<EquationEntity> parse(String input) {
		Pattern pattern = ParseGroup.getPattern();

		Matcher matcher = pattern.matcher(input);
		System.out.println("Matcher:");

		ArrayList<EquationEntity> entities = new ArrayList<>();

		while (matcher.find()) {

			if (matcher.group(ParseGroup.and) != null) {
				entities.add(new EquationEntity(EntitySymbols.AND));
				continue;
			} else if (matcher.group(ParseGroup.or) != null) {
				entities.add(new EquationEntity(EntitySymbols.OR));
				continue;
			} else if (matcher.group(ParseGroup.xor) != null) {
				entities.add(new EquationEntity(EntitySymbols.XOR));
				continue;
			} else if (matcher.group(ParseGroup.equiv) != null) {
				entities.add(new EquationEntity(EntitySymbols.EQUIV));
				continue;
			} else if (matcher.group(ParseGroup.implies) != null) {
				entities.add(new EquationEntity(EntitySymbols.IMPLIES));
				continue;
			} else if (matcher.group(ParseGroup.not) != null) {
				entities.add(new EquationEntity(EntitySymbols.NOT));
				continue;
			} else if (matcher.group(ParseGroup.sample) != null) {
				entities.add(new EquationEntity(EntitySymbols.VAR, matcher.group()));
				continue;
			} else if (matcher.group(ParseGroup.leftParend) != null) {
				entities.add(new EquationEntity(EntitySymbols.LEFT_PAREND));
				continue;
			} else if (matcher.group(ParseGroup.rightParend) != null) {
				entities.add(new EquationEntity(EntitySymbols.RIGHT_PAREND));
				continue;
			}

		}
		return entities;

	}
}
