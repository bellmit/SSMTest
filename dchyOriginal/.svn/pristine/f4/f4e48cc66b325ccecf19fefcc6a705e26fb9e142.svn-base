package cn.gtmap.onemap.platform.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONArray;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-1-22 上午9:21
 */
public final class FilesUtils {

    public static final String IMAGE_PNG = "png";
    public static final String IMAGE_JPG = "jpg";
    public static final String IMAGE_BMP = "bmp";
    public static final String IMAGE_GIF = "gif";
    public static final String IMAGE_TIF = "tif";
    protected static final Logger logger = LoggerFactory.getLogger(FilesUtils.class);

    static List<String> images = new ArrayList<String>();
    
    

    static {
        images.add(IMAGE_PNG);
        images.add(IMAGE_JPG);
        images.add(IMAGE_BMP);
        images.add(IMAGE_GIF);
        images.add(IMAGE_TIF);
    }

    private static final String THUMB_SUFFIX = "_thumb";//压缩图后缀
    
    /**
     * 判别文件是否为图片
     *
     * @param file
     * @return
     */
    public static final boolean isImage(File file) {
        Assert.notNull(file, "文件不可为空");
        String suffix = file.getPath().substring(file.getPath().lastIndexOf(".") + 1, file.getPath().length());
        if (StringUtils.isBlank(suffix)) return false;
        if (images.contains(suffix.toLowerCase())) return true;
        return false;
    }

