package recognizer;
import java.util.LinkedHashMap;

/**
 * A table to place all identifiers and types into
 * @author Sam Giese
 *
 */
public class SymbolTable {
	
	private LinkedHashMap<String, Type> table;
	
	
	/**
	 * Constructor that makes the symbol table
	 */
	public SymbolTable() {
		table = new LinkedHashMap<>();
	}
	
	
	/**
	 * adds the identifier into the table
	 * @param name	the name of the identifier
	 * @param type	the identifiers type (program, procedure, function, variable)
	 */
	public void add(String name, Type kind) {
		
		table.put(name, kind);
		
	}
	
	
	
	
	
	
	
	/**
	 * Checks to see if the table contains the given program name
	 * @param name the name to check for
	 * @return true if found, false if not
	 */
	public Boolean isProgramName(String name) {
		
		if(table.containsKey(name) && table.get(name) == Type.PROGRAM) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	/**
	 * Checks to see if the table contains the given procedure name
	 * @param name the name to check for
	 * @return true if found, false if not
	 */
	public Boolean isProcedureName(String name) {
		
		if(table.containsKey(name) && table.get(name) == Type.PROCEDURE) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	/**
	 * Checks to see if the table contains the given variable name
	 * @param name the name to check for
	 * @return true if found, false if not
	 */
	public Boolean isVariableName(String name) {
		
		if(table.containsKey(name) && table.get(name) == Type.VARIABLE) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks to see if the table contains the given function name
	 * @param name the name to check for
	 * @return true if found, false if not
	 */
	public Boolean isFunctionName(String name) {
		
		if(table.containsKey(name) && table.get(name) == Type.FUNCTION) {
			return true;
		}
		else {
			return false;
		}
	}
	

	/**
	 * adds the program name into the table
	 * @param name	the name of the program
	 */
	public void addProgramName(String name) {
		
		this.add(name, Type.PROGRAM);
		
	}
	
	/**
	 * adds the variable name into the table
	 * @param name	the name of the variable
	 */
	public void addVariableName(String name) {
		
		this.add(name, Type.VARIABLE);
		
	}
	
	
	/**
	 * adds the functions name into the table
	 * @param name	the name of the function
	 */
	public void addFunctionName(String name) {
		
		this.add(name, Type.FUNCTION);
		
	}
	
	
	/**
	 * adds the procedures name into the table
	 * @param name	the name of the procedure
	 */
	public void addProcedureName(String name) {
		
		this.add(name, Type.PROCEDURE);
		
	}
	
	
	
	
	public String toString() {
		
		String returnTable = " Symbol  |  Kind ";
		for(String name : table.keySet()) {
			
			String key = name.toString();
			String value = table.get(name).toString();
			returnTable += "\n " + value + ": " + key;
			
		}
		
		return returnTable;
	}
	
	/**
	 * method that returns the toString of the symbol table
	 */
	public String getSymString() {
		return table.toString();
	}
	
	
	
	public LinkedHashMap<String, Type> getTable() {
		return table;
	}
	
	/**
	 * class that declares the types of symbols to be added to the table
	 * @author Sam Giese
	 *
	 */
	public enum Type{
		PROGRAM, VARIABLE, FUNCTION, PROCEDURE;
	}
	
	
	
}
