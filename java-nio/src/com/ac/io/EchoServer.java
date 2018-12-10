package com.ac.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    public static final int PORT = 9999;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        ExecutorService executorService = Executors.newCachedThreadPool();

        System.out.println("Server started. Listening in port : " + PORT + "\t on thread  : " + Thread.currentThread().getName());

        while(true) {
            Socket client = server.accept();
            executorService.submit(new Runnable() {

                @Override
                public void run() {

                    System.out.println("New connection served using thread : " + Thread.currentThread().getName());

                    try (PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {

                        out.println("Welcome, this is the echo server");
                        out.flush();

                        String data;
                        while ((data = in.readLine()) != null) {
                            out.write(data + "\n");
                            out.flush();
                            if (data.equals("exit"))
                                break;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}