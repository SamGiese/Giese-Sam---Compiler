package scanner;

import java.util.HashMap;

/**
 * 
 * @author Sam Giese
 * Class that places all of the keywords and symbols into a hashmap for the scanner to look at
 *
 */
public class LookupTable extends HashMap <String, TokenType> {


	public LookupTable() {
		HashMap<String, TokenType> Map = new HashMap<String, TokenType>();
			
			this.put(":=", TokenType.BECOMES);
			this.put(">=", TokenType.GREATERTHANOREQUAL);
			this.put(">", TokenType.GREATERTHAN);
			this.put("<=", TokenType.LESSTHANOREQUAL);
			this.put("<", TokenType.LESSTHAN);
			this.put("<>", TokenType.NOTEQUAL);
			this.put(")", TokenType.RPARENTHESES);
			this.put("(", TokenType.LPARENTHESES);
			this.put("[", TokenType.LBRACKET);
			this.put("]", TokenType.RBRACKET);
			this.put(":", TokenType.COLON);
			this.put(";", TokenType.SEMICOLON);
			this.put("*", TokenType.ASTERISK);
			this.put("/", TokenType.SLASH);
			this.put("=", TokenType.EQUAL);
			this.put("+", TokenType.PLUS);
			this.put("-", TokenType.MINUS);
			this.put(".", TokenType.PERIOD);
			this.put(",", TokenType.COMMA);
			this.put("of", TokenType.OF);
			this.put("div", TokenType.DIV);
			this.put("end", TokenType.END);
			this.put("do", TokenType.DO);
			this.put("array", TokenType.ARRAY);
			this.put("else", TokenType.ELSE);
			this.put("if", TokenType.IF);
			this.put("write", TokenType.WRITE);
			this.put("read", TokenType.READ);
			this.put("and", TokenType.AND);
			this.put("mod", TokenType.MOD);
			this.put("not", TokenType.NOT);
			this.put("or", TokenType.OR);
			this.put("procedure", TokenType.PROCEDURE);
			this.put("program", TokenType.PROGRAM);
			this.put("function", TokenType.FUNCTION);
			this.put("integer", TokenType.INTEGER);
			this.put("real", TokenType.REAL);
			this.put("begin", TokenType.BEGIN);
			this.put("then", TokenType.THEN);
			this.put("while", TokenType.WHILE);
			this.put("var", TokenType.VAR);
			this.put("return", TokenType.RETURN);
			this.put("=", TokenType.RELOP);
			this.put("<>", TokenType.RELOP);
			this.put("<", TokenType.RELOP);
			this.put("<=", TokenType.RELOP);
			this.put(">=", TokenType.RELOP);
			this.put(">", TokenType.RELOP);
			this.put("float", TokenType.FLOAT);
			
	}
}
