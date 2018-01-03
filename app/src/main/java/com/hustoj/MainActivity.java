package com.hustoj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etUsername, etPassword, etVCode;
    private ImageView ivVCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etVCode = findViewById(R.id.et_vcode);
        ivVCode = findViewById(R.id.iv_vcode);
        ivVCode.setOnClickListener(this);

        Glide.with(this)
                .load("http://zjicm.hustoj.com/vcode.php")
                .into(ivVCode);
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
                break;
        }
    }
}
