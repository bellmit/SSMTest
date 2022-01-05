package cn.gtmap.onemap.platform.utils;


import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * .
 *
 * @author <a href="mailto:lanxy88@gmail.com">NelsonXu</a>
 * @version V1.0, 13-9-12 上午9:06
 */
public class PattenTest {

    @Test
    public void testPatten() {
        String source = "DLTB_H_2011\n" +
                "DLTB_H_2011\n" +
                "DLTB_E_2011\n" +
                "TDLY.XZDW_H_2011\n" +
                "TDLY.XZQ_H_2011\n" +
                "TDLY.ZD_H_2011\n" +
                "TDLY.JBNTBHPK_H_2011\n" +
                "TDLY.JBNTBHPK_H_20112\n" +
                "TDLY.JBNTBHTB_H_2011\n";

//        Pattern pattern = Pattern.compile("(TDYTQ|GHJBNTBHQ|JSYDGZQ|MZZDJSXM)_[H|E]_//d+");
        /*Matcher matcher = Pattern.compile("(TDYTQ|GHJBNTBHQ|JSYDGZQ|MZZDJSXM)_[H|E]_\\d+", Pattern.MULTILINE).matcher(source);
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                String s = matcher.group(i);
                System.out.println(s);
            }
        }*/

        Matcher matcher = Pattern.compile("((\\w+)\\.)?(DLTB|XZDW|ZD|JBNTBHPK|JBNTBHTB)_[H|E]_\\d+$").matcher("tdly.DLtB_H_2011".toUpperCase());
        boolean m = matcher.matches();
        while (matcher.find()){
            System.out.println(matcher.group(0));
        }
    }

}
