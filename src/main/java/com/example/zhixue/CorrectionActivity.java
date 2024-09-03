package com.example.zhixue;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CorrectionActivity extends AppCompatActivity {
    public static final String hostUrl = "https://api.xf-yun.com/v1/private/s9a87e3ec";
    public static final String appid = "71771e92";
    public static final String apiSecret = "ZmYyMmJhNTFhNWFkMjQ0MDczMGUwMGI3";
    public static final String apiKey = "57e8f7d1cc72a8f6b731a0f39e9719df";
    public static  String  text;

    EditText input;
    ImageView correctionbtn;
    LinearLayout sv;
    TextView output;

    class CorrectionInfo {
        List<List<String>> black_list;
        List<List<String>> punc;
        List<List<String>> leader;
        List<List<String>> org;
        List<List<String>> pol;
        List<List<String>> grammar_pc;
        List<List<String>> order;
        List<List<String>> idm;
        List<List<String>> word;
        @SerializedName("char")
        List<List<String>> char_list;
        List<List<String>> redund;
        List<List<String>> miss;
        List<List<String>> dapei;
        List<List<String>> number;
        List<List<String>> addr;
        List<List<String>> name;

        public List<List<String>> getIdm() {
            return idm;
        }

        public void setIdm(List<List<String>> idm) {
            this.idm = idm;
        }

        public List<List<String>> getBlack_list() {
            return black_list;
        }

        public void setBlack_list(List<List<String>> black_list) {
            this.black_list = black_list;
        }

        public List<List<String>> getPunc() {
            return punc;
        }

        public void setPunc(List<List<String>> punc) {
            this.punc = punc;
        }

        public List<List<String>> getLeader() {
            return leader;
        }

        public void setLeader(List<List<String>> leader) {
            this.leader = leader;
        }

        public List<List<String>> getOrg() {
            return org;
        }

        public void setOrg(List<List<String>> org) {
            this.org = org;
        }

        public List<List<String>> getPol() {
            return pol;
        }

        public void setPol(List<List<String>> pol) {
            this.pol = pol;
        }

        public List<List<String>> getGrammar_pc() {
            return grammar_pc;
        }

        public void setGrammar_pc(List<List<String>> grammar_pc) {
            this.grammar_pc = grammar_pc;
        }

        public List<List<String>> getOrder() {
            return order;
        }

        public void setOrder(List<List<String>> order) {
            this.order = order;
        }

        public List<List<String>> getWord() {
            return word;
        }

        public void setWord(List<List<String>> word) {
            this.word = word;
        }

        public List<List<String>> getChar_list() {
            return char_list;
        }

        public void setChar_list(List<List<String>> char_list) {
            this.char_list = char_list;
        }

        public List<List<String>> getRedund() {
            return redund;
        }

        public void setRedund(List<List<String>> redund) {
            this.redund = redund;
        }

        public List<List<String>> getMiss() {
            return miss;
        }

        public void setMiss(List<List<String>> miss) {
            this.miss = miss;
        }

        public List<List<String>> getDapei() {
            return dapei;
        }

        public void setDapei(List<List<String>> dapei) {
            this.dapei = dapei;
        }

        public List<List<String>> getNumber() {
            return number;
        }

        public void setNumber(List<List<String>> number) {
            this.number = number;
        }

        public List<List<String>> getAddr() {
            return addr;
        }

        public void setAddr(List<List<String>> addr) {
            this.addr = addr;
        }

        public List<List<String>> getName() {
            return name;
        }

        public void setName(List<List<String>> name) {
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
        setContentView(R.layout.activity_correction);
        input=findViewById(R.id.input_text);
        correctionbtn=findViewById(R.id.correction_btn);
        sv=findViewById(R.id.sv);
        TextView tbtitle=findViewById(R.id.tbtitle);
        tbtitle.setText("文本纠错");
        ImageButton backbtn=findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        correctionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text=input.getText().toString();
                input.setText("");
                LayoutInflater inflater = getLayoutInflater();
                View view1 = inflater.inflate(R.layout.answer, null);
                TextView input = view1.findViewById(R.id.chat_output_text);
                input.setText(text);
                sv.addView(view1);
                String url = null;
                try {
                    url = getAuthUrl(hostUrl, apiKey, apiSecret);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String json = getRequestJson();
                doPostJson(url, json);
            }
        });


    }

    public static String getRequestJson() {
        return "{\n" +
                "  \"header\": {\n" +
                "    \"app_id\": \"" + appid + "\",\n" +
                //"    \"uid\": \"XXXXX\",\n" +
                "    \"status\": 3\n" +
                "  },\n" +
                "  \"parameter\": {\n" +
                "    \"s9a87e3ec\": {\n" +
                //"    \"res_id\": \"XXXXX\",\n" +
                "      \"result\": {\n" +
                "        \"encoding\": \"utf8\",\n" +
                "        \"compress\": \"raw\",\n" +
                "        \"format\": \"json\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"payload\": {\n" +
                "    \"input\": {\n" +
                "      \"encoding\": \"utf8\",\n" +
                "      \"compress\": \"raw\",\n" +
                "      \"format\": \"plain\",\n" +
                "      \"status\": 3,\n" +
                "      \"text\": \"" + getBase64TextData(text) + "\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

    public static String getBase64TextData(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    public void doPostJson(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败的处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        output.setText(e.toString());
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功的处理逻辑
                if (response.isSuccessful()) {
                    // 获取响应字符串
                    String responseString = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Gson gson =new Gson();
                                JsonParse jsonParse = gson.fromJson(responseString, JsonParse.class);
                                String base64Decode = new String(Base64.getDecoder().decode(jsonParse.payload.result.text), StandardCharsets.UTF_8);

                                // 解析纠错信息
                                CorrectionInfo correctionInfo = gson.fromJson(base64Decode, CorrectionInfo.class);
                                for (int i = 0; i < correctionInfo.getBlack_list().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getBlack_list().get(i).get(1)+"→"+correctionInfo.getBlack_list().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getIdm().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getIdm().get(i).get(1)+"→"+correctionInfo.getIdm().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getPunc().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getPunc().get(i).get(1)+"→"+correctionInfo.getPunc().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getChar_list().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getChar_list().get(i).get(1)+"→"+correctionInfo.getChar_list().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getOrg().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getOrg().get(i).get(1)+"→"+correctionInfo.getOrg().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getPol().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getPol().get(i).get(1)+"→"+correctionInfo.getPol().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getOrder().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getOrder().get(i).get(1)+"→"+correctionInfo.getOrder().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getGrammar_pc().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getGrammar_pc().get(i).get(1)+"→"+correctionInfo.getGrammar_pc().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getWord().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getWord().get(i).get(1)+"→"+correctionInfo.getWord().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getName().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getName().get(i).get(1)+"→"+correctionInfo.getName().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getRedund().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getRedund().get(i).get(1)+"→"+correctionInfo.getRedund().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getMiss().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getMiss().get(i).get(1)+"→"+correctionInfo.getMiss().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getNumber().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getNumber().get(i).get(1)+"→"+correctionInfo.getNumber().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getLeader().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getLeader().get(i).get(1)+"→"+correctionInfo.getLeader().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getDapei().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getDapei().get(i).get(1)+"→"+correctionInfo.getDapei().get(i).get(2));
                                    sv.addView(view1);
                                }
                                for (int i = 0; i < correctionInfo.getAddr().size(); i++) {
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.question, null);
                                    TextView input = view1.findViewById(R.id.chat_output_text);
                                    input.setText(correctionInfo.getAddr().get(i).get(1)+"→"+correctionInfo.getAddr().get(i).get(2));
                                    sv.addView(view1);
                                }
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        output.setText(e.toString());
                                    }
                                });
                            }
                        }
                    });


                    // 这里可以处理响应字符串，例如将其发布到主线程的 UI 组件上

                    // 注意：不要在后台线程上直接更新 UI，需要使用 Handler 或其他机制切换到主线程
                } else {
                    output.setText("2");
                    // 处理错误响应
                }
            }
        });



    }


    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "POST " + url.getPath() + " HTTP/1.1";
        //System.out.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        return httpUrl.toString();
    }

    static class JsonParse {
        Payload payload;
    }

    static class Payload {
        Result result;
    }

    static class Result {
        String text;
    }
}