package com.smartdevice.aidltestdemo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartdevice.aidltestdemo.manager.ActionCallBack;
import com.smartdevice.aidltestdemo.manager.IWaitUIRets;
import com.smartdevice.aidltestdemo.utils.ToastUtils;
import com.smartdevice.aidltestdemo.R;

import java.util.HashMap;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


public class BaseActivity extends AppCompatActivity implements ActionCallBack {

    protected TextView mTvTitle;
    protected ImageView mIvPersonCenter;
    protected ImageView mIvMessage;
    protected LinearLayout mLlyBack;
    protected LinearLayout mLlyPersonCenter;
    private ImageView mIvBack;
    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;
    protected TextView mTextView1;
    private long mClickBackMills;
    private boolean mResumed;
    protected static Stack<String> m_prcoList = new Stack<String>();

    public final static int RET_TIMEOUT = -2;
    public final static int RET_BACK = -1;
    public final static int RET_CANCEL = 0;
    public final static int RET_OK = 1;
    public final static String LIFE_TAG = "life_tag";
    /************************常量********************************/
    public static final String TITLE = "title";
    public static final String IS_NEED_FINISH = "isNeedFinsh";
    public static final String RET_TYPE = "ret_type";
    /*************************常量******************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LIFE_TAG, "onCreate");
    }

    protected void initAppBarLayout() {
//        mLlyBack = (LinearLayout) findViewById(R.id.lly_back);
//        mLlyPersonCenter = (LinearLayout) findViewById(R.id.lly_person_center);
//        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLlyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickBackMills = System.currentTimeMillis();
                onBackPressed();
            }
        });
//        mTvTitle = (TextView) findViewById(R.id.tv_title);
//        mIvPersonCenter = (ImageView) findViewById(R.id.iv_person_center);
//        mIvMessage = (ImageView) findViewById(R.id.iv_message);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
//        mTextView1 = (TextView) findViewById(R.id.text_view1);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LIFE_TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LIFE_TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LIFE_TAG, "onResume");
        mResumed = true;
        resetTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LIFE_TAG, "onPause");
        mResumed = false;
        if (isTimer()) {
            stopTimer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LIFE_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LIFE_TAG, "onDestroy");
        //ActionService.Instance().RemoveItem(this);
        if (isTimer()) {
            stopTimer();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle param = intent.getExtras();
        if (param != null) {
            m_title = param.getString("title");
            if (m_title != null) {
                setTitle(m_title);
            }
            m_TimeBase = param.getInt("timeout");
            if (m_TimeBase == 0) {
                //m_TimeBase = 60;
            }
        }
        resetTimer();
    }

    protected int UpdateListView(String id) {
        return 1;
    }

    public void getMenuParams(HashMap<String, Object> map) {
        if (map == null || map.size() == 0) return;
        SelectMenu(map.get("id").toString(), map.get("title").toString(), map.get("parent").toString());
    }

    public void SelectMenu(String[] menuArray) {
        SelectMenu(menuArray[0], menuArray[1], menuArray[2]);
    }

    public void SelectMenu(String id, String title, String parent) {
        int ret;

    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTvTitle != null)
            mTvTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        mTvTitle.setText(titleId);
    }

    public void showToast(String msg) {
        ToastUtils.show(this, msg);
    }

    public void showToast(@StringRes int resId) {
        ToastUtils.show(this, getString(resId));
    }

    public Context getContext() {
        return this;
    }

    public boolean isDestroyed() {
        return getSupportFragmentManager().isDestroyed();
    }

    public boolean isPause() {
        return !mResumed;
    }


    private static String TAG = "action.activity";
    private boolean m_sendmsg = false;

    protected boolean isTimer() {
        return true;
    }

    public boolean isBase() {
        return false;
    }


    @Override
    public void onBackPressed() {
        if (!SetResult(RET_CANCEL)) {
            super.onBackPressed();
        }
    }

    public void resetTimer() {
        if (isTimer()) {
            stopTimer();
            m_TimeOut = m_TimeBase;
            startTimer();
        }
    }

    int m_TimeBase = 0;
    int m_TimeOut = 0;
    CharSequence m_title = "";

    Timer m_timer;
    TimerTask m_task;

    void startTimer() {
        if (m_timer == null) {
            m_timer = new Timer();
        }
        if (m_task == null) {
            m_task = new TimerTask() {
                @Override
                public void run() {
                    if (m_TimeOut > 0) {
                        m_TimeOut--;

                        //Log.i(LOG_TAG , "timeout:" + m_TimeOut );
                        if (m_TimeOut == 0) {
                            OnTimerOut();
                        } else {
                            BaseActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setTitle(m_title + "(" + String.valueOf(m_TimeOut) + ")");
                                }
                            });
                        }
                    }
                }
            };
        }
        m_timer.schedule(m_task, 1000, 1000);
    }

    public void stopTimer() {
        if (m_timer != null) {
            m_timer.cancel();
            m_timer = null;
        }

        if (m_task != null) {
            m_task.cancel();
            m_task = null;
        }
    }

    public boolean SetResult(Object ores) {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return false;
        }
        IWaitUIRets i = (IWaitUIRets) extras.getSerializable("iWaitUIRet");
        if (i != null) {
            i.SetResult(ores);
            return true;
        } else {
            Log.w(TAG, "SetResult IWaitUIRets=null");
            return false;
        }
    }

    public void setBaseTime(int timebase) {
        m_TimeBase = timebase;
    }

    public void setTitleShow(String title) {
        if (title != null) {
            m_title = title;
            setTitle(m_title);
        }
    }

    public void setTitleShow() {
        if (!TextUtils.isEmpty(m_title)) {
            setTitle(m_title);
        }
    }

    protected void OnTimerOut() {
        SetResult(RET_TIMEOUT);
    }


    public void onEnterPressed() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            int key = event.getKeyCode();

            if (key == KeyEvent.KEYCODE_ENTER) {
                onEnterPressed();
                return true;
            }

        }
        return super.dispatchKeyEvent(event);
    }

    public void onClick(View view) {

    }

    public void hideBackView() {
        if (mLlyBack != null) {
            mLlyBack.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * ActionService 结束一个Activity的方法
     */
    public void action_finish() {
        Intent intent = new Intent(getContext(), getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * ActionService 启动一个Activity的方法
     *
     * @param className 应用名称
     */
    public boolean action_startActivity(String className, Bundle bundle) {
        Intent intent = null;
        try {
            intent = new Intent(getContext(), Class.forName(className));
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void startNextActivity(Class cls) {
        startNextActivity(cls, null);
    }

    public void startNextActivity(Class cls, Bundle bundle) {
        Intent intent = new Intent(getContext(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    private Bundle mNextBundle;

    public void putStringExtra(String key, String s) {
        if (mNextBundle == null) {
            mNextBundle = new Bundle();
        }
        mNextBundle.putString(key, s);
    }

    public void putBooleanExtra(String key, boolean b) {
        if (mNextBundle == null) {
            mNextBundle = new Bundle();
        }
        mNextBundle.putBoolean(key, b);
    }
    public void putIntExtra(String key, int i) {
        if (mNextBundle == null) {
            mNextBundle = new Bundle();
        }
        mNextBundle.putInt(key, i);
    }

    public Bundle getNextBundle() {
        if (mNextBundle == null) {
            mNextBundle = new Bundle();
        }
        return mNextBundle;
    }

}
