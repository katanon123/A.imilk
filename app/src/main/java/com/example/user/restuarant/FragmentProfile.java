package com.example.user.restuarant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.restuarant.commonclass.ReadJson;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class FragmentProfile extends Fragment {
    private static final String LOG_TAG = "";
    private TextView confrim_order_id_reserved_textView, Total_textView, date_in_service_textView, Quantity_textView,
            myorder_Name_textview, StandbyOrder_textView, AcceptedOrder_textView,showticket_textView;
    private Button confrim_order_Confrim_button, confrim_order_cancel_button, payment;
    private ListView confrim_order_listView;
    private ReadJson readJson;
    private String url = "", Id = "", resultJson, CheckInDate, CheckOutDate, Date, Time, sPeople,Language;
    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
    private int TicketId = 0;
    private int StatusTicket = 0, CheckConfirmPayment =0;
    String webservicepath;
    private ImageView delete_imageView;
    private int TicketOrder = 0;
    private static final int Datefrom = 0;
    private static final int Dateto = 1;
    private RelativeLayout MyOrder_Layout;
    private GoogleApiClient client;
    private int Status;
    private boolean chkRefresh = false;
    private LinearLayout textconfrim_Layout, button_linearLayout;
    private ScrollView scrollView7;
    String p125,p122,p123,p143,p107,p144,p145,p146,p147,p148,p149,p150,p151,p152,p153,p154,p120,p155,
            p156,p157,p158,p159;
    TextView ticketId_textView,date_textView,totalprice_textview,bath_textView;
    Button addr_button;
    int PostComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View rootView = inflater.inflate(R.layout.activity_fragment_profile, container, false);
        if(Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //ไม่ให้keybordเด้งขึ้นอัตโนมัติ

        Intent intent = getActivity().getIntent();
        Id = intent.getStringExtra("Id");
        SharedPreferences cookie1 = getActivity().getSharedPreferences("DateTime", 0);//เรียกใช้ คุ้กกี้
        String Date = cookie1.getString("sDate", "null");
        String Time = cookie1.getString("sTime", "null");
        String sPeople = cookie1.getString("sPeople", "null");
        CheckInDate = Date + " " + Time;
        CheckOutDate = Date + " " + Time;

        //status_textView=(TextView)findViewById(R.id.status_textView);
        webservicepath = getResources().getString(R.string.path_webservice);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        date_in_service_textView = (TextView) rootView.findViewById(R.id.date_in_service_textView);
        delete_imageView = (ImageView) rootView.findViewById(R.id.delete_imageView);
        Total_textView = (TextView) rootView.findViewById(R.id.Total_textView);
        confrim_order_id_reserved_textView = (TextView) rootView.findViewById(R.id.confrim_order_id_reserved_textView);
        //myorderlayout = (RelativeLayout) findViewById(R.id.myorderlayout);
        confrim_order_listView = (ListView) rootView.findViewById(R.id.confrim_order_listView);
        confrim_order_Confrim_button = (Button) rootView.findViewById(R.id.confrim_order_Confrim_button);
        confrim_order_cancel_button = (Button) rootView.findViewById(R.id.confrim_order_cancel_button);
        payment = (Button) rootView.findViewById(R.id.payment_button);
        myorder_Name_textview = (TextView) rootView.findViewById(R.id.myorder_Name_textview);
        textconfrim_Layout = (LinearLayout) rootView.findViewById(R.id.textconfrim_Layout);
        StandbyOrder_textView = (TextView) rootView.findViewById(R.id.StandbyOrder_textView);
        AcceptedOrder_textView = (TextView) rootView.findViewById(R.id.AcceptedOrder_textView);

        button_linearLayout = (LinearLayout) rootView.findViewById(R.id.button_linearLayout);
        //scrollView7=(ScrollView)findViewById(R.id.scrollView7);
        MyOrder_Layout = (RelativeLayout) rootView.findViewById(R.id.MyOrder_Layout);

        ticketId_textView = (TextView) rootView.findViewById(R.id.ticketId_textView);
        date_textView = (TextView) rootView.findViewById(R.id.date_textView);
        showticket_textView=(TextView)rootView.findViewById(R.id.showticket_textView);
        totalprice_textview=(TextView)rootView.findViewById(R.id.totalprice_textview);
        bath_textView=(TextView)rootView.findViewById(R.id.bath_textView);
        addr_button=(Button)rootView.findViewById(R.id.addr_button);
        addr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostComment.class);
                startActivity(intent);
            }
        });
        SharedPreferences Ticket = getActivity().getSharedPreferences("CartInfo", 0);
        TicketId = Ticket.getInt("sId", 0);
        confrim_order_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setMessage(p154);
                adb.setNegativeButton(p150, new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        CancelStatus();
                    }
                });
                adb.setNeutralButton(p149, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                adb.show();


            }
        });
        confrim_order_Confrim_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences cookie = getActivity().getSharedPreferences("UserInfo", 0);
                int UserId = cookie.getInt("sId", 0);
                if (UserId != 0) {
                    final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setMessage(p153);
                    adb.setNegativeButton(p150, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            sendconfrim();

                        }
                    });
                    adb.setNeutralButton(p149, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    adb.show();

                } else {
                    final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setMessage(p155);
                    adb.setNegativeButton(p120, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            getActivity().finish();

                        }
                    });
                    adb.show();

                }

            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textconfrim_Layout.setVisibility(View.VISIBLE);
                SharedPreferences Ticket = getActivity().getSharedPreferences("CartInfo", 0);
                TicketId = Ticket.getInt("sId", 0);
                if (TicketId != 0) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), Payment.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        confrim_order_id_reserved_textView.setText("DGS-" + TicketId);
        date_in_service_textView.setText(Date + " " + Time);//ดึงข้อมูลจากข้างนอกใส้มาใชว์

        getMassage();
        CheckStatus();
        //new AsyncCaller().execute();
        return rootView;
    }
    private void CheckStatus() {
        SharedPreferences Ticket = getActivity().getSharedPreferences("CartInfo", 0);
        TicketId = Ticket.getInt("sId", 0);
        confrim_order_id_reserved_textView.setText("DGS-" + TicketId);
        SharedPreferences Post = getActivity().getSharedPreferences("PostComment", 0);
        PostComment = Post.getInt("sPostComment", 0);
//        if(PostComment >0){
//            addr_button.setVisibility(View.GONE);
//        }
        readJson = new ReadJson();
        url = webservicepath+"GetTicketOrderById/" + TicketId;
//        url = "http://services.totiti.net/DSME289Wcf/Service1.svc/GetTicketOrderById/"+TicketId;
        resultJson = "GetTicketOrderByIdResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                StatusTicket = c.getInt("Status");
                CheckConfirmPayment = c.getInt("ChkPayment");
            }
            if (StatusTicket == 1) {
                button_linearLayout.setVisibility(View.GONE);
                payment.setVisibility(View.GONE);
                getData();
                confrim_order_listView.setAdapter(new ImageAdapter(getActivity(), MyArrListTotal));
                if (MyArrListTotal.size() > 0) {
                    button_linearLayout.setVisibility(View.VISIBLE);// ถ้าในหน้า รายการของฉันมีเมนูอาหาร ถึงจะโชว์ปุ่มให้กด
                }
            } else if (StatusTicket == 2) {
                button_linearLayout.setVisibility(View.GONE);
                StandbyOrder_textView.setVisibility(View.VISIBLE);
                textconfrim_Layout.setVisibility(View.VISIBLE);
//                if(PostComment > 0){
//                    addr_button.setVisibility(View.GONE);
//                }else {
//                    addr_button.setVisibility(View.VISIBLE);
//                }
                if(CheckConfirmPayment == 0){
                    payment.setVisibility(View.VISIBLE);
                }else if(CheckConfirmPayment > 0){
                    payment.setVisibility(View.GONE);
                }
                getData();
                confrim_order_listView.setAdapter(new ImageAdapter(getActivity(), MyArrListTotal));
            } else if (StatusTicket == 3) {
                // ลบตะกร้า
                button_linearLayout.setVisibility(View.GONE);
                StandbyOrder_textView.setVisibility(View.GONE);
                AcceptedOrder_textView.setVisibility(View.VISIBLE);
                textconfrim_Layout.setVisibility(View.VISIBLE);

                payment.setVisibility(View.GONE);
                getData();
                confrim_order_listView.setAdapter(new ImageAdapter(getActivity(), MyArrListTotal));
            }
