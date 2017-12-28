package com.sampling.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bill.ultimatefram.ui.UltimateFragment;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.sampling.R;

/**
 * Created by zzf on 17-7-4.
 */

public class SignFrag extends UltimateFragment {
    SignaturePad signaturePad;
    @Override
    protected int setContentView() {
        return R.layout.frag_sign;
    }

    @Override
    protected void initView() {
        setOnFlexibleClickListener();
        setFlexTitle("签名");
        setFlexRightText("保存");
        signaturePad = findViewById(R.id.signature_pad);
    }

    @Override
    public void onRightClickListener() {
        Bitmap bitmap = signaturePad.getTransparentSignatureBitmap();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap",bitmap);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK,intent);
    }

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }
}
