package cn.gtmap.msurveyplat.serv.web;

import cn.gtmap.msurveyplat.common.core.support.mybatis.mapper.EntityMapper;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglChxmChdwxx;
import cn.gtmap.msurveyplat.common.domain.dchyxmgl.DchyXmglMqCzrz;
import cn.gtmap.msurveyplat.common.dto.DchyXmglChxmDto;
import cn.gtmap.msurveyplat.common.util.CalendarUtil;
import cn.gtmap.msurveyplat.serv.service.impl.TestService;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.gtis.common.util.UUIDGenerator;
import com.gtis.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/getjson")
    public Object test() {
        Map map = Maps.newHashMap();
        map.put("1", "2");
        DchyXmglMqCzrz dchyXmglMqCzrz = new DchyXmglMqCzrz();
        String str = "{\"clsx\":[\"地形图补测（扩建、续建等工程）\",\"房产预测绘\",\"规划验线测量\",\"人防竣工测量\",\"地下管线测量\",\"地籍竣工测量\",\"房产实测绘\",\"绿地测量\",\"规划核实测量\",\"1001\",\"1002\"],\"gdwwyqwjList\":[{\"clsx\":\"地形图补测（扩建、续建等工程）\",\"mlmc\":\"XXTXCS0127/工程建设许可/地形图补测（扩建、续建等工程）/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"房产预测绘\",\"mlmc\":\"XXTXCS0127/施工许可/房产预测绘/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"规划验线测量\",\"mlmc\":\"XXTXCS0127/施工许可/规划验线测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"人防竣工测量\",\"mlmc\":\"XXTXCS0127/竣工验收/人防竣工测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"地下管线测量\",\"mlmc\":\"XXTXCS0127/竣工验收/地下管线测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"地籍竣工测量\",\"mlmc\":\"XXTXCS0127/竣工验收/地籍竣工测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"房产实测绘\",\"mlmc\":\"XXTXCS0127/竣工验收/房产实测绘/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"绿地测量\",\"mlmc\":\"XXTXCS0127/竣工验收/绿地测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"},{\"clsx\":\"规划核实测量\",\"mlmc\":\"XXTXCS0127/竣工验收/规划核实测量/其他资料/\",\"msxx\":\"规定外未要求文件\",\"wjmc\":\"\"}],\"gscwList\":[],\"sftj\":\"0\",\"wjcfList\":[],\"wjqsList\":[{\"clsx\":\"1001\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/地形图测量/矢量数据/地形图测绘.dwg\",\"msxx\":\"文件缺失\",\"wjmc\":\"地形图测绘.dwg\"},{\"clsx\":\"1001\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/地形图测量/扫描资料/用地现状地形图.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"用地现状地形图.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/不动产权籍调查报告.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"不动产权籍调查报告.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/建设项目勘测定界报告.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"建设项目勘测定界报告.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/界址点坐标成果表.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"界址点坐标成果表.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/勘测定界图.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"勘测定界图.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/平面控制测量成果.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"平面控制测量成果.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/土地分类面积表.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"土地分类面积表.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/土地勘测定界表.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"土地勘测定界表.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/扫描资料/项目用地地理位置图.pdf\",\"msxx\":\"文件缺失\",\"wjmc\":\"项目用地地理位置图.pdf\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/矢量数据/土地勘测定界测绘.dwg\",\"msxx\":\"文件缺失\",\"wjmc\":\"土地勘测定界测绘.dwg\"},{\"clsx\":\"1002\",\"mlmc\":\"XXTXCS0127/立项用地规划许可/土地勘测定界/元数据/metadata.Xml\",\"msxx\":\"文件缺失\",\"wjmc\":\"metadata.Xml\"}]}\n";
        dchyXmglMqCzrz.setCzrzid(UUIDGenerator.generate18());
        dchyXmglMqCzrz.setCzsj(CalendarUtil.getCurHMSDate());
        dchyXmglMqCzrz.setMsg(str.getBytes(Charsets.UTF_8));
        return map;
    }

    @GetMapping("/testCommit")
    public Object testCommit() {
        DchyXmglChxmDto dchyXmglChxmDto = testService.dchyXmglChxmDto();
        dchyXmglChxmDto.getDchyXmglChgc().setGcmc("sdfasdfas");
        dchyXmglChxmDto.getDchyXmglChgc().setLxdh("15051995513");
        dchyXmglChxmDto.getDchyXmglChxmClsxList().get(1).setClsxid("delete_" + dchyXmglChxmDto.getDchyXmglChxmClsxList().get(1).getClsxid());
        DchyXmglChxmChdwxx dchyXmglChxmChdwxx = new DchyXmglChxmChdwxx();
        dchyXmglChxmChdwxx.setChdwmc("测试");
        dchyXmglChxmChdwxx.setChdwxxid(UUIDGenerator.generate18());
        dchyXmglChxmDto.getDchyXmglChxmChdwxxList().add(dchyXmglChxmChdwxx);
        System.out.println(dchyXmglChxmDto);
        return dchyXmglChxmDto;
    }

    @GetMapping("/testExport")
    public Object testExport(HttpServletResponse response) {
        String path = "D:/testout1.pdf";
        String egovConf = AppConfig.getProperty("egov.conf");
        return egovConf.substring(6, egovConf.length() - 1);
    }

    public static void main(String[] args) {
        String zipname = "XXTXCS0127/竣工验收/地籍竣工测量/矢量数据/地籍测绘.dwg";
        String zipname1 = "XXTXCS0127/竣工验收/地籍竣工测量/矢量数据/";

        String num = "454887";
        System.out.print("请您输入内容:");
        Scanner input = new Scanner(System.in);//创建一个键盘扫描类对象
        String contents = input.next(); //输入字符型

        if (contents.length() > 3) {
            contents = contents.substring(contents.length() - 3, contents.length());
            System.out.println(contents);
        } else {
            System.out.println(contents);
        }
    }

    public static int getCharacterPosition(String string, String ch, int index) {
        //这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile(ch).matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start() + 1;
    }
}