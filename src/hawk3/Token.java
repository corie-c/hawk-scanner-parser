/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hawk3;

/**
 *
 * @author bhcor
 */
public class Token {
           private  TokenType type; //will store the token it represents like KEYWORD or NUMBER
    private String lexeme; //stores actual text that makes token
   
    
    public Token(TokenType type, String lexeme){
        this.type = type; //assigns value type to parameter type
        this.lexeme = lexeme;
      
    }//initializes the type and lexeme

   
    
         
 
    public TokenType getType() {
        return type;
    } //returns value of type 
    public String getLexeme() {
        return lexeme;
    }//returns value of lexeme
    
  
    public String toString(){
        return type + ":" + lexeme;
    }
   
    
}


