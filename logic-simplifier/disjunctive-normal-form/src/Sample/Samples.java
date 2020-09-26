package Sample;

/**
 * Tracks the information of the variables when going through the Disjunctive
 * Normal Form Converter and the Quine McCLuskey. More information can be added.
 * 
 * @author mavz1
 *
 */
public class Samples implements Comparable<Samples> {
	private String variableName;
	private boolean want = true;

	public Samples(Samples samples) {
		variableName = samples.getVariableName();
		want = samples.isWanted();
	}

	public Samples() {

	}

	public Samples(String varName, Boolean wanted) {
		variableName = varName;
		want = wanted;
	}

	public boolean isWanted() {
		return want;
	}

	public void setWant(boolean want) {
		this.want = want;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((variableName == null) ? 0 : variableName.hashCode());
		result = prime * result + (want ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Samples other = (Samples) obj;
		if (variableName == null) {
			if (other.variableName != null)
				return false;
		} else if (!variableName.equals(other.variableName))
			return false;
		if (want != other.want)
			return false;
		return true;
	}

	@Override
	public int compareTo(Samples o) {
		return this.variableName.compareTo(o.variableName);
	}

	@Override
	public String toString() {
		return variableName + " " + isWanted();
	}
}
