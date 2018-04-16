package com.smartdevice.aidltestdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.morefun.ypossdk.misc.Handware;
import com.morefun.ypossdk.misc.Misc;
import com.morefun.ypossdk.pub.Api;
import com.morefun.ypossdk.pub.listener.PrintWriteListener;
import com.morefun.ypossdk.pub.param.PrintWriteParam;
import com.morefun.ypossdk.pub.result.PrintWriteResult;

public class MenuActivity extends AppCompatActivity {

    LinearLayout card, analytics, events, discovery;
    ImageView first, second;

    ActionItems ac;
    Api mfapi;
    SharedPreferences config = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //makeFullscreen();

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);

        discovery = findViewById(R.id.discovery);
        discovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent i = new Intent(getBaseContext(), Print.class);
                // startActivity(i);
                Print();
            }
        });

        card = findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CaptureActivity.class);
                startActivity(i);
            }
        });

        analytics = findViewById(R.id.analytics);
        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CardReader.class);
                i.putExtra("Source", "menu");
                startActivity(i);
            }
        });

        events = findViewById(R.id.events);
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), TestActivity.class);
                startActivity(i);
            }
        });

       // refrushcsid(config.getInt("CSID",0));
        refrushcsid(0);
        initAidCpak(this);

    }

    void refrushcsid( int csid)
    {
        mfapi = Api.Create(this,csid);
        ac = new ActionItems( this , mfapi );
//        this.setTitle( getString(R.string.activity_title_mid) + "(" + csid + ")") ;
    }

    private static void initAidCpak(Context ctx){
        //(new File(Misc.m_app_data_path)).mkdirs();
        Misc.m_app_data_path = ctx.getFilesDir().getAbsolutePath() + "/";
        Misc.CreatInitDataFile(ctx, R.raw.f_capk_dat,  Misc.m_app_data_path + "F_capk.dat");
        Misc.CreatInitDataFile(ctx, R.raw.f_capk_idx,  Misc.m_app_data_path + "F_capk.idx");
        Misc.CreatInitDataFile(ctx, R.raw.f_tmaid_dat,  Misc.m_app_data_path + "F_tmaid.dat");
        Misc.CreatInitDataFile(ctx, R.raw.f_tmaid_idx,  Misc.m_app_data_path + "F_tmaid.idx");
    }

    // hide UI action bar and make the app fullscreen
    public void makeFullscreen() {
        getSupportActionBar().hide();
        // API 19 (Kit Kat)
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    // View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else {
            if (Build.VERSION.SDK_INT > 10) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }



    // printing

    private void Print() {
        //打印输出
        ac.showwait(getString(R.string.msg_title), getString(R.string.msg_printing));
        Handware.Manager.PrintInit();
        PrintWriteParam pwparam = new PrintWriteParam();


//        pwparam.setPrintdata(PrintDataEn());
        pwparam.setPrintdata(PrintData());


        mfapi.PrintWrite(pwparam, new PrintWriteListener() {
            @Override
            public void OnReturn(PrintWriteResult ret) {
                //获取打印结果
                int printret = ret.getRet();
                ac.closewait();
                if (printret != 0) {
                    ac.blockmsg(getString(R.string.msg_title), getString(R.string.msg_nopaper));
                }
            }
        });
    }

    private String PrintDataEn() {
        StringBuilder sbprint = new StringBuilder();
        //打印浓度
        sbprint.append(Handware.Manager.Print_heat_factor(15));
        //设置字体大小
        sbprint.append(getprintstytle(4));
        //设置字体居中
        sbprint.append(Handware.Manager.Print_align(1));
        sbprint.append("POS purchase order\r\n");
        //设置左对齐
        sbprint.append(Handware.Manager.Print_align(0));
        sbprint.append(getprintstytle(2));
        sbprint.append("==============================\r\n");

        sbprint.append(getprintitem("MERCHANT NAME", "Demo shop name"));
        sbprint.append(getprintitem("MERCHANT NO.", "20321545656687"));
        sbprint.append(getprintitem("TERMINAL NO.", "25689753"));
        sbprint.append(getprintitem("OPERATOR NO.", "01"));
        sbprint.append(getprintitem("ISS NO.", "1544"));
        sbprint.append(getprintitem("ACQ NO.", "0121"));

        sbprint.append(getprintstytle(1));
        sbprint.append("CARD NUMBER\r\n");

        sbprint.append(getprintstytle(3));
        sbprint.append("62179390*****3426");
        sbprint.append("\r\n");


        sbprint.append(getprintstytle(1));
        sbprint.append("TRANS TYPE");
        sbprint.append("\r\n");
        sbprint.append(getprintstytle(3));
        sbprint.append("SALE");
        sbprint.append("\r\n");

        sbprint.append(getprintitem("EXP DATE", "2029"));

        sbprint.append(getprintitem("BATCH NO.", "000012"));
        sbprint.append(getprintitem("VOUCHER NO.", "000001"));
        sbprint.append(getprintitem("AUTH NO.", "56890547"));

        sbprint.append(getprintitem("DATE/TIME", "2016-05-23 16:50:32"));
        sbprint.append(getprintitem("REFER NO.", "365897453214"));

        sbprint.append(getprintstytle(1));
        sbprint.append("AMOUNT");
        sbprint.append("\r\n");

        sbprint.append(getprintstytle(3));
        sbprint.append("RMB ");
        sbprint.append("10.00");
        sbprint.append("\r\n");

        sbprint.append(getprintstytle(2));
        sbprint.append("==============================\r\n");

        sbprint.append(getprintstytle(1));
        //输出空行
        sbprint.append(Handware.Manager.Print_row_space(24));
        sbprint.append("CARD HOLDER SIGNATURE\r\n");
        sbprint.append(Handware.Manager.Print_row_space(72));
        sbprint.append("--------------------------------------------\r\n");

        sbprint.append("I ACKNOWLEDGE	SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES\r\n");

        sbprint.append(getprintstytle(2));
        sbprint.append("MERCHANT COPY\r\n");

        sbprint.append(Handware.Manager.Print_row_space(24));

        sbprint.append(getprintstytle(1));
        sbprint.append("---X---X---X---X---X--X--X--X--X--X--X--X--X--\r\n");

        sbprint.append(Handware.Manager.Print_row_space(108));
        return sbprint.toString();
    }

    private String PrintData() {
        if (Handware.Manager.PrintPaper() == 0) {
            // 缺纸
            ac.blockmsg("提示", "打印机缺纸,请装纸");
            return "";
        }

//        StringBuilder sbprint = new StringBuilder();
//        //打印浓度
//        sbprint.append(Handware.Manager.Print_heat_factor(15));
//        //设置字体大小
//        sbprint.append(getprintstytle(4));
//        //设置字体居中
//        sbprint.append(Handware.Manager.Print_align(1));
//        sbprint.append("银联POS签购单\r\n");
//        //设置左对齐
//        sbprint.append(Handware.Manager.Print_align(0));
//        sbprint.append(getprintstytle(2));
//        sbprint.append("==============================\r\n");
//
//        sbprint.append(getprintitem("商户名称(MERCHANT NAME)", "测试商户"));
//        sbprint.append(getprintitem("商户编号(MERCHANT NO.)", "20321545656687"));
//        sbprint.append(getprintitem("终端编号(TERMINAL NO.)", "25689753"));
//        sbprint.append(getprintitem("操作员号(OPERATOR NO.)", "01"));
//        sbprint.append(getprintitem("发卡行号(ISS NO)", "1544"));
//        sbprint.append(getprintitem("收单行号(ACQ NO)", "0121"));
//
//        sbprint.append(getprintstytle(1));
//        sbprint.append("卡号(CARD NUMBER)\r\n");
//
//        sbprint.append(getprintstytle(3));
//        sbprint.append("62179390*****3426");
//        sbprint.append("\r\n");
//
//
//        sbprint.append(getprintstytle(1));
//        sbprint.append("交易类型(TRANS TYPE)");
//        sbprint.append("\r\n");
//        sbprint.append(getprintstytle(3));
//        sbprint.append("消费(S)");
//        sbprint.append("\r\n");
//
//        sbprint.append(getprintitem("有效期(EXP DATE)", "2029"));
//
//        sbprint.append(getprintitem("批次号(BATCH NO)", "000012"));
//        sbprint.append(getprintitem("凭证号(VOUCHER NO)", "000001"));
//        sbprint.append(getprintitem("授权码(AUTH NO)", "56890547"));
//
//        sbprint.append(getprintitem("交易日期/时间(DATE/TIME)", "2016-05-23 16:50:32"));
//        sbprint.append(getprintitem("交易参考号(REFER NO)", "365897453214"));
//
//        sbprint.append(getprintstytle(1));
//        sbprint.append("金额(AMOUNT)");
//        sbprint.append("\r\n");
//
//        sbprint.append(getprintstytle(3));
//        sbprint.append("RMB ");
//        sbprint.append("10.00");
//        sbprint.append("\r\n");
//
//        sbprint.append(getprintstytle(2));
//        sbprint.append("==============================\r\n");
//
//        sbprint.append(getprintstytle(1));
//        //输出空行
//        sbprint.append(Handware.Manager.Print_row_space(24));
//        sbprint.append("持卡人签名(CARD HOLDER SIGNATURE)\r\n");
//        sbprint.append(Handware.Manager.Print_row_space(72));
//        sbprint.append("--------------------------------------------\r\n");
//
//        sbprint.append("本人确认以上交易，同意将其记入本卡账户 I ACKNOWLEDGE	SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES\r\n");
//
//        sbprint.append(getprintstytle(2));
//        sbprint.append("商户存根(MERCHANT COPY)\r\n");
//
//        sbprint.append(Handware.Manager.Print_row_space(24));
//
//        sbprint.append(getprintstytle(1));
//        sbprint.append("---X---X---X---X---X--X--X--X--X--X--X--X--X--\r\n");
//
//        sbprint.append(Handware.Manager.Print_row_space(108));
//        return sbprint.toString();

        StringBuilder s = new StringBuilder();
        s.append("1. item");
        s.append(Handware.Manager.Print_row_space(108));
        s.append("dfsafdas");
        s.append(Handware.Manager.Print_row_space(108));
        s.append(Handware.Manager.Print_row_space(108));
        return s.toString();
    }

    public static String getprintstytle(int s){
        StringBuilder sb = new StringBuilder();

        if(s == 1){	//16
            sb.append(Handware.Manager.Print_cn_font_size(1));
            sb.append(Handware.Manager.Print_en_font_size(1));
            sb.append(Handware.Manager.Print_cn_font_zoom(1, 1));
            sb.append(Handware.Manager.Print_en_font_zoom(1, 1));
        }
        else if(s == 2){	//24
            sb.append(Handware.Manager.Print_cn_font_size(0));
            sb.append(Handware.Manager.Print_en_font_size(6));
            sb.append(Handware.Manager.Print_cn_font_zoom(2, 2));
            sb.append(Handware.Manager.Print_en_font_zoom(1, 1));
        }
        else if(s == 3){	//32
            sb.append(Handware.Manager.Print_cn_font_size(0));
            sb.append(Handware.Manager.Print_en_font_size(6));
            sb.append(Handware.Manager.Print_cn_font_zoom(2, 4));
            sb.append(Handware.Manager.Print_en_font_zoom(1, 2));
        }
        else if(s == 4){	//48
            sb.append(Handware.Manager.Print_cn_font_size(0));
            sb.append(Handware.Manager.Print_en_font_size(6));
            sb.append(Handware.Manager.Print_cn_font_zoom(4, 4));
            sb.append(Handware.Manager.Print_en_font_zoom(2, 2));
        }
        else if(s == 5){	//8-16
            sb.append(Handware.Manager.Print_cn_font_size(1));
            sb.append(Handware.Manager.Print_en_font_size(5));
            sb.append(Handware.Manager.Print_cn_font_zoom(1, 1));
            sb.append(Handware.Manager.Print_en_font_zoom(1, 1));
        }

        return sb.toString();
    }

    private String getprintitem(String head, String val)
    {
        StringBuffer sb = new StringBuffer();
        sb.append( getprintstytle(1) );
        sb.append(head);
        sb.append("\r\n");

        sb.append( getprintstytle(2));
        sb.append(val);
        sb.append("\r\n");
        return sb.toString();
    }
}
