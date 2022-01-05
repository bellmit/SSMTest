package cn.gtmap.onemap.server.monitor.collector.impl;

import cn.gtmap.onemap.server.monitor.collector.Collector;
import com.alibaba.fastjson.JSONObject;
import com.gtis.support.JndiSupportBasicDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-26
 */
public class DatabaseCollector implements Collector {
    private JdbcTemplate jdbc;
    private final JSONObject attrs;

    public DatabaseCollector(JSONObject attrs) {
        this.attrs = attrs;
    }

    @Override
    public int ping() {
        try {
            long t1 = System.currentTimeMillis();
            jdbc.execute(new ConnectionCallback<Object>() {
                @Override
                public Object doInConnection(Connection con) throws SQLException, DataAccessException {
                    return con.getMetaData().getTables(null, null, "KEEPALIVE", new String[]{"TABLE"});
                }
            });
            return (int) (System.currentTimeMillis() - t1);
        } catch (DataAccessException e) {
            return -1;
        }
    }

    @Override
    public String collect(String key) {
        List<String> list = jdbc.query(key, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString(1);
            }
        });
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void start() {
        BasicDataSource dataStore = new JndiSupportBasicDataSource();
        dataStore.setInitialSize(1);
        dataStore.setMaxActive(5);
        dataStore.setMinIdle(1);
        dataStore.setMaxIdle(2);
        dataStore.setDriverClassName(attrs.getString("driver"));
        dataStore.setUrl(attrs.getString("url"));
        dataStore.setUsername(attrs.getString("username"));
        dataStore.setPassword(attrs.getString("password"));
        jdbc = new JdbcTemplate(dataStore);
    }

    @Override
    public void stop() {
        try {
            ((BasicDataSource) jdbc.getDataSource()).close();
        } catch (SQLException ignored) {
        }
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
