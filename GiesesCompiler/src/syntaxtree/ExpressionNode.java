package syntaxtree;

import recognizer.DataType;

/**
 * General representation of any expression.
 * @author erik
 */
public abstract class ExpressionNode extends SyntaxTreeNode {
    
	DataType type;
	
	public ExpressionNode() {
		type = null;
	}
	
	public ExpressionNode(DataType type) {
		this.type = type;
	}
	
	public void setType(DataType data) {
		this.type = data;
	}
	
	public DataType getType() {
		return type;
	}
	
}
