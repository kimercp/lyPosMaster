package com.smartdevice.aidltestdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.morefun.ypossdk.misc.Handware;
import com.morefun.ypossdk.misc.Misc;
import com.morefun.ypossdk.pub.Api;
import com.morefun.ypossdk.pub.listener.PrintWriteListener;
import com.morefun.ypossdk.pub.param.PrintWriteParam;
import com.morefun.ypossdk.pub.result.PrintWriteResult;
import com.smartdevice.aidltestdemo.base.BaseActivity;
import com.smartdevice.aidltestdemo.manager.ActionService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import static com.smartdevice.aidltestdemo.print.PrintFunc.PrintWriteWithPage;
import static com.smartdevice.aidltestdemo.print.PrintFunc.osl_print_add;

public class MenuActivity extends BaseActivity {

    public static String SCAN_SALE= "Scan QR Code";

    LinearLayout card, analytics, events, discovery;
    ImageView first, second;

    ActionItems ac;
    Api mfapi;
    SharedPreferences config = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //makeFullscreen();

        first = findViewById(R.id.first);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActionService.Instance().Action(MenuActivity.this, SCAN_SALE);
            }
        });

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

    void refrushcsid( int csid) {
        mfapi = Api.Create(this,csid);
        ac = new ActionItems( this , mfapi );
//        this.setTitle( getString(R.string.activity_title_mid) + "(" + csid + ")") ;
    }

    private static void initAidCpak(Context ctx) {
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
        StringBuilder stringToPrint = new StringBuilder();
        //打印浓度
        stringToPrint.append(Handware.Manager.Print_heat_factor(15));
        //设置字体大小
        stringToPrint.append(getprintstytle(4));
        //设置字体居中
        stringToPrint.append(Handware.Manager.Print_align(1));
        stringToPrint.append("POS purchase order\r\n");
        //设置左对齐
        stringToPrint.append(Handware.Manager.Print_align(0));
        stringToPrint.append(getprintstytle(2));
        stringToPrint.append("==============================\r\n");

        stringToPrint.append(getprintitem("MERCHANT NAME", "Demo shop name"));
        stringToPrint.append(getprintitem("MERCHANT NO.", "20321545656687"));
        stringToPrint.append(getprintitem("TERMINAL NO.", "25689753"));
        stringToPrint.append(getprintitem("OPERATOR NO.", "01"));
        stringToPrint.append(getprintitem("ISS NO.", "1544"));
        stringToPrint.append(getprintitem("ACQ NO.", "0121"));

        stringToPrint.append(getprintstytle(1));
        stringToPrint.append("CARD NUMBER\r\n");

        stringToPrint.append(getprintstytle(3));
        stringToPrint.append("62179390*****3426");
        stringToPrint.append("\r\n");


        stringToPrint.append(getprintstytle(1));
        stringToPrint.append("TRANS TYPE");
        stringToPrint.append("\r\n");
        stringToPrint.append(getprintstytle(3));
        stringToPrint.append("SALE");
        stringToPrint.append("\r\n");

        stringToPrint.append(getprintitem("EXP DATE", "2029"));

        stringToPrint.append(getprintitem("BATCH NO.", "000012"));
        stringToPrint.append(getprintitem("VOUCHER NO.", "000001"));
        stringToPrint.append(getprintitem("AUTH NO.", "56890547"));

        stringToPrint.append(getprintitem("DATE/TIME", "2016-05-23 16:50:32"));
        stringToPrint.append(getprintitem("REFER NO.", "365897453214"));

        stringToPrint.append(getprintstytle(1));
        stringToPrint.append("AMOUNT");
        stringToPrint.append("\r\n");

        stringToPrint.append(getprintstytle(3));
        stringToPrint.append("RMB ");
        stringToPrint.append("10.00");
        stringToPrint.append("\r\n");

        stringToPrint.append(getprintstytle(2));
        stringToPrint.append("==============================\r\n");

        stringToPrint.append(getprintstytle(1));
        //输出空行
        stringToPrint.append(Handware.Manager.Print_row_space(24));
        stringToPrint.append("CARD HOLDER SIGNATURE\r\n");
        stringToPrint.append(Handware.Manager.Print_row_space(72));
        stringToPrint.append("--------------------------------------------\r\n");

        stringToPrint.append("I ACKNOWLEDGE	SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES\r\n");

        stringToPrint.append(getprintstytle(2));
        stringToPrint.append("MERCHANT COPY\r\n");

        stringToPrint.append(Handware.Manager.Print_row_space(24));

        stringToPrint.append(getprintstytle(1));
        stringToPrint.append("---X---X---X---X---X--X--X--X--X--X--X--X--X--\r\n");

        stringToPrint.append(Handware.Manager.Print_row_space(108));
        return stringToPrint.toString();
    }

    private String PrintData() {
        if (Handware.Manager.PrintPaper() == 0) {
            // 缺纸
            ac.blockmsg("提示", "打印机缺纸,请装纸");
            return "";
        }

        StringBuilder stringToPrint = new StringBuilder();

        //打印浓度
        stringToPrint.append(Handware.Manager.Print_heat_factor(15));
        //设置字体大小
        stringToPrint.append(getprintstytle(4));
        //设置字体居中
        stringToPrint.append(Handware.Manager.Print_align(1));
        stringToPrint.append("LYNQ\r\n");
        //设置左对齐
//        stringToPrint.append(Handware.Manager.Print_align(0));
//        stringToPrint.append(getprintstytle(2));
//        stringToPrint.append("==============================\r\n");
//
//        stringToPrint.append(getprintitem("(MERCHANT NAME)", "DemoName"));
//        stringToPrint.append(getprintitem("(MERCHANT NO.)", "20321545656687"));
//        stringToPrint.append(getprintitem("(TERMINAL NO.)", "25689753"));
//        stringToPrint.append(getprintitem("(OPERATOR NO.)", "01"));
//        stringToPrint.append(getprintitem("(ISS NO)", "1544"));
//        stringToPrint.append(getprintitem("(ACQ NO)", "0121"));
//
//        stringToPrint.append(getprintstytle(1));
//        stringToPrint.append("(CARD NUMBER)\r\n");
//
//        stringToPrint.append(getprintstytle(3));
//        stringToPrint.append("62179390*****3426");
//        stringToPrint.append("\r\n");
//
//
//        stringToPrint.append(getprintstytle(1));
//        stringToPrint.append("(TRANS TYPE)");
//        stringToPrint.append("\r\n");
//        stringToPrint.append(getprintstytle(3));
//        stringToPrint.append("(S)");
//        stringToPrint.append("\r\n");
//
//        stringToPrint.append(getprintitem("(EXP DATE)", "2029"));
//
//        stringToPrint.append(getprintitem("(BATCH NO)", "000012"));
//        stringToPrint.append(getprintitem("(VOUCHER NO)", "000001"));
//        stringToPrint.append(getprintitem("(AUTH NO)", "56890547"));
//
//        stringToPrint.append(getprintitem("(DATE/TIME)", "2016-05-23 16:50:32"));
//        stringToPrint.append(getprintitem("(REFER NO)", "365897453214"));
//
//        stringToPrint.append(getprintstytle(1));
//        stringToPrint.append("(AMOUNT)");
//        stringToPrint.append("\r\n");
//
//        stringToPrint.append(getprintstytle(3));
//        stringToPrint.append("RMB ");
//        stringToPrint.append("10.00");
//        stringToPrint.append("\r\n");
//
//        stringToPrint.append(getprintstytle(2));
//        stringToPrint.append("==============================\r\n");
//
//        stringToPrint.append(getprintstytle(1));
//        //输出空行
//        stringToPrint.append(Handware.Manager.Print_row_space(24));
//        stringToPrint.append("(CARD HOLDER SIGNATURE)\r\n");
//        stringToPrint.append(Handware.Manager.Print_row_space(72));
//        stringToPrint.append("--------------------------------------------\r\n");
//
//        stringToPrint.append("I ACKNOWLEDGE	SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES\r\n");

        stringToPrint.append(getprintstytle(2));
        stringToPrint.append("(MERCHANT COPY)\r\n");

        stringToPrint.append(Handware.Manager.Print_row_space(24));

        if (SignActivity.mBitmap != null)
            stringToPrint.append(SignActivity.mBitmap);

        stringToPrint.append(Handware.Manager.Print_row_space(108));
        osl_print_add(stringToPrint.toString());
//        PrintWriteWithPage();

        stringToPrint.append(getprintstytle(1));
        stringToPrint.append("---X---X---X---X---X--X--X--X--X--X--X--X--X--\r\n");

        stringToPrint.append(Handware.Manager.Print_row_space(108));

        return stringToPrint.toString();
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
