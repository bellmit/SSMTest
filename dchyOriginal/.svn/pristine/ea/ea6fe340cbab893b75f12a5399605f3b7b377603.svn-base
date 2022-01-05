package cn.gtmap.msurveyplat.promanage.web.utils;

public class XmbhFormatUtil {

    /**
     * 不足6位，后面补0
     * @return
     */
    /**
     * 在字符串后面追加 0，示例：str 为 1, length 为 4, 则为 "1000"
     *
     * @param str 被处理的字符串
     * @param length 处理之后的位数
     * @return
     */
    public static String numberAfterFillZero(String str, int length) {
        StringBuffer buffer = new StringBuffer(str);
        if (buffer.length() >= length) {
            return buffer.toString();
        } else {
            while (buffer.length() < length) {
                buffer.append("0");
            }
        }
        return buffer.toString();
    }
}
