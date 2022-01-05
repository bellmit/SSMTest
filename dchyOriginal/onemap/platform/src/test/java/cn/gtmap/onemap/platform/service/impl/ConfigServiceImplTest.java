package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import cn.gtmap.onemap.platform.entity.Configuration;
import cn.gtmap.onemap.platform.entity.Service;
import cn.gtmap.onemap.platform.service.ConfigService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-11-2 上午10:41
 */
public class ConfigServiceImplTest extends BaseServiceTest {

    @Autowired
    private ConfigService configService;

    @Test
    public void testCreateTpl() throws Exception {

        String tpl = "111";

        Configuration configuration = null;
        try {
            configuration = configService.createTpl("111");
            configuration = configService.createTpl("moban");
            configuration = configService.createTpl("模板");
//            configService.deleteTpl("111");
            configService.deleteTpl("moban");
            configService.deleteTpl("模板");


        } catch (Exception e) {
            e.printStackTrace();
        }

        print("-- create tpl--");

    }

    @Test
    public void testService() throws Exception {

        String tpl = "112";
        try {
            configService.createTpl(tpl);

            Service service = new Service();
            service.setId("serviceId_001");
            configService.insertService(tpl, service);
            List<Service> services = configService.getAllService(tpl);
            service.setAlias("alias__11");
            configService.updateService(tpl, service);
            services = configService.getAllService(tpl);
            configService.deleteServices(tpl, new String[]{service.getId()});
            configService.updateService(tpl, service);

            print("-- test services --");
        } catch (Exception e) {
            e.printStackTrace();
            configService.deleteTpl(tpl);
        }

    }

    @Test
    public void testWidget() throws Exception {

        String tpl = "113";
        try {
            configService.createTpl(tpl);

            List<Configuration.Widget> widgets = configService.getAllDockWidgets(tpl);

            Configuration.Widget widget = widgets.get(0);
//            widget.setLeft(10);
            configService.updateDockWidget(tpl, widget);

            Configuration.Widget item = new Configuration.Widget("Test", "Test", "Test");
            configService.insertWidget(tpl, item);

            //
            widgets = configService.getAllWidgets(tpl);

            widget = widgets.get(0);
            widget.setLabel("TTT");
            configService.updateWidget(tpl, widget);
            //

            List<Configuration.WidgetsGroup> widgetsGroups = configService.getAllWidgetGroups(tpl);
            Configuration.WidgetsGroup widgetsGroup = widgetsGroups.get(0);
            widgetsGroup.setLabel("TTGroup");
            configService.updateWidgetGroup(tpl, widgetsGroup, widget);

            Configuration.WidgetsGroup widgetsGroup2 = new Configuration.WidgetsGroup();
            widgetsGroup2.setId("Test3");
            configService.updateWidgetGroup(tpl, widgetsGroup2, widget);


            print("-- -- ");

            configService.deleteTpl(tpl);


        } catch (Exception ex) {
            ex.printStackTrace();
            configService.deleteTpl(tpl);
        }

    }
    @Test
    public void testServiceWithClassify() throws Exception {

        List groups = configService.getServicesWithClassify();
        print("--");
    }

    @Test
    public void testGroupService() throws Exception {

        List info = null;
        configService.getAllSevices();
//        configService.getServicesByGroupId(null);
        info = configService.getServicesByGroupId("014051b4e9802c92825b404c8a430322");
        info = configService.getServicesByGroupId("014051b4b7b72c92825b404c8a430320");

        print("--");

    }
    @Test
    public void testCommonwidgets() throws  Exception {

        List<Configuration.Widget> list = configService.getCommonWidgets();
//        configService.deleteCommonWidget("Print");
        Configuration.Widget widget = new Configuration.Widget("testWidget","测试widget","test");
        configService.insertCommonWidget(widget);
        list = configService.getCommonWidgets();
        print("--");

    }
    @Test
    public void testSearchConfig() throws Exception {

        Map  map = configService.getSearchConfig();
//        map.get("layers")
        print("--");
    }

    @Test
    public void testDeleteWidget() throws Exception {

        Configuration.Widget widget = new Configuration.Widget("E0294A80-EE46-300C-B14A-36A463708D65","属性识别","NIdenityfy");
        configService.deleteWidget("gtmap",widget);
        print("--");
    }

    @Test
    public void testDeleteWidgetsGroup() throws Exception {

        String tplName = "new";
        Configuration.WidgetsGroup widgetsGroup = new Configuration.WidgetsGroup();
        widgetsGroup.setId("Analysis");
        configService.deleteWidgetsGroup(tplName,widgetsGroup,null);
        print("--");

    }
    @Test
    public void testInsertPublicWidget() throws Exception{

        Configuration.Widget widget = new Configuration.Widget("E0294A80-EE46-300C-B14A-36A463708D65","属性识别2","NIdenityfy");
//        configService.deletePublicWidget("E0294A80-EE46-300C-B14A-36A463708D65");
        configService.insertPublicWidget(widget);
        List<Configuration.Widget> widgets = configService.getPublicWidgets();
        print("--");

    }

}
