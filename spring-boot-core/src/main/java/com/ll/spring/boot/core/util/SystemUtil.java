package com.ll.spring.boot.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SystemUtil {
    
    public static void main(String[] args) throws UnknownHostException, SocketException {
        getMac(null);
    }

    /**
     * 
     * @param ia 若ia为空，则获取本机的mac
     * @throws SocketException
     * @throws UnknownHostException
     */
    public static String getMac(InetAddress ia) {
        try {
            ia = ia == null ? InetAddress.getLocalHost() : ia;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        byte[] mac = null;
        try {
            mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
            return null;
        }
        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i]&0xff;
            String str = Integer.toHexString(temp);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }
}
