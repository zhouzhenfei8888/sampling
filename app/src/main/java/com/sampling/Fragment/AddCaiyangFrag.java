package com.sampling.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.bill.ultimatefram.app.UltimateService;
import com.bill.ultimatefram.net.RequestFileParams;
import com.bill.ultimatefram.net.RequestParams;
import com.bill.ultimatefram.ui.UltimateNetFrag;
import com.bill.ultimatefram.util.BaiduLocationHelper;
import com.bill.ultimatefram.util.Compatible;
import com.bill.ultimatefram.util.ExternalFileHelper;
import com.bill.ultimatefram.util.JsonFormat;
import com.bill.ultimatefram.util.UltimateImageLoaderHelper;
import com.bill.ultimatefram.util.UltimatePreferenceHelper;
import com.bill.ultimatefram.view.dialog.IOSListDialog;
import com.bill.ultimatefram.view.dialog.IOSProgressDialog;
import com.bill.ultimatefram.view.imageview.DelImageView;
import com.bill.ultimatefram.view.listview.adapter.CommonAdapter;
import com.bill.ultimatefram.view.listview.adapter.Holder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sampling.Beans.Body;
import com.sampling.Beans.Child;
import com.sampling.Beans.ClassifyBean;
import com.sampling.Beans.JiancheBean;
import com.sampling.Beans.MethondBean;
import com.sampling.Beans.OrderInfo;
import com.sampling.Beans.OrderListBean;
import com.sampling.Beans.ResponseBean;
import com.sampling.Beans.SamplingBean;
import com.sampling.Beans.下属;
import com.sampling.Beans.抽样方法;
import com.sampling.Beans.菜市场;
import com.sampling.Common.CommonInfo;
import com.sampling.App;
import com.sampling.R;
import com.sampling.dao.DaoSession;
import com.sampling.dao.DictionaryDao;
import com.sampling.dao.SamplingBeanDao;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okio.BufferedSource;
import okio.Okio;

/**
 * Created by zzf on 17-10-24.
 */

public class AddCaiyangFrag extends UltimateNetFrag implements View.OnClickListener, BaiduLocationHelper.OnReceiveLocationListener {
    TextView tvOrderNo, tvLeibie, tvMingchen, tvGps, tvCaiyangyuan, tvMethond, tvCheckFile;
    EditText edShuliang, edCandi,edDetail;
    AutoCompleteTextView edMarketName, edBoothNo;
    RadioGroup mRadioGroup;
    List<String> marketNameList = new ArrayList<>();//菜市场字典
    List<String> boothNoList = new ArrayList<>();//摊位号字典
    Map<String, Object> userinfo;
    Gson gson;
    OrderListBean orderListBean;
    DictionaryDao dictionaryDao;
    RxPermissions rxPermissions;
    LocationClient mLocationClient;
    DecimalFormat df = new DecimalFormat("#.0000");//经纬度保留2位小数
    String strogeMethond = "常温";//radiogroup的储存方式
    Bitmap bitmap, bitmap2;//采样人签字，被采样人签字
    ImageView ivSign, ivGiveSign;
    LinearLayout linSign, linGiveSign;
    GridView gv;//现场图片添加
    List<String> mDatum;//拍照的图片路径
    ImageAdapter imageAdapter;
    String address;
    List<File> imageFiles;// 拍照的图片
    String givePersonSign, personSign;//采样员，被采样人签名的图片路径
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    IOSProgressDialog progressDialog;
    String classifyStr, methondStr;//assets中的json文件
    DaoSession mdaoSession;
    SamplingBean samplingBean;
    String caiyangno;//采样单号
    BaiduLocationHelper baiduLocationHelper;
    private List<下属> marketInfo;

    @Override
    public void onConnComplete(String result, int flag, Object... tag) {
        switch (flag) {
            case 1:
                orderListBean = gson.fromJson(result, OrderListBean.class);
                List<OrderInfo> orderlist = new ArrayList<>();
                orderlist.addAll(orderListBean.getBody());
                IOSListDialog iosListDialog = new IOSListDialog(getActivity());
                for (OrderInfo orderInfo : orderlist) {
                    iosListDialog.addListItem(orderInfo.get任务编号(), Color.parseColor("#90000000"));
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
                    QueryBuilder<SamplingBean> queryBuilder = mdaoSession.getSamplingBeanDao().queryBuilder();
                    List<SamplingBean> samplingBeanList = queryBuilder.where(SamplingBeanDao.Properties.Caiyanno.eq(caiyangno)).list();
                    SamplingBean sam = samplingBeanList.get(0);
                    sam.setIsupload("1");
                    mdaoSession.update(sam);
                } else {
                    toast("上传失败," + message);
                }
                break;
            case 3:
                Log.d(TAG, result);
                JiancheBean jiancheBean = gson.fromJson(result, JiancheBean.class);
                List<菜市场> caishichanglist = jiancheBean.getSitinfo().get菜市场();
                for (菜市场 csc : caishichanglist) {
                    marketNameList.add(csc.get名称());
                    for (下属 x : csc.get下属()) {
                        boothNoList.add(x.get摊位号());
                    }
                }
//                Log.d(TAG,""+marketNameList.size()+"   "+boothNoList);
                edMarketName.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, marketNameList));
                edBoothNo.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, boothNoList));
                break;
        }
    }

    @Override
    public void onConnError(String result, int flag, Object... tag) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Log.d(TAG, result);
    }

    @Override
    protected int setContentView() {
        return R.layout.frag_addcaiyang;
    }

    @Override
    protected void initView() {
        getFlexibleBar().setTitle("新增采样单");
        getFlexibleBar().getRightTextView().setText("上传");
        setOnFlexibleClickListener();
        gson = new GsonBuilder().create();
        mdaoSession = ((App) getActivity().getApplication()).mdaoSession;
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
        Map<String, Object> argument = getArgument(new String[]{"sorderno"});
        Log.d("gggjjjjccc",argument.get("sorderno").toString());
        tvOrderNo.setText(argument.get("sorderno").toString());
        userinfo = UltimatePreferenceHelper.get("userinfo", new String[]{"susername", "spwd"});
        openUrl(CommonInfo.getSiteInfo, new RequestParams(new String[]{"user", "pw"},
                new String[]{userinfo.get("susername").toString(), userinfo.get("spwd").toString()}), 3);
        tvCaiyangyuan.setText(userinfo.get("susername").toString());
        dictionaryDao = ((App) getActivity().getApplication()).daoSession.getDictionaryDao();
        tvOrderNo.setOnClickListener(this);
        tvLeibie.setOnClickListener(this);
        tvMingchen.setOnClickListener(this);
        linSign.setOnClickListener(this);
        linGiveSign.setOnClickListener(this);
        tvMethond.setOnClickListener(this);
        tvCheckFile.setOnClickListener(this);
        getClassify();//获取本地的样品分类列表
        getMethond();//获取采样方法
        imageFiles = new ArrayList<>();
        setGpsInfo();
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
        addPhotos();
        getFlexibleBar().getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] fileKeys = new String[2 + imageFiles.size()];
                RequestFileParams.FileParams[] fileValues = new RequestFileParams.FileParams[2 + imageFiles.size()];
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
                for (int i = 0; i < imageFiles.size(); i++) {
                    int num = i + 1;
                    fileKeys[i + 2] = "照片_" + num;
                    fileValues[i + 2] = new RequestFileParams.FileParams(imageFiles.get(i).getPath());
                    Log.d(TAG, imageFiles.get(i).getPath());
                    imgBuilder.append(imageFiles.get(i).getName()).append(",");
                }
                Date date = new Date();
                String time = String.valueOf(date.getTime());
                caiyangno = "NO" + Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID).toUpperCase() + time;
                Log.d(TAG, caiyangno);
                openUrl(CommonInfo.upLoad, new RequestParams(new String[]{"采样编号", "任务编号", "样本类别", "样本名", "采样点GPS", "采样数量", "存储方式", "_采样员用户ID", "_pw", "来源产地", "采样时间", "菜市场名", "摊位号","样本详情"},
                                new String[]{caiyangno, getSFText(tvOrderNo), getSFText(tvLeibie), getSFText(tvMingchen), getSFText(tvGps), getSFText(edShuliang), strogeMethond,
                                        userinfo.get("susername").toString(), userinfo.get("spwd").toString(), getSFText(edCandi), simpleDateFormat.format(date), getSFText(edMarketName), getSFText(edBoothNo),getSFText(edDetail)}),
                        new RequestFileParams(fileKeys, fileValues), 2);
                samplingBean = new SamplingBean(null, "0", caiyangno, getSFText(tvOrderNo), getSFText(tvLeibie), getSFText(tvMingchen), getSFText(tvGps), getSFText(edShuliang),
                        simpleDateFormat.format(date), strogeMethond, userinfo.get("susername").toString(), userinfo.get("spwd").toString(), getSFText(edCandi),
                        personSign, givePersonSign, imgBuilder.toString(), getSFText(edMarketName),getSFText(edBoothNo),getSFText(edDetail));
                Log.d(TAG, samplingBean.toString());
                mdaoSession.getSamplingBeanDao().insert(samplingBean);
                progressDialog = new IOSProgressDialog(getActivity());
                progressDialog.setMessage("正在上传...");
                progressDialog.show();
            }
        });
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

    public String getSFText(TextView textView) {
        if (TextUtils.isEmpty(textView.getText())) {
            return "";
        } else {
            return textView.getText().toString();
        }
    }

    private void addPhotos() {
        mDatum = new ArrayList<>();
        mDatum.add(null);
        gv.setAdapter(imageAdapter = new ImageAdapter(getActivity(), mDatum, R.layout.lay_simple_del_imageview));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                if (position == parent.getCount() - 1) {
                    UltimateService.takePictureOnFragment(AddCaiyangFrag.this, address = ExternalFileHelper.getPath(System.currentTimeMillis() + ".png", true));
                }
            }
        });
    }

    private class ImageAdapter extends CommonAdapter {

        public ImageAdapter(Context context, List mDatum, int resItemID) {
            super(context, mDatum, resItemID);
        }

        @Override
        public void convert(Object item, Holder holder, final int position) {
            Compatible.compatSize(holder.getContentView(), 230);
            DelImageView delIv = holder.getContentView();
            if (mDatum.size() - 1 == position) {
                delIv.setDelViewVisibility(View.GONE);
                UltimateImageLoaderHelper.loadImageByNormalOption(R.mipmap.addphoto, delIv.getImageView(), UltimateImageLoaderHelper.LoadType.DRAWABLE);
            } else {
                delIv.setDelViewVisibility(View.VISIBLE);
                UltimateImageLoaderHelper.loadImageByNormalOption(item, delIv.getImageView(), UltimateImageLoaderHelper.LoadType.STORAGE);
            }
            delIv.setOnDelClickListener(new DelImageView.OnDelClickListener() {
                @Override
                public void onDelClick(ImageView ivDel) {
                    mDatum.remove(position);
                    Log.d(TAG, imageFiles.size() + "position" + position);
                    imageFiles.remove(imageFiles.size() - position - 1);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            for (File file : imageFiles) {
            }
        }
    }

    private void setGpsInfo() {
//        mLocationClient = new LocationClient(getActivity());
//        mLocationClient.registerLocationListener(this);
//        mLocationClient.start();
        baiduLocationHelper = new BaiduLocationHelper(getApplicationContext()).registerLocationListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_orderno:
                openUrl(CommonInfo.getOrderList, new RequestParams(new String[]{"user", "pw"},
                        new String[]{userinfo.get("susername").toString(), userinfo.get("spwd").toString()}), 1);
                break;
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
            case R.id.tv_checkfile:
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
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case UltimateService.TAKE_PICTURE:
     /*               Bitmap bitmap = UltimateImageLoaderHelper.decodeFile(address, 1024, 1024);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
                    BufferedSink bufferedSink = null;
                    try {
                        bufferedSink = Okio.buffer(Okio.sink(new File(address)));
                        bufferedSink.write(bos.toByteArray());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    mDatum.add(0, address);
                    imageFiles.add(new File(address));
                    imageAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
//        tvGps.setText("" + df.format(bdLocation.getLongitude()) + "," + df.format(bdLocation.getLatitude()));
        tvGps.setText("" + bdLocation.getLongitude() + "," + bdLocation.getLatitude());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (baiduLocationHelper != null) {
            baiduLocationHelper.unregisterLocationListener();
            baiduLocationHelper.locationStop();
        }
    }
}
