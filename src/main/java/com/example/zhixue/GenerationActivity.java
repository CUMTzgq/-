package com.example.zhixue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gyf.immersionbar.ImmersionBar;
import com.iflytek.sparkchain.core.*;

import static com.iflytek.sparkchain.plugins.utils.Utils.TAG;

public class GenerationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AEE_ImageGeneration";
    LLM llm;
    private LinearLayout tv_result;

    private ImageView btn_imageGeneration_arun_start,outputimg;

    private EditText ed_input;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_generation);
        ed_input = findViewById(R.id.imageGeneration_input_text);
        tv_result = findViewById(R.id.imageGeneration_Notification);
        btn_imageGeneration_arun_start = findViewById(R.id.imageGeneration_arun_start_btn);
        btn_imageGeneration_arun_start.setOnClickListener(this);
        TextView tbtitle=findViewById(R.id.tbtitle);
        tbtitle.setText("文生图");
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        initSDK();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.imageGeneration_arun_start_btn) {
            if (llm != null) {
                clearImage();
                showInfo("图片生成中，请稍后.....");
                imageGeneration_arun_start();
            }
        }
    }

    private void initSDK() {
        // 初始化SDK，Appid等信息在清单中配置
        SparkChainConfig sparkChainConfig = SparkChainConfig.builder();
        sparkChainConfig.appID("ffa8bb47")
                .apiSecret("OTAzMWY1MzNkMGZmZjE0YWIwNjljMGU2")
                .apiKey("9aff904d766d215945cc1fdeee95f508")//应用申请的appid三元组
                .logLevel(0);

        int ret = SparkChain.getInst().init(getApplicationContext(),sparkChainConfig);
        if(ret == 0){
            Log.d(TAG,"SDK初始化成功：" + ret);
//            showToast(XunfeiActivity.this, "SDK初始化成功：" + ret);
            setLLMConfig();
        }else{
            Log.d(TAG,"SDK初始化失败：其他错误:" + ret);
//            showToast(XunfeiActivity.this, "SDK初始化失败-其他错误：" + ret);
        }
    }

    private void setLLMConfig(){
        llm = LLMFactory.imageGeneration(1024,1024);
        llm.registerLLMCallbacks(mLLMCallbacksListener);
    }

    private LLMCallbacks mLLMCallbacksListener = new LLMCallbacks() {
        @Override
        public void onLLMResult(LLMResult result, Object o) {
            byte [] bytes = result.getImage();//获取图片生成结果二进制流，大模型一次性返回
            showImage(bytes);
            showInfo("图片生成结束。");
        }

        @Override
        public void onLLMEvent(LLMEvent llmEvent, Object o) {

        }

        @Override
        public void onLLMError(LLMError error, Object o) {
            int errCode = error.getErrCode();
            String errMsg = error.getErrMsg();
            String errInfo = "出错了，错误码：" + errCode + ",错误信息：" + errMsg;
            showInfo(errInfo);
        }
    };

    private void imageGeneration_stop(){
        llm.stop();
    }

    private void imageGeneration_arun_start(){
        String usrInputText = ed_input.getText().toString();
        Log.d("SparkChain","content: " + usrInputText);
        //异步请求
        if(usrInputText.length() >= 1){
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.answer, null);
            TextView input = view1.findViewById(R.id.chat_output_text);
            input.append(usrInputText);
            tv_result.addView(view1);
            LayoutInflater inflater1 = getLayoutInflater();
            View view2 = inflater.inflate(R.layout.texttoimg, null);
            outputimg=view2.findViewById(R.id.outputimg);
            tv_result.addView(view2);
        }
        llm.arun(usrInputText);
        ed_input.setText("");
    }



    private void showImage(byte [] bytes){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);//把二进制图片流转换成图片
                outputimg.setImageBitmap(Bitmap.createScaledBitmap(bmp,bmp.getWidth(),bmp.getHeight(),false));//把图片设置到对应的控件
            }
        });
    }

    private void clearImage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                imageView.setImageDrawable(null);
            }
        });
    }

    private void showInfo(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GenerationActivity.this,text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}