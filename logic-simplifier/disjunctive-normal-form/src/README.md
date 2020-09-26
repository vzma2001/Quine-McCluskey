# Disjunctive Normal Form Converter

This program takes in a boolean expression and will convert it into Disjunctive Normal Form.
Disjunctive Normal Form is when groups of variables are connected with and's and these groups are connected by or's
This is meant to be combined with the Quine McCluskey program, however, this can be used individually.
## Using the program
Enter the boolean equation and submit. Valid operands are AND, NOT, OR, XOR, EQUIV, and IMPLIES.
When writing equations, each operand MUST be paired with parenthesis. Each parenthesis must have two entities and an operand.
An entity can be either a variable, a variable and a not, or a complete parenthesis group.
Ex: 
	VALID: ((A AND B) AND !C) OR D
	INVALID; (A AND B AND !C) OR D
	
## Code Setup
```
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
```

## Authors
* **Vincent Ma**