//            else if (StatusTicket == 4) {
//                //payment.setVisibility(View.GONE);
//                final SharedPreferences settings = getActivity().getSharedPreferences("CartInfo", 0);
//                settings.edit().clear().commit();
//                final SharedPreferences DateTimeCookie = getActivity().getSharedPreferences("DateTime", 0);
//                DateTimeCookie.edit().clear().commit();
//                final SharedPreferences fPostComment = getActivity().getSharedPreferences("PostComment", 0);
//                fPostComment.edit().clear().commit();
//                //MyOrder_Layout.setVisibility(View.GONE);
//                confrim_order_id_reserved_textView.setText(" ");
//                Total_textView.setText(" ");
//                date_in_service_textView.setText(" ");
//                button_linearLayout.setVisibility(View.GONE);
//                StandbyOrder_textView.setVisibility(View.GONE);
//                AcceptedOrder_textView.setVisibility(View.GONE);
//                date_in_service_textView.setVisibility(View.GONE);
//            }
            else {
                //payment.setVisibility(View.GONE);
                final SharedPreferences settings = getActivity().getSharedPreferences("CartInfo", 0);
                settings.edit().clear().commit();
                //final SharedPreferences DateTimeCookie = getActivity().getSharedPreferences("DateTime", 0);
                //DateTimeCookie.edit().clear().commit();
                final SharedPreferences fPostComment = getActivity().getSharedPreferences("PostComment", 0);
                fPostComment.edit().clear().commit();
                //MyOrder_Layout.setVisibility(View.GONE);
                confrim_order_id_reserved_textView.setText(" ");
                Total_textView.setText(" ");
                date_in_service_textView.setText(" ");
                button_linearLayout.setVisibility(View.GONE);
                StandbyOrder_textView.setVisibility(View.GONE);
                AcceptedOrder_textView.setVisibility(View.GONE);
                date_in_service_textView.setVisibility(View.GONE);


//                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
//                adb.setMessage(p143);
//                adb.setNegativeButton(p107, new AlertDialog.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int arg1) {
//                        getActivity().finish();
//                    }
//                });
//
//                adb.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }
    private void sendconfrim() {
        SharedPreferences cookie = getActivity().getSharedPreferences("UserInfo", 0);  //เชค user
        int UserId = cookie.getInt("sId", 0);

        SharedPreferences Table = getActivity().getSharedPreferences("Table", 0);  //เชค user
        String TableType = Table.getString("sTableType", "");

        SharedPreferences Branch = getActivity().getSharedPreferences("ActiveBranch", 0);  //เชค user
        String ActiveBranch = Branch.getString("sActiveBranch", "");


        SharedPreferences cookie1 = getActivity().getSharedPreferences("DateTime", 0);//เรียกใช้ คุ้กกี้
        Date = cookie1.getString("sDate", "null");
        Time = cookie1.getString("sTime", "null");
        sPeople = cookie1.getString("sPeople", "null");


        url = webservicepath+"TicketOrder/ConfirmOrder";
        JSONObject jsonObj = new JSONObject();

        String tb="";
        if(!TableType.equals("")){
            tb = TableType;
        }
        try {
            //เราส่ง The following is an example request Json body:
            jsonObj.put("CustomerId", UserId);
            jsonObj.put("CheckInDate", Date + " " + Time);
            jsonObj.put("CheckOutDate", Date + " " + Time);
            jsonObj.put("TicketOrderId", TicketId);
            //jsonObj.put("Remark", tb);
            jsonObj.put("Remark", sPeople);
            jsonObj.put("BranchId", ActiveBranch);
            jsonObj.put("ServiceType", TableType);
            String resultServer = readJson.getHttpPost(url, jsonObj);
            try {
                JSONObject jsonObjRs = new JSONObject(resultServer);
                int Id = 0;
                String Message = "";
                boolean Success = false;

//ตี๋รีเทินมา The following is an example response Json body: มีแค่2อัน
                for (int i = 0; i < jsonObjRs.length(); i++) {
                    Message = jsonObjRs.getString("Message");
                    Success = jsonObjRs.getBoolean("Success");
                }
                if (Success) {
                    Toast.makeText(getActivity(), p144, Toast.LENGTH_LONG).show();
                    textconfrim_Layout.setVisibility(View.VISIBLE);
                    confrim_order_cancel_button.setVisibility(View.GONE);//ซ่อน
                    confrim_order_Confrim_button.setVisibility(View.GONE); //ซ่อน
                    addr_button.setVisibility(View.VISIBLE);
                    startActivity(getActivity().getIntent());//คำสั่ง refash หน้าจอเมื่อลบเสร็จ
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), p145, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), p145, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), p145, Toast.LENGTH_LONG).show();
        }
    }
    private void CancelStatus() {
        url = webservicepath+"TicketOrder/CancelStatus";
        JSONObject jsonObj = new JSONObject();
        try {
            //เราส่ง The following is an example request Json body:
            jsonObj.put("Id", TicketId);
            String resultServer = readJson.getHttpPost(url, jsonObj);
            try {
                JSONObject jsonObjRs = new JSONObject(resultServer);
                int Id = 0;
                String Message = "";
                boolean Success = false;

//ตี๋รีเทินมา The following is an example response Json body: มีแค่2อัน
                for (int i = 0; i < jsonObjRs.length(); i++) {
                    Message = jsonObjRs.getString("Message");
                    Success = jsonObjRs.getBoolean("Success");
                }
                if (Success) {
                    final SharedPreferences settings = getActivity().getSharedPreferences("CartInfo", 0);
                    settings.edit().clear().commit();
                    final SharedPreferences DateTimeCookie = getActivity().getSharedPreferences("DateTime", 0);
                    DateTimeCookie.edit().clear().commit();
                    final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setMessage(Message);
                    startActivity(getActivity().getIntent());
                    Toast.makeText(getActivity(), p146, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), p147, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), p147, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), p147, Toast.LENGTH_LONG).show();
        }
    }
    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();
        String Price;
        Double price =0.0;
        DecimalFormat df = new DecimalFormat("#,###.00");
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
                convertView = inflater.inflate(R.layout.activity_my_order, null);
            }

            TextView Quantity_textView = (TextView) convertView.findViewById(R.id.Quantity_textView);
            Quantity_textView.setText(MyArr.get(position).get("Quantity"));

            myorder_Name_textview = (TextView) convertView.findViewById(R.id.myorder_Name_textview);
            myorder_Name_textview.setText(MyArr.get(position).get("Name"));


            TextView myorder_number_textview = (TextView) convertView.findViewById(R.id.myorder_number_textview);
            myorder_number_textview.setText(MyArr.get(position).get("Number"));

            TextView myorder_price_textview = (TextView)convertView.findViewById(R.id.pro_myorder_price_textview);
            myorder_price_textview.setText(MyArr.get(position).get("Price"));

            TextView qty_textView = (TextView)convertView.findViewById(R.id.qty_textView);
            TextView price_textView = (TextView)convertView.findViewById(R.id.price_textView);
            TextView bath_textView = (TextView)convertView.findViewById(R.id.bath_textView);

            qty_textView.setText(p125);
            price_textView.setText(p122);
            bath_textView.setText(p123);

            ImageView detailimage = (ImageView) convertView.findViewById(R.id.list_service_imageview);
            String url = (MyArr.get(position).get("Picture"));

            TextView Product_id_textView = (TextView) convertView.findViewById(R.id.Product_id_textView);
            Product_id_textView.setText(MyArr.get(position).get("Product"));

            final ImageView delete_imageView = (ImageView) convertView.findViewById(R.id.delete_imageView);
            //เชคให้ปิดปุ่มdelete
            if (StatusTicket == 2) {
                delete_imageView.setVisibility(View.GONE);
            } else if (StatusTicket == 3) {
                delete_imageView.setVisibility(View.GONE);
            }

            delete_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setMessage(p148 + MyArr.get(position).get("Name"));
                    adb.setNegativeButton(p150, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            delete(MyArr.get(position).get("Product"));

                        }
                    });
                    adb.setNeutralButton(p149, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    adb.show();
                    //delete(MyArr.get(position).get("Product"));
                }
            });

            if (!url.equals("null")) {
                try {
                    //Picasso.with(context).load(url).into(detailimage);//เก่า
                    Picasso.with(context)
                            .load(url)
                            .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                            .error(R.drawable.logo_bunchooo)
//                            .resize(imgWidth, imgHeight)//ปรับขนาด
//                            .centerCrop()//ตัดภาพ
                            .into(detailimage);//เอารูปตัวไหน
                } catch (Exception e) {
                    detailimage.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                detailimage.setImageResource(android.R.drawable.ic_menu_report_image);
            }
            return convertView;
        }
    }
    @SuppressLint("SetTextI18n")
    private void getData() {
        SharedPreferences Ticket = getActivity().getSharedPreferences("CartInfo", 0);
        TicketId = Ticket.getInt("sId", 0);
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getActivity().getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        String Language = cookieLanguage.getString("sName", " ");
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath+"GetMyOrder/" + TicketId;
        resultJson = "GetMyOrderResult";
        MyArrListTotal.clear();
        String jsonResult = readJson.getHttpGet(url);
        String Name = null, Quantity = null, Price = null, CheckInDate = null, tt = null;
        Double Total = 0.00;
        DecimalFormat df = new DecimalFormat("#,##0.00");//แปลงเลขให้มี ตามนี้ #,###0.00

        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                //map.put("Name", c.getString("Name"));
                map.put("Quantity", c.getString("Quantity"));
                map.put("Price", c.getString("Price"));
                map.put("Picture", c.getString("Picture"));
                map.put("Product", c.getString("Product"));
                map.put("CheckInDate", c.getString("CheckInDate"));

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
                //date_in_service_textView.setText(c.getString("CheckInDate"));//ดึงข้อมูลจากข้างนอกใส้มาใชว์
                Total += Double.parseDouble(c.getString("Price")) * Double.parseDouble(c.getString("Quantity"));

                SharedPreferences ts = getActivity().getSharedPreferences("Total", Context.MODE_PRIVATE);//สร้างคุกกี้ ชื่อ""
                SharedPreferences.Editor editor = ts.edit(); // Create Session
                editor.putString("sTotal", Total.toString());//ค่าที่จะเก็บค่าที่ต้องการ
                editor.commit();

            }
            Total_textView.setText(df.format(Total));//แปลงเลขให้มี ตามนี้ #,###0.00
            //Total_textView.setText(Total.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }
    private void delete(final String Product) {
        url = webservicepath+"ProductOrder/Delete";
        JSONObject jsonObj = new JSONObject();
        try {
            //เราส่ง The following is an example request Json body:
            jsonObj.put("TicketOrder", TicketId);
            jsonObj.put("Product", Product);
            String resultServer = readJson.getHttpPost(url, jsonObj);
            try {
                JSONObject jsonObjRs = new JSONObject(resultServer);
                int Id = 0;
                String Message = "";
                boolean Success = false;

//ตี๋รีเทินมา The following is an example response Json body: มีแค่2อัน
                for (int i = 0; i < jsonObjRs.length(); i++) {
                    Message = jsonObjRs.getString("Message");
                    Success = jsonObjRs.getBoolean("Success");
                }
                if (Success) {
                    Toast.makeText(getActivity(), p151, Toast.LENGTH_LONG).show();
                    startActivity(getActivity().getIntent());//คำสั่ง refash หน้าจอเมื่อลบเสร็จ
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), p152, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), p152, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), p152, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onResume() {

        // TODO Auto-generated method stub
        super.onResume();
        SharedPreferences Ticket = getActivity().getSharedPreferences("PostComment", 0);
        int PostComment = Ticket.getInt("sPostComment", 0);
        if(PostComment >0){
            addr_button.setVisibility(View.GONE);
        }
        CheckStatus();
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
                    case 125://จำนวน
                        p125 = c.getString(Language);
                        break;
                    case 122://ราคา
                        p122 = c.getString(Language);
                        break;
                    case 123://บาท
                        p123 = c.getString(Language);
                        bath_textView.setText(c.getString(Language));
                        break;
                    case 143://กรุณาเลือกรายการ
                        p143 = c.getString(Language);
                        break;
                    case 107://ตกลง
                        p107 = c.getString(Language);
                        break;
                    case 144://ยืนยันรายการสำเร็จ
                        p144 = c.getString(Language);
                        break;
                    case 145://ยืนยันรายการไม่สำเร็จ!!!
                        p145 = c.getString(Language);
                        break;
                    case 146://ยกเลิกรายการสำเร็จ
                        p146 = c.getString(Language);
                        break;
                    case 147://ยกเลิกรายการไม่สำเร็จ!!!
                        p147 = c.getString(Language);
                        break;
                    case 148://ลบรายการ
                        p148 = c.getString(Language);
                        break;
                    case 149://ไม่ใช่
                        p149 = c.getString(Language);
                        break;
                    case 150://ใช่
                        p150 = c.getString(Language);
                        break;
                    case 151://ลบสำเร็จ
                        p151 = c.getString(Language);
                        break;
                    case 152://ลบไม่สำเร็จ!!!
                        p152 = c.getString(Language);
                        break;
                    case 153://ต้องการยืนยันรายการ
                        p153 = c.getString(Language);
                        break;
                    case 154://ต้องการยกเลิกรายการ
                        p154 = c.getString(Language);
                        break;
                    case 120://เข้าสู่ระบบ
                        p120 = c.getString(Language);
                        break;
                    case 155://กรุณาเข้าสู่ระบบ
                        p155 = c.getString(Language);
                        break;
                    case 156://รหัสการสั่งซื้อ
                        p156 = c.getString(Language);
                        ticketId_textView.setText(c.getString(Language));
                        break;case 183://แสดงรหัสการสั่งซื้อกับพนักงงาน
                        addr_button.setText(c.getString(Language));
                        break;

                    case 157://วันที่เข้าใช้บริการ
                        p157 = c.getString(Language);
                        date_textView.setText(c.getString(Language));
                        break;
                    case 158://ยืนยันรายการ
                        p158 = c.getString(Language);
                        confrim_order_Confrim_button.setText(c.getString(Language));
                        break;
                    case 159://ยกเลิกรายการ
                        p159 = c.getString(Language);
                        confrim_order_cancel_button.setText(c.getString(Language));
                        break;
                    case 168://รอรับการสั่งซื้อ
                        StandbyOrder_textView.setText(c.getString(Language));
                        break;
                    case 169://รับรายการซั่งซื้อแล้ว
                        AcceptedOrder_textView.setText(c.getString(Language));
                        break;
                    case 171://แสดงรหัสการสั่งซื้อกับพนักงงาน
                        showticket_textView.setText(c.getString(Language));
                        break;
                    case 200://ราคารวม
                        totalprice_textview.setText(c.getString(Language));
                        break;
                    case 213://แจ้งชำระเงิน
                        payment.setText(c.getString(Language));
                        break;
                    default:
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    } //จบ
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
            CheckStatus();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (StatusTicket == 1) {
                button_linearLayout.setVisibility(View.GONE);
                //payment.setVisibility(View.GONE);
                getData();
                confrim_order_listView.setAdapter(new ImageAdapter(getActivity(), MyArrListTotal));
                if (MyArrListTotal.size() > 0) {
                    button_linearLayout.setVisibility(View.VISIBLE);// ถ้าในหน้า รายการของฉันมีเมนูอาหาร ถึงจะโชว์ปุ่มให้กด
                }
            } else if (StatusTicket == 2) {
                button_linearLayout.setVisibility(View.GONE);
                StandbyOrder_textView.setVisibility(View.VISIBLE);
                textconfrim_Layout.setVisibility(View.VISIBLE);

                if(PostComment > 0){
                    addr_button.setVisibility(View.GONE);
                }else {
                    addr_button.setVisibility(View.VISIBLE);
                }
//                if(CheckConfirmPayment == 0){
//                    payment.setVisibility(View.VISIBLE);
//                }else if(CheckConfirmPayment > 0){
//                    payment.setVisibility(View.GONE);
//                }
                getData();
                confrim_order_listView.setAdapter(new ImageAdapter(getActivity(), MyArrListTotal));
            } else if (StatusTicket == 3) {
                // ลบตะกร้า
                button_linearLayout.setVisibility(View.GONE);
                AcceptedOrder_textView.setVisibility(View.VISIBLE);
                textconfrim_Layout.setVisibility(View.VISIBLE);

                //payment.setVisibility(View.GONE);
                getData();
                confrim_order_listView.setAdapter(new ImageAdapter(getActivity(), MyArrListTotal));
            } else {
                //payment.setVisibility(View.GONE);
                final SharedPreferences settings = getActivity().getSharedPreferences("CartInfo", 0);
                settings.edit().clear().commit();
                final SharedPreferences DateTimeCookie = getActivity().getSharedPreferences("DateTime", 0);
                DateTimeCookie.edit().clear().commit();
                final SharedPreferences fPostComment = getActivity().getSharedPreferences("PostComment", 0);
                fPostComment.edit().clear().commit();
                //MyOrder_Layout.setVisibility(View.GONE);
                confrim_order_id_reserved_textView.setText(" ");
                Total_textView.setText(" ");
                date_in_service_textView.setText(" ");
                button_linearLayout.setVisibility(View.GONE);
                StandbyOrder_textView.setVisibility(View.GONE);
                AcceptedOrder_textView.setVisibility(View.GONE);
                date_in_service_textView.setVisibility(View.GONE);
            }
            pdLoading.dismiss();
        }
    }

}
