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
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.zhixue.Utils.isValidEmail;

public class LoginActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    TextView loginButton;
    TextView yzmLogin;
    TextView registerbtn;
    TextView forgottenbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // 用户已登录，跳转到主界面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // 关闭当前的Launcher Activity
        }
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.layout_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        yzmLogin=findViewById(R.id.yzmlogin);
        registerbtn=findViewById(R.id.registerbtn);
        registerbtn.setText("注册");
        forgottenbtn=findViewById(R.id.forgottenbtn);
        forgottenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgottenPasswordActivity.class);
                startActivity(intent);
            }
        });
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
        password.addTextChangedListener(new TextWatcher() {
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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ema = email.getText().toString();
                String pwd = password.getText().toString();
                if (isValidEmail(ema)) {
                    login(ema, pwd);
                } else {
                    Toast.makeText(LoginActivity.this, "邮箱错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginButton.setClickable(false);
        yzmLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LoginAvtivity1.class);
                startActivity(intent);
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }



    @SuppressLint("ResourceAsColor")
    private void checkFieldsAndEnableButton() {
        // 检查文本框是否都已填写
        if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            // 启用按钮并改变颜色
            loginButton.setClickable(true);
            loginButton.setBackgroundResource(R.drawable.enablebutton);
            loginButton.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            // 禁用按钮并恢复颜色
            loginButton.setClickable(false);
            loginButton.setBackgroundResource(R.drawable.search_edit_background);
            loginButton.setTextColor(ContextCompat.getColor(this, R.color.text));
        }
    }
    private void login(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

        Request request = new Request.Builder()
                .url("http://1.92.139.32:8080/user/login")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理请求失败的情况
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Toast.makeText(LoginActivity.this,"3", Toast.LENGTH_SHORT).show();

                                  }
                              }
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    // 处理成功的响应
                    String responseData = response.body().string();
                    // 将响应数据转换为你需要的对象
                    JsonResponse jsonResponse=gson.fromJson(responseData, JsonResponse.class);
                    String msg = jsonResponse.getData();
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(LoginActivity.this,msg, Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                    if(msg.equals("登陆成功")){
                        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", email);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                } else {
                    // 处理错误状态码
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(LoginActivity.this,"2", Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                }
            }
        });
    }


}