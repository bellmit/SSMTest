package cn.gtmap.msurveyplat.portalol.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/2
 * @description TODO
 */
public class CodeUtil {
    public static final String ILLEGALCHARACTER = "1111";
    public static final String NEEDLOGIN = "6666";
    public static final String DECRYPTERROR = "4444";
    public static final String NORIGHTHANDLE = "2333";
    public static final String SAVEFAILED = "2334";
    public static final String UPDATEFAILED = "2335";
    public static final String DELETEFAILED = "2336";
    public static final String ZDYYC = "9999";
    public static final String CAHASEXISTS = "0201";
    public static final String CANOTEXISTS = "0202";
    public static final String CAORGNOTEQUALUSERORG = "0203";
    public static final String CABINDINGTYPEERROR = "0204";
    public static final String USERHASBINDCA = "0205";
    public static final String EASYPWD = "0101";
    public static final String NEWPWDEQUALSOLDPWD = "0102";
    public static final String SUCCESS = "0000";
    public static final String PARAMNULL = "0001";
    public static final String ENCRYPTERROR = "0002";
    public static final String TOKENINVALID = "0003";
    public static final String PARAMERROR = "0004";
    public static final String RUNERROR = "0005";
    public static final String SIGNDIFF = "0006";
    public static final String DATANULLORCHANGEERROR = "0007";
    public static final String CHANGEERROR = "0008";
    public static final String OPENIDNULL = "0009";
    public static final String ORIGINDIFF = "0010";
    public static final String GETPUBLICDATAFAIL = "0011";
    public static final String DATESTYLEERROR = "0012";
    public static final String ZDNULl = "0013";
    public static final String GETTOKENFAIL = "0014";
    public static final String HTTPLINKFAIL = "0015";
    public static final String FREQUENCYERROR = "0016";
    public static final String VIDEOSAVEERROR = "0017";
    public static final String AUDIOSAVEERROR = "0018";
    public static final String LOWPASSWORDCOMPLEXITY = "0019";
    public static final String GETACCESSTOKENFAILL = "0020";
    public static final String ACCESSTOKENISNULL = "0021";
    public static final String ACCESSTOKENEXPIRED = "0022";
    public static final String INVALIDACCESSTOKEN = "0023";
    public static final String APPCONFIGNULL = "0024";
    public static final String CLASSCASTFAILED = "0025";
    public static final String RESOURCENOTEXISTS = "0026";
    public static final String RESOURCEILLEGAL = "0027";
    public static final String GETRESOURCENULL = "0028";
    public static final String NORIGHTCREATEROLE = "0031";
    public static final String NORIGHTCREATEORGANIZE = "0032";
    public static final String NORIGHTCREATEUSER = "0033";
    public static final String ROLEILLEGAL = "0034";
    public static final String ORGANIZEILLEGAL = "0035";
    public static final String ROLECANNOTDELETE = "0036";
    public static final String REQUSERIDORBEOPERATEUSERIDNULL = "0037";
    public static final String ROLEEXISTS = "0038";
    public static final String ORGANIZEEXISTS = "0039";
    public static final String ORGANIZENOTEXISTS = "0040";
    public static final String ROLENOTEXISTS = "0041";
    public static final String CANNOTBEPARENTASSELF = "0042";
    public static final String MUSTCHOOSEPARENTORGID = "0043";
    public static final String GETORGANIZEINFOFAILL = "0044";
    public static final String MUSTCALOGIN = "0045";
    public static final String FUNCTIONMENUILLEGAL = "0046";
    public static final String DATASCOPEILLEGAL = "0047";
    public static final String OPENIDEXISTS = "0048";
    public static final String REALNAMENULL = "0049";
    public static final String SJEXISTS = "0050";
    public static final String USERDJZXERROR = "0051";
    public static final String BDCQZHNULL = "0052";
    public static final String BDCDYBHNULL = "0053";
    public static final String BDCDYHNULL = "0054";
    public static final String XMIDNULL = "0055";
    public static final String THIRD_INTERFACE_EXECUTE_FAILED = "0056";
    public static final String HIGH_PHOTO_OPEN_FAILED = "0057";
    public static final String SERVICEID_ERROR = "0058";
    public static final String USERNAMENULL = "1001";
    public static final String PWDNULL = "1002";
    public static final String USERNAMENONE = "1003";
    public static final String QLRUSERNAMENONE = "10031";
    public static final String JSUSORDINARYUSERCANLOGIN = "10036";
    public static final String QLRJTCYNULL = "10032";
    public static final String QLRJTCYGXMCNULL = "10033";
    public static final String QLRJTCYMCZJHNULL = "10034";
    public static final String QLRJTCYGXNULL = "10035";
    public static final String HYZTCHECKERROR = "10037";
    public static final String FWTCCHECKERROR = "10038";
    public static final String PWDERROR = "1004";
    public static final String PHONENULL = "1005";
    public static final String ZJNUll = "1006";
    public static final String ROLENUll = "1007";
    public static final String LOGINNAMEEXIST = "1008";
    public static final String ZJHEXIST = "1009";
    public static final String USERNOTEXIST = "1010";
    public static final String NAMEANDPWDNULL = "1011";
    public static final String PHONEEXIST = "1012";
    public static final String USERMOREEXIST = "1013";
    public static final String DISABLE = "1014";
    public static final String IDCARDNOTRIGHT = "1015";
    public static final String USEROREMAILNOTEXIST = "1016";
    public static final String USERNOTADMIN = "1017";
    public static final String PWDERRORCOUNTLIMIT = "1018";
    public static final String ROLEMUSTBEUSER = "1019";
    public static final String USERNAMEPWDERROR = "1020";
    public static final String USERNAMEEXIST = "1021";
    public static final String USERGUIDISNULL = "1022";
    public static final String LOGINYZMERROR = "1023";
    public static final String SAVEUSERFAILED = "1024";
    public static final String JGNAMEEXISTS = "1025";
    public static final String GETUSERINFOFAILL = "1026";
    public static final String CACERTIFYFAILED = "1027";
    public static final String USERROLENULL = "1028";
    public static final String NUMORLETTER = "1029";
    public static final String NUMANDLETTER = "1030";
    public static final String NUMAaLETTER = "1031";
    public static final String NUMANDMARK = "1032";
    public static final String RESULTNONE = "2001";
    public static final String SQXXNULL = "2002";
    public static final String SQXXSLBHNULL = "20021";
    public static final String SQXXSQLXNULL = "20022";
    public static final String SQXXBDCLXNULL = "20023";
    public static final String SQXXCREATEUSERNULLORNOTEXIST = "20024";
    public static final String SQXXCREATEBYADMIN = "20025";
    public static final String SQXXZSHNULL = "20026";
    public static final String SQXXZLNULL = "20027";
    public static final String SQXXBDCDYHNULL = "20028";
    public static final String SQXXMJNULL = "20029";
    public static final String SQXXFCZHNULL = "20030";
    public static final String SQXXCQZHEXIST = "200301";
    public static final String SQXXBDCDYHEXIST = "200302";
    public static final String SQXXIDNULL = "200210";
    public static final String SQXXNOTEXIST = "200211";
    public static final String SQXXEXIST = "200212";
    public static final String DJXTCXYC = "200213";
    public static final String DJXTTOKENCW = "200214";
    public static final String BANKDYTSFAILL = "200215";
    public static final String CFZTCANNOTDY = "200216";
    public static final String CFZTCANNOT = "200217";
    public static final String DYZTCANNOT = "200218";
    public static final String SQXXMMHTHNULL = "200219";
    public static final String SQXXZSLYNULL = "200220";
    public static final String SQXXDYZMHNULL = "200221";
    public static final String SQXXUPDATEFAILL = "200222";
    public static final String SQXXDYZMHERROR = "200223";
    public static final String SQXXTJSBQLXGLY = "200224";
    public static final String SQXXDYZMHEXIST = "200225";
    public static final String ZSNOTMATCH = "200226";
    public static final String SQXXSQLXNOTEXIST = "200227";
    public static final String SQXXCANNOTRECALL = "200228";
    public static final String SQXXCANNOTDELETE = "200229";
    public static final String SQXXSLZTABNORMAL = "200230";
    public static final String SQXXCANNOTSUPPLYFJ = "200231";
    public static final String HTHISEXIST = "200232";
    public static final String YWXTSLBHNOTEXIST = "200233";
    public static final String QUERYBYMINE = "200234";
    public static final String SQXXXXBL = "200235";
    public static final String QZYSXLHISNULL = "200236";
    public static final String AUTOFINISHFAIL = "200237";
    public static final String QLRNULL = "2003";
    public static final String QLRMCNULL = "20031";
    public static final String QLRZJZLNULL = "20032";
    public static final String QLRZJHNULL = "20033";
    public static final String QLRIDNULL = "20034";
    public static final String QLRSQIDNULL = "20035";
    public static final String QLRUSERNOTMATCH = "20036";
    public static final String QLRQLBLERROR = "20037";
    public static final String YWRQLBLERROR = "20038";
    public static final String QLRYWRXT = "20039";
    public static final String QLRYZERROR = "20040";
    public static final String FJXMNULL = "2004";
    public static final String FJDATANULL = "20041";
    public static final String FJXMFJLXNULL = "20042";
    public static final String QUERYSQXXHQDATAFAILL = "20043";
    public static final String HASUSERISNOTVERIFICATION = "20044";
    public static final String QLRGYFSERROR = "20045";
    public static final String BDCDYHISTAXVERIFY = "20046";
    public static final String FJXXNULL = "2005";
    public static final String FJXXPICNULL = "20051";
    public static final String FJXXFILEMCNULL = "20052";
    public static final String FJXXCREATEUSERNULL = "20053";
    public static final String FJXXPICSAVEERROR = "20054";
    public static final String FJOVERLIMIT = "20070";
    public static final String FJTYPEISNULL = "20100";
    public static final String FJISNOTSUPORTTHISTYPE = "20101";
    public static final String FJTYPEWRONGFUL = "20102";
    public static final String SQXXNEEDVERIFYBUTNOTPASS = "200103";
    public static final String EXCELERROR = "20055";
    public static final String SQXXWXBLDAISERROR = "20056";
    public static final String SQXXWXBLINSERTFAILL = "20057";
    public static final String TKSELECTFAILORNOTEXIST = "20058";
    public static final String SQXXWXBLTHROWENTRYEXCEPTION = "20059";
    public static final String UPDATERLSBRZFAILL = "20060";
    public static final String GYFSERROR = "200600";
    public static final String YYXXNOTEXIST = "2006";
    public static final String YYXXCREATEUSERNULL = "20061";
    public static final String YYXXYYRMCNULL = "20062";
    public static final String YYXXYYRZJHNULL = "20063";
    public static final String YYXXYYRLXDHNULL = "20064";
    public static final String YYXXYYDJZXNULL = "20065";
    public static final String YYXXYYDJZXNOTEXIST = "20066";
    public static final String YYXXYYSDNULL = "20067";
    public static final String YYXXYYSDNOTEXIST = "20068";
    public static final String YYXXYYDJLXNULL = "20069";
    public static final String YYXXYYDJLXNOTEXIST = "200610";
    public static final String YYXXYYLYNULL = "200611";
    public static final String YYXXYYNUMLIMIT = "200612";
    public static final String YYXXYYEXIST = "200613";
    public static final String YYXXYYNUMLIMITBYDAY = "200614";
    public static final String YYXXCREATEUSERNOTEXIST = "200615";
    public static final String YYXXYYSJNULL = "200616";
    public static final String YYXXCANCELTIMELIMIT = "200617";
    public static final String YYXXEXIST = "200618";
    public static final String YYSJJJR = "200619";
    public static final String GETDATEFAIL = "200620";
    public static final String YYSDFULL = "200621";
    public static final String YYERROR = "200622";
    public static final String CONTYYTODAY = "200623";
    public static final String YYBMDMISNULL = "200624";
    public static final String YYHMD = "200625";
    public static final String YYXXYYSQLXNULL = "200626";
    public static final String CONTYYTOWEEEK = "200627";
    public static final String HASYYTODAY = "200628";
    public static final String YYSJERROR = "200629";
    public static final String JFXXNULL = "20071";
    public static final String ACTIVEJFXXEXIST = "20072";
    public static final String GENERATEDDXXFAIL = "20073";
    public static final String SHINFONULL = "20074";
    public static final String PAYREQUESTURLNULL = "20075";
    public static final String DDPARAMNULL = "20076";
    public static final String SIGNERROR = "20077";
    public static final String PAYREQUESTERROR = "20078";
    public static final String FFFSNULL = "20079";
    public static final String FFFSERROR = "200710";
    public static final String ZFFSNULL = "200711";
    public static final String ZFFSERROR = "200712";
    public static final String DDNOTEXIST = "200713";
    public static final String DDJEERROR = "200714";
    public static final String YHKNOTEXIST = "200715";
    public static final String YHKINFONULL = "200716";
    public static final String YHKYBD = "200718";
    public static final String YHKBDFAIL = "200719";
    public static final String PAYPWDERROR = "200720";
    public static final String PAYFAILED = "200721";
    public static final String YHKJBFAIL = "200722";
    public static final String DDNOTCLOSE = "200717";
    public static final String WJFACTIVEDD = "200723";
    public static final String DDNOTPAY = "200724";
    public static final String TRANSERROR = "200725";
    public static final String SFXMERROR = "200726";
    public static final String SLBHNOTEXIST = "200727";
    public static final String REFUNDFAILED = "200730";
    public static final String REFUNDNOTEXIST = "200732";
    public static final String HJJEERROR = "200731";
    public static final String JF_WHQDSWXX = "200733";
    public static final String JF_HQSWXXYC = "200734";
    public static final String JF_WHQDSWXXMX = "200735";
    public static final String REFUNDRESULTFALL = "200736";
    public static final String JF_WCXDJFRXX = "200740";
    public static final String NSRSBHNULL = "200741";
    public static final String TSSWXXFAIL = "200742";
    public static final String YZMERROR = "20081";
    public static final String SMSSENDERROR = "20082";
    public static final String ZSSLBHNULL = "20091";
    public static final String ZSQLRERROR = "20092";
    public static final String BDCDYHBYGDZHNULL = "20094";
    public static final String BDCDYHBYGDZHERROR = "20095";
    public static final String WSHDAXXEXIST = "20093";
    public static final String IDENTITYNOTMATCH = "30009";
    public static final String IDENTITYVALIDEERROR = "30008";
    public static final String LIVEDETECTFAIL = "110001";
    public static final String FACECOMPAREFAIL = "110002";
    public static final String LIVEDETECTTHRESHOLDLOW = "110003";
    public static final String LIVEDETECTRESULTNONE = "110004";
    public static final String LIVEDETECTTIMEOUT = "110005";
    public static final String LIVEDETECTRESULTEMPTY = "110006";
    public static final String LIVEDETECTSUCCESS = "110007";
    public static final String LIVEDETECTYWLXNONE = "110008";
    public static final String LIVEDETECTCOUNTLIMIT = "110009";
    public static final String ALIPAYREQUESTFAIL = "110010";
    public static final String ALIPAYQUERYFAIL = "110011";
    public static final String ALIPAYUSERINFONOTCERTIFIED = "110012";
    public static final String ALIPAYUSERTYPENOTSUPPORT = "110013";
    public static final String NEEDREDIRECT = "12001";
    public static final String ONEMAPIDNOTEXIST = "12002";
    public static final String PERSONMUSTVERIFY = "12003";
    public static final String NEEDCHOOSEORGANIZE = "12004";
    public static final String FWWDNOTEXIT = "20010";
    public static final String SERVEEVALUATED = "210001";
    public static final String WECHATTEMPLATEPUSHFAIL = "220001";
    public static final String WECHATTEMPLATEIDISEMPTY = "220002";
    public static final String WECHATTEMPLATEDATAISEMPTY = "220003";
    public static final String SPWXSMIDNULL = "230001";
    public static final String SPWXSMSMRNULL = "230002";
    public static final String SQLXNUll = "230003";
    public static final String SPWXYYNUll = "230004";
    public static final String SQLXPKNUll = "240001";
    public static final String SQLXEXIST = "250001";
    public static final String CAZDILLEGAL = "250002";
    public static final String ZDILLEGAL = "250003";
    public static final String TRANSPUBLICSQXXFAILED = "260010";
    public static final String TRANSPUBLICFJXXFAILED = "260011";
    public static final String ACCEPTANCEBUILDFAILED = "260001";
    public static final String ACCEPTANCETRANSFJFAILED = "260002";
    public static final String UCUSERINFOFAILED = "270001";
    public static final String MYYCAPPGETINITCODEFAILED = "28001";
    public static final String MYYCAPPGETAUTHCODEFAILED = "28002";
    public static final String MYYCAPPGETACCESSTOKENFAILED = "28003";
    public static final String MYYCAPPGETUSERACCESSTOKENFAILED = "28004";
    public static final String MYYCAPPGETUSERINFOFAILED = "28005";
    public static final String WECHATQUICKLOGINFAILED = "29001";
    public static final String WECHATQUICKLOGINNEEDSUPPLEMENT = "29002";
    public static final String VERIFYJSZWTICKETFAILED = "30001";
    public static final String VERIFYJSZWTOKENFAILED = "30002";
    public static final String GETZWWUSERFAILED = "30003";
    public static final String NOTSUPPORTCORPORATIONQUICKLOGIN = "30004";
    public static final String CXTRQLXXCSCX = "40001";
    public static final String GSGGXXTSMHWZYC = "50001";
    public static final String FHXXZDY = "60001";
    public static final String HTXXNONE = "60002";
    public static final String FWXXNONE = "60003";
    public static final String FWXXYBL = "60004";
    public static final String HTHNONE = "60005";
    public static final String YKQHQJFMXSB = "61001";
    public static final String YKQSCDDSB = "61002";
    public static final String YKQCXDDSB = "61003";
    public static final String YKQHQDJ3JFMX = "61004";
    public static final String YKQHQDJ2JFMX = "61005";
    public static final String BMQXDQ = "70001";
    public static final String CXCSR = "80001";
    public static final String CXCSY = "80002";
    public static final String CXCSN = "80003";
    public static final String ZSCODEZERO = "80100";
    public static final String ZSCODEONE = "80101";
    public static final String ZSCODETWO = "80102";
    public static final String ZSCODETHREE = "80103";
    public static final String ZSCODEFOUR = "80104";
    public static final String ZSCODEFIVE = "80105";
    public static final String WDYFZJL = "80106";
    public static final String WDYSFD = "80107";
    public static final Map<Object, String> RESP_INFO = new HashMap();

