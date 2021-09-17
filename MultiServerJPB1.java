//package com.mycompany.onlineaddressbook;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MultiServerJPB1 {

    private static final int SERVER_PORT = 8764;
    public static int counter=0;
    public static HashMap<String, String> address = new HashMap<>();
    public static HashMap<String, String> current = new HashMap<>();
    public static HashMap<Integer,String> userMap=new HashMap<>();

    public static class AddressBook
    {
          public static List<ArrayList<String>> addressbook= new ArrayList<>();
          public AddressBook(String fname,String lname,String num)
          {
              ArrayList<String> temp=new ArrayList<>();
              temp.add(fname);temp.add(lname);temp.add(num);
              addressbook.add(temp);
          }
          public AddressBook()
          {
              //empty constuctor
          }
          public List<ArrayList<String>> search(int criteria,String item)
          {
              List<ArrayList<String>> tempo=new ArrayList<>();
             for(int i=0;i<addressbook.size();i++)
             {

                 if(addressbook.get(i).get(criteria-1).equalsIgnoreCase(item))
                 {

                     tempo.add(addressbook.get(i));
                 }

             }
             return tempo;
          }
          public StringBuilder traverse()
          {
              StringBuilder tr=new StringBuilder();
              for(int i=0;i<addressbook.size();i++)
              {
                  tr.append(addressbook.get(i)+"\n");
              }
              return tr;

          }

    }

    public static void main(String[] args) {
        AddressBook root=new AddressBook("root","raid","435-891-090");
        AddressBook john=new AddressBook("john","keen","573-342-212");
        AddressBook david=new AddressBook("david","stones","122-811-411");
        AddressBook mary=new AddressBook("mary","mary01","891-421-122");
        AddressBook add=new AddressBook("David","Miller","313-510-6001");
        AddressBook dde=new AddressBook("John","Miller","315-290-123");
        AddressBook ere=new AddressBook("Percy","Hiller","123-432-124");
        AddressBook tyu=new AddressBook("Intel","Seven","332-892-980");
        AddressBook ro=new AddressBook("root","raid","435-891-090");
        AddressBook jo=new AddressBook("john","keen","573-342-212");
        AddressBook da=new AddressBook("david","stones","122-811-411");
        AddressBook ma=new AddressBook("mary","mary01","891-421-122");
        address.put("root", "root01");
        address.put("john", "john01");
        address.put("david", "david01");
        address.put("mary", "mary01");


//        createCommunicationLoop();
        createMultithreadCommunicationLoop();

    }//end main

    public static void createMultithreadCommunicationLoop() {
        int clientNumber = 0;

        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on " + new Date() + ".");
            //listen for new connection request
            while (true) {
                Socket socket = serverSocket.accept();
                clientNumber++;  //increment client num


                DataInputStream inputFromClient =
                        new DataInputStream(socket.getInputStream());

                DataOutputStream outputToClient =
                        new DataOutputStream(socket.getOutputStream());

                //Find client's host name 
                //and IP address
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println("Connection from client " +
                        clientNumber);
                System.out.println("\tHost name: " +
                        inetAddress.getHostName());
                System.out.println("\tHost IP address: " +
                        inetAddress.getHostAddress());

                //create and start new thread for the connection
                Thread clientThread = new Thread(
                        new ClientHandler(clientNumber, socket, serverSocket));
                clientThread.start();

            }//end while
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }//end createMultithreadCommunicationLoop
}
    
