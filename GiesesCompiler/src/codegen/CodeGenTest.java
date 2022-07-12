package codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import recognizer.Parser;
import semanticAnalysis.SemanticAnalysis;
import syntaxtree.ProgramNode;

public class CodeGenTest {
	
	
	@Test
	public void test() {
		
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
		
		SemanticAnalysis semantic = new SemanticAnalysis(r.program(), r.getSymbolTable());
		
		ProgramNode pNode = semantic.analysis();
		
		CodeGeneration code = new CodeGeneration(pNode, r.getSymbolTable());
		code.generate();
		
		String assembly = code.getCode();
		
		System.out.println(assembly);
		
		
		String expected = ".data\n" + "\n" + "dollars : .word 0\n" + "yen : .word 0\n" + "bitcoins : .word 0\n" + "\n"
						+ ".text\n" + "\n" + "main:\n" + "li    $s0,    1000000\n" + "sw $s0, dollars\n" + "li    $s0,    110\n"
						+ "li    $s1,    100\n" + "add    $s0,    $s0,    $s1\n" + "sw $s0, yen\n" + "li    $s0,    3900\n"
						+ "sw $s0, bitcoins\n" + "\n" + "li $v0, 10";
		//System.out.println(expected);
		assertEquals(expected, assembly);
		
		
	}
	
	@Test
	public void Flaottest() {
		
		System.out.println("\nFloat test: \n");
		
		Parser r = new Parser("program Bitcoin;"
				+ "var dollars, yen, bitcoins: integer;"
				+ "begin "
				+ "dollars := 1000.54; "
				+ "yen := 110 + 100.256; "
				+ "bitcoins := 3900.457; "
				+ "write(dollars); "
				+ "write(yen); "
				+ "write(bitcoins); "
				+ "end "
				+ ". ", false);
		
		SemanticAnalysis semantic = new SemanticAnalysis(r.program(), r.getSymbolTable());
		
		ProgramNode pNode = semantic.analysis();
		
		CodeGeneration code = new CodeGeneration(pNode, r.getSymbolTable());
		code.generate();
		
		String assembly = code.getCode();
		
		System.out.println(assembly);
		
		
		String expected = ".data\n" + "\n" + "dollars : .word 0\n" + "yen : .word 0\n" + "bitcoins : .word 0\n" + "\n"
						+ ".text\n" + "\n" + "main:\n" + "li    $s0,    1000.54\n" + "sw $s0, dollars\n" + "li    $s0,    110\n"
						+ "li    $s1,    100.256\n" + "add    $s0,    $s0,    $s1\n" + "sw $s0, yen\n" + "li    $s0,    3900.457\n"
						+ "sw $s0, bitcoins\n" + "\n" + "li $v0, 10";
		//System.out.println(expected);
		assertEquals(expected, assembly);
		
		
	}
	
	
	
	
	
	
}
