package com.smartdevice.aidltestdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smartdevice.aidltestdemo.base.BaseActivity;
import com.smartdevice.aidltestdemo.widget.PaintView;
import com.morefun.ypossdk.misc.Handware;
import com.morefun.ypossdk.misc.ImgUtils;

public class SignActivity extends BaseActivity {
    private static final String TAG = "SignActivity";

    private Button btn1, btn2;
    private PaintView mPaintView = null;

    public static String mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        mPaintView = (PaintView) findViewById(R.id.auth_signature_canvas);
        mBitmap = null;
        btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //获取签名bitmap
                    mBitmap = PrintSignatureImage(getApplicationContext(), mPaintView.getCanvasBitmap(), 320, 350);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SetResult(RET_OK);
            }
        });

        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPaintView.clearCanvas();
            }
        });
    }

    public static String PrintSignatureImage(Context context, Bitmap bitmap, int nWidth, int nHeight) {
        String filename = context.getFilesDir().getAbsolutePath() + "/sign.dat";
        ImgUtils bus = new ImgUtils();
        bus.saveBlockfile(filename, bitmap, nWidth, nHeight);
        return Handware.Manager.Print_block_width(nWidth) + Handware.Manager.Print_block(filename);
    }

}
