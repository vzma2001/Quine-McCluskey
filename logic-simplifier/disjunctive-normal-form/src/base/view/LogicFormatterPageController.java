package base.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;

import Entities.Node;
import Processes.DNFRunner;
import Processes.GetUserInput;
import Processes.NodeToTerm;
import Sample.Samples;
import base.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import quinemccluskey.QuineMcCluskey;
import quinemccluskey.Term;

public class LogicFormatterPageController {
	@FXML
	private MainApp parent;
	@FXML
	private HBox outerPane;
	@FXML
	private TextFlow outputTextFlow;
	@FXML
	private TextField inputTextField;

	private int oldCaretPosition = 0;
	private IndexRange selectedText;
	private boolean hasSelectedText;
	private static final String sample = "sample", and = "and", or = "or", not = "not", leftParend = "leftParend",
			rightParend = "rightParend";
	private static final String[] groupNames = { sample, and, or, not, leftParend, rightParend };

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		inputTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				oldCaretPosition = inputTextField.getCaretPosition();
				selectedText = inputTextField.getSelection();
				hasSelectedText = selectedText.getLength() > 0;
			}
		});
	}

	/**
	 * When this button is pressed, it will simplify the equation
	 */
	@FXML
	private void runProcessing() {

		String input = inputTextField.getText();
		GetUserInput keyboard = new GetUserInput();
		if (keyboard.isValid(input)) {
			DNFRunner dnfRunner = new DNFRunner();
			Node node = dnfRunner.run(input);
			NodeToTerm nodeConvertor = new NodeToTerm();
			ArrayList<String> allLetters = nodeConvertor.setAllLetters(node);
			ArrayList<Samples> allSamples = new ArrayList<Samples>();
			allSamples = nodeConvertor.getAllSamples(allSamples, node);
			ArrayList<Term> termList = nodeConvertor.convertNodeToTerm(node, allSamples, allLetters);
			QuineMcCluskey simplifier = new QuineMcCluskey();
			termList = simplifier.simplify(termList, allLetters);
			simplifier.display(termList);
			setOutputTextFlow(termList);
		} else {
			// showError(keyboard.getException(input));
			outputTextFlow.getChildren().clear();
			Text text = new Text("\nImproper input.\nPlease Try Again");
			text.setFill(Color.RED);
			text.setFont(Font.font("Helvetica", 40));
			outputTextFlow.getChildren().add(text);
		}
	}

	/**
	 * Takes in an exception it will say what was expected
	 * 
	 * @param e
	 */
	public void showError(Exception e) {
		outputTextFlow.getChildren().clear();
		String toAdd = e.getMessage().substring(e.getMessage().indexOf("Expected"));
		String temp = e.getMessage().substring(e.getMessage().indexOf("but found ") + 10);
		int end = temp.indexOf(" ");
		end = e.getMessage().indexOf("but found ") + end - 3;
		toAdd = toAdd.substring(0, end);
		Text text = new Text(toAdd + "\nImproper input.\nPlease Try Again");
		text.setFill(Color.RED);

		text.setFont(Font.font("Helvetica", 25));
		outputTextFlow.getChildren().add(text);
	}

	/**
	 * Sets the output display
	 * 
	 * @param termList
	 */
	private void setOutputTextFlow(ArrayList<Term> termList) {
		// Clear the display
		outputTextFlow.getChildren().clear();

		for (int i = 0; i < termList.size(); i++) {

			if (i != 0) {
				Text or = new Text(" OR ");
				or.setFill(Color.GREEN);
				or.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
				outputTextFlow.getChildren().add(or);
			}

			Text leftParend = new Text(" (");
			leftParend.setFill(Color.BLACK);
			leftParend.setFont(Font.font("Helvetica", 40));
			outputTextFlow.getChildren().add(leftParend);

			ArrayList<Samples> samples = termList.get(i).getSample();
			HashSet<Samples> set = new HashSet<>(samples);
			samples = new ArrayList<Samples>(set);
			for (int j = 0; j < samples.size(); j++) {
				if (j != 0) {
					Text and = new Text(" AND ");
					and.setFill(Color.BLUE);
					and.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
					outputTextFlow.getChildren().add(and);
				}
				String toAdd = samples.get(j).getVariableName();
				if (!samples.get(j).isWanted()) {
					Text not = new Text(" NOT ");
					not.setFill(Color.RED);
					not.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
					outputTextFlow.getChildren().add(not);
				}

				Text text = new Text(toAdd);
				text.setFill(Color.BLACK);
				text.setFont(Font.font("Helvetica", FontPosture.ITALIC, 40));
				outputTextFlow.getChildren().add(text);
			}
			Text rightParend = new Text(") ");
			rightParend.setFill(Color.BLACK);
			rightParend.setFont(Font.font("Helvetica", 40));
			outputTextFlow.getChildren().add(rightParend);

		}
	}

	private String getGroupName(Matcher matcher) {
		int index = getGroupIndex(matcher);

		// subtract because groups start at 1 but our array starts at 0.
		return groupNames[index - 1];
	}

	private int getGroupIndex(Matcher matcher) {
		int index = 0;
		for (int i = 1; i <= matcher.groupCount(); i++) {
			if (matcher.group(i) != null) {
				index = i;
				break;
			}
		}

		return index;
	}

	@FXML
	private void xorButtonPressed() {
		insertIntoTextInput(" XOR ", oldCaretPosition);
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 5);

	}

	@FXML
	private void equivButtonPressed() {
		insertIntoTextInput(" EQUIV ", oldCaretPosition);
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 7);

	}

	@FXML
	private void impliesButtonPressed() {
		insertIntoTextInput(" IMPLIES ", oldCaretPosition);
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 9);

	}

	@FXML
	private void andButtonPressed() {
		insertIntoTextInput(" AND ", oldCaretPosition);
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 5);

	}

	@FXML
	private void orButtonPressed() {
		insertIntoTextInput(" OR ", oldCaretPosition);
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 4);

	}

	@FXML
	private void notButtonPressed() {

		if (hasSelectedText) {
			insertIntoTextInput(" ) ", selectedText.getEnd());
			insertIntoTextInput(" NOT ( ", selectedText.getStart());
		} else {
			insertIntoTextInput(" NOT ", oldCaretPosition);
		}
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 5);

	}

	@FXML
	private void leftParendButtonPressed() {
		insertIntoTextInput(" ( ", oldCaretPosition);
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 3);

	}

	@FXML
	private void rightParendButtonPressed() {
		insertIntoTextInput(" ) ", oldCaretPosition);
		inputTextField.requestFocus();
		inputTextField.positionCaret(oldCaretPosition + 3);
	}

	@FXML
	private void groupButtonPressed() {

		if (hasSelectedText) {
			insertIntoTextInput(" ) ", selectedText.getEnd());
			insertIntoTextInput(" ( ", selectedText.getStart());
		} else {
			insertIntoTextInput(" (  ) ", oldCaretPosition);
		}

	}

	private void insertIntoTextInput(String text, int index) {
		String oldText = inputTextField.getText();
		inputTextField.setText(oldText.substring(0, index) + text + oldText.substring(index));
	}

}
