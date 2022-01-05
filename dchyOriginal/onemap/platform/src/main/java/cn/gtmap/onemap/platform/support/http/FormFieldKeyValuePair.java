package cn.gtmap.onemap.platform.support.http;

import java.io.Serializable;

/**
 * .
 *
 * @author <a href="mailto:yingxiufeng@gtmap.cn">alex.y</a>
 * @version v1.0, 2017/9/29 (c) Copyright gtmap Corp.
 */
public class FormFieldKeyValuePair implements Serializable {

    private static final long serialVersionUID = -4506717918386214299L;


    private String key;


    private String value;

    public FormFieldKeyValuePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
