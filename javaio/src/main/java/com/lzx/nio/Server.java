/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 10:13
 **/
package com.lzx.nio;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;


@NoArgsConstructor
public class Server {
   @Setter
    private static  int DEFAULT_PORT=7777;
    //shared Obj,may ++ or --,the same as Set + volatile
    private static BlockingDeque<SocketChannel> clients = new LinkedBlockingDeque<>();
    private static ServerSocketChannel serverSocketChannel ;
    private static ExecutorService pool = Executors.newFixedThreadPool(4);
    private Charset charset = Charset.forName("utf-8");

    public void start () throws IOException {
        if(serverSocketChannel!=null) return;
        serverSocketChannel.socket().bind(new InetSocketAddress(9090));
        serverSocketChannel.configureBlocking(false);
        for(;;){
            SocketChannel client = serverSocketChannel.accept();
            if(client!=null){
                System.out.println("recive"+client.getRemoteAddress()+"连接connect");
                clients.add(client);
                pool.submit(new ServerThread(client,clients));
            }

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