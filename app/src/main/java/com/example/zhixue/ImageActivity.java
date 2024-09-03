package com.example.zhixue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gyf.immersionbar.ImmersionBar;
import com.iflytek.sparkchain.core.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.iflytek.sparkchain.plugins.utils.Utils.TAG;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "AEE_imageUnderstanding";
    private static final int AUDIO_FILE_SELECT_CODE = 1024;

    private ImageView btn_imgInput,btn_arunStart,btn_stop;

    private LinearLayout tv_Notification;

    private EditText ed_textInput;
    private TextView output;
    private String imagePath = null;
    private int token = 0;
    LLM llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_image);
        initSDK();
        initView();

    }

    private void initView(){
        btn_imgInput = findViewById(R.id.image_understanding_imginput);
        btn_arunStart = findViewById(R.id.image_understanding_arun_start_btn);
//        btn_stop = findViewById(R.id.image_understanding_arun_stop_btn);
        tv_Notification = findViewById(R.id.image_understanding_Notification);
        ed_textInput = findViewById(R.id.image_understanding_input_text);
        btn_imgInput.setOnClickListener(this);
        btn_arunStart.setOnClickListener(this);
//        btn_stop.setOnClickListener(this);
        TextView tbtitle=findViewById(R.id.tbtitle);
        tbtitle.setText("图片问答");
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_understanding_imginput) {
            showFileChooser();
        } else if (view.getId() == R.id.image_understanding_arun_start_btn) {
            startChat();
            // syncStartChat();
        }
//        else if (view.getId() == R.id.image_understanding_arun_stop_btn) {
//            stop();
//        }
    }

    private void showFileChooser() {
        Log.d(TAG,"showFileChooser");
        //调用系统文件管理器
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //设置文件格式
        intent.setType("*/*");
        startActivityForResult(intent, AUDIO_FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case AUDIO_FILE_SELECT_CODE:
                if (data != null) {
                    Uri uri = data.getData();
                    String path = GetFilePathFromUri.getFileAbsolutePath(this, uri);
                    imagePath = path;
                }
                showInfo("图片已设置完成:"+imagePath);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG,"imagePath = " + imagePath);
    }

    private void showInfo(String text){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ImageActivity.this,text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLLMConfig(){
        LLMConfig llmConfig = LLMConfig.builder()
                .domain("image");
        Memory window_memory = Memory.windowMemory(5);
        llm = LLMFactory.imageUnderstanding(llmConfig,window_memory);
        llm.registerLLMCallbacks(mLLMCallbacksListener);
    }

    private LLMCallbacks mLLMCallbacksListener = new LLMCallbacks() {
        @Override
        public void onLLMResult(LLMResult llmResult, Object usrContext) {
            if(token == (int)usrContext){
                Log.d(TAG,"onLLMResult\n");
                String content = llmResult.getContent();//获取大模型返回的结果信息
                Log.e(TAG,"onLLMResult:" + content);
                int status = llmResult.getStatus();
                if(content != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            output.append(content);
                            toend();
                        }
                    });
                }
                if(status == 2){//2表示大模型结果返回完成
                    int completionTokens = llmResult.getCompletionTokens();
                    int promptTokens = llmResult.getPromptTokens();//
                    int totalTokens = llmResult.getTotalTokens();
                    Log.e(TAG,"completionTokens:" + completionTokens + "promptTokens:" + promptTokens + "totalTokens:" + totalTokens);
                }
            }
        }
        @Override
        public void onLLMEvent(LLMEvent llmEvent, Object o) {

        }

        @Override
        public void onLLMError(LLMError error, Object o) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    tv_Notification.append("错误:" + " err:" + error.getErrCode() + " errDesc:" + error.getErrMsg() + "\n");
                }
            });
        }
    };

    private void startChat() {
        if(llm == null){
            Log.e(TAG,"startChat failed,please setLLMConfig before!");
            return;
        }
        String usrInputText = ed_textInput.getText().toString();
        Log.d(TAG,"用户输入：" + usrInputText);
        if(usrInputText.length() >= 1){
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.imgtotext, null);
            TextView input = view1.findViewById(R.id.chat_output_text);
            input.append(usrInputText);
            ImageView inputimg=view1.findViewById(R.id.inputimg);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            inputimg.setImageBitmap(bitmap);
            tv_Notification.addView(view1);
            LayoutInflater inflater1 = getLayoutInflater();
            View view2 = inflater.inflate(R.layout.question, null);
            output = view2.findViewById(R.id.chat_output_text);
            tv_Notification.addView(view2);
        }

        token++;
        Log.e(TAG,"SparkChain imagePath:\n" + imagePath);
        int ret = -1;
        if(imagePath!=null) {
            llm.clearHistory();//重新传图片前，需要清空memory，因为memory带有上一次图片的信息
            ret = llm.arun(usrInputText, readFileByBytes(imagePath), token);//首轮会话需要带上图片信息
        }else {
            ret = llm.arun(usrInputText, token);//多轮会话可以不用携带图片信息，SDK会在历史会话中自动拼接图片信息。
        }
        if(ret != 0){
            Log.e(TAG,"SparkChain failed:\n" + ret);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ed_textInput.setText("");
                imagePath = null;//第一轮会话后清空图片信息
            }
        });
    }

    private void stop(){
        if(llm == null){
            Log.e(TAG,"startChat failed,please setLLMConfig before!");
            return;
        }
        llm.stop();
    }

    public void toend(){

    }

    private byte[] readFileByBytes(String fileName) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            Log.e("AEE", "readFileByBytes:" + e.toString());
        }
        byte[] bytes = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            bytes = out.toByteArray();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }
}