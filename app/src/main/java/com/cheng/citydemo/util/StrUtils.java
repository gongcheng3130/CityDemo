package com.cheng.citydemo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtils {

    //禁止实例化
    private StrUtils(){}

    /**
     * 判断字符串
     * 字符串为空，返回true
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if ("".equals(str) || str == null || "null".equals(str) || "(null)".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串
     * 字符串为空，返回true
     *
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        if ("".equals(str) || str == null || "null".equals(str) || "(null)".equals(str)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 去掉字符串中所有空格
     * @param str
     * @return String
     */
    public static String StrTrim(String str){
        if(isNull(str)){
            return "";
        }else{
            String b = "";
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                if(isNotNull(charArray[i]+"")){
                    b = b + charArray[i];
                }
            }
            return b;
        }
    }

    /**
     * 判断字符串是否相同
     * @param a
     * @param b
     * @return boolean
     */
    public static boolean isSame(String a, String b){
        if(isNull(a)){
            if(isNull(b)){
                return true;
            }else{
                return false;
            }
        }else if(isNull(b)){
            if(isNull(a)){
                return true;
            }else{
                return false;
            }
        }else{
            return a.equals(b);
        }
    }

    /**
     * 判断字符串是否为中文
     * @param str
     * @return boolean
     */
    public static boolean isChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");;
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }

}
