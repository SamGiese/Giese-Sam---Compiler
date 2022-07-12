package scanner;

import static org.junit.Assert.fail;

import org.junit.Test;

//import static org.junit.Assert.*;
//import org.junit.Test;


/**
 * Unit test of the Token class
 * 
 * @author Sam Giese
 */


public class ScannerTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	
	  /**
     * Test of the nextToken function
     */
    @Test
    public void testF2c() {
        System.out.println("nextToken() function test");
        
        Token temp = new Token("program foo;\r\n" + 
        						"begin\r\n" + 
        						"end\r\n" + 
        						".", null);
        
        
    }
    
}
