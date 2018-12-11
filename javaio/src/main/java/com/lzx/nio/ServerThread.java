/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 14:11
 **/
package com.lzx.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingDeque;

public class ServerThread implements Runnable {
    private SocketChannel client;
    private BlockingDeque<SocketChannel> clients;
    private String msg;
    public ServerThread(){};
    public ServerThread(SocketChannel client,BlockingDeque<SocketChannel> clients){
        this.client=client;
        this.clients=clients;
    }
    @Override
    public void run() {
        System.out.println("thread is start");
        try {
            msg = "welcome 【" + client.getRemoteAddress()+ "】talkedRoom！【"
                    + clients.size() + "】people in room";
        } catch (IOException e) {
            e.printStackTrace();
        }
        send();//每个客户端进来发送一次消息
       // receive();//由于channel是非阻塞的所以循环等待接受消息
    }

    public  void  send(){
        clients.forEach((ct)->{
            ByteBuffer byteBuffer=ByteBuffer.allocate(42);
            try {
                int count = ct.read(byteBuffer);
                while (count!=-1){
                    System.out.print("recive clinet's message:"+(char)byteBuffer.get());//recvie a byte at the time
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                byteBuffer.clear();
            }

        });
    }

    public void  receive(){
        try {
            while (true){
                if(1==1){
                    System.out.println("server accept:"+msg);
                    send();

                }
            }
        }catch (Exception e){
            e.printStackTrace();;
        }

    }
}