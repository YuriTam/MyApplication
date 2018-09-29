package com.yuri.tam.common.constant;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.util.Currency;
import java.util.Locale;

/**
 * 系统常量
 *
 * @author 谭忠扬-YuriTam
 * @time 2015年10月10日
 */
public class SysConstant {

    //编码格式
    public static final String UTF_8 = "UTF-8";
    public static final String UTF_16 = "UTF-16";
    public static final String GB_2312 = "GB2312";
    public static final String GBK = "GBK";
    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String ASCII = "ASCII";
    public static final String GB18030 = "GB18030";

    //时间（秒）
    public static final int TIME_OUT_5 = 5;
    public static final int TIME_OUT_10 = 10;
    public static final int TIME_OUT_15 = 15;
    public static final int TIME_OUT_20 = 20;
    public static final int TIME_OUT_30 = 30;
    public static final int TIME_OUT_60 = 60;

    //1000*60*60*24 一天毫秒数
    public static final long ONE_DAY_MILLS = 86400000;
    //时间解析格式
    public static final String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_FORMAT = "yyyyMMdd";
    public static final String TIME_FORMAT_DEFAULT = "HH:mm:ss";
    public static final String TIME_FORMAT = "HHmmss";
    public static final String RFC_3339_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    //管理员默认密码
    public static final String DEFAULT_PASSWORD = "12345678";

    //SD卡路径
    public static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    //日志保存路径
    public static final String LOG_PATH = BASE_PATH + File.separator + "AppLog" + File.separator;
    //异常信息保存路径
    public static final String CRASH_PATH = BASE_PATH + File.separator + "crash_log" + File.separator;
    //下载路径
    public static final String DOWNLOAD_PATH = BASE_PATH + File.separator + "download" + File.separator;
    //数据库路径
//    public static final String DB_PATH = BASE_PATH + File.separator + BuildConfig.APPLICATION_ID + File.separator;
    public static final String DB_PATH = "/private/config" + File.separator;
    //缓存目录
    public static final String CACHE_DIR = BASE_PATH + File.separator + "cache" + File.separator;
    //缓存大小
    public static final int CACHE_SIZE = 10 * 2014 *1024;
    //交易列表每页显示条数
    public static final int MAX_PAGE_SIZE = 20;
    //当前国家
    public static final Locale LOCALE = Locale.getDefault();
    //货币代码
    public static final String FUND_CODE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? String.valueOf(Currency.getInstance(LOCALE).getNumericCode()) : "156";
    //货币符号
    public static final String FUND_SYMBOL = Currency.getInstance(LOCALE).getSymbol();
    //货币小数位
    public static final int FUND_DIGITS = 2;
    //响应码正则表达式字符串
    public static final String RESP_CODE_REGEX = "^(A|a|[0-9])[0-9]$";
    //初始化参数的标识
    public static final String PARAM_INIT_FLAG = "param_init_flag";
    //初始化参数的文件名
    public static final String PARAM_FILE_NAME = "param.json";
    //每页请求数量
    public static final int PER_PAGE = 99;
}
