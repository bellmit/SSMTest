package cn.gtmap.onemap.platform.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Lenovo on 14-9-9.
 */
public final class ZipUtils {


    /**
     * 压缩文件(.zip)的函数
     *
     * @param zipDirectory:(需要)压缩的文件夹路径
     * @param zipPath:文件压缩后放置的路径,该路径可以为null,null表示压缩到原文件的同级目录
     * @return :返回一个压缩好的文件(File),否则返回null
     */
    public static File doZip(String zipDirectory, String zipPath) {
        File zipDir = new File(zipDirectory);

        if (zipPath == null) {
            zipPath = zipDir.getParent();
        }

        // 压缩后生成的zip文件名
        String zipFileName = zipPath + "\\" + zipDir.getName() + ".zip";

        try {
            ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(
                    new FileOutputStream(zipFileName)));

            // 压缩文件
            handleDir(zipDir, zipDir.getParent().length() + 1, zipOut);

            zipOut.close();
            return new File(zipFileName);
        } catch (IOException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 由doZip调用,递归完成目录文件读取
     *
     * @param dir:(需要)压缩的文件夹(File           类型)
     * @param len:一个参数(记录压缩文件夹的parent路径的长度)
     * @param zipOut:需要压缩进的压缩文件
     * @throws java.io.IOException :如果出错,会抛出IOE异常
     */
    private static void handleDir(File dir, int len, ZipOutputStream zipOut)
            throws IOException {
        File[] files = dir.listFiles();
        int readedBytes;
        byte[] buf;
        if (files != null) {
            if (files.length > 0) { // 如果目录不为空,则分别处理目录和文件.
                for (File fileName : files) {
                    if (fileName.isDirectory()) {
                        handleDir(fileName, len, zipOut);
                    } else {
                        try (FileInputStream fileIn = new FileInputStream(fileName);) {
                            buf = new byte[fileIn.available()];
//                        zipOut.putNextEntry(new ZipEntry(fileName.getPath()
//                                .substring(len+dir.getName().length()).replaceAll("\\\\", "/")));
                            zipOut.putNextEntry(new ZipEntry(fileName.getPath()
                                    .substring(len + dir.getName().length()).replaceAll("\\\\", "")));
                            while ((readedBytes = fileIn.read(buf)) > 0) {
                                zipOut.write(buf, 0, readedBytes);
                            }
                            zipOut.closeEntry();
                        }
                    }
                }
            } else { // 如果目录为空,则单独创建之.
                zipOut.putNextEntry(new ZipEntry(dir.getPath().substring(len)
                        + "/"));
                zipOut.closeEntry();
            }
        } else {// 如果是一个单独的文件
            try (FileInputStream fileIn = new FileInputStream(dir);) {
                buf = new byte[fileIn.available()];
                zipOut.putNextEntry(new ZipEntry(dir.getPath().substring(len)));
                while ((readedBytes = fileIn.read(buf)) > 0) {
                    zipOut.write(buf, 0, readedBytes);
                }
                zipOut.closeEntry();
            }
        }
    }
}
