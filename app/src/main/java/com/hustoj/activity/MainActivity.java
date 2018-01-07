package com.hustoj.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.hustoj.R;
import com.hustoj.bean.LoginResult;
import com.hustoj.network.ServiceAccessor;
import com.hustoj.util.CSRFConverter;
import com.hustoj.util.MD5;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername, etPassword, etVCode;
    private ImageView ivVCode;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private TextView tv_text;

    private String csrf;

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etVCode = findViewById(R.id.et_vcode);
        ivVCode = findViewById(R.id.iv_vcode);
        ivVCode.setOnClickListener(this);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);


        loadVCode("");

        tv_text = findViewById(R.id.tv_text);

        mBtnLogin.setClickable(false);
        ServiceAccessor.getHttpService().csrf().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull final Response<String> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String body = response.body();
                        body += "</input>";
                        csrf = CSRFConverter.convert(body);
                        mBtnLogin.setClickable(true);
                        tv_text.setText(csrf);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //TODO
        etUsername.setText("130706236");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_vcode:
                double random = Math.random();
                loadVCode(random + "");
                break;
            case R.id.btn_login:
                String userId = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String pwdMD5 = MD5.md5(password);
                String vcode = etVCode.getText().toString().trim();
                ServiceAccessor.getHttpService().login(userId, pwdMD5, vcode, csrf).enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResult> call, @NonNull Response<LoginResult> response) {
                        LoginResult body = response.body();
                        String script = body.script;
                        if (script.contains("history.go(-2)")) {
                            Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            SubmitActivity.start(MainActivity.this);

                        } else {
                            Toast.makeText(MainActivity.this, script, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResult> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }


    private void loadVCode(String random) {
        GlideUrl cookie = new GlideUrl("http://zjicm.hustoj.com/vcode.php" +
                (TextUtils.isEmpty(random) ? "" : "?" + random), new LazyHeaders.Builder()
                .addHeader("Cookie", "hustoj-SESSID=s1uuue0ui6o1714prtlgkvgik0")
                .build());
        Glide.with(this)
                .load(cookie)
                .into(ivVCode);
    }
}
