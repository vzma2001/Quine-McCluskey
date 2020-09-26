package Entities;

import java.util.*;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

/**
 * Contains the basic information of different variables and operands for use in
 * nodes.
 * 
 * @author mavz1
 *
 */
public class EquationEntity {
	private String termName;
	private EntitySymbols symbol;
	private EntityCategories type;
	private final static String[] OPERATIONS = { "*", "+", "-", "@", ">" };
	private final static String NOT = "!";
	private final static ArrayList<String> OPERATION_ARRAY_LIST = new ArrayList<String>(Arrays.asList(OPERATIONS));

	public EquationEntity(EntitySymbols symbol, String name) {
		this.symbol = symbol;

		if (this.symbol.equals(EntitySymbols.VAR)) {
			this.termName = name;
		} else {
			this.termName = symbol.toString();
		}
	}

	public EquationEntity(EntitySymbols symbol) {
		this(symbol, null);
	}

	public boolean isType(EntityCategories type) {
		return this.type.equals(type);
	}

	public boolean isSymbol(EntitySymbols symbol) {
		return this.symbol.name().equals(symbol.name());
	}

	public String getTermName() {
		return termName;
	}

	public EntityCategories getType() {
		return type;
	}

	@Override
	public String toString() {
		return termName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((termName == null) ? 0 : termName.hashCode());
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
		EquationEntity other = (EquationEntity) obj;
		if (termName == null) {
			if (other.termName != null)
				return false;
		} else if (!termName.equals(other.termName))
			return false;
		return true;
	}

}
