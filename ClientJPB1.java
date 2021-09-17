//package com.mycompany.onlineaddressbook;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;


public class ClientJPB1 {
    
    private static final int SERVER_PORT = 8764;

    public static void main(String[] args) {
        
        DataOutputStream toServer;
        DataInputStream fromServer;
        Scanner input = 
                new Scanner(System.in);
        String message;
        
        //attempt to connect to the server
        try {
            Socket socket = 
                    new Socket("localhost", SERVER_PORT);
            
            //create input stream to receive data
            //from the server
            fromServer =  new DataInputStream(socket.getInputStream());
            
            toServer = new DataOutputStream(socket.getOutputStream());
            
        System.out.println("Online Address Book II\t");
         System.out.println("----------------------\t");
         System.out.println("List of Commands:\t");
          System.out.println("Add"+", Delete"+", List"+", Who"+", Look"+", Quit"+", Login"+", Logout"+", Shutdown");
             while(true) {
                
                System.out.print("Send command to server:\t");
                message = input.nextLine().toLowerCase();
                if(message.startsWith("shutdown")||message.startsWith("quit"))
                    System.exit(1);
                toServer.writeUTF(message);


                
                //received message:
                message = fromServer.readUTF();
                 System.out.println("Server says: " + message);
             }
             
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch
        
        
    }//end main
}
