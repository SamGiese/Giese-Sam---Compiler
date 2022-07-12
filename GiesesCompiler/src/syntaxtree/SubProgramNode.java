package syntaxtree;


/**
 * 
 * @author Sam Giese
 *
 */
public class SubProgramNode extends SubProgramDeclarationsNode {
	String name;
	DeclarationsNode variables;
	CompoundStatementNode main;
	private SubProgramDeclarationsNode functions;
	
	
	public SubProgramNode(String f) {
		this.name = f;
	}



	public void setVariables(DeclarationsNode declarations) {
		this.variables = declarations;
		
		
	}
	
	public void setMain(CompoundStatementNode main) {
		this.main = main;
	}
	
	public CompoundStatementNode getMain() {
		return main;
	}



	public void setFunctions(SubProgramDeclarationsNode functions) {
		this.functions = functions;
	}
	
	
	
}