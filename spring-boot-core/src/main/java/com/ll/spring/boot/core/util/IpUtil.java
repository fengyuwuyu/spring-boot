package com.ll.spring.boot.core.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/** 
 * @description: 从request里面获得IP 
 */
public class IpUtil {
    
    public static String getLocalAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "127.0.0.1";
    }

	public static String getIpAddress(HttpServletRequest request)
	{
		String ip=request.getHeader("X-Forwarded-For");
		if(ip == null || ip.length() == 0) {
			ip=request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0) {
			ip=request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0) {
			ip=request.getRemoteAddr();
		}
		if(ip!=null) {
			String[] temp = ip.split(",");
			if(temp.length>1)
				ip = temp[0];
		}
		return ip.trim();
	}
	
	/**
	 * 
	 * @Description 返回所有网络接口
	 * 
	 * @date 2013-6-4 下午05:58:53 
	 * @return
	 * @throws SocketException NetworkInterface[]
	 */
	public static NetworkInterface[] getAllNetworkInterface() throws SocketException {
		Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
		List<NetworkInterface> list = new ArrayList<NetworkInterface>();
		
		for( ;enumeration.hasMoreElements(); ) {
			list.add(enumeration.nextElement());
		}
		
		return list.toArray(new NetworkInterface[list.size()]);
	}
	
	/**
	 * 
	 * @Description 返回物理地址, 每一字节用冒号分割.
	 * @date 2013-6-4 下午05:46:54 
	 * @return String
	 * @throws SocketException 
	 */
	public static String getPhysicalAddress( NetworkInterface networkInterface ) throws SocketException {
		byte[] bytearray = networkInterface.getHardwareAddress();
		
		return bytesToHexString( bytearray );
	}
	
	/**
	 * 
	 * @Description 
	 * @date 2013-6-4 下午05:54:35 
	 * @param src
	 * @return String
	 */
	public static String bytesToHexString(byte[] src) {
		if( src == null || src.length == 0 ) {
			return null;
		}
		
	    StringBuilder stringBuilder = new StringBuilder();
	    
	    if (src == null || src.length <= 0) {  
	        return null;  
	    }
	    
	    for (int i = 0; i < src.length; i++) {  
	        int v = src[i] & 0xFF;
	        
	        String hv = Integer.toHexString(v);
	        
	        if (hv.length() < 2) {  
	            stringBuilder.append(0);  
	        }
	        
	        stringBuilder.append(hv).append(":");  
	    }
	    
	    return stringBuilder.substring(0, stringBuilder.toString().length() - 1);
	}
	
	public static String getFirstPhysicalAddress() throws SocketException {
		
		NetworkInterface[] networkIntefaceArray = getAllNetworkInterface();
		NetworkInterface networkInterface = null;
		String result = null;
			
		for( int i = 0; i < networkIntefaceArray.length; i++ ) {
			networkInterface = networkIntefaceArray[i];
				
			result = getPhysicalAddress(networkInterface);
				
			if( result != null ) {
				return result;
			}
		}
		
		return null;
	}
	
	private final static int INADDRSZ = 4;

    /**
     * 把IP地址转化为字节数组
     * @param ipAddr
     * @return byte[]
     */
    public static byte[] ipToBytesByInet(String ipAddr) {
        try {
            return InetAddress.getByName(ipAddr).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }

    /**
     * 把IP地址转化为int
     * @param ipAddr
     * @return int
     */
    public static byte[] ipToBytesByReg(String ipAddr) {
        byte[] ret = new byte[4];
        try {
            String[] ipArr = ipAddr.split("\\.");
            ret[0] = (byte) (Integer.parseInt(ipArr[0]) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(ipArr[1]) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(ipArr[2]) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(ipArr[3]) & 0xFF);
            return ret;
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }

    }

    /**
     * 字节数组转化为IP
     * @param bytes
     * @return int
     */
    public static String bytesToIp(byte[] bytes) {
        return new StringBuffer().append(bytes[0] & 0xFF).append('.').append(
                bytes[1] & 0xFF).append('.').append(bytes[2] & 0xFF)
                .append('.').append(bytes[3] & 0xFF).toString();
    }

    /**
     * 根据位运算把 byte[] -> int
     * @param bytes
     * @return int
     */
    public static int bytesToInt(byte[] bytes) {
        int addr = bytes[3] & 0xFF;
        addr |= ((bytes[2] << 8) & 0xFF00);
        addr |= ((bytes[1] << 16) & 0xFF0000);
        addr |= ((bytes[0] << 24) & 0xFF000000);
        return addr;
    }

    /**
     * 把IP地址转化为int
     * @param ipAddr
     * @return int
     */
    public static int ipToInt(String ipAddr) {
        try {
            return bytesToInt(ipToBytesByInet(ipAddr));
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }

    /**
     * ipInt -> byte[]
     * @param ipInt
     * @return byte[]
     */
    public static byte[] intToBytes(int ipInt) {
        byte[] ipAddr = new byte[INADDRSZ];
        ipAddr[0] = (byte) ((ipInt >>> 24) & 0xFF);
        ipAddr[1] = (byte) ((ipInt >>> 16) & 0xFF);
        ipAddr[2] = (byte) ((ipInt >>> 8) & 0xFF);
        ipAddr[3] = (byte) (ipInt & 0xFF);
        return ipAddr;
    }

    /**
     * 把int->ip地址
     * @param ipInt
     * @return String
     */
    public static String intToIp(int ipInt) {
        return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
                .append((ipInt >> 16) & 0xff).append('.').append(
                        (ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
                .toString();
    }

   

   

   
    
	
	public static void main(String[] args ) throws SocketException {
		System.out.println(	getFirstPhysicalAddress() );
	}
}
