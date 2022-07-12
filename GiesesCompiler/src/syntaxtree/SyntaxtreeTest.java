package syntaxtree;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import recognizer.Parser;

public class SyntaxtreeTest {
	
	/*@Test
	public void test() {
		Recognizer p = new Recognizer("program Bitcoin;"
				+ "var dollars, yen, bitcoins: integer;"
				+ "begin "
				+ "dollars := 1000000; "
				+ "yen := dollars * 110; "
				+ "bitcoins := yen / 3900; "
				+ "write(dollars); "
				+ "write(yen); "
				+ "write(bitcoins); "
				+ "end "
				+ ". ", false);
		
		String tree = p.program().indentedToString(0);
		System.out.println(tree);
		
		
		
		

		
		
		
		
	}

	}*/

	
	@Test
	public void tempTest() {
		Parser p = new Parser("program Bitcoin; begin end.", false);
		
		String tree = p.program().indentedToString(0);
		System.out.println(tree);
	}
	
	@Test
	public void EasyTest() {
		Parser p = new Parser("program Bitcoin;"
				+ "var dollars, yen, bitcoins: integer;"
				+ "begin "
				+ "dollars := 1000000; "
				+ "yen := 110 + 100; "
				+ "bitcoins := 3900; "
				+ "write(dollars); "
				+ "write(yen); "
				+ "write(bitcoins); "
				+ "end "
				+ ". ", false);
		
		String tree = p.program().indentedToString(0);
		System.out.println(tree);
		
		String expected = "Program: Bitcoin\r\n" + 
				"|-- Declarations\r\n" + 
				"|-- --- Name: dollars\r\n" + 
				"|-- --- Name: yen\r\n" + 
				"|-- --- Name: bitcoins\r\n" + 
				"|-- SubProgramDeclarations\r\n" + 
				"|-- Compound Statement\r\n" + 
				"|-- --- Assignment\r\n" + 
				"|-- --- --- Name: dollars\r\n" + 
				"|-- --- --- Value: 1000000\r\n" + 
				"|-- --- Assignment\r\n" + 
				"|-- --- --- Name: yen\r\n" + 
				"|-- --- --- Operation: PLUS\r\n" + 
				"|-- --- --- --- Value: 110\r\n" + 
				"|-- --- --- --- Value: 100\r\n" + 
				"|-- --- Assignment\r\n" + 
				"|-- --- --- Name: bitcoins\r\n" + 
				"|-- --- --- Value: 3900";
		
		assertEquals(expected, tree);
		
		
		
	}
	
	
	
}
