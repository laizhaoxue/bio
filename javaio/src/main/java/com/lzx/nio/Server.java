/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 10:13
 **/
package com.lzx.nio;



import com.lzx.nio.model.UserInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;


public class Server {
    private static  int DEFAULT_PORT=7777;
    private static ServerSocketChannel serverSocketChannel ;
    private Charset charset = Charset.forName("utf-8");
    private CharsetDecoder charsetDecoder = charset.newDecoder();
    private Selector selector;
    public void start ()   {
        try {
            if(serverSocketChannel!=null) return;
            selector = Selector.open();
            serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9090));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            watching();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                selector.close();
                serverSocketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void watching() throws IOException {
        while (true){
            int selectLine = selector.select();//waitting for client
            Set set = selector.selectedKeys();
            Iterator<SelectionKey> iterable =set.iterator() ;
            if(selectLine==0){
                continue;
            }
            while(iterable.hasNext()){
                SelectionKey selectionKey = iterable.next();
                if(selectionKey.isReadable()){
                    SocketChannel sc=(SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(42);
                    UserInfo userInfo = (UserInfo) selectionKey.attachment();
                    if(sc.read(byteBuffer)>0){
                        byteBuffer.flip();
                        CharBuffer cb = charsetDecoder.decode(byteBuffer);
                        String message = cb.toString();
                        if(userInfo!=null&&userInfo.isInit()){//client is readyed for chat
                            broadcast(userInfo.getName(),message,selector);//notice  to all clinets
                        }else{
                            //get  client first input and  init user's name
                            UserInfo user = new UserInfo();
                            user.setName(message);
                            user.setInit(true);
                            selectionKey.attach(user);
                            sc.write(charset.encode("hello! "+user.getName()+" you can chat"));
                        }
                    }
                }
                if (selectionKey.isAcceptable()){
                    SocketChannel sc =  ((ServerSocketChannel)selectionKey.channel()).accept();
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);
                    sc.write(charset.encode("wellcom chat room，please input your name!"));
                }
                iterable.remove();
            }


        }
    }

     private void broadcast(String userName,String msg,final Selector selector) throws IOException {
            Set<SelectionKey> keys = selector.keys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                Channel channel = selectionKey.channel();
                if(channel instanceof SocketChannel){
                    ((SocketChannel) channel).write(charset.encode(userName+" say:"+msg));
                }
            }
     }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}