package com.example.zhixue;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import static com.example.zhixue.Utils.*;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText password;
    EditText repassword;
    EditText yzm;
    TextView getyzmbtn;
    TextView confirmbtn;
    List<EditText> editTextList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_reset_password);
        TextView tbtitle=findViewById(R.id.tbtitle);
        tbtitle.setText("修改密码");
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        password=findViewById(R.id.npswd);
        repassword=findViewById(R.id.renpswd);
        yzm=findViewById(R.id.yzm);
        editTextList.add(password);
        editTextList.add(repassword);
        editTextList.add(yzm);
        getyzmbtn=findViewById(R.id.getyzm);
        confirmbtn=findViewById(R.id.modbtn);
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String email=sharedPreferences.getString("email","");
        getyzmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getyzm(ResetPasswordActivity.this,getyzmbtn,email);
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkFieldsAndEnableButton(editTextList,confirmbtn);
            }
        };
        for (EditText editText : editTextList) {
            editText.addTextChangedListener(textWatcher);
        }
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = password.getText().toString();
                String repwd = repassword.getText().toString();
                if (!pwd.equals(repwd)) {
                    Toast.makeText(ResetPasswordActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(pwd)) {
                    Toast.makeText(ResetPasswordActivity.this, "密码格式错误！", Toast.LENGTH_SHORT).show();
                } else {
                    String code = yzm.getText().toString();
                    resetPassword(ResetPasswordActivity.this,email, pwd, code);
                }
            }
        });
        confirmbtn.setClickable(false);
    }



}