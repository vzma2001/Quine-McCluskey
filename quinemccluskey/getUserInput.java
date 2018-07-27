package quinemccluskey;

import java.util.Scanner;

public class getUserInput {
	private static Scanner scanner = new Scanner(System.in);

	public static String getString() {
		String userInputtedString = "";
		System.out.println("Enter minterms: ");

		userInputtedString = scanner.nextLine();
		userInputtedString = userInputtedString.trim();
		return userInputtedString;
	}
}
