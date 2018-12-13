package com.ldzhn.baseframework.utils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * @Author : llliu
 * @Despriction : IO流操作工具类
 * @Date : create on 2018/9/20 21:44
 */
public class StreamUtil {
    public static void flush(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(Closeable closeable){
        if(closeable!=null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
