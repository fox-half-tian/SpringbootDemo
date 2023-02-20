package com.fox.miniodemo.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密解密工具
 *
 * @author 狐狸半面添
 * @create 2023-01-18 20:34
 */
public class CipherUtils {

    private static final String SECRET_KEY = "tangyulang5201314";
    private static final String AES = "AES";
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 生成密钥 key
     *
     * @param password 加密密码
     * @return
     * @throws Exception
     */
    private static SecretKeySpec generateKey(String password) throws Exception {
        // 1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        // 2. 因为AES要求密钥的长度为128，我们需要固定的密码，因此随机源的种子需要设置为我们的密码数组
        // 生成一个128位的随机源, 根据传入的字节数组
        /*
         * 这种方式 windows 下正常, Linux 环境下会解密失败
         * keyGenerator.init(128, new SecureRandom(password.getBytes()));
         */
        // 兼容 Linux
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        keyGenerator.init(128, random);
        // 3.产生原始对称密钥
        SecretKey original_key = keyGenerator.generateKey();
        // 4. 根据字节数组生成AES密钥
        return new SecretKeySpec(original_key.getEncoded(), AES);
    }

    /**
     * 加密
     *
     * @param content  加密的内容
     * @param password 加密密码
     * @return
     */
    private static String aESEncode(String content, String password) {
        try {
            // 根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES);
            // 基于加密模式和密钥初始化Cipher
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password));
            // 单部分加密结束, 重置Cipher, 获取加密内容的字节数组(这里要设置为UTF-8)防止解密为乱码
            byte[] bytes = cipher.doFinal(content.getBytes(CHARSET_NAME));
            // 将加密后的字节数组转为字符串返回
            return Base64.getUrlEncoder().encodeToString(bytes);
        } catch (Exception e) {
            // 如果有错就返回 null
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content  解密内容
     * @param password 解密密码
     * @return
     */
    private static String AESDecode(String content, String password) {
        try {
            // 将加密并编码后的内容解码成字节数组
            byte[] bytes = Base64.getUrlDecoder().decode(content);
            // 这里指定了算法为AES
            Cipher cipher = Cipher.getInstance(AES);
            // 基于解密模式和密钥初始化Cipher
            cipher.init(Cipher.DECRYPT_MODE, generateKey(password));
            // 单部分加密结束，重置Cipher
            byte[] result = cipher.doFinal(bytes);
            // 将解密后的字节数组转成 UTF-8 编码的字符串返回
            return new String(result, CHARSET_NAME);
        } catch (Exception e) {
            // 如果有错就返回 null
            return null;
        }


    }

    /**
     * 加密
     *
     * @param content 加密内容
     * @return 加密结果
     */
    public static String encrypt(String content) {
        return aESEncode(content, SECRET_KEY);
    }

    /**
     * 解密
     *
     * @param content 解密内容
     * @return 解密结果
     */
    public static String decrypt(String content) {
        try {
            return AESDecode(content, SECRET_KEY);
        } catch (Exception e) {
            return null;
        }
    }
}