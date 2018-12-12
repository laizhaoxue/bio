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
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
    private static String impress ="127.0.0.1";
    private static int port=9090;
    private SocketChannel socketChannel;
    private Selector selector;
    private Charset charset = Charset.forName("utf-8");

    public Client(){

    }
    public Client(String impress,int port){
        this.impress=impress;
        this.port=port;
    }

    public void connect() throws IOException {
        selector = Selector.open();
        socketChannel= SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(impress,port));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        new Thread(new ClientThread(selector)).start();
        for (;;){
            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();
            socketChannel.write(charset.encode(msg));
        }
    }


    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();


    }
}