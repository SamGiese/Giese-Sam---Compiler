package codegen;

import java.io.PrintWriter;
import java.util.HashMap;

import recognizer.SymbolTable;
import scanner.TokenType;
import syntaxtree.AssignmentStatementNode;
import syntaxtree.CompoundStatementNode;
import syntaxtree.ExpressionNode;
import syntaxtree.IfStatementNode;
import syntaxtree.OperationNode;
import syntaxtree.ProgramNode;
import syntaxtree.StatementNode;
import syntaxtree.ValueNode;
import syntaxtree.VariableNode;
import syntaxtree.WriteNode;




/**
 * 
 * @author Sam Giese
 *Takes the given syntax tree and creates MIPS assembly code with it.
 */
public class CodeGeneration {
	
	int currentTRegister = 0;
	SymbolTable table;
	String assembly;
	ProgramNode pNode;
	private HashMap<String, String> memTable = new HashMap<String, String>();
	int ifId;
	
	
	
	
	
	/**
	 * 
	 * @param pNode
	 * @param table
	 */
	public CodeGeneration(ProgramNode pNode, SymbolTable table) {
		this.pNode = pNode;
		currentTRegister = 0;
		assembly = "";
		this.table = table;
		
	}
	
	
	/**
	 * Generates the code
	 */
	public void generate() {
		
		assembly += ".data\n\n";
		
		for(VariableNode vNode : pNode.getVariables().getDeclarations()) {
			
				memTable.put(vNode.getName(), vNode.getName());
				assembly += vNode.getName() + " : .word 0\n";
			
			
		}
		
		
		assembly += "input: .asciiz \"Input: \" \n";
		
		assembly += "newLine: .asciiz \"\\n\"\n";
		
		assembly += "\n.text\n\nmain:\n";
		
		for(StatementNode sNode : pNode.getMain().getStateNodes()) {
			String str = "$s" + currentTRegister;
			codeStatement(sNode, str);
			
			
			
		}
		
		
		assembly += "\nli $v0, 10 \n";
		//assembly += "syscall \n";
		
		
	}

	
	
	
	
	
	

	/**
	 * generates the code for statements
	 * @param sNode
	 * @param str
	 */
	public void codeStatement(StatementNode sNode, String str) {
		if(sNode instanceof AssignmentStatementNode) {
			codeAssignment((AssignmentStatementNode) sNode, str);
		}
		else if(sNode instanceof IfStatementNode) {
			codeIf((IfStatementNode) sNode, str);
		}
		else if(sNode instanceof CompoundStatementNode) {
			for (StatementNode compNode : ((CompoundStatementNode) sNode).getStateNodes()) {
				codeStatement(compNode, str);
			}
		}
		else if(sNode instanceof WriteNode) {
			codeWrite((WriteNode) sNode, str);
		}
		
	}


	private void codeWrite(WriteNode wNode, String str) {
		codeExp(wNode.getContent(), str);
		assembly += "addi    $v0,    $zero,    1\n" + "add $a0,    " + str + ",    $zero\n" + "syscall\n" + "li $v0, 4" + "\nla    $a0, newLine\n" + "syscall\n";
	}


	/**
	 * generates the code for if's
	 * @param sNode
	 * @param str
	 */
	public void codeIf(IfStatementNode sNode, String str) {
		
		String reg;
		
		
		
		if(sNode.getTest() instanceof ValueNode) {
			codeExp(sNode.getTest(), str);
			reg = "$s" + ++currentTRegister;
			assembly += "li    " + reg + ",    " + "1\n";
			assembly += "bne    " + str + ",    " + reg + ",    ";
			assembly += "else" + ifId + "\n";
		}
		else {
			codeOperation((OperationNode) sNode.getTest(), str);
			assembly += "else" + ifId + "\n";
		}
		
		str = "$s" + currentTRegister++;
		codeStatement(sNode.getThenStatement(), str);
		assembly += "j endIf" + ifId + "\n";
		
		str = "$s" + currentTRegister++;
		assembly += "else" + ifId + ":\n";
		codeStatement(sNode.getElseStatement(), str);
		assembly += "endIf" + ifId + ":\n";
		
		ifId++;
		currentTRegister -= 2;
		
		
	}


