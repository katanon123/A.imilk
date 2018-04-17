package com.example.user.restuarant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.user.restuarant.commonclass.ReadJson;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private GoogleApiClient client;
    String type;
    private ReadJson readJson;
    String webservicepath;
    String url,Language;
    String p128,p129,p130,p142,p172;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        type = intent.getStringExtra("Tye_Id");


        SharedPreferences sp = getSharedPreferences("GetCategoryByTypeId", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ""
        SharedPreferences.Editor editor = sp.edit(); // Create Session
        editor.putString("sTye_Id", type);
        editor.commit();

        webservicepath = getResources().getString(R.string.path_webservice);
        getMassage();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.service).setText(p128));

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.cart).setText(p142));

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.promotion).setText(p172));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.more).setText(p130));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#FF000000")));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getMassage() {
        readJson = new ReadJson();
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        Language = cookieLanguage.getString("sName", "Thai");
        HashMap<String, String> map;
        url = webservicepath+"GetTextmessage";
        String resultJson = "GetTextmessageResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);//Object
            JSONArray data = jsonObMain.getJSONArray(resultJson);//Array
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);//Object
                switch(c.getInt("Id")) {
                    case 128://รายการ
                        p128 = c.getString(Language);
                        break;
                    case 129://ข้อมูลส่วนตัว
                        p129 = c.getString(Language);
                        break;
                    case 130://อื่นๆ
                        p130 = c.getString(Language);
                        break;
                    case 142://สถานะการสั่งซื้อ
                        p142 = c.getString(Language);
                        break;

                    case 172://อโปรโมชั่น
                        p172 = c.getString(Language);
                        break;
                    default:
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    } //จบ


}
