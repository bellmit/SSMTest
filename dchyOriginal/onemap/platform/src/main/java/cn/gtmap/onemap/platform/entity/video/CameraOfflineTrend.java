package cn.gtmap.onemap.platform.entity.video;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * . camera offline trend
 *
 * @author <a href="mailto:zhaozhuyi@gtmap.cn">zhaozhuyi</a>
 * @version v1.0, 2018/1/29 (c) Copyright gtmap Corp.
 */
@Data
@AllArgsConstructor
public class CameraOfflineTrend implements Serializable {

    private static final long serialVersionUID = -3420986372749127681L;


    /**
     * camera name
     */
    private String name;

    /**
     * camera indexCode
     */
    private String indexCode;

    /**
     * offline seconds
     */
    private long offlineTime;

    /**
     * camera region
     */
    private String region;

    private String xzq;

}
