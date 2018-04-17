package com.example.user.restuarant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.restuarant.commonclass.ReadJson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Gallery extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
    private ReadJson readJson;
    String url, resultJson, webservicepath, Idgallery, Namegallery = "";
    GridView list_gallary_gridView;
    TextView no_textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        list_gallary_gridView = (GridView) findViewById(R.id.list_gallary_gridView);
        webservicepath = getResources().getString(R.string.path_webservice);
        Intent intent = getIntent();
        Idgallery = intent.getStringExtra("Id");//รับค่าจากหน้าที่ส่งมาให้
        Namegallery = intent.getStringExtra("Name");//รับค่าจากหน้าที่ส่งมาให้
        no_textView=(TextView)findViewById(R.id.no_textView);
        setTitle(Namegallery);


        getData();
        if(MyArrListTotal.size()<1){
            no_textView.setVisibility(View.VISIBLE);
        }
        list_gallary_gridView.setAdapter(new ImageAdapter(getApplication(), MyArrListTotal));
    }

    //เรียกใช้ ListView promotion_imagView
    private void getData() {
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath+"GetProductByCategory/"+Idgallery;
        resultJson = "GetProductBycateResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("Picture", c.getString("Picture"));
                map.put("Name", c.getString("Name"));
                map.put("Id", c.getString("Id"));
                MyArrListTotal.add(map);
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.promotion_zada, null);
            }


            ImageView gallery_imageView = (ImageView) convertView.findViewById(R.id.gallery_imageView);

            final String url = (MyArr.get(position).get("Picture"));

            if (!url.equals("null")) {
                try {
                    Picasso.with(context)
                            .load(url)
                            .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                            .error(R.drawable.logo_bunchooo)
                            .into(gallery_imageView);//เอารูปตัวไหน
                } catch (Exception e) {

                }
            }

            gallery_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ShowFullSizePhoto().ShowPhoto(url, Gallery.this);
                }
            });
            return convertView;
        }
    }
}
