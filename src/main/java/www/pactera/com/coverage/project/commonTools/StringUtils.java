package www.pactera.com.coverage.project.commonTools;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class StringUtils {

    public static boolean isEmpty(Object o){
        if(o == null || o == ""){
            return true;
        }
        return false;
    }

    /**
     * 覆盖率换算为字符串格式的百分数
     * @param coverLine
     * @param totalLine
     * @return
     */
    public static String coverConversion(int coverLine,int totalLine){
        if(new BigDecimal(totalLine).equals(BigDecimal.ZERO)){
            return "";
        }
        BigDecimal  coverRate = new BigDecimal(coverLine).divide(new BigDecimal(totalLine),2,BigDecimal.ROUND_HALF_DOWN);
        NumberFormat percent = NumberFormat.getPercentInstance();
        return  percent.format(coverRate);
    }

}
