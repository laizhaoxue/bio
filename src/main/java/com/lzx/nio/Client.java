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
import java.net.Socket;
import java.util.Scanner;

public class Client {
    @Setter
    private static String adress="127.0.0.1";
    @Setter
    private static int port=7777;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private static String msg;

    public Client(){

    }
    public Client(String adress,int port){
        this.adress=adress;
        this.port=port;
    }

    public void connect() throws IOException {
        socket = new Socket(adress,port);
        System.out.println("连接服务器成功");
        printWriter = new PrintWriter(socket.getOutputStream(),true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new ClientThread(bufferedReader)).start();
    }
    public void send(String msg) throws IOException {
        printWriter.println(msg);
        printWriter.flush();
    }

    private void receive() throws IOException {
        while ((msg=bufferedReader.readLine())!=null){
            System.out.println("收到服务端消息："+msg);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("请输入消息：");
            String msg = sc.nextLine();
            client.send(msg);
        }

    }
}