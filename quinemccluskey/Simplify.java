package quinemccluskey;

import java.util.ArrayList;

public class Simplify {

	public static Boolean combine(ArrayList<Term> termList, int length) {

		int differences = 0;
		int position = 0;
		String column;
		ArrayList<Term> combinedList = new ArrayList<Term>();
		ArrayList<Term> termList1 = new ArrayList<Term>();
		ArrayList<Term> termList2 = new ArrayList<Term>();
		ArrayList<Term> usedTerms = new ArrayList<>();
		Boolean wasCombined = false;
		for (Term term : termList) {

			int groupNum = term.getGroup();

			for (int i = 0; i < termList.size(); i++) {
				if (termList.get(i).getGroup() == groupNum) {
					termList1.add(termList.get(i));
				}

				if (termList.get(i).getGroup() == groupNum + 1) {
					termList2.add(termList.get(i));

				}
			}
			for (int i = 0; i < termList1.size(); i++) {
				for (int j = 0; j < termList2.size(); i++) {
					for (int k = 0; k < length; k++) {
						if (termList1.get(i).getCombo().charAt(k) != termList1.get(i).getCombo().charAt(k)) {
							differences++;
							position = k;
						}
					}
					if (differences == 1) {
						String newCombo = termList1.get(i).getCombo().substring(0, position - 1) + '-'
								+ termList1.get(i).getCombo().substring(0, position + 1);
						column = termList1.get(i).getColumns() + " " + termList2.get(j).getColumns();
						combinedList.add(new Term(newCombo, column));
						usedTerms.add(termList1.get(i));
						usedTerms.add(termList2.get(j));
						wasCombined = true;
					}
					differences = 0;
					
				}
			}

		}
		termList.removeAll(usedTerms);
		termList.addAll(combinedList);
		return wasCombined;
		
	}
}
