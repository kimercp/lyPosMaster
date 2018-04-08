package com.smartdevice.aidltestdemo.volleyHelpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

/**
 * Created by Gadour on 05/12/2017.
 */

public class ObjectToJsonParser {

    public JSONObject userSignInJson(String email, String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            String newstring = jsonObject.toString();
            Integer a = 0;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
