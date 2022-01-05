package cn.gtmap.onemap.platform.utils;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * utils for dom4j
 * Author: <a href="mailto:yingxiufeng@gtmap.cn">yingxiufeng</a>
 * Date:  2015/10/23 13:47
 */
public final class Dom4jUtils {

    /***
     * 为指定节点添加属性
     * @param element
     * @param name
     * @param value
     * @return
     */
    public static Element addAttribute(Element element, String name, String value) {
        element.addAttribute(name, value);
        return element;
    }

    /***
     * 设置指定节点的指定属性的值
     * @param element
     * @param name
     * @param value
     * @return
     */
    public static Element setAttribute(Element element, String name, String value) {
        Attribute attribute = element.attribute(name);
        if (attribute==null)
            throw new RuntimeException("attribute 【" + name + "】is null");
        attribute.setValue(value);
        return element;
    }

    /***
     * 移除指定节点的指定属性
     * @param element
     * @param name
     * @return
     */
    public static Element removeAttribute(Element element,String name){
        element.remove(element.attribute(name));
        return element;
    }

    /***
     * 设置指定节点的值
     * @param element
     * @param value
     * @return
     */
    public static Element setElementValue(Element element,String value){
        element.setText(value);
        return element;
    }

    /***
     * 为指定节点添加子节点
     * @param parent
     * @param name
     * @return
     */
    public static Element addElement(Element parent, String name) {
        Element element = (Element) parent.selectSingleNode(name);
        if (element != null)
            parent.remove(element);
        return parent.addElement(name);
    }


}
