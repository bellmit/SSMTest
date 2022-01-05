package cn.gtmap.msurveyplat.common.dto;

import cn.gtmap.msurveyplat.common.annotion.DescribeAno;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:lijingmo@gtmap.cn">lijingmo</a>
 * @version 1.o, 2020-12-10
 * description
 */
public class DchyXmglHtxxDto {
    String glsxid;

    @DescribeAno("合同详情")
    DchyXmglHtxx dchyXmglHtxx;

    @DescribeAno("合同详情列表")
    List<DchyXmglHtxx> dchyXmglHtxxList;

    @DescribeAno("测量事项与测绘单位关系")
    List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList;

    @DescribeAno("合同与测绘单位关系")
    List<DchyXmglHtxxChdwxxGx> dchyXmglHtxxChdwxxGxList;

    @DescribeAno("测量事项与合同关系")
    List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList;

    @DescribeAno("收件信息")
    DchyXmglSjxx dchyXmglSjxx;

    @DescribeAno("收件信息列表")
    List<DchyXmglSjxx> dchyXmglSjxxList;

    @DescribeAno("合同信息")
    DchyXmglSjcl dchyXmglSjcl;

    @DescribeAno("合同信息列表")
    List<DchyXmglSjcl> dchyXmglSjclList;

    @DescribeAno("合同文件")
    List<Map<String,String>> sjclList;

    @DescribeAno("拆分合同文件")
    List<Map<String, List>> htFileList;

    @DescribeAno("合同文件测量事项关系")
    List<Map<String, String>> htClsxList;

    public List<Map<String, String>> getHtClsxList() {
        return htClsxList;
    }

    public void setHtClsxList(List<Map<String, String>> htClsxList) {
        this.htClsxList = htClsxList;
    }

    public List<Map<String, List>> getHtFileList() {
        return htFileList;
    }

    public void setHtFileList(List<Map<String, List>> htFileList) {
        this.htFileList = htFileList;
    }

    public DchyXmglHtxx getDchyXmglHtxx() {
        return dchyXmglHtxx;
    }

    public void setDchyXmglHtxx(DchyXmglHtxx dchyXmglHtxx) {
        this.dchyXmglHtxx = dchyXmglHtxx;
    }

    public DchyXmglSjxx getDchyXmglSjxx() {
        return dchyXmglSjxx;
    }

    public void setDchyXmglSjxx(DchyXmglSjxx dchyXmglSjxx) {
        this.dchyXmglSjxx = dchyXmglSjxx;
    }

    public DchyXmglSjcl getDchyXmglSjcl() {
        return dchyXmglSjcl;
    }

    public void setDchyXmglSjcl(DchyXmglSjcl dchyXmglSjcl) {
        this.dchyXmglSjcl = dchyXmglSjcl;
    }

    public List<DchyXmglHtxx> getDchyXmglHtxxList() {
        return dchyXmglHtxxList;
    }

    public void setDchyXmglHtxxList(List<DchyXmglHtxx> dchyXmglHtxxList) {
        this.dchyXmglHtxxList = dchyXmglHtxxList;
    }

    public List<DchyXmglSjxx> getDchyXmglSjxxList() {
        return dchyXmglSjxxList;
    }

    public void setDchyXmglSjxxList(List<DchyXmglSjxx> dchyXmglSjxxList) {
        this.dchyXmglSjxxList = dchyXmglSjxxList;
    }

    public List<DchyXmglSjcl> getDchyXmglSjclList() {
        return dchyXmglSjclList;
    }

    public void setDchyXmglSjclList(List<DchyXmglSjcl> dchyXmglSjclList) {
        this.dchyXmglSjclList = dchyXmglSjclList;
    }

    public List<DchyXmglClsxChdwxxGx> getDchyXmglClsxChdwxxGxList() {
        return dchyXmglClsxChdwxxGxList;
    }

    public void setDchyXmglClsxChdwxxGxList(List<DchyXmglClsxChdwxxGx> dchyXmglClsxChdwxxGxList) {
        this.dchyXmglClsxChdwxxGxList = dchyXmglClsxChdwxxGxList;
    }

    public List<DchyXmglHtxxChdwxxGx> getDchyXmglHtxxChdwxxGxList() {
        return dchyXmglHtxxChdwxxGxList;
    }

    public void setDchyXmglHtxxChdwxxGxList(List<DchyXmglHtxxChdwxxGx> dchyXmglHtxxChdwxxGxList) {
        this.dchyXmglHtxxChdwxxGxList = dchyXmglHtxxChdwxxGxList;
    }

    public List<DchyXmglClsxHtxxGx> getDchyXmglClsxHtxxGxList() {
        return dchyXmglClsxHtxxGxList;
    }

    public void setDchyXmglClsxHtxxGxList(List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList) {
        this.dchyXmglClsxHtxxGxList = dchyXmglClsxHtxxGxList;
    }

    public String getGlsxid() {
        return glsxid;
    }

    public void setGlsxid(String glsxid) {
        this.glsxid = glsxid;
    }

    public List<Map<String, String>> getSjclList() {
        return sjclList;
    }

    public void setSjclList(List<Map<String, String>> sjclList) {
        this.sjclList = sjclList;
    }



}
