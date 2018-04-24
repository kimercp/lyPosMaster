package com.smartdevice.aidltestdemo.manager;

import java.util.HashMap;


/**
 * Created by Administrator on 2016-10-18.
 */
public class SignFun {


    public static  int sign_save(HashMap<String, Object> tlv_data , byte[] outjbigencode  )
    {
        int ret;

        HashMap<String, Object> tlv_sign = new HashMap<String, Object>();

        if ( outjbigencode != null && outjbigencode.length > 0 )
        {
            tlv_sign.put( "62",outjbigencode );
        }
        else{
        }


        tlv_sign.put( "63" ,outjbigencode);

        return  0;
    }


}
