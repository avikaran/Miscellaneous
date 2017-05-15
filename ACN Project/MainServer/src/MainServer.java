import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Avikaran on 2/17/2017.
 */
public class MainServer extends Thread
{

    HashMap<String, InetAddress> hmap = new HashMap<>();
    private ServerSocket serverSocket;

    public MainServer(int port) throws Exception
    {
        serverSocket = new ServerSocket(port);

    }


    /****************************************************************************
     * Function: register(String name, InetAddress ip)
     * Descr: This function is used to save the client name with his IP address.
     ****************************************************************************/
    public void register(String name, InetAddress ip)
    {
        hmap.put(name,ip);
    }



    /****************************************************************************
     * Function: dnsLookup(String name)
     * Descr: This function perfomrs a dns lookup and returns the IP corresponding
     *        to the name.
     ****************************************************************************/
    public String dnsLookup(String name)
    {
        System.out.println("Reached inside DNS LOOKUP");
        return hmap.get(name).toString();
    }

    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                System.out.println("Waiting for a client on port : " + serverSocket.getLocalPort());
                Socket socket = serverSocket.accept();
                System.out.println("Connected to " + socket.getRemoteSocketAddress());
                DataInputStream input = new DataInputStream(socket.getInputStream());
                String message="";
                message = input.readUTF();
                String name = input.readUTF();

                if(message.equals("1"))  //message = 1 tells the server that this is the first time someone is connecting.
                {
                    String userIP = input.readUTF();
                    System.out.println("New incoming Client. Registering...");
                    System.out.println("CLIENT IP IS : " + userIP);
                    InetAddress ip = InetAddress.getByName(userIP);
                    register(name,ip);
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Registered Successfully");
                    out.writeUTF("Registered Successfully : " + userIP);

                }
                else
                {
                    System.out.println("Client asked for friend's IP");
                    System.out.println("Performing a DNS Lookup...");
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    String temp = dnsLookup(name);
                    System.out.println(temp.substring(1,temp.length()));
                    temp = temp.substring(1, temp.length());
                    System.out.println("Friend's IP is : " + temp);
                    System.out.println("Sending this to the client");
                    out.writeUTF(temp);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }


    }

    public static void main(String[] args) throws Exception
    {
        int port = 0;
        Thread t = new MainServer(port);
        t.start();
    }

}
