package scanner;

import java.io.FileInputStream;
import java.io.InputStreamReader;

/*
Class that runs the input text file through the scanner to test it.
*/
public class TestMyScanner
{

    public static void main( String[] args)
    {
		//Takes in the input text file from args[0]
        String filename = args[0];
		//Use FileInputStream to take in bytes from the input file.
        FileInputStream fis = null;
		
		//Loops through file, grabbing tokens until it has gone through the entire thing.
        try {
            fis = new FileInputStream( filename);
        } catch (Exception e ) { e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader( fis);
        Scanner scanner = new Scanner( isr);
        Token aToken = null;
        do
        {
            try {
                aToken = scanner.nextToken();
            }
            catch( Exception e) { e.printStackTrace();}
            //if( aToken != null && !aToken.equals( ""))
                System.out.println("The token returned was " + aToken + " ");
        } while( aToken != null);
    }


}
