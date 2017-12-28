package com.sampling.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.bill.ultimatefram.util.ExternalFileHelper;
import com.bill.ultimatefram.util.UltimateImageLoaderHelper;
import com.bill.ultimatefram.view.listview.adapter.CommonAdapter;
import com.bill.ultimatefram.view.listview.adapter.Holder;
import com.sampling.App;
import com.sampling.Beans.SamplingBean;
import com.sampling.Common.BarcodeCreater;
import com.sampling.Common.BitmapTools;
import com.sampling.R;
import com.sampling.Service.ScanService;
import com.sampling.dao.DaoSession;
import com.sampling.dao.SamplingBeanDao;

import org.apache.http.util.EncodingUtils;
import org.greenrobot.greendao.query.QueryBuilder;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzf on 17-10-30.
 */

public class CaiyangDetailFrag extends UltimateFragment {
    TextView tvOrderNo, tvLeibie, tvMingchen, tvGps, tvCaiyangyuan, tvDetail;
    EditText edShuliang, edCandi;
    TextView tvMarketName, tvBoothNo;
    ImageView ivSign, ivGiveSign;
    RadioGroup mRadioGroup;
    LinearLayout lineImages;
    GridView gv;//现场图片添加

    public DaoSession mdaoSession;
    QueryBuilder<SamplingBean> queryBuilder;
    SamplingBean samplingBean;
    List<String> imagelist;

    PosApi mApi;
    private boolean isCanPrint = true;
    private PrintQueue mPrintQueue;

    private byte mGpioPower = 0x1E;// PB14
    private byte mGpioTrig = 0x29;// PC9

    private int mCurSerialNo = 3; // usart3
    private int mBaudrate = 4; // 9600

    @Override
    protected int setContentView() {
        return R.layout.frag_caiyangdetail;
    }

    @Override
    protected void initView() {
        tvOrderNo = findViewById(R.id.tv_orderno);
        tvLeibie = findViewById(R.id.tv_leibie);
        tvMingchen = findViewById(R.id.tv_mingchen);
        tvGps = findViewById(R.id.tv_gps);
        tvCaiyangyuan = findViewById(R.id.tv_caiyangyuan);
        edShuliang = findViewById(R.id.ed_shuliang);
        edCandi = findViewById(R.id.ed_candi);
        mRadioGroup = findViewById(R.id.radio_group);
        ivSign = findViewById(R.id.iv_sign);
        ivGiveSign = findViewById(R.id.iv_give_sign);
        tvBoothNo = findViewById(R.id.tv_booth_no);
        tvMarketName = findViewById(R.id.tv_market_name);
        tvDetail = findViewById(R.id.tv_detail);
        gv = findViewById(R.id.gv_photo);
        lineImages = findViewById(R.id.lin_images);
        getFlexibleBar().setTitle("采样单详情");
        getFlexibleBar().setRightText("打印");
        setOnFlexibleClickListener();
    }

    @Override
    protected void initEvent(Bundle savedInsGnceState) {
        Long id = (Long) getArgument(new String[]{"lsamplebeanId"}).get("lsamplebeanId");
        Log.d(TAG, "" + id);
        mdaoSession = ((App) getActivity().getApplication()).mdaoSession;
        queryBuilder = mdaoSession.getSamplingBeanDao().queryBuilder();
        queryBuilder.where(SamplingBeanDao.Properties.Id.eq(id));
        samplingBean = queryBuilder.list().get(0);
        tvOrderNo.setText(samplingBean.getRenwuno());
        tvLeibie.setText(samplingBean.getYangpinglb());
        tvMingchen.setText(samplingBean.getYangpingmc());
        tvGps.setText(samplingBean.getGps());
        tvCaiyangyuan.setText(samplingBean.getUser());
        edShuliang.setText(samplingBean.getCaiyangshuliang());
        edCandi.setText(samplingBean.getCandi());
        tvMarketName.setText(samplingBean.getCscm());
        tvBoothNo.setText(samplingBean.getTwh());
        tvDetail.setText(samplingBean.getDetail());
        if (samplingBean.getStrogemethond().equals("常温")) {
            Log.d(TAG, "常温");
            ((RadioButton) findViewById(R.id.radio_normal)).setChecked(true);
            ((RadioButton) findViewById(R.id.radio_cool)).setChecked(false);
        } else {
            ((RadioButton) findViewById(R.id.radio_cool)).setChecked(true);
            ((RadioButton) findViewById(R.id.radio_normal)).setChecked(false);
            Log.d(TAG, "冷藏");
        }
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

        setPrint();
        getFlexibleBar().getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.print_view, null);
                ((TextView) view.findViewById(R.id.tv_caiyangno)).setText(samplingBean.getCaiyanno());
                ((TextView) view.findViewById(R.id.tv_orderno)).setText(samplingBean.getRenwuno());
                ((TextView) view.findViewById(R.id.tv_leibie)).setText(samplingBean.getYangpinglb());
                ((TextView) view.findViewById(R.id.tv_mingchen)).setText(samplingBean.getYangpingmc());
                ((TextView) view.findViewById(R.id.tv_gps)).setText(samplingBean.getGps());
                ((TextView) view.findViewById(R.id.tv_shuliang)).setText(samplingBean.getCaiyangshuliang());
                ((TextView) view.findViewById(R.id.tv_storge)).setText(samplingBean.getStrogemethond());
                ((TextView) view.findViewById(R.id.tv_caiyangyuan)).setText(samplingBean.getUser());
                ((TextView) view.findViewById(R.id.tv_candi)).setText(samplingBean.getCandi());
                final Bitmap mBitmap = BarcodeCreater.encode2dAsBitmap(samplingBean.getCaiyanno(), 200, 200, 2);

                ((ImageView) view.findViewById(R.id.iv_qr)).setImageBitmap(mBitmap);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view).setCancelable(true).create().show();
                ((TextView) view.findViewById(R.id.tv_print)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        printBitmap(image);
                        StringBuilder sb = new StringBuilder();
                        sb.append("-------------------------------");
                        sb.append("采样编号：" + samplingBean.getCaiyanno());
                        sb.append("\n");
                        sb.append("任务编号：" + samplingBean.getRenwuno());
                        sb.append("\n");
                        sb.append("样品类别：" + samplingBean.getYangpinglb());
                        sb.append("\n");
                        sb.append("样品名称：" + samplingBean.getYangpingmc());
                        sb.append("\n");
                        sb.append("GPS：" + samplingBean.getGps());
                        sb.append("\n");
//                        sb.append("采样数量：" + samplingBean.getCaiyangshuliang());
//                        sb.append("\n");
                        sb.append("存储方式：" + samplingBean.getStrogemethond());
                        sb.append("\n");
                        sb.append("采样员：" + samplingBean.getUser());
                        sb.append("\n");
                        sb.append("来源产地：" + samplingBean.getCandi());
                        sb.append("\n");
                        sb.append("\n");
                        printText(sb.toString(), mBitmap);
//                        printBitmap(bitmap);
//                        printText("-----------------------------");
                    }
                });
            }
        });
    }

    private void setPrint() {
        mPrintQueue = new PrintQueue(getActivity(), ScanService.mApi);
        mPrintQueue.init();
        mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

                Toast.makeText(getApplicationContext(),
                        getString(R.string.print_complete), Toast.LENGTH_SHORT)
                        .show();

                isCanPrint = true;
            }

            @Override
            public void onFailed(int state) {
                // TODO Auto-generated method stub
                isCanPrint = true;
                switch (state) {
                    case PosApi.ERR_POS_PRINT_NO_PAPER:
                        // 打印缺纸
                        showTip(getString(R.string.print_no_paper));
                        break;
                    case PosApi.ERR_POS_PRINT_FAILED:
                        // 打印失败
                        showTip(getString(R.string.print_failed));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_LOW:
                        // 电压过低
                        showTip(getString(R.string.print_voltate_low));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_HIGH:
                        // 电压过高
                        showTip(getString(R.string.print_voltate_high));
                        break;
                }
                // Toast.makeText(PrintBarcodeActivity.this, "打印失败  错误码:"+state,
                // Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGetState(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPrinterSetting(int state) {
                // TODO Auto-generated method stub
                isCanPrint = true;
                switch (state) {
                    case 0:
                        Toast.makeText(getActivity(), "持续有纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "缺纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "检测到黑标", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showTip(String msg) {
        toast(msg);
    }

    private void printText(String str, Bitmap bitmap) {
        int concentration = 65;
        try {
            addPrintTextWithSize(1, concentration,
                    (str + "\n").getBytes("GBK"));
            byte[] printData = BitmapTools.bitmap2PrinterBytes(bitmap);
            mPrintQueue.addBmp(65, 90, 200, 200, printData);
            addPrintTextWithSize(1, concentration, "-------------------------------\n\n\n\n\n".getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        isCanPrint = false;
        mPrintQueue.printStart();
    }

    private void printBitmap(Bitmap mBitmap) {
        if (mBitmap == null)
            return;

        if (!isCanPrint) return;

        int mLeft = 10;
        byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);
        int concentration = 40;

        mPrintQueue.addBmp(concentration, mLeft, mBitmap.getWidth(),
                mBitmap.getHeight(), printData);
        try {
            mPrintQueue.addText(concentration, "\n\n\n\n\n".toString()
                    .getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        isCanPrint = false;
        mPrintQueue.printStart();
    }

    public Bitmap getViewBitmap(View comBitmap, int width, int height) {
        Bitmap bitmap = null;
        if (comBitmap != null) {
            comBitmap.clearFocus();
            comBitmap.setPressed(false);

            boolean willNotCache = comBitmap.willNotCacheDrawing();
            comBitmap.setWillNotCacheDrawing(false);

            // Reset the drawing cache background color to fully transparent
            // for the duration of this operation
            int color = comBitmap.getDrawingCacheBackgroundColor();
            comBitmap.setDrawingCacheBackgroundColor(0);
            float alpha = comBitmap.getAlpha();
            comBitmap.setAlpha(1.0f);

            if (color != 0) {
                comBitmap.destroyDrawingCache();
            }

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            comBitmap.measure(widthSpec, heightSpec);
            comBitmap.layout(0, 0, width, height);

            comBitmap.buildDrawingCache();
            Bitmap cacheBitmap = comBitmap.getDrawingCache();
            if (cacheBitmap == null) {
                Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")",
                        new RuntimeException());
                return null;
            }
            bitmap = Bitmap.createBitmap(cacheBitmap);
            // Restore the view
            comBitmap.setAlpha(alpha);
            comBitmap.destroyDrawingCache();
            comBitmap.setWillNotCacheDrawing(willNotCache);
            comBitmap.setDrawingCacheBackgroundColor(color);
        }
        return bitmap;
    }

    /*
     * 打印文字 size 1 --倍大小 2--2倍大小
     */
    private void addPrintTextWithSize(int size, int concentration, byte[] data) {
        if (data == null)
            return;
        // 2倍字体大小
        byte[] _2x = new byte[]{0x1b, 0x57, 0x02};
        // 1倍字体大小
        byte[] _1x = new byte[]{0x1b, 0x57, 0x01};
        byte[] mData = null;
        if (size == 1) {
            mData = new byte[3 + data.length];
            // 1倍字体大小 默认
            System.arraycopy(_1x, 0, mData, 0, _1x.length);
            System.arraycopy(data, 0, mData, _1x.length, data.length);
            mPrintQueue.addText(concentration, mData);
        } else if (size == 2) {
            mData = new byte[3 + data.length];
            // 1倍字体大小 默认
            System.arraycopy(_2x, 0, mData, 0, _2x.length);
            System.arraycopy(data, 0, mData, _2x.length, data.length);

            mPrintQueue.addText(concentration, mData);

        }

    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        if (width >= newWidth) {
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {

            Bitmap bitmap2 = Bitmap.createBitmap(newWidth, newHeight,
                    bitmap.getConfig());
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(BitmapOrg, (newWidth - width) / 2, 0, null);

            return bitmap2;
        }
    }

    private void openDevice() {
        // open power

        ScanService.mApi.gpioControl(mGpioPower, 0, 1);

        ScanService.mApi.extendSerialInit(mCurSerialNo, mBaudrate, 1, 1, 1, 1);

    }

    private void closeDevice() {
        // close power
        ScanService.mApi.gpioControl(mGpioPower, 0, 0);

        ScanService.mApi.extendSerialClose(mCurSerialNo);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            openDevice();
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mApi.closeDev();
        if (mPrintQueue != null) {
            mPrintQueue.close();
//            closeDevice();
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
 /*           holder.setOnClickListener(R.id.iv, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    ImageView imageView = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.myphoto,null,false);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(imageView.getWidth(), imageView.getHeight()));
                    builder.setView(imageView);
                    Dialog dialog = builder.create();
                    dialog.show();
                    dialog.setCancelable(true);
                }
            });*/
        }
    }
}
