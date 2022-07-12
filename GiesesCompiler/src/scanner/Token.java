package scanner;


/**
 *Class that returns the token
*/
public class Token{
	
	public TokenType type;
    private String contents;
    
    /**
     * 
     * @param input - The next element from the document to be looked at by the scanner
     */
    public Token( String input, TokenType t)
    {
        this.contents = input;
        this.type = t;
    };
    
    public String getLexeme() { return this.contents;}
    
    
    public TokenType getType() {
    	return this.type;
    }
    
    public void setContents(String s) {
    	this.contents = s + " ";
    }
    
    public String toString() { return "Token: " + this.contents + " " + "Token Type: " + this.type;}
}
