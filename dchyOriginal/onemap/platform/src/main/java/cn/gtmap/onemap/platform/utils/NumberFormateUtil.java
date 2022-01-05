package cn.gtmap.onemap.platform.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberFormateUtil {
	public static String doubleToDoubleDotN(BigDecimal doubleValue, int N,
			boolean ForceToN) {
		if (doubleValue == null) {
			return "";
		}
		return doubleToDoubleDotN(doubleValue.doubleValue(), N, ForceToN, false);
	}

	/**
	 * @param doubleValue
	 *            要格式化的值
	 * @param N
	 *            保留几位小数
	 * @param ForceToN
	 *            是否强制添0
	 * @param ReturnZero
	 *            值为零时，false返回"",true返回"0"
	 * @return
	 * @作者 王建明
	 * @创建日期 2012-9-27
	 * @创建时间 下午04:08:32
	 * @描述 ——
	 */
	public static String doubleToDoubleDotN(double doubleValue, int N,
			boolean ForceToN, boolean ReturnZero) {
		String ret = String.valueOf(doubleValue);
		boolean IsZero = doubleValue == 0.0D;
		try {
			BigDecimal b = new BigDecimal(Double.toString(doubleValue));
			BigDecimal one = new BigDecimal("1");
			double aa = b.divide(one, N, 4).doubleValue();
			StringBuffer sb = new StringBuffer();
			sb.append("###");
			if (N > 0) {
				sb.append(".");
				for (int i = 1; i <= N; ++i) {
					sb.append("#");
				}
			}

			DecimalFormat df = new DecimalFormat(sb.toString());
			String tempValue = df.format(aa);

			ret = tempValue;
			if ((ForceToN) && (N > 0)) {
				if (ret.indexOf(".") < 0) {
					ret = ret + ".0";
				}
				while (ret.substring(ret.indexOf(".") + 1, ret.length())
						.length() < N) {
					ret = ret + "0";
				}
			}
		} catch (Exception e) {
			ret = String.valueOf(doubleValue);
		}
		if (!(ForceToN)) {
			while (ret.endsWith("0")) {
				ret = ret.substring(0, ret.length() - 1);
			}
			if (ret.endsWith(".")) {
				ret = ret.substring(0, ret.length() - 1);
			}
		}
		if ((IsZero) && (!(ReturnZero))) {
			return "";
		}
		if ((IsZero) && (ReturnZero)) {
			return "0";
		}
		return ret;
	}

	public static String doubleToDoubleDot4(double doubleValue) {
		return doubleToDoubleDotN(doubleValue, 4, false, false);
	}

	public static String doubleToDoubleDot2(double doubleValue) {
		return doubleToDoubleDotN(doubleValue, 2, false, false);
	}

	/**
	 * @param doubleIn
	 * @return
	 * @作者 王建明
	 * @创建日期 2013-06-19
	 * @创建时间 15:31:06
	 * @描述 —— 过滤空的double类型数据
	 */
	public static Double filterNullDouble(Double doubleIn) {
		if (doubleIn == null)
			return 0.0;
		return doubleIn;
	}

	public static Integer filterNullInt(Integer intValue) {
		if (intValue == null)
			return 0;
		return intValue;
	}

	public static BigDecimal StringToBigDecimal(String InputValue) {
		if (InputValue == null)
			return null;
		InputValue = InputValue.trim();
		if (InputValue.length() == 0)
			return null;
		BigDecimal temp = null;
		try {
			temp = new BigDecimal(InputValue);
		} catch (Exception ex) {
			return null;
		}
		return temp;
	}

	public static String formatNumber(String value, Integer n) {
		if ((value == null) || (value.trim().equals(""))) {
			return "";
		}
		return doubleToDoubleDotN(Double.parseDouble(value), n, true, true);
	}

	public static String formatNumber(double value, int n) {
		return doubleToDoubleDotN(value, n, true, true);
	}

	public static String formatNumber(String value) {
		return doubleToDoubleDotN(Double.parseDouble(value), 4, false, true);
	}

	public static String formatNumber(double value) {
		return doubleToDoubleDotN(value, 4, true, true);
	}

	public static String toNumber(double value, int n) {
		return doubleToDoubleDotN(value, n, true, false);
	}

	public static String toNumber(Double value, int n) {
		if (value == null)
			return "";
		return toNumber(value.doubleValue(), n);
	}

	public static String toNumber(Float value, int n) {
		if (value == null)
			return "";
		return toNumber(value.doubleValue(), n);
	}

	public static String toNumber(BigDecimal value, int n) {
		if (value == null)
			return "";
		return toNumber(value.doubleValue(), n);
	}

	public static String toNumber(Long value, int n) {
		if (value == null)
			return "";
		return toNumber(value.doubleValue(), n);
	}

	public static String toNumberTwoFigures(Integer value) {
		if (value == null)
			return "";
		if (value < 10 && value >= 0) {
			return "0" + value;
		}
		return String.valueOf(value);
	}
	
	/**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    public static String intToChinese(BigDecimal num){
    	double n = num.doubleValue();
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"万元", "万", "亿"},
                     {"", "拾", "佰", "仟"}};
 
        String head = n < 0? "负": "";
        n = Math.abs(n);
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";    
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
    
    /**
     * 两个Double数相除，并保留scale位小数
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    public static Double div(Double v1,Double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
            "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 两个Double数相除，返回百分比
     * @param v1
     * @param v2
     * @param scale
     * @return String
     */
    public static String getPercent(Double v1,Double v2,int scale){
    	if(scale<0){
            throw new IllegalArgumentException(
            "The scale must be a positive integer or zero");
        }
        if(v2 == 0){
            return "0.0%";
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        double per =  ((b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(100)).doubleValue());
        return per + "%";
    }
    
}
