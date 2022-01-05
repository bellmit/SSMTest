package cn.gtmap.msurveyplat.serviceol.service;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.Example;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.*;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.dto.DchyXmglHtxxDto;
import cn.gtmap.msurveyplat.serviceol.core.BaseUnitTest;
import cn.gtmap.msurveyplat.serviceol.core.service.DchyXmglHtService;
import cn.gtmap.msurveyplat.serviceol.core.service.mq.service.PushDataToMqService;
import cn.gtmap.msurveyplat.serviceol.utils.Constants;
import cn.gtmap.msurveyplat.serviceol.web.util.FileDownoadUtil;
import com.google.common.collect.Maps;
import com.gtis.fileCenter.model.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static cn.gtmap.msurveyplat.serviceol.utils.PlatformUtil.getNodeService;

/**
 * @author <a href="mailto:lijun1@gtmap.cn">lijun1</a>
 * @version 1.0, 2021/5/31 9:38
 * @description 测试线上备案数据同步至线下
 */
public class XsbfServiceTest extends BaseUnitTest {


    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DchyXmglHtService dchyXmglHtService;
    @Autowired
    PushDataToMqService pushDataToMqService;

    /**
     * 测试线上备案
     */
    @Test
    public void testPushDataToXx() throws Exception{
        String chxmid = "55VE48505Q6JT50V";
        String htwjzxid = "196500,196503";
        String chdwxxid = "55VE4928736JT515";
        /*同步数据*/
        this.pushXsBaseDataToXx(htwjzxid, chxmid, chdwxxid);
        /*同步文件*/
        Thread thread = new Thread() {
            @Override
            public void run() {
                /*组织核验与委托文件*/
                pushXsFileDataToXx(chxmid);
            }
        };
        thread.start();
        Thread.sleep(2000);
    }


    private void pushXsFileDataToXx(String chxmid) {
        /*组织委托核验材料*/
        DchyXmglChxmDto xmglChxmDto = this.generateWtAndHyFileToXx(chxmid);
        List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
        /*生成合同信息dto*/
        Example htxxExample = new Example(DchyXmglHtxx.class);
        htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
        if (CollectionUtils.isNotEmpty(htxxList)) {
            /*组织合同信息*/
            for (int i = 0; i < htxxList.size(); i++) {
                DchyXmglHtxxDto xmglHtxxDto = this.generateHtFileToXx(chxmid, htxxList.get(i).getHtxxid());
                htxxDtos.add(xmglHtxxDto);
            }
            xmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
            /*统一推送*/
            pushDataToMqService.pushOnlineWjFileToxx(xmglChxmDto);
        }
    }

    private DchyXmglChxmDto generateWtAndHyFileToXx(String chxmid) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        if (StringUtils.isNotBlank(chxmid)) {
            /*组织委托,核验时的数据文件*/
            /*测绘项目*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            /*修改状态后 线上数据推送到线下 --->  已拒绝的状态不用推送*/
            if (!StringUtils.equals(xmglChxm.getWtzt(), Constants.WTZT_YJJ)) {
                dchyXmglChxmDto.setDchyXmglChxm(xmglChxm);
            }
            /*委托和核验后的sjxx与sjcl*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                /*设置收件信息(委托，核验)*/
                dchyXmglChxmDto.setDchyXmglSjxxList(sjxxList);
                List<Object> sjxxidList = new ArrayList<>();
                for (DchyXmglSjxx sjxx : sjxxList) {
                    sjxxidList.add(sjxx.getSjxxid());
                }
                Example sjclExample = new Example(DchyXmglSjcl.class);
                sjclExample.createCriteria().andIn("sjxxid", sjxxidList).andIsNotNull("wjzxid");
                List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                if (CollectionUtils.isNotEmpty(sjclList)) {
                    /*委托、核验材料*/
                    List<Map<String, List>> fileList = new ArrayList<>();
                    List<Map<String, String>> file;
                    /*收件材料对应测量事项，用于线下备案文件拆分*/
                    List<Map<String, String>> sjclClsxList = new ArrayList<>();
                    dchyXmglChxmDto.setDchyXmglSjclList(sjclList);
                    Map<String, List> listMap = new HashMap<>();
                    for (DchyXmglSjcl sjcl : sjclList) {
                        String wjzxid = sjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            /*生成文件(委托，核验)*/
                            file = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                            listMap.put(sjcl.getSjclid(), file);
                            /*生成文件(委托，核验)收件材料对应测量事项*/
                            if (StringUtils.isNotEmpty(sjcl.getClsx()) && StringUtils.isNotEmpty(sjcl.getSjclid())) {
                                Map<String, String> sjclClsxMap = new HashMap<>();
                                sjclClsxMap.put(sjcl.getSjclid(), sjcl.getClsx());
                                sjclClsxList.add(sjclClsxMap);
                            }
                        }
                    }
                    fileList.add(listMap);
                    dchyXmglChxmDto.setFileList(fileList);
                    dchyXmglChxmDto.setSjclClsxList(sjclClsxList);
                }
            }
        }
        return dchyXmglChxmDto;
    }

    private DchyXmglHtxxDto generateHtFileToXx(String chxmid, String htxxid) {
        /*生成合同信息dto*/
        String glsxid = htxxid;
        DchyXmglHtxxDto dchyXmglHtxxDto = new DchyXmglHtxxDto();
        DchyXmglHtxx xmglHtxx = entityMapper.selectByPrimaryKey(DchyXmglHtxx.class, htxxid);
        List<DchyXmglHtxx> htxxList = new ArrayList<>();
        if (null != xmglHtxx) {
            htxxList.add(xmglHtxx);
        }
        if (CollectionUtils.isNotEmpty(htxxList)) {
            dchyXmglHtxxDto.setDchyXmglHtxxList(htxxList);
        }
        if (StringUtils.isNotBlank(glsxid)) {
            /*通过htxxid作为glsxid获取合同上传时的收件材料和收件信息*/
            Example htSjxxExample = new Example(DchyXmglSjxx.class);
            htSjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
            if (CollectionUtils.isNotEmpty(htSjxxList)) {
                /*设置收件信息*/
                dchyXmglHtxxDto.setDchyXmglSjxxList(htSjxxList);
                List<Object> sjxxs = new ArrayList<>();
                for (DchyXmglSjxx sjxx : htSjxxList) {
                    sjxxs.add(sjxx.getSjxxid());
                }
                Example htSjclExample = new Example(DchyXmglSjcl.class);
                htSjclExample.createCriteria().andIn("sjxxid", sjxxs);
                List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                /*设置收件材料*/
                dchyXmglHtxxDto.setDchyXmglSjclList(htSjclList);
                List<Map<String, List>> htFileList = new LinkedList<>();
                List<Map<String, String>> htFile;
                Map<String, List> listMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(htSjclList)) {
                    for (DchyXmglSjcl xmglSjcl : htSjclList) {
                        String wjzxid = xmglSjcl.getWjzxid();
                        if (StringUtils.isNotBlank(wjzxid)) {
                            /*生成文件(委托，核验)*/
                            htFile = this.generateFileByWjzxid(Integer.parseInt(wjzxid));
                            if (htFile != null) {
                                listMap.put(xmglSjcl.getSjclid(), htFile);
                                htFileList.add(listMap);
                            }
                        }
                    }
                }
                /*设置上传合同文件*/
                dchyXmglHtxxDto.setHtFileList(htFileList);

                /*合同测量事项关系*/
                List<Map<String, String>> htClsxList = new LinkedList<>();
                Example exampleClsxHtxxGx = new Example(DchyXmglClsxHtxxGx.class);
                exampleClsxHtxxGx.createCriteria().andEqualTo("htxxid", htxxid);
                List<DchyXmglClsxHtxxGx> dchyXmglClsxHtxxGxList = entityMapper.selectByExampleNotNull(exampleClsxHtxxGx);
                if (CollectionUtils.isNotEmpty(dchyXmglClsxHtxxGxList)) {
                    for (DchyXmglClsxHtxxGx dchyXmglClsxHtxxGx : dchyXmglClsxHtxxGxList) {
                        String clsxid = dchyXmglClsxHtxxGx.getClsxid();
                        if (StringUtils.isNotEmpty(clsxid)) {
                            DchyXmglChxmClsx dchyXmglChxmClsx = entityMapper.selectByPrimaryKey(DchyXmglChxmClsx.class, clsxid);
                            if (dchyXmglChxmClsx != null) {
                                String clsx = dchyXmglChxmClsx.getClsx();
                                if (StringUtils.isNotEmpty(clsx)) {
                                    Map<String, String> map1 = Maps.newHashMap();
                                    map1.put(htxxid, clsx);
                                    htClsxList.add(map1);
                                }
                            }
                        }
                    }
                }
                dchyXmglHtxxDto.setHtClsxList(htClsxList);
            }
        }
        return dchyXmglHtxxDto;
    }

    private List<Map<String, String>> generateFileByWjzxid(int nodeId) {
        List<Map<String, String>> sjclList = new ArrayList<>();
        try {
            int num = FileDownoadUtil.getFileNumberByNodeId(nodeId);
            List<Node> nodeList = getNodeService().getChildNodes(nodeId);
            if (CollectionUtils.isNotEmpty(nodeList)) {
                /*单个文件*/
                if (nodeList.size() == 1) {
                    Node node = nodeList.get(0);
                    byte[] body = FileDownoadUtil.downloadWj(node.getId());
                    String fileName = node.getName();
                    String file = Base64.getEncoder().encodeToString(body);
                    Map<String, String> sjcl = new HashMap<>();
                    sjcl.put(fileName, file);
                    sjclList.add(sjcl);
                } else {
                    for (Node node : nodeList) {
                        byte[] body = FileDownoadUtil.downloadWj(node.getId());
                        String fileName = node.getName();
                        String file = Base64.getEncoder().encodeToString(body);
                        Map<String, String> sjcl = new HashMap<>();
                        sjcl.put(fileName, file);
                        sjclList.add(sjcl);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
        }
        return sjclList;
    }



    private void pushXsBaseDataToXx(String htwjzxid, String chxmid, String chdwxxid) {
        String[] htwjs = htwjzxid.split(",");
        DchyXmglChxmDto xmglChxmDto = this.generateChxmDtoToXx(chxmid, chdwxxid);
        List<DchyXmglHtxxDto> htxxDtos = new ArrayList<>();
        /*组织合同数据*/
        /*生成合同信息dto*/
        Example htxxExample = new Example(DchyXmglHtxx.class);
        htxxExample.createCriteria().andEqualTo("chxmid", chxmid);
        List<DchyXmglHtxx> htxxList = entityMapper.selectByExample(htxxExample);
        if (CollectionUtils.isNotEmpty(htxxList)) {
            for (int i = 0; i < htxxList.size(); i++) {
                DchyXmglHtxxDto dchyXmglHtxxDto = this.generateHtxxDtoxToXx(chxmid, htwjs[i], htxxList.get(i).getHtxxid());
                htxxDtos.add(dchyXmglHtxxDto);
            }
            xmglChxmDto.setDchyXmglHtxxDtoList(htxxDtos);
            /*统一推送基础数据*/
            pushDataToMqService.pushSlxxMsgToMq(xmglChxmDto);
        }
    }

    private DchyXmglHtxxDto generateHtxxDtoxToXx(String chxmid, String htwjzxid, String htxxid) {
        /*生成合同信息dto*/
        String glsxid = "";
        Map<String, Object> map = new HashMap<>();
        map.put("chxmid", chxmid);
        map.put("htxxid", htxxid);
        map.put("folderId", htwjzxid);//合同文件中心id
        glsxid = htxxid;
        DchyXmglHtxxDto dchyXmglHtxxDto = dchyXmglHtService.generateHtxxDto(map);
        if (StringUtils.isNotBlank(glsxid)) {
            /*通过htxxid作为glsxid获取合同上传时的收件材料和收件信息*/
            Example htSjxxExample = new Example(DchyXmglSjxx.class);
            htSjxxExample.createCriteria().andEqualTo("glsxid", glsxid);
            List<DchyXmglSjxx> htSjxxList = entityMapper.selectByExample(htSjxxExample);
            if (CollectionUtils.isNotEmpty(htSjxxList)) {
                /*设置收件信息*/
                dchyXmglHtxxDto.setDchyXmglSjxxList(htSjxxList);
                List<Object> sjxxs = new ArrayList<>();
                for (DchyXmglSjxx sjxx : htSjxxList) {
                    sjxxs.add(sjxx.getSjxxid());
                }
                Example htSjclExample = new Example(DchyXmglSjcl.class);
                htSjclExample.createCriteria().andIn("sjxxid", sjxxs);
                List<DchyXmglSjcl> htSjclList = entityMapper.selectByExample(htSjclExample);
                /*设置收件材料*/
                dchyXmglHtxxDto.setDchyXmglSjclList(htSjclList);
            }
        }
        return dchyXmglHtxxDto;
    }


    private DchyXmglChxmDto generateChxmDtoToXx(String chxmid, String chdwxxid) {
        DchyXmglChxmDto dchyXmglChxmDto = new DchyXmglChxmDto();
        if (StringUtils.isNotBlank(chxmid)) {
            /*组织委托,核验时的数据文件*/
            /*测绘项目*/
            DchyXmglChxm xmglChxm = entityMapper.selectByPrimaryKey(DchyXmglChxm.class, chxmid);
            /*修改状态后 线上数据推送到线下 --->  已拒绝的状态不用推送*/
            if (!StringUtils.equals(xmglChxm.getWtzt(), Constants.WTZT_YJJ)) {
                /*设置测绘工程*/
                DchyXmglChgc chgc = entityMapper.selectByPrimaryKey(DchyXmglChgc.class, xmglChxm.getChgcid());
                if (null != chgc) {
                    dchyXmglChxmDto.setDchyXmglChgc(chgc);
                }
                dchyXmglChxmDto.setDchyXmglChxm(xmglChxm);
            }
            /*设置测绘单位信息*/
            DchyXmglChxmChdwxx chdwxx = entityMapper.selectByPrimaryKey(DchyXmglChxmChdwxx.class, chdwxxid);
            if (null != chdwxx) {
                List<DchyXmglChxmChdwxx> list3 = new ArrayList<>();
                list3.add(chdwxx);
                dchyXmglChxmDto.setDchyXmglChxmChdwxxList(list3);
            }
            /*设置测量事项*/
            Example clsxExample = new Example(DchyXmglChxmClsx.class);
            clsxExample.createCriteria().andEqualTo("chxmid", chxmid);
            List<DchyXmglChxmClsx> clsxList = entityMapper.selectByExample(clsxExample);
            dchyXmglChxmDto.setDchyXmglChxmClsxList(clsxList);
            /*设置申请信息*/
            Example exampleSqxx = new Example(DchyXmglSqxx.class);
            exampleSqxx.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSqxx> dchyXmglSqxxList = entityMapper.selectByExampleNotNull(exampleSqxx);
            if (null != dchyXmglSqxxList) {
                dchyXmglChxmDto.setDchyXmglSqxxList(dchyXmglSqxxList);
            }
            /*clsx_chdwxx_gx*/
            Example clsxChdwxxGx = new Example(DchyXmglClsxChdwxxGx.class);
            clsxChdwxxGx.createCriteria().andEqualTo("chxmid",chxmid);
            List<DchyXmglClsxChdwxxGx> clsxChdwxxGxList = entityMapper.selectByExample(clsxChdwxxGx);
            if(CollectionUtils.isNotEmpty(clsxChdwxxGxList)){
                dchyXmglChxmDto.setDchyXmglClsxChdwxxGxList(clsxChdwxxGxList);
            }
            /*委托和核验后的sjxx与sjcl*/
            Example sjxxExample = new Example(DchyXmglSjxx.class);
            sjxxExample.createCriteria().andEqualTo("glsxid", chxmid);
            List<DchyXmglSjxx> sjxxList = entityMapper.selectByExample(sjxxExample);
            if (CollectionUtils.isNotEmpty(sjxxList)) {
                /*设置收件信息(委托，核验)*/
                dchyXmglChxmDto.setDchyXmglSjxxList(sjxxList);
                List<DchyXmglSjcl> wtAndHySjclList = new ArrayList<>();
                for (DchyXmglSjxx xmglSjxx : sjxxList) {
                    String sjxxid = xmglSjxx.getSjxxid();
                    Example sjclExample = new Example(DchyXmglSjcl.class);
                    sjclExample.createCriteria().andEqualTo("sjxxid", sjxxid).andIsNotNull("wjzxid");
                    List<DchyXmglSjcl> sjclList = entityMapper.selectByExample(sjclExample);
                    if (CollectionUtils.isNotEmpty(sjclList)) {
                        /*委托、核验材料*/
                        wtAndHySjclList.addAll(sjclList);
                    }
                }
                dchyXmglChxmDto.setDchyXmglSjclList(wtAndHySjclList);
            }
        }
        return dchyXmglChxmDto;
    }
}
