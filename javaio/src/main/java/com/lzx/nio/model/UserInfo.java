/**
 * @program: javaio
 * @description:
 * @author: lzx
 * @create: 2018-12-11 14:14
 **/
package com.lzx.nio.model;

import java.net.InetAddress;

public class UserInfo {
    private String name;
    /**
    * isInit : if connected is true,else false
    */
    private boolean isInit=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public UserInfo(){

    }
    public UserInfo(String name){
        this.name=name;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                '}';
    }
}