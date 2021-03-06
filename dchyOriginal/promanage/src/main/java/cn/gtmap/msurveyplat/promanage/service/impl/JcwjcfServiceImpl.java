package cn.gtmap.msurveyplat.promanage.service.impl;

import cn.gtmap.msurveyplat.promanage.model.ErrorInfoModel;
import cn.gtmap.msurveyplat.promanage.service.CheckZipFileService;
import cn.gtmap.msurveyplat.promanage.utils.Constants;
import cn.gtmap.msurveyplat.promanage.utils.WorkFlowXmlUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2021/4/21
 * @description 成果检查-检查文件重复
 */
@Service("jcwjcfServiceImpl")
public class JcwjcfServiceImpl implements CheckZipFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JcwjcfServiceImpl.class);

    @Autowired
    WorkFlowXmlUtil workFlowXmlUtil;

    @Override
    public List<ErrorInfoModel> checkZipFiles(byte[] zipBytes, String bh, Map<String, Object> mlMap, String babh, String gcbh, String chxmid, String glsxid) throws IOException {
        List<ErrorInfoModel> errorInfoModels = Lists.newArrayList();
        InputStream sbs = new ByteArrayInputStream(zipBytes);
        ZipInputStream zipInputStream = new ZipInputStream(sbs, Charset.forName("GBK"));
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            ErrorInfoModel errorInfoModel = new ErrorInfoModel();
            String zipEntryName = entry.getName();
            LOGGER.info(zipEntryName);
            String[] strings = zipEntryName.split("/");
            if (strings.length < 2) {
                continue;
            } else {
                if (MapUtils.isNotEmpty(mlMap) && mlMap.containsKey(strings[1])) {
                    Map<String, Object> yjmlMap = MapUtils.getMap(mlMap, strings[1]);
                    if (strings.length > 2) {
                        if (MapUtils.isNotEmpty(yjmlMap) && yjmlMap.containsKey(strings[2])) {
                            Map<String, Object> ejmlMap = MapUtils.getMap(yjmlMap, strings[2]);
                            if (strings.length > 3) {
                                if (strings[3].contains(".")) {
                                    String fileName = strings[3];
                                    if (ResultsSubmitServiceImpl.fileIsExist(zipEntryName)) {
                                        errorInfoModel.setClsx(strings[2]);
                                        errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_WJCF);
                                        errorInfoModel.setMlmc(zipEntryName);
                                        errorInfoModel.setWjmc(fileName);
                                        errorInfoModel.setWjzt(ResultsSubmitServiceImpl.queryShztByClcg(strings[2], gcbh, chxmid));
                                        errorInfoModels.add(errorInfoModel);
                                    }
                                } else {
                                    if (MapUtils.isNotEmpty(ejmlMap) && ejmlMap.containsKey(strings[3])) {
                                        List<String> filenameList = Lists.newArrayList();
                                        if (null != ejmlMap.get(strings[3]) && "" != ejmlMap.get(strings[3])) {
                                            filenameList = (List<String>) ejmlMap.get(strings[3]);
                                        }
                                        if (CollectionUtils.isNotEmpty(filenameList)) {
                                            if (strings.length > 4) {
                                                if (CollectionUtils.isNotEmpty(filenameList)) {
                                                    //去除后缀名
                                                    List<String> fileList = Lists.newArrayList();
                                                    for (String str : filenameList) {
                                                        fileList.add(str.substring(0, str.indexOf(".")));
                                                    }
                                                    if (fileList.contains(strings[4].substring(0, strings[4].indexOf(".")))) {
                                                        String fileName = strings[strings.length - 1];
                                                        if (ResultsSubmitServiceImpl.fileIsExist(zipEntryName)) {
                                                            errorInfoModel.setClsx(strings[2]);
                                                            errorInfoModel.setMsxx(Constants.DCHY_XMGL_CGSC_MSXX_WJCF);
                                                            errorInfoModel.setMlmc(zipEntryName);
                                                            errorInfoModel.setWjmc(fileName);
                                                            errorInfoModel.setWjzt(ResultsSubmitServiceImpl.queryShztByClcg(strings[2], gcbh, chxmid));
                                                            errorInfoModels.add(errorInfoModel);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return errorInfoModels;
    }

    @Override
    public String getCode() {
        return Constants.DCHY_XMGL_YWYZXX_CGTJ_WJCF;
    }
}
