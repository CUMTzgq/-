package com.example.zhixue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.gyf.immersionbar.ImmersionBar;
import com.iflytek.sparkchain.core.*;
import io.noties.markwon.Markwon;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.iflytek.sparkchain.plugins.utils.Utils.TAG;

public class XunfeiActivity extends AppCompatActivity {

    private ImageView startChatBtn;
    private LinearLayout chatText;
    private EditText inputText;
    private boolean sessionFinished = true;
    private LLM llm;
    private TextView output;
    private Markwon markwon;
    private StringBuilder markdownBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_xunfei);
        initView();
        initButtonClickListener();
        initSDK();
        markwon = Markwon.builder(this)
                .build();
    }

    private void initView() {
        startChatBtn = findViewById(R.id.chat_start_btn);
        chatText = findViewById(R.id.chat_output_text);
        inputText = findViewById(R.id.chat_input_text);
        TextView tbtitle=findViewById(R.id.tbtitle);
        tbtitle.setText("讯飞星火");
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private float dp2px(Context context, float dipValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (float) (dipValue * scale + 0.5f);
    }

    private void initButtonClickListener() {
        startChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionFinished){
                    startChat();
                    toend();
                } else {
                    Toast.makeText(XunfeiActivity.this, "Busying! Please Wait", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 监听文本框点击时间,跳转到底部
        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toend();
            }
        });
    }

    private void startChat() {
        if(llm == null){
            Log.e(TAG,"startChat failed,please setLLMConfig before!");
            return;
        }
        String usrInputText = inputText.getText().toString();
        Log.d(TAG,"用户输入：" + usrInputText);
        if(usrInputText.length() >= 1){
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.answer, null);
            TextView input = view1.findViewById(R.id.chat_output_text);
            ImageView inputimg=view1.findViewById(R.id.inputimg);
            input.append(usrInputText);
            chatText.addView(view1);
            LayoutInflater inflater1 = getLayoutInflater();
            View view2 = inflater.inflate(R.layout.question, null);
            output = view2.findViewById(R.id.chat_output_text);
            markdownBuilder.setLength(0);
            chatText.addView(view2);
        }

        String myContext = "myContext";

        int ret = llm.arun(usrInputText,myContext);
        if(ret != 0){
            Log.e(TAG,"SparkChain failed:\n" + ret);
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inputText.setText("");
            }
        });

        sessionFinished = false;
        return;
    }

    public void toend(){
//        int scrollAmount = chatText.getLayout().getLineTop(chatText.getLineCount()) - chatText.getHeight();
//        if (scrollAmount > 0) {
//            chatText.scrollTo(0, scrollAmount+10);
//        }
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

    public static void showToast(final Activity context, final String content){

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int random = (int) (Math.random()*(1-0)+0);
                Toast.makeText(context,content,random).show();
            }
        });

    }

    private void setLLMConfig(){
        Log.d(TAG,"setLLMConfig");
        LLMConfig llmConfig = LLMConfig.builder();
        llmConfig.domain("4.0Ultra");
        llmConfig.url("ws(s)://spark-api.xf-yun.com/v4.0/chat");//必填
        //memory有两种，windows_memory和tokens_memory，二选一即可
        Memory window_memory = Memory.windowMemory(5);
        llm = new LLM(llmConfig,window_memory);

//        Memory tokens_memory = Memory.tokenMemory(8192);
//        llm = new LLM(llmConfig,tokens_memory);
        llm.registerLLMCallbacks(llmCallbacks);
    }

    LLMCallbacks llmCallbacks = new LLMCallbacks() {


        @Override
        public void onLLMResult(LLMResult llmResult, Object usrContext) {
            Log.d(TAG,"onLLMResult\n");
            String content = llmResult.getContent();
            Log.e(TAG,"onLLMResult:" + content);

            int status = llmResult.getStatus();
            if(content != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        markdownBuilder.append(content); // 追加新的内容
                        markwon.setMarkdown(output, markdownBuilder.toString());
                        toend();
                    }
                });
            }
            if(usrContext != null) {
                String context = (String)usrContext;
                Log.d(TAG,"context:" + context);
            }
            if(status == 2){
                int completionTokens = llmResult.getCompletionTokens();
                int promptTokens = llmResult.getPromptTokens();//
                int totalTokens = llmResult.getTotalTokens();
                Log.e(TAG,"completionTokens:" + completionTokens + "promptTokens:" + promptTokens + "totalTokens:" + totalTokens);
                sessionFinished = true;
            }
        }

        @Override
        public void onLLMEvent(LLMEvent event, Object usrContext) {
            Log.d(TAG,"onLLMEvent\n");
            Log.w(TAG,"onLLMEvent:" + " " + event.getEventID() + " " + event.getEventMsg());
        }

        @Override
        public void onLLMError(LLMError error, Object usrContext) {
            Log.d(TAG,"onLLMError\n");
            Log.e(TAG,"errCode:" + error.getErrCode() + "errDesc:" + error.getErrMsg());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    chatText.append("错误:" + " err:" + error.getErrCode() + " errDesc:" + error.getErrMsg() + "\n");
                }
            });
            if(usrContext != null) {
                String context = (String)usrContext;
                Log.d(TAG,"context:" + context);
            }
            sessionFinished = true;
        }
    };

}