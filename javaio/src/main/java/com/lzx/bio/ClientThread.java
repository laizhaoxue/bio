/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-11-01 17:26
 **/
package com.lzx.bio;

import java.io.BufferedReader;
import java.net.Socket;

public class ClientThread implements Runnable{
    private BufferedReader bufferedReader;
    private String msg;
    public ClientThread(){

    }
    public ClientThread(BufferedReader bufferedReader){
        this.bufferedReader=bufferedReader;
    }
    @Override
    public void run() {
          try {
               while ((msg=bufferedReader.readLine())!=null){
                   System.out.println(msg);
               }
          }catch (Exception e){
              e.printStackTrace();
          }
    }
}