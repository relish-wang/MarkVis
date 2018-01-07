package com.hustoj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.hustoj.R;
import com.hustoj.network.ServiceAccessor;
import com.hustoj.util.CSRFConverter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Relish Wang
 * @since 2018/01/07
 */
public class SubmitActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SOURCE = "import java.util.Scanner;\n" +
            "\n" +
            "/**\n" +
            " * @author Relish Wang\n" +
            " * @since 2018/01/07\n" +
            " */\n" +
            "public class Main {\n" +
            "    public static void main(String[] args) {\n" +
            "        Scanner cin = new Scanner(System.in);\n" +
            "        StringBuilder str = new StringBuilder(cin.nextLine());\n" +
            "        System.out.println(str.reverse());\n" +
            "    }\n" +
            "}\n";
    public static final String INPUT = "I am a student";

    ImageView ivVCode;
    EditText etVCode;
    Button btnRun;
    Button btnReload;
    private String csrf;
    private EditText etSource;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        etVCode = findViewById(R.id.et_vcode);
        ivVCode = findViewById(R.id.iv_vcode);
        ivVCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadVCode(String.valueOf(Math.random()));
            }
        });
        loadVCode("");
        btnRun = findViewById(R.id.btn_run);
        btnRun.setOnClickListener(this);
        ServiceAccessor.getHttpService().csrf().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull final Response<String> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String body = response.body();
                        body += "</input>";
                        csrf = CSRFConverter.convert(body);
                        btnRun.setClickable(true);
                        Toast.makeText(SubmitActivity.this, csrf, Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(SubmitActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        etSource = findViewById(R.id.et_source);
        etSource.setText(SOURCE);
        webView = findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        btnReload = findViewById(R.id.btn_reload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
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

    @Override
    public void onClick(View view) {
        String vcode = etVCode.getText().toString().trim();
        String source = etSource.getText().toString().trim();
        ServiceAccessor.getHttpService().submit("1050", 3, vcode, source, csrf).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String html = response.body();
                Toast.makeText(SubmitActivity.this, html, Toast.LENGTH_SHORT).show();
                webView.loadData(html, "text/html", "UTF-8");
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(SubmitActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SubmitActivity.class);
        context.startActivity(intent);
    }
}
