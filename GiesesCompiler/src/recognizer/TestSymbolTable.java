package recognizer;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import recognizer.SymbolTable;
import recognizer.SymbolTable.Type;


/**
 * Class to test the adding and checking of the symbol table
 * @author Sam Giese
 *
 */
public class TestSymbolTable {
	
	SymbolTable table = new SymbolTable();
	
	//Tests the add function into the symbol table, and also the "is" checking functions
	@Test
	public void testAdd() {
		table.add("program", Type.PROGRAM);
		table.add("procedure", Type.PROCEDURE);
		table.add("variable", Type.VARIABLE);
		table.add("function", Type.FUNCTION);
		
		System.out.println(table.toString());
		
		assertTrue(table.isProgramName("program"));
		assertTrue(table.isProcedureName("procedure"));
		assertTrue(table.isVariableName("variable"));
		assertTrue(table.isFunctionName("function"));
		
		assertFalse(table.isProgramName("porgam"));
		assertFalse(table.isProcedureName("porcedure"));
		assertFalse(table.isVariableName("varyable"));
		assertFalse(table.isFunctionName("funcxion"));
		
	}
	
	
	
	
	
}
