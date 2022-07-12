package syntaxtree;

public class WriteNode extends StatementNode{
	ExpressionNode content;
	
	public WriteNode(ExpressionNode content) {
		this.content = content;
	}
	
	
	public ExpressionNode getContent() {
		return content;
	}
	
	public String indentedToString(int level) {
		String answer = this.indentedToString(level);
		answer += "Write\n";
		answer += this.content.indentedToString(level + 1);
		return answer;
	}
	
	
	
	
	
	
}
