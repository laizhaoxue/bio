/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 17:26
 **/
package com.lzx.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class ClientThread implements Runnable{
   private Selector selector;
    private Charset charset = Charset.forName("utf-8");
    private CharsetDecoder charsetDecoder = charset.newDecoder();
   public ClientThread(){
      super();
   }
   public ClientThread(Selector selector){
       this.selector=selector;
   }
    @Override
    public void run() {
        try {
            while (true){
              int selectorLine = selector.select();
              if(selectorLine==0){
                  continue;
              }
               Set<SelectionKey> keys= selector.selectedKeys();
               Iterator<SelectionKey> iterator = keys.iterator();
               while (iterator.hasNext()){
                   SelectionKey selectionKey = iterator.next();
                   if(selectionKey.isReadable()){
                      SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                       ByteBuffer bb = ByteBuffer.allocate(1024);
                       if (socketChannel.write(bb)>0){
                           bb.flip();
                           System.out.println(charsetDecoder.decode(bb).toString());
                           bb.clear();
                       }

                   }
                   iterator.remove();
               }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}