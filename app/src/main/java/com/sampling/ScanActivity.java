package com.sampling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.bill.ultimatefram.ui.UltimateActivity;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CameraPreview;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

/**
 * Created by zzf on 17-7-28.
 */

public class ScanActivity extends UltimateActivity {
    private CompoundBarcodeView mCompoundScanView;
    private CaptureManager capture;
    int scan_type;
    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected View setCustomContentView() {
        mCompoundScanView = new CompoundBarcodeView(this);
        return mCompoundScanView;
    }

    @Override
    protected void initView() {
        setTitle("二维码");
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        capture = new CaptureManager(this, mCompoundScanView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        mCompoundScanView.getBarcodeView().addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {

            }

            @Override
            public void previewStarted() {
                mCompoundScanView.setTorchOn();
            }

            @Override
            public void previewStopped() {

            }

            @Override
            public void cameraError(Exception error) {

            }
        });
        capture.decodeContinue(callback);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            capture.onPause();
//            if (scan_type == 1) {
                Intent intent = new Intent();
                intent.putExtra("qr_code", String.valueOf(result));
                setResult(Activity.RESULT_OK, intent);
                ScanActivity.this.finish();
//                popBackStack();
//            } else {
//                startFragmentForResult(new ChatAddNewFriendFrag().setArgument(new String[]{"s_mobile"}, new Object[]{result}), 0);
//            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        capture.onPause();
        mCompoundScanView.setTorchOff();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
        mCompoundScanView.setTorchOff();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mCompoundScanView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        capture.onResume();
    }
}
