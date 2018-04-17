package com.example.user.restuarant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.restuarant.commonclass.ReadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Fragmentetc extends Fragment {

    private LinearLayout more_about,more_contact, more_livechat, about_Linearlayout, contact_Linearlayout,profile_Linearlayout;
    private WebView webView;
    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private String url = "", resultJson,webservicepath;
    private ReadJson readJson;
    TextView about_textView,contact_textView,profile_textView,more_textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View rootview = inflater.inflate(R.layout.activity_fragmentetc, container, false);
        //return inflater.inflate(R.layout.activity_fragmentetc, container, false);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        webservicepath = getResources().getString(R.string.path_webservice);
        webView =(WebView)rootview.findViewById(R.id.webView);
        about_Linearlayout = (LinearLayout)rootview.findViewById(R.id.about_Linearlayout);
        contact_Linearlayout = (LinearLayout)rootview.findViewById(R.id.contact_Linearlayout);
        profile_Linearlayout=(LinearLayout)rootview.findViewById(R.id.profile_Linearlayout);
        about_textView=(TextView)rootview.findViewById(R.id.about_textView);
        contact_textView=(TextView)rootview.findViewById(R.id.contact_textView);
        profile_textView=(TextView)rootview.findViewById(R.id.profile_textView);
        more_textView=(TextView)rootview.findViewById(R.id.more_textView);

        about_Linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("url","about.aspx");
                startActivity(intent);
            }
        });
        contact_Linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("url","Contact.aspx");
                startActivity(intent);
            }
        });

        profile_Linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ViewProfile.class);
                startActivity(intent);
            }
        });

        getMassage();
        return rootview;
    }
    private void getMassage() {
        readJson = new ReadJson();
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getActivity().getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        String Language = cookieLanguage.getString("sName", "English");
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
                    case 165://เกี่ยวกับแอพพลิเคชั่น
                        about_textView.setText(c.getString(Language));
                        break;
                    case 166://ข้อมูลติดต่อ
                        contact_textView.setText(c.getString(Language));
                        break;
                    case 129://ข้อมูลส่วนตัว
                        profile_textView.setText(c.getString(Language));
                        break;
                    case 223://ข้อมูลส่วนตัว
                        more_textView.setText(c.getString(Language));
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
