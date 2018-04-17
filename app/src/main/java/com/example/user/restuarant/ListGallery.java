package com.example.user.restuarant;import android.content.Context;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.os.StrictMode;import android.support.v7.app.AppCompatActivity;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.AdapterView;import android.widget.BaseAdapter;import android.widget.ListView;import android.widget.TextView;import com.example.user.restuarant.commonclass.ReadJson;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import java.util.ArrayList;import java.util.HashMap;public class ListGallery extends AppCompatActivity {    ListView contactLL;    private String url = "", resultJson,webservicepath,Id,Name;    ReadJson readJson;    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_list_gallery);        if (Build.VERSION.SDK_INT > 9) {            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();            StrictMode.setThreadPolicy(policy);        }        webservicepath = getResources().getString(R.string.path_webservice);        Intent intent = getIntent();        Id = intent.getStringExtra("Id");//รับค่าจากหน้าที่ส่งมาให้        Name = intent.getStringExtra("Name");//รับค่าจากหน้าที่ส่งมาให้        setTitle(Name);        contactLL=(ListView)findViewById(R.id.contactLL);        getData();        contactLL.setAdapter(new Adapter(getApplication(), MyArrListTotal));    }    private void getData() {        HashMap<String, String> map;        readJson = new ReadJson();        url = webservicepath + "GetCategoryByTypeId/"+Id;        resultJson = "GetCategoryByTypeIdResult";        String jsonResult = readJson.getHttpGet(url);        try {            JSONObject jsonObMain = new JSONObject(jsonResult);            JSONArray data = jsonObMain.getJSONArray(resultJson);            for (int i = 0; i < data.length(); i++) {                JSONObject c = data.getJSONObject(i);                map = new HashMap<String, String>();                map.put("Id", c.getString("Id"));                map.put("ImageURL", c.getString("ImageURL"));                map.put("Name", c.getString("Name"));                map.put("Type_id", c.getString("Type_id"));                MyArrListTotal.add(map);            }        } catch (JSONException e) {            e.printStackTrace();            Log.e("error", e.toString());        }    }    public class Adapter extends BaseAdapter {        private Context context;        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();        public Adapter(Context c, ArrayList<HashMap<String, String>> list) {            context = c;            MyArr = list;        }        @Override        public int getCount() {            return MyArr.size();        }        @Override        public Object getItem(int position) {            return position;        }        @Override        public long getItemId(int position) {            return position;        }        @Override        public View getView(int position, View convertView, ViewGroup parent) {            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);            if (convertView == null) {                convertView = inflater.inflate(R.layout.gut_branch, null);            }            TextView name_textview = (TextView) convertView.findViewById(R.id.name_textview);            TextView Id_textView = (TextView) convertView.findViewById(R.id.Id_textView);            name_textview.setText(MyArr.get(position).get("Name"));            Id_textView.setText(MyArr.get(position).get("Id"));            contactLL.setOnItemClickListener(new AdapterView.OnItemClickListener() {                @Override                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                    Intent intent = new Intent(ListGallery.this,Gallery.class);                    intent.putExtra("Id", MyArr.get(position).get("Id"));                    intent.putExtra("Name", MyArr.get(position).get("Name"));                    startActivity(intent);                }            });            return convertView;        }    }}