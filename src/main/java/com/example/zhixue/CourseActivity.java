package com.example.zhixue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.zhixue.Utils.maskEmail;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_course);
        TextView tbtitle=findViewById(R.id.tbtitle);
        tbtitle.setText("课程");
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String email=sharedPreferences.getString("email","");
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .build();
        Request request = new Request.Builder()
                .url("http://1.92.139.32:8080/user/course")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败的情况
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CourseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    List<Utils.Course> courses=gson.fromJson(response.body().string(), new TypeToken<List<Utils.Course>>(){}.getType());;
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          for (int i = 0; i < courses.size(); i++) {
                                              LayoutInflater inflater = getLayoutInflater();
                                              LinearLayout sv = findViewById(R.id.sv);
                                              View view1 = inflater.inflate(R.layout.cardview, null);
                                              TextView cname = view1.findViewById(R.id.cname);
                                              cname.setText(courses.get(i).getName());
                                              TextView teachername = view1.findViewById(R.id.teachername);
                                              teachername.setText( courses.get(i).getTeacher_name());
                                              TextView school = view1.findViewById(R.id.school);
                                              school.setText( courses.get(i).getSchool_name());
                                              sv.addView(view1);
                                          }
                                      }
                                  }
                    );
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CourseActivity.this, "???????", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}