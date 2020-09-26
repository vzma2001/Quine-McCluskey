This program implements the Quine-McCluskey Algorithm for simplifying Boolean equations.

Program takes in minterms in terms of 1,0, and -. The - represents both 1 and 0. 1 represents true and 0 represents false.

The program first groups these terms depending on how many 1's the term has. It combines the terms that have one difference until it is no longer able to combine

The  prime implicants are found and the terms are put into a map. Finds the prime terms, terms that are the only one in it's column, and removes other terms.

## Using the program
Enter in the wanted minterms to combine and simplify.

These terms should be made up of '1', '0', and '-'.

'-' will represent both 1 and 0

1 means true and 0 means false

There are multiple correct answers at times. This will only give one version.

## Code for Running
```
String[] alphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		String listOfNums = "";
		ArrayList<Term> termList = new ArrayList<Term>();
		QuineMcCluskey simplifier = new QuineMcCluskey();

		listOfNums = simplifier.getUserInput();

		termList = simplifier.parseTerms(listOfNums, new ArrayList<String>());

		boolean taut = simplifier.isTautology(termList, new ArrayList<String>(Arrays.asList(alphabet)));
		termList = simplifier.simplify(termList, new ArrayList<String>());

		simplifier.display(termList);

```
## Authors
* **Vincent Ma**
