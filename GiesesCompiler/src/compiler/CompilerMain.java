package compiler;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import codegen.CodeGeneration;
import recognizer.Parser;
import semanticAnalysis.SemanticAnalysis;
import syntaxtree.ProgramNode;


/**
 * Main class for the compiler
 * @author Sam Giese
 *
 */
public class CompilerMain {
	
	public static Parser r;
	public static String file;
	public static SemanticAnalysis semantic;
	public static CodeGeneration code;
	
	
	
	
	
	public static void main(String[] args) {
		
		file = args[0];
		
		/*Parser r = new Parser("program Bitcoin;"
				+ "var dollars, yen, bitcoins: integer;"
				+ "begin "
				+ "dollars := 1000000; "
				+ "yen := 110 + 100; "
				+ "bitcoins := 3900; "
				+ "write(dollars); "
				+ "write(yen); "
				+ "write(bitcoins); "
				+ "end "
				+ ". ", false);*/
		//Parser r = new Parser("testPascal.txt", true);
		
		r = new Parser(file, true);
		String noExtFile = file.substring(0, file.lastIndexOf('.'));
		
		//String newfile = "bitcoin";
		
		ProgramNode pNode = r.program();
		
		semantic = new SemanticAnalysis(pNode, r.getSymbolTable());
		
		pNode = semantic.analysis();
		
		String tree = pNode.indentedToString(0);
		
		String symTable = r.getSymbolTableStr();
		
		code = new CodeGeneration(pNode, r.getSymbolTable());
		
		code.generate();
		
		String assembly = code.getCode();
		
		
		PrintWriter codeWriter;
		try {
			codeWriter = new PrintWriter(noExtFile + ".asm");
			codeWriter.println(assembly);
			codeWriter.close();
		} catch(FileNotFoundException e){
			System.out.println("Problem creating the asm file");
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}

}
