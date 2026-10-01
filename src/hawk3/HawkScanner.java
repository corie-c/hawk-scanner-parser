/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hawk3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.util.Scanner;
public class HawkScanner {
    private final BufferedReader reader;
    private int lineNum = 1; 
    private int currentChar;
    
    public HawkScanner(String filePath) throws IOException{
        reader = new BufferedReader(new FileReader(filePath));
        next(); //intialize first character
    }//end scanner
    
    //go to next character
    private void next() throws IOException{
        currentChar = reader.read();
        
    }//end next
    
    //gets line number
    public int getLineNum(){
        return lineNum;
    }
    
    //gets the next token
    public Token nextToken() throws IOException{
        while(currentChar != -1){
            if (Character.isWhitespace(currentChar)){
                skipWhitespace(); //deals with whitespace in source
                continue;
            }//end whitespace if
            if(Character.isLetter(currentChar) || currentChar == '_'){
                 Token token = IDorKeyword();
            //System.out.println("Generated token: " + token.getType() + " (" + token.getLexeme() + ")");
            return token;
                //return IDorKeyword();
            }//end isLetter if
            if(Character.isDigit(currentChar)){
                 Token token = recognizeNum();
            //System.out.println("Generated token: " + token.getType() + " (" + token.getLexeme() + ")");
            return token;
                //return recognizeNum();
            }//end isDigit if
            switch(currentChar){//applies tokens to symbols
                 case '+': next(); return new Token(TokenType.PLUS, "+");
                case '-': next(); return new Token(TokenType.MINUS, "-");
                case '*': next(); return new Token(TokenType.MULTIPLY, "*");
                case '/': next(); return new Token(TokenType.DIVIDE, "/");
                case '(': next(); return new Token(TokenType.LPAREN, "(");
                case ')': next(); return new Token(TokenType.RPAREN, ")");
                case ';': next(); return new Token(TokenType.SEMICOLON, ";");
                //case ':': next(); return new Token(TokenType.COLON, ":");
                //case ',': next(); return new Token(TokenType.COMMA, ",");
                case ':':
                    next();
                    if (currentChar == '=') {
                        next();
                        return new Token(TokenType.ASSIGN, ":=");
                    }
                    return new Token(TokenType.COLON, ":");
                    
                case '<':
                    next();
                    if (currentChar == '>'){
                        next();
                        return new Token(TokenType.NEQ, "<>");//neq = not equal
                    }
                   return new Token(TokenType.LT, "<"); //lt = less than 
                case '>' : next(); return new Token(TokenType.GT, ">");//gt = greater than 
                case ',': next(); return new Token(TokenType.COMMA, ",");
                //deals with := and <>
                case '=': next(); return new Token(TokenType.EQ, "=");
                default:
                    System.out.println("Unrecognized symbol: " + (char)currentChar);
                    next();
                    return new Token(TokenType.ERROR, "Symbol not recognized");
            }//end switch
        }
        return new Token(TokenType.EOF, "");//end of file
    }//end nextToken
    
    private void skipWhitespace() throws IOException{
        while (Character.isWhitespace(currentChar)){
            if (currentChar == '\n'){
                lineNum++;//if character is whitespace increment line number
          
            }
              next();
        }
    }//end skip white space
    
    //keyworkds and identifiers
    private Token IDorKeyword() throws IOException{
        StringBuilder lexeme = new StringBuilder();
        while(Character.isLetterOrDigit(currentChar) || currentChar =='_'){
            lexeme.append((char)currentChar);
            next();
        }//end while
        String result = lexeme.toString();
        switch(result){
            case "program": return new Token(TokenType.PROGRAM, result);
            case "begin": return new Token(TokenType.BEGIN, result);
            case "end": return new Token(TokenType.END, result);
            case "input": return new Token(TokenType.INPUT, result);
            case "output": return new Token(TokenType.OUTPUT, result);
            case "int": return new Token(TokenType.INT, result);
            case "float": return new Token(TokenType.FLOAT, result);
            case "double": return new Token(TokenType.DOUBLE, result);
            case "if": return new Token(TokenType.IF, result);
            case "then": return new Token(TokenType.THEN, result);
            case "else": return new Token(TokenType.ELSE, result);
            case "while": return new Token(TokenType.WHILE, result);
            case "loop": return new Token(TokenType.LOOP, result);
            default: 
                return new Token(TokenType.ID, result);//identifier if not keyword
        }//end switch
    }//end id or keyword
    
    //numbers
    private Token recognizeNum() throws IOException{
        StringBuilder lexeme = new StringBuilder();
        while(Character.isDigit(currentChar)){
            lexeme.append((char)currentChar);
            next();
        }
        //floating points
        if (currentChar == '.'){
            lexeme.append((char)currentChar);
            next();
            while (Character.isDigit(currentChar)){
           lexeme.append((char)currentChar);
            next(); 
            }
            return new Token(TokenType.NUM, lexeme.toString());
        }
        return new Token(TokenType.NUM, lexeme.toString());
    }//end numbers
    
    public void close()  throws IOException{
        reader.close();
    }//closes reader when done 
    
    
}//end class
