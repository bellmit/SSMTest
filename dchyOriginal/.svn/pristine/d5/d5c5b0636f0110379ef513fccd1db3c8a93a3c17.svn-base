package cn.gtmap.onemap.server;

import com.sixlegs.png.PngImage;
import com.vividsolutions.jts.geom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2017/5/18
 * Version: v1.0 (c) Copyright gtmap Corp.2017
 */
public final class ImgUtils {

    private static Logger logger = LoggerFactory.getLogger(ImgUtils.class);

    private static int BLANK_RGB = 16448250;

    /***
     * 判断该网格是否在rect范围内
     * @param rect
     * @param level
     * @param row
     * @param col
     * @return
     */
    public static boolean inRect(String[] rect, int level, int row, int col) {
        //计算请求图片坐标
        double xmin = PixelToLng(col * 256, level);
        double xmax = PixelToLng(col * 256 + 256, level);
        double ymin = PixelToLat(row * 256 + 256, level);
        double ymax = PixelToLat(row * 256, level);
        //rect的坐标
        double rectXmin = Double.parseDouble(rect[0]);
        double rectYmin = Double.parseDouble(rect[1]);
        double rectXmax = Double.parseDouble(rect[2]);
        double rectYmax = Double.parseDouble(rect[3]);
        if (xmin >= rectXmin && xmin <= rectXmax && ymin >= rectYmin && ymin <= rectYmax) {
            return true;
        } else if (xmax >= rectXmin && xmax <= rectXmax && ymin >= rectYmin && ymin <= rectYmax) {
            return true;
        } else if (xmax >= rectXmin && xmax <= rectXmax && ymax >= rectYmin && ymax <= rectYmax) {
            return true;
        } else if (xmin >= rectXmin && xmin <= rectXmax && ymax >= rectYmin && ymax <= rectYmax) {
            return true;
        }
        return false;
    }


