package cn.gtmap.onemap.platform.entity.video;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 视频平台
 * Author: yingxiufeng
 * Date:  2017/6/13 19:28
 * Version: v1.0
 */
public class VideoPlats {


    private List<Plat> plats;

    public VideoPlats() {
    }

    public Plat getPlat(String name) {
        for (Plat plat : plats) {
            if (plat.getName().equalsIgnoreCase(name)) {
                return plat;
            }
        }
        return null;
    }

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    /**
     * plat
     */
    public static final class Plat {

        private String name;
        private String platType;
        private String userName;
        private String password;
        private String server;
        private String port;
        private String ptzServer;
        private String wsuServer;

        public Plat(String platType, String userName, String password, String server, String port, String ptzServer) {
            this.platType = platType;
            this.userName = userName;
            this.password = password;
            this.server = server;
            this.port = port;
            this.ptzServer = ptzServer;
            this.wsuServer = ptzServer;
        }

        public Plat() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlatType() {
            return platType;
        }

        public void setPlatType(String platType) {
            this.platType = platType;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = StringUtils.trim(server);
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getPtzServer() {
            return ptzServer;
        }

        public void setPtzServer(String ptzServer) {
            this.ptzServer = StringUtils.trim(ptzServer);
        }

        public String getWsuServer() {
            return wsuServer;
        }

        public void setWsuServer(String wsuServer) {
            this.wsuServer = StringUtils.trim(wsuServer);
        }
    }
}
