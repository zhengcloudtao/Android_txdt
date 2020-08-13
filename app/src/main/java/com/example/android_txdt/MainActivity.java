package com.example.android_txdt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

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


public class MainActivity extends Activity implements TencentLocationListener {
    MapView mapView;
    MapView mapview = null;
    private List<add> fruitList = new ArrayList<add>();
    private EditText search;
    private ImageButton location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.search);
        search.addTextChangedListener(textWatcher);
        mapview = (MapView) findViewById(R.id.mapview);
        qx = findViewById(R.id.qx);
        qx.setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView1.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 0);
            }
            initTencentLocationRequest();
        }
        TencentLocationManager mLocationManager = TencentLocationManager.getInstance(this);
        mLocationManager.requestLocationUpdates(TencentLocationRequest.create().setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI).setInterval(500).setAllowDirection(true), this);

        TencentMap tencentMap = mapview.getMap();
        location = (ImageButton) findViewById(R.id.location);//id后面为上方button的id
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("定位定位");
                //  setClean();
                setLocation(mapview, getX1(), getY1());

            }
        });
        System.out.println("-----------------" + tencentMap.getCameraPosition());

        tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinished(CameraPosition cameraPosition) {
                TencentMap tencentMap = mapview.getMap();
                System.out.println("-----------------" + tencentMap.getCameraPosition().toString());
                String a = tencentMap.getCameraPosition().toString();

                String b = a.substring(0, a.indexOf(",zoom"));
                System.out.println("---------------" + b);
                String c = b.substring(0, b.indexOf(","));
                String d = b.substring(c.length() + 1, b.length());
                String c1 = c.substring(0, c.indexOf(":"));
                String e = c.substring(c1.length() + 1, c.length());
                System.out.println("c........" + c + "d:" + d);
                System.out.println("c1........" + c1 + "..............e:" + e);
                Geo2AddressParam geo2AddressParam = new Geo2AddressParam();
                geo2AddressParam.getPoi(true);
                LatLng l = new LatLng(Double.parseDouble(e), Double.parseDouble(d));
                geo2AddressParam.location(l);

                fruitList.clear();

                tencentMap.clearAllOverlays();
                LatLng latLng = new LatLng(Double.parseDouble(e), Double.parseDouble(d));
                final Marker marker = tencentMap.addMarker(new MarkerOptions(latLng).
                        position(latLng).
                        title("").
                        snippet(""));
                //创建图标

                OkHttpClient okhttpClient = new OkHttpClient();
                //创建Request对象
                Request request = new Request.Builder()
                        .url("https://apis.map.qq.com/ws/geocoder/v1/?location=" + e + "," + d + "&key=NYPBZ-D2NW4-CLHUI-DLU4P-BGTFS-Y7BVK&get_poi=1")//请求的地址,根据需求带参
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
                                JSONObject state = null;//通过name字段获取其所包含的字符串
                                String obj1 = null;//最外层的JSONObject对象
                                try {
                                    state = obj.getJSONObject("result");
                                    System.out.println(state);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    obj1 = state.getString("pois");
                                    System.out.println(obj1);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONArray name = new JSONArray(obj1);
                                    for (int i = 0; i < name.length(); i++) {
                                        JSONObject object = name.getJSONObject(i);
                                        String address = object.getString("address");
                                        String jname = object.getString("title");
                                        String jlocation = object.getString("location");
                                        System.out.println(jlocation);
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
                                        add apple = new add(jname, address, Double.parseDouble(j3), Double.parseDouble(j8));
                                        fruitList.add(apple);
                                        System.out.println(object);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();


                                }
                                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                                final addAdapter adapter = new addAdapter(fruitList, mapview);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                //recyclerView的点击事件(点击事件是recyclerView专属的自定义回传接口）
                                //不是recyclerView的话点击事件有所不同
                                adapter.setOnRecyclerViewItemClickListener(new addAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        //拿适配器调用适配器内部自定义好的setThisPosition方法（参数写点击事件的参数的position）
                                        adapter.setThisPosition(position);
                                        //嫑忘记刷新适配器
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onLongClick(int position) {

                                    }
                                });

                            }
                        });
                    }
                });


            }
        });
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    System.out.println("未获取焦点");
                } else {
                    System.out.println("获取焦点");

                    mapview = (MapView) findViewById(R.id.mapview);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                    RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
                    recyclerView1.setVisibility(View.GONE);
                    location = (ImageButton) findViewById(R.id.location);//id后面为上方button的id
                    location.setVisibility(View.GONE);
                    mapview.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    qx = findViewById(R.id.qx);
                    qx.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    Button qx;
    private List<search_list> searchList = new ArrayList<search_list>();
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
                RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
                recyclerView1.setVisibility(View.VISIBLE);
            } else {
                System.out.println("输入框为口");
                searchList.clear();
                RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
                recyclerView1.setVisibility(View.GONE);

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
                                RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
                                recyclerView1.setVisibility(View.GONE);

                            }
                            RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recycler_view1);
                            mapview = (MapView) findViewById(R.id.mapview);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                            location = (ImageButton) findViewById(R.id.location);//id后面为上方button的id
                            qx = findViewById(R.id.qx);


                            recyclerView1.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                            final searchAdapter adapter = new searchAdapter(searchList, mapview, recyclerView, recyclerView1, qx, location, search);
                            recyclerView1.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //recyclerView的点击事件(点击事件是recyclerView专属的自定义回传接口）
                            //不是recyclerView的话点击事件有所不同
                            adapter.setOnRecyclerViewItemClickListener(new searchAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int position) {
                                    //拿适配器调用适配器内部自定义好的setThisPosition方法（参数写点击事件的参数的position）

                                    InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    // 隐藏软键盘
                                    imm.hideSoftInputFromWindow(MainActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                                    adapter.setThisPosition(position);
                                    //嫑忘记刷新适配器
                                    adapter.notifyDataSetChanged();

                                }

                                @Override
                                public void onLongClick(int position) {

                                }
                            });

                        }
                    });
                }
            });
        }

    };

    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mapview.onStop();
        super.onStop();
    }

    /**
     * 开启定位监听器
     */
    private void initTencentLocationRequest() {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setInterval(30000).setRequestLevel(4).setAllowGPS(true);
        TencentLocationManager locationManager = TencentLocationManager.getInstance(this);
        int error = locationManager.requestLocationUpdates(request, this);
        System.out.println("///////////////////////////////////////////" + error);
        if (error == 0)
            System.out.println("注册位置监听器成功！");

        else
            System.out.println("注册位置监听器失败！");
    }


    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {

        String msg = null;
        if (TencentLocation.ERROR_OK == 0) {
            System.out.println("定位成功");

            // 定位成功
            if (location != null) {
                StringBuilder sb = new StringBuilder();

                sb.append("来源=").append(location.getProvider())
                        .append("，纬度=").append(location.getLatitude())
                        .append(",经度=").append(location.getLongitude())
                        .append(",海拔=").append(location.getAltitude())
                        .append(",精度=").append(location.getAccuracy())
                        .append(",国家=").append(location.getNation())
                        .append(",省=").append(location.getProvince())
                        .append(",市=").append(location.getCity())
                        .append(",区=").append(location.getDistrict())
                        .append(",镇=").append(location.getTown())
                        .append(",村=").append(location.getVillage())
                        .append(", 街道").append(location.getStreet())
                        .append(", 门号").append(location.getStreetNo())
                        .append(", POI列表").append(location.getPoiList())
                        // 注意, 根据国家相关法规, wgs84坐标下无法提供地址信息
                        .append("{84坐标下不提供地址!}");
                msg = sb.toString();
                double x = getX1();
                double y = getY1();
                final MapView mapView = (MapView) findViewById(R.id.mapview);
                TencentMap tencentMap = mapView.getMap();
                //

                if (getI() == 1) {
                    setL(location.getLatitude(), location.getLongitude());
                    System.out.println("第一次");
                    setLocation(mapview, location.getLatitude(), location.getLongitude());
                    setI(2);
                } else {
                    if (x != location.getLatitude() & y != location.getLongitude()) {
                        //   System.out.println("不同");
                        Circle circle1 = getCircle();
                        circle1.remove();
                        setL(location.getLatitude(), location.getLongitude());
                    } else {
                        //  System.out.println("相同");
                        Circle circle1 = getCircle();
                        circle1.remove();
                        setL(location.getLatitude(), location.getLongitude());
                    }
                }

            }
        } else {
            // 定位失败
        }

    }

    public void setL(double x, double y) {
        final MapView mapView = (MapView) findViewById(R.id.mapview);
        TencentMap tencentMap = mapView.getMap();
        //


        LatLng latLng = new LatLng(x, y);
        Circle circle = tencentMap.addCircle(new CircleOptions().
                center(latLng).
                radius(7d).
                fillColor(0xff05bb07).
                strokeColor(0xffd1e8ce).
                strokeWidth(15));
        setCircle(circle);
        setX1(x);
        setY1(y);


    }

    /*设置个人位置跳转*/
    public static void setLocation(final MapView mapview, double x, double y) {

        System.out.println("定位");
        TencentMap tencentMap = mapview.getMap();

        UiSettings mapUiSettings = tencentMap.getUiSettings();
        CameraUpdate cameraSigma =
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(x, y), //新的中心点坐标
                        17,  //新的缩放级别
                        0, //俯仰角 0~45° (垂直地图时为0)
                        0)); //偏航角 0~360° (正北方为0)
        //移动地图
        tencentMap.moveCamera(cameraSigma);


        System.out.println("定位");


    }

    double x1;
    double y1;
    Circle circle;

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    int i = 1;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}







