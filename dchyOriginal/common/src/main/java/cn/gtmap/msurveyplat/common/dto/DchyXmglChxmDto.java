package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-10
 * description
 */
public class DchyXmglChxmDto {

    String glsxid;

    //推送的操作类型(新增或者删除)
    String czlx;

    @DescribeAno("工程信息")
    DchyXmglChgc dchyXmglChgc;

    @DescribeAno("项目信息")
    DchyXmglChxm dchyXmglChxm;

    @DescribeAno("测绘单位")
    List<DchyXmglChdw> dchyXmglChdwList;

    @DescribeAno("单位信息")
    List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList;

    @DescribeAno("测量事项")
    List<DchyXmglChxmClsx> dchyXmglChxmClsxList;

    @DescribeAno("测量事项测绘单位关系")
    List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList;

    @DescribeAno("合同信息")
    List<DchyXmglHtxxDto> dchyXmglHtxxDtoList;

    @DescribeAno("收件材料")
    DchyXmglSjcl dchyXmglSjcl;

    @DescribeAno("收件材料列表")
    List<DchyXmglSjcl> dchyXmglSjclList;

    @DescribeAno("收件信息")
    DchyXmglSjxx dchyXmglSjxx;

    @DescribeAno("收件信息列表")
    List<DchyXmglSjxx> dchyXmglSjxxList;

    @DescribeAno("线上委托已办任务")
    List<DchyXmglYbrw> dchyXmglYbrwList;

    @DescribeAno("申请信息")
    DchyXmglSqxx dchyXmglSqxx;

    @DescribeAno("申请信息列表")
    List<DchyXmglSqxx> dchyXmglSqxxList;

    @DescribeAno("待办任务")
    DchyXmglDbrw dchyXmglDbrw;

    Map<String, List<Map<String, String>>> sjcl;

    @DescribeAno("核验、委托文件")
    List<Map<String, List>> fileList;

    @DescribeAno("核验、委托文件收件材料对应测量事项")
    List<Map<String, String>> sjclClsxList;

    @DescribeAno("收件材料")
    List<Map<String, String>> sjclList;

    @DescribeAno("测量事项测绘体量")
    List<DchyXmglClsxChtl> dchyXmglClsxChtlList;

    //删除的文件(base64)
    Map<String, String> deletefile;

    //删除的数据(主键id)
    Map idMaps;

    public List<DchyXmglClsxChtl> getDchyXmglClsxChtlList() {
        return dchyXmglClsxChtlList;
    }

    public void setDchyXmglClsxChtlList(List<DchyXmglClsxChtl> dchyXmglClsxChtlList) {
        this.dchyXmglClsxChtlList = dchyXmglClsxChtlList;
    }

    public List<Map<String, String>> getSjclClsxList() {
        return sjclClsxList;
    }

    public void setSjclClsxList(List<Map<String, String>> sjclClsxList) {
        this.sjclClsxList = sjclClsxList;
    }

    public List<Map<String, List>> getFileList() {
        return fileList;
    }

    public void setFileList(List<Map<String, List>> fileList) {
        this.fileList = fileList;
    }

    public List<DchyXmglChdw> getDchyXmglChdwList() {
        return dchyXmglChdwList;
    }

    public void setDchyXmglChdwList(List<DchyXmglChdw> dchyXmglChdwList) {
        this.dchyXmglChdwList = dchyXmglChdwList;
    }

    public List<DchyXmglClsxChdwxxGx> getDchyXmglClsxChdwxxGxList() {
        return dchyXmglClsxChdwxxGxList;
    }

    public void setDchyXmglClsxChdwxxGxList(List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList) {
        this.dchyXmglClsxChdwxxGxList = dchyXmglClsxChdwxxGxList;
    }


    public DchyXmglSjcl getDchyXmglSjcl() {
        return dchyXmglSjcl;
    }

    public void setDchyXmglSjcl(DchyXmglSjcl dchyXmglSjcl) {
        this.dchyXmglSjcl = dchyXmglSjcl;
    }

    public DchyXmglSjxx getDchyXmglSjxx() {
        return dchyXmglSjxx;
    }

    public void setDchyXmglSjxx(DchyXmglSjxx dchyXmglSjxx) {
        this.dchyXmglSjxx = dchyXmglSjxx;
    }

    public DchyXmglSqxx getDchyXmglSqxx() {
        return dchyXmglSqxx;
    }

    public void setDchyXmglSqxx(DchyXmglSqxx dchyXmglSqxx) {
        this.dchyXmglSqxx = dchyXmglSqxx;
    }

    public DchyXmglChxm getDchyXmglChxm() {
        return dchyXmglChxm;
    }

    public void setDchyXmglChxm(DchyXmglChxm dchyXmglChxm) {
        this.dchyXmglChxm = dchyXmglChxm;
    }

    public String getGlsxid() {
        return glsxid;
    }

    public void setGlsxid(String glsxid) {
        this.glsxid = glsxid;
    }

    public DchyXmglChgc getDchyXmglChgc() {
        return dchyXmglChgc;
    }

