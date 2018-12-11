/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 15:04
 **/
package com.lzx.nio;

import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    @Setter
    private static String adress="127.0.0.1";
    @Setter
    private static int port=9090;
    private SocketChannel socketChannel;
    private static String msg;

    public Client(){

    }
    public Client(String adress,int port){
        this.adress=adress;
        this.port=port;
    }

    public void connect() throws IOException {
        Selector selector = Selector.open();
        socketChannel= SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(adress,port));
        System.out.println("start connect to server");
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(new ClientThread(selector)).start();
        for (;;){
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            ByteBuffer bb = ByteBuffer.allocate(1024);
            bb.put(msg.getBytes());
            bb.flip();
            socketChannel.write(bb);
        }
    }


    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();


    }
}