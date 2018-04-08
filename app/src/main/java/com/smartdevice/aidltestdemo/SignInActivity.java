package com.smartdevice.aidltestdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.smartdevice.aidltestdemo.models.User;
import com.smartdevice.aidltestdemo.util.LottieAnimations;
import com.smartdevice.aidltestdemo.volleyHelpers.ObjectToJsonParser;
import com.smartdevice.aidltestdemo.volleyHelpers.VolleyCallback;
import com.smartdevice.aidltestdemo.volleyHelpers.VolleyRequestsHandler;

public class SignInActivity extends AppCompatActivity {


    private EditText etSignInEmail, etSignInPwd;
    private Button btnSignIn;

    private RelativeLayout rlLoading;
    private LottieAnimationView loading;
    private LottieAnimations lottieAnimations;
    private SharedPreferences preferences;

    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //makeFullscreen();

        try {
            ProviderInstaller.installIfNeeded(getBaseContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
        //Initializing values
        lottieAnimations = new LottieAnimations();


        //Bind UI
        etSignInEmail = findViewById(R.id.et_sign_in_email);
        etSignInPwd = findViewById(R.id.et_sign_in_pwd);
        btnSignIn = findViewById(R.id.btn_sign_in);
        rlLoading = findViewById(R.id.rl_sign_in_loading);
        loading = findViewById(R.id.loading);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String login = preferences.getString("login", "");
        if(!login.equals(""))
        {
            etSignInEmail.setText(login);
            etSignInPwd.setText(preferences.getString("password", ""));
        }

        //Sign in action
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email, password;
                email = etSignInEmail.getText().toString();
                password = etSignInPwd.getText().toString();

                //Starting Loading animation
                lottieAnimations.StartSignInLoading(loading, rlLoading);
// sends email and password as JSON to https://api.lynq.eu/api/user and receive token should https://api.lynq.eu/api/login
                new VolleyRequestsHandler(SignInActivity.this).userSignIn(new ObjectToJsonParser().userSignInJson(email, password),
                        new VolleyCallback() {
                            @Override
                            public void onSuccess(Object response) {
                                /*
                                * the login end point return Token
                                * we save the email, password and the returned token to shareprefs then we call
                                * user_info endpoint to retrieve wallet,email,name,lastname and type parameters
                                */
                                final String token = (String) response;
                                User u = new User();
                                u.setToken(token);
                                System.out.println(token);
                                // delete later
                                Toast.makeText(SignInActivity.this, token, Toast.LENGTH_SHORT).show();
                                u.setEmail(email);
                                u.setPassword(password);
                                User.setCurrentUserAfterLogin(u, SignInActivity.this);
                                System.out.println(u.toString());
                                //user_info endpoint
                                new VolleyRequestsHandler(SignInActivity.this).getUserInfo(token, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(Object respons) {

                                        lottieAnimations.StoppingSignInLoading(loading, rlLoading);
                                        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                        editor = preferences.edit();
                                        editor.putString("login", email);
                                        editor.putString("password", password);
                                        editor.apply();

                                        startActivity(new Intent(SignInActivity.this, MenuActivity.class));
                                    }

                                    @Override
                                    public void onError(Object error) {
                                        lottieAnimations.StoppingSignInLoading(loading, rlLoading);

                                    }
                                });

                            }

                            @Override
                            public void onError(Object error) {

                                Toast.makeText(SignInActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                lottieAnimations.StoppingSignInLoading(loading, rlLoading);

                            }
                        });

            }
        });
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
}
