package cn.gtmap.msurveyplat.promanage.web.utils;


import cn.gtmap.msurveyplat.common.util.SM4Util;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/1/7
 * @description TODO
 */
public class UZipFile {

    /**
     * @param zipFile
     * @return
     * @description 2021/1/8
     * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
     */
    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        String gcbh = zipFile.getName();

        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");

            System.out.println("******************目录********************" + outPath);

            in.close();
        }
        System.out.println("******************解压完毕********************");
    }

    public static void main(String[] args) {

        String ss = "ke41Qj9/l75qO8HIkWvsGg==";
        String sss = SM4Util.decryptData_ECB(ss);
        System.out.println(sss);
    }

//    public static void main(String[] args) {
//        int[] random = new int[10];
//        for (int i = 0; i < 10; i++) {
//            int x = (int) (Math.random() * 100 + 1);
//            for (int j = 0; j < 10; j++) {
//                while (x == random[j]) {
//                    x = (int) (Math.random() * 100 + 1);
//                }
//            }
//            random[i] = x;
//        }
//        for (int i = 0; i < 10; i++) {
//            System.out.println(random[i] + "  ");
//        }
//    }

    /**
     * @Description: 图片转base64编码格式
     * @Auther: pss
     * @Date: 2019/9/6 19:39
     */
    public static String readImage(String path) {
        byte[] fileByte = null;
        try {
//            File file = new File("C:\\Users\\gt\\Pictures\\work\\1\\" + path);
//            File file = new File("C:\\Users\\gt\\Pictures\\work\\2\\" + path);
            File file = new File("C:\\Users\\gt\\Pictures\\work\\3\\" + path);
            fileByte = Files.readAllBytes(file.toPath());
            System.out.println("data:image/png;base64," + Base64.encodeBase64String(fileByte));
        } catch (IOException e) {
//            e.printStackTrace();
//            logger.error("错误原因{}：", e);
        }
        return "data:image/png;base64," + Base64.encodeBase64String(fileByte);
    }
}
