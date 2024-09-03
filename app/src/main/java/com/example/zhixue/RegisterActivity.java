package com.example.zhixue;

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
import java.util.ArrayList;
import java.util.List;

import static com.example.zhixue.Utils.*;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText repassword;
    EditText yzm;
    TextView registerbtn;
    TextView getyzmbtn;

    List<EditText> editTextList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        yzm=findViewById(R.id.yzm);
        registerbtn=findViewById(R.id.registerButton);
        getyzmbtn = findViewById(R.id.getyzm);
        editTextList.add(email);
        editTextList.add(password);
        editTextList.add(repassword);
        editTextList.add(yzm);

        TextView loginbtn=findViewById(R.id.loginbtn);
        TextView loginbtn1=findViewById(R.id.loginbtn1);
        ImageButton backbtn=findViewById(R.id.backbtn);
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        };
        loginbtn.setOnClickListener(onClickListener);
        loginbtn1.setOnClickListener(onClickListener);
        backbtn.setOnClickListener(onClickListener);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsAndEnableButton(editTextList);
            }
        };
        for (EditText editText : editTextList) {
            editText.addTextChangedListener(textWatcher);
        }
        getyzmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ema = email.getText().toString();
                if (isValidEmail(ema)) {
                    getyzm(ema);
                } else {
                    Toast.makeText(RegisterActivity.this, "邮箱错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = password.getText().toString();
                String repwd = repassword.getText().toString();
                if (!pwd.equals(repwd)) {
                    Toast.makeText(RegisterActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(pwd)) {
                    Toast.makeText(RegisterActivity.this, "密码格式错误！", Toast.LENGTH_SHORT).show();
                } else {
                    String ema = email.getText().toString();
                    String code = yzm.getText().toString();
                    register(ema, pwd, code);
                }
            }
        });
        registerbtn.setClickable(false);

    }




    private void checkFieldsAndEnableButton(List<EditText> editTextList) {
        if(areAllEditTextsFilled(editTextList)){
            registerbtn.setClickable(true);
            registerbtn.setBackgroundResource(R.drawable.enablebutton);
            registerbtn.setTextColor(ContextCompat.getColor(this, R.color.white));
        }else {
            registerbtn.setClickable(false);
            registerbtn.setBackgroundResource(R.drawable.search_edit_background);
            registerbtn.setTextColor(ContextCompat.getColor(this, R.color.text));
        }
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
                                          Toast.makeText(RegisterActivity.this, responseData, Toast.LENGTH_SHORT).show();

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
                                          Toast.makeText(RegisterActivity.this, "2", Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                }
            }
        });
    }

    private void register(String ema, String pwd, String code) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", ema)
                .add("password",pwd)
                .add("code",code)
                .build();

        Request request = new Request.Builder()
                .url("http://1.92.139.32:8080/code/register")
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
                                          Toast.makeText(RegisterActivity.this, responseData, Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                    if(responseData.equals("注册成功")){
                        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", ema);
                        editor.apply();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    // 处理错误响应
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(RegisterActivity.this, "2", Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                }
            }
        });
    }

}