package com.smartdevice.aidltestdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.smartdevice.aidltestdemo.adapter.walletAdapter;
import com.smartdevice.aidltestdemo.models.WalletItem;
import com.smartdevice.aidltestdemo.volleyHelpers.CustomJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.views.BannerSlider;

public class UserWalletActivity extends AppCompatActivity {

    String url = "https://api.lynq.eu/api/user_wallet";
    String address;
    JSONObject jsonBody;
    List<WalletItem> walletList = new ArrayList<>();
    GridView walletGrid;
    boolean scrolling=false;
    walletAdapter walletAdapter;
    BannerSlider banner_slider;
    ImageView cross;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet);
        Intent i = getIntent();

        walletGrid= findViewById(R.id.walletGrid);
        banner_slider= findViewById(R.id.banner_slider);
        cross= findViewById(R.id.cross);

        address=i.getStringExtra("address");
        List<Banner> banners=new ArrayList<>();

        banners.add(new DrawableBanner(R.drawable.lynq_banner));
        banners.add(new DrawableBanner(R.drawable.santander));
        banners.add(new DrawableBanner(R.drawable.natwest));

        banner_slider.setBanners(banners);






        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getBaseContext(), MenuActivity.class);
        startActivity(i);
            }
        });






        jsonBody= new JSONObject();
        try {
            jsonBody.put("wallet",address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(Request.Method.POST, url,jsonBody,
                new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                       // System.out.println("Omar");
                        System.out.println(response.toString());
                        JSONArray valueString= new JSONArray();
                        try {
                            Iterator<String> keys= response.getJSONObject(0).keys();
                            while (keys.hasNext())
                            {
                                String keyValue = (String)keys.next();
                                valueString = response.getJSONObject(0).getJSONArray(keyValue);
                            }

                            System.out.println("length: "+valueString.length());
                        for(int i=0;i<valueString.length(); i++)
                        {

                                JSONObject et = valueString.getJSONObject(i);
                                WalletItem wi= new WalletItem();
                                wi.setAccount(et.getString("account"));
                                wi.setAsset(et.getString("asset"));
                                wi.setBalance(Integer.parseInt(et.getString("balance")));
                                wi.setVersion(et.getString("version"));
                                wi.setName(et.getString("name"));
                                wi.setNameShort(et.getString("nameShort"));
                                wi.setIconUrl(et.getString("iconUrl"));
                                wi.setPrice(et.getString("price"));
                                wi.setPath(et.getString("path"));
                                wi.setDescription(et.getString("description"));
                                wi.setCategory(et.getString("category"));
                              //  wi.getSubCategoris().add(et.getString("subcategory"));
                               // wi.setRating(et.getString("rating"));
                                wi.setIssuer(et.getString("issuer"));
                               // et.getJSONObject("modifiers");
                                //wi.setWalletModifiers(new ObjectMapper().readValue(<JSON_OBJECT>, HashMap.class));
                                wi.setVersion(et.getString("version"));
                                wi.setCode(et.getString("code"));

                                walletList.add(wi);




                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                       walletAdapter = new walletAdapter(getBaseContext(), R.layout.list_wallet, walletList);

                        walletGrid.setAdapter(walletAdapter);


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  System.out.println("omar2");
                System.out.println(error.toString());
                //Log.e("TAG", error.getMessage(), error);
            }


        }){


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



        walletGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState!=0)
                {
                    walletAdapter.isScrolling=true;

                }
                else
                {
                    walletAdapter.isScrolling=false;
                    walletAdapter.notifyDataSetChanged();

                }
            }

            public void onScroll(AbsListView view, int firstVisible,
                                 int visibleCount, int total) {


            }});




    }
}
