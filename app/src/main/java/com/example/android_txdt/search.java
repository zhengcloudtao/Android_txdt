package com.example.android_txdt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class search extends AppCompatActivity {
    EditText search;
    Button qx;
    private List<search_list> searchList = new ArrayList<search_list>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = findViewById(R.id.search);
        search.addTextChangedListener(textWatcher);
        qx = findViewById(R.id.qx);
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(search.this, MainActivity.class)); // 首先需要通过getActivity()方法获取到当前Activity

            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
//
            //  System.out.println("-1-onTextChanged-->"
            //  + search.getText().toString() + "<--");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
//
            //  System.out.println("-2-beforeTextChanged-->"
            //   + search.getText().toString() + "<--");

        }

        @Override
        public void afterTextChanged(Editable s) {
//
            // System.out.println("-3-afterTextChanged-->"
            //  + search.getText().toString() + "<--");

            if (search.getText().toString() != null && search.getText().toString().length() > 0) {
                searchList.clear();
                getjson(search.getText().toString());
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                System.out.println("输入框为口");
                searchList.clear();
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                recyclerView.setVisibility(View.GONE);

            }

        }

        public void getjson(final String keyword) {

            OkHttpClient okhttpClient = new OkHttpClient();
            //创建Request对象
            Request request = new Request.Builder()
                    .url("https://apis.map.qq.com/ws/place/v1/suggestion?keyword=" + keyword + "&key=NYPBZ-D2NW4-CLHUI-DLU4P-BGTFS-Y7BVK")//请求的地址,根据需求带参
                    .build();
            //创建call对象
            Call call = okhttpClient.newCall(request);
            call.enqueue(new Callback() {
                /**
                 * 请求失败后执行
                 *
                 * @param call
                 * @param e
                 */
                @Override
                public void onFailure(Call call, IOException e) {
                    // Toast.makeText(my_bind.this,"异步get方式请求数据失败！",Toast.LENGTH_LONG).show();
                }

                /**
                 * 请求成功后执行
                 *
                 * @param call
                 * @param response
                 * @throws IOException
                 */
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Toast.makeText(my_bind.this,"异步get方式请求数据成功！",Toast.LENGTH_LONG).show();
                            JSONObject obj = null;//最外层的JSONObject对象
                            try {
                                obj = new JSONObject(res);
                                System.out.println(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // JSONObject user = obj.getJSONObject("act");//通过user字段获取其所包含的JSONObject对象
                            String state = null;//通过name字段获取其所包含的字符串
                            String obj1 = null;//最外层的JSONObject对象
                            try {
                                state = obj.getString("data");
                                System.out.println(state);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        /*try {
                            obj1 = state.getString("pois");
                            System.out.println(obj1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                            try {
                                JSONArray name = new JSONArray(state);
                                for (int i = 0; i < name.length(); i++) {
                                    JSONObject object = name.getJSONObject(i);
                                    String address = object.getString("address");
                                    String jname = object.getString("title");
                                    String jlocation = object.getString("location");
                                    //  String sopening_time= jopening_time.replaceAll(" ", "\n");
                                    // String oopening_time=sopening_time.replaceAll("@", " ");
                                    String j1 = jlocation.substring(0, jlocation.indexOf(",\"lng\":"));
                                    String j2 = j1.substring(0, j1.indexOf(":"));
                                    String j3 = j1.substring(j2.length() + 1, j1.length());
                                    System.out.println(j1);
                                    System.out.println(j3);
                                    String j4 = jlocation.substring(0, jlocation.indexOf("}"));
                                    String j5 = j4.substring(0, j4.indexOf("\"lng\":"));
                                    String j6 = j4.substring(j5.length() + 1, j4.length());
                                    String j7 = j6.substring(0, j6.indexOf(":"));
                                    String j8 = j6.substring(j7.length() + 1, j6.length());
                                    System.out.println(j4);
                                    System.out.println(j5);
                                    System.out.println(j6);
                                    System.out.println(j8);
                                    search_list apple = new search_list(jname, address, Double.parseDouble(j3), Double.parseDouble(j8));
                                    searchList.add(apple);
                                    System.out.println(object);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                            if (search.getText().toString().equals(keyword)) {
                                System.out.println("相同");
                            } else {
                                System.out.println("不相同");

                            }
                            if (search.getText().toString() != null && search.getText().toString().length() > 0) {

                            } else {
                                System.out.println("输入框为空");
                                searchList.clear();
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                recyclerView.setVisibility(View.GONE);

                            }
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


                            recyclerView.setLayoutManager(new LinearLayoutManager(search.this));


                        }
                    });
                }
            });
        }

    };
}