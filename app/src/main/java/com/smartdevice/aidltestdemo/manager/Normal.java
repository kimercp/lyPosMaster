package com.smartdevice.aidltestdemo.manager;

import android.graphics.Color;
import android.text.Layout;

import com.smartdevice.aidltestdemo.print.PrintFunc;
import com.smartdevice.aidltestdemo.print.PrintTransaction;
import com.smartdevice.aidltestdemo.utils.SpannableStringUtils;
import com.morefun.ypossdk.misc.Misc;

/**
 * Created by Administrator on 2018/03/29.
 */

public class Normal {

    private Object pin;

    public void init(){
        ActionService.Instance().addFunc(new ActionService.ItemListener() {
            @Override
            public int actionitem(String action) {
                //获取密码

//                getPin();
                //
                for (int i = 60 ; i > 0 ; i--){
                    Misc.Sleep(1_000);
                    CommFunc.comm_func_set_comm_page("Connecting Server(" + i +")s",true);
                    if (i == 50){
                        break;
                    }
                }

                String type = "";
                String f15 = "";
                String f37 = "";
                String msg = "";

                byte[] bret = Actions.SignPage(type, "", f15, f37, msg);

                if (bret != null) {

                    SignFun.sign_save(null, bret);//签名数据保存

                }
                PrintTransaction.testPrint("SALE", Misc.AmountToShow(UpayParam.sAmt));
                Actions.ShowAndWait("Trading Success" , "" , "OK");

                return 0;
            }
        });
    }

    public void getPin() {
        SpannableStringUtils.Builder builder = new SpannableStringUtils.Builder();
        int gray = Color.parseColor("#999999");
        if (UpayParam.sAmt.length() > 0) {
            builder.append("sale amount\n").setForegroundColor(gray).setFontSize(24).setAlign(Layout.Alignment.ALIGN_CENTER)
                    .append(String.format("%s\n" ,   Misc.AmountToShow(UpayParam.sAmt) )).setForegroundColor(Color.BLACK).setFontSize(80).setAlign(Layout.Alignment.ALIGN_CENTER);
        }

        if (UpayParam.sPan.length() > 0) {
            String span = PrintFunc.PrintConvertBankCardFormat(UpayParam.sPan);
            builder.append("bank card number\n").setForegroundColor(gray).setFontSize(24).setAlign(Layout.Alignment.ALIGN_CENTER)
                    .append(span +"\n") .setForegroundColor(Color.BLACK).setFontSize(36).setAlign(Layout.Alignment.ALIGN_CENTER)  ;
        }
        String ret = Actions.GetPin("", builder.create(), 4, 12);
    }
}
