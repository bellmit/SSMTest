package cn.gtmap.onemap.server.monitor.collector.impl;


import cn.gtmap.onemap.core.Constants;
import cn.gtmap.onemap.server.monitor.collector.Collector;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 13-7-26
 */
class ZabbixCollector implements Collector {
    private static final Logger LOG = LoggerFactory.getLogger(ZabbixCollector.class);
    public static final String HEADER = "ZBXD";

    private final String host;
    private final int port;

    public ZabbixCollector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public int ping() {
        String ping = request("agent.ping");
        return ping == null ? -1 : Integer.valueOf(ping);
    }

    @Override
    public String collect(String key) {
        return request(key);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    private String request(String key) {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 5000);
            os = socket.getOutputStream();
            Writer writer = new PrintWriter(socket.getOutputStream());
            writer.write(key + "\n");
            writer.flush();
            is = socket.getInputStream();
            String ret = IOUtils.toString(is, Constants.DEFAULT_CHARSET);
            if (ret != null && ret.startsWith(HEADER)) {
                return ret.substring(13);
            }
        } catch (Throwable e) {
            LOG.info("Read key [" + key + "] from " + host + ":" + port + " error,", e.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String s = new ZabbixCollector("192.168.51.160", 10050).request("vm.memory.size[total]");
        System.out.println(s);
    }
}
