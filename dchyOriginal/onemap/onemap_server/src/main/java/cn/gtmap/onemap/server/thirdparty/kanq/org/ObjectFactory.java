
package cn.gtmap.onemap.server.thirdparty.kanq.org;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.gtmap.onemap.server.thirdparty.kanq.org package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetOrganStrResponseReturn_QNAME = new QName("http://service.ser.kanq.com", "return");
    private final static QName _AddRemark_QNAME = new QName("http://service.ser.kanq.com", "remark");
    private final static QName _AddName_QNAME = new QName("http://service.ser.kanq.com", "name");
    private final static QName _AddCode_QNAME = new QName("http://service.ser.kanq.com", "code");
    private final static QName _GetOrganStrLicense_QNAME = new QName("http://service.ser.kanq.com", "license");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.gtmap.onemap.server.thirdparty.kanq.org
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetOrganStrResponse }
     * 
     */
    public GetOrganStrResponse createGetOrganStrResponse() {
        return new GetOrganStrResponse();
    }

    /**
     * Create an instance of {@link AddResponse }
     * 
     */
    public AddResponse createAddResponse() {
        return new AddResponse();
    }

    /**
     * Create an instance of {@link Add }
     * 
     */
    public Add createAdd() {
        return new Add();
    }

    /**
     * Create an instance of {@link GetOrganStr }
     * 
     */
    public GetOrganStr createGetOrganStr() {
        return new GetOrganStr();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ser.kanq.com", name = "return", scope = GetOrganStrResponse.class)
    public JAXBElement<String> createGetOrganStrResponseReturn(String value) {
        return new JAXBElement<String>(_GetOrganStrResponseReturn_QNAME, String.class, GetOrganStrResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ser.kanq.com", name = "return", scope = AddResponse.class)
    public JAXBElement<String> createAddResponseReturn(String value) {
        return new JAXBElement<String>(_GetOrganStrResponseReturn_QNAME, String.class, AddResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ser.kanq.com", name = "remark", scope = Add.class)
    public JAXBElement<String> createAddRemark(String value) {
        return new JAXBElement<String>(_AddRemark_QNAME, String.class, Add.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ser.kanq.com", name = "name", scope = Add.class)
    public JAXBElement<String> createAddName(String value) {
        return new JAXBElement<String>(_AddName_QNAME, String.class, Add.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ser.kanq.com", name = "code", scope = Add.class)
    public JAXBElement<String> createAddCode(String value) {
        return new JAXBElement<String>(_AddCode_QNAME, String.class, Add.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.ser.kanq.com", name = "license", scope = GetOrganStr.class)
    public JAXBElement<String> createGetOrganStrLicense(String value) {
        return new JAXBElement<String>(_GetOrganStrLicense_QNAME, String.class, GetOrganStr.class, value);
    }

}
