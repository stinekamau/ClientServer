//package com.mycompany.onlineaddressbook;
import javax.swing.plaf.multi.MultiMenuItemUI;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


//Runnable class allows us to create a task
//to be run on a thread
public class ClientHandler implements Runnable {
    private Socket socket;  //connected socket
    private ServerSocket serverSocket;  //server's socket
    private int clientNumber;
    
    //create an instance
    public ClientHandler(int clientNumber, Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.clientNumber = clientNumber;
    }//end ctor
    
    
    //run() method is required by all
    //Runnable implementers
    @Override
    public void run() {
        //run the thread in here
        try {
            DataInputStream inputFromClient =
                    new DataInputStream(socket.getInputStream());
            
            DataOutputStream outputToClient = 
                    new DataOutputStream(socket.getOutputStream());
            
             
            //continuously serve the client
            while(true) {                
                String strReceived = inputFromClient.readUTF();
                boolean root=false;
                System.out.println("\n\t[[Command " + strReceived +
                        " received from client " + clientNumber +"]]");



                if (strReceived.startsWith("add")) {
                    List<String> loggers=new ArrayList<>(MultiServerJPB1.current.keySet());
                    if(loggers.size()<1)
                    {
                        outputToClient.writeUTF("401 You are not currently logged in, login first" );


                    }
//                    System.out.println("adding client");
                    MultiServerJPB1.address.put(strReceived.split(" ")[1].trim(), strReceived.split(" ")[2]);
                    String [] ans=strReceived.split("\\s+");
                    MultiServerJPB1.counter++;
                    MultiServerJPB1.userMap.put(MultiServerJPB1.counter,strReceived.split("\\s+")[1]);


                    if(ans.length>3) {
                        String fname = strReceived.split(" ")[1];
                        String lname = strReceived.split(" ")[3];
                        String number = strReceived.split(" ")[4];
                        MultiServerJPB1.AddressBook user = new MultiServerJPB1.AddressBook(fname, lname, number);
                    }

                    outputToClient.writeUTF("client was added!");
                } else if (strReceived.startsWith("login")) {
                    List<String> keyset = new ArrayList<>(MultiServerJPB1.address.keySet());
                    boolean isValid = keyset.contains(strReceived.split(" ")[1]);
//                    outputToClient.writeUTF(MultiServerJPB1.address.get(strReceived.split(" ")[1]));
                    String name = " ";
                    if (MultiServerJPB1.address.get(strReceived.split(" ")[1]) != null) {
                        name = MultiServerJPB1.address.get(strReceived.split(" ")[1]);
                    } else {
                        name = "";
                    }

                    if (strReceived.split(" ")[2].equals(name)) {
                        outputToClient.writeUTF("200 OK");
                        root = true;
                        MultiServerJPB1.current.put(strReceived.split(" ")[1], InetAddress.getLocalHost().getHostAddress());
                    } else {
                        outputToClient.writeUTF("410 Wrong UserID or Password");
                    }
                }
                else if (strReceived.startsWith("list"))
                {
//                    List<String> keyset = new ArrayList<>(MultiServerJPB1.address.keySet());
                    MultiServerJPB1.AddressBook rand=new MultiServerJPB1.AddressBook();
                    StringBuilder ans=rand.traverse();

                    String s="Available users are:  "+"\n"+ans;
                    outputToClient.writeUTF(s);

                }
                else if (strReceived.startsWith("logout")) {
                    root = false;
                    MultiServerJPB1.current.clear();
                    //(strReceived.split(" ")[1])
                    outputToClient.writeUTF("200 OK");


                }
                else if(strReceived.startsWith("look"))
                {
                    MultiServerJPB1.AddressBook rand=new MultiServerJPB1.AddressBook();
                    int i=Integer.parseInt(strReceived.split(" ")[1]);
                    List<ArrayList<String>> ans=rand.search(i,strReceived.split(" ")[2]);
                    if(ans.size()>1)
                    {
                        StringBuilder p=new StringBuilder();
                        for(int t=0;t<ans.size();t++)
                        {
                            p.append(ans.get(t).toString()+"\n");
                        }
                        String s="200 OK"+"\n"+" Found "+ans.size()+" match"+"\n"+p;



                        outputToClient.writeUTF(s);
                    }
                    else
                    {
                        outputToClient.writeUTF("404 Your search did not match any records");
                    }

                }
                else if (strReceived.startsWith("delete")) {
                    List<String> loggers=new ArrayList<>(MultiServerJPB1.current.keySet());

                    if(loggers.size()<1)
                    {
                        outputToClient.writeUTF("401 You are not currently logged in, login first" );

                    }
                    int n=Integer.parseInt(strReceived.split("\\s+")[1]);
                    MultiServerJPB1.address.remove(MultiServerJPB1.userMap.get(n));
                    MultiServerJPB1.current.remove(MultiServerJPB1.userMap.get(n));
                    String s="Client "+MultiServerJPB1.userMap.get(n)+" Removed!";
                    outputToClient.writeUTF( s);

                }
                else if(strReceived.startsWith("who"))
                {

                    StringBuilder current_users=new StringBuilder();
                    Set st=MultiServerJPB1.current.entrySet();
                    Iterator i=st.iterator();
                    while(i.hasNext())
                    {
                        Map.Entry item=(Map.Entry)i.next();
                        current_users.append(item+"\n");
                    }

                    String s="200 OK"+"\n"+"The  list of the active users:"+"\n"+current_users;
                    outputToClient.writeUTF(s);


                }
                else if (strReceived.startsWith("shutdown")) {
                    List<String> loggers=new ArrayList<>(MultiServerJPB1.current.keySet());

                    if(loggers.size()<1)
                    {
                        outputToClient.writeUTF("401 You are not currently logged in, login first" );

                    }
                    System.out.println("Shutting down server...");
                    outputToClient.writeUTF("Shutting down server...");
                    serverSocket.close();
                    socket.close();
                    System.exit(1);
                     //get out of loop
                }

                else if (strReceived.equalsIgnoreCase("quit")) {
                    System.out.println("Shutting down server...");
                    outputToClient.writeUTF("Shutting down server...");
                    serverSocket.close();
                    socket.close();
                    System.exit(1);

                } else {

                    System.out.println("Unknown command received: "
                            + strReceived);
                    outputToClient.writeUTF("Unknown command.  "
                            + "Please try again. with s");

                }
            }//end while

            }//end while

        catch(IOException ex) {
            ex.printStackTrace();
        }//end try-catch

    }//end run

}//end ClientHandler
