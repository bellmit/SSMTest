package cn.gtmap.onemap.platform.entity;

import org.apache.commons.lang.StringUtils;

/**
 * .Doc entity
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-12-9 上午10:54
 */
public final class Document {

    public static final String DOTS = ".";

    /**
     * current support type
     */
    public static enum Type {
        doc, docx, xls, xlsx, txt, xml, zip, cpg, dbf, shp, shx, prj, sbx, sbn, fix, qix;

        public static final Type getType(String value) {
            for (Type item : values()) {
                if (item.name().equals(value)) return item;
            }
            return null;
        }
    }

    private String name;
    private byte[] content;
    private Type type;

    public String getName() {
        return name;
    }

    public Document(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Document(String name, byte[] content, Type type) {
        this.name = name;
        this.content = content;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public Document setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFileName() {
        return name.concat(".").concat(type.name());
    }

    /**
     * get doc by file name
     *
     * @param fileName
     * @return
     */
    public static final Document getDocByName(String fileName) {
        if (StringUtils.isBlank(fileName)) throw new RuntimeException("Document's name can't be empty!");
        int dots = fileName.lastIndexOf(DOTS);
        if (dots > -1) {
            String name = fileName.substring(0, dots);
            String suffix = fileName.substring(dots + 1, fileName.length());
            Type t = Type.getType(suffix);
            if (t != null) return new Document(name, t);
        }
        throw new RuntimeException(" get Document [" + fileName + "] error, Current doc type do not supported!");
    }
}
