package cn.gtmap.onemap.platform.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-3-27 上午9:37
 */
public class MappingFastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object>
        implements GenericHttpMessageConverter<Object> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private SerializerFeature[] serializerFeatures;

    /**
     * Construct an {@code AbstractHttpMessageConverter} with one supported media type.
     */
    public MappingFastJsonHttpMessageConverter() {
        super(new MediaType("application", "json", DEFAULT_CHARSET), new MediaType("application", "*+json", DEFAULT_CHARSET));
    }

    /**
     * Indicates whether the given class is supported by this converter.
     *
     * @param clazz the class to test for support
     * @return {@code true} if supported; {@code false} otherwise
     */
    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * Abstract template method that reads the actual object. Invoked from {@link #read}.
     *
     * @param clazz        the type of object to return
     * @param inputMessage the HTTP input message to read from
     * @return the converted object
     * @throws java.io.IOException in case of I/O errors
     * @throws org.springframework.http.converter.HttpMessageNotReadableException
     *                             in case of conversion errors
     */
    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return  JSON.parseObject(FileCopyUtils.copyToByteArray(inputMessage.getBody()), clazz);
    }

    /**
     * Abstract template method that writes the actual body. Invoked from {@link #write}.
     *
     * @param outputMessage the message to write to
     * @throws java.io.IOException in case of I/O errors
     * @throws org.springframework.http.converter.HttpMessageNotWritableException
     *                             in case of conversion errors
     */
    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String jsonString = JSON.toJSONString(o, this.serializerFeatures);
        OutputStream out = outputMessage.getBody();
        out.write(jsonString.getBytes(DEFAULT_CHARSET));
        out.flush();
    }

    public void setSerializerFeatures(SerializerFeature[] serializerFeatures) {
        this.serializerFeatures = serializerFeatures;
    }

    /**
     * Indicates whether the given type can be read by this converter.
     *
     * @param type         the type to test for readability
     * @param contextClass a context class for the target type, for example a class
     *                     in which the target type appears in a method signature, can be {@code null}
     * @param mediaType    the media type to read, can be {@code null} if not specified.
     *                     Typically the value of a {@code Content-Type} header.
     * @return {@code true} if readable; {@code false} otherwise
     */
    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return true;
    }

    /**
     * Read an object of the given type form the given input message, and returns it.
     *
     * @param type         the type of object to return. This type must have previously
     *                     been passed to the {@link #canRead canRead} method of this interface,
     *                     which must have returned {@code true}.
     * @param contextClass a context class for the target type, for example a class
     *                     in which the target type appears in a method signature, can be {@code null}
     * @param inputMessage the HTTP input message to read from
     * @return the converted object
     * @throws java.io.IOException in case of I/O errors
     * @throws org.springframework.http.converter.HttpMessageNotReadableException
     *                             in case of conversion errors
     */
    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }
}
