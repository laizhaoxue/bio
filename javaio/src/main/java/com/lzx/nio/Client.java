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
        ByteBuffer byteBuffer =ByteBuffer.allocate(42);
        byteBuffer.put("aaaaa".getBytes());
        socketChannel.register(selector, SelectionKey.OP_WRITE);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        byteBuffer.clear();
        System.in.read();
    }
    public void send(String msg) throws IOException {


    }

    private void receive() throws IOException {

    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();


    }
}