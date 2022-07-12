package recognizer;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import scanner.Scanner;
import scanner.Token;
import scanner.TokenType;
import syntaxtree.*;


/**
 * The recognizer looks at the input brought in by the scanner
 * and decides if it is a well formed mini pascal expression.
 * 
 * @author Sam Giese
 *
 */
public class Parser {
	 ///////////////////////////////
    //    Instance Variables
    ///////////////////////////////
    
    private Token lookahead;
    
    private Scanner scanner;
    SymbolTable table = new SymbolTable();
    String lexeme = "";
    
    ArrayList<String> varNames = new ArrayList<String>();
    
    boolean isArray = false;
    int size = 0;
    
    
    
    ///////////////////////////////
    //       Constructors
    ///////////////////////////////
    
    
    /**
     * Recognizer creates the scanner, and then initiates the lookahead sequence
     * saving the nextToken as lookahead to be checked for correct grammar.
     */
    public Parser( String text, boolean isFilename) {
        if( isFilename) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(text);
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
    public ProgramNode program() {
    	match(TokenType.PROGRAM);
    	lexeme = this.lookahead.getLexeme();
    	String pName = lexeme;
    	match(TokenType.ID);
    	table.addProgramName(lexeme);
    	
    	ProgramNode pNode = new ProgramNode(pName);
    	
    	match(TokenType.SEMICOLON);
    	
    	DeclarationsNode declarations = declarations();
    	SubProgramDeclarationsNode subProgramDeclarations = subprogram_declarations();
    	CompoundStatementNode compoundStatement = compound_statement();
    	match(TokenType.PERIOD);
    	
    	pNode.setVariables(declarations);
    	pNode.setMain(compoundStatement);
    	pNode.setFunctions(subProgramDeclarations);
    	
    	pNode.setAllNames(varNames);
    	
    	return pNode;
    }
    
    /**
     * Checks to see if the following expression after the function call identifier_list has correct grammar
     */
    public ArrayList<VariableNode> identifier_list() {
    	ArrayList<VariableNode> idList = new ArrayList<>();
    	lexeme = this.lookahead.getLexeme();
    	String name = lexeme;
    	match(TokenType.ID);
    	VariableNode vnode = new VariableNode(lexeme);
    	table.addVariableName(lexeme);
    	idList.add(vnode);
    	if(lookahead.getType() == TokenType.COMMA) {
    		match(TokenType.COMMA);
    		idList.addAll(identifier_list());
    	}
    	else {
    		//lambda option
    	}
    	return idList;
    }
    
    
    /**
     * Method to verify correct grammar usage for declarations ruleset
     * @return 
     */
    public DeclarationsNode declarations() {
    	DeclarationsNode dNode = new DeclarationsNode();
    	if(lookahead.getType() == TokenType.VAR) {
    		match(TokenType.VAR);
    		ArrayList<VariableNode> idlist = identifier_list();
    		
    		match(TokenType.COLON);
    		type();
    		match(TokenType.SEMICOLON);
    		for(int i = 0; i < idlist.size(); i++) {
    			dNode.addVariable(idlist.get(i));
    		}
    		
    		
    	}
    	else {
    		//Lambda Option
    	}
    	return dNode;
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
    public DataType standard_type() {
    	if(lookahead.getType() == TokenType.INTEGER) {
    		match(TokenType.INTEGER);
    		return DataType.DINTEGER;
    	}
    	else if(lookahead.getType() == TokenType.REAL){
    		match(TokenType.REAL);
    		return DataType.DREAL;
    	}
    	else {
    		match(TokenType.FLOAT);
    		return DataType.DFLOAT;
    	}
    }
    
    
    /**
     * checks to see if the expression following the function call subprogram_declarations
     * has the correct grammar
     * @return 
     */
    public SubProgramDeclarationsNode subprogram_declarations() {
    	SubProgramDeclarationsNode subNode = new SubProgramDeclarationsNode();
    	if(lookahead.getType() == TokenType.FUNCTION || lookahead.getType() == TokenType.PROCEDURE) {
    		subNode.addSubProgramDeclaration(subprogram_declaration());
    		//subprogram_declaration();
    		match(TokenType.SEMICOLON);
    		subprogram_declarations();
    	}
    	else {
    		//lambda option
    	}
    	return subNode;
    }
    
    /**
     * checks to see if the expression following the function call subprogram_declaration
     * has the correct grammar
     * @return 
     */
    public SubProgramNode subprogram_declaration() {
    	SubProgramNode temp = subprogram_head();
    	temp.setVariables(declarations());
    	temp.setFunctions(subprogram_declarations());
    	temp.setMain(compound_statement());
    	return temp;
    	
    }
    
    
    /**
     * checks to see if the expression following the function call subprogram_head
     * has the correct grammar
     * @return 
     */
    public SubProgramNode subprogram_head() {
    	SubProgramNode temp = null;
    	if(lookahead.getType() == TokenType.FUNCTION) {
    		match(TokenType.FUNCTION);
    		lexeme = lookahead.getLexeme();
    		temp = new SubProgramNode(lexeme);
    		match(TokenType.ID);
    		arguments();
    		match(TokenType.COLON);
    		table.addFunctionName(lexeme);
    		standard_type();
    		match(TokenType.SEMICOLON);
    	}
    	else if(lookahead.getType() == TokenType.PROCEDURE) {
    		match(TokenType.PROCEDURE);
    		lexeme = lookahead.getLexeme();
    		temp = new SubProgramNode(lexeme);
    		match(TokenType.ID);
    		table.addProcedureName(lexeme);
    		arguments();
    		match(TokenType.SEMICOLON);
    	}
    	return temp;
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
     * checks to see if the expression following the function call compound_statement
     * has the correct grammar
     * @return 
     */
    public CompoundStatementNode compound_statement() {
    	CompoundStatementNode compNode = new CompoundStatementNode();
    	match(TokenType.BEGIN);
    	compNode = optional_statements();
    	match(TokenType.END);
    	return compNode;
    }
    
    /**
     * checks to see if the expression following the function call optional_statements has
     * the correct grammar
     */
    public CompoundStatementNode optional_statements() {
    	CompoundStatementNode temp = new CompoundStatementNode();
    	if(lookahead.getType() == TokenType.ID || lookahead.getType() == TokenType.BEGIN || lookahead.getType() == TokenType.IF ||
    			lookahead.getType() == TokenType.WHILE || lookahead.getType() == TokenType.READ || lookahead.getType() == TokenType.WRITE) {
    		temp.addAllStateNodes(statement_list());
    	}
    	else {
    		//lambda
    		//return temp;
    	}
    	return temp;
    }
    
    /**
     * checks to see if the expression following the function call statement_list
     * has the correct grammar
     * @return 
     */
    public ArrayList<StatementNode> statement_list(){
    	
    	ArrayList<StatementNode> nodes = new ArrayList<StatementNode>();
    	StatementNode sNode = statement();
    	
    	if(sNode != null) {
    		nodes.add(sNode);
    	}
    	
    	if(lookahead.getType() == TokenType.SEMICOLON) {
    		match(TokenType.SEMICOLON);
    		nodes.addAll(statement_list());
    	}
    	else {
    		//lambda
    		//return nodes;
    	}
		return nodes;
    }
    
    
    /**
     * checks to see if the expression following the function call statements
     * has the correct grammar
     */
    public StatementNode statement() {
    	StatementNode temp = null;
    	if(lookahead.getType() == TokenType.ID) {
    		if(table.isVariableName(lookahead.getLexeme())) {
    			
    			AssignmentStatementNode assignment = new AssignmentStatementNode();
    			
    			// BREAK POINT
    			
    			
    			assignment.setLvalue(variable());
        		match(TokenType.BECOMES);
        		assignment.setExpression(expression());   
        		//removing the comment on this return breaks it
        		//
        		
        		return assignment;
        		
        		}
    		else if(table.isProcedureName(lookahead.getLexeme())) {
    			procedure_statement();
    		}
    	}
    	else if(lookahead.getType() == TokenType.BEGIN) {
    		temp = compound_statement();
    	}
    	else if(lookahead.getType() == TokenType.IF) {
    		IfStatementNode tempIf = new IfStatementNode();
    		match(TokenType.IF);
    		tempIf.setTest(expression());
    		match(TokenType.THEN);
    		tempIf.setThenStatement(statement());
    		match(TokenType.ELSE);
    		tempIf.setElseStatement(statement());
    	}
    	/*else if(lookahead.getType() == TokenType.WHILE) {
    		match(TokenType.WHILE);
    		expression();
    		match(TokenType.DO);
    		statement();
    	}*/
    	else if(lookahead.getType() == TokenType.READ) {
    		match(TokenType.READ);
    		match(TokenType.LPARENTHESES);
    		lexeme = lookahead.getLexeme();
    		match(TokenType.ID);
    		table.addVariableName(lexeme);
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
    	
    	return temp;
    }
    
    /**
     * checks to see if the expression following the function call variable
     * has the correct grammar
     */
    public VariableNode variable() {
    	VariableNode varTemp = null;
    	lexeme = lookahead.getLexeme();
    	match(TokenType.ID);
    	table.addVariableName(lexeme);
    	if(lookahead.getType() == TokenType.LBRACKET) {
    		match(TokenType.LBRACKET);
    		ExpressionNode temp = expression();
    		match(TokenType.RBRACKET);
    		
    	}
    	else {
    		varTemp = new VariableNode(lexeme);
    		
    		if (!varNames.contains(varTemp.getName()))
				varNames.add(varTemp.getName());
    		
    		return varTemp;
    		//lambda
    	}
		return varTemp;
    	
    }
    	
    /**
     * checks to see if the expression following the function call procedure_statement
     * has the correct grammar
     */
    public void procedure_statement(){
    		lexeme = lookahead.getLexeme();
    		match(TokenType.ID);
    		table.addProcedureName(lexeme);
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
    public ArrayList<ExpressionNode> expression_list() {
    		ArrayList<ExpressionNode> list = new ArrayList<>();
    		list.add(expression());
    		if(lookahead.getType() == TokenType.EQUAL || lookahead.getType() == TokenType.LESSTHANOREQUAL || 
    				lookahead.getType() == TokenType.NOTEQUAL || lookahead.getType() == TokenType.LESSTHAN ||
    				lookahead.getType() == TokenType.GREATERTHANOREQUAL || lookahead.getType() == TokenType.GREATERTHAN) {
    			
    			match(TokenType.COMMA);
    			list.addAll(expression_list());
    		}
    		else {
    			return list;
    		}
    		return list;
    	}
    	
    	
    	
    	/**
    	 * checks to see if the expression following the function call expression
    	 * has the correct grammar
    	 * @return 
    	 */
    public ExpressionNode expression() {
    	ExpressionNode temp = simple_expression();
    		if(lookahead.getType() == TokenType.EQUAL || 
    				lookahead.getType() == TokenType.LESSTHANOREQUAL || 
    				lookahead.getType() == TokenType.NOTEQUAL || 
    				lookahead.getType() == TokenType.LESSTHAN ||
    				lookahead.getType() == TokenType.GREATERTHANOREQUAL || 
    				lookahead.getType() == TokenType.GREATERTHAN ||
    				lookahead.getType() == TokenType.NOT) {
    			
    			
    			match(lookahead.getType()); //RELOP is = < <> > <= >=
    			simple_expression();
    			
    		}
    		return temp;
    	}
    	
    /**
     * checks to see if the expression following the call of simple_expression
     * has the correct grammar
     * @return 
     */
    public ExpressionNode simple_expression(){
    		ExpressionNode temp = null;
    		if(lookahead.getType() == TokenType.ID || 
    				lookahead.getType() == TokenType.INTEGER ||
    				lookahead.getType() == TokenType.REAL ||
    				lookahead.getType() == TokenType.LPARENTHESES ||
    				lookahead.getType() == TokenType.NOT ||
    				lookahead.getType() == TokenType.FLOAT){
    					temp = term();
    					temp = simple_part(temp);
    		}
    		else if(lookahead.getType() == TokenType.PLUS ||
    				lookahead.getType() == TokenType.MINUS) {
    			sign();
    			temp = term();
    			simple_part(temp);
    		}
    		return temp;
    	}
    	
    	
    /**
     * checks to see if the expression following the call of simple_part
     * has the correct grammar	
     * @param temp 
     * @return 
     */
    public ExpressionNode simple_part(ExpressionNode temp){
    		if(lookahead.getType() == TokenType.PLUS ||
    				lookahead.getType() == TokenType.MINUS) {
    				OperationNode opNode = new OperationNode(lookahead.getType());
    				match(lookahead.getType());
    				ExpressionNode temp2 = term();
    				opNode.setLeft(temp);
    				opNode.setRight(temp2);
    				return simple_part(opNode);
    		}
    		else {
    			//lambda
    		}
    		return temp;
    	}
    	

    /**
     * checks for correct grammar following term
     * @return 
     */
    public ExpressionNode term() {
	   ExpressionNode temp = factor();
	   return term_part(temp);
   }
    
    /**
     * checks for correct grammar following term_part
     * @param temp 
     */
    public ExpressionNode term_part(ExpressionNode temp) {
    	if(lookahead.getType() == TokenType.ASTERISK ||
    			lookahead.getType() == TokenType.SLASH) {
    		
    		OperationNode opNode = new OperationNode(lookahead.getType());
    		match(lookahead.getType());
    		ExpressionNode right = factor();
    		opNode.setLeft(temp);
    		opNode.setRight(term_part(right));
    		return opNode;
    		
    		
    	}
    	
    	return temp;
    }
    
    /**
     * checks for correct grammar following factor
     */
    public ExpressionNode factor() {
    	ExpressionNode temp = null;
    	if(lookahead.getType() == TokenType.ID) {
    		lexeme = lookahead.getLexeme();
    		match(TokenType.ID);
    		table.addVariableName(lexeme);
    		if(lookahead.getType() == TokenType.LBRACKET) {
    			match(TokenType.LBRACKET);
    			temp = expression();
    			match(TokenType.RBRACKET);
    			return temp;
    		}
    		else if(lookahead.getType() == TokenType.LPARENTHESES) {
    			match(TokenType.LPARENTHESES);
    			expression_list();
    			match(TokenType.RPARENTHESES);
    		}
    		}else if(lookahead.getType() == TokenType.INTEGER) {
    			temp = new ValueNode(lookahead.getLexeme(), DataType.DINTEGER);
    			match(TokenType.INTEGER);
    		}
    		else if(lookahead.getType() == TokenType.FLOAT) {
    			temp = new ValueNode(lookahead.getLexeme(), DataType.DFLOAT);
    			match(TokenType.FLOAT);
    		}
    		else if(lookahead.getType() == TokenType.REAL) {
    			temp = new ValueNode(lookahead.getLexeme(), DataType.DREAL);
    			match(TokenType.REAL);
    		}
    	else if(lookahead.getType() == TokenType.LPARENTHESES) {
    		match(TokenType.LPARENTHESES);
    		expression();
    		match(TokenType.RPARENTHESES);
    	}
    	else if(lookahead.getType() == TokenType.NOT) {
    		match(TokenType.NOT);
    		factor();
    	}
    	return temp;
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
    
    
    public SymbolTable getSymbolTable() {
    	return table;
    }
    
    public String getSymbolTableStr() {
    	return table.toString();
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
