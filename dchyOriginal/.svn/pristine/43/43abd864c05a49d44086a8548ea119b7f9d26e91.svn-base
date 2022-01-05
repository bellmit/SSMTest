package cn.gtmap.onemap.platform.utils;

import com.vividsolutions.jts.geom.*;
import org.geotools.geometry.jts.JTSFactoryFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 坐标转换通用类
 * 四参数转换
 * 七参数转换
 * Author:  yingxiufeng
 * Project: onemap-parent
 * Date:    2015/1/21 19:30
 * File:    CoordinateConversion
 * (c) Copyright gtmap Corp.2015
 */
public final class CoordinateConversion {


    public static final int bj54ToXian80 = 0;

    public static final int nt94ToXian80 = 1;

    public static final int xian80ToNt94 = 2;

    private static List<Point> ptsNeedConvert = new ArrayList<Point>();

    private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    /**
     * convert  x y
     *
     * @param type
     * @return
     */
    public double[] convert(int type) {
        List<Double> coords = new ArrayList<Double>();
        Bj54ToXian80 bj54ToXian = new Bj54ToXian80();
        Nt94ToXian80 nt94ToXian = new Nt94ToXian80();
        Xian80ToNt94 xian80ToNt = new Xian80ToNt94();
        Iterator<Point> iterator = ptsNeedConvert.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            double[] coord;
            switch (type) {
                case bj54ToXian80:
                    coord = bj54ToXian.convert(point.getX(), point.getY());
                    break;
                case nt94ToXian80:
                    coord = nt94ToXian.convert(point.getX(), point.getY());
                    break;
                case xian80ToNt94:
                    coord = xian80ToNt.convert(point.getX(), point.getY());
                    break;
                default:
                    throw new IllegalArgumentException(String.valueOf(type) + " is not supported!");
            }
            coords.add(coord[0]);
            coords.add(coord[1]);
        }
        return org.apache.commons.lang.ArrayUtils.toPrimitive(coords.toArray(new Double[0]));
    }

    /**
     * 自定义四参数 进行转换
     * 一般适用于平面坐标系之间转换
     * @param A
     * @param B
     * @param T
     * @param K
     * @return
     */
    public double[] convert(double A, double B, double T, double K) {
        List<Double> coords = new ArrayList<Double>();
        CustomConversion customConversion = new CustomConversion();
        customConversion.setParams(A, B, T, K);
        Iterator<Point> iterator = ptsNeedConvert.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            double[] coord = customConversion.convert(point.getX(), point.getY());
            coords.add(coord[0]);
            coords.add(coord[1]);
        }
        return org.apache.commons.lang.ArrayUtils.toPrimitive(coords.toArray(new Double[0]));
    }

    /***
     *
     * @param deltaX
     * @param deltaY
     * @param deltaZ
     * @param wx
     * @param wy
     * @param wz
     * @param K
     * @return
     */
    public double[] convert(double deltaX, double deltaY, double deltaZ, double wx, double wy, double wz, double K) {
        List<Double> coords = new ArrayList<Double>();
        CustomConversion customConversion = new CustomConversion();
        customConversion.setParams(deltaX,deltaY,deltaZ,wx,wy,wz,K);
        Iterator<Point> iterator = ptsNeedConvert.iterator();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            Point3D coord = customConversion.transCoord(point.getX(), point.getY(),0);
            coords.add(coord.getX());
            coords.add(coord.getY());
        }
        return org.apache.commons.lang.ArrayUtils.toPrimitive(coords.toArray(new Double[0]));
    }

    /***
     *
     * @param A
     * @param B
     * @param T
     * @param K
     * @return
     */
    public List<Point> convert2Pnt(double A, double B, double T, double K) {
        CustomConversion customConversion = new CustomConversion();
        customConversion.setParams(A, B, T, K);
        Iterator<Point> iterator = ptsNeedConvert.iterator();
        List<Point> points=new ArrayList<Point>();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            points.add(customConversion.convertPnt(point.getX(), point.getY()));
        }
        clearAndGc();
        return points;
    }



    /**
     * 提供对 geometry的坐标转换
     *
     * @param geometry point/linestring/polygon ...
     * @param type     0 1 2
     * @return
     */
    public Geometry convertGeometry(Geometry geometry, int type) {
        ptsNeedConvert.clear();
        double[] coords;
        if (geometry instanceof com.vividsolutions.jts.geom.Point) {
            addPoint(((com.vividsolutions.jts.geom.Point) geometry).getX(), ((com.vividsolutions.jts.geom.Point) geometry).getY());
            coords = convert(type);
            return geometryFactory.createPoint(new Coordinate(coords[0], coords[1]));
        } else if (geometry instanceof MultiPoint) {
            coords = getCoordinates(geometry.getCoordinates());
            addPoints(coords);
            Coordinate[] coordinates = array2Coords(convert(type));
            return geometryFactory.createMultiPoint(coordinates);
        } else if (geometry instanceof LineString) {
            addPoints(getCoordinates(geometry.getCoordinates()));
            return geometryFactory.createLineString(array2Coords(convert(type)));
        } else if (geometry instanceof MultiLineString) {
            MultiLineString multiLineString = (MultiLineString) geometry;
            LineString[] lineStrings = new LineString[multiLineString.getNumGeometries()];
            for (int i = 0; i < lineStrings.length; i++) {
                lineStrings[i] = (LineString) convertGeometry(multiLineString.getGeometryN(i), type);
            }
            return geometryFactory.createMultiLineString(lineStrings);
        } else if (geometry instanceof Polygon) {
            Polygon polygon = (Polygon) geometry;
            LinearRing shell = createLinearRing(convertGeometry(polygon.getExteriorRing(), type).getCoordinates());
            LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
            if (holes.length > 0) {
                for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                    holes[i] = createLinearRing(convertGeometry(polygon.getInteriorRingN(i), type).getCoordinates());
                }
            }
            return geometryFactory.createPolygon(shell, holes);
        } else if (geometry instanceof MultiPolygon) {
            //TODO
        } else if (geometry instanceof GeometryCollection) {
            //TODO
        }
        throw new RuntimeException("current geometry don't supported");
    }

    /***
     * 清除 回收变量
     */
    private static void clearAndGc(){
        ptsNeedConvert.clear();
        ptsNeedConvert=null;
        System.gc();
    }
    /**
     * create linear ring
     *
     * @param coordinates
     * @return
     */
    private static LinearRing createLinearRing(Coordinate[] coordinates) {
        return geometryFactory.createLinearRing(coordinates);
    }

    /**
     * get []
     *
     * @param coordinates
     * @return
     */
    private static double[] getCoordinates(Coordinate[] coordinates) {
        List<Double> coords = new ArrayList<Double>();
        for (Coordinate c : coordinates) {
            coords.add(c.x);
            coords.add(c.y);
        }
        return org.apache.commons.lang.ArrayUtils.toPrimitive(coords.toArray(new Double[0]));
    }

    /**
     * array 2 coords
     *
     * @param coords
     * @return
     */
    private static Coordinate[] array2Coords(double[] coords) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int i = 0; i < coords.length; i += 2) {
            Coordinate coordinate = new Coordinate(coords[i], coords[i + 1]);
            coordinates.add(coordinate);
        }
        return coordinates.toArray(new Coordinate[0]);
    }

    /**
     * add point for conversion
     *
     * @param x
     * @param y
     */
    public void addPoint(double x, double y) {
        ptsNeedConvert.add(new Point(x, y));
    }

    /**
     * add points for conversion
     *
     * @param coords
     */
    public void addPoints(double[] coords) {
        for (int i = 0; i < coords.length; i += 2) {
            Point pnt = new Point(coords[i], coords[i + 1]);
            ptsNeedConvert.add(pnt);
        }
    }

    /**
     * clear pnts
     */
    public void clear() {
        ptsNeedConvert = new ArrayList<Point>();
    }

    /**
     * validate lat long
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    private void validateLatLon(double latitude, double longitude) {
        if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0
                || longitude >= 180.0) {
            throw new IllegalArgumentException(
                    "Legal ranges: latitude [-90,90], longitude [-180,180).");
        }
    }

    /**
     * 度转为弧度
     *
     * @param degree
     * @return
     */
    private double degreeToRadian(double degree) {
        return degree * Math.PI / 180;
    }

    /**
     * 弧度转度
     *
     * @param radian
     * @return
     */
    private double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }

    private double SIN(double value) {
        return Math.sin(value);
    }

    private double COS(double value) {
        return Math.cos(value);
    }

    /**
     * point
     */
    private class Point {
        double x;
        double y;

        public Point(double X, double Y) {
            this.x = X;
            this.y = Y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

    /**
     * point 3d
     */
    private class Point3D {

        double x;
        double y;
        double z;

        public Point3D(double X, double Y, double Z) {
            this.x = X;
            this.y = Y;
            this.z = Z;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }
    }

    /**
     * 北京54转西安80
     */
    private class Bj54ToXian80 {

        double a = -14.959196;

        double b = 39999982.973589;

        double t = -0.0003453875; //弧度

        double k = 0.999990898222958;

        /**
         * convert
         *
         * @param x
         * @param y
         * @return
         */
        public double[] convert(double x, double y) {
            double[] coord = {0.0, 0.0};
            double X;
            double Y;
            X = a + x * k * COS(t) - y * k * SIN(t);
            Y = b + y * k * COS(t) + x * k * SIN(t);
            coord[0] = X;
            coord[1] = Y;
            return coord;
        }
    }

    /**
     * 南通94坐标转为西安80坐标系
     */
    private class Nt94ToXian80 {

        double a = -4373.284079;

        double b = 40127241.54955;

        double t = -0.53067909055555555555555555555556; //度

        double k = 1.0000810804422;

        double xOffset = 3500000;

        double yOffset = 400000;

        /**
         * convert
         *
         * @param x
         * @param y
         * @return
         */
        public double[] convert(double x, double y) {
            double[] coord = {0.0, 0.0};
            double X;
            double Y;
            if (x < xOffset)
                x = x + xOffset;
            if (y < yOffset)
                y = y + yOffset;
            X = a + x * k * COS(degreeToRadian(t)) - y * k * SIN(degreeToRadian(t));
            Y = b + y * k * COS(degreeToRadian(t)) + x * k * SIN(degreeToRadian(t));
            coord[0] = Y;       //注意x y的顺序
            coord[1] = X;
            return coord;
        }
    }

    /**
     * 西安80转为南通94坐标系
     */
    private class Xian80ToNt94 {

        double a = -40522226.3359641;

        double b = -3124000.34095673;

        double t = -0.00926121180978233;

        double k = 0.999876026884487;

        /**
         * convert
         *
         * @return
         */
        public double[] convert(double x, double y) {
            double[] coord = {0.0, 0.0};
            double X;
            double Y;
            X = a + x * k - y * t;
            Y = b + y * k + x * t;
            coord[0] = X;
            coord[1] = Y;
            return coord;
        }
    }

    /**
     * 自定义参数值 以进行转换
     */
    private class CustomConversion {

        /***
         * x方向平移量
         */
        double a = 0;

        /***
         * y方向平移量
         */
        double b = 0;

        /***
         * z方向平移量
         */
        double c=0;

        /***
         * x方向旋转角度
         */
        double xr=0;

        /***
         * y方向旋转角度
         */
        double yr=0;

        /***
         * z方向旋转角度
         */
        double zr=0;

        double t = 0;

        double k = 1;

        /**
         * 设置投影转换四参数
         * 注意：四参数只适用于二维点的投影变换
         * @param x0 x方向平移量，单位米
         * @param y0 y方向平移量，单位米
         * @param T  旋转角度，单位度
         * @param K  尺度因子
         */
        public void setParams(double x0, double y0, double T, double K) {
            a = x0;
            b = y0;
            t = degreeToRadian(T);
            k = K;
        }

        /**
         * 设置投影转换七参数
         * 注意：七参数只适用于三维点的投影变换
         *
         * @param deltaX x方向平移量，单位米
         * @param deltaY y方向平移量，单位米
         * @param deltaZ z方向平移量，单位米
         * @param wx x轴旋转角度，单位度
         * @param wy y轴旋转角度，单位度
         * @param wz z轴旋转角度，单位度
         * @param K  尺度因子 ≈1
         */
        public void setParams(double deltaX, double deltaY, double deltaZ, double wx, double wy, double wz, double K) {
            a=deltaX;
            b=deltaY;
            c=deltaZ;
            xr=wx;
            yr=wy;
            zr=wz;
//            xr=degreeToRadian(wx);
//            yr=degreeToRadian(wy);
//            zr=degreeToRadian(wz);
            k=K;
        }

        /**
         * @param x
         * @param y
         * @return
         */
        public double[] convert(double x, double y) {
            double[] coord = {0.0, 0.0};
            double X;
            double Y;
            X = a + x * k * COS(t) - y * k * SIN(t);
            Y = b + y * k * COS(t) + x * k * SIN(t);
            coord[0] = X;
            coord[1] = Y;
            return coord;
        }

        /***
         *
         * @param x
         * @param y
         * @return
         */
        public Point convertPnt(double x, double y) {
            double X;
            double Y;
            X = a + x * k * COS(t) - y * k * SIN(t);
            Y = b + y * k * COS(t) + x * k * SIN(t);
            return new Point(X,Y);
        }

        /***
         * 七参数转换坐标点 反算
         * @param x
         * @param y
         * @param z
         * @return
         */
        public Point3D transCoord(double x, double y, double z) {
            double X;
            double Y;
            double Z;
            X = a + (1 + k) * x + zr * y - yr * z;
            Y = b + (1 + k) * y - zr * x + xr * z;
            //  X=(x-a+z*yr-y*zr)/(1+k);
//            Y =(y-b-z*xr-x*zr)/(1+k);
//            Z = (1 + k) * (z) + (-y * xr + x * yr) + c;
            return new Point3D(X, Y, 0);
        }
    }

}