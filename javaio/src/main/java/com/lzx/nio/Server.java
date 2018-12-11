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
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
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
        Selector selector = Selector.open();
        serverSocketChannel=ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9090));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select()>0){

            Set set = selector.selectedKeys();
            Iterator<SelectionKey> iterable =set.iterator() ;
            while(iterable.hasNext()){
               SelectionKey selectionKey = iterable.next();
                if(selectionKey.isReadable()){
                    SocketChannel sc=(SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1);

                   while (sc.read(byteBuffer)>0){
                       byteBuffer.flip();
                       System.out.print((char)byteBuffer.get());
                   };
                }
                if (selectionKey.isWritable()){
                    System.out.println("write is arrived");
                }
                if (selectionKey.isAcceptable()){
                    System.out.println("accept is arrived "+set.size());
                    SocketChannel sc =  ((ServerSocketChannel)selectionKey.channel()).accept();
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);

                }
                iterable.remove();
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