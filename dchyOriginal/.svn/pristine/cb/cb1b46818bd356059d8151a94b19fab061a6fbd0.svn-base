package cn.gtmap.onemap.platform.utils;

import com.gtis.config.AppConfig;

import java.sql.*;

public final class DBUtils {
    //数据库连接对象
    private static Connection conn = null;

    private static String driver = AppConfig.getProperty("oms.db.driver"); //驱动

    private static String url = AppConfig.getProperty("oms.db.url"); //连接字符串

    private static String username = AppConfig.getProperty("oms.db.username"); //用户名

    private static String password = AppConfig.getProperty("oms.db.password"); //密码

    // 获得连接对象
    public static final Connection getConn(){
        if(conn == null){
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }


    //关闭连接
    public static final void close(){
        try {
            getConn().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
