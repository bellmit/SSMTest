package cn.gtmap.onemap.server.monitor.service.impl;

import cn.gtmap.onemap.core.util.DateUtils;
import cn.gtmap.onemap.server.monitor.model.History;
import cn.gtmap.onemap.server.monitor.model.Item;
import cn.gtmap.onemap.server.monitor.model.Trend;
import cn.gtmap.onemap.server.monitor.model.enums.ValueType;
import cn.gtmap.onemap.server.monitor.service.DataManager;
import cn.gtmap.onemap.server.monitor.service.ItemManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-5
 */
public class DataManagerImpl implements DataManager {
    public static final String HISTORY = "m_history";
    public static final String TREND = "m_trend";
    private ItemManager itemManager;
    private JdbcOperations jdbc;
    private LobHandler lobHandler = new DefaultLobHandler();

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void setDataSource(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    @Override
    public List<History> findHistories(int itemId, Date begin, Date end) {
        final Item item = itemManager.getItem(itemId);
        List<Object> args = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select * from " + getHistoryTableName(item.getValueType()) + " where item_id=?");
        args.add(itemId);
        if (begin != null) {
            sql.append(" and clock>?");
            args.add(DateUtils.toUnixTimestamp(begin));
        }
        if (end != null) {
            sql.append(" and clock<=?");
            args.add(DateUtils.toUnixTimestamp(end));
        }
        sql.append(" order by clock");
        return jdbc.query(sql.toString(), new RowMapper<History>() {
            @Override
            public History mapRow(ResultSet rs, int rowNum) throws SQLException {
                History history = new History();
                history.setItemId(rs.getInt("item_id"));
                history.setClock(rs.getInt("clock"));
                history.setType(item.getValueType());
                Object value = null;
                switch (history.getType()) {
                    case LONG:
                        value = rs.getLong("value");
                        break;
                    case DOUBLE:
                        value = rs.getDouble("value");
                        break;
                    case STRING:
                        value = rs.getString("value");
                        break;
                    case TEXT:
                        value = lobHandler.getClobAsString(rs, "value");
                        break;
                    case BYTE:
                        value = lobHandler.getBlobAsBytes(rs, "value");
                        break;
                }
                history.setValue(value);
                return history;
            }
        }, args.toArray(new Object[args.size()]));
    }

    @Override
    public List<Trend> findTrends(int itemId, int interval, Date begin, Date end) {
        final Item item = itemManager.getItem(itemId);
        List<Object> args = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("select * from " + getTrendTableName(item.getValueType()) + " where item_id=? and num=?");
        args.add(itemId);
        args.add(interval);
        if (begin != null) {
            sql.append(" and clock>?");
            args.add(DateUtils.toUnixTimestamp(begin));
        }
        if (end != null) {
            sql.append(" and clock<=?");
            args.add(DateUtils.toUnixTimestamp(end));
        }
        sql.append(" order by clock");
        return jdbc.query(sql.toString(), new RowMapper<Trend>() {
            @Override
            public Trend mapRow(ResultSet rs, int rowNum) throws SQLException {
                Trend trend = new Trend();
                trend.setItemId(rs.getInt("item_id"));
                trend.setClock(rs.getInt("clock"));
                trend.setNum(rs.getInt("num"));
                trend.setType(item.getValueType());
                if (trend.getType() == ValueType.LONG) {
                    trend.setMin(rs.getLong("v_min"));
                    trend.setMax(rs.getLong("v_max"));
                    trend.setAvg(rs.getLong("v_avg"));
                } else {
                    trend.setMin(rs.getDouble("v_min"));
                    trend.setMax(rs.getDouble("v_max"));
                    trend.setAvg(rs.getDouble("v_avg"));
                }
                return trend;
            }
        }, args.toArray(new Object[args.size()]));
    }

    @Override
    public void saveHistories(Collection<History> histories) {
        for (History history : histories) {
            saveHistory(history);
        }
    }

    private void saveHistory(final History history) {
        String sql = "insert into " + getHistoryTableName(history.getType()) + " (item_id,clock,value) values (?,?,?)";
        jdbc.execute(sql, new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException, DataAccessException {
                ps.setInt(1, history.getItemId());
                ps.setLong(2, history.getClock());
                Object value = history.getValue();
                switch (history.getType()) {
                    case LONG:
                        ps.setLong(3, ((Number) value).longValue());
                        break;
                    case DOUBLE:
                        ps.setDouble(3, ((Number) value).doubleValue());
                        break;
                    case STRING:
                        ps.setString(3, value.toString());
                        break;
                    case TEXT:
                        lobCreator.setClobAsString(ps, 3, value.toString());
                        break;
                    case BYTE:
                        lobCreator.setBlobAsBytes(ps, 3, (byte[]) value);
                        break;
                }
            }
        });
    }