    /**
     * 根据底图进行融合
     *
     * @param imageGuo
     * @param imageJiangsu
     * @param dituJiangsu
     * @return
     */
    public static byte[] fuseMultiImage(byte[] imageGuo, byte[] imageJiangsu, byte[] dituJiangsu) {
        try {
            long t1 = System.currentTimeMillis();
            PngImage pi = new PngImage();
            BufferedImage bImageGuo = pi.read(new ByteArrayInputStream(imageGuo), false);
            BufferedImage bImageJiangsu = pi.read(new ByteArrayInputStream(imageJiangsu), false);
            BufferedImage bImageDitu = pi.read(new ByteArrayInputStream(dituJiangsu), false);
            for (int x = 0; x < 256; x++) {
                for (int y = 0; y < 256; y++) {
                    int rgbBaseMap = bImageDitu.getRGB(x, y);
                    if (rgbBaseMap == BLANK_RGB) {
                        int rgbGuo = bImageGuo.getRGB(x, y);
                        if (bImageJiangsu.getRGB(x, y) == BLANK_RGB)
                            bImageJiangsu.setRGB(x, y, rgbGuo);
                    }
                }
            }
            long t2 = System.currentTimeMillis();
            logger.debug("注记融合耗时：" + (t2 - t1));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bImageJiangsu, "png", outputStream);// 写图片
            byte[] result = outputStream.toByteArray();
            outputStream.close();
            return result;
        } catch (Exception ex) {
            logger.error("注记融合失败！");
        }
        return imageJiangsu;
    }

    /***
     * 融合多个图片
     * @param images
     * @param width
     * @param height
     * @return
     */
    public static byte[] fuseMultiImage(List<byte[]> images, int width, int height) {
        if (images.size() == 1) {
            return images.get(0);
        } else if (images.size() == 0) {
            return null;
        } else {
            try {
                BufferedImage imageOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = (Graphics2D) imageOut.getGraphics();
                for (int i = 0; i < images.size(); i++) {
                    PngImage pi = new PngImage();
                    BufferedImage png = pi.read(new ByteArrayInputStream(images.get(i)), false);
                    g2d.drawImage(png, 0, 0, width, height, null);
                }
                g2d.dispose();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(imageOut, "png", outputStream);// 写图片
                byte[] result = outputStream.toByteArray();
                outputStream.close();
                return result;
            } catch (Exception ex) {
                logger.error("合并图片失败！");
                return images.get(0);
            }

        }
    }

    /***
     * 融合地图服务图片(静态切片和动态服务)
     * @param shps
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static byte[] fuseServiceImage(Map<String, Object> shps, byte[] image, int width, int height) {
        Geometry tileGeo = (Geometry) shps.get("box");
        Geometry clipGeo = (Geometry) shps.get("geo");
        try {
            PngImage pi = new PngImage();
            BufferedImage png = pi.read(new ByteArrayInputStream(image), false);
            width = png.getWidth();
            height= png.getHeight();
            BufferedImage imageIn = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d1 = imageIn.createGraphics();
            g2d1.setBackground(Color.white);
            g2d1.drawImage(png, 0, 0, width, height, null);
            g2d1.dispose();
            //拆分着色
            BufferedImage imageOut = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) imageOut.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            double xMin = tileGeo.getEnvelopeInternal().getMinX();
            double xMax = tileGeo.getEnvelopeInternal().getMaxX();
            double yMin = tileGeo.getEnvelopeInternal().getMinY();
            double yMax = tileGeo.getEnvelopeInternal().getMaxY();
            double unit = (xMax - xMin) / width;

            if (clipGeo instanceof MultiPolygon) {
                MultiPolygon multiClipGeo = (MultiPolygon) clipGeo;
                for (int i = 0; i < multiClipGeo.getNumGeometries(); i++) {
                    Geometry geoChild = multiClipGeo.getGeometryN(i);
                    if (geoChild instanceof com.vividsolutions.jts.geom.Polygon) {
                        //第一个为外环
                        LineString lineRing = ((com.vividsolutions.jts.geom.Polygon) geoChild).getExteriorRing();
                        Coordinate[] coords1 = lineRing.getCoordinates();
                        int[] xPoints1 = new int[coords1.length];
                        int[] yPoints1 = new int[coords1.length];
                        for (int m = 0; m < coords1.length; m++) {
                            Coordinate coordinate1 = coords1[m];
                            xPoints1[m] = (int) ((coordinate1.x - xMin) / unit) == width - 1 ? width : (int) ((coordinate1.x - xMin) / unit);
                            yPoints1[m] = (int) (Math.abs(coordinate1.y - yMax) / unit) == height - 1 ? height : (int) (Math.abs(coordinate1.y - yMax) / unit);
                        }
                        g2d.setColor(Color.red);
                        g2d.drawPolygon(xPoints1, yPoints1, xPoints1.length);
                        g2d.fillPolygon(xPoints1, yPoints1, xPoints1.length);
                        int ringNum = ((com.vividsolutions.jts.geom.Polygon) geoChild).getNumInteriorRing();
                        if (ringNum > 0) {
                            for (int n = 0; n < ringNum; n++) {
                                Geometry geo2 = ((com.vividsolutions.jts.geom.Polygon) geoChild).getInteriorRingN(n);
                                //Geometry geo2 = geoChild.getGeometryN(n);
                                if (geo2 instanceof LineString) {
                                    Coordinate[] coords = geo2.getCoordinates();
                                    int[] xPoints = new int[coords.length];
                                    int[] yPoints = new int[coords.length];
                                    for (int m = 0; m < coords.length; m++) {
                                        Coordinate coordinate = coords[m];
                                        xPoints[m] = (int) ((coordinate.x - xMin) / unit) == width - 1 ? width : (int) ((coordinate.x - xMin) / unit);
                                        yPoints[m] = (int) (Math.abs(coordinate.y - yMax) / unit) == height - 1 ? height : (int) (Math.abs(coordinate.y - yMax) / unit);
                                    }
                                    g2d.setColor(Color.red);
                                    g2d.drawPolygon(xPoints, yPoints, xPoints.length);
                                    g2d.fillPolygon(xPoints, yPoints, xPoints.length);
                                }
                            }
                        } else {
                            if (i == 0) {
                                Coordinate[] coords = geoChild.getCoordinates();
                                int[] xPoints = new int[coords.length - 1];
                                int[] yPoints = new int[coords.length - 1];
                                for (int m = 0; m < coords.length - 1; m++) {
                                    Coordinate coordinate = coords[m];
                                    xPoints[m] = (int) ((coordinate.x - xMin) / unit) == width - 1 ? width - 1 : (int) ((coordinate.x - xMin) / unit);
                                    yPoints[m] = (int) (Math.abs(coordinate.y - yMax) / unit) == height - 1 ? height - 1 : (int) (Math.abs(coordinate.y - yMax) / unit);
                                }
                                g2d.setColor(Color.blue);
                                g2d.drawPolygon(xPoints, yPoints, xPoints.length);
                                g2d.fillPolygon(xPoints, yPoints, xPoints.length);
                            }
                        }
                    } else {
                        logger.warn("not polygon!!!");
                    }
                }
            } else {
                Coordinate[] coords = clipGeo.getCoordinates();
                //最后一个是闭合的点，需要排除掉
                int[] xPoints = new int[coords.length - 1];
                int[] yPoints = new int[coords.length - 1];
                for (int m = 0; m < coords.length - 1; m++) {
                    Coordinate coordinate = coords[m];
                    xPoints[m] = (int) ((coordinate.x - xMin) / unit) == width - 1 ? width : (int) ((coordinate.x - xMin) / unit);
                    yPoints[m] = (int) (Math.abs(coordinate.y - yMax) / unit) == height - 1 ? height : (int) (Math.abs(coordinate.y - yMax) / unit);
                }
                //用 红色标记出这个裁剪的区域
                g2d.setColor(Color.red);
                g2d.drawPolygon(xPoints, yPoints, xPoints.length);
                g2d.fillPolygon(xPoints, yPoints, xPoints.length);
            }
            g2d.dispose();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int isRed = imageOut.getRGB(x, y);
                    //将imageIn的不在红色区域内的设为0
                    if (isRed != -65536) {
                        imageIn.setRGB(x, y, 0);
                    }
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(imageIn, "png", out);// 写图片
            byte[] result = out.toByteArray();
            out.close();
            return result;
        } catch (Exception ex) {
            logger.error("bound fuse fail！[{}]",ex.getLocalizedMessage());
            return image;
        }
    }

    /**
     * 将像素转换为经度
     *
     * @param xPixel
     * @param zoom
     * @return
     */
    private static double PixelToLng(int xPixel, int zoom) {
        double lng = xPixel / Math.pow(2, zoom) / 256L * 360L;
        lng = lng - 180;
        return lng;
    }

    /**
     * 将像素转换维度
     *
     * @param pixelY
     * @param zoom
     * @return
     */
    private static double PixelToLat(double pixelY, int zoom) {
        double lat = pixelY / Math.pow(2, zoom) / 256L * 360L;
        return Math.abs(90 - lat);
    }

    /**
     * 判断图片是否完全为空
     *
     * @param image
     * @return
     */
    public static boolean isBlank(byte[] image) {
        return image.length < 800;
    }

    /**
     * 图片是否部分为空
     * 通过在边界采样
     *
     * @param image
     * @return
     */
    public static boolean isPartialBlank(byte[] image) {
        try {
            PngImage pi = new PngImage();
            BufferedImage bufferedImage = pi.read(new ByteArrayInputStream(image), false);
            int x = 0, y = 0, step = 2;
            for (; x < 256; x = x + step) {
                int rgb = bufferedImage.getRGB(x, 0);
                if (rgb == BLANK_RGB) {
                    rgb = bufferedImage.getRGB(x, 2);
                    if (rgb == BLANK_RGB) {
                        return true;
                    }
                }
                rgb = bufferedImage.getRGB(x, 255);
                if (rgb == BLANK_RGB) {
                    rgb = bufferedImage.getRGB(x, 253);
                    if (rgb == BLANK_RGB) {
                        return true;
                    }
                }
            }
            while (y < 256) {
                int rgb = bufferedImage.getRGB(0, y);
                if (rgb == BLANK_RGB) {
                    rgb = bufferedImage.getRGB(2, y);
                    if (rgb == BLANK_RGB) {
                        return true;
                    }
                }
                rgb = bufferedImage.getRGB(255, y);
                if (rgb == BLANK_RGB) {
                    rgb = bufferedImage.getRGB(253, y);
                    if (rgb == BLANK_RGB) {
                        return true;
                    }
                }
                y = y + step;
            }
        } catch (Exception ex) {
            logger.error("判断图片部分为空异常[{}]",ex.getLocalizedMessage());
        }
        return false;
    }
}
