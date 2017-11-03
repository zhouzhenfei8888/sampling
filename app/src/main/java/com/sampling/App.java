package com.sampling;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import com.bill.ultimatefram.app.UltimateApplication;
import com.bill.ultimatefram.util.ExternalFileHelper;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sampling.Common.AssetsDatabaseManager;
import com.sampling.dao.DaoMaster;
import com.sampling.dao.DaoSession;

/**
 * Created by zzf on 17-10-23.
 */

public class App extends UltimateApplication {
    ExternalFileHelper.Builder builder;
    SQLiteDatabase sqLiteDatabase;
    public DaoMaster daoMaster;
    //这个是从asset中的数据库中的
    public DaoSession daoSession;
    //这个是新建的
    public DaoSession mdaoSession;


    public static App getInstance() {
        return (App) UltimateApplication.getAppInstance();
    }

    @Override
    protected void init() {
        /*外部文件存储*/
        builder = new ExternalFileHelper.Builder();
        builder.setFileFolderName("My_File");
        builder.setImageFolderName("My_Images");
        builder.setRootFolderName("My");
        ExternalFileHelper.getInstance().builder(builder);
        UltimatePreferenceHelper.getInstance();
        AssetsDatabaseManager.initManager(this);
        setDataBase();
    }


    private void setDataBase() {
        if (AssetsDatabaseManager.getManager() != null) {
            sqLiteDatabase = AssetsDatabaseManager.getManager().getDatabase("Foodsafe.db");
            daoMaster = new DaoMaster(sqLiteDatabase);
            daoSession = daoMaster.newSession();
        }
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "Sampling.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        mdaoSession = daoMaster.newSession();
    }

    @Override
    protected void buildImageOptions(DisplayImageOptions.Builder builder) {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        builder.showImageForEmptyUri(drawable).showImageOnFail(drawable).showImageOnLoading(drawable);
    }

    @Override
    protected void buildImageConfig(ImageLoaderConfiguration.Builder builder) {
        builder.memoryCacheExtraOptions(480, 800);
    }
}
