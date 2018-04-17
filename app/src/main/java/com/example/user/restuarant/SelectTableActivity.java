package com.example.user.restuarant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.restuarant.commonclass.ReadJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectTableActivity extends AppCompatActivity {
    private String url = "", resultJson, webservicepath;
    ReadJson readJson;
    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
    ListView Branch_listview;
    LinearLayout selectserviceLL, BranchLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ImageView tableImageView = (ImageView) findViewById(R.id.table_image_view);
        ImageView japantableImageView = (ImageView) findViewById(R.id.japantable_image_view);
        Branch_listview = (ListView) findViewById(R.id.Branch_listview);
        selectserviceLL = (LinearLayout) findViewById(R.id.selectserviceLL);
        BranchLL = (LinearLayout) findViewById(R.id.BranchLL);

        //setTitle("เลือกบริการ");
        webservicepath = getResources().getString(R.string.path_webservice);

        SharedPreferences cookieTable = getApplication().getSharedPreferences("Table", 0);//เรียกใช้ คุ้กกี้
        String Table = cookieTable.getString("sTableType", "null");

        SharedPreferences cookieActiveBranch = getApplication().getSharedPreferences("ActiveBranch", 0);//เรียกใช้ คุ้กกี้
        String ActiveBranch = cookieActiveBranch.getString("sActiveBranch", "null");

//        //เลือกบริการแล้วแต่ยังไม่เลือกสาขา
//        if ((Table.equals("null")) && (ActiveBranch.equals("null"))) {
//            selectserviceLL.setVisibility(View.VISIBLE);
//            BranchLL.setVisibility(View.GONE);
//        } else if ((!Table.equals("null")) && (ActiveBranch.equals("null"))) {
//            selectserviceLL.setVisibility(View.GONE);
//            BranchLL.setVisibility(View.VISIBLE);
//        } else if ((!Table.equals("null")) && (!ActiveBranch.equals("null"))) {
//            Intent intent = new Intent(SelectTableActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        tableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCookie("1");
                selectserviceLL.setVisibility(View.GONE);
                BranchLL.setVisibility(View.VISIBLE);

            }
        });
        japantableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCookie("2");
                selectserviceLL.setVisibility(View.GONE);
                BranchLL.setVisibility(View.VISIBLE);

            }
        });

        getData();
        Branch_listview.setAdapter(new Adapter(getApplication(), MyArrListTotal));
    }

    private void setCookie(String TableType) {
        SharedPreferences sp = getSharedPreferences("Table", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ""
        SharedPreferences.Editor editor = sp.edit(); // Create Session
        editor.putString("sTableType", TableType);
        editor.commit();
    }


    private void getData() {
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath + "GetActiveBranch";
        resultJson = "GetActiveBranchResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("Id", c.getString("Id"));
                map.put("Name", c.getString("Name"));
                MyArrListTotal.add(map);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }

    public class Adapter extends BaseAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public Adapter(Context c, ArrayList<HashMap<String, String>> list) {
            context = c;
            MyArr = list;
        }

        @Override
        public int getCount() {
            return MyArr.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gut_branch, null);
            }
            TextView name_textview = (TextView) convertView.findViewById(R.id.name_textview);
            TextView Id_textView = (TextView) convertView.findViewById(R.id.Id_textView);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_hotel_imageview);


            //name_textview.setText(MyArr.get(position).get("Name"));
            name_textview.setText(MyArr.get(position).get("Name"));
            Id_textView.setText(MyArr.get(position).get("Id"));

            Branch_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SharedPreferences sp = getSharedPreferences("ActiveBranch", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ""
                    SharedPreferences.Editor editor = sp.edit(); // Create Session
                    editor.putString("sActiveBranch", MyArr.get(position).get("Id"));
                    editor.commit();
                    Intent intent = new Intent(SelectTableActivity.this, MainActivity.class);
                    startActivity(intent);
                    //finish();
                }
            });

            return convertView;
        }
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        final SharedPreferences ActiveBranch = getSharedPreferences("ActiveBranch", 0);
        ActiveBranch.edit().clear().commit();
        final SharedPreferences Table = getSharedPreferences("Table", 0);
        Table.edit().clear().commit();
        selectserviceLL.setVisibility(View.VISIBLE);
        BranchLL.setVisibility(View.GONE);
    }
}
