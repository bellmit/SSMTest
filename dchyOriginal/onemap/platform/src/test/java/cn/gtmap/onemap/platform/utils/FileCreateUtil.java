package cn.gtmap.onemap.platform.utils;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @作者 王建明
 * @创建日期 Feb 4, 2010
 * @创建时间 9:56:15 AM
 * @版本号 V 1.0
 */
public class FileCreateUtil {
	private static final String CONTENT_TYPE_IMAGE = "image/jpeg";

	/**
	 * @说明 将二进制字节流保存为文件
	 * @param byteArry 二进制流
	 * @param file 要保存的文件
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void saveByteArry2File(byte[] byteArry, File file)
			throws SQLException, IOException {
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		bos.write(byteArry);
		bos.close();
	}

	/**
	 * @说明 将文件转换为二进制字节流
	 * @param file 要转换的文件
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static byte[] changeFile2Bytes(File file) throws SQLException,
			IOException {
		long len = file.length();
		byte[] bytes = new byte[(int) len];
		FileInputStream inputStream = new FileInputStream(file);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				inputStream);
		int r = bufferedInputStream.read(bytes);
		if (r != len) {
			throw new IOException("File read error");
		}
		inputStream.close();
		bufferedInputStream.close();
		return bytes;
	}

	/**
	 * @说明 将Blob类类型的文件转换成二进制字节流
	 * @param pic
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static byte[] changeBlob2Bytes(Blob pic) throws SQLException,
			IOException {
		byte[] bytes = pic.getBytes(1, (int) pic.length());

		return bytes;
	}

	/**
	 * @说明 将Blob类类型的数据保存为本地文件
	 * @param blob
	 * @param file
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void saveBlob2File(Blob blob, File file) throws SQLException,
			IOException {
		InputStream is = blob.getBinaryStream();
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		int b = -1;
		while ((b = is.read()) != -1) {
			bos.write(b);
		}
		bos.close();
		is.close();
	}

	/**
	 * @说明 将一个文件拷贝到另一个文件中
	 * @param oldFile源文件
	 * @param newFile目标文件
	 */
	public static void saveFileToFile(File oldFile, File newFile) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(oldFile); // 建立文件输入流

			fos = new FileOutputStream(newFile);

			int r;
			while ((r = fis.read()) != -1) {
				fos.write((byte) r);
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Source File not found");
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (fos != null)
					fos.close();
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
	}

	/**
	 * @说明 获取文本形式文件的内容
	 * @param file要读取的文件
	 * @return
	 */
	public static String getTxtFileContent(File file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder sb = new StringBuilder((int) file.length());
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Read file error");
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}


	public static String getFileContentUTF8(File file) {
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			StringBuilder sb = new StringBuilder((int) file.length());
			is = new FileInputStream(file);
			isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
			isr.close();
			is.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (isr != null)
					isr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * @作者 王建明
	 * @创建日期 2017/11/18 0018
	 * @创建时间 下午 7:34
	 * @描述 —— 删除文件夹及其下所有文件
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			//递归删除目录中的子目录下
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * @说明 把一个文件转化为字节
	 * @param file
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getByteFromFile(File file) throws Exception {
		byte[] bytes = null;
		if (file != null) {
			InputStream is = new FileInputStream(file);
			int length = (int) file.length();
			if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
			{
				System.out.println("this file is max ");
				return null;
			}
			bytes = new byte[length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			// 如果得到的字节长度和file实际的长度不一致就可能出错了
			if (offset < bytes.length) {
				System.out.println("file length is error");
				return null;
			}
			is.close();
		}
		return bytes;
	}

	/**
	 * @说明 将指定文本内容写入到指定文件中(原文件将被覆盖！)
	 * @param content要写入的内容
	 * @param file写到的文件
	 */
	public static void writeContentIntoFile(String content, File file) {
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();

			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter fw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
			fos.close();
		} catch (IOException e) {
			System.out.println("Write file error");
			e.printStackTrace();
		}
	}

	/**
	 * @param file
	 * @param encode
	 * @return
	 * @throws Exception
	 * T：2012-03-01 11:12:51
	 * A：王建明
	 * X：问题ID——
	 * R：备注——读取文件时设置txt文件的编码方式
	 */
	public static String readFileContent(File file,String encode) throws Exception {
		StringBuilder sb = new StringBuilder();
		if (file.isFile() && file.exists()) {
			InputStreamReader insReader = new InputStreamReader(
					new FileInputStream(file), encode);

			BufferedReader bufReader = new BufferedReader(insReader);

			String line = new String();
			while ((line = bufReader.readLine()) != null) {
				// System.out.println(line);
				sb.append(line + "\n");
			}
			bufReader.close();
			insReader.close();
		}
		return sb.toString();
	}

	/**
	 * @param file
	 * @return
	 * T：2012-03-01 11:12:25
	 * A：王建明
	 * X：问题ID——
	 * R：备注——获取txt文件的编码格式
	 */
	public static String getTxtEncode(File file) {
		String code = "";
		try {
			InputStream inputStream = new FileInputStream(file);
			byte[] head = new byte[3];
			inputStream.read(head);

			code = "gb2312";
			if (head[0] == -1 && head[1] == -2)
				code = "UTF-16";
			if (head[0] == -2 && head[1] == -1)
				code = "Unicode";
			if ((head[0] == -17 && head[1] == -69 && head[2] == -65))
				code = "UTF-8";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * @说明 获取文件后缀名
	 * @param file
	 * @return
	 */
	public static String getFileExtension(File file) {
		if (file != null && file.isFile()) {
			String filename = file.getName();
			int i = filename.lastIndexOf(".");
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return "";
	}

	/**
	 * @说明 删除文件或文件夹
	 * @param file
	 * @作者 王建明
	 * @创建日期 2012-5-26
	 * @创建时间 上午09:36:58
	 * @描述 ——
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			System.out.println("所删除的文件不存在！" + '\n');
		}
	}

	/**
	 * @说明 创建文件夹，如果不存在的话
	 * @param dirPath
	 * @作者 王建明
	 * @创建日期 2012-5-26
	 * @创建时间 上午09:49:12
	 * @描述 ——
	 */
	public static void createMirs(String dirPath) {
		File dirPathFile = new File(dirPath);
		if (!dirPathFile.exists())
			dirPathFile.mkdirs();
	}
}
