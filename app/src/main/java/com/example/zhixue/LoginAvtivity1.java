package com.example.zhixue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import com.gyf.immersionbar.ImmersionBar;
import okhttp3.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.zhixue.Utils.isValidEmail;
import static com.example.zhixue.Utils.startCountdown;

public class LoginAvtivity1 extends AppCompatActivity {

    EditText email;
    EditText yzm;
    TextView loginbtn;
    TextView getyzmbtn;
    TextView pwdloginbtn;
    TextView registerbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.layout_login1);
        email = findViewById(R.id.email);
        yzm = findViewById(R.id.yzm);
        loginbtn = findViewById(R.id.loginButton);
        getyzmbtn = findViewById(R.id.getyzm);
        pwdloginbtn=findViewById(R.id.pwdlogin);
        registerbtn=findViewById(R.id.registerbtn);
        registerbtn.setText("注册");
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsAndEnableButton();
            }
        });
        yzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsAndEnableButton();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ema = email.getText().toString();
                String code = yzm.getText().toString();
                if (isValidEmail(ema)) {
                    yzmlogin(ema, code);
                } else {
                    Toast.makeText(LoginAvtivity1.this, "邮箱错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        loginbtn.setClickable(false);
        getyzmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ema = email.getText().toString();
                if (isValidEmail(ema)) {

                    getyzm(ema);
                } else {
                    Toast.makeText(LoginAvtivity1.this, "邮箱错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pwdloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginAvtivity1.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getyzm(String email) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .build();

        Request request = new Request.Builder()
                .url("http://1.92.139.32:8080/code")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理请求失败的情况
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 请求成功，处理响应内容
                    String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(LoginAvtivity1.this, responseData, Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                    if(responseData.equals("验证码已发送")){
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              startCountdown(getyzmbtn);
                                          }
                                      }
                        );
                    }

                } else {
                    // 处理错误响应
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(LoginAvtivity1.this, "2", Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                }
            }
        });
    }


    private void yzmlogin(String email,String code) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("code",code)
                .build();

        Request request = new Request.Builder()
                .url("http://1.92.139.32:8080/code/login")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理请求失败的情况
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 请求成功，处理响应内容
                    String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(LoginAvtivity1.this, responseData, Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                    if(responseData.equals("登陆成功")){
                        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", email);
                        editor.apply();
                        Intent intent = new Intent(LoginAvtivity1.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    // 处理错误响应
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(LoginAvtivity1.this, "2", Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                }
            }
        });
    }



    @SuppressLint("ResourceAsColor")
    private void checkFieldsAndEnableButton() {
        // 检查文本框是否都已填写
        if (!email.getText().toString().isEmpty() && !yzm.getText().toString().isEmpty()) {
            // 启用按钮并改变颜色
            loginbtn.setClickable(true);
            loginbtn.setBackgroundResource(R.drawable.enablebutton);
            loginbtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            // 禁用按钮并恢复颜色
            loginbtn.setClickable(false);
            loginbtn.setBackgroundResource(R.drawable.search_edit_background);
            loginbtn.setTextColor(ContextCompat.getColor(this, R.color.text));
        }
    }
}