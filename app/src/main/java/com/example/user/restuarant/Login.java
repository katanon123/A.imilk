package com.example.user.restuarant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.restuarant.commonclass.ReadJson;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private EditText editemail, editpassword;
    private Button login_button ;
    private ReadJson readJson;
    String webservicepath;
    String url,Language;
    ImageView uk_imageView,th_imageView;
    String p101,p102,p185;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        webservicepath = getResources().getString(R.string.path_webservice);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //ไม่ให้keybordเด้งขึ้นอัตโนมัติ

        readJson = new ReadJson();
        editemail = (EditText) findViewById(R.id.editemail);
        editpassword = (EditText) findViewById(R.id.editpassword);
        login_button = (Button) findViewById(R.id.login_button);

        uk_imageView=(ImageView)findViewById(R.id.uk_imageView);
        th_imageView=(ImageView)findViewById(R.id.th_imageView);

        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        Language = cookieLanguage.getString("sName", " ");

        SharedPreferences cookie = getSharedPreferences("UserInfo", 0);//เรียกใช้ คุ้กกี้
        int UserId  = cookie.getInt("sId", 0);


        if(UserId>0){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(Language.equals(" ")){
            Language = "Thai";
            SharedPreferences sp = getSharedPreferences("Language", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ"ภาษาไทย
            SharedPreferences.Editor editor = sp.edit(); // Create Session
            editor.putString("sName", "Thai");
            editor.commit();
            getMassage();
        }

        uk_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("Language", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ""ภาษาอังกฤษ
                SharedPreferences.Editor editor = sp.edit(); // Create Session
                editor.putString("sName", "English");
                editor.commit();
                Language = "English";
                getMassage();

//                Intent intent = new Intent(getApplication(),MainActivity.class);
//                startActivity(intent);
//                finish();
            }
        });

        th_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("Language", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ"ภาษาไทย
                SharedPreferences.Editor editor = sp.edit(); // Create Session
                editor.putString("sName", "Thai");
                editor.commit();
                Language = "Thai";
                getMassage();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editemail.getText().toString())) {
                    editemail.setError(p185);
                    return;
                } else if (TextUtils.isEmpty(editpassword.getText().toString())) {
                    editpassword.setError(p185);
                    return;
                } else {
                    url = webservicepath+"UserAccount/SignIn";
                    JSONObject jsonObj = new JSONObject();

                    try {
                        jsonObj.put("Email", editemail.getText().toString());
                        jsonObj.put("Password", editpassword.getText().toString());
                        String resultServer = readJson.getHttpPost(url, jsonObj);
                        try {
                            JSONObject jsonObjRs = new JSONObject(resultServer);
                            int Id = 0;

                            String FirstName = "";
                            String LastName = "";
                            String Email = "";

                            for (int i = 0; i < jsonObjRs.length(); i++) {
                                Id = jsonObjRs.getInt("Id");
                                FirstName = jsonObjRs.getString("FirstName");
                                LastName = jsonObjRs.getString("LastName");
                                Email = jsonObjRs.getString("Email");
                            }

                            if (Id > 0) {
                                SharedPreferences sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ""
                                SharedPreferences.Editor editor = sp.edit(); // Create Session
                                editor.putInt("sId", Id);//ค่าที่จะเก็บค่าที่ต้องการ
                                editor.putString("sFirstName", FirstName);
                                editor.putString("sLastName", LastName);
                                editor.putString("sEmail", Email);
                                editor.commit();

                                SharedPreferences Language = getSharedPreferences("Language", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ"ภาษาไทย
                                SharedPreferences.Editor editor2 = sp.edit(); // Create Session
                                editor2.putString("sName", "Thai");
                                editor2.commit();
                                getMassage();
                                Toast.makeText(getApplicationContext(), p101, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), p102, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), p102, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), p102, Toast.LENGTH_LONG).show();
                    }
                }
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
                    case 100://ลงชื่อเข้าใช้
                        login_button.setText(c.getString(Language));
                        break;
                    case 101://เข้าสู่ระบบสำเร็จ
                        p101 = c.getString(Language);
                        break;
                    case 102://เข้าสู่ระบบไม่สำเร็จ!!!
                        p102 = c.getString(Language);
                        break;
                    case 118://ชชื่อผู้ใช้
                        //p118 = c.getString(Language);
                        editemail.setHint(c.getString(Language));
                        break;
                    case 119://เข้าสู่ระบบไม่สำเร็จ!!!
                        //p119 = c.getString(Language);
                        editpassword.setHint(c.getString(Language));
                        break;
                    case 185://กรุณาใส่ข้อมูล
                        p185 = c.getString(Language);
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
