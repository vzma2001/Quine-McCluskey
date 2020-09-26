# Logic Simplifier

This is a Logic Simplifier that will take in a boolean expression and convert it
into Disjunctive Normal Form and then use the Quine McCluskey algorithm to simplify it.


## Usage
The user can enter boolean expressions into the text bar. Variables MUST be grouped in groups of two within parenthesis.

Ex: 

    Proper Input:((A AND B) AND C) OR D

    Improper Input: (A AND B AND C) OR D
    
When running, buttons linked to different boolean operands will be available. 

The user can either use these or type the expression out fully.

The " (...) " button allows the user to highlight a section of their input and surround it with parenthesis

To simplify the input, press the "RunProcessing" Button. If the input is valid, the simplified answer will be output below the input text box.

## Valid Operands and Symbols:
    
* **Valid And Symbols:** _"AND", "&&", "*"_
* **Valid Or Symbols:** _"OR", "||", "+"_
* **Valid XOr Symbols:** _"XOR", "^^", "@", "^"_
* **Valid Equiv Symbols:** _"EQUIV", "--", "=", "-"_
* **Valid Implies Symbols:** _"IMPLIES", ">"_
* **Valid Not Symbols:** _"NOT", "!", "~"_
* **Valid Left Parenthesis Symbols:**  _"("_
* **Valid Right Parenthesis Symbols:** _")"_
    

## Authors
* **Vincent Ma**