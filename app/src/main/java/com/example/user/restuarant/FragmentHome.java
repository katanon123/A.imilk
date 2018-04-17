package com.example.user.restuarant;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.restuarant.commonclass.ReadJson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentHome extends Fragment {
    ListView promotion_listview;
    private String url = "", resultJson,webservicepath,Id,Name;
    ReadJson readJson;
    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.activity_fragment_home, container, false);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webservicepath = getResources().getString(R.string.path_webservice);
        promotion_listview = (ListView)rootview.findViewById(R.id.promotion_listview);

        new AsyncCaller().execute();
        return rootview;
    }

    private void getData() {
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getActivity().getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        String Language = cookieLanguage.getString("sName", " ");
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath + "GetProductByCategory/30";
        resultJson = "GetProductBycateResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                //map.put("Name", c.getString("Name"));
                map.put("Picture", c.getString("Picture"));
                //map.put("Detail", c.getString("Detail"));
                map.put("Id", c.getString("Id"));

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


                String p = c.getString("DetailProduct"); //ภาษา
                try {
                    JSONObject  j = new JSONObject(p);
                    if(Language.equals("English")){
                        map.put("Detail", j.getString("English"));
                    }
                    else if(Language.equals("Thai")){
                        map.put("Detail", j.getString("Thai"));
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
                convertView = inflater.inflate(R.layout.gut_promotion, null);
            }
            TextView name_textView = (TextView) convertView.findViewById(R.id.name_textView);
            ImageView pm_imageView = (ImageView) convertView.findViewById(R.id.pm_imageView);
            TextView id_textView =(TextView)convertView.findViewById(R.id.id_textView);

            name_textView.setText(MyArr.get(position).get("Name"));

            String url = MyArr.get(position).get("Picture");

            if (!url.equals("null")) {
                try {
                    Picasso.with(context)
                            .load(url)
                            .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                            .error(R.drawable.logo_bunchooo)
                            .into(pm_imageView);//เอารูปตัวไหน
                } catch (Exception e) {

                }

            }


            promotion_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(),ViewDetailNews.class);
                    intent.putExtra("Name", MyArr.get(position).get("Name"));
                    intent.putExtra("Picture", MyArr.get(position).get("Picture"));
                    intent.putExtra("Detail", MyArr.get(position).get("Detail"));
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getData();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            promotion_listview.setAdapter(new Adapter(getActivity(), MyArrListTotal));
            pdLoading.dismiss();
        }
    }
}
