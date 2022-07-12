package semanticAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

import recognizer.DataType;
import recognizer.SymbolTable;
import syntaxtree.AssignmentStatementNode;
import syntaxtree.CompoundStatementNode;
import syntaxtree.ExpressionNode;
import syntaxtree.IfStatementNode;
import syntaxtree.OperationNode;
import syntaxtree.ProgramNode;
import syntaxtree.StatementNode;
import syntaxtree.ValueNode;
import syntaxtree.VariableNode;

/**
 * 
 * @author Sam Giese
 * Takes in the given syntax tree and checks that 
 * the variables in the expression have been declared.
 *
 */
public class SemanticAnalysis {
	
	
	ProgramNode pNode = null;
	SymbolTable symbol = null;
	
	private HashMap<String, DataType> vTypes = new HashMap<String, DataType>();
	
	
	public SemanticAnalysis(ProgramNode pNode, SymbolTable symbol) {
		this.pNode = pNode;
		this.symbol = symbol;
	}
	
	
	/**
	 * Takes the variables and their types and puts into hashmap.
	 * Also calls the verifyVariables function to check that they are declared.
	 * @return
	 */
	public ProgramNode analysis() {
		ArrayList<VariableNode> vNodes = pNode.getVariables().getDeclarations();
		
		for(int i = 0; i < vNodes.size(); i++) {
			vTypes.put(vNodes.get(i).getName(), vNodes.get(i).getType());
		}
		
		
		//call the verifying method
		verifyVariables();
		
		
		CompoundStatementNode CompStatNode = pNode.getMain();
		//calls on the assigner method
		assignTypes(CompStatNode);
		
		
		for(int i = 0; i < pNode.getFunctions().getSubProgs().size(); i++) {
			CompoundStatementNode tempCompStat = pNode.getFunctions().getSubProgs().get(i).getMain();
			assignTypes(tempCompStat);
		}
		
		return pNode;
		
	}

	/**
	 * Assigns either type real or integer to expressions
	 */
	private void assignTypes(CompoundStatementNode compStatNode) {
		
		ArrayList<StatementNode> statementList = compStatNode.getStateNodes();
		
		for(StatementNode current : statementList) {
			if(current instanceof AssignmentStatementNode) {
				setTypes(((AssignmentStatementNode) current).getExpression());
				
				if((vTypes.get(((AssignmentStatementNode) current).getLvalue().getName()) !=null)){
					((AssignmentStatementNode) current).getLvalue().setType(vTypes.get(((AssignmentStatementNode) current).getLvalue().getName()));
					
				}
			}
			else if (current instanceof IfStatementNode) {
				
				setTypes(((IfStatementNode) current).getTest());
				
				StatementNode then = (((IfStatementNode) current).getThenStatement());
				StatementNode elseStatement = (((IfStatementNode) current).getElseStatement());
				
				CompoundStatementNode ifThen = new CompoundStatementNode();
				
				ifThen.addStatement(then);
				ifThen.addStatement(elseStatement);
				
				assignTypes(ifThen);
				
			}
			
			else if(current instanceof CompoundStatementNode) {
				
				assignTypes(((CompoundStatementNode) current));
				
			}
			
		}
		
	}

	
	
	
	

	/**
	 * Sets the expression type for nodes. Either real or integer.
	 */
	private void setTypes(ExpressionNode expression) {
		
		if(getLeft(expression) instanceof OperationNode) {
			setTypes(getLeft(expression));
		}
		else if(getLeft(expression) instanceof VariableNode || getLeft(expression) instanceof ValueNode) {
			setVariable(getLeft(expression));
		}
		
		if(getRight(expression) instanceof OperationNode) {
			setTypes(getRight(expression));
		}
		else if(getRight(expression) instanceof VariableNode || getRight(expression) instanceof ValueNode) {
			setVariable(getRight(expression));
		}
		
		if(expression instanceof OperationNode) {
			
			if(getLeft(expression).getType() == DataType.DREAL || getRight(expression).getType() == DataType.DREAL) {
				expression.setType(DataType.DREAL);
			}
			else {
				expression.setType(DataType.DINTEGER);
			}
			
		}
		
	}




	/**
	 * setter for the variable or value for the expression
	 */
	private void setVariable(ExpressionNode expression) {
		
		if(expression instanceof ValueNode) {
			if(((ValueNode)expression).getAttribute().contains(".")) {
				expression.setType(DataType.DREAL);
			}
			else {
				expression.setType(DataType.DINTEGER);
			}
		}
		
	}

	/**
	 * getter for the right expression node
	 */
	private ExpressionNode getRight(ExpressionNode expression) {
		ExpressionNode answer = null;
		
		if(expression instanceof OperationNode) {
			answer = ((OperationNode) expression).getRight();
		}
		return answer;
	}
	
	/**
	 * getter for the left expression node
	 */
	private ExpressionNode getLeft(ExpressionNode expression) {
		ExpressionNode answer = null;
		
		if(expression instanceof OperationNode) {
			answer = ((OperationNode) expression).getLeft();
		}
		return answer;
	}


	/**
	 * Checks to see if variables are declared.
	 */
	private void verifyVariables() {
		
		ArrayList<String> declaredNames = new ArrayList<String>();
		
		for (int i = 0; i < pNode.getVariables().getDeclarations().size(); i++) {
			declaredNames.add(pNode.getVariables().getDeclarations().get(i).getName());
		}
		
		ArrayList<String> usedNames = pNode.getVarNames();
		
		for(int i = 0; i < usedNames.size(); i++) {
			if(!declaredNames.contains(usedNames.get(i))) {
				System.out.println("Error with declaring variables");
			}
		}
		
	}
	
	
	
	
	
	
}
