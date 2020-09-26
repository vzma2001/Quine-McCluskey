package Testing;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import Entities.EntitySymbols;
import Entities.EquationEntity;
import Entities.Node;
import Processes.GetUserInput;
import Processes.Parser;

class ParserTests {

	/**
	 * Checks if it recognizes a valid equation
	 */
	@Test
	public void testIsValid() {
		String equation = "A AND B";
		GetUserInput keyboard = new GetUserInput();
		assertTrue(keyboard.isValid(equation));
	}

	/**
	 * Checks if it recognizes an invalid equation dues to extra parenthesis
	 */
	@Test
	public void testIsValidOddAmountOfParenthesis() {
		String equation = "((A AND B)";
		GetUserInput keyboard = new GetUserInput();
		assertFalse(keyboard.isValid(equation));
	}

	/**
	 * Checks if it recognizes an invalid equation due to a missing variable
	 */
	@Test
	public void testIsValidMissingVariable() {
		String equation = "(A AND)";
		GetUserInput keyboard = new GetUserInput();
		assertFalse(keyboard.isValid(equation));
	}

	/**
	 * Checks if it recognizes an empty string is invalid
	 */
	@Test
	public void testIsValidEmptyString() {
		String equation = "";
		GetUserInput keyboard = new GetUserInput();
		assertFalse(keyboard.isValid(equation));
	}

	/**
	 * Tests if it processes the equation into equation entities correctly
	 */
	@Test
	public void testStringToList() {
		String equation = "(A AND B)";
		Parser parser = new Parser();
		ArrayList<EquationEntity> actual = parser.parse(equation);
		ArrayList<EquationEntity> expected = new ArrayList<>();
		expected.add(new EquationEntity(EntitySymbols.LEFT_PAREND));
		expected.add(new EquationEntity(EntitySymbols.VAR, "A"));
		expected.add(new EquationEntity(EntitySymbols.AND));
		expected.add(new EquationEntity(EntitySymbols.VAR, "B"));
		expected.add(new EquationEntity(EntitySymbols.RIGHT_PAREND));
		Boolean isTrue = expected.get(0).equals(actual.get(0));
		assertTrue(isTrue);
	}

	/**
	 * Tests if it processes the equation that contains multiple operands into
	 * equation entities correctly
	 */
	@Test
	public void testStringToListMultipleAndsAndOrs() {
		String equation = "(A AND B) OR !D";
		Parser parser = new Parser();
		ArrayList<EquationEntity> actual = parser.parse(equation);
		ArrayList<EquationEntity> expected = new ArrayList<>();
		expected.add(new EquationEntity(EntitySymbols.LEFT_PAREND));
		expected.add(new EquationEntity(EntitySymbols.VAR, "A"));
		expected.add(new EquationEntity(EntitySymbols.AND));
		expected.add(new EquationEntity(EntitySymbols.VAR, "B"));
		expected.add(new EquationEntity(EntitySymbols.RIGHT_PAREND));
		expected.add(new EquationEntity(EntitySymbols.OR));
		expected.add(new EquationEntity(EntitySymbols.NOT));
		expected.add(new EquationEntity(EntitySymbols.VAR, "D"));
		assertTrue(expected.equals(actual));
	}

	/**
	 * Tests if the operant converter converts correctly
	 */
	@Test
	public void testOperandConverter() {
		String equation = "A @ B";
		Parser parser = new Parser();
		Node actual = parser.simplifyOperations(parser.createTree(parser.parse(equation)));
		Node left = new Node(new EquationEntity(EntitySymbols.AND),
				new Node(new EquationEntity(EntitySymbols.VAR, "A")),
				new Node(new EquationEntity(EntitySymbols.NOT), new Node(new EquationEntity(EntitySymbols.VAR, "B"))));
		Node right = new Node(new EquationEntity(EntitySymbols.AND),
				new Node(new EquationEntity(EntitySymbols.NOT), new Node(new EquationEntity(EntitySymbols.VAR, "A"))),
				new Node(new EquationEntity(EntitySymbols.VAR, "B")));
		Node expected = new Node(new EquationEntity(EntitySymbols.OR), left, right);
		assertTrue(expected.equals(actual));
	}

	/**
	 * Tests if the equation is properly turned into a Node Tree
	 */
	@Test
	public void testTreeBuilder() {
		String equation = "(A AND B) OR !D";
		Parser parser = new Parser();
		Node actual = parser.createTree(parser.parse(equation));
		Node a = new Node(new EquationEntity(EntitySymbols.VAR, "A"));
		Node b = new Node(new EquationEntity(EntitySymbols.VAR, "B"));
		Node d = new Node(new EquationEntity(EntitySymbols.VAR, "D"));
		Node and = new Node(new EquationEntity(EntitySymbols.AND), a, b);
		Node expected = new Node(new EquationEntity(EntitySymbols.OR), and,
				new Node(new EquationEntity(EntitySymbols.NOT), d));
		assertTrue(expected.equals(actual));
	}

	/**
	 * Tests if it properly pushes the or's down through distribution. Should result
	 * in disjunctive normal form
	 */
	@Test
	public void testPushOrDown() {
		String equation = "(A OR B) AND !D";
		Parser parser = new Parser();
		Node actual = parser.createTree(parser.parse(equation));
		boolean madeChange = false;
		do {
			madeChange = parser.pushAllOrDown(actual);
		} while (madeChange);
		Node a = new Node(new EquationEntity(EntitySymbols.VAR, "A"));
		Node b = new Node(new EquationEntity(EntitySymbols.VAR, "B"));
		Node d = new Node(new EquationEntity(EntitySymbols.VAR, "D"));
		Node not = new Node(new EquationEntity(EntitySymbols.NOT), d);
		Node left = new Node(new EquationEntity(EntitySymbols.AND), a, not);
		Node right = new Node(new EquationEntity(EntitySymbols.AND), b, not);
		Node expected = new Node(new EquationEntity(EntitySymbols.OR), left, right);
		assertTrue(expected.equals(actual));
	}

	/**
	 * Test if it pushes nots down to the variables correctly.
	 */
	@Test
	public void testPushNotDown() {
		String equation = "!(A AND B)";
		Parser parser = new Parser();
		Node actual = parser.createTree(parser.parse(equation));
		actual = parser.pushDownNots(actual);
		Node notA = new Node(new EquationEntity(EntitySymbols.NOT),
				new Node(new EquationEntity(EntitySymbols.VAR, "A")));
		Node notB = new Node(new EquationEntity(EntitySymbols.NOT),
				new Node(new EquationEntity(EntitySymbols.VAR, "B")));
		Node expected = new Node(new EquationEntity(EntitySymbols.OR), notA, notB);
		assertTrue(expected.equals(actual));
	}
}
