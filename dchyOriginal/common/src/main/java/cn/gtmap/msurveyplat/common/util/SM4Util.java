package cn.gtmap.msurveyplat.common.util;


import cn.gtmap.msurveyplat.common.encrypt.SM4;
import cn.gtmap.msurveyplat.common.encrypt.SM4_Context;
import com.gtis.config.AppConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0, 2020/2/26.
 * @auto <a href="mailto:huangyongkai@gtmap.cn">huangyongkai</a>
 * @description
 */
public class SM4Util {

    private static final Logger logger = LoggerFactory.getLogger(SM4Util.class);
        public static String secretKey = AppConfig.getProperty("SECRET_KEY"); //"SM4_GTMAP_SECRET";
//    public static String secretKey = "SM4_GTMAP_SECRET";
    public static String iv = "SM4_GTMAP_IV_CBC";
    public static boolean hexString = false;
    public static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    public static String encryptData_ECB(String plainText) {
        try {
            if (StringUtils.isBlank(plainText)) {
                return null;
            }
            if (StringUtils.isBlank(secretKey)) {
                return plainText;
            }
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = hexStringToBytes(secretKey);
                //keyBytes = DataUtil.hexStringToBytes(secretKey);

            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, strToByteGbk(plainText));
            //byte[] encrypted = sm4.sm4_crypt_ecb(ctx, StringUtil.strToByteGbk(plainText));
            //String cipherText = Base64Util.encodeByteToBase64Str(encrypted);
            String cipherText = encodeByteToBase64Str(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            logger.error("encryptData_ECB plainText:{}  ERROR:{}", plainText, e);
        }
        return null;
    }

    public static String decryptData_ECB(String cipherText) {
        try {
            if (StringUtils.isBlank(cipherText)) {
                return null;
            }
            if (StringUtils.isBlank(secretKey)) {
                return cipherText;
            }
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            if (hexString) {
                keyBytes = hexStringToBytes(secretKey);
                // keyBytes = DataUtil.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, decodeBase64StrToByte(cipherText));
            //byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64Util.decodeBase64StrToByte(cipherText));
            //return StringUtil.byteToStrGbk(decrypted);
            return byteToStrGbk(decrypted);
        } catch (Exception e) {
            logger.error("decryptData_ECB plainText:{}  ", cipherText);
        }
        return null;
    }

    /**
     * @param b
     * @return
     * @author <a href="mailto:chenyongqiang@gtmap.cn">chenyongqiang</a>
     * @description byte数组转字符串
     */
    public static String byteToStr(byte[] b, String encode) {
        String str = null;
        if (null != b) {
            try {
                str = new String(b, encode);
            } catch (UnsupportedEncodingException e) {
                logger.error("StringUtils:byteToStrUtf8", e);
            }
        }
        return str;
    }

    /**
     * @param b
     * @return
     * @author <a href="mailto:chenyongqiang@gtmap.cn">chenyongqiang</a>
     * @description byte数组转字符串 编码GBK
     */
    public static String byteToStrGbk(byte[] b) {
        return byteToStr(b, ENCODING_GBK);
    }

    /**
     * @param base64Str
     * @return
     * @description base64字符串解密为数组
     */
    public static byte[] decodeBase64StrToByte(String base64Str) {
        byte[] bytes = null;
        if (StringUtils.isNotBlank(base64Str)) {
            bytes = Base64.decodeBase64(base64Str);
        }
        return bytes;
    }

    /**
     * @param bytes
     * @return
     * @author <a href="mailto:chenyongqiang@gtmap.cn">chenyongqiang</a>
     * @description 数组 加密 为base64
     */
    public static String encodeByteToBase64Str(byte[] bytes) {

        String base64Str = "";
        if (null != bytes) {
            base64Str = Base64.encodeBase64String(bytes);
        }
        return base64Str;
    }

    public static final String ENCODING_GBK = "GBK";

    /**
     * @param str
     * @return
     * @author <a href="mailto:chenyongqiang@gtmap.cn">chenyongqiang</a>
     * @description 字符串转byte数组 编码GBK
     */
    public static byte[] strToByteGbk(String str) {
        return strToByte(str, ENCODING_GBK);
    }

    /**
     * @param str
     * @return
     * @author <a href="mailto:chenyongqiang@gtmap.cn">chenyongqiang</a>
     * @description 字符串转byte数组
     */
    public static byte[] strToByte(String str, String encode) {
        byte[] b = null;
        if (StringUtils.isNotBlank(str)) {
            try {
                b = str.getBytes(encode);
            } catch (UnsupportedEncodingException e) {
                logger.error("StringUtils:strToByteUtf8", e);
            }
        }
        return b;
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }

        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*public static String encryptData_CBC(String plainText)
    {
        try
        {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString)
            {
                keyBytes = DataUtil.hexStringToBytes(secretKey);
                ivBytes = DataUtil.hexStringToBytes(iv);
            }
            else
            {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, StringUtil.strToByteGbk(plainText));
            String cipherText = Base64Util.encodeByteToBase64Str(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0)
            {
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        }
        catch (Exception e)
        {
            logger.error("encryptData_CBC", e);
        }
        return null;
    }

    public static String decryptData_CBC(String cipherText)
    {
        try
        {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString)
            {
                keyBytes = DataUtil.hexStringToBytes(secretKey);
                ivBytes = DataUtil.hexStringToBytes(iv);
            }
            else
            {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64Util.decodeBase64StrToByte(cipherText));
            return StringUtil.byteToStrGbk(decrypted);
        } catch (Exception e)
        {
            logger.error("decryptData_CBC", e);
        }

        return null;
    }*/
    public static void main(String[] args) {
        String plainText = "谢谢你";

        System.out.println("ECB模式");
        String cipherText = SM4Util.encryptData_ECB(plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = SM4Util.decryptData_ECB(cipherText);
        System.out.println("明文: " + plainText);
        System.out.println("");

        /*System.out.println("CBC模式");
        String cipherText2 = SM4Util.encryptData_CBC(plainText);
        System.out.println("密文: " + cipherText2);
        System.out.println("");

        plainText = SM4Util.decryptData_CBC(cipherText2);
        System.out.println("明文: " + plainText);*/
    }
}
