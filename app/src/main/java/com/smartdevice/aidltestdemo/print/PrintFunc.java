package com.smartdevice.aidltestdemo.print;

import android.graphics.Color;
import android.text.Layout;

import com.smartdevice.aidltestdemo.manager.Actions;
import com.smartdevice.aidltestdemo.utils.SpannableStringUtils;
import com.smartdevice.aidltestdemo.R;
import com.morefun.ypossdk.misc.Handware;
import com.morefun.ypossdk.misc.Misc;


public class PrintFunc {
    private static String m_print_buff = "";

    public static void osl_print_add(String str) {
        m_print_buff += str;
    }

    public static String getPrintStytle(int s){
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

    public static String getPrintItem(String head, String val)
    {
        StringBuffer sb = new StringBuffer();
        sb.append( getPrintStytle(2) );
        sb.append(head);
        sb.append("\r\n");

        sb.append( getPrintStytle(2));
        sb.append(val);
        sb.append("\r\n");
        return sb.toString();
    }



    //输出到打印机
    public static int PrintWriteWithPage() {
        int ret = 0;
        boolean print = true;

        Handware.Manager.PrintInit();

        while (print) {
            SpannableStringUtils.Builder builder =new SpannableStringUtils.Builder();
            builder.append("\n").setResourceId(R.drawable.icon_trade_success,SpannableStringUtils.ALIGN_CENTER)
                    .append("printing ticket").setFontSize(36).setForegroundColor(Color.BLACK).setAlign(Layout.Alignment.ALIGN_CENTER);
            Actions.ShowMsg("msg", builder.create(), false);
            byte[] tmp = m_print_buff.getBytes();

            ret = Handware.Manager.PrintWrite(m_print_buff);        //输出到打印机

            //if (ret != tmp.length )	{
            if (ret != 0) {
                    builder =new SpannableStringUtils.Builder();
                    builder.append("\n").setResourceId(R.drawable.icon_trade_success,SpannableStringUtils.ALIGN_CENTER)
                            .append("The printer is out of paper. Please fill it up").setFontSize(36).setForegroundColor(Color.BLACK).setAlign(Layout.Alignment.ALIGN_CENTER);
                    int sel = Actions.ShowAndWait(builder.create(), "Cancel", "retry");
                    if (sel != 1) {
                        print = false;
                    }
            } else {
                print = false;            // 成功退出
            }
        }

        return ret;
    }

    //屏蔽银行卡
    public static String PrintConvertBankCardFormat(String card) {
        byte[] buff = card.getBytes();
        int nLen, i;

        nLen = buff.length;

        if (nLen < 10) {
            return "";
        }

        for (i = 6; i < nLen - 4; i++) {
            buff[i] = '*';
        }

        return Misc.BytesToStr(buff);

    }
}