	/**
	 * generates the code for assignments
	 * @param sNode
	 * @param str
	 */
	public void codeAssignment(AssignmentStatementNode sNode, String str) {
		
		
		
		codeExp(sNode.getExpression(), str);
		assembly += "sw " + str + ", " + memTable.get(sNode.getLvalue().getName()) + '\n';
		
	}


	/**
	 * generates the code for expressions
	 * @param expression
	 * @param str
	 */
	public void codeExp(ExpressionNode expression, String str) {
		
		
		if(expression instanceof ValueNode) {
			codeValue((ValueNode) expression, str);
			
		}
		else if(expression instanceof OperationNode) {
			codeOperation((OperationNode) expression, str);
		}
		else if(expression instanceof VariableNode) {
			String var = ((VariableNode) expression).getName();
			
			assembly += "lw " + str + ", " + var + "\n";
			
			
		}
		
	}


	/**
	 * generates the code for operations: +, -, >, <, <=, >=, =, <>, *, /, and, or, mod
	 * @param operation
	 * @param str
	 */
	public void codeOperation(OperationNode operation, String str) {
		
		ExpressionNode left = operation.getLeft();
		String leftStr = "$s" + currentTRegister++;
		codeExp(left, leftStr);
		
		ExpressionNode right = operation.getRight();
		String rightStr = "$s" + currentTRegister++;
		codeExp(right, rightStr);
		
		TokenType type = operation.getOperation();
		
		
		
		if(type == TokenType.PLUS) {
			assembly += "add    " + str + ",    " + leftStr + ",    " + rightStr + "\n";
			
		}
		
		else if(type == TokenType.MINUS) {
			assembly += "sub    " + str + ",    " + leftStr + ",    " + rightStr + "\n";
		}
		
		else if(type == TokenType.LESSTHAN) {
			assembly += "bge    " + leftStr + ",    " + rightStr + ",    ";
		}
		else if(type == TokenType.GREATERTHAN) {
			assembly += "ble    " + leftStr + ",    " + rightStr + ",    ";
		}
		else if(type == TokenType.LESSTHANOREQUAL) {
			assembly += "bgt    " + leftStr + ",    " + rightStr + ",    ";
		}
		else if(type == TokenType.GREATERTHANOREQUAL) {
			assembly += "blt    " + leftStr + ",    " + rightStr + ",    ";
		}
		else if(type == TokenType.EQUAL) {
			assembly += "bne    " + leftStr + ",    " + rightStr + ",    ";
		}
		else if(type == TokenType.NOTEQUAL) {
			assembly += "beq    " + leftStr + ",    " + rightStr + ",    ";
		}
		
		else if(type == TokenType.MOD) {
			assembly += "div    " + leftStr + ",    " + rightStr + "\n";
			assembly += "mfhi    " + str + "\n";
		}
		
		else if(type == TokenType.ASTERISK) {
			assembly += "mult    " + leftStr + ",    " + rightStr + "\n";
			assembly += "mflo    " + str + "\n";
		}
		else if(type == TokenType.SLASH) {
			assembly += "div    " + leftStr + ",    " + rightStr + "\n";
			assembly += "mflo    " + str + "\n";
		}
		else if(type == TokenType.AND) {
			assembly += "and    " + str + ",    " + leftStr + ",    " + rightStr + "\n";
		}
		else if(type == TokenType.OR) {
			assembly += "or    " + str + ",    " + leftStr + ",    " + rightStr + "\n";
		}
		
		
		this.currentTRegister -=2;
		
		
		
	}


	/**
	 * generates code for values
	 * @param expression
	 * @param str
	 */
	public void codeValue(ValueNode expression, String str) {
		assembly += "li    " + str + ",    " + expression.getAttribute() + "\n";
		
	}
	
	/**
	 * returns the assembly code
	 * @return
	 */
	public String getCode() {
		return assembly;
	}
	
	
	
	
	
}