    public void setDchyXmglChgc(DchyXmglChgc dchyXmglChgc) {
        this.dchyXmglChgc = dchyXmglChgc;
    }

    public List<DchyXmglChxmChdwxx> getDchyXmglChxmChdwxxList() {
        return dchyXmglChxmChdwxxList;
    }

    public void setDchyXmglChxmChdwxxList(List<DchyXmglChxmChdwxx> dchyXmglChxmChdwxxList) {
        this.dchyXmglChxmChdwxxList = dchyXmglChxmChdwxxList;
    }

    public List<DchyXmglChxmClsx> getDchyXmglChxmClsxList() {
        return dchyXmglChxmClsxList;
    }

    public void setDchyXmglChxmClsxList(List<DchyXmglChxmClsx> dchyXmglChxmClsxList) {
        this.dchyXmglChxmClsxList = dchyXmglChxmClsxList;
    }

    public List<DchyXmglHtxxDto> getDchyXmglHtxxDtoList() {
        return dchyXmglHtxxDtoList;
    }

    public void setDchyXmglHtxxDtoList(List<DchyXmglHtxxDto> dchyXmglHtxxDtoList) {
        this.dchyXmglHtxxDtoList = dchyXmglHtxxDtoList;
    }

    public List<DchyXmglSjcl> getDchyXmglSjclList() {
        return dchyXmglSjclList;
    }

    public void setDchyXmglSjclList(List<DchyXmglSjcl> dchyXmglSjclList) {
        this.dchyXmglSjclList = dchyXmglSjclList;
    }

    public List<DchyXmglSjxx> getDchyXmglSjxxList() {
        return dchyXmglSjxxList;
    }

    public void setDchyXmglSjxxList(List<DchyXmglSjxx> dchyXmglSjxxList) {
        this.dchyXmglSjxxList = dchyXmglSjxxList;
    }

    public List<DchyXmglYbrw> getDchyXmglYbrwList() {
        return dchyXmglYbrwList;
    }

    public void setDchyXmglYbrwList(List<DchyXmglYbrw> dchyXmglYbrwList) {
        this.dchyXmglYbrwList = dchyXmglYbrwList;
    }

    public List<DchyXmglSqxx> getDchyXmglSqxxList() {
        return dchyXmglSqxxList;
    }

    public void setDchyXmglSqxxList(List<DchyXmglSqxx> dchyXmglSqxxList) {
        this.dchyXmglSqxxList = dchyXmglSqxxList;
    }

    public DchyXmglDbrw getDchyXmglDbrw() {
        return dchyXmglDbrw;
    }

    public void setDchyXmglDbrw(DchyXmglDbrw dchyXmglDbrw) {
        this.dchyXmglDbrw = dchyXmglDbrw;
    }

    public Map<String, List<Map<String, String>>> getSjcl() {
        return sjcl;
    }

    public void setSjcl(Map<String, List<Map<String, String>>> sjcl) {
        this.sjcl = sjcl;
    }

    public String getCzlx() {
        return czlx;
    }

    public void setCzlx(String czlx) {
        this.czlx = czlx;
    }

    public List<Map<String, String>> getSjclList() {
        return sjclList;
    }

    public void setSjclList(List<Map<String, String>> sjclList) {
        this.sjclList = sjclList;
    }

    public Map<String, String> getDeletefile() {
        return deletefile;
    }

    public void setDeletefile(Map<String, String> deletefile) {
        this.deletefile = deletefile;
    }

    public Map getIdMaps() {
        return idMaps;
    }

    public void setIdMaps(Map idMaps) {
        this.idMaps = idMaps;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dchyXmglChgc").append(" .getGcbh ").append(dchyXmglChgc != null ? dchyXmglChgc.getGcbh() : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglChxm").append(" .getSlbh  ").append(dchyXmglChxm != null ? dchyXmglChxm : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglChxmChdwxxList").append(" size ").append(dchyXmglChxmChdwxxList != null ? CollectionUtils.size(dchyXmglChxmChdwxxList) : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglChxmClsxList").append(" size ").append(dchyXmglChxmClsxList != null ? CollectionUtils.size(dchyXmglChxmClsxList) : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglHtxxDtoList").append(" size ").append(dchyXmglHtxxDtoList != null ? CollectionUtils.size(dchyXmglHtxxDtoList) : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglSjclList").append(" size ").append(dchyXmglSjclList != null ? CollectionUtils.size(dchyXmglSjclList) : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglSjxxList").append(" size ").append(dchyXmglSjxxList != null ? CollectionUtils.size(dchyXmglSjxxList) : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglYbrwList").append(" size ").append(dchyXmglYbrwList != null ? CollectionUtils.size(dchyXmglYbrwList) : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglSqxxList").append(" size ").append(dchyXmglSqxxList != null ? CollectionUtils.size(dchyXmglSqxxList) : 0);
        stringBuilder.append("/r/n");
        stringBuilder.append("dchyXmglDbrw").append(" .getDbrwid ").append(dchyXmglDbrw != null ? dchyXmglDbrw.getDbrwid() : 0);
        stringBuilder.append("/r/n");

        return stringBuilder.toString();
    }
}
