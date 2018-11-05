/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 10:13
 **/
package com.lzx.bio;

import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;


@NoArgsConstructor
public class Server {
   @Setter
    private static  int DEFAULT_PORT=7777;
    //shared Obj,may ++ or --,the same as Set + volatile
    private static BlockingDeque<Socket> clients = new LinkedBlockingDeque<>();
    private static ServerSocket socket ;
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public void start () throws IOException {
        if(socket!=null) return;
        socket = new ServerSocket(DEFAULT_PORT);
        System.out.println("服务端启动成功");
        for(;;){
            Socket client = socket.accept();
            System.out.println("收到客户端"+client.getInetAddress()+"连接");
            clients.add(client);
            pool.submit(new ServerThread(client,clients));
        }
    }

    public int size(){
        return clients.size();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}