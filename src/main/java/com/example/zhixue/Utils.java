package com.example.zhixue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private Utils() {
        throw new AssertionError();
    }

    // 静态方法，可以在不创建对象的情况下调用
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean areAllEditTextsFilled(List<EditText> editTexts){
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                return false;
            }
        }
        return true;
    }
    public static boolean isValidPassword(String password) {
        String regex = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{8,16}$";

        return  password.matches(regex);
    }
    public static void startCountdown(TextView textView) {
        // 设置不可点击
        textView.setClickable(false);
        textView.setTextColor(Color.parseColor("#999999"));

        // 倒计时60秒
        final int duration = 60;
        CountDownTimer countDownTimer = new CountDownTimer(duration * 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // 每秒更新TextView的文本
                textView.setText(millisUntilFinished / 1000 + " 秒后重新获取");
            }

            @Override
            public void onFinish() {
                // 倒计时结束，恢复可点击状态
                textView.setTextColor(Color.parseColor("#336fdf"));
                textView.setText("获取验证码");
                textView.setClickable(true);
            }
        }.start();
    }
    public static String maskEmail(String email) {
        if (email == null || email.isEmpty() ) {
            return email; // 如果邮箱格式不正确或为空，直接返回
        }

        String localPart = email.substring(0, email.indexOf('@')); // 获取本地部分
        String domainPart = email.substring(email.indexOf('@')); // 获取域名部分

        // 如果本地部分只有一个字符，则只保留一个字符
        if (localPart.length() <= 1) {
            return localPart +"*****"+ domainPart;
        }

        // 只保留本地部分的第一个和最后一个字符，中间用"*****"替换
        return localPart.charAt(0) + "*****" + localPart.charAt(localPart.length() - 1) + domainPart;
    }
    public static void getyzm(Activity activity,TextView getyzmbtn,String email) {
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
                    activity.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(activity, responseData, Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                    if(responseData.equals("验证码已发送")){
                        activity.runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              startCountdown(getyzmbtn);
                                          }
                                      }
                        );
                    }

                } else {
                    // 处理错误响应
                    activity.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(activity, "2", Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                }
            }
        });
    }
    public static void checkFieldsAndEnableButton(List<EditText> editTextList,TextView btn) {
        if(areAllEditTextsFilled(editTextList)){
            btn.setClickable(true);
            btn.setBackgroundResource(R.drawable.enablebutton);
            btn.setTextColor(Color.parseColor("#ffffff"));
        }else {
            btn.setClickable(false);
            btn.setBackgroundResource(R.drawable.search_edit_background);
            btn.setTextColor(Color.parseColor("#999999"));
        }
    }
    public static void resetPassword(Activity activity, String email, String pwd, String code) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password",pwd)
                .add("code",code)
                .build();

        Request request = new Request.Builder()
                .url("http://1.92.139.32:8080/code/resetpassword")
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
                    activity.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(activity, responseData, Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                    if(responseData.equals("修改成功")){
                        activity.onBackPressed();
                    }
                } else {
                    // 处理错误响应
                    activity.runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(activity, "2", Toast.LENGTH_SHORT).show();

                                      }
                                  }
                    );
                }
            }
        });
    }
    public class Course{
        Long id;
        String name;
        String teacher_name;
        String school_name;
        String img_path;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public String getImg_path() {
            return img_path;
        }

        public void setImg_path(String img_path) {
            this.img_path = img_path;
        }
    }


}