    /**
     * 删除文件或者文件夹
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static final boolean delFileOrDirectory(String path) throws IOException {
        boolean result = false;
        try {
            File file = new File(path);
            if (file.exists()) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        files[i].delete();
                    }
                    result = file.delete();
                    return result;
                } else {
                    result = file.delete();
                    return result;
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /***
     * 由byte数组生成file
     * @param bytes
     * @param filePath   生成文件的文件目录
     * @param fileName   生成的文件名
     */
    public static void generateFile(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    /**
	 * @功能 下载临时素材接口
	 * @param filePath
	 *            文件将要保存的目录
	 * @param method
	 *            请求方法，包括POST和GET
	 * @param url
	 *            请求的路径
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static File saveFileByUrl(String url, String filePath, String method,String fileName) {
		// 创建不同的文件夹目录
		File file = new File(filePath);
		// 判断文件夹是否存在
		if (!file.exists()) {
			// 如果文件夹不存在，则创建新的的文件夹
			file.mkdirs();
		}else{
			File fileTemp = new File(filePath +"//"+ fileName);
			if(fileTemp.exists()){
				//fileTemp.delete();
				return null;
			}
		}
		
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		BufferedOutputStream bos = null;
		try {
			// 建立链接
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			// 以Post方式提交表单，默认get方式
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// post方式不能使用缓存
			conn.setUseCaches(false);
			// 连接指定的资源
			conn.connect();
			// 获取网络输入流
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			// 判断文件的保存路径后面是否以/结尾
			if (!filePath.endsWith("/")) {
				filePath += "/";
			}
			// 写入到文件（注意文件保存路径的后面一定要加上文件的名称）
			filePath = filePath + fileName;
			fileOut = new FileOutputStream(filePath);
			bos = new BufferedOutputStream(fileOut);
			byte[] buf = new byte[4096];
			int length = bis.read(buf);
			// 保存文件
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
			bos.close();
			bis.close();
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileOut != null){
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new File(filePath);
	}
	
	/**
	 * 描述：生成带标注的图片
	 * @author 卜祥东
	 * 2019年4月26日 下午3:47:21
	 * @param oldImg 原图片
	 * @param diffXyJsonStr 差异坐标串 (JsonStr)
	 * @param generateFile 生成文件（空文件）
	 */
	public static File generateMarkImg(File oldImg,String diffXyJsonStr,File generateFile) {
		FileOutputStream out = null;
		try {
			if(!oldImg.isFile()){
				logger.error("replaceImgByMarkImg mothed file is not found!");
				return null;
			}
			if(StringUtils.isBlank(diffXyJsonStr)){
    			return null;
    		}
			List<HashMap> diffXyJsonStrList = JSONArray.parseArray(diffXyJsonStr, HashMap.class);
    		if(CollectionUtils.isEmpty(diffXyJsonStrList)){
    			return null;
    		}
			List<Double> xmaxArr = new ArrayList<Double>();//占图片x轴最大值
    		List<Double> xminArr = new ArrayList<Double>();//占图片x轴最小值
    		List<Double> ymaxArr = new ArrayList<Double>();//占图片y轴最大值
    		List<Double> yminArr = new ArrayList<Double>();//占图片y轴最小值
    		for (HashMap hashMap : diffXyJsonStrList) {
    			xmaxArr.add(MapUtils.getDouble(hashMap,"xmax"));
    			xminArr.add(MapUtils.getDouble(hashMap,"xmin"));
    			ymaxArr.add(MapUtils.getDouble(hashMap,"ymax"));
    			yminArr.add(MapUtils.getDouble(hashMap,"ymin"));
			}
			if(CollectionUtils.isEmpty(xmaxArr) || CollectionUtils.isEmpty(xminArr) || CollectionUtils.isEmpty(ymaxArr) || CollectionUtils.isEmpty(yminArr)){
				logger.error("xMax="+xmaxArr+",xMin="+xminArr+",yMax="+ymaxArr+",yMin="+yminArr+" maybe is null!");
				return null;
			}
			BufferedImage image = ImageIO.read(oldImg);
			Double imgWidth = Double.valueOf(image.getWidth());//图片宽度
			Double imgHeight = Double.valueOf(image.getHeight());//图片高度
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.RED);//画笔颜色
			g2d.setStroke(new BasicStroke(8.0f)); //线条加粗（像素）
			for (int i = 0; i < xmaxArr.size(); i++) {
				Double xMax = xmaxArr.get(i);
				Double xMin = xminArr.get(i);
				Double yMax = ymaxArr.get(i);
				Double yMin = yminArr.get(i);
				//标注的矩形框(原点x坐标，原点y坐标，矩形的长(宽)，矩形的宽（高）)
				Double rectangleXPoint = imgWidth.doubleValue()*xMin;
				Double rectangleYPoint = imgHeight.doubleValue()*yMin;
				Double rectangleWidth = imgWidth.doubleValue()*Double.valueOf(NumberFormateUtil.toNumber(xMax-xMin, 4));
				Double rectangleHeight = imgHeight.doubleValue()*Double.valueOf(NumberFormateUtil.toNumber(yMax-yMin, 4));
				//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
				g2d.drawRect(rectangleXPoint.intValue(), rectangleYPoint.intValue(), rectangleWidth.intValue(), rectangleHeight.intValue());
			}
			out = new FileOutputStream(generateFile);//输出图片的地址
			ImageIO.write(image, "jpeg", out);
			logger.info("生成标注图片成功！图片地址-->"+generateFile.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}
		return generateFile;
	}
	
	/**
	 * 描述：生成带标注的图片（瞭望神州）
	 * @author 卜祥东
	 * 2019年7月17日 下午3:47:21
	 * @param oldImg 原图片
	 * @param diffXyJsonStr 差异坐标串 (JsonStr)
	 * @param generateFile 生成文件（空文件）
	 */
	public static File generateMarkImgByLwsz(File oldImg,List<RecognitionResultSubModel> recognitionResultList,File generateFile) {
		FileOutputStream out = null;
		try {
			if(!oldImg.isFile()){
				logger.error("oldImg 无法找到文件!path="+oldImg.getAbsolutePath());
				return null;
			}
			if(CollectionUtils.isEmpty(recognitionResultList)){
				logger.error("recognitionResultList 参数不能为空!");
				return null;
			}
			BufferedImage image = ImageIO.read(oldImg);
			Double imgWidth = Double.valueOf(image.getWidth());//图片宽度
			Double imgHeight = Double.valueOf(image.getHeight());//图片高度
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.RED);//画笔颜色
			g2d.setStroke(new BasicStroke(8.0f)); //线条加粗（像素）
			for (RecognitionResultSubModel result : recognitionResultList) {
				Double xMax = result.getXmax().doubleValue();
				Double xMin = result.getXmin().doubleValue();
				Double yMax = result.getYmax().doubleValue();
				Double yMin = result.getYmin().doubleValue();
				//标注的矩形框(原点x坐标，原点y坐标，矩形的长(宽)，矩形的宽（高）)
				Double rectangleXPoint = imgWidth.doubleValue()*xMin;
				Double rectangleYPoint = imgHeight.doubleValue()*yMin;
				Double rectangleWidth = imgWidth.doubleValue()*Double.valueOf(NumberFormateUtil.toNumber(xMax-xMin, 4));
				Double rectangleHeight = imgHeight.doubleValue()*Double.valueOf(NumberFormateUtil.toNumber(yMax-yMin, 4));
				//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
				g2d.drawRect(rectangleXPoint.intValue(), rectangleYPoint.intValue(), rectangleWidth.intValue(), rectangleHeight.intValue());
			}
			out = new FileOutputStream(generateFile);//输出图片的地址
			ImageIO.write(image, "jpeg", out);
			logger.info("生成标注图片成功！图片地址-->"+generateFile.getAbsoluteFile());
		} catch (FileNotFoundException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}
		return generateFile;
	}
	
