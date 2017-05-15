package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Avikaran on 2/22/2017.
 */




public class Client {

    String name;                        //Name of this Client
    static String fName;
    static InetAddress friendIP;
    int serverPort ;
    String serverIP ;
    static boolean activeSession = false;
    static Socket mainSocket;
    static Socket fileSocket;


    /****************************************************************************
     * Function: registerClient()
     * Descr: This function establishes a connection with the server and registers
     *        its name and its local ip address with the server.
     ****************************************************************************/
    public void registerClient() throws Exception
    {
        System.out.println("Connecting to the server and trying to register...");
        Socket socket = new Socket(serverIP, serverPort); // This ip is the ip of EC2 machine
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.writeUTF("1");   // 1 i am using to tell server that i am registering my self
        // 2 will be used if i want to tell server that i want the ip of my friend.
        output.writeUTF(name);
        System.out.println(InetAddress.getLocalHost());
        output.writeUTF(socket.getLocalAddress().toString().split("/")[1]);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        System.out.println(input.readUTF().toString());
        socket.close();
    }

//    public void setServerDetails(String serverIP, String serverPort)
//    {
//        this.serverIP = serverIP;
//        this.serverPort = Integer.parseInt(serverPort);
//    }

    /****************************************************************************
     * Constructor : Client(String name, String serverIP, String serverPort)
     * Descr: This is used to initialize the client name, the server' ip address
     *        and the server's port number.
     ****************************************************************************/
    public Client(String name, String serverIP, String serverPort) throws Exception
    {
        System.out.println("Reaching the client code");
        this.name = name;
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);
        registerClient();
    }

}
