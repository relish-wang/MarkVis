package com.hustoj;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hustoj.network.ServiceAccessor;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUsername, etPassword, etVCode;
    private ImageView ivVCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private TextView tv_text;

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etVCode = findViewById(R.id.et_vcode);
        ivVCode = findViewById(R.id.iv_vcode);
        ivVCode.setOnClickListener(this);

        Glide.with(this)
                .load("http://zjicm.hustoj.com/vcode.php")
                .into(ivVCode);

        tv_text = findViewById(R.id.tv_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_vcode:
                double random = Math.random();
                Toast.makeText(this, random+"", Toast.LENGTH_SHORT).show();
                Glide.with(this)
                        .load("http://zjicm.hustoj.com/vcode.php?"+random)
                        .into(ivVCode);
                ServiceAccessor.getHttpService().csrf().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull final Response<String> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String body = response.body();
                                Toast.makeText(MainActivity.this, body, Toast.LENGTH_SHORT).show();
                                tv_text.setText(body);
                            }
                        });
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
