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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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
            Selector selector = Selector.open();
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
        int selectLine = selector.select();
        while (selectLine>0){
            Set set = selector.selectedKeys();
            Iterator<SelectionKey> iterable =set.iterator() ;
            while(iterable.hasNext()){
                SelectionKey selectionKey = iterable.next();
                if(selectionKey.isReadable()){
                    SocketChannel sc=(SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    UserInfo userInfo = (UserInfo) selectionKey.attachment();
                    if(sc.read(byteBuffer)>0){
                        byteBuffer.flip();
                        CharBuffer cb = charsetDecoder.decode(byteBuffer);
                        String message = cb.toString();
                        if(userInfo!=null&&userInfo.isInit()){
                            //sendmessage(userInfo.getName(),message);
                            ByteBuffer bb = ByteBuffer.allocate(1024);
                            bb.put((userInfo.getName()+" say:"+message).getBytes());
                            sc.write(bb);
                        }else{
                            //get  client first input and  init user's name
                            UserInfo user = new UserInfo();
                            user.setName(message);
                            user.setInit(true);
                            selectionKey.attach(userInfo);
                            ByteBuffer bb = ByteBuffer.allocate(1024);
                            bb.put(("hello!"+user.getName()+"you can chat").getBytes());
                            sc.write(bb);
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

     private void sendmessage(String userName,String msg){

     }
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}