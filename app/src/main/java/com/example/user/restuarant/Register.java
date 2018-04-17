package com.example.user.restuarant;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.user.restuarant.commonclass.ReadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    private EditText editName, editLastName, editTelephone, editEmail, editPassword, editConfrimPassword, Status ;
    private Button btnRegister;
    private ReadJson readJson;
    String webservicepath;
    String url;
    String Email,FirstName,LastName,MobilePhoneNumber,Language;
    String p225,p121,p226;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        webservicepath = getResources().getString(R.string.path_webservice);
        readJson = new ReadJson();
        editName = (EditText)findViewById(R.id.editName);
        editLastName = (EditText)findViewById(R.id.editLastName);
        editTelephone = (EditText)findViewById(R.id.editTelephone);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPassword = (EditText)findViewById(R.id.editPassword);
        editConfrimPassword = (EditText)findViewById(R.id.editConfrimPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        getMassage();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editName.getText().toString())) {
                    editName.setError("This field is required!");
                    return;
                }
                else if(TextUtils.isEmpty(editLastName.getText().toString())) {
                    editLastName.setError("This field is required!");
                    return;
                }
                else if (TextUtils.isEmpty(editTelephone.getText().toString())) {
                    editTelephone.setError("This field is required!");
                    return;
                }
                else if (TextUtils.isEmpty(editEmail.getText().toString())) {
                    editEmail.setError("This field is required!");
                    return;
                }
                else if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    editPassword.setError("This field is required!");
                    return;
                }
                else if (TextUtils.isEmpty(editConfrimPassword.getText().toString())) {
                    editConfrimPassword.setError("This field is required!");
                    return;
                }
                else if (!editPassword.getText().toString().equals(editConfrimPassword.getText().toString())) {
                    editConfrimPassword.setError("ยืนยันรหัสผ่านไม่ถูกต้อง");
                    return;
                }
                else if(!isEmailValid(editEmail.getText().toString())){
                    editEmail.setError("อีเมล์ไม่ถูกต้อง");
                    return;
                }
                else if(!isValidMobile(editTelephone.getText().toString())){
                    editTelephone.setError("หมายเลขโทรศัพท์ไม่ถูกต้อง");
                    return;
                }
                else {
                    url = webservicepath + "UserAccount/Add";
                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("Email",editEmail.getText().toString());
                        jsonObj.put("FirstName",editName.getText().toString());
                        jsonObj.put("LastName",editLastName.getText().toString());
                        jsonObj.put("MobilePhoneNumber",editTelephone.getText().toString());
                        jsonObj.put("Password",editPassword.getText().toString());

                        String resultServer  = readJson.getHttpPost(url,jsonObj);
                        try {
                            JSONObject jsonObjRs = new JSONObject(resultServer);
                            int Id = 0;
                            for(int i = 0; i < jsonObjRs.length(); i++){
                                Id = jsonObjRs.getInt("Id");
                                Email = jsonObjRs.getString("Email");
                                FirstName = jsonObjRs.getString("FirstName");
                                LastName = jsonObjRs.getString("LastName");
                                MobilePhoneNumber = jsonObjRs.getString("MobilePhoneNumber");
                            }if (Id > 0) {
                                final AlertDialog.Builder adb = new AlertDialog.Builder(Register.this);
                                adb.setMessage(p225);
                                adb.setNegativeButton(p121, new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                adb.show();

                            } else {
                                Toast.makeText(getApplicationContext(), p226, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), p226, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        //Log.e(TAG, "JSONException: " + e);
                        Toast.makeText(getApplicationContext(), p226, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;


        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() != 10)
            {
                check = false;
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
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
                    case 118://ชื่อผู้ใช้
                        editName.setHint(c.getString(Language));
                        break;
                    case 132://นามสกุล
                        editLastName.setHint(c.getString(Language));
                        break;
                    case 134://เบอร์โทรศัพท์
                        editTelephone.setHint(c.getString(Language));
                        break;
                    case 133://อีเมล์
                        editEmail.setHint(c.getString(Language));
                        break;
                    case 119://รหัสผ่าน
                        editPassword.setHint(c.getString(Language));
                        break;
                    case 224://ยืนยันรหัสผ่าน
                        editConfrimPassword.setHint(c.getString(Language));
                        break;
                    case 225://ยืนยันรหัสผ่าน
                        p225 = c.getString(Language);
                        break;
                    case 121://เข้าสู่ระบบ
                        p121 = c.getString(Language);
                        break;
                    case 226://สมัครสมาชิกไม่สำเร็จ!!!
                        p226 = c.getString(Language);
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
