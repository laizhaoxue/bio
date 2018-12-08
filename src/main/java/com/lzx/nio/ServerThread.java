/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 14:11
 **/
package com.lzx.nio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;

public class ServerThread implements Runnable {
    private Socket socket;
    private BlockingDeque<Socket> clients;
    private String msg;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    public ServerThread(){};
    public ServerThread(Socket socket,BlockingDeque<Socket> clients){
        this.socket=socket;
        this.clients=clients;
    }
    @Override
    public void run() {
        System.out.println("线程开始启动");
        msg = "欢迎【" + socket.getInetAddress() + "】进入聊天室！当前聊天室有【"
                + clients.size() + "】人";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
        }catch (Exception e){
            e.printStackTrace();
        }
        send();
        receive();
    }

    public  void  send(){
        clients.forEach((client)->{
            try {
                printWriter = new PrintWriter(client.getOutputStream(),true);
                System.out.println("向客户端发送消息："+msg);
                printWriter.println(msg);
                //printWriter.flush();
            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }

    public void  receive(){
        try {
            while (true){
                if((msg=bufferedReader.readLine())!=null){
                    System.out.println("服务器收到消息"+msg);
                    send();

                }
            }
        }catch (Exception e){
            e.printStackTrace();;
        }

    }
}