package com.example.user.restuarant;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.restuarant.commonclass.ReadJson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class FragmentService extends Fragment {

    private String url = "", resultJson, d1, d2, t1, t2, type;
    private ReadJson readJson;
    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
    private ListView service_listView;
    private TextView myorder_service_textView, around_delivery_textView, edit_time_textView, datetime_textView,selectdate_fragementservice_textView;
    private Button checkindate_fragementservice_button, time_fragmentservice_button, ok_button;
    private LinearLayout myoder_layout, datetime_linearLayout, listmenu_linearLayout, around_delivery;
    private ImageButton list_service_search_imageButton;
    private String timeBtn, dateBtn,p105,p106,p107;
    private int TicketId = 0;
    private int StatusTicket = 0;
    private String webservicepath,s,localTime,Language;
    private ImageView banner_imageView;
    private EditText peopleeditText;
    private int d, m, y;
    LinearLayout newsLL,buffeLL,foodLL,desestLL,galleryLL,mapLL,commentLL;
    ImageView L1_imageView,L2_imageView,L3_imageView,L4_imageView,L5_imageView,L6_imageView,L7_imageView;
    TextView beer_textView,Cider_textView,Wine_textView,Tonicwater_textView,Bakery_textView,Coffeebean_textView;
    String p108,p109,p110,p111,p112,p113,p114,p115,p116,p117,p204;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //final View rootView = inflater.inflate(R.layout.activity_fragment_service, container, false);
        final View rootview = inflater.inflate(R.layout.activity_fragment_service, container, false);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webservicepath = getResources().getString(R.string.path_webservice);
        edit_time_textView = (TextView) rootview.findViewById(R.id.edit_time_textView);//setวันที่เวลาที่เลือกไว้ให้โชวด้านบน

        SpannableString content = new SpannableString("แก้ไขวันที่");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);//ขีดเส้นใต้ข้อความ
        edit_time_textView.setText(content);

        SharedPreferences cookie1 = getActivity().getSharedPreferences("GetCategoryByTypeId", 0);//เรียกใช้ คุ้กกี้
        type = cookie1.getString("sTye_Id", "null");
        SharedPreferences cookie2 = getActivity().getSharedPreferences("Table", 0);//เรียกใช้ คุ้กกี้
        String Table = cookie2.getString("sTableType", "null");

        ok_button = (Button) rootview.findViewById(R.id.ok_button);
        datetime_textView = (TextView) rootview.findViewById(R.id.datetime_textView);
        around_delivery = (LinearLayout) rootview.findViewById(R.id.around_delivery);
        datetime_linearLayout = (LinearLayout) rootview.findViewById(R.id.datetime_linearLayout);
        listmenu_linearLayout = (LinearLayout) rootview.findViewById(R.id.listmenu_linearLayout);
        myoder_layout = (LinearLayout) rootview.findViewById(R.id.myoder_layout);
        around_delivery_textView = (TextView) rootview.findViewById(R.id.around_delivery_textView);
        checkindate_fragementservice_button = (Button) rootview.findViewById(R.id.checkindate_fragementservice_button);
        time_fragmentservice_button = (Button) rootview.findViewById(R.id.time_fragmentservice_button);
        service_listView = (ListView) rootview.findViewById(R.id.service_listView);
        peopleeditText = (EditText) rootview.findViewById(R.id.peopleeditText);
        selectdate_fragementservice_textView=(TextView)rootview.findViewById(R.id.selectdate_fragementservice_textView);
        myorder_service_textView=(TextView)rootview.findViewById(R.id.myorder_service_textView);


        beer_textView = (TextView) rootview.findViewById(R.id.beer_textView);
        Cider_textView = (TextView) rootview.findViewById(R.id.Cider_textView);
        Wine_textView = (TextView) rootview.findViewById(R.id.Wine_textView);
        Tonicwater_textView = (TextView) rootview.findViewById(R.id.Tonicwater_textView);
        Bakery_textView = (TextView) rootview.findViewById(R.id.Bakery_textView);
        Coffeebean_textView = (TextView) rootview.findViewById(R.id.Coffeebean_textView);

        newsLL=(LinearLayout)rootview.findViewById(R.id.newsLL);
        buffeLL=(LinearLayout)rootview.findViewById(R.id.buffeLL);
        foodLL=(LinearLayout)rootview.findViewById(R.id.foodLL);
        desestLL=(LinearLayout)rootview.findViewById(R.id.desestLL);
        galleryLL=(LinearLayout)rootview.findViewById(R.id.galleryLL);
        mapLL=(LinearLayout)rootview.findViewById(R.id.mapLL);
        commentLL=(LinearLayout)rootview.findViewById(R.id.commentLL);

        L1_imageView=(ImageView) rootview.findViewById(R.id.L1_imageView);
        L2_imageView=(ImageView) rootview.findViewById(R.id.L2_imageView);
        L3_imageView=(ImageView) rootview.findViewById(R.id.L3_imageView);
        L4_imageView=(ImageView) rootview.findViewById(R.id.L4_imageView);
        L5_imageView=(ImageView) rootview.findViewById(R.id.L5_imageView);
        L6_imageView=(ImageView) rootview.findViewById(R.id.L6_imageView);
        L7_imageView=(ImageView) rootview.findViewById(R.id.L7_imageView);

        //Seticon();

        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getActivity().getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        Language = cookieLanguage.getString("sName", " ");


        myoder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyOrder.class);
                startActivity(intent);
            }
        });
        edit_time_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datetime_linearLayout.setVisibility(View.VISIBLE);
                around_delivery.setVisibility(View.VISIBLE);
                listmenu_linearLayout.setVisibility(View.GONE);
                SharedPreferences cookie1 = getActivity().getSharedPreferences("DateTime", 0);//เรียกใช้ คุ้กกี้
                final String Date = cookie1.getString("sDate", "null");
                final String Time = cookie1.getString("sTime", "null");
                if (cookie1 != null) {
                    checkindate_fragementservice_button.setText(Date);
                    time_fragmentservice_button.setText(Time);
                    //ok_button.setVisibility(View.VISIBLE);
                }
            }
        });
        checkindate_fragementservice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateIn();
                newFragment.show(getActivity().getFragmentManager(), "DatePicker");
                time_fragmentservice_button.setText(p106);
                around_delivery.setVisibility(View.VISIBLE);
            }
        });
        time_fragmentservice_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getActivity().getFragmentManager(), "timePicker");


//                ok_button.setVisibility(View.VISIBLE);
            }
        });



        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkindate_fragementservice_button.getText().toString().equals(p105)) {
                    Toast.makeText(getActivity(), (p105), Toast.LENGTH_LONG).show();//เลขเท่ากับ 0
                    return;
                } else if (time_fragmentservice_button.getText().toString().equals(p106)) {
                    Toast.makeText(getActivity(), (p106), Toast.LENGTH_LONG).show();//เลขเท่ากับ 0
                    return;
                }
                else {
                    dateBtn = ((Button) rootview.findViewById(R.id.checkindate_fragementservice_button)).getText().toString();
                    timeBtn = ((Button) rootview.findViewById(R.id.time_fragmentservice_button)).getText().toString();
                    datetime_linearLayout.setVisibility(View.GONE);
                    listmenu_linearLayout.setVisibility(View.VISIBLE);
                    setCookie();
                    new AsyncCaller().execute();
                }

            }
        });



        newsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Promotion.class);
                intent.putExtra("Id","65");
                intent.putExtra("Name", "ข่าวสารและโปรโมชั่น");
                startActivity(intent);
            }
        });

        buffeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListService.class);
                intent.putExtra("Id","79");
                intent.putExtra("Name", "บุฟเฟ่ต์");
                startActivity(intent);
            }
        });

        foodLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetCategoryFood.class);
                intent.putExtra("Id","4" );
                intent.putExtra("Name", "ประเภทอาหาร");
                startActivity(intent);
            }
        });

        desestLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetCategoryFood.class);
                intent.putExtra("Id", "5");
                intent.putExtra("Name", "ประเภทเครื่องดื่ม");
                startActivity(intent);
            }
        });

        galleryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListGallery.class);
                intent.putExtra("Id", "2");
                intent.putExtra("Name", "Gallery");
                startActivity(intent);
            }
        });

        mapLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ListMap.class);
                intent.putExtra("Id", "7");
                intent.putExtra("Name", "ติดต่อเรา");
                startActivity(intent);
            }
        });

        commentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostComment.class);
                intent.putExtra("Name", "แสดงความคิดเห็น");
                startActivity(intent);
            }
        });

        //get date now
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        dateFormatter.setLenient(false);
        Date today = new Date();
        s = dateFormatter.format(today);

        //get time now
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat dateNow = new SimpleDateFormat("HH:mm");
        dateNow.setTimeZone(TimeZone.getTimeZone("GMT+7:00"));
        localTime = dateNow.format(currentLocalTime);


        getMassage();
        setPage();
        //new AsyncCaller().execute();

        return rootview;
    }


    private void setPage() {
        SharedPreferences cookie1 = getActivity().getSharedPreferences("DateTime", 0);//เรียกใช้ คุ้กกี้
        final String Date = cookie1.getString("sDate", "null");
        final String Time = cookie1.getString("sTime", "null");
        if (!Date.equals("null") && !Time.equals("null")) {
            checkindate_fragementservice_button.setText(Date);
//            time_fragmentservice_button.setText(Time);
            datetime_linearLayout.setVisibility(View.GONE);
            listmenu_linearLayout.setVisibility(View.VISIBLE);
            datetime_textView.setText("วันที่ " + Date + " " + "เวลา " + Time);
            getData();
            service_listView.setAdapter(new Adapter(getActivity(), MyArrListTotal));
        } else {
            checkindate_fragementservice_button.setText(p105);
            time_fragmentservice_button.setText(p106);
            datetime_linearLayout.setVisibility(View.VISIBLE);
            listmenu_linearLayout.setVisibility(View.GONE);
        }
    }
    private void Seticon(){
        Picasso.with(getActivity())
                .load(R.drawable.megaphone)
                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                .error(R.drawable.logo_bunchooo)
                .into(L1_imageView);//เอารูปตัวไหน

        Picasso.with(getActivity())
                .load(R.drawable.catering)
                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                .error(R.drawable.logo_bunchooo)
                .into(L2_imageView);//เอารูปตัวไหน

        Picasso.with(getActivity())
                .load(R.drawable.spoon)
                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                .error(R.drawable.logo_bunchooo)
                .into(L3_imageView);//เอารูปตัวไหน

        Picasso.with(getActivity())
                .load(R.drawable.orangejuice)
                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                .error(R.drawable.logo_bunchooo)
                .into(L4_imageView);//เอารูปตัวไหน

        Picasso.with(getActivity())
                .load(R.drawable.camera)
                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                .error(R.drawable.logo_bunchooo)
                .into(L5_imageView);//เอารูปตัวไหน

        Picasso.with(getActivity())
                .load(R.drawable.maplocation)
                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                .error(R.drawable.logo_bunchooo)
                .into(L6_imageView);//เอารูปตัวไหน

        Picasso.with(getActivity())
                .load(R.drawable.chat)
                .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                .error(R.drawable.logo_bunchooo)
                .into(L7_imageView);//เอารูปตัวไหน

    }
    @SuppressLint("ValidFragment")
    public class SelectDateIn extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);  //date is dateSetListener as per your code in question
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            d1 = String.valueOf(day);//แปลง int เป็น string
            if (d1.length() == 1) {
                d1 = "0" + day;
            }
            month = month + 1;
            d2 = String.valueOf(month);//แปลง int เป็น string
            if (String.valueOf(month).length() == 1) {
                d2 = "0" + month;
            }
            d = day;
            m = month;
            y = year;
            checkindate_fragementservice_button.setText(d1 + "-" + d2 + "-" + year);
        }
    }
    @SuppressLint("ValidFragment")
    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // Create a new instance of TimePickerDialog and return it

            new Date(System.currentTimeMillis()+25*24*60*60*1000l);


            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            t1 = String.valueOf(hourOfDay);//แปลง int เป็น string
            t2 = String.valueOf(minute);//แปลง int เป็น string
            if (t1.length()==1){
                t1 = "0"+hourOfDay;
            }
            if (t2.length()==1){
                t2 = "0"+minute;
            }
            String dd = checkindate_fragementservice_button.getText().toString();
            String tt = t1+":"+t2;
            final Calendar c = Calendar.getInstance();
            if(s.equals(dd)){
                if(minute <= c.get(Calendar.MINUTE)&& hourOfDay <= c.get(Calendar.HOUR_OF_DAY)){
                    Toast.makeText(getActivity(), p204, Toast.LENGTH_LONG).show();
                    time_fragmentservice_button.setText(p106);
                }else{
                    time_fragmentservice_button.setText(t1 + ":" + t2);
                }
            }else{
                time_fragmentservice_button.setText(t1 + ":" + t2);
            }
        }
    }
    private void setCookie() {
        if ((!dateBtn.equals(p105)) && (!dateBtn.equals(p106))) {
            SharedPreferences sp = getActivity().getSharedPreferences("DateTime", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ""
            SharedPreferences.Editor editor = sp.edit(); // Create Session
            editor.putString("sDate", dateBtn);//ค่าที่จะเก็บค่าที่ต้องการ
            editor.putString("sTime", timeBtn);
            //editor.putString("sPeople", peopleeditText.getText().toString());
            editor.commit();
        }
    }
    private void getData() {
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath + "GetType";
        resultJson = "GetTypeResult";
        String jsonResult = readJson.getHttpGet(url);
        String Name = null, Quantity = null, Price = null, CheckInDate = null, tt = null;
        Double Total = 0.0;
        MyArrListTotal.clear();
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("Id", c.getString("Id"));
                map.put("UrlImage", c.getString("UrlImage"));
                map.put("Status", c.getString("Status"));
                //map.put("Name", c.getString("Name"));

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
                convertView = inflater.inflate(R.layout.list_hotel_equipment, null);
            }
            TextView list_hotel_name_textview = (TextView) convertView.findViewById(R.id.list_hotel_name_textview);
            TextView Id_textView = (TextView) convertView.findViewById(R.id.Id_textView);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_hotel_imageview);
            TextView status_textView =(TextView)convertView.findViewById(R.id.status_textView);

            list_hotel_name_textview.setText(MyArr.get(position).get("Name"));
            Id_textView.setText(MyArr.get(position).get("Id"));
            String url = (MyArr.get(position).get("UrlImage"));
            status_textView.setText(MyArr.get(position).get("Status"));

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
            } else {

            }

            service_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String Status  = MyArr.get(position).get("Status");
                    if (Status.equals("1")) {//photo
                        Intent intent = new Intent(getActivity(), GetCategoryFood.class);
                        intent.putExtra("Name", MyArr.get(position).get("Name"));
                        intent.putExtra("Id", MyArr.get(position).get("Id"));
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), ListService.class);
                        intent.putExtra("Name", MyArr.get(position).get("Name"));
                        //intent.putExtra("Type", Type);//ส่งค่า wedservice
                        intent.putExtra("Id", MyArr.get(position).get("Id"));//ส่งค่า wedservice
                        startActivity(intent);
                    }
                }
            });
            return convertView;
        }
    }
    private void CheckStatus() {
        readJson = new ReadJson();
        url = webservicepath + "GetTicketOrderById/" + TicketId;
//        url ="http://services.totiti.net/DSME289Wcf/Service1.svc/GetTicketOrderById/"+TicketId;
        resultJson = "GetTicketOrderByIdResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                StatusTicket = c.getInt("Status");
            }
            if (StatusTicket == 1) {
                myoder_layout.setVisibility(View.VISIBLE);
            } else if (StatusTicket == 2) {
                myoder_layout.setVisibility(View.VISIBLE);
            } else if (StatusTicket == 3) {
                myoder_layout.setVisibility(View.VISIBLE);
            } else if (StatusTicket == 4) {
                ClearDateTime_Ticket();
                myoder_layout.setVisibility(View.VISIBLE);
            } else {
                myoder_layout.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }
    private void ClearDateTime_Ticket() {
//        final SharedPreferences DateTimeCookie = getActivity().getSharedPreferences("DateTime", 0); // เคลียวันเวลา
//        DateTimeCookie.edit().clear().commit();

        final SharedPreferences Ticket = getActivity().getSharedPreferences("CartInfo", 0);
        Ticket.edit().clear().commit();

//        final SharedPreferences DeliveryCookie = getActivity().getSharedPreferences("Table", 0); // เคลียdelivery
//        DeliveryCookie.edit().clear().commit();

    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //new AsyncCaller().execute();
        setPage();
        super.onResume();

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
            // MyArrListTotal.clear();
            getData();
            //setPage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            SharedPreferences Ticket = getActivity().getSharedPreferences("CartInfo", 0);
            TicketId = Ticket.getInt("sId", 0);
            CheckStatus();
            setPage();
            pdLoading.dismiss();
        }
    }
    private void getMassage() {
        readJson = new ReadJson();
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getActivity().getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
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
                    case 103://เลือกวันเข้าใช้บริการ
                        selectdate_fragementservice_textView.setText(c.getString(Language));
                        break;
                    case 104://เลือกเวลาเข้าใช้บริการ
                        around_delivery_textView.setText(c.getString(Language));
                        break;
                    case 105://เลือกวัน
                        p105 = c.getString(Language);
                        checkindate_fragementservice_button.setText(c.getString(Language));
                        break;
                    case 106://เลือกเวลา
                        p106 = c.getString(Language);
                        time_fragmentservice_button.setText(c.getString(Language));
                        break;
                    case 107://ตกลง
                        p107 = c.getString(Language);
                        ok_button.setText(c.getString(Language));
                        break;
                    case 108://เบียร์
                        p108 = c.getString(Language);
                        beer_textView.setText(c.getString(Language));
                        break;
                    case 109://ไซเดอร์
                        p109 = c.getString(Language);
                        Cider_textView.setText(c.getString(Language));
                        break;
                    case 110://ไวน์
                        p110 = c.getString(Language);
                        Wine_textView.setText(c.getString(Language));
                        break;
                    case 111://โทนิควอเตอร์
                        p111 = c.getString(Language);
                        Tonicwater_textView.setText(c.getString(Language));
                        break;
                    case 112://เบเกอร์รี่
                        p112 = c.getString(Language);
                        Bakery_textView.setText(c.getString(Language));
                        break;
                    case 113://เมล็ดกาแฟ
                        p113 = c.getString(Language);
                        Coffeebean_textView.setText(c.getString(Language));
                        break;
                    case 114://วันที่
                        p114 = c.getString(Language);
                        break;
                    case 115://เวลา
                        p115 = c.getString(Language);
                        break;
                    case 116://แก้ไข
                        p116 = c.getString(Language);
                        edit_time_textView.setText(c.getString(Language));
                        break;
                    case 117://รายการสินค้า
                        p117 = c.getString(Language);
                        myorder_service_textView.setText(c.getString(Language));
                        break;
                    case 204://กรุณาเลือกเวลาใหม่อีกครั้ง!!!
                        p204 = c.getString(Language);

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
