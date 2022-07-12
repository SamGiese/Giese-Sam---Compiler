package semanticAnalysis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import recognizer.Parser;

public class SemanticTest {
	
	
	//legal program test
	@Test
	public void testSemantic() {
		
		System.out.println("\ntest semantic \n");
		
		Parser r = new Parser("program Bitcoin;"
				+ "var dollars, yen, bitcoinds: integer;"
				+ "begin "
				+ "dollars := 1000000; "
				+ "yen := dollars * 110; "
				+ "bitcoins := potato / 3900; "
				+ "write(dollars); "
				+ "write(yen) "
				+ "write(bitcoins); "
				+ "end "
				+ ".", false);
		
		SemanticAnalysis s = new SemanticAnalysis(r.program(), r.getSymbolTable());
		
		String tree = s.analysis().indentedToString(0);
		
		System.out.println(tree);
		
	}
	
	
	//if then test
	@Test
	public void ifTest() {
		
		System.out.println("\niftest: \n");
		Parser r = new Parser("program ifThen;"
				+ "var a, b, c, d: integer;"
				+ "begin "
				+ "a := 4; "
				+ "b := 5;"
				+ "if a < b"
				+ "then"
				+ "a:= 1"
				+ "else"
				+ "b := 2"
				+ "end "
				+ ".", false);
		
		SemanticAnalysis s = new SemanticAnalysis(r.program(), r.getSymbolTable());
		
		String tree = s.analysis().indentedToString(0);
		
		System.out.println(tree);
		
		
	}
	
	
	@Test
	public void EasyTest() {
		System.out.println("\nEasyTest: \n");
		Parser r = new Parser("program Bitcoin;"
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
		
		SemanticAnalysis s = new SemanticAnalysis(r.program(), r.getSymbolTable());
		String tree = s.analysis().indentedToString(0);
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
	
	@Test
	public void FloatTest() {
		
		Parser r = new Parser("program Bitcoin;"
				+ "var dollars, yen, bitcoins: float;"
				+ "begin "
				+ "dollars := 1000.15; "
				+ "yen := 110 + 100.89; "
				+ "bitcoins := 3900.25; "
				+ "write(dollars); "
				+ "write(yen); "
				+ "write(bitcoins); "
				+ "end "
				+ ". ", false);
		System.out.println("\nFloat test: \n");
		
		SemanticAnalysis s = new SemanticAnalysis(r.program(), r.getSymbolTable());
		String tree = s.analysis().indentedToString(0);
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
				"|-- --- --- Value: 1000.15\r\n" + 
				"|-- --- Assignment\r\n" + 
				"|-- --- --- Name: yen\r\n" + 
				"|-- --- --- Operation: PLUS\r\n" + 
				"|-- --- --- --- Value: 110\r\n" + 
				"|-- --- --- --- Value: 100.89\r\n" + 
				"|-- --- Assignment\r\n" + 
				"|-- --- --- Name: bitcoins\r\n" + 
				"|-- --- --- Value: 3900.25";
		
		assertEquals(expected, tree);
		
		
		
	}
	
	
	
	
	
	
}
