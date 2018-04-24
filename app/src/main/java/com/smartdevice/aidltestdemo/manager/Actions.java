package com.smartdevice.aidltestdemo.manager;

import android.os.Bundle;

import com.smartdevice.aidltestdemo.activities.ShowMsgActivity;
import com.smartdevice.aidltestdemo.SignActivity;


/**
 * Created by Administrator on 2018/03/29.
 */

public final class Actions {


    //提示 正在读卡
    public static void ShowMsg(CharSequence msg, boolean hascancel) {
        ShowMsg("提示",msg,hascancel);
    }

    public static void GetAmount(){

    }
    public static void ShowMsg(CharSequence title, CharSequence msg, boolean hascancel){
        Bundle param = new Bundle();
        param.putCharSequence("title",title);
        param.putCharSequence("msg",msg);
        param.putString("okBtn",null);
        param.putBoolean("iswait", false);

        if( hascancel ){
            param.putString("cancelBtn","取消");
        }
        else{
            param.putString("cancelBtn",null);
        }

        ActionService.Instance().RunAndWaitUI(ShowMsgActivity.class.getName(),param );
    }
    public static boolean ShowMsg_IsCancel() {
        return ActionService.Instance().IsCancel();
    }


    public static int ShowAndWait(CharSequence title, CharSequence msg, String cancelBtn , String okBtn, int timeout){
        return  ShowAndWait(title,msg, cancelBtn , okBtn,timeout , null);
    }

    public static int ShowAndWait(CharSequence title, CharSequence msg, String cancelBtn , String okBtn, int timeout, String qr){

        Bundle param = new Bundle();
        param.putCharSequence("msg",msg);
        param.putCharSequence("title",title);
        param.putString("okBtn",okBtn);
        param.putString("cancelBtn",cancelBtn);
        param.putBoolean("iswait", true);
        param.putString("qr",qr);
        param.putInt("timeout", timeout);

        Object o = ActionAndWait(ShowMsgActivity.class.getName(),param );

        int ret = 0;
        if(o != null){
            ret =  (Integer) o;
        }

        return ret;
    }
    static Object ActionAndWait(String s, Bundle param){
        IWaitUIRets w = ActionService.Instance().RunAndWaitUI(s, param);
        return  w.WaitForResult();
    }
    public static int ShowAndWait(CharSequence msg, String cancelBtn , String okBtn, int timeout){
        return ShowAndWait(null,msg,cancelBtn,okBtn,timeout);
    }

    public static int ShowAndWait(CharSequence msg, String cancelBtn , String okBtn) {
        return ShowAndWait(msg,cancelBtn,okBtn, 60);
    }

    public final static int Input_Rnd_Passowrd = 3;
    public static String GetPin(CharSequence tip, CharSequence msg  , int minlength , int maxlength )	{
        String ret = null;
        Bundle param = new Bundle();
        param.putCharSequence("tip",tip);
        param.putInt("type",  Input_Rnd_Passowrd);
        param.putInt("maxlength",  maxlength );
        param.putInt("minlength",  minlength );
        param.putCharSequence("msg",  msg );
//        Intent  intent = new Intent();
//        intent.putExtra("")
//        A.startActivity(intent);
        return ret;
    }
    static String getResultString(Object result){
        if( result != null && result.getClass().getSimpleName().equals("String") )
        {
            return (String) result;
        }
        return null;

    }


    public static byte[] SignPage(String title , String xlh, String date, String ckh, String msg )	{
        byte[] ret = null;
        Bundle param = new Bundle();
        param.putString("title", title);
        param.putString("xlh", xlh);
        param.putString("date", date);
        param.putString("ckh", ckh);
        param.putString("msg", msg);

        Object o = ActionAndWait(SignActivity.class.getName(),param );

        if(o != null && o.getClass().getSimpleName().equals("byte[]") ){
            ret = (byte[]) o;
        }
        return ret;
    }
}
