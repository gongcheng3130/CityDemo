package com.cheng.citydemo.db;

import android.content.Context;
import com.cheng.citydemo.bean.City;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class MyDBHelp extends SQLiteOpenHelper {

    //数据库名称
    public static final String DB_NMAE = "CityDemo.db";
    //数据库版本，每对数据库需要做修改的时候，版本号加1
    public static final int DB_VERSION = 1;
    //数据库密码，加密使用，修改密码也只需要改这个字段的值，不需要做额外处理
    public static final String DB_PWD = "";//数据库密码，为空时等于没有加密

    public MyDBHelp(Context context){
        super(context, DB_NMAE, null, DB_VERSION);
        SQLiteDatabase.loadLibs(context);//不可忽略的 进行so库加载
    }

    //数据库创建或者获取时调用
    //此处一般作为当前版本数据库需要初始化的操作，比如创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBUtils.getCreateTableSql(City.class));
    }

    //版本升级时要做的操作，没有操作不需处理
    //此处一般作为修改上个版本数据库信息时需要改动的数据库字段等等
    //比如删除上个版本某个表，修改旧版本表中某个字段名等等
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
