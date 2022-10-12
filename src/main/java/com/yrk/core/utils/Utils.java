package com.yrk.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @BelongsProject: study_RPC_Demo
 * @Author: ruikun
 * @CreateTime: 2022-10-11  19:03
 * @Description: TODO
 * @Version: 1.0
 */
public class Utils {
    /**
     * 对象转byte字节数组
     * @param bytes
     * @return
     */

    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        try {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
    /**
     * @description: 字节转换对象
     * @author: ruikun
     * @date: 2022/10/11 18:43
     * @param: [bytes]
     * @return: java.lang.Object
     **/
    public static Object bytesToObject(byte[] bytes) throws Exception {

        //byte转object
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn = new ObjectInputStream(in);
        return sIn.readObject();

    }
}
