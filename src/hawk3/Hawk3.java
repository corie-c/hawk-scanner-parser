/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hawk3;

import java.io.IOException;
import java.util.Scanner;
public class Hawk3 {

   
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter the path to your source code file: ");
        String filePath = input.nextLine();
        
        try{
            hawk3.HawkScanner scanner = new hawk3.HawkScanner(filePath);
            Parser parser = new Parser(scanner);
            parser.parseProgram();
            scanner.close();
        }catch (IOException e){
            System.err.println("Error reading the file: " + e.getMessage());
        }finally{
            input.close(); //makes sure file closes
        }
  
    }
    
}
