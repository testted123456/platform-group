package com.nonobank.group.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * Created by tangrubei on 2018/3/2.
 */
public class CharsUtil {


    private static final String SET_PREFIX = "set";


    //
    /**
     * 首字母大写
     */
    public static Function<String, String> functionFirstLetterUp = inputstr -> {
        char[] chs = inputstr.toCharArray();
        if (chs[0] >= 'a' && chs[0] <= 'z') {
            chs[0] -= 32;
        }
        return String.valueOf(chs);
    };

    /**
     * 对字符进行分割，并获取对应的索引的结果
     *
     * @param orginStr 数据字符串
     * @param regex    分割标示
     * @param index    获取索引
     * @return
     */
    public static String strSplitGetIndex(String orginStr, String regex, int index) {
        if (orginStr == null) {
            return null;
        }
        if (regex == null) {
            return null;
        }
        String[] substrs = orginStr.split(regex);
        if (index < 0 || index >= substrs.length) {
            return null;
        }

        return substrs[index];
    }



    /**
     * 添加set 属性
     * @param name
     * @return
     */
    public static String createSetMethodName(String name) {
        Objects.requireNonNull(name);
        return SET_PREFIX + name;
    }


    public static void main(String[] args) {
//        System.out.println(str_split_get_index("aa=bb", "=", 0));
    }

}
