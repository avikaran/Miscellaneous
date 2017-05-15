//Main controller Project
package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Controller {


    int LISTENING_PORT_MSGS = 5555;
    int LISTENING_PORT_FILES = 5000;
    int FRIEND_LISTENING_PORT_MSGS = 5555;
    int FRIEND_LISTENING_PORT_FILES = 5000;
    public SoundRecorder recorder;
    //These tasks are executed parallely in their own thread.
    Task<Void> taskListenIncomingMessages;
    Task<Void> taskSetup; // task to listen for incoming connections
    Task<Void> taskFile;
    static Client client;
    static String clientName;

    @FXML
    private  TextField username;

    @FXML
    private  TextField password;

    @FXML
    private  TextField serverIP;

    @FXML
    private  TextField serverPort;

    @FXML
    public TextField friendName;

    @FXML
    public  Button connectButton ;

    @FXML
    public  Button sendButton ;

    @FXML
    public TextArea messageBox;

    @FXML
    public  TextArea outgoingMessageField;

    @FXML
    public Button readyButton;

    @FXML
    public Button sendFileButton;

    /****************************************************************************
     * Function: Login(ActionEvent)
     * Descr: This function is called when the login button is clicked.
     *        The password is verified and the chat application starts
     ****************************************************************************/
    public void Login(ActionEvent eve) throws Exception
    {
        System.out.print("LOGGING IN");
        System.out.println(username.getText());
        //Avikaran
        if(password.getText().equals("password"))
        {
            System.out.println("WELCOME");
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("sample2.fxml"));
            primaryStage.setTitle("Client 1 Chat Window");
            primaryStage.setScene(new Scene(root, 428, 721));
            primaryStage.show();
            clientName = username.getText();
        }
        else
        {
            System.out.println("Wrong password");
        }
    }


    /****************************************************************************
     * Function: readyButton(ActionEvent)
     * Descr: This function is called when the ready button is clicked.
     *        The client gets registered with the server
     *        Starts listening for incoming chat connections and also incoming files
     ****************************************************************************/
    public void readyButton(ActionEvent e) throws Exception
    {
        client = new Client(clientName, serverIP.getText(), serverPort.getText()); // This registers the new client with the server
        connectButton.setDisable(false);
        messageBox.setDisable(false);
        friendName.setDisable(false);
        taskSetup = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("I am listening for incoming connections now ");
                ServerSocket serverSocket = new ServerSocket(LISTENING_PORT_MSGS);
                Socket socket = serverSocket.accept();
                Client.friendIP = socket.getInetAddress();
                Client.mainSocket = socket;
                Client.activeSession = true;
                connectionEstablished();
                return null;
            }
        };
        Thread taskSetupThread = new Thread(taskSetup);
        taskSetupThread.setDaemon(true);
        taskSetupThread.start();
        taskFile = new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                boolean endTransfers = false;
                System.out.println("I am listening for incoming Files ");
                ServerSocket serverSocket = new ServerSocket(LISTENING_PORT_FILES);
                while(!endTransfers)
                {
                    Socket socket = serverSocket.accept();
                    Client.fileSocket = socket;
                    System.out.println("Connection for files established");
                }
                return null;
            }
        };
        Thread taskFileThread = new Thread(taskFile);
        taskFileThread.setDaemon(true);
        taskFileThread.start();
    }


    /****************************************************************************
     * Function: connectionEstablished()
     * Descr: This function is called when either there is an incoming chat request
     *        or an outgoing chat request and the connection with the other party
     *        is established. This function starts a thread to listen for messages
     ****************************************************************************/
    public  void  connectionEstablished() throws Exception
    {
        sendButton.setDisable(false);
        sendFileButton.setDisable(false);
        outgoingMessageField.setDisable(false);
        friendName.setDisable(true);
        connectButton.setDisable(true);
        messageBox.setEditable(false);
        System.out.println("CONNECTION IS ESTABLISHED");
        taskListenIncomingMessages = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                System.out.println("Listening for messages");
                boolean endConversation = false;
                String incomingMessage="";
                DataInputStream input = new DataInputStream(Client.mainSocket.getInputStream());
                while(!endConversation)
                {
                    String fileName = "";
                    int fileSize;
                    incomingMessage = input.readUTF();
                    System.out.println("Incoming Message : ");
                    System.out.println(incomingMessage);
                    if(incomingMessage.equals("!@#$%"))
                    {
                        // user hass clicked on send file button
                        fileName = input.readUTF();
                        fileSize = Integer.parseInt(input.readUTF());
                        System.out.println("Incoming file size : " + fileSize);
                        recieveFile(fileName, fileSize, 0); // 0 means not receiving audio file
                        incomingMessage = fileName + " Recieved Successfully";
                    }
                    if(incomingMessage.equals("RECORDING!@#$%"))
                    {
                        fileName = "RecordAudio.wav";
                        fileSize = Integer.parseInt(input.readUTF());
                        recieveFile(fileName, fileSize,1); // 1 means recieving audio
                        incomingMessage = "Voice Message Received";
                    }
                    String finalIncomingMessage = incomingMessage;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            messageBox.setText(messageBox.getText()+ "\nIncoming Message:\n" +  finalIncomingMessage);
                        }
                    });
                }
                return null;
            }
        };
        Thread th1 = new Thread(taskListenIncomingMessages);
        th1.setDaemon(true);
        th1.start();
    }

    /****************************************************************************
     * Function: connect(ActionEvent)
     * Descr: This function is called when the user wants to initiate a chat with
     *        a friend and presses the connect button.
     ****************************************************************************/
    public void connect(ActionEvent e) throws Exception
    {
        connectButton.setDisable(true);
        Client.fName = friendName.getText();
        try
        {
            //We need to find the ip address of this friend. Make a connection with the server and ask for ip.
            Socket socket = new Socket(client.serverIP, client.serverPort); // This ip is the ip of EC2 machine
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF("2");           // 1 i am using to tell server that i am registering my self
            // 2 will be used if i want to tell server that i want the ip of my friend.
            output.writeUTF(Client.fName);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            Client.friendIP = InetAddress.getByName(input.readUTF());
            System.out.println("IP address of the friend is : " + Client.friendIP.toString());
            socket.close();
            Client.mainSocket = new Socket(Client.friendIP.toString().split("/")[1], FRIEND_LISTENING_PORT_MSGS);  // This should actually be using friendip nad listeningport(4444)
            //Client.mainSocket = new Socket("localhost", 4444);
            Client.activeSession = true;
            taskSetup = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    connectionEstablished();
                    return null;
                }
            };
            Thread taskSetupThread = new Thread(taskSetup);
            taskSetupThread.setDaemon(true);
            taskSetupThread.start();

        }catch(Exception e2)
        {

        }

    }

    /****************************************************************************
     * Function: sendData(ActionEvent)
     * Descr: This function is called when the user clicks on send. This is used
     *        to send messages to the friend.
     ****************************************************************************/
    public void SendData(ActionEvent event) throws Exception
    {
        String outgoingMessage = outgoingMessageField.getText();
        if(Client.mainSocket == null)
        {
            System.out.println("MAIN SOCKET IS NULL");
            return;
        }
        System.out.println("SENDING SOME DATA : " + client.mainSocket.isClosed());
        System.out.println("SENDING SOME DATA : " + client.mainSocket.isConnected());
        DataOutputStream output = new DataOutputStream(Client.mainSocket.getOutputStream());
        output.writeUTF(outgoingMessage);
        System.out.println("Outgoing Message : ");
        System.out.println(outgoingMessage);
        outgoingMessageField.setText("");
        messageBox.setText(messageBox.getText() + "\n" + "Outgoing Message : \n" + outgoingMessage);
    }


    /****************************************************************************
     * Function: sendFile(ActionEvent)
     * Descr: This function is called when the sendFile button is clicked.
     *        This is used to send a file to a friend.
     ****************************************************************************/
    public void sendFile(ActionEvent event) throws Exception
    {
        if(Client.mainSocket == null)
        {
            System.out.println("MAIN SOCKET IS NULL");
            return;
        }
        Client.fileSocket = new Socket(Client.friendIP.toString().split("/")[1], FRIEND_LISTENING_PORT_FILES);
        DataOutputStream output = new DataOutputStream(Client.mainSocket.getOutputStream());
        output.writeUTF("!@#$%");  // Its a signal that the use is sending a file
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        output.writeUTF(selectedFile.getName());
        output.writeUTF(String.valueOf(selectedFile.length()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(selectedFile));
        OutputStream os = Client.fileSocket.getOutputStream();
        byte[] myFileBytes = new byte[1024];
        int bytesRead = 0;
        while( (bytesRead = bis.read(myFileBytes)) > 0)
        {
            os.write(myFileBytes, 0, bytesRead); // puts the byte array onto output stream
            os.flush();
        }
        os.flush();
        os.close();
    }


    /****************************************************************************
     * Function: recieveFile(String fileName, int fileSize, int type)
     * Descr: This function is called when someone is trying to send a file.
     ****************************************************************************/
    public void recieveFile(String fileName, int fileSize, int type) throws Exception
    {
        byte[] mybytearray = new byte[1024];
        InputStream is = Client.fileSocket.getInputStream();
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int bytesRead;
        while ( (bytesRead = is.read(mybytearray, 0, 1024)) > 0)  // InputStream reader reads upto mybytearray.length amount of data from input stream and store it in mybytearray
        {
            bos.write(mybytearray, 0 , bytesRead);
        }
        bos.close();
        System.out.println("Done");
        if(type==1)
        {
            System.out.println("Will try to play");
            playMusic("RecordAudio.wav");
        }
    }

    /****************************************************************************
     * Function: startRecording(ActionEvent event)
     * Descr: This function is called when the user wants to send a voice message
     *        and he clicks on startRecording button.
     * source : http://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
     ****************************************************************************/
    public void startRecording(ActionEvent event)
    {
        recorder = new SoundRecorder();
        recorder.start();
    }


    /****************************************************************************
     * Function: stopAndSendRecording(ActionEvent event)
     * Descr: This function is called when the user clicks on stopAndSendRecording
     *        button. This function stops the recorder and send the recorded file
     *        to the friend.
     * source : http://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
     ****************************************************************************/
    public void stopAndSendRecording(ActionEvent event) throws  Exception
    {
        recorder.finish();

        if(Client.mainSocket == null)
        {
            System.out.println("MAIN SOCKET IS NULL");
            return;
        }
        Client.fileSocket = new Socket(Client.friendIP.toString().split("/")[1], FRIEND_LISTENING_PORT_FILES);
        DataOutputStream output = new DataOutputStream(Client.mainSocket.getOutputStream());
        output.writeUTF("RECORDING!@#$%");  // Its a signal that the use is sending a file

        File selectedFile = new File("RecordAudio.wav");
        System.out.println(" FILE SIZE : " + selectedFile.length());
        System.out.println(" FILE SIZE : " + (int)selectedFile.length());
        output.writeUTF(String.valueOf(selectedFile.length()));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(selectedFile));

        OutputStream os = Client.fileSocket.getOutputStream();

        byte[] myFileBytes = new byte[1024];
        int bytesRead = 0;
        while( (bytesRead = bis.read(myFileBytes)) > 0)
        {
            os.write(myFileBytes, 0, bytesRead); // puts the byte array onto output stream
            os.flush();
        }

        os.flush();
        os.close();

    }

    /****************************************************************************
     * Function: playMusic(String fileName)
     * Descr: This function is automatically called when the user receives a voice
     *        message
     * source : http://stackoverflow.com/questions/2416935/how-to-play-wav-files-with-java
     ****************************************************************************/
    public void playMusic(String fileName) throws Exception
    {
        final int BUFFER_SIZE = 128000;
        File soundFile;
        AudioInputStream audioStream;
        AudioFormat audioFormat;
        SourceDataLine sourceLine;
        soundFile = new File(fileName);
        audioStream = AudioSystem.getAudioInputStream(soundFile);
        audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceLine.open(audioFormat);
        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            nBytesRead = audioStream.read(abData, 0, abData.length);
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();

    }

}

