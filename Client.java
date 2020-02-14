package clientserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyClient {

    static Socket socket;
    static DataOutputStream outStream; // output stream to server
    static DataInputStream inStream; // input stream from server

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Incorrect arguments used!");
            System.out.println("Usage: java ClientClass hostName portNo");
            System.exit(1);
        }

        String host = args[0]; // get host
        int port = Integer.valueOf(args[1]).intValue(); // get port#

        try {           
            socket = new Socket(host, port); // create socket connection
            // get input output streams
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
            
        } catch (UnknownHostException e) {
            System.out.println(e);
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        chatStart(); // perform chatting loop

        try { // close resources
            socket.close();
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    } // end main

    // method chatStart
    public static void chatStart() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                String lineInput = sc.nextLine();

                if (lineInput.length() > 0) { // if client types something
                    outStream.writeBytes(lineInput); // send message to server
                    outStream.write(13); // carriage return
                    outStream.write(10); // line feed
                    outStream.flush(); // flush the stream line

                    if (lineInput.equalsIgnoreCase("quit")) {
                        System.exit(0); // stop client chatting as well.
                    }

                    // print any message received from server
                    int inByte;
                    System.out.print("From Server>>> ");
                    while ((inByte = inStream.read()) != '\n') {
                        System.out.write(inByte);
                    }
                    System.out.println();
                }
            } catch (IOException e) {
                System.out.println(e);
                System.exit(1);
            }
        }
    }
}
