package com.yuri.tam.core.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.common.utils.FileUtils;
import com.google.gson.Gson;
import com.yuri.tam.common.constant.SysConstant;
import com.yuri.tam.core.bean.UserInfo;
import com.yuri.tam.core.database.CustomOpenHelper;
import com.yuri.tam.databases.DaoMaster;
import com.yuri.tam.databases.DaoSession;
import com.yuri.tam.databases.UserInfoDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import java8.util.Optional;
import java8.util.function.Function;

/**
 * 数据操作接口实现
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年02月02日
 */
public class ApiRepository implements IDataSource {
    private Logger mLog = LoggerFactory.getLogger(ApiRepository.class.getSimpleName());

    private DaoSession mDaoSession;
    private Context mContext;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private ApiRepository(){}

    //创建单例模式
    private static class SingletonHolder {
        private static final ApiRepository INSTANCE = new ApiRepository();
    }

    //获取单例
    public static ApiRepository getInstance(){
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void initDataSource(Context context) {
        this.mContext = context;
        //数据库数据
        SQLiteDatabase db = new CustomOpenHelper(mContext, "yuri-tam.db", null)
                .getWritableDatabase();
        mDaoSession = new DaoMaster(db).newSession();
        //配置数据
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

//        initParams();
    }

    /**
     * 初始化应用参数
     */
    private void initParams(){
        try {
            if ("1".equals(getParamValue(SysConstant.PARAM_INIT_FLAG, "0"))){
                mLog.debug("--------------- 参数已经初始化 ----------------");
                return;
            }
            //从文件中读取参数
            AssetManager am = mContext.getAssets();
            InputStream is = am.open(SysConstant.PARAM_FILE_NAME);
            String paramJson = FileUtils.stream2String(is);
            if (is != null) is.close();
            Optional.ofNullable(paramJson)
                    .map((Function<String, Map<String, String>>) jsonStr -> new Gson().fromJson(jsonStr, Map.class))
                    .filter(params -> params.size() > 0)
                    .ifPresent(params -> {
                        mLog.debug("----- 开始初始化终端参数 -----");
                        //遍历所有参数
                        for (String key : params.keySet()) {
                            mLog.debug("{} = {}", key, params.get(key));
                            //如果该参数有值，则直接跳过
                            if (!TextUtils.isEmpty(getParamValue(key, ""))) continue;
                            //设置参数的值
                            setParamValue(key, params.get(key));
                        }
                        setParamValue(SysConstant.PARAM_INIT_FLAG, "1");
                        syncParamValue();
                        mLog.debug("----- 结束初始化终端参数 -----");
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setParamValue(String key, String value) {
        mLog.debug("保存数据 key = {}, value = {}", key, value);
        mEditor.putString(key, value);
    }

    @Override
    public String getParamValue(String key, String defValue) {
        String value = mSharedPreferences.getString(key, defValue);
        mLog.debug("获取数据 key = {}, defValue = {}, return value = {}", key, defValue, value);
        return TextUtils.isEmpty(value) ? defValue : value;
    }

    @Override
    public void syncParamValue() {
        mEditor.apply();
        try {
            Runtime.getRuntime().exec("sync");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(UserInfo info) {
        mDaoSession.getUserInfoDao().insert(info);
    }

    @Override
    public UserInfo getUserInfo(long userId) {
        return mDaoSession.getUserInfoDao()
                .queryBuilder()
                .where(UserInfoDao.Properties.Id.eq(userId))
                .build()
                .unique();
    }

    @Override
    public void updateUserInfo(UserInfo info) {
        mDaoSession.getUserInfoDao().update(info);
    }

    @Override
    public void deleteByUserId(long userId) {
        mDaoSession.getUserInfoDao().deleteByKey(userId);
    }

    @Override
    public void deleteAllUserInfo() {
        mDaoSession.getUserInfoDao().deleteAll();
    }

}
