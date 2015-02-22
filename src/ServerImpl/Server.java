
package ServerImpl;


/**
 * Programmed By Sanat Rohatgi
 */

import static ServerImpl.dowork.ThreadReadToOtherThreadWrite;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    static dowork obj[];
    static int ConnectionPresent = 0;

    void reduceNoOfConnections() { //reduces threadCount when User exits
        ConnectionPresent -= 1;
    }

    public static void main(String br[]) throws Exception {
     
        ServerSocket ss = new ServerSocket(444);    //Start server at port 444
        Socket client;
        obj = new dowork[5];
        while (true) {
            if (ConnectionPresent < 5) {
                client = ss.accept(); //Accept Client if the thread count is less than N
                ConnectionPresent += 1;
                obj[ConnectionPresent] = new dowork(client); //Initialize dowork Object for new user connection
                
            }

        }
    }
}

 class dowork implements Runnable {

    
    static HashMap<String, PrintWriter> UserNameToWriteObj = new HashMap<>(); // Maps user name to its printwt=riter objects
    static HashMap<PrintWriter, BufferedReader> writeToRead = new HashMap<>();//Maps the User printwriter object to its bufferedreader object
    static HashMap<BufferedReader, PrintWriter> ThreadReadToOtherThreadWrite = new HashMap<>();// Maps the User buffered reader object
                                                                                               // to the printwriter of the user it is conneted to. 
    Socket client1;// Socket of the User 
    Thread t;
    PrintWriter printWriterOfCurrentThread;
    BufferedReader ReaderOfCurrentThread;
    PrintWriter PrintWriterOfConnectedTOclient;
    boolean threadExitFlag, threadConnectedToOtherUserFlag;
    String UserName;

    dowork() {
    }

    dowork(Socket client2) throws IOException {
        this.threadConnectedToOtherUserFlag = false;
        this.threadExitFlag = false;
        t = new Thread(this);
        client1 = client2;
        printWriterOfCurrentThread = new PrintWriter(client1.getOutputStream(), true);
        ReaderOfCurrentThread = new BufferedReader(new InputStreamReader(client1.getInputStream()));
         UserName=ReaderOfCurrentThread.readLine(); // reads the username trying to connect
       t.setName((t.getName()+"."+UserName));
        UserNameToWriteObj.put(t.getName(), printWriterOfCurrentThread);
        writeToRead.put(printWriterOfCurrentThread, ReaderOfCurrentThread);
        broadcast();            
        t.start(); // run thread
    }

    
    
    @Override
    public void run() {
        this.printWriterOfCurrentThread.println("Connection to Server Successful");
        while (true) {
            try {
                String ss2 = "";
                if (!threadConnectedToOtherUserFlag) {
                    this.printWriterOfCurrentThread.println("Which User Do You Want To Connect To?");
                    ss2 = ReaderOfCurrentThread.readLine(); // Input about which user it wants to connect to.
                                        
                     /*  
                     To check if the user is already busy!
                    
                    if(ss2.equals(Thread.currentThread().getName())){
                        
                     printWriterOfCurrentThread.println("You cannot connect to yourself!! Try a new name");
                     continue;
                     }
                     if(!CheckIfConnectionBusy(ss2)){
                     printWriterOfCurrentThread.println("The User is Already Busy");
                     continue;
                     }
                         */
                   
                           PrintWriterOfConnectedTOclient = UserNameToWriteObj.get(ss2);
                
                    if(!alreadyPresent(printWriterOfCurrentThread))
                {   if(PrintWriterOfConnectedTOclient!=null){
                     PrintWriterOfConnectedTOclient.println("Connection Asked:"+t.getName());
                    }
                } 
                    threadConnectedToOtherUserFlag = true;
                }
                 PrintWriterOfConnectedTOclient = UserNameToWriteObj.get(ss2);// Get PrintWriter obj of the thread thisThread is trying to connect to.
                
              keeptrack(ReaderOfCurrentThread, PrintWriterOfConnectedTOclient); // Feed data to Map

                if (threadExitFlag) {
                    keeptrack(ReaderOfCurrentThread, null);
                }

                printWriterOfCurrentThread.println("Connected to:" + ss2);
                if (PrintWriterOfConnectedTOclient == null) {  // If threads are ending ie user quits
                    cleanup(ReaderOfCurrentThread, printWriterOfCurrentThread); // Remove thread data from hashmaps
                    return;
                }
            } catch (IOException ex) {
                Logger.getLogger(dowork.class.getName()).log(Level.SEVERE, null, ex);
            }

            chatting obj = new chatting();
            synchronized (obj) {
                obj.chat(this.ReaderOfCurrentThread, this.printWriterOfCurrentThread, PrintWriterOfConnectedTOclient);// Chat function
            }
        }
    }
    
    
    static void keeptrack(BufferedReader object1, PrintWriter object2) {    //Manipulate the hashmap
        ThreadReadToOtherThreadWrite.put(object1, object2);
    }

    static boolean CheckIfConnectionBusy(String ss) { //Check if connection is already busy with another client
        return (ThreadReadToOtherThreadWrite.containsKey(writeToRead.get(UserNameToWriteObj.get(ss))));
    }
    static boolean alreadyPresent( PrintWriter pw){         //Check the thisUserThread has already  been connected by some other thread 
      return  ThreadReadToOtherThreadWrite.containsValue(pw);
    }

    static void cleanup(BufferedReader ReaderOfCleanUpThread, PrintWriter WriterOfCleanUpThread) throws IOException {   // Removing of thread related entries from hashmaps before the thread closes 
        String CleanupThreadName;

        Iterator th2 = UserNameToWriteObj.keySet().iterator();
        while (th2.hasNext()) {
            CleanupThreadName = (String) th2.next();
            if ((UserNameToWriteObj.get(CleanupThreadName)).equals(WriterOfCleanUpThread)) {
                UserNameToWriteObj.remove(CleanupThreadName);
                // System.out.println("it gets here???");
                broadcast();
                break;
            }
        }
        writeToRead.remove(WriterOfCleanUpThread);
        ThreadReadToOtherThreadWrite.remove(ReaderOfCleanUpThread);
        new Server().reduceNoOfConnections();
        ReaderOfCleanUpThread.close();
        WriterOfCleanUpThread.close();
    }

    static void broadcast() {   // To inform all connected Users about other connected  Users 
        Iterator it3 = UserNameToWriteObj.keySet().iterator();
        Iterator it2 = UserNameToWriteObj.keySet().iterator();

        while (it3.hasNext()) {
            PrintWriter te = UserNameToWriteObj.get(it3.next());
            te.println("Broadcast:1");  
            while (it2.hasNext()) {
                te.println("Broadcast:" + it2.next());  // Broadcast tag tells user that it is User info
            }
            it2 = UserNameToWriteObj.keySet().iterator();
        }

    }

}

class chatting {

    String s;
    BufferedReader ob1;
    PrintWriter temp2;

    void chat(BufferedReader ob, PrintWriter pw, PrintWriter temp2) { 
        ob1 = ob;

        try {
            do {
                if (temp2 != null) {
                    temp2 =ThreadReadToOtherThreadWrite.get(ob);
                    s = this.ob1.readLine(); // this function blocks here
                                             // the thread ,from which this data is entered, is activated performs its work.
                    temp2.println(s);
                    if (s.equals("EXIT")) {   // If the User Wants to Exit
                        pw.println("DISCONNECTED"); //Tell both Users to Disconnect 
                        temp2.println("DISCONNECTED");
                        new dowork().threadExitFlag=true; // inform the first thread is closing for cleanup
                        return;
                    }
                } else {
                    new dowork().threadExitFlag= true;//  inform the other thread is closing for cleanup
                    return;
                }
            } while (true);
        } catch (IOException ex) {
            Logger.getLogger(dowork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