	/**
	 * 描述：生成带标注的图片(替换原有图片)
	 * @author 卜祥东
	 * 2019年4月26日 下午3:47:21
	 * @param oldNodeId 原图片id
	 * @param diffXyJsonStr 差异坐标串 (JsonStr)
	 */
	public static boolean replaceImgByMarkImg(File oldImg,String diffXyJsonStr) {
		boolean result = false;
		FileOutputStream out = null;
		try {
			if(!oldImg.isFile()){
				logger.error("replaceImgByMarkImg mothed file is not found!");
				return result;
			}
			if(StringUtils.isBlank(diffXyJsonStr)){
				return result;
			}
			List<HashMap> diffXyJsonStrList = JSONArray.parseArray(diffXyJsonStr, HashMap.class);
			if(CollectionUtils.isEmpty(diffXyJsonStrList)){
				return result;
			}
			List<Double> xmaxArr = new ArrayList<Double>();//占图片x轴最大值
			List<Double> xminArr = new ArrayList<Double>();//占图片x轴最小值
			List<Double> ymaxArr = new ArrayList<Double>();//占图片y轴最大值
			List<Double> yminArr = new ArrayList<Double>();//占图片y轴最小值
			for (HashMap hashMap : diffXyJsonStrList) {
				xmaxArr.add(MapUtils.getDouble(hashMap,"xmax"));
				xminArr.add(MapUtils.getDouble(hashMap,"xmin"));
				ymaxArr.add(MapUtils.getDouble(hashMap,"ymax"));
				yminArr.add(MapUtils.getDouble(hashMap,"ymin"));
			}
			if(CollectionUtils.isEmpty(xmaxArr) || CollectionUtils.isEmpty(xminArr) || CollectionUtils.isEmpty(ymaxArr) || CollectionUtils.isEmpty(yminArr)){
				logger.error("xMax="+xmaxArr+",xMin="+xminArr+",yMax="+ymaxArr+",yMin="+yminArr+" maybe is null!");
				return result;
			}
			String oldImgPath = oldImg.getAbsolutePath();
			String oldImgThumbPath = oldImgPath.substring(0,oldImgPath.indexOf(".")).concat(THUMB_SUFFIX).concat(".").concat(IMAGE_JPG);
			File oldImgThumb = new File(oldImgThumbPath);
			List<File> fileList = new ArrayList<File>();
			fileList.add(oldImg);//原图
			fileList.add(oldImgThumb);//缩略图
			
			//替换原有图片和缩略图
			for (File file : fileList) {
				String filePath = file.getAbsolutePath();
				BufferedImage image = ImageIO.read(file);
				Double imgWidth = Double.valueOf(image.getWidth());//图片宽度
				Double imgHeight = Double.valueOf(image.getHeight());//图片高度
				Graphics2D g2d = image.createGraphics();
				g2d.setColor(Color.RED);//画笔颜色
				if(filePath.indexOf(THUMB_SUFFIX)>-1){
					g2d.setStroke(new BasicStroke(2.0f)); //线条加粗（像素）压缩图
				}else{
					g2d.setStroke(new BasicStroke(8.0f)); //线条加粗（像素）
				}
				for (int i = 0; i < xmaxArr.size(); i++) {
					Double xMax = xmaxArr.get(i);
					Double xMin = xminArr.get(i);
					Double yMax = ymaxArr.get(i);
					Double yMin = yminArr.get(i);
					//标注的矩形框(原点x坐标，原点y坐标，矩形的长(宽)，矩形的宽（高）)
					Double rectangleXPoint = imgWidth.doubleValue()*xMin;
					Double rectangleYPoint = imgHeight.doubleValue()*yMin;
					Double rectangleWidth = imgWidth.doubleValue()*Double.valueOf(NumberFormateUtil.toNumber(xMax-xMin, 4));
					Double rectangleHeight = imgHeight.doubleValue()*Double.valueOf(NumberFormateUtil.toNumber(yMax-yMin, 4));
					//矩形框(原点x坐标，原点y坐标，矩形的长，矩形的宽)
					g2d.drawRect(rectangleXPoint.intValue(), rectangleYPoint.intValue(), rectangleWidth.intValue(), rectangleHeight.intValue());
				}
				out = new FileOutputStream(file.getAbsoluteFile());//输出图片的地址
				ImageIO.write(image, "jpeg", out);
				result = true;
				logger.info("生成标注图片成功！图片地址-->"+file.getAbsoluteFile());
			}
		} catch (FileNotFoundException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}
		return result;
	}
	
}
