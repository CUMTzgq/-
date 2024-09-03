package com.example.zhixue;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import static com.example.zhixue.Utils.*;
import static com.example.zhixue.Utils.resetPassword;

public class ForgottenPasswordActivity extends AppCompatActivity {
    EditText email;
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
        setContentView(R.layout.activity_forgotten_password);
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        yzm=findViewById(R.id.yzm);
        editTextList.add(email);
        editTextList.add(password);
        editTextList.add(repassword);
        editTextList.add(yzm);
        getyzmbtn=findViewById(R.id.getyzm);
        confirmbtn=findViewById(R.id.confirm);
        getyzmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ema=email.getText().toString();
                getyzm(ForgottenPasswordActivity.this,getyzmbtn,ema);
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
                String ema=email.getText().toString();
                String pwd = password.getText().toString();
                String repwd = repassword.getText().toString();
                if (!isValidEmail(ema)) {
                    Toast.makeText(ForgottenPasswordActivity.this, "邮箱错误！", Toast.LENGTH_SHORT).show();
                } else if (!pwd.equals(repwd)) {
                    Toast.makeText(ForgottenPasswordActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(pwd)) {
                    Toast.makeText(ForgottenPasswordActivity.this, "密码格式错误！", Toast.LENGTH_SHORT).show();
                } else {
                    String code = yzm.getText().toString();
                    resetPassword(ForgottenPasswordActivity.this, ema, pwd, code);
                }
            }
        });
        confirmbtn.setClickable(false);
    }
}