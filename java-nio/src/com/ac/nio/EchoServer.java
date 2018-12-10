package com.ac.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EchoServer {
    public static final int PORT = 9999;
    Selector selector;
    Map<SelectionKey, SocketChannel> keyChannelMap = new HashMap<>();

    public EchoServer() {
        try {
            init();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        serverSocketChannel.bind(new InetSocketAddress(PORT));

        while (true) {
            selector.select(); //waits for next event to occur

            // wakeup to work on selected keys
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                // this is necessary to prevent the same key from coming up
                // again the next time around.
                keys.remove();

                if (! key.isValid()) {
                    continue;
                }

                //New connection
                if (key.isAcceptable()) {
                    accept(serverSocketChannel);
                } else if (key.isReadable()) {
                    echo(keyChannelMap.get(key));
                }
            }
        }
    }

    private void accept(ServerSocketChannel server) {
        try {
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            SelectionKey channelKey = channel.register(selector, SelectionKey.OP_READ);
            keyChannelMap.put(channelKey, channel);

            System.out.println("New connection accepted.. Selection key registered for READ : " + channelKey);

            //write welcome message
            channel.write(ByteBuffer.wrap("Welcome, this is the echo server\r\n".getBytes("US-ASCII")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void echo(SocketChannel channel) throws IOException {
        if (channel == null) return;

        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int numRead = -1;
        try {
            numRead = channel.read(buffer);
        } catch (IOException e) {
            disconnect(channel);
            System.out.println(e.getMessage());
        }

        System.out.println("Data read from channel " + numRead);

        if (numRead == -1) {
            disconnect(channel);
            return;
        }

        buffer.flip();
        System.out.println("Echoing back .. ");
        channel.write(buffer);
    }

    void disconnect(SocketChannel channel) throws IOException {
        SelectionKey key = channel.keyFor(selector);
        keyChannelMap.remove(key);
        channel.close();
        key.cancel();
    }

    public static void main(String[] args) throws IOException {
        new EchoServer();
    }
}