package com.smartdevice.aidltestdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import co.paystack.android.design.widget.PinPadView;

public class PinActivity extends AppCompatActivity {
    PinPadView pinPad;
    String pin1, pin2, userId, cardId, concatenated;
    String url = "https://api.lynq.eu/api/save_card_hash";
    JSONObject jsonBody;
    byte[] hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        jsonBody = new JSONObject();
        pin1 = "";
        pin2 = "";

        Intent i = getIntent();
        userId = i.getStringExtra("qrCode");
        cardId = i.getStringExtra("cardNumber");
//        cardId = "qsd";
//        userId = "XyAiyeirQP91yJvt97iSTfXjE5ytcZKitm";
        pinPad = (PinPadView) findViewById(R.id.pinpadView);

        pinPad.setNumericTextSize(80);
        pinPad.setPlaceDigitsRandomly(false);
        //pinPad.setBackgroundColor();

        pinPad.setOnSubmitListener(new PinPadView.OnSubmitListener() {

            @Override
            public void onCompleted(String pin) {
                if (pin1.equals("")) {
                    pin1 = pin;
                    pinPad.setPromptText("ENTER PIN AGAIN");
                    pinPad.clear();
                } else {
                    pin2 = pin;
                    pinPad.clear();

                    System.out.println(pin2);
                    if (!pin1.equals(pin2)) {
                        pin1 = "";
                        pin2 = "";
                        pinPad.setPromptText("YOU HAVE TO ENTER THE SAME PIN. ENTER PIN AGAIN");
                    } else {
                        pinPad.setPromptText("PERFECT");

                        concatenated = pin1.substring(0, 2) + cardId + pin1.substring(2, 4);
                        System.out.println(concatenated);
                        MessageDigest digest = null;

                        try {
                            digest = MessageDigest.getInstance("SHA-256");
                            hash = digest.digest(concatenated.getBytes(Charset.forName("UTF-8")));
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();

                        }

                        concatenated = concatenated.replace(" ", "");
                        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
                        try {
                            jsonBody.put("hash", concatenated);
                            jsonBody.put("address", userId);
                            //jsonBody=new JSONObject("{card_hash:"+""+","+""+":"+""+"}");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("Omar");
                                        System.out.println(response.toString());
                                        pinPad.setPromptText(response.toString());
                                        if (response.toString().contains("Success")) {
                                            AlertDialog alertDialog = new AlertDialog.Builder(PinActivity.this).create();
                                            alertDialog.setTitle("Card Lynq");
                                            alertDialog.setMessage("Card Lynq Succesful");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            Intent i = new Intent(getBaseContext(), MenuActivity.class);
                                                            startActivity(i);
                                                        }
                                                    });
                                            alertDialog.show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("omar2");
                                System.out.println(error.toString());
                                //Log.e("TAG", error.getMessage(), error);
                            }

                        }) {

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return jsonBody.toString() == null ? null : jsonBody.toString().getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                System.out.println("omar1");
                                Map<String, String> params = new HashMap<>();
                                params.put("Content-Type", "application/json");
                                params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1MDQ1NjQ3NzYsInN1YiI6ImpvaG5AcmVwLndhbGVzIiwiZXhwIjoxNTA1Nzc0Mzc2fQ.TZjgc-Bg46JvgUjbQewX_eIK7SnK2159zbJRXzzZ_OE");
                                return params;
                            }
                        };

                        mQueue.add(jsonObjectRequest);
                    }
                }
            }

            @Override
            public void onIncompleteSubmit(String pin) {
            }
        });
    }


    public void execRequest() {
    }
}

