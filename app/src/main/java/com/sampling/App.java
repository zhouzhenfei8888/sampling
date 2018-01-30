package com.sampling;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.posapi.PosApi;
import android.util.Log;
import android.widget.Toast;

import com.bill.ultimatefram.app.UltimateApplication;
import com.bill.ultimatefram.util.ExternalFileHelper;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sampling.Common.AssetsDatabaseManager;
import com.sampling.dao.DaoMaster;
import com.sampling.dao.DaoSession;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by zzf on 17-10-23.
 * red branch
 */

public class App extends UltimateApplication {
    ExternalFileHelper.Builder builder;
    SQLiteDatabase sqLiteDatabase;
    public DaoMaster daoMaster;
    //这个是从asset中的数据库中的
    public DaoSession daoSession;
    //这个是新建的
    public DaoSession mdaoSession;

    private PosApi mPosApi;
    private static String mCurDev1 = "";

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
        try {
            mPosApi = PosApi.getInstance(this);

            initPos();
        }catch (UnsatisfiedLinkError error){
            error.printStackTrace();
        }
        CrashReport.initCrashReport(getApplicationContext(),"8962ad55d4",false);
    }

    private void initPos() {
        if (Build.MODEL.equalsIgnoreCase("3508") || Build.MODEL.equalsIgnoreCase("403")) {
            mPosApi.initPosDev("ima35s09");
            setCurDevice("ima35s09");
        } else if (Build.MODEL.equalsIgnoreCase("5501")) {
            mPosApi.initPosDev("ima35s12");
            setCurDevice("ima35s12");
            mPosApi.setOnComEventListener(new PosApi.OnCommEventListener() {
                @Override
                public void onCommState(int cmdFlag, int state, byte[] bytes, int i2) {
                    Log.d("cmdFlag", "cmdFlag+++" + cmdFlag);
                    switch (cmdFlag) {
                        case PosApi.POS_INIT:
                            if (state == PosApi.COMM_STATUS_SUCCESS) {
                                Toast.makeText(getApplicationContext(), "设备初始化成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "设备初始化失败", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
            });
        } else {
            mPosApi.initPosDev(PosApi.PRODUCT_MODEL_IMA80M01);
            setCurDevice(PosApi.PRODUCT_MODEL_IMA80M01);
        }
    }

    public static void setCurDevice(String mCurDev) {
        mCurDev1 = mCurDev;
    }

    public PosApi getPosApi() {
        return mPosApi;
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
