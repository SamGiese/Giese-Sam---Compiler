package syntaxtree;

import recognizer.DataType;

public class ArrayNode extends VariableNode{
	
	ExpressionNode expression;
	DataType type = null;
	
	public ArrayNode(String str, DataType t) {
		super(str);
		this.type = t;
	}
	
	public String getName() {
		return (super.getName());
	}
	
	public ExpressionNode getExpNode() {
		return this.expression;
	}
	
	
	public void setExpNode(ExpressionNode input) {
		this.expression = input;
	}
	
	public String indentedToString(int level) {
		String answer = this.indentation(level);
		answer += "Array: " + super.getName() + ", Type: " + type + "\n";
		if(expression != null) {
			answer += this.expression.indentedToString(level + 1);
		}
		
		return answer;
	}
	
	
	
	
	
	
	
}
