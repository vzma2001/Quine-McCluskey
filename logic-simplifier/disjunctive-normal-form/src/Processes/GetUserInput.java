package Processes;

import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import Entities.ParseGroup;
import base.view.LogicFormatterPageController;

public class GetUserInput {

	/**
	 * User inputs a boolean expression until it is valid
	 * 
	 * @return valid expression
	 */
	public String getUserInput() {
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter the equation: ");
		String toReturn = keyboard.nextLine();
		while (!isValid(toReturn)) {
			System.out.println("Invalid input. Try again");
			System.out.println("Enter the equation: ");
			toReturn = keyboard.nextLine();
		}
		keyboard.close();
		return toReturn;
	}

	/**
	 * Checks if the string the user input is a valid boolean expression
	 * 
	 * @param equation
	 * @return if the equation is valid
	 */
	public boolean isValid(String equation) {
		try {
			// Replace operators (Does not replace with proper character, just makes sure it
			// is formatted correct)
			equation = equation.replaceAll("(?i)" + ParseGroup.andPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.orPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.xorPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.equivPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.impliesPattern, "||");
			equation = equation.replaceAll("(?i)" + ParseGroup.notPattern, "!");

			// Surround anything that starts with a $, followed by a number with
			// quotes.
			equation = equation.replaceAll("(\\w+)", "'$1'");

			// System.out.println(equation);

			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine se = sem.getEngineByName("JavaScript");
			String myExpression = " ( " + equation + " ) == ( " + equation + " ) ";
			System.out.println(se.eval(myExpression));

		} catch (ScriptException e) {
			return false;
		}
		return true;
	}

	public Exception getException(String equation) {
		try {
			// Replace operators (Does not replace with proper character, just makes sure it
			// is formatted correct)
			equation = equation.replaceAll("(?i)" + ParseGroup.andPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.orPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.xorPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.equivPattern, "&&");
			equation = equation.replaceAll("(?i)" + ParseGroup.impliesPattern, "||");
			equation = equation.replaceAll("(?i)" + ParseGroup.notPattern, "!");

			// Surround anything that starts with a $, followed by a number with
			// quotes.
			equation = equation.replaceAll("(\\w+)", "'$1'");

			// System.out.println(equation);

			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine se = sem.getEngineByName("JavaScript");
			String myExpression = " ( " + equation + " ) == ( " + equation + " ) ";
			System.out.println(se.eval(myExpression));

		} catch (ScriptException e) {
			return e;
		}
		return null;
	}
}
