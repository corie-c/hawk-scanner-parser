
package hawk3;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Parser {
    private HawkScanner scanner;
    private Token currentToken;
    private Set<String>symbolTable;//stores the delcared identifiers and will keep track 

    public Parser(HawkScanner scanner) throws IOException {
        this.scanner = scanner; 
        this.currentToken = scanner.nextToken();
        this.symbolTable = new HashSet<>();//initilaizes symbol table
    }

    private void next() throws IOException {
        currentToken = scanner.nextToken();
    }

    private void error(String message) {
        System.err.println("There is an error on line: " + scanner.getLineNum() + ": " + message);
        System.exit(1);
    }

    private void match(TokenType expected) throws IOException {
        if (currentToken.getType() == expected) {
            next();
        } else {
            error("Expected " + expected);
        }
    }

    public void parseProgram() throws IOException {
        System.out.println("PROGRAM");
        match(TokenType.PROGRAM);

        //System.out.println("Current Token after PROGRAM: " + currentToken.getType() + " (" + currentToken.getLexeme() + ")");//debug
        /*if (currentToken.getType() != TokenType.BEGIN) {
            parseDeclSec();
        }
*/
        if(currentToken.getType() == TokenType.ID){
            parseDeclSec();
        }
        if(currentToken.getType() == TokenType.SEMICOLON){
            next();
        }
         //System.out.println("Expecting BEGIN - Current Token: " + currentToken.getType() + " (" + currentToken.getLexeme() + ")");//debug
        match(TokenType.BEGIN);
        parseStmtSec();
        match(TokenType.END);
    }

    private void parseDeclSec() throws IOException {
        System.out.println("DECL_SEC");
        //parseDecl();
        while (currentToken.getType() == TokenType.ID) {
            parseDecl();
            if (currentToken.getType() == TokenType.SEMICOLON){
                next();
            }
        }
    }

    private void parseDecl() throws IOException {
        System.out.println("DECL");
        parseIDList();
        // Add symbol being declared to the symbol table
        //if(currentToken.getType() == TokenType.BEGIN){//another debug
            //return;
        //}
        //if(currentToken.getType() == TokenType.COLON){
            //next();
        //}
        
        
        
        // Check for variable type
        if(currentToken.getType() == TokenType.COLON){
            next();
           if (currentToken.getType() == TokenType.INT || currentToken.getType() == TokenType.FLOAT || currentToken.getType() == TokenType.DOUBLE) {
            next(); // Consume the type
        
        }
        }
        
        
    }//end parseDecl

    private void parseIDList() throws IOException {
        System.out.println("ID_LIST");
        // save our current token
        symbolTable.add(currentToken.getLexeme());//add ID to symbol table
        match(TokenType.ID);
        while (currentToken.getType() == TokenType.COMMA) {
            next();
            // save our current token
            symbolTable.add(currentToken.getLexeme());//add ID to symbol table
            match(TokenType.ID);
            System.out.println("ID_LIST");
        }
    }

    private void parseStmtSec() throws IOException {
        System.out.println("STMT_SEC");
        while (currentToken.getType() != TokenType.END && currentToken.getType() != TokenType.EOF && currentToken.getType() != TokenType.ELSE) {
            //System.out.println("STMT_SEC: Current Token - " + currentToken.getType() + " (" + currentToken.getLexeme() + ")");
            if(currentToken.getType() == TokenType.SEMICOLON){
                next();
                continue; 
            }
            parseStmt();
        }
    }

    private void parseStmt() throws IOException {
        System.out.println("STMT");
        switch (currentToken.getType()) {
            case ID -> parseAssign();
            case IF -> parseIfStmt();
            case WHILE -> parseWhileStmt();
            case INPUT -> parseInput();
            case OUTPUT -> parseOutput();
            default -> error("Unexpected statement type: " + currentToken.getLexeme());
        }
    }

    private void parseAssign() throws IOException {
        System.out.println("ASSIGN");
        match(TokenType.ID);
        match(TokenType.ASSIGN);
        // Error check
        // Check if all variables have been declared
        // x := x *y;
        var temp = this.symbolTable;
        parseExpr();
        match(TokenType.SEMICOLON);
    }

    private void parseIfStmt() throws IOException {
        System.out.println("IFSTMT");
        match(TokenType.IF);
        parseComp();
        match(TokenType.THEN);
        parseStmtSec();
        //System.out.println("IF_STMT: Current Token before ELSE check: " + currentToken.getType());//debug
        if (currentToken.getType() == TokenType.ELSE) {
            //System.out.println("IF_STMT: Found ELSE - proceeding to parse ELSE block.");//debug
            next();
            parseStmtSec();
        }
        match(TokenType.END);
        match(TokenType.IF);
    }

    private void parseWhileStmt() throws IOException {
        System.out.println("WHILESTMT");
        match(TokenType.WHILE);
        parseComp();
        match(TokenType.LOOP);
        parseStmtSec();
        match(TokenType.END);
        match(TokenType.LOOP);
        if(currentToken.getType() != TokenType.SEMICOLON){
            error("Expected ';");
        }
    }

    private void parseInput() throws IOException {
        System.out.println("INPUT");
        match(TokenType.INPUT);
        parseIDList();
        match(TokenType.SEMICOLON);
    }

    private void parseOutput() throws IOException {
        System.out.println("OUTPUT");
        match(TokenType.OUTPUT);
        if (currentToken.getType() == TokenType.NUM) {
            next();
        } else {
            parseIDList();
        }
        match(TokenType.SEMICOLON);
    }

    private void parseExpr() throws IOException {
        System.out.println("EXPR");
        parseFactor();
        while (currentToken.getType() == TokenType.PLUS || currentToken.getType() == TokenType.MINUS) {
            next();
            parseFactor();
        }
    }

    private void parseFactor() throws IOException {
        System.out.println("FACTOR");
        parseOperand();
        while (currentToken.getType() == TokenType.MULTIPLY || currentToken.getType() == TokenType.DIVIDE) {
            next();
            parseOperand();
        }
    }

    private void parseOperand() throws IOException {
        System.out.println("OPERAND");
        switch (currentToken.getType()) {
            case NUM -> next();
            case ID -> {
                var currentVarName = currentToken.getLexeme();
                // if doesn't contain, call the error method
                // then break 
                if(!this.symbolTable.contains(currentVarName)){
                    error("Identifier not declared in: ");
                    var temp = currentToken;
                }
                next();
            }
            case LPAREN -> {
                next();
                parseExpr();
                match(TokenType.RPAREN);
            }
            default -> error("Unexpected operand");
        }
    }

    private void parseComp() throws IOException {
        System.out.println("COMP");
        match(TokenType.LPAREN);
        parseOperand();
        switch (currentToken.getType()) {
            case LT, GT, EQ, NEQ -> next();
            default -> error("Expected a comparison operator");
        }
        parseOperand();
        match(TokenType.RPAREN);
    }
}
