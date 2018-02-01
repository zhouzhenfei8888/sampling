package com.sampling.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bill.ultimatefram.net.RequestFileParams;
import com.bill.ultimatefram.net.RequestParams;
import com.bill.ultimatefram.ui.UltimateNetFrag;
import com.bill.ultimatefram.util.ExternalFileHelper;
import com.bill.ultimatefram.util.JsonFormat;
import com.bill.ultimatefram.util.UltimateImageLoaderHelper;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.bill.ultimatefram.view.dialog.IOSListDialog;
import com.bill.ultimatefram.view.dialog.IOSProgressDialog;
import com.bill.ultimatefram.view.listview.adapter.CommonAdapter;
import com.bill.ultimatefram.view.listview.adapter.Holder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding2.view.RxView;
import com.sampling.App;
import com.sampling.Beans.Body;
import com.sampling.Beans.Child;
import com.sampling.Beans.ClassifyBean;
import com.sampling.Beans.JiancheBean;
import com.sampling.Beans.MethondBean;
import com.sampling.Beans.OrderInfo;
import com.sampling.Beans.OrderListBean;
import com.sampling.Beans.SamplingBean;
import com.sampling.Beans.下属;
import com.sampling.Beans.抽样方法;
import com.sampling.Beans.菜市场;
import com.sampling.Common.CommonInfo;
import com.sampling.R;
import com.sampling.dao.DaoSession;
import com.sampling.dao.SamplingBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zzf on 18-1-5.
 * 未上传信息补全重新上传
 */

public class AddCaiyangBeanFrag extends UltimateNetFrag implements View.OnClickListener {
    TextView tvOrderNo, tvLeibie, tvMingchen, tvGps, tvCaiyangyuan, tvMethond, tvCheckFile, edMarketName;
    EditText edShuliang, edCandi, edDetail;
    AutoCompleteTextView edBoothNo;
    RadioGroup mRadioGroup;
    Bitmap bitmap, bitmap2;//采样人签字，被采样人签字
    ImageView ivSign, ivGiveSign;
    LinearLayout linSign, linGiveSign;
    GridView gv;//现场图片添加
    LinearLayout lineImages;


    List<String> marketNameList = new ArrayList<>();//菜市场字典
    List<String> boothNoList = new ArrayList<>();//摊位号字典
    Map<String, Object> userinfo;

    String classifyStr, methondStr;//assets中的json文件
    DaoSession mdaoSession;
    private QueryBuilder<SamplingBean> queryBuilder;
    private SamplingBean samplingBean;
    private Gson gson;
    private OrderListBean orderListBean;
    private ArrayList<String> imagelist;
    IOSProgressDialog progressDialog;
    String givePersonSign, personSign;//采样员，被采样人签名的图片路径
    private String caiyangno;
    private String strogeMethond = "常温";//radiogroup的储存方式;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onConnComplete(String result, int flag, Object... tag) {
        switch (flag) {
            case 1:
                orderListBean = gson.fromJson(result, OrderListBean.class);
                List<OrderInfo> orderlist = new ArrayList<>();
                orderlist.addAll(orderListBean.getBody());
                int num = 0;
                IOSListDialog iosListDialog = new IOSListDialog(getActivity());
                for (OrderInfo orderInfo : orderlist) {
                    long shengyuvalue = orderInfo.get抽检数量() - Integer.valueOf(TextUtils.isEmpty(orderInfo.getM已采样数量()) ? "0" : orderInfo.getM已采样数量());
                    if (shengyuvalue > 0) {
                        iosListDialog.addListItem(orderInfo.get任务编号(), Color.parseColor("#90000000"));
                        num++;
                    }
                }
                if (num == 0) {
                    toast("任务已全部完成");
                    return;
                }
                iosListDialog.show();
                iosListDialog.setOnIOSItemClickListener(new IOSListDialog.OnIOSItemClickListener() {
                    @Override
                    public void onIOSItemClick(IOSListDialog.IOSListItem data, TextView item, int position, Object tag) {
                        tvOrderNo.setText(item.getText());
                    }
                });
                break;
            case 2:
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                int code = (int) JsonFormat.formatJson(result, new String[]{"code"}).get("code");
                String message = (String) JsonFormat.formatJson(result, new String[]{"message"}).get("message");
                if (code == 200) {
                    toast("上传成功");
                    SamplingBean sam = samplingBean;
                    sam.setIsupload("1");
                    mdaoSession.update(sam);
                } else {
                    toast("上传失败," + message);
                }
                break;
            case 3:
                Log.d(TAG, result);
                JiancheBean jiancheBean = gson.fromJson(result, JiancheBean.class);
                final List<菜市场> caishichanglist = jiancheBean.getSitinfo().get菜市场();
                for (菜市场 csc : caishichanglist) {
                    marketNameList.add(csc.get名称());
                    for (下属 x : csc.get下属()) {
                        boothNoList.add(x.get摊位号());
                    }
                }
//                Log.d(TAG,""+marketNameList.size()+"   "+boothNoList);
                if (caishichanglist.size() > 0) {
                    edMarketName.setText(caishichanglist.get(0).get名称());
                }
                edMarketName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IOSListDialog dialog = new IOSListDialog(getActivity());
                        for (菜市场 csc : caishichanglist) {
                            dialog.addListItem(csc.get名称(), Color.parseColor("#90000000"));
                        }
                        dialog.show();
                        dialog.setOnIOSItemClickListener(new IOSListDialog.OnIOSItemClickListener() {
                            @Override
                            public void onIOSItemClick(IOSListDialog.IOSListItem data, TextView item, int position, Object tag) {
                                edMarketName.setText(item.getText());
                            }
                        });
                    }
                });
                edBoothNo.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, boothNoList));
                break;
        }
    }

    @Override
    public void onConnError(String result, int flag, Object... tag) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        Log.d(TAG, result);
        toast(result);
    }

    @Override
    protected int setContentView() {
        return R.layout.frag_addcaiyang;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("信息补全");
        getFlexibleBar().getRightTextView().setText("上传");
        setOnFlexibleClickListener();
        getClassify();//获取本地的样品分类列表
        getMethond();//获取采样方法
        gson = new GsonBuilder().create();
        Long id = (Long) getArgument(new String[]{"lsamplebeanId"}).get("lsamplebeanId");
        mdaoSession = ((App) getActivity().getApplication()).mdaoSession;
        queryBuilder = mdaoSession.getSamplingBeanDao().queryBuilder();
        queryBuilder.where(SamplingBeanDao.Properties.Id.eq(id));
        samplingBean = queryBuilder.list().get(0);
        userinfo = UltimatePreferenceHelper.get("userinfo", new String[]{"susername", "spwd"});
        tvOrderNo = findViewById(R.id.tv_orderno);
        tvLeibie = findViewById(R.id.tv_leibie);
        tvMingchen = findViewById(R.id.tv_mingchen);
        tvGps = findViewById(R.id.tv_gps);
        tvCaiyangyuan = findViewById(R.id.tv_caiyangyuan);
        edShuliang = findViewById(R.id.ed_shuliang);
        edCandi = findViewById(R.id.ed_candi);
        mRadioGroup = findViewById(R.id.radio_group);
        linSign = findViewById(R.id.lin_sign);
        linGiveSign = findViewById(R.id.lin_give_sign);
        ivSign = findViewById(R.id.iv_sign);
        ivGiveSign = findViewById(R.id.iv_give_sign);
        gv = findViewById(R.id.gv_photo);
        tvMethond = findViewById(R.id.tv_methond);
        tvCheckFile = findViewById(R.id.tv_checkfile);
        edMarketName = findViewById(R.id.tv_market_name);
        edBoothNo = findViewById(R.id.tv_booth_no);
        edDetail = findViewById(R.id.tv_detail);
        lineImages = findViewById(R.id.lin_images);
        RxView.clicks(tvOrderNo).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        openUrl(CommonInfo.getOrderList, new RequestParams(new String[]{"user", "pw"},
                                new String[]{userinfo.get("susername").toString(), userinfo.get("spwd").toString()}), 1);
                    }
                });
        initData();
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                if (id == R.id.radio_normal) {
                    strogeMethond = "常温";
                } else {
                    strogeMethond = "冷藏";
                }
            }
        });
        tvLeibie.setOnClickListener(this);
        tvMingchen.setOnClickListener(this);
        edMarketName.setOnClickListener(this);
        linSign.setOnClickListener(this);
        linGiveSign.setOnClickListener(this);
        openUrl(CommonInfo.getSiteInfo, new RequestParams(new String[]{"user", "pw"},
                new String[]{userinfo.get("susername").toString(), userinfo.get("spwd").toString()}), 3);
        getFlexibleBar().getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] fileKeys = new String[2];
                RequestFileParams.FileParams[] fileValues = new RequestFileParams.FileParams[2];
                if (!TextUtils.isEmpty(personSign)) {
                    fileKeys[0] = "采样员签名";
                    fileValues[0] = new RequestFileParams.FileParams(ExternalFileHelper.getStorageDirectory() + File.separator + personSign);
                } else {
                    toast("请添加采样员签名");
                    return;
                }
                if (!TextUtils.isEmpty(givePersonSign)) {
                    fileValues[1] = new RequestFileParams.FileParams(ExternalFileHelper.getStorageDirectory() + File.separator + givePersonSign);
                    fileKeys[1] = "被采样人签名";
                } else {
                    toast("请添加被采样人签名");
                    return;
                }
                StringBuilder imgBuilder = new StringBuilder();
                Date date = new Date();
                caiyangno = samplingBean.getCaiyanno();
                Log.d(TAG, caiyangno);
                openUrl(CommonInfo.upLoad, new RequestParams(new String[]{"采样编号", "任务编号", "样本类别", "样本名", "采样点GPS", "采样数量", "存储方式", "_采样员用户ID", "_pw", "来源产地", "采样时间", "菜市场名", "摊位号", "样本详情","上传仪器"},
                                new String[]{caiyangno, getSFText(tvOrderNo), getSFText(tvLeibie), getSFText(tvMingchen), getSFText(tvGps), getSFText(edShuliang), strogeMethond,
                                        userinfo.get("susername").toString(), userinfo.get("spwd").toString(), getSFText(edCandi), simpleDateFormat.format(date), getSFText(edMarketName), getSFText(edBoothNo), getSFText(edDetail),"123456"}),
                        new RequestFileParams(fileKeys, fileValues), 2);
                samplingBean = new SamplingBean(samplingBean.getId(), "0", caiyangno, getSFText(tvOrderNo), getSFText(tvLeibie), getSFText(tvMingchen), getSFText(tvGps), getSFText(edShuliang),
                        simpleDateFormat.format(date), strogeMethond, userinfo.get("susername").toString(), userinfo.get("spwd").toString(), getSFText(edCandi),
                        personSign, givePersonSign, imgBuilder.toString(), getSFText(edMarketName), getSFText(edBoothNo), getSFText(edDetail));
                Log.d(TAG, samplingBean.toString());
                mdaoSession.getSamplingBeanDao().insertOrReplace(samplingBean);
                progressDialog = new IOSProgressDialog(getActivity());
                progressDialog.setMessage("正在上传...");
                progressDialog.show();
            }
        });
    }

    public String getSFText(TextView textView) {
        if (TextUtils.isEmpty(textView.getText())) {
            return "";
        } else {
            return textView.getText().toString();
        }
    }


    private void initData() {
        Log.d(TAG,samplingBean.toString());
        tvOrderNo.setText(samplingBean.getRenwuno());
        tvLeibie.setText(samplingBean.getYangpinglb());
        tvMingchen.setText(samplingBean.getYangpingmc());
        edDetail.setText(samplingBean.getDetail());
        tvMethond.setText(samplingBean.getYangpingmc() + "采样方法");
        tvCheckFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(tvMingchen.getText())) {
                    toast("请先选择样品名称");
                    return;
                }
                String ypmc = tvMingchen.getText().toString();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
//                alertDialog.setTitle("采样方法");
                alertDialog.setCancelable(true);
                MethondBean methondBean = gson.fromJson(methondStr, MethondBean.class);
                抽样方法 cyff = methondBean.get抽样方法();
                String content = null;
                try {
                    Method mmethond = cyff.getClass().getMethod("get" + ypmc);
                    content = (String) mmethond.invoke(cyff);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
//                alertDialog.setMessage(content);
                View scrollView = getActivity().getLayoutInflater().inflate(R.layout.dialog_method, null);
                TextView tvTitle = scrollView.findViewById(R.id.tv_mdialog_title);
                tvTitle.setText(tvMethond.getText());
                TextView tvContent = scrollView.findViewById(R.id.tv_mdialog_content);
                tvContent.setText(content);
                alertDialog.setView(scrollView);
                alertDialog.create().show();
            }
        });
        tvGps.setText(samplingBean.getGps());
        edMarketName.setText(samplingBean.getCscm());
        edBoothNo.setText(samplingBean.getTwh());
        if (!TextUtils.isEmpty(samplingBean.getStrogemethond())) {
            strogeMethond = samplingBean.getStrogemethond();
            if (samplingBean.getStrogemethond().equals("常温")) {
                Log.d(TAG, "常温");
                ((RadioButton) findViewById(R.id.radio_normal)).setChecked(true);
                ((RadioButton) findViewById(R.id.radio_cool)).setChecked(false);
            } else {
                ((RadioButton) findViewById(R.id.radio_cool)).setChecked(true);
                ((RadioButton) findViewById(R.id.radio_normal)).setChecked(false);
                Log.d(TAG, "冷藏");
            }
        }
        tvCaiyangyuan.setText(samplingBean.getUser());
        edCandi.setText(samplingBean.getCandi());
        UltimateImageLoaderHelper.loadImage(ExternalFileHelper.getStorageDirectory().getPath() + File.separator + samplingBean.getSignpath(), ivSign, UltimateImageLoaderHelper.LoadType.STORAGE);
        UltimateImageLoaderHelper.loadImage(ExternalFileHelper.getStorageDirectory().getPath() + File.separator + samplingBean.getGivesignpath(), ivGiveSign, UltimateImageLoaderHelper.LoadType.STORAGE);
        String imagestr = samplingBean.getImages();
        String[] images = imagestr.split(",");
        imagelist = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            imagelist.add(images[i]);
        }
        if (imagestr.equals("")) {
            lineImages.setVisibility(View.GONE);
        } else {
            gv.setAdapter(new MyImageAdapter(getActivity(), imagelist, R.layout.myphoto));
        }
        givePersonSign = samplingBean.getGivesignpath();
        personSign = samplingBean.getSignpath();

    }

    private void getMethond() {
        try {
            InputStream inputStream = getActivity().getAssets().open("methond.json");
            BufferedSource buffer = Okio.buffer(Okio.source(inputStream));
            methondStr = buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getClassify() {
        try {
            InputStream inputStream = getActivity().getAssets().open("classify.json");
            BufferedSource buffer = Okio.buffer(Okio.source(inputStream));
            classifyStr = buffer.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_leibie:
                ClassifyBean classifyBean = gson.fromJson(classifyStr, ClassifyBean.class);
                List<Body> listBody = classifyBean.getBody();
                IOSListDialog iosListDialog = new IOSListDialog(getActivity());
                for (Body body : listBody) {
                    iosListDialog.addListItem(body.getTitle(), Color.parseColor("#90000000"));
                }
                iosListDialog.show();
                iosListDialog.setOnIOSItemClickListener(new IOSListDialog.OnIOSItemClickListener() {
                    @Override
                    public void onIOSItemClick(IOSListDialog.IOSListItem data, TextView item, int position, Object tag) {
                        tvLeibie.setText(item.getText());
                    }
                });
                break;
            case R.id.tv_mingchen:
                if (TextUtils.isEmpty(tvLeibie.getText())) {
                    toast("请先选择样品类别");
                    return;
                } else {
                    ClassifyBean classifyBean2 = gson.fromJson(classifyStr, ClassifyBean.class);
                    List<Body> listBody2 = classifyBean2.getBody();
                    IOSListDialog listDialog = new IOSListDialog(getActivity());
                    String ypflstr = tvLeibie.getText().toString();
                    List<Child> childList = new ArrayList<>();
                    for (Body body : listBody2) {
                        if (body.getTitle().equals(ypflstr)) {
                            childList.clear();
                            childList.addAll(body.getChildren());
                        }
                    }
                    for (Child child : childList) {
                        listDialog.addListItem(child.getTitle(), Color.parseColor("#000000"));
                    }
                    listDialog.show();
                    listDialog.setOnIOSItemClickListener(new IOSListDialog.OnIOSItemClickListener() {
                        @Override
                        public void onIOSItemClick(IOSListDialog.IOSListItem data, TextView item, int position, Object tag) {
                            tvMingchen.setText(item.getText());
                            tvMethond.setText(item.getText() + "采样方法");
                        }
                    });
                }

                break;
            case R.id.lin_sign:
                startFragmentForResult(new SignFrag(), 200);
                break;
            case R.id.lin_give_sign:
                startFragmentForResult(new GiveSignFrag(), 201);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 200) {
            Bundle b = data.getExtras();
            bitmap = b.getParcelable("bitmap");
            ivSign.setImageBitmap(bitmap);
            personSign = new Date().getTime() + "personSign.png";
            ExternalFileHelper.saveBitmap(ExternalFileHelper.getStorageDirectory().getPath() + File.separator + personSign, bitmap);
        } else if (resultCode == Activity.RESULT_OK && requestCode == 201) {
            Bundle b = data.getExtras();
            bitmap2 = b.getParcelable("bitmap");
            ivGiveSign.setImageBitmap(bitmap2);
            givePersonSign = new Date().getTime() + "givePersonSign.png";
            ExternalFileHelper.saveBitmap(ExternalFileHelper.getStorageDirectory().getPath() + File.separator + givePersonSign, bitmap2);
        }
    }

    private class MyImageAdapter extends CommonAdapter<String> {
        public MyImageAdapter(Context context, List<String> datum, int resItemID) {
            super(context, datum, resItemID);
        }

        @Override
        protected void convert(String s, Holder holder, int position) {
            Log.d(TAG, ExternalFileHelper.getPath(s, true));
            UltimateImageLoaderHelper.loadImage(ExternalFileHelper.getPath(s, true),
                    (ImageView) holder.getView(R.id.iv), UltimateImageLoaderHelper.LoadType.STORAGE);
        }
    }
}
