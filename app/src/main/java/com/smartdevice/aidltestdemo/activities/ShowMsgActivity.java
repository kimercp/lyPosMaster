package com.smartdevice.aidltestdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdevice.aidltestdemo.base.BaseActivity;
import com.smartdevice.aidltestdemo.manager.ActionService;
import com.smartdevice.aidltestdemo.R;


public class ShowMsgActivity extends BaseActivity {

    TextView tx;
    Button btncancel;
    Button btnOk;
    boolean iswait;
    View mSpace;
    LinearLayout mLLyCainter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_msg);

        initAppBarLayout();
        hideBackView();
        btncancel = (Button) findViewById(R.id.btncancel);
        btnOk = (Button) findViewById(R.id.btnOk);
//        mSpace = findViewById(R.id.space);

        btncancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onEnterPressed();
            }
        });

        mLlyBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onNewIntent(getIntent());
    }

    void initBtn(Button btn, String text) {
        if (text != null && text.length() > 0) {
            btn.setVisibility(View.VISIBLE);
//            btn.setText(text);
//            mSpace.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.GONE);
//            mSpace.setVisibility(View.GONE);
        }

    }

    public static final String TXN_DETAIL = "查询交易明细";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle param = intent.getExtras();
        CharSequence msg = param.getCharSequence("msg");
        String qr = param.getString("qr");
        CharSequence title = param.getCharSequence("title");
        //
        Log.d("xiaomao", "msg = " + msg);
        iswait = param.getBoolean("iswait");
        setTitle(title);
        boolean isShow = true;
        if (!TextUtils.isEmpty(msg) && msg.toString().contains(":")) {
            if (TXN_DETAIL.equals(title)) {
                isShow = false;
            } else if (title.toString().contains("撤销")) {
                isShow = false;
            }
        }

        if (isShow) {
            showContent(true);
            initActivityView(param, msg, qr, title);
        }
    }

    /**
     * @param show 是否显示Activity中的布局，若果是否的话，则显示fragment里的布局。
     */
    public void showContent(boolean show) {
        showContentLay(show);
    }
    public void showContentLay(boolean show){
//        mLlyContent.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    private void initActivityView(Bundle param, CharSequence msg, String qr, CharSequence title) {
//        View progressBar = findViewById(R.id.progress_bar);
//        progressBar.setVisibility(iswait ? View.GONE : View.VISIBLE);
        if (iswait){
            mLlyBack.setVisibility(View.VISIBLE);
        }else {
            mLlyBack.setVisibility(View.GONE);
        }
        showContentLay(iswait);

        tx = (TextView) findViewById(R.id.textView1);
        tx.setText(msg);
//        btnOk.setVisibility(View.GONE);
//        btncancel.setVisibility(View.GONE);
        initBtn(btnOk, param.getString("okBtn"));
        initBtn(btncancel, param.getString("cancelBtn"));


        if (btnOk.getVisibility() == View.VISIBLE) {
            btnOk.requestFocus();
        } else {
            btncancel.requestFocus();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (iswait) {
            SetResult(BaseActivity.RET_CANCEL);
        } else {
            ActionService.Instance().SetCancel();
        }
    }

    @Override
    public void onEnterPressed() {
        SetResult(BaseActivity.RET_OK);
    }
}
