package com.example.user.restuarant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.restuarant.commonclass.ReadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePassWord extends AppCompatActivity {
    EditText editoldpassword_textview,editNewPassword,editconfirmnewpassword;
    Button changepassword_button;
    ReadJson readJson;
    String webservicepath,url,Language;
    int UserId;
    TextView confirmnewpassword_textView,newpasswordtextView,oldpassword_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //ไม่ให้keybordเด้งขึ้นอัตโนมัติ
        webservicepath = getResources().getString(R.string.path_webservice);
        editoldpassword_textview = (EditText)findViewById(R.id.editoldpassword_textview);
        editNewPassword = (EditText)findViewById(R.id.editNewPassword);
        editconfirmnewpassword = (EditText)findViewById(R.id.editconfirmnewpassword);
        changepassword_button = (Button)findViewById(R.id.changepassword_button);

        confirmnewpassword_textView=(TextView)findViewById(R.id.confirmnewpassword_textView);
        newpasswordtextView=(TextView)findViewById(R.id.newpasswordtextView);
        oldpassword_textView=(TextView)findViewById(R.id.oldpassword_textView);

        SharedPreferences cookie = getApplication().getSharedPreferences("UserInfo", 0);//เรียกใช้ คุ้กกี้
        UserId  = cookie.getInt("sId", 0);


        SharedPreferences settingsIP = getSharedPreferences("ipServer", 0);
        final String  IP = settingsIP.getString("sIP", "");

        getMassage();

        changepassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(TextUtils.isEmpty(editoldpassword_textview.getText().toString())) {
                    editoldpassword_textview.setError("This field cannot be blank");
                    return;
                }
                else if (TextUtils.isEmpty(editNewPassword.getText().toString())) {
                    editNewPassword.setError("This field cannot be blank");
                    return;
                }
                else if (TextUtils.isEmpty(editconfirmnewpassword.getText().toString())) {
                    editconfirmnewpassword.setError("This field cannot be blank");
                    return;
                }
                else if (!(editNewPassword.getText().toString()).equals(editconfirmnewpassword.getText().toString())){
                    editconfirmnewpassword.setError("The password and confirmation password do not match.");
                    return;
                }
                else
                {

                    readJson = new ReadJson();
                    url = webservicepath+"UserAccount/ChangePassword";
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("Id", UserId);
                        jsonObj.put("NewPassword", editconfirmnewpassword.getText());
                        jsonObj.put("OldPassword",editoldpassword_textview.getText());

                        String resultServer  = readJson.getHttpPost(url,jsonObj);
                        try {
                            JSONObject jsonObjRs = new JSONObject(resultServer);
                            Boolean Success = false;
                            String Message =null;

                            for(int i = 0; i < jsonObjRs.length(); i++){
                                Success = jsonObjRs.getBoolean("Success");
                                Message = jsonObjRs.getString("Message");
                            }
                            if (Success) {

                                final AlertDialog.Builder adb = new AlertDialog.Builder(ChangePassWord.this);
                                adb.setMessage(Message);
                                adb.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        // TODO Auto-generated method stub
                                        SingOut();

                                    }
                                });
                                adb.show();

                            }
                            else {
                                final AlertDialog.Builder adb = new AlertDialog.Builder(ChangePassWord.this);
                                adb.setMessage(Message);
                                adb.setNegativeButton("OK", new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {

                                    }
                                });
                                adb.show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        //Log.e(TAG, "JSONException: " + e);
                    }

                }
            }
        });

    }

    private void getMassage() {
        readJson = new ReadJson();
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        Language = cookieLanguage.getString("sName", "English");
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
                    case 206://เปลี่ยนรหัสผ่าน
                        changepassword_button.setText(c.getString(Language));
                        break;
                    case 227://รหัสผ่านเก่า
                        oldpassword_textView.setText(c.getString(Language));
                        editoldpassword_textview.setHint(c.getString(Language));
                        break;
                    case 228://รหัสผ่านใหม่
                        newpasswordtextView.setText(c.getString(Language));
                        editNewPassword.setHint(c.getString(Language));
                        break;
                    case 224://ยืนยันรหัสผ่าน
                        confirmnewpassword_textView.setText(c.getString(Language));
                        editconfirmnewpassword.setHint(c.getString(Language));
                        break;
                    default:
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    } //จบ



    public void SingOut(){
        final SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        settings.edit().clear().commit();
        this.finish();
        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);




    }
}
