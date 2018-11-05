/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 15:04
 **/
package com.lzx.bio;

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
        System.out.println("开始连接服务器消息");
        socket = new Socket(adress,port);
        printWriter = new PrintWriter(socket.getOutputStream(),true);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        new Thread(new ClientThread(bufferedReader)).start();
    }
    public void send(String msg) throws IOException {
        printWriter = new PrintWriter(socket.getOutputStream(),true);
        printWriter.write(msg);
        printWriter.flush();

    }

    private void receive() throws IOException {
        System.out.println("开始接受服务器消息");
        while ((msg=bufferedReader.readLine())!=null){
            System.out.println(msg);
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.connect();
      //  client.send("xxxx");
        Scanner sc = new Scanner(System.in);
        while (true){
            System.out.println("请输入消息：");
            String msg = sc.nextLine();
            System.out.println(msg);
            client.send(msg);
        }

    }
}