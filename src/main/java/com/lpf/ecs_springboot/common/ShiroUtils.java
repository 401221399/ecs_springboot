package com.lpf.ecs_springboot.common;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * Shiro工具类
 *
 */
public class ShiroUtils {

    /**  加密算法  SHA-256*/
    public final static String hashAlgorithmName = "SHA-256";
    /**  循环次数 */
    public final static int hashIterations = 16;

    //明码+盐+迭代次数=密文
    public static String sha256(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }



}
