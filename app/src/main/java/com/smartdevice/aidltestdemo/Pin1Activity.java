package com.smartdevice.aidltestdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import co.paystack.android.design.widget.PinPadView;

public class Pin1Activity extends AppCompatActivity {
    PinPadView pinPad;
    String pin1, pin2, userId, cardId, concatenated;
    String url = "https://api.lynq.eu/api/verify_card_hash";
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

        cardId = i.getStringExtra("cardNumber");
        // delete later
        System.out.println(cardId);
        System.out.println(cardId);
        //Toast.makeText(this, cardId, Toast.LENGTH_LONG).show();
        //cardId="3B6E00000031C071C665740B0416310F9000";

        pinPad = (PinPadView) findViewById(R.id.pinpadView);

        pinPad.setNumericTextSize(80);
        pinPad.setPlaceDigitsRandomly(false);

        pinPad.setOnSubmitListener(new PinPadView.OnSubmitListener() {
            @Override
            public void onCompleted(String pin) {
                pin1 = pin;
                pinPad.setPromptText("PERFECT");

                concatenated = pin1.substring(0, 2) + cardId + pin1.substring(2, 4);
                // delete later
                Toast.makeText(Pin1Activity.this, concatenated, Toast.LENGTH_LONG).show();
                MessageDigest digest = null;

                try {
                    digest = MessageDigest.getInstance("SHA-256");
                    hash = digest.digest(concatenated.getBytes(Charset.forName("UTF-8")));
                    System.out.println(hash.toString());
                    System.out.println(hash.toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                concatenated = concatenated.replace(" ", "");

                RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
                try {
                    jsonBody.put("hash", concatenated);
                    // jsonBody.put*("card_hash", hash.toString()); is this line correct? hashed value send to server?
                    System.out.println(concatenated);
                    System.out.println(concatenated);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                System.out.println(response.toString());
                                System.out.println(response.toString());
                                System.out.println(response.toString());
                                try {
                                    pinPad.setPromptText(response.getString("address"));
                                    Intent i = new Intent(getBaseContext(), UserWalletActivity.class);
                                    i.putExtra("address", response.getString("address"));
                                    startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.toString());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1MDQ1NjQ3NzYsInN1YiI6ImpvaG5AcmVwLndhbGVzIiwiZXhwIjoxNTA1Nzc0Mzc2fQ.TZjgc-Bg46JvgUjbQewX_eIK7SnK2159zbJRXzzZ_OE");
                        return params;
                    }
                };

                mQueue.add(jsonObjectRequest);
            }

            @Override
            public void onIncompleteSubmit(String pin) {

            }
        });
    }
}

