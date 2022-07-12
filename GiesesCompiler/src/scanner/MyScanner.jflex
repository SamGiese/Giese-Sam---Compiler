/**
 * Jflex file to create a scanner that detects the keywords and symbols for the mini pascal language
 * 
 */

/* Declarations */


%%

%class  Scanner   /* Names the produced java file */
%function nextToken /* Renames the yylex() function */
%type   Token      /* Defines the return type of the scanning function */


%eofval{
  return null;
%eofval}


%{
	LookupTable look = new LookupTable();
%}


/* Patterns */

id			  = {letter}({letter}|{digit}|"_")*
letter        = [A-Za-z]
addop		  = [\+\-]
decimal		  = {integer}(\.){integer}*
whitespace    = [\ \f\n\t\r]
Ex			  = [E]
exponent	  = ({integer}|{decimal}){Ex}{addop}{integer}
real		  = {decimal}|{exponent}
digit		  = [0-9]
integer		  = {digit}{digit}*
float	      = [0-9]+ \. [0-9]*



/* Symbols*/

symbol        =[.|,|.|:|;|(|)|\[|\]|<|>|*|/|+|\-|=]|<>|:=|<=|>=
other         = .






%%
/* Lexical Rules */

{id}     {
             if(look.get(yytext()) != null) return new Token(yytext(), look.get(yytext()));
			 return new Token(yytext(), TokenType.ID);
            }

{whitespace}  {  /* Ignore Whitespace */

              }
{symbol}	{
				return new Token(yytext(), look.get(yytext()));
				}

		   
/*all of the keywords and symbols lexical rules*/

{integer}  {
            return new Token( yytext(), TokenType.INTEGER);
            }
{float}		{
			 return( new Token( yytext(), TokenType.FLOAT));
			}

{real}	{
			return new Token(yytext(), TokenType.REAL);
			}

/* What the scanner will do should it find a character that fits into other.*/
{other}    {
             return new Token(yytext(), TokenType.ILLEGAL);
           }


			
			
			
			
			
			