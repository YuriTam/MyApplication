package com.yuri.tam.common.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

/**
 * Created by xiaox on 2017/1/13.
 */
public class StringUtils {
    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    public static String formatAmount(String number, int digits) {
        if (TextUtils.isEmpty(number)) number = "0";
        NumberFormat format;
        format = NumberFormat.getInstance();
        format.setMinimumIntegerDigits(1);
        format.setMaximumFractionDigits(digits);
        format.setMinimumFractionDigits(digits);
        format.setGroupingUsed(false);
        return format.format(Double.valueOf(number) / Math.pow(10, digits));
    }
    /**
     * @param amount  转换为12位金额  eg: String 0.01  ---->  String 000000000001
     * @return
     */
    public static String getFormatAmount(String amount) {
        String amt = amount;
        if (TextUtils.isEmpty(amt)) amt = "0";
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
        format.setMinimumIntegerDigits(12);
        DecimalFormat df = new DecimalFormat("0.00");
        String tmp = df.format(Double.valueOf(amt)).replace(".", "");
        return format.format(Long.valueOf(tmp));
    }

    /**
     * 格式化日期时间
     * @param dateStr 日期｛格式：yyyyMMdd、MMdd｝
     * @param timeStr 时间｛格式：HHmmss｝
     * @return 格式化后的时间：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(String dateStr, String timeStr){
        String formatTime = "";
        if (TextUtils.isEmpty(dateStr) || TextUtils.isEmpty(timeStr)) return formatTime;
        if (!TextUtils.isEmpty(dateStr) && dateStr.length() == 4){
            dateStr = StringUtils.getCalenderField(Calendar.YEAR) + dateStr;
        }
        try {
            Date date = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).parse(dateStr + timeStr);
            formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatTime;
    }

    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        return mat.find();
    }

    public static String rightPad(String str, int size, char padChar) {
        StringBuilder padded = new StringBuilder(str == null ? "" : str);
        while (padded.length() < size) {
            padded.append(padChar);
        }
        return padded.toString();
    }

    public static String leftPad(String str, int size, char padChar) {
        StringBuilder padded = new StringBuilder(str == null ? "" : str);
        while (padded.length() < size) {
            padded.insert(0, padChar);
        }
        return padded.toString();
    }

    public static String trimLeft(String str, char chr) {
        StringBuilder sb = new StringBuilder(str == null ? "" : str);
        while (sb.toString().startsWith(String.valueOf(chr))) {
            sb.delete(0, 1);
        }
        return sb.toString();
    }

    public static String trimRight(String str, char chr) {
        StringBuilder sb = new StringBuilder(str == null ? "" : str);
        while (sb.toString().endsWith(String.valueOf(chr))) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * 空格分隔卡号
     *
     * @param pan
     * @return
     */
    public static String separateWithSpace(String pan) {
        if (pan == null)
            return null;

        String temp = "";
        int total = pan.length() / 4;
        for (int i = 0; i < total; i++) {
            temp += pan.substring(i * 4, i * 4 + 4);
            if (i != (total - 1)) {
                temp += " ";
            }
        }
        if (total * 4 < pan.length()) {
            temp += " " + pan.substring(total * 4, pan.length());
        }

        return temp;
    }


    /**
     * 获取日期中的相应值
     *
     * @param field 如：Calendar.YEAR
     * @return 返回相应值
     */
    public static int getCalenderField(int field) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(field);
    }

    /**
     * 返回第一个不为空的string
     *
     * @param first
     * @param second
     * @return
     */
    public static String firstNonEmpty(String first, String second) {
        if (TextUtils.isEmpty(first)) {
            if (TextUtils.isEmpty(second)) {
                throw new NullPointerException();
            } else {
                return second;
            }
        } else {
            return first;
        }
    }

    /**
     * 检查日期
     *
     * @param dateStr mmdd
     * @return
     */
    public static boolean checkDate(String dateStr){
        if (dateStr.length() != 4) {
            return false;
        }
        int m = parseInt(dateStr.substring(0, 2), 10);
        int d = parseInt(dateStr.substring(2, 4), 10);
        if (m > 12 || d > 31) {
            return false;
        }
        if (m <= 0 || d <= 0) {
            return false;
        }
        return true;
    }
}
