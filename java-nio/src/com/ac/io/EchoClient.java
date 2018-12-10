package com.ac.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EchoClient {
    public static final String HOST = "localhost";
    public static final int PORT = 9999;

    public static void main(String[] args) {

        List<String> messages = Arrays.asList("Hello", "Hi", "Greeting", "exit");

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PORT));
            System.out.println("Connection established with server");

            try(PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println(in.readLine());

                Scanner cIn = new Scanner(System.in);

                while(true) {
                    String input = cIn.nextLine();
                    out.println(input);
                    out.flush();

                    System.out.println("Echoed Back msg : " + in.readLine());
                }
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