    @Override
    public void saveTrends(Collection<Trend> trends) {
        for (Trend trend : trends) {
            saveTrend(trend);
        }
    }

    private void saveTrend(final Trend trend) {
        String sql = "insert into " + getTrendTableName(trend.getType()) + " (item_id,clock,num,v_min,v_max,v_avg) values (?,?,?,?,?,?)";
        jdbc.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, trend.getItemId());
                ps.setLong(2, trend.getClock());
                ps.setInt(3, trend.getNum());
                if (trend.getType() == ValueType.LONG) {
                    if (trend.getMin() != null) {
                        ps.setLong(4, trend.getMin().longValue());
                    }
                    ps.setLong(5, trend.getMax().longValue());
                    ps.setLong(6, trend.getAvg().longValue());
                } else {
                    ps.setDouble(4, trend.getMin().doubleValue());
                    ps.setDouble(5, trend.getMax().doubleValue());
                    ps.setDouble(6, trend.getAvg().doubleValue());
                }
            }
        });
    }

    @Override
    public void clearHistories(Integer itemId, Date begin, Date end) {
        Item item = itemManager.getItem(itemId);
        List<Object> args = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("delete from " + getHistoryTableName(item.getValueType()) + " where item_id=?");
        args.add(itemId);
        if (begin != null) {
            sql.append(" and clock>?");
            args.add(DateUtils.toUnixTimestamp(begin));
        }
        if (end != null) {
            sql.append(" and clock<=?");
            args.add(DateUtils.toUnixTimestamp(end));
        }
        jdbc.update(sql.toString(), args.toArray(new Object[args.size()]));
    }

    @Override
    public void clearTrends(Integer itemId, Date begin, Date end) {
        Item item = itemManager.getItem(itemId);
        List<Object> args = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("delete from " + getTrendTableName(item.getValueType()) + " where item_id=?");
        args.add(itemId);
        if (begin != null) {
            sql.append(" and clock>?");
            args.add(DateUtils.toUnixTimestamp(begin));
        }
        if (end != null) {
            sql.append(" and clock<=?");
            args.add(DateUtils.toUnixTimestamp(end));
        }
        jdbc.update(sql.toString(), args.toArray(new Object[args.size()]));
    }

    @Override
    public Long getTrendLastClock(int itemId, int num) {
        List<Long> list = jdbc.queryForList("select max(clock) from " + getTrendTableName(itemManager.getItem(itemId).getValueType()) + " where item_id=? and num=?", Long.class, itemId, num);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Map<Integer, Integer> stat(int hostId, Date begin, Date end, final int size) {
        StringBuilder sql = new StringBuilder("select t1.item_id,count(t1.v_avg) num from m_trend_int t1 inner join m_item t2 on t2.id = t1.item_id where t2.host_id = ?");
        List<Object> args = Lists.newArrayList();
        args.add(hostId);
        if (begin != null) {
            sql.append(" and clock>?");
            args.add(DateUtils.toUnixTimestamp(begin));
        }
        if (end != null) {
            sql.append(" and clock<=?");
            args.add(DateUtils.toUnixTimestamp(end));
        }
        sql.append(" group by t1.item_id order by num desc");
        return jdbc.query(sql.toString(), new ResultSetExtractor<Map<Integer, Integer>>() {
            @Override
            public Map<Integer, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Map<Integer, Integer> map = Maps.newLinkedHashMap();
                int rowNum = 0;
                while (rs.next()) {
                    map.put(rs.getInt(1), rs.getInt(2));
                    if (++rowNum >= size) {
                        break;
                    }
                }
                return map;
            }
        }, args.toArray());
    }

    private String getHistoryTableName(ValueType type) {
        switch (type) {
            case LONG:
                return HISTORY + "_int";
            case DOUBLE:
                return HISTORY;
            case STRING:
                return HISTORY + "_str";
            case TEXT:
                return HISTORY + "_text";
            case BYTE:
                return HISTORY + "_bin";
        }
        return null;
    }

    private String getTrendTableName(ValueType type) {
        return type == ValueType.LONG ? TREND + "_int" : TREND;
    }
}
