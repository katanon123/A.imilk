package com.example.user.restuarant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.user.restuarant.commonclass.ReadJson;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListService extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    private ReadJson readJson;
    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
    private String Type = "", Id = "", Name = "", Picture = "", Price = "", IdCategory,p122,p123,p124;
    private String url = "", resultJson, Date1, Date2, CheckInDate = "", Date, Time, CheckOutDate = "",Language;
    private int TicketId = 0;
    private int Category = 1;
    private ListView service_activity_listView;
    String webservicepath;
    private TextView no_menu_textView;
    private SearchView tabListSearch;
    Adapter adapter;


    private GoogleApiClient client9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_service);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        webservicepath = getResources().getString(R.string.path_webservice);
        service_activity_listView = (ListView) findViewById(R.id.service_activity_listView);
        no_menu_textView = (TextView) findViewById(R.id.no_menu_textView);
        tabListSearch = (SearchView) findViewById(R.id.tabListSearch);


        Intent intent = getIntent();
        Type = intent.getStringExtra("Type");//รับค่าจากหน้าที่ส่งมาให้
        IdCategory = intent.getStringExtra("Id");//รับค่าจากหน้าที่ส่งมาให้
        Name = intent.getStringExtra("Name");
        setTitle(Name);
        getMassage();
        SharedPreferences cookie1 = getApplication().getSharedPreferences("DateTime", 0);//เรียกใช้ คุ้กกี้
        Date = cookie1.getString("sDate", "null");
        Time = cookie1.getString("sTime", "null");
        CheckInDate = Date + " " + Time;
        CheckOutDate = Date + " " + Time;

        if (CheckInDate != null && CheckOutDate != null) {
            getData();
            adapter = new Adapter(ListService.this, MyArrListTotal);
            service_activity_listView.setAdapter(adapter);
            if (MyArrListTotal.size() < 1) {
                no_menu_textView.setVisibility(View.VISIBLE);// ถ้าในหน้า รายการของฉันมีเมนูอาหาร ถึงจะโชว์ปุ่มให้กด
            }
        }

        Intent intent1 = getIntent();
        Id = intent1.getStringExtra("Id");
        Name = intent1.getStringExtra("Name");
        Picture = intent1.getStringExtra("Picture");
        Price = intent1.getStringExtra("Price");



        tabListSearch.setOnQueryTextListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client9 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void getData() {
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        Language = cookieLanguage.getString("sName", " ");
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath+"GetProductByCategory/" + IdCategory;
        resultJson = "GetProductBycateResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                //map.put("Name", c.getString("Name"));
                map.put("Price", c.getString("Price"));
                map.put("Status", c.getString("Status"));
                map.put("Id", c.getString("Id"));
                map.put("Picture", c.getString("Picture"));
                map.put("Detail", c.getString("Detail"));
                map.put("PromotionPrice", c.getString("PromotionPrice"));
                String x = c.getString("NameProduct"); //ภาษา
                try {
                    JSONObject  j = new JSONObject(x);
                    if(Language.equals("English")){
                        map.put("Name", j.getString("English"));
                    }
                    else if(Language.equals("Thai")){
                        map.put("Name", j.getString("Thai"));
                    }

                }
                catch (JSONException e){

                }
                MyArrListTotal.add(map);
            }
        } catch (JSONException e) {

            Log.e("error", e.toString());
        }
    }

    public class Adapter extends BaseAdapter implements Filterable {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        private ArrayList<HashMap<String, String>> mStringFilterList = new ArrayList<HashMap<String, String>>();
        ValueFilter valueFilter;

        public Adapter(Context c, ArrayList<HashMap<String, String>> list) {
            context = c;
            MyArr = list;
            mStringFilterList = list;
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
                convertView = inflater.inflate(R.layout.list_service, null);
            }
            TextView list_name_textview = (TextView) convertView.findViewById(R.id.list_servicr_name_textview);
            TextView list_servicr_price_textview = (TextView) convertView.findViewById(R.id.pro_myorder_price_textview);
            TextView list_servicr_status_textview = (TextView) convertView.findViewById(R.id.list_servicr_status_textview);
            TextView Id_textView = (TextView) convertView.findViewById(R.id.list_servicr_Id_textView);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_service_imageview);
            TextView detailimage = (TextView) convertView.findViewById(R.id.list_service_img_textView);
            TextView Detail_textView=(TextView)convertView.findViewById(R.id.Detail_textView);
            LinearLayout promotionLL = (LinearLayout)convertView.findViewById(R.id.promotionLL);
            TextView promotionprice_textview=(TextView)convertView.findViewById(R.id.promotionprice_textview);
            TextView probath_textView=(TextView)convertView.findViewById(R.id.probath_textView);
            TextView Pro_textView =(TextView)convertView.findViewById(R.id.Pro_textView);

            TextView price = (TextView)convertView.findViewById(R.id.pricetextView);
            TextView bath = (TextView)convertView.findViewById(R.id.bath_textView);

            price.setText(p122);
            bath.setText(p123);
            Pro_textView.setText(p124);
            probath_textView.setText(p123);

            list_name_textview.setText(MyArr.get(position).get("Name"));
            //promotionprice_textview.setText(MyArr.get(position).get("PromotionPrice"));
            if ((MyArr.get(position).get("PromotionPrice")).equals("-")) {
                list_servicr_price_textview.setPaintFlags(list_servicr_price_textview.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                promotionLL.setVisibility(View.GONE);
                list_servicr_price_textview.setText(MyArr.get(position).get("Price"));
            }else{
                list_servicr_price_textview.setText(MyArr.get(position).get("Price"));
                list_servicr_price_textview.setPaintFlags(list_servicr_price_textview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                promotionprice_textview.setText(MyArr.get(position).get("PromotionPrice"));
                promotionLL.setVisibility(View.VISIBLE);
            }

            list_servicr_status_textview.setText(MyArr.get(position).get("Status"));
            Id_textView.setText(MyArr.get(position).get("Id"));
            String url = (MyArr.get(position).get("Picture"));
            Detail_textView.setText(MyArr.get(position).get("Detail"));

            detailimage.setText(url);
            if (!url.equals("null")) {
                try {
                    Picasso.with(context)
                            .load(url)
                            .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                            .error(R.drawable.logo_bunchooo)
//                            .resize(imgWidth, imgHeight)//ปรับขนาด
//                            .centerCrop()//ตัดภาพ
                            .into(imageView);//เอารูปตัวไหน
                } catch (Exception e) {

                }
            }

            service_activity_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplication(), ProductDetail.class);//ส่งให้detail
                    intent.putExtra("Id", MyArr.get(position).get("Id"));//ส่งค่า wedservice
                    intent.putExtra("Name", MyArr.get(position).get("Name"));
                    intent.putExtra("Imagedetail", MyArr.get(position).get("Picture"));
                    intent.putExtra("Detail", MyArr.get(position).get("Detail"));
                    startActivity(intent);
                }

            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<HashMap<String, String>> filterList = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> map;
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).get("Name").toUpperCase()).contains(constraint.toString().toUpperCase())) {
                            map = new HashMap<String, String>();
                            map.put("Id", mStringFilterList.get(i).get("Id"));
                            map.put("Name", mStringFilterList.get(i).get("Name"));
                            map.put("Price", mStringFilterList.get(i).get("Price"));
                            map.put("Status", mStringFilterList.get(i).get("Status"));
                            map.put("Picture", mStringFilterList.get(i).get("Picture"));
                            map.put("PromotionPrice", mStringFilterList.get(i).get("PromotionPrice"));
                            filterList.add(map);
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                MyArr = (ArrayList<HashMap<String, String>>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
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
                    case 122://ราคา
                        p122 = c.getString(Language);
                        break;
                    case 123://Bath
                        p123 = c.getString(Language);
                        break;
                    case 124://Bath
                        p124 = c.getString(Language);
                        break;
                    case 203://Bath
                        no_menu_textView.setText(c.getString(Language));
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
