package com.service.guarantee.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonFunction {
    public static final char UNDERLINE = '_';

    /**
     * 异常路径追踪
     *
     * @param e
     * @return
     */
    public static String GetErrorStack(Throwable e) {
        StackTraceElement[] stackElements = e.getStackTrace();
        String error = "";
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                error += stackElements[i].getClassName() + " ";
                error += stackElements[i].getFileName() + " ";
                error += stackElements[i].getLineNumber() + " ";
                error += stackElements[i].getMethodName() + "\r\n";
            }
        }
        error += "GetErrorStack Message:" + e.getMessage();
        return error;
    }

    /**
     * 判断给定字符串是否满足GUID规则
     *
     * @param eid
     * @return
     */
    public static boolean isGuidByReg(String eid) {
        String regEx = "^[a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}$";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(eid);
        return matcher.matches();
    }

    /**
     * 将字符串中的下划线转成驼峰形式
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {

        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
