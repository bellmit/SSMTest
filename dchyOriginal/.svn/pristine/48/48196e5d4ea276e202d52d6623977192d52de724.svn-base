package cn.gtmap.onemap.platform.service.impl;

import cn.gtmap.onemap.platform.BaseServiceTest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.codehaus.xfire.client.Client;
import org.junit.Test;

import javax.xml.namespace.QName;

import java.net.URL;

/**
 * Created by Administrator on 2015/7/21.
 */
public class TransitServiceImplTest extends BaseServiceTest {


    @Test
    public void testWcf(){
        String wcfUrl="http://192.168.254.2/services/CmsService?wsdl";
        try {
            Client client = new Client(new URL(wcfUrl));
            JSONObject params = new JSONObject();
            params.put("userName","guotuju");
            params.put("pwd","95F162EAB370A15B03398DACD78A26FB");
            params.put("clientIp","200.200.6.68");
            params.put("clientPort",8080);
            params.put("cmsUrl","http://192.168.254.2");
            Object[] objects=new Object[]{};
            try {
                objects = client.invoke("userLogin", new String[]{JSON.toJSONString(params)});
            } catch (Exception e) {
              e.printStackTrace();
            }
            print(String.valueOf(objects[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final QName SERVICE_NAME = new QName("http://webservice.cms.hikvision.com", "CmsService");

    @Test
    public void testGetAllControlUnit(){

////        List<ControlUnitDTO> controlUnitDTOs=transitService.getAllControlUnit();
//        List<RegionInfoDTO> regionInfoDTOs = transitService.getAllRegionInfo();
//        print(JSON.toJSONString(regionInfoDTOs));
//        List<Map> list = transitService.getCameraViewInfo(indexCode);
//        print(JSON.toJSONString(list));
   //     List<CameraInfoDTO> cameraInfoDTOs =transitService.getAllCameras("2364");
    //    print(JSON.toJSONString(cameraInfoDTOs));
     //   String indexCode =cameraInfoDTOs.get(0).getIndexCode();
      //  String previewXml = transitService.getXmlByCamId("admin", "7ece99e593ff5dd200e2b9233d9ba654", TransitService.OperaType.valueOf("preview"), indexCode);
       // print(previewXml);
        //print("----");
        //String x = "118.781739136";
        //String y = "34.41449341";
        //transitService.controlPTZ(indexCode,x,y);
//        print(playbackXml);
        //print("----");
    }

    @Test
    public void testCms(){

//        String cmsUrl = "http://192.168.254.2";
//        URL wsdlURL = CmsService.WSDL_LOCATION;
//        CmsService ss = new CmsService(wsdlURL, CmsService.SERVICE);
//        CmsServicePortType port = ss.getCmsServiceHttpSoap12Endpoint();
//        System.out.println("Invoking userLogin...");
//        String _userLogin_userName = "guotuju";
//        String _userLogin_pwd = "95F162EAB370A15B03398DACD78A26FB";
//        String _userLogin_clientIp = "200.200.6.68";
//        Integer _userLogin_clientPort = 8080;
//        String _userLogin_cmsUrl = cmsUrl;
//        LoginResult _userLogin__return = null;
//        try {
//            _userLogin__return = port.userLogin(_userLogin_userName, _userLogin_pwd, _userLogin_clientIp, _userLogin_clientPort, _userLogin_cmsUrl);
//
//            String sessionId = _userLogin__return.getSessionId().getValue();
//            print("userLogin.result=" + sessionId);
//
//            ControlUnitsResult controlUnitsResult = port.getAllControlCenterForList(sessionId);
//            print("control unit:"+controlUnitsResult.getControlUnitArray().size());
//
//            RegionInfoResult regionInfoResult = port.getAllRegionInfoForList(sessionId);
//            print("region info:"+regionInfoResult.getRegionInfoArray().size());
//
//            PreviewResult previewResult = port.getPreviewParam(sessionId, "6774");
//            print("preview xml:" + previewResult.getPreviewXml().getValue());


//            CameraInfoResult cameraInfoResult = port.getAllCameraInfoForList(sessionId);
//            List<CameraInfoDTO> list = cameraInfoResult.getCameraInfoDTOArray();
//            print("camera count:"+list.size());
//            for (CameraInfoDTO cameraInfoDTO : list) {
//                print("camera id:"+cameraInfoDTO.getCameraId().getValue());
//                print("camera indexcode:"+cameraInfoDTO.getIndexCode().getValue());
//            }

//           String url = "http://192.168.254.2/thirdparty!previewIndex.action?user=guotuju&pwd=95F162EAB370A15B03398DACD78A26FB&ipmac=10.2.2.2&cameraIndexCode=13103112191310884344,13103112191310389839,13103112191310749441,13103112191310686670&cameraId=54,55,56,57";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }







    }
}
