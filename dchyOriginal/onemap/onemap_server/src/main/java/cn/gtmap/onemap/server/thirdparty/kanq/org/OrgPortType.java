package cn.gtmap.onemap.server.thirdparty.kanq.org;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.5.0
 * 2013-12-27T11:07:05.233+08:00
 * Generated source version: 2.5.0
 * 
 */
@WebService(targetNamespace = "http://service.ser.kanq.com", name = "orgPortType")
@XmlSeeAlso({ObjectFactory.class})
public interface OrgPortType {

    @WebResult(name = "return", targetNamespace = "http://service.ser.kanq.com")
    @Action(input = "urn:getOrganStr", output = "urn:getOrganStrResponse")
    @RequestWrapper(localName = "getOrganStr", targetNamespace = "http://service.ser.kanq.com", className = "cn.gtmap.onemap.server.thirdparty.kanq.org.GetOrganStr")
    @WebMethod(action = "urn:getOrganStr")
    @ResponseWrapper(localName = "getOrganStrResponse", targetNamespace = "http://service.ser.kanq.com", className = "cn.gtmap.onemap.server.thirdparty.kanq.org.GetOrganStrResponse")
    public java.lang.String getOrganStr(
        @WebParam(name = "license", targetNamespace = "http://service.ser.kanq.com")
        java.lang.String license
    );

    @WebResult(name = "return", targetNamespace = "http://service.ser.kanq.com")
    @Action(input = "urn:add", output = "urn:addResponse")
    @RequestWrapper(localName = "add", targetNamespace = "http://service.ser.kanq.com", className = "cn.gtmap.onemap.server.thirdparty.kanq.org.Add")
    @WebMethod(action = "urn:add")
    @ResponseWrapper(localName = "addResponse", targetNamespace = "http://service.ser.kanq.com", className = "cn.gtmap.onemap.server.thirdparty.kanq.org.AddResponse")
    public java.lang.String add(
        @WebParam(name = "code", targetNamespace = "http://service.ser.kanq.com")
        java.lang.String code,
        @WebParam(name = "name", targetNamespace = "http://service.ser.kanq.com")
        java.lang.String name,
        @WebParam(name = "main", targetNamespace = "http://service.ser.kanq.com")
        java.lang.Integer main,
        @WebParam(name = "remark", targetNamespace = "http://service.ser.kanq.com")
        java.lang.String remark,
        @WebParam(name = "order", targetNamespace = "http://service.ser.kanq.com")
        java.lang.Integer order,
        @WebParam(name = "type", targetNamespace = "http://service.ser.kanq.com")
        java.lang.Integer type
    );
}
