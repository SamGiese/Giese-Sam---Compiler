package recognizer;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import scanner.Scanner;
import scanner.Token;
import scanner.TokenType;


/**
 * The recognizer looks at the input brought in by the scanner
 * and decides if it is a well formed mini pascal expression.
 * 
 * @author Sam Giese
 *
 */
public class Recognizer {
	 ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////
    
    private Token lookahead;
    
    private Scanner scanner;
    
    
    
    ///////////////////////////////
    //       Constructors
    ///////////////////////////////
    
    
    /**
     * Recognizer creates the scanner, and then initiates the lookahead sequence
     * saving the nextToken as lookahead to be checked for correct grammar.
     */
    public Recognizer( String text, boolean isFilename) {
        if( isFilename) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("expressions/simplest.pas");
        } catch (FileNotFoundException ex) {
            error( "No file");
        }
        InputStreamReader isr = new InputStreamReader( fis);
        scanner = new Scanner( isr);
                 
        }
        else {
            scanner = new Scanner( new StringReader( text));
        }
        try {
            lookahead = scanner.nextToken();
        } catch (IOException ex) {
            error( "Scan error");
        }
        
    }
    
    
    /**
     * Checks to see if the given token has the correct token type
     * @param expected the input token type to be compared
     */
    public void match(TokenType expected) {
    System.out.println("match( " + expected + ")");
    if( this.lookahead.getType() == expected) {
        try {
            this.lookahead = scanner.nextToken();
            if( this.lookahead == null) {
                this.lookahead = new Token( "End of File", null);
            }
        } catch (IOException ex) {
            error( "Scanner exception");
        }
    }
    else {
        error("Match of " + expected + " found " + this.lookahead.getType()
                + " instead.");
    }
   }
    
    
    
    /**
     * Errors out of the parser.
     * Prints an error message and then exits the program.
     * @param message The error message to print.
     */
    public void error( String message) {
        System.out.println( "Error " + message);  //" at line "
        		//+ this.scanner.getLine() + " column " + 
                //this.scanner.getColumn());
        throw new RuntimeException(message);
    }
    
    
    /**
     * Program executes if the given TokenType is PROGRAM, and checks to see if
     * the following expression after it forms correct grammar.
     */
    public void program() {
    	match(TokenType.PROGRAM);
    	match(TokenType.ID);
    	match(TokenType.SEMICOLON);
    	declarations();
    	subprogram_declarations();
    	compound_statement();
    	match(TokenType.PERIOD);
    }
    
    /**
     * Checks to see if the following expression after the function call identifier_list has carrect grammar
     */
    public void identifier_list() {
    	match(TokenType.ID);
    	if(lookahead.getType() == TokenType.COMMA) {
    		match(TokenType.COMMA);
    		identifier_list();
    	}
    	else {
    		//lambda option
    	}
    }
    
    
    /**
     * Method to verify correct grammar usage for declarations ruleset
     */
    public void declarations() {
    	if(lookahead.getType() == TokenType.VAR) {
    		match(TokenType.VAR);
    		identifier_list();
    		match(TokenType.COLON);
    		type();
    		match(TokenType.SEMICOLON);
    		declarations();
    	}
    	else {
    		//Lamnbda Option
    	}
    }
    
    /**
     * checks to see if the expression following the function call type
     * has the correct grammar
     */
    public void type() {
    	if(lookahead.getType() == TokenType.ARRAY) {
    		match(TokenType.ARRAY);
    		match(TokenType.LBRACKET);
    		match(TokenType.INTEGER);
    		match(TokenType.COLON);
    		match(TokenType.INTEGER);
    		match(TokenType.RBRACKET);
    		match(TokenType.OF);
    		standard_type();
    	}
    	else {
    		standard_type();
    	}
    }
    
    
    /**
     * checks to see if the expression following the function call standard_type
     * has the correct grammar
     */
    public void standard_type() {
    	if(lookahead.getType() == TokenType.INTEGER) {
    		match(TokenType.INTEGER);
    	}
    	else {
    		match(TokenType.REAL);
    	}
    }
    
    
    /**
     * checks to see if the expression following the function call subprogram_declarations
     * has the correct grammar
     */
    public void subprogram_declarations() {
    	if(lookahead.getType() == TokenType.FUNCTION || lookahead.getType() == TokenType.PROCEDURE) {
    		subprogram_declaration();
    		subprogram_declarations();
    	}
    	else {
    		//lambda option
    	}
    }
    
    /**
     * checks to see if the expression following the function call subprogram_declaration
     * has the correct grammar
     */
    public void subprogram_declaration() {
    	subprogram_head();
    	declarations();
    	compound_statement();
    }
    
    
    /**
     * checks to see if the expression following the function call subprogram_head
     * has the correct grammar
     */
    public void subprogram_head() {
    	if(lookahead.getType() == TokenType.FUNCTION) {
    	match(TokenType.FUNCTION);
    	match(TokenType.ID);
    	arguments();
    	match(TokenType.COLON);
    	standard_type();
    	match(TokenType.SEMICOLON);
    	}
    	else if(lookahead.getType() == TokenType.PROCEDURE) {
    	match(TokenType.PROCEDURE);
    	match(TokenType.ID);
    	arguments();
    	match(TokenType.SEMICOLON);
    	}
    }
    

    
    
    /**
     * checks to see if the expression following the function call arguments has
     * the correct grammar
     */
    public void arguments() {
    	if(lookahead.getType() == TokenType.LPARENTHESES) {
    		match(TokenType.LPARENTHESES);
    		parameter_list();
    		match(TokenType.RPARENTHESES);
    	}
    	else {
    		//lambda option
    	}
    }
    
    
    /**
     * checks to see if the expression following the function call parameter_list
     * has the correct grammar
     */
    public void parameter_list() {
    	identifier_list();
    	match(TokenType.COLON);
    	type();
    	if(lookahead.getType() == TokenType.SEMICOLON) {
    		match(TokenType.SEMICOLON);
    		parameter_list();
    	}
    	else {
    		//lambda option
    	}
    }
    
    /**
     * checks to see if the expression following the function call compoun_statement
     * has the correct grammar
     */
    public void compound_statement() {
    	match(TokenType.BEGIN);
    	optional_statements();
    	match(TokenType.END);
    }
    
    /**
     * checks to see if the expression following the function call optional_statements has
     * the correct grammar
     */
    public void optional_statements() {
    	if(lookahead.getType() == TokenType.ID || lookahead.getType() == TokenType.BEGIN || lookahead.getType() == TokenType.IF ||
    			lookahead.getType() == TokenType.WHILE || lookahead.getType() == TokenType.READ) {
    		statement_list();
    	}
    	else {
    		//lambda
    	}
    }
    
    /**
     * checks to see if the expression following the function call statement_list
     * has the correct grammar
     */
    public void statement_list(){
    	statement();
    	if(lookahead.getType() == TokenType.SEMICOLON) {
    		match(TokenType.SEMICOLON);
    		statement_list();
    	}
    	else {
    		//lambda
    	}
    }
    
    
    /**
     * checks to see if the expression following the function call statements
     * has the correct grammar
     */
    public void statement() {
    	if(lookahead.getType() == TokenType.ID) {
    		variable();
    		match(TokenType.EQUAL);
    		expression();
    	}
    	else if(lookahead.getType() == TokenType.BEGIN) {
    		compound_statement();
    	}
    	else if(lookahead.getType() == TokenType.IF) {
    		match(TokenType.IF);
    		expression();
    		match(TokenType.THEN);
    		statement();
    		match(TokenType.ELSE);
    		statement();
    	}
    	else if(lookahead.getType() == TokenType.WHILE) {
    		match(TokenType.WHILE);
    		expression();
    		match(TokenType.DO);
    		statement();
    	}
    	else if(lookahead.getType() == TokenType.READ) {
    		match(TokenType.READ);
    		match(TokenType.LPARENTHESES);
    		match(TokenType.ID);
    		match(TokenType.RPARENTHESES);
    	}
    	else if(lookahead.getType() == TokenType.WRITE) {
    		match(TokenType.WRITE);
    		match(TokenType.LPARENTHESES);
    		expression();
    		match(TokenType.RPARENTHESES);
    	}
    	else if(lookahead.getType() == TokenType.RETURN) {
    		match(TokenType.RETURN);
    		expression();
    	}
    }
    
    /**
     * checks to see if the expression following the function call variable
     * has the correct grammar
     */
    public void variable() {
    	match(TokenType.ID);
    	if(lookahead.getType() == TokenType.LBRACKET) {
    		match(TokenType.LBRACKET);
    		expression();
    		match(TokenType.RBRACKET);
    	}
    	else {
    		//lambda
    	}
    }
    	
    /**
     * checks to see if the expression following the function call procedure_statement
     * has the correct grammar
     */
    public void procedure_statement(){
    		match(TokenType.ID);
        	if(lookahead.getType() == TokenType.LPARENTHESES) {
        		match(TokenType.LPARENTHESES);
        		expression();
        		match(TokenType.LPARENTHESES);
        	}
        	else {
        		//lambda
        	}
    	}
    	
    //if statement has RELOP's
    /**
     * checks to see if the expression following the function call expression_list
     * has correct grammar
     */
    public void expression_list() {
    		if(lookahead.getType() == TokenType.EQUAL || lookahead.getType() == TokenType.LESSTHANOREQUAL || 
    				lookahead.getType() == TokenType.NOTEQUAL || lookahead.getType() == TokenType.LESSTHAN ||
    				lookahead.getType() == TokenType.GREATERTHANOREQUAL || lookahead.getType() == TokenType.GREATERTHAN) {
    			expression();
    			match(TokenType.COMMA);
    			expression_list();
    		}
    		else {
    			expression();
    		}
    	}
    	
    	
    	
    	/**
    	 * checks to see if the expression following the function call expression
    	 * has the correct grammar
    	 */
    public void expression() {
    		if(lookahead.getType() == TokenType.EQUAL || 
    				lookahead.getType() == TokenType.LESSTHANOREQUAL || 
    				lookahead.getType() == TokenType.NOTEQUAL || 
    				lookahead.getType() == TokenType.LESSTHAN ||
    				lookahead.getType() == TokenType.GREATERTHANOREQUAL || 
    				lookahead.getType() == TokenType.GREATERTHAN ||
    				lookahead.getType() == TokenType.PLUS || 
    				lookahead.getType() == TokenType.OR || 
    				lookahead.getType() == TokenType.MINUS || 
    				lookahead.getType() == TokenType.ASTERISK || 
    				lookahead.getType() == TokenType.DIV ||
    				lookahead.getType() == TokenType.MOD || 
    				lookahead.getType() == TokenType.AND ||
    				lookahead.getType() == TokenType.ID|| 
    				lookahead.getType() == TokenType.INTEGER ||
    				lookahead.getType() == TokenType.NOT) {
    			
    			simple_expression();
    			match(TokenType.RELOP); //RELOP is = < <> > <= >=
    			simple_expression();
    		}
    		else {
    			simple_expression();
    		}
    	}
    	
    /**
     * checks to see if the expression following the call of simple_expression
     * has the correct grammar
     */
    public void simple_expression(){
    		if(lookahead.getType() == TokenType.ID || 
    				lookahead.getType() == TokenType.INTEGER ||
    				lookahead.getType() == TokenType.REAL ||
    				lookahead.getType() == TokenType.LPARENTHESES ||
    				lookahead.getType() == TokenType.NOT) {
    					term();
    					simple_part();
    		}
    		else if(lookahead.getType() == TokenType.PLUS ||
    				lookahead.getType() == TokenType.MINUS) {
    			sign();
    			term();
    			simple_part();
    		}
    	}
    	
    	
    /**
     * checks to see if the expression following the call of simple_part
     * has the correct grammar	
     */
    public void simple_part(){
    		if(lookahead.getType() == TokenType.PLUS ||
    				lookahead.getType() == TokenType.MINUS) {
    				match(lookahead.getType());
    				term();
    				simple_part();
    		}
    		else {
    			//lambda
    		}
    	}
    	

    /**
     * checks for correct grammar following term
     */
    public void term() {
	   factor();
	   term_part();
   }
    
    /**
     * checks for correct grammar following term_part
     */
    public void term_part() {
    	if(lookahead.getType() == TokenType.ASTERISK ||
    			lookahead.getType() == TokenType.DIV) {
    		match(lookahead.getType());
    		factor();
    		term_part();
    	}
    }
    
    /**
     * checks for correct grammar following factor
     */
    public void factor() {
    	if(lookahead.getType() == TokenType.ID) {
    		match(TokenType.ID);
    		if(lookahead.getType() == TokenType.LBRACKET) {
    			match(TokenType.LBRACKET);
    			expression();
    			match(TokenType.RBRACKET);
    		}
    		else if(lookahead.getType() == TokenType.LPARENTHESES) {
    			match(TokenType.LPARENTHESES);
    			expression_list();
    			match(TokenType.RPARENTHESES);
    		}
    		else if(lookahead.getType() == TokenType.INTEGER) {
    			match(TokenType.INTEGER);
    		}
    		else if(lookahead.getType() == TokenType.REAL) {
    			match(TokenType.REAL);
    		}
    	}else if(lookahead.getType() == TokenType.LPARENTHESES) {
    		match(TokenType.LPARENTHESES);
    		expression();
    		match(TokenType.RPARENTHESES);
    	}
    	else if(lookahead.getType() == TokenType.NOT) {
    		match(TokenType.NOT);
    		factor();
    	}
    }
    
    /**
     * checks for correct grammar following sign
     */
    public void sign() {
    	if(lookahead.getType() == TokenType.PLUS) {
    		match(TokenType.PLUS);
    	}
    	else if(lookahead.getType() == TokenType.MINUS) {
    		match(TokenType.MINUS);
    	}
    }
    
    /**
     * Combines the relational operator enums into one single use relop function
     */
	public void relop() {
		if (lookahead.getType() == TokenType.EQUAL) {
			match(TokenType.EQUAL);
		} else if (lookahead.getType() == TokenType.NOTEQUAL) {
			match(TokenType.NOTEQUAL);
		} else if (lookahead.getType() == TokenType.LESSTHAN) {
			match(TokenType.LESSTHAN);
		} else if (lookahead.getType() == TokenType.LESSTHANOREQUAL) {
			match(TokenType.LESSTHANOREQUAL);
		} else if (lookahead.getType() == TokenType.GREATERTHANOREQUAL) {
			match(TokenType.GREATERTHANOREQUAL);
		} else if (lookahead.getType() == TokenType.GREATERTHAN) {
			match(TokenType.GREATERTHAN);
		} else {
			error("Relop");
		}
	}
    
}
