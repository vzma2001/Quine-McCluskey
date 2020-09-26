package Visualization;

import java.util.ArrayList;
import java.util.List;

import Entities.Node;

/**
 * Creates and prints a visualization of the nodes in the console
 * 
 * @author mavz1
 *
 */
public class TreePrinter {

	public static final String LEFT = "|";// "\u250C"; //218;
	public static final String RIGHT = "|";// "\u2510";//191;
	public static final String DASH = "-";
	public static final String SPLIT_LEFT = "|";// "\u2518"; //217;
	public static final String SPLIT_RIGHT = "|";// "\u2514"; //192;
	public static final String SPLIT_BOTH = "|";// "\u2534"; //193;

	public static void print(Node root) {
		ArrayList<List<String>> lines = new ArrayList<List<String>>();
		ArrayList<Node> level = new ArrayList<Node>();
		ArrayList<Node> next = new ArrayList<Node>();

		level.add(root);
		int nn = 1;

		int widest = 0;

		while (nn != 0) {
			ArrayList<String> line = new ArrayList<String>();

			nn = 0;

			for (Node n : level) {
				if (n == null) {
					line.add(null);
					next.add(null);
					next.add(null);
				} else {
					String aa = n.getNodeChar().toString();
					line.add(aa);
					if (aa.length() > widest)
						widest = aa.length();

					next.add(n.getLeftNode());
					next.add(n.getRightNode());

					if (n.getLeftNode() != null)
						nn++;
					if (n.getRightNode() != null)
						nn++;
				}
			}

			if (widest % 2 == 1)
				widest++;

			lines.add(line);

			ArrayList<Node> tmp = level;
			level = next;
			next = tmp;
			next.clear();
		}

		int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
		for (int i = 0; i < lines.size(); i++) {
			List<String> line = lines.get(i);
			int hpw = (int) Math.floor(perpiece / 2f) - 1;

			if (i > 0) {
				for (int j = 0; j < line.size(); j++) {

					// split node
					String c = " ";
					if (j % 2 == 1) {
						if (line.get(j - 1) != null) {
							c = (line.get(j) != null) ? SPLIT_BOTH : SPLIT_LEFT;
						} else {
							if (j < line.size() && line.get(j) != null)
								c = SPLIT_RIGHT;
						}
					}
					System.out.print(c);

					// lines and spaces
					if (line.get(j) == null) {
						for (int k = 0; k < perpiece - 1; k++) {
							System.out.print(" ");
						}
					} else {

						for (int k = 0; k < hpw; k++) {
							System.out.print(j % 2 == 0 ? " " : DASH);
						}
						System.out.print(j % 2 == 0 ? LEFT : RIGHT);
						for (int k = 0; k < hpw; k++) {
							System.out.print(j % 2 == 0 ? DASH : " ");
						}
					}
				}
				System.out.println();
			}

			// print line of numbers
			for (int j = 0; j < line.size(); j++) {

				String f = line.get(j);
				if (f == null)
					f = "";
				int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
				int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

				// a number
				for (int k = 0; k < gap1; k++) {
					System.out.print(" ");
				}
				System.out.print(f);
				for (int k = 0; k < gap2; k++) {
					System.out.print(" ");
				}
			}
			System.out.println();

			perpiece /= 2;
		}
	}
}
