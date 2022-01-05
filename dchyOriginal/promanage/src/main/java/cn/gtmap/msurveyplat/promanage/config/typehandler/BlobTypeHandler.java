package cn.gtmap.msurveyplat.promanage.config.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class BlobTypeHandler extends BaseTypeHandler<byte[]> {

    //###指定字符集
    private static final String DEFAULT_CHARSET = "GB2312";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,

                                    byte[] parameter, JdbcType jdbcType) throws SQLException {
        ByteArrayInputStream bis = null;
        int size = 0;
        try {
            //###把String转化成byte流
            bis = new ByteArrayInputStream(parameter);
            size = bis.available();
//
//            InputStreamReader input = new InputStreamReader(bis);
//            BufferedReader bf = new BufferedReader(input);
//            String line = null;
//            StringBuilder sb = new StringBuilder();
//            while((line=bf.readLine()) != null){
//                sb.append(line);
//            }
//
//            System.out.println(new String (sb.toString().getBytes(DEFAULT_CHARSET),DEFAULT_CHARSET));
        } catch (Exception e) {
            throw new RuntimeException("Blob Encoding Error!");
        }
        ps.setBinaryStream(i, bis, size);
    }

    @Override
    public byte[] getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
//        try {
        //###把byte转化成string
        if (null != returnValue) {
            return returnValue;
        }
        return new byte[0];
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("Blob Encoding Error!");
//        }
    }


    @Override
    public byte[] getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
        }
        return returnValue;
//        try {
//            return new String(returnValue, DEFAULT_CHARSET);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("Blob Encoding Error!");
//        }
    }

    @Override
    public byte[] getNullableResult(ResultSet arg0, int arg1)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;

    }
}