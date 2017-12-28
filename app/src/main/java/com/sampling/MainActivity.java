package com.sampling;

import android.content.Intent;
import android.posapi.PosApi;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bill.ultimatefram.ui.UltimateActivity;
import com.sampling.Common.DialogUtils;
import com.sampling.Fragment.MainFrag;
import com.sampling.Service.ScanService;

public class MainActivity extends UltimateActivity {

    private String TAG = "MainActivity";
    private PosApi mPosSDK;
    private byte mGpioPower = 0x1E ;//PB14
    private byte mGpioTrig = 0x29 ;//PC9

    private int mCurSerialNo = 3; //usart3
    private int mBaudrate = 4; //9600

    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void initView() {
        setPrint();
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        startFragment(new MainFrag(), true);
    }

    private void setPrint() {
        mPosSDK = App.getInstance().getPosApi();
        //初始化接口时回调
//        mPosSDK.setOnComEventListener(mCommEventListener);
        //获取状态时回调
        mPosSDK.setOnDeviceStateListener(onDeviceStateListener);

        Intent newIntent = new Intent(this, ScanService.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(newIntent);
    }

    PosApi.OnCommEventListener mCommEventListener = new PosApi.OnCommEventListener() {
        @Override
        public void onCommState(int cmdFlag, int state, byte[] resp, int respLen) {
            // TODO Auto-generated method stub
            Log.d(TAG,"cmdFlag+++"+cmdFlag);
            switch(cmdFlag){
                case PosApi.POS_INIT:
                    if(state==PosApi.COMM_STATUS_SUCCESS){
                        Toast.makeText(MainActivity.this, "设备初始化成功", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, "设备初始化失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private PosApi.OnDeviceStateListener onDeviceStateListener = new PosApi.OnDeviceStateListener() {
        /**
         * @param state 0-获取状态成功  1-获取状态失败
         * @param version 设备固件版本
         * @param serialNo 设备序列号
         * @param psam1 psam1 状态   0-正常   1-无卡   2-卡错误
         * @param psam2 psam2 状态   0-正常   1-无卡   2-卡错误
         * @param ic IC卡 状态   0-正常   1-无卡   2-卡错误
         * @param swipcard 磁卡状态 o-正常  1-故障
         * @param printer 打印机状态 0-正常  1-缺纸
         */
        public void OnGetState(int state, String version, String serialNo, int psam1, int psam2, int ic, int swipcard, int printer) {
            if (state == PosApi.COMM_STATUS_SUCCESS) {

                StringBuilder sb = new StringBuilder();
                String mPsam1 = null;
                switch (psam1) {
                    case 0:
                        mPsam1 = getString(R.string.state_normal);
                        break;
                    case 1:
                        mPsam1 = getString(R.string.state_no_card);
                        break;
                    case 2:
                        mPsam1 = getString(R.string.state_card_error);
                        break;
                }

                String mPsam2 = null;
                switch (psam2) {
                    case 0:
                        mPsam2 = getString(R.string.state_normal);
                        break;
                    case 1:
                        mPsam2 = getString(R.string.state_no_card);
                        break;
                    case 2:
                        mPsam2 = getString(R.string.state_card_error);
                        break;
                }

                String mIc = null;
                switch (ic) {
                    case 0:
                        mIc = getString(R.string.state_normal);
                        break;
                    case 1:
                        mIc = getString(R.string.state_no_card);
                        break;
                    case 2:
                        mIc = getString(R.string.state_card_error);
                        break;
                }

                String magnetic_card = null;
                switch (swipcard) {
                    case 0:
                        magnetic_card = getString(R.string.state_normal);
                        break;
                    case 1:
                        magnetic_card = getString(R.string.state_fault);
                        break;

                }

                String mPrinter = null;
                switch (printer) {
                    case 0:
                        mPrinter = getString(R.string.state_normal);
                        break;
                    case 1:
                        mPrinter = getString(R.string.state_no_paper);
                        break;

                }

                sb.append(/*getString(R.string.pos_status)+"\n "
                            +*/
                        getString(R.string.psam1_) + mPsam1 + "\n" //pasm1
                                + getString(R.string.psam2) + mPsam2 + "\n" //pasm2
                                + getString(R.string.ic_card) + mIc + "\n" //card
                                + getString(R.string.magnetic_card) + magnetic_card + "\n" //磁条卡
                                + getString(R.string.printer) + mPrinter + "\n" //打印机
                );

                sb.append(getString(R.string.pos_serial_no) + serialNo + "\n");
                sb.append(getString(R.string.pos_firmware_version) + version);
                DialogUtils.showTipDialog(MainActivity.this, sb.toString());

            } else {
                // 获取状态失败
                DialogUtils.showTipDialog(MainActivity.this, getString(R.string.get_pos_status_failed));
            }
        }
    };

    @Override
    protected boolean canSetSystemBarOnFragment() {
        return false;
    }

    private void closeDevice() {
        // close power
        ScanService.mApi.gpioControl(mGpioPower, 0, 0);

        ScanService.mApi.extendSerialClose(mCurSerialNo);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDevice();
    }
}
