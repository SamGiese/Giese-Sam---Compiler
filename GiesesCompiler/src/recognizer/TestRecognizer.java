package recognizer;
import static org.junit.Assert.*;

import org.junit.Test;

import recognizer.Parser;

public class TestRecognizer {
	
	/**
	 * Program happy test:
	 * Tests to see if the given pascal code passes the recognizer: Should pass
	 */
	@Test
	public void testProgramHappy() {
		Parser r = new Parser("program foo; begin end.", false);
		System.out.println("Test Program Happy path:");
		
		try {
			r.program();
			System.out.println("Program happy path success");
		}
		catch(Exception e){
			fail();
			System.out.println("Program happy path fail");
		}
	}
	
	/**
	 * Program sad test:
	 * Tests to see if the given pascal code gets caught by the the recognizer: Should not pass
	 */
	@Test
	public void testProgramSad() {
		Parser r = new Parser("program Lesson;; Begin. End", false);
		System.out.println("Test program sad path:");
		
		try {
			r.program();
			System.out.println("Program sad path success. Means it actually failed.");
			fail();
		}
		catch(Exception e) {
			
			System.out.println("Program sad path fail --- should fail");
		}
	}
	
	/**
	 * Declarations happy test:
	 * Tests to see if the given pascal code passes the recognizer: Should pass
	 */
	@Test
	public void testDeclarationsHappy() {
		Parser r = new Parser("var sam, foo: integer;", false);
		System.out.println("Test declarations happy path:");
		
		try {
			r.declarations();
			System.out.println("Declarations happy path success");
		}
		catch(Exception e){
			fail();
			System.out.println("Declarations happy path fail");
		}
	}
	
	/**
	 * Declarations sad test:
	 * Tests to see if the given pascal code gets caught by the the recognizer: Should not pass
	 */
	@Test
	public void testDeclarationsSad() {
		Parser r = new Parser("var sam, foo; integer;", false);
		System.out.println("Test declarations sad path:");
		
		try {
			r.declarations();
			System.out.println("Declarations sad path success. Means it actually failed.");
			fail();
		}
		catch(Exception e) {
			
			System.out.println("Declarations sad path fail --- should fail");
		}
	}
	
	/**
	 * subprogram_declaration happy test:
	 * Tests to see if the given pascal code passes the recognizer: Should pass
	 */
	@Test
	public void testSubprogramDeclarationHappy() {
		Parser r = new Parser("procedure someID (foo: integer); "
				+ "begin "
				+ "end", false);
		System.out.println("Test subprogram declaration happy path:");
		
		try {
			r.subprogram_declaration();
			System.out.println("subprogram_declarations happy path success");
		}
		catch(Exception e){
			fail();
			System.out.println("subprogram_declarations happy path fail");
		}
	}
	
	/**
	 * subprogram_declaration sad test:
	 * Tests to see if the given pascal code gets caught by the the recognizer: Should not pass
	 */
	@Test
	public void testSubprogramDeclarationSad() {
		System.out.println("Test subprogram declaration sad path:");
		Parser r = new Parser("program foo :;", false);
		
		try {
			r.subprogram_declaration();
			System.out.println("subprogram_declaration sad path success. Means it actually failed.");
			fail();
		}
		catch(Exception e) {
			
			System.out.println("subprogram_declaration sad path fail --- should fail");
		}
	}
	
	/**
	 * statement happy test:
	 * Tests to see if the given pascal code passes the recognizer: Should pass
	 */
	@Test
	public void testStatementHappy() {
		Parser r = new Parser("foo := sam * foobar", false);
		System.out.println("Test statement happy path:");
		
		try {
			r.statement();
			System.out.println("statement happy path success");
		}
		catch(Exception e){
			fail();
			System.out.println("statement happy path fail");
		}
	}
	
	/**
	 * statement sad test:
	 * Tests to see if the given pascal code gets caught by the the recognizer: Should not pass
	 */
	@Test
	public void testStatementSad() {
		Parser r = new Parser("foo := sam * foobar", false);
		System.out.println("Test statement sad path:");
		
		try {
			r.statement();
			System.out.println("statement sad path success. Means it actually failed.");
			fail();
		}
		catch(Exception e) {
			
			System.out.println("statement sad path fail --- should fail");
		}
	}
	
	/**
	 * simple_expression happy test:
	 * Tests to see if the given pascal code passes the recognizer: Should pass
	 */
	@Test
	public void testSimpleExpressionHappy() {
		System.out.println("Test simple expression happy path:");
		Parser r = new Parser("foo * bar", false);
		try {
			r.simple_expression();
			System.out.println("simple_expression happy path success");
		}
		catch(Exception e){
			fail();
			System.out.println("simple_expression happy path fail");
		}
	}
	
	/**
	 * simple_expression sad test:
	 * Tests to see if the given pascal code gets caught by the the recognizer: Should not pass
	 */
	@Test
	public void testSimpleExpressionSad() {
		System.out.println("simple expression sad path:");
		Parser r = new Parser("Program foo ;;", false);
		
		try {
			r.simple_expression();
			System.out.println("simple_expression sad path success. Means it actually failed.");
			fail();
		}
		catch(Exception e) {
			
			System.out.println("simple_expression sad path fail --- should fail");
		}
	}
	
	/**
	 * factor happy test:
	 * Tests to see if the given pascal code passes the recognizer: Should pass
	 */
	@Test
	public void testFactorHappy() {
		System.out.println("test factor happy path:");
		Parser r = new Parser("not foo", false);
		
		try {
			r.factor();
			System.out.println("factor happy path success");
		}
		catch(Exception e){
			fail();
			System.out.println("factor happy path fail");
		}
	}
	
	/**
	 * factor sad test:
	 * Tests to see if the given pascal code gets caught by the the recognizer: Should not pass
	 */
	@Test
	public void testFactorSad() {
		System.out.println("test factor sad path:");
		Parser r = new Parser("not ;", false);
		
		try {
			r.factor();
			System.out.println("factor sad path success. Means it actually failed.");
			fail();
		}
		catch(Exception e) {
			
			System.out.println("factor sad path fail --- should fail");
		}
	}
	
	
	/**
	 * Statement assignment/procedure test
	 * tests to see if the recognizer can use the symbol table properly
	 * to decide if it will take the procedure or assignment path.
	 */
	@Test
	public void testStatement() {
		System.out.println("Test statement for assignment call:");
		Parser r = new Parser("foo := sam * foobar", false);
		
		try {
			r.statement();
			System.out.println("statement assignment call worked");
		}
		catch(Exception e){
			fail();
			System.out.println("statement assignment call failed");
		}
		
		Parser t = new Parser("procedure someID", false);
		System.out.println("Test statement for procedure call:");
		try {
			t.statement();
			System.out.println("statement procedure call worked");
		}
		catch(Exception e) {
			fail();
			System.out.println("statement procedure call failed");
		}
	}
	
	
	
	
	
}