    public CodeUtil() {
    }

    static {
        RESP_INFO.put("1111", "输入值存在非法字符");
        RESP_INFO.put("6666", "用户需要重新登录");
        RESP_INFO.put("4444", "解密错误");
        RESP_INFO.put("2333", "无权操作");
        RESP_INFO.put("2334", "保存数据失败");
        RESP_INFO.put("2335", "更新数据失败");
        RESP_INFO.put("2336", "删除数据失败");
        RESP_INFO.put("0000", "成功");
        RESP_INFO.put("0201", "该CA已经绑定过");
        RESP_INFO.put("0202", "CA证书不存在");
        RESP_INFO.put("0203", "CA绑定部门和用户所在部门不一致");
        RESP_INFO.put("0204", "CA绑定类型出错");
        RESP_INFO.put("0205", "该用户已经绑定过CA");
        RESP_INFO.put("0001", "参数为空");
        RESP_INFO.put("0002", "AES加密错误");
        RESP_INFO.put("0003", "token无效");
        RESP_INFO.put("0004", "参数错误");
        RESP_INFO.put("0005", "程序执行出错");
        RESP_INFO.put("0006", "数字签名不一致");
        RESP_INFO.put("0007", "data参数为空或json转对象错误");
        RESP_INFO.put("0008", "参数转对象失败");
        RESP_INFO.put("0009", "微信openid为空");
        RESP_INFO.put("0010", "来源与token来源不一致");
        RESP_INFO.put("0011", "获取共享数据失败");
        RESP_INFO.put("0012", "日期格式不正确");
        RESP_INFO.put("0013", "字典数据为空");
        RESP_INFO.put("0014", "获取token失败");
        RESP_INFO.put("0015", "http请求失败");
        RESP_INFO.put("0016", "发送短信过频，稍后再试");
        RESP_INFO.put("0017", "视频保存出错");
        RESP_INFO.put("0018", "音频保存出错");
        RESP_INFO.put("0019", "密码复杂度低，请输入8-20位同时包含数字，大小写英文的密码");
        RESP_INFO.put("0020", "获取access_token失败");
        RESP_INFO.put("0021", "access_token不能为空");
        RESP_INFO.put("0022", "access_token已过期");
        RESP_INFO.put("0023", "无效的access_token");
        RESP_INFO.put("0024", "配置不存在,请联系管理员");
        RESP_INFO.put("0025", "类型转换异常");
        RESP_INFO.put("0026", "资源不存在");
        RESP_INFO.put("0027", "资源非法");
        RESP_INFO.put("0028", "未获取到资源");
        RESP_INFO.put("0031", "您无权操作角色");
        RESP_INFO.put("0032", "您无权操作组织");
        RESP_INFO.put("0033", "您无权操作用户");
        RESP_INFO.put("0034", "角色非法");
        RESP_INFO.put("0035", "部门非法");
        RESP_INFO.put("0036", "不可删除管理角色");
        RESP_INFO.put("0037", "请求用户ID或者被操作人ID为空");
        RESP_INFO.put("0038", "角色已经存在");
        RESP_INFO.put("0039", "部门已经存在");
        RESP_INFO.put("0040", "部门不存在");
        RESP_INFO.put("0041", "角色不存在");
        RESP_INFO.put("0042", "不能自己作为自己的父部门");
        RESP_INFO.put("0043", "新建部门时父部门必传");
        RESP_INFO.put("0044", "获取部门信息失败");
        RESP_INFO.put("0045", "请用CA登录");
        RESP_INFO.put("0046", "资源菜单不合法");
        RESP_INFO.put("0047", "数据可见范围字典项非法");
        RESP_INFO.put("0048", "微信openID已存在");
        RESP_INFO.put("0049", "真实姓名为空");
        RESP_INFO.put("0050", "数据已存在");
        RESP_INFO.put("0051", "该人员新增窗口和登记中心与历史数据不一致");
        RESP_INFO.put("0052", "不动产权证号参数为空");
        RESP_INFO.put("0053", "不动产单元编号参数为空");
        RESP_INFO.put("0054", "不动产单元号参数为空");
        RESP_INFO.put("0055", "项目id参数为空");
        RESP_INFO.put("0056", "第三方接口执行失败");
        RESP_INFO.put("0057", "高拍仪开启失败");
        RESP_INFO.put("0058", "serviceId参数错误");
        RESP_INFO.put("0101", "密码过于简单");
        RESP_INFO.put("0102", "原密码新密码相同");
        RESP_INFO.put("1001", "用户名为空");
        RESP_INFO.put("1002", "密码为空");
        RESP_INFO.put("1003", "不存在该用户名");
        RESP_INFO.put("10031", "会签人员账户未全部注册");
        RESP_INFO.put("10032", "已婚人员家庭成员必填");
        RESP_INFO.put("10033", "家庭成员名称必填");
        RESP_INFO.put("10034", "家庭成员证件号必填");
        RESP_INFO.put("10035", "家庭成员关系必填");
        RESP_INFO.put("10036", "微信、支付宝不允许机构或者管理员登录");
        RESP_INFO.put("10037", "婚姻状态校验错误");
        RESP_INFO.put("10038", "房屋套次校验错误");
        RESP_INFO.put("1004", "密码错误");
        RESP_INFO.put("1005", "电话号码为空");
        RESP_INFO.put("1006", "证件号为空");
        RESP_INFO.put("1007", "角色为空");
        RESP_INFO.put("1008", "登录名已存在");
        RESP_INFO.put("1009", "证件号已存在");
        RESP_INFO.put("1010", "不存在该用户");
        RESP_INFO.put("1011", "登录名和密码不为空");
        RESP_INFO.put("1012", "电话号码已存在，请用手机号验证码登录并修改密码");
        RESP_INFO.put("1013", "该登录名有多个用户，请联系管理员");
        RESP_INFO.put("1014", "用户被禁用");
        RESP_INFO.put("1016", "用户或邮箱不存在");
        RESP_INFO.put("1017", "用户为非管理员");
        RESP_INFO.put("1018", "密码输入错误次数过多");
        RESP_INFO.put("1019", "角色必须是普通用户");
        RESP_INFO.put("1020", "用户名密码错误");
        RESP_INFO.put("1021", "用户名已存在");
        RESP_INFO.put("1022", "用户GUID为空");
        RESP_INFO.put("1023", "用户登录错误次数过多,帐号已锁定");
        RESP_INFO.put("1024", "保存用户失败");
        RESP_INFO.put("1025", "机构名称已存在");
        RESP_INFO.put("1026", "获取用户信息失败");
        RESP_INFO.put("1027", "CA认证失败");
        RESP_INFO.put("1028", "用户角色信息为空");
        RESP_INFO.put("1029", "至少六位数字或字母");
        RESP_INFO.put("1030", "至少八位数字与字母组合");
        RESP_INFO.put("1031", "至少八位数字与字母组合,必须包含大写字母 ");
        RESP_INFO.put("1032", "至少八位数字大写字母、小写字母、数字和符号");
        RESP_INFO.put("2001", "未查询到结果");
        RESP_INFO.put("2002", "申请信息为空,请重新申请");
        RESP_INFO.put("20020", "申请信息更新主键为空");
        RESP_INFO.put("20021", "受理编号为空");
        RESP_INFO.put("20022", "申请信息申请类型为空");
        RESP_INFO.put("20023", "申请信息不动产类型为空");
        RESP_INFO.put("20024", "申请信息创建用户为空或者不存在");
        RESP_INFO.put("20025", "管理员不允许创建申请信息");
        RESP_INFO.put("20026", "证书号为空");
        RESP_INFO.put("20027", "申请信息坐落为空");
        RESP_INFO.put("20028", "申请信息不动产单元号为空,请联系业务管理员,线下办理");
        RESP_INFO.put("20029", "申请信息面积为空");
        RESP_INFO.put("20030", "申请信息不动产权证号/房产证号为空,请联系业务管理员,线下办理");
        RESP_INFO.put("200301", "该不动产权证号已经创建过信息");
        RESP_INFO.put("200302", "该不动产单元号已经创建过信息");
        RESP_INFO.put("200210", "申请信息更新主键为空");
        RESP_INFO.put("200211", "申请信息不存在");
        RESP_INFO.put("200212", "申请信息已经存在");
        RESP_INFO.put("200213", "登记系统查询异常,请联系管理员");
        RESP_INFO.put("200214", "登记系统TOKEN异常,请联系管理员");
        RESP_INFO.put("200215", "抵押申请推送失败");
        RESP_INFO.put("200216", "该房产处于查封状态,不允许抵押");
        RESP_INFO.put("200217", "该房产处于查封状态");
        RESP_INFO.put("200218", "该房产处于抵押状态");
        RESP_INFO.put("200219", "买卖合同编号为空,请联系业务管理员");
        RESP_INFO.put("200220", "证书来源不能为空,请联系业务管理员,线下办理");
        RESP_INFO.put("200221", "抵押证明号不允许为空,请联系业务管理员,线下办理");
        RESP_INFO.put("200222", "申请信息更新失败");
        RESP_INFO.put("200223", "抵押证明号不是当前用户,请联系业务管理员,线下办理");
        RESP_INFO.put("200224", "登记创建业务流程失败，请联系管理员");
        RESP_INFO.put("200225", "该抵押证明已经进行抵押注销申请");
        RESP_INFO.put("200226", "产权证不匹配");
        RESP_INFO.put("200227", "申请类型不存在");
        RESP_INFO.put("200228", "申请信息不可撤回");
        RESP_INFO.put("200229", "申请信息不可删除");
        RESP_INFO.put("200230", "申请信息受理状态异常");
        RESP_INFO.put("200231", "申请信息不可补录附件");
        RESP_INFO.put("200232", "该合同存在查封或抵押信息，请重新确认");
        RESP_INFO.put("200233", "业务系统受理编号为空");
        RESP_INFO.put("200234", "请用本人或本部门用户登陆查询");
        RESP_INFO.put("200235", "该业务需到线下窗口办理");
        RESP_INFO.put("200236", "不存在印制号");
        RESP_INFO.put("200237", "自动办结失败");
        RESP_INFO.put("2003", "权利人为空");
        RESP_INFO.put("20031", "权利人名称为空");
        RESP_INFO.put("20032", "权利人证件种类为空");
        RESP_INFO.put("20033", "权利人证件号为空");
        RESP_INFO.put("20034", "权利人更新主键为空");
        RESP_INFO.put("20035", "权利人更新sqid为空");
        RESP_INFO.put("20036", "权利人和用户不匹配，请勿匹配他人的身份信息");
        RESP_INFO.put("20037", "权利人权利比例有误");
        RESP_INFO.put("20038", "义务人权利比例有误");
        RESP_INFO.put("20039", "权利人与义务人完全一致");
        RESP_INFO.put("20040", "权利人名称证件号验证失败");
        RESP_INFO.put("2004", "附件项目为空");
        RESP_INFO.put("20041", "附件数据为空");
        RESP_INFO.put("20042", "附件项目中附件类型不能为空");
        RESP_INFO.put("20043", "未查询到会签信息");
        RESP_INFO.put("20044", "存在未核验抵押人,提醒其核验后方可推送");
        RESP_INFO.put("20045", "共有方式错误，请重新核验");
        RESP_INFO.put("20046", "该不动产单元已核税");
        RESP_INFO.put("2005", "附件信息为空");
        RESP_INFO.put("20051", "附件图片流不能为空");
        RESP_INFO.put("20052", "附件名称不能为空");
        RESP_INFO.put("20053", "附件上传人不能为空");
        RESP_INFO.put("20054", "附件图片保存失败");
        RESP_INFO.put("20070", "附件大小超过限制");
        RESP_INFO.put("20100", "附件支持类型为空,请联系管理元");
        RESP_INFO.put("20101", "不支持该附件类型，请重新上传附件");
        RESP_INFO.put("20102", "附件不合格,请上传有效附件");
        RESP_INFO.put("200103", "申请信息需要预审但是尚未预审通过");
        RESP_INFO.put("20055", "EXCEL上传异常");
        RESP_INFO.put("20056", "您不符合申请条件，请核实后重新尝试");
        RESP_INFO.put("20057", "问询笔录录入失败");
        RESP_INFO.put("20058", "题库查询失败,或不存在该申请类型(权利人)题库");
        RESP_INFO.put("20059", "问询笔录录入异常,请核对信息是否正确");
        RESP_INFO.put("20060", "更新人脸识别认证状态失败");
        RESP_INFO.put("200600", "您的物权共有方式选择有误请重新选择");
        RESP_INFO.put("2006", "预约信息不存在");
        RESP_INFO.put("20061", "预约创建人（yyrbs）为空");
        RESP_INFO.put("20062", "预约人名称为空");
        RESP_INFO.put("20063", "预约人证件号为空");
        RESP_INFO.put("20064", "预约人联系电话为空");
        RESP_INFO.put("20065", "预约登记中心为空");
        RESP_INFO.put("20066", "预约登记中心不存在");
        RESP_INFO.put("20067", "预约时段为空");
        RESP_INFO.put("20068", "预约时段不存在");
        RESP_INFO.put("20069", "预约登记类型为空");
        RESP_INFO.put("200610", "预约登记类型不存在");
        RESP_INFO.put("200611", "预约来源不为空");
        RESP_INFO.put("200612", "该用户当天已预约，不可再预约");
        RESP_INFO.put("200613", "用户当前已存在该登记类型的预约，不可再预约");
        RESP_INFO.put("200614", "同一身份证号，${within_qxts}天之内取消满${canceltimes}次，${future_days}天之内不得在预约");
        RESP_INFO.put("200615", "预约创建人不存在");
        RESP_INFO.put("200616", "预约时间为空");
        RESP_INFO.put("200617", "当天预约取消次数达到上限");
        RESP_INFO.put("200618", "已存在预约信息");
        RESP_INFO.put("200619", "预约日期为节假日");
        RESP_INFO.put("200620", "获取日期失败");
        RESP_INFO.put("200621", "预约时段已满");
        RESP_INFO.put("200622", "预约发生错误，请刷新页面");
        RESP_INFO.put("200623", "不可以预约当天,请重新选择预约日期");
        RESP_INFO.put("200624", "未查询到部门代码");
        RESP_INFO.put("200625", "当前用户已加入预约黑名单，若需继续办理预约申请，请联系管理员");
        RESP_INFO.put("200626", "预约申请类型为空");
        RESP_INFO.put("200627", "一周有效预约不可以超过两次");
        RESP_INFO.put("200628", "该手机号当天当前预约日期已经预约过，请重新选择");
        RESP_INFO.put("200629", "当前预约日期错误");
        RESP_INFO.put("20071", "获取不到缴费信息");
        RESP_INFO.put("20072", "该业务已完成支付");
        RESP_INFO.put("20073", "生成订单失败");
        RESP_INFO.put("20074", "商户信息为空");
        RESP_INFO.put("20075", "缴费成功后的通知地址为空");
        RESP_INFO.put("20076", "订单参数为空");
        RESP_INFO.put("20077", "验证签名不通过");
        RESP_INFO.put("20078", "支付请求失败");
        RESP_INFO.put("20079", "付费方式为空");
        RESP_INFO.put("200710", "付费方式错误");
        RESP_INFO.put("200711", "支付方式为空");
        RESP_INFO.put("200712", "支付方式错误");
        RESP_INFO.put("200713", "订单不存在");
        RESP_INFO.put("200714", "订单金额有误");
        RESP_INFO.put("200715", "该用户无相关银行卡信息");
        RESP_INFO.put("200716", "银行卡信息为空");
        RESP_INFO.put("200717", "订单已缴费无法关闭");
        RESP_INFO.put("200718", "该银行卡已绑定，请选择其他银行卡");
        RESP_INFO.put("200719", "绑定银行卡失败");
        RESP_INFO.put("200720", "支付密码错误");
        RESP_INFO.put("200721", "支付失败");
        RESP_INFO.put("200722", "银行卡解绑失败");
        RESP_INFO.put("200723", "您有处于激活中的订单或该订单已完成缴费，请前往订单列表中进行查看");
        RESP_INFO.put("200724", "订单未支付成功");
        RESP_INFO.put("200725", "缴费信息推送失败");
        RESP_INFO.put("200726", "该缴费项无法缴费");
        RESP_INFO.put("200727", "该受理编号不存在缴费信息");
        RESP_INFO.put("200730", "退款失败");
        RESP_INFO.put("200731", "合计金额错误");
        RESP_INFO.put("200732", "不存在退款信息");
        RESP_INFO.put("200733", "未查询到税务信息");
        RESP_INFO.put("200734", "获取税务信息异常");
        RESP_INFO.put("200735", "未查询到税务信息明细");
        RESP_INFO.put("200736", "查询退款信息出错");
        RESP_INFO.put("200740", "未查询到缴费人信息");
        RESP_INFO.put("200741", "纳税人识别号为空");
        RESP_INFO.put("200742", "推送税务失败");
        RESP_INFO.put("20081", "验证码错误");
        RESP_INFO.put("20082", "短信发送发生错误");
        RESP_INFO.put("20091", "证书查询受理编号为空");
        RESP_INFO.put("20092", "证书查询输入的权利人与扫描结果不一致");
        RESP_INFO.put("20093", "申请处于正在审核中，不可以再次申请");
        RESP_INFO.put("20094", "该产权未落宗或未匹配不动产单元号，请至中心落宗或者匹配不动产单元号");
        RESP_INFO.put("20095", "该产权匹配多个不动产单元号");
        RESP_INFO.put("20010", "暂无服务网点信息");
        RESP_INFO.put("30009", "上传证件和填写信息不一致,请重新上传证件!");
        RESP_INFO.put("30008", "身份验证异常");
        RESP_INFO.put("110001", "活体检测失败");
        RESP_INFO.put("110002", "人脸比对失败");
        RESP_INFO.put("110003", "人脸对比阈值过低");
        RESP_INFO.put("110004", "人脸比对获取结果错误");
        RESP_INFO.put("110005", "人脸比对二维码超时失效");
        RESP_INFO.put("110006", "人脸比对二维码查询结果为空，继续轮询");
        RESP_INFO.put("110007", "身份认证已完成");
        RESP_INFO.put("110008", "人脸核身业务类型为空");
        RESP_INFO.put("110009", "人脸核身使用次数超过限制");
        RESP_INFO.put("110010", "阿里人脸请求错误");
        RESP_INFO.put("110011", "阿里的人脸认证查询结果不正确");
        RESP_INFO.put("110012", "身份未认证，请前往支付宝进行认证");
        RESP_INFO.put("110013", "暂不支持个人以外的身份类型");
        RESP_INFO.put("12001", "需要重定向");
        RESP_INFO.put("12002", "政务一张网用户主键缺失");
        RESP_INFO.put("12003", "用户必须先完成实名认证");
        RESP_INFO.put("12004", "需要重新选择企业类型");
        RESP_INFO.put("210001", "服务已被评价过，不可再次评价");
        RESP_INFO.put("220001", "微信公众号通知信息推送失败");
        RESP_INFO.put("220002", "微信公众号信息通知模板id为空");
        RESP_INFO.put("220003", "微信公众号信息通知模板内容为空");
        RESP_INFO.put("230001", "视频问询声明数据不存在");
        RESP_INFO.put("230002", "视频问询声明中声明人为空");
        RESP_INFO.put("230003", "申请类型为空");
        RESP_INFO.put("230004", "视频问询原因为空");
        RESP_INFO.put("240001", "申请类型主键不存在");
        RESP_INFO.put("250001", "申请类型重复");
        RESP_INFO.put("250002", "CA字典项非法");
        RESP_INFO.put("250003", "字典项非法");
        RESP_INFO.put("260001", "一窗受理创建失败");
        RESP_INFO.put("260002", "一窗受理附件接收失败");
        RESP_INFO.put("260010", "public创建申请失败");
        RESP_INFO.put("260011", "public接受附件失败");
        RESP_INFO.put("270001", "获取UC用户信息失败");
        RESP_INFO.put("28001", "获取initCode失败");
        RESP_INFO.put("28002", "获取authCode失败");
        RESP_INFO.put("28003", "获取accessToken失败");
        RESP_INFO.put("28004", "获取userAccessToken失败");
        RESP_INFO.put("28005", "获取userInfo失败");
        RESP_INFO.put("29001", "微信快捷登录失败");
        RESP_INFO.put("29002", "微信快捷登录需要补充身份信息");
        RESP_INFO.put("30001", "验证江苏政务网ticket失败");
        RESP_INFO.put("30002", "验证江苏政务网token失败");
        RESP_INFO.put("30003", "获取政务网用户失败");
        RESP_INFO.put("30004", "暂不支持法人快捷登录");
        RESP_INFO.put("40001", "查询他人权利信息次数超限");
        RESP_INFO.put("50001", "公示公告信息推送门户网站失败");
        RESP_INFO.put("60001", "");
        RESP_INFO.put("60002", "未查询到合同信息");
        RESP_INFO.put("60003", "未查询到房屋信息");
        RESP_INFO.put("60004", "房屋已办理产权信息");
        RESP_INFO.put("60005", "合同不存在");
        RESP_INFO.put("61001", "获取缴费明细失败");
        RESP_INFO.put("61002", "生成缴费订单失败");
        RESP_INFO.put("61003", "查询订单信息失败");
        RESP_INFO.put("70001", "该银行部门权限已到期，不允许登入，请与不动产登记中心联系");
        RESP_INFO.put("80001", "当日查询次数已达上限，请明日再试");
        RESP_INFO.put("80002", "当月查询次数已达上限，请与不动产登记中心联系");
        RESP_INFO.put("80003", "当年查询次数已达上限，请与不动产登记中心联系");
        RESP_INFO.put("80100", "接口参数为空或处理异常");
        RESP_INFO.put("80101", "印制号不存在或被删除");
        RESP_INFO.put("80102", "印制号存在，但是已经被使用");
        RESP_INFO.put("80103", "印制号存在、未使用，但是该印制号不在当前用户权限下");
        RESP_INFO.put("80104", "印制号存在、未使用，且在当前用户名下，可使用");
        RESP_INFO.put("80105", "该证书已经在登记系统打证");
        RESP_INFO.put("80106", "请先完成发证记录打印操作");
        RESP_INFO.put("80107", "请先完成收费单打印操作");
        RESP_INFO.put("9999", "");
    }
}