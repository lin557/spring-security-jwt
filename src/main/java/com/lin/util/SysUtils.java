package com.lin.util;

import java.lang.management.ManagementFactory;
import java.util.UUID;

/**
 * 系统工具类
 * @author zhen.lin
 * @date 2019年8月16日
 */
public class SysUtils {

    /**
     * 获取uuid 中间不带-
     * 
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
    
    /**
     * 获取进程id
     * 
     * @return
     */
    public static int getPid() {
        String sName = ManagementFactory.getRuntimeMXBean().getName();
        // get pid
        String pid = sName.split("@")[0];
        return Integer.parseInt(pid);
    }
    
    /**
     * 判断当前系统是否为windons
     * @return
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name");  
        String flag = "win";
        if (os.toLowerCase().startsWith(flag)){  
            return true;
        } else {
            return false;
        }
    }
    
}
