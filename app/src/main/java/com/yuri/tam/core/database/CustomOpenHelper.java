package com.yuri.tam.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yuri.tam.databases.DaoMaster;

import org.greenrobot.greendao.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库升级操作
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年2月2日
 */
public class CustomOpenHelper extends DaoMaster.DevOpenHelper {
    private Logger mLog = LoggerFactory.getLogger(CustomOpenHelper.class.getSimpleName());

    public CustomOpenHelper(Context context, String name) {
        super(context, name);
    }

    public CustomOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //数据库更新
        mLog.debug("数据库版本信息，oldVersion:{}，newVersion:{}", oldVersion, newVersion);
        if (newVersion > oldVersion){
            mLog.debug("------------ 数据库更新 开始 -------------");
            //更改过的实体类(新增的不用加)更新UserDao文件 可以添加多个  XXDao.class 文件
            //MigrationHelper.getInstance().migrate(db, TransRecordDao.class,XXDao.class);
            mLog.debug("------------ 数据库更新 结束 -------------");
        }
    }
}
