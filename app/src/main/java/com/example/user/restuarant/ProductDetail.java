package com.example.user.restuarant;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
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

public class ProductDetail extends AppCompatActivity {

    private EditText productdetail_Quantity_editText;
    private Button productdetail_addtoorder_button, Delete_my_order_button, list_service_date_from_button, list_service_date_to_button, pust_button, minus_button;
    private ImageView productdetail_imageView;
    private TextView productdetail_name_textView, productdetail_detail_textView, productdetail_price_textView, Id_listservice_textView, numproduct;
    private ReadJson readJson;
    private String resultJson, url,Name, num;
    private String Id = "", Imagedetail = "", CheckInDate = "", CheckOutDate = "",value,numQuantity,Language;
    private GridView productdetail_gridView;
    private int TicketOrder = 0;
    private int TicketId=0;
    private ListView confrim_order_listView;
    private ArrayList<HashMap<String, String>> MyArrList;
    String webservicepath, Price;
    String allsum = "0";
    int sum;
    private NumberPicker numberPicker;
    private int number;
    private int numproductint = 1;

    ProgressDialog pDialog;
    AlertDialog.Builder imageDialog;
    private GoogleApiClient client;
    private GoogleApiClient client2;

    TextView pro_textView,Quantity_textView,detail_textView,bath_textView;

    String proprice,price,p123,p124,p125,p126,p135,p136,p137,p138,p139,p140,p141;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        webservicepath = getResources().getString(R.string.path_webservice);
        Intent intent = getIntent();
        Id = intent.getStringExtra("Id");//รับค่าจากหน้าที่ส่งมาให้
        Imagedetail = intent.getStringExtra("Imagedetail");
        Name = intent.getStringExtra("Name");



        //setTitle(Name);
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        Language = cookieLanguage.getString("sName", "English");


        SharedPreferences cookie1 = getApplication().getSharedPreferences("DateTime", 0);//เรียกใช้ คุ้กกี้
        String Date  = cookie1.getString("sDate", "null");
        String Time  = cookie1.getString("sTime", "null");
        CheckInDate = Date+" "+Time ;
        CheckOutDate = Date+" "+Time;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        productdetail_Quantity_editText = (EditText) findViewById(R.id.productdetail_Quantity_editText);
        productdetail_addtoorder_button = (Button) findViewById(R.id.productdetail_addtoorder_button);
        productdetail_name_textView = (TextView) findViewById(R.id.productdetail_name_textView);
        productdetail_detail_textView = (TextView) findViewById(R.id.productdetail_detail_textView);
        productdetail_price_textView = (TextView) findViewById(R.id.productdetail_price_textView);
        productdetail_imageView = (ImageView) findViewById(R.id.productdetail_imageView);
        productdetail_gridView = (GridView) findViewById(R.id.productdetail_gridView);
        confrim_order_listView = (ListView) findViewById(R.id.confrim_order_listView);
        confrim_order_listView = (ListView) findViewById(R.id.confrim_order_listView);
        pust_button = (Button) findViewById(R.id.pust_button);
        minus_button = (Button) findViewById(R.id.minus_button);
        numproduct = (TextView) findViewById(R.id.numproduct);

        Quantity_textView=(TextView)findViewById(R.id.Quantity_textView);
        detail_textView=(TextView)findViewById(R.id.detail_textView);
        bath_textView=(TextView)findViewById(R.id.bath_textView);
        pro_textView=(TextView)findViewById(R.id.pro_textView);



        pust_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numproductint<=99) {
                    numproductint++;
                    num = Integer.toString(numproductint);
                    numproduct.setText(num);
                    int muney = Integer.parseInt(Price);
                    sum = muney * numproductint;
                    allsum = Integer.toString(sum);
                    Double Total = 0.00;
                    DecimalFormat df = new DecimalFormat("###,###,###");//แปลงเลขให้มี ตามนี้ #,###0.00
                    Total += Double.parseDouble(allsum);
                    productdetail_price_textView.setText(df.format(Total));//แปลงเลขให้มี ตามนี้ #,###0.00
                }

            }
        });
        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numproductint >= 2){
                    numproductint--;
                    num = Integer.toString(numproductint);
                    numproduct.setText(num);
                    int muney = Integer.parseInt(Price);
                    sum = muney * numproductint;
                    allsum = Integer.toString(sum);
                    Double Total = 0.00;
                    DecimalFormat df = new DecimalFormat("###,###,###");//แปลงเลขให้มี ตามนี้ #,###0.00
                    Total += Double.parseDouble(allsum);
                    productdetail_price_textView.setText(df.format(Total));//แปลงเลขให้มี ตามนี้ #,###0.00
                }
            }
        });
        productdetail_addtoorder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numproductint!=0){
                    Order();
                }else{
                    Toast.makeText(getApplication(), (p141), Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });

        SharedPreferences cookie = getSharedPreferences("CartInfo", 0);
        TicketOrder = cookie.getInt("sId", 0);
        if (TicketOrder == 0) {
            TicketOrder();
        } else {
            CheckStatus(TicketOrder);
        }
        getMassage();
        getData2();
    }
    private void CheckStatus(int TicketId) {
        readJson = new ReadJson();
        url = webservicepath+"GetTicketOrderById/" + TicketId;
        resultJson = "GetTicketOrderByIdResult";
        String jsonResult = readJson.getHttpGet(url);
        int StatusTicket = 0;
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                StatusTicket = c.getInt("Status");
            }
            if (StatusTicket > 3) {
                // มากวว่า2 คือสร้างแล้ว ลบตะกร้า
                final SharedPreferences settings = getSharedPreferences("CartInfo", 0);
                settings.edit().clear().commit();
                TicketOrder();
            } else if (StatusTicket == 2 || StatusTicket == 3) {
                productdetail_addtoorder_button.setVisibility(View.GONE); //มีตะกร้าแล้ว สร้างตะกร้าไม่ได้ ซ้อนปุ่ม
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
    }
    private void Order() {
        SharedPreferences cookie = getSharedPreferences("CartInfo", 0);
        int CartInfo = cookie.getInt("sId", 0);
        readJson = new ReadJson();
        url = webservicepath+"ProductOrder/Add";
//        url = "http://services.totiti.net/DSME289Wcf/Service1.svc/ProductOrder/Add";
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("CheckInDate",CheckInDate);
            jsonObj.put("CheckOutDate",CheckInDate);
            jsonObj.put("Product", Id);
            jsonObj.put("Quantity", numproductint);
            jsonObj.put("Remark", null);
            jsonObj.put("TicketOrder", CartInfo);
            String resultServer = readJson.getHttpPost(url, jsonObj);
            try {
                JSONObject jsonObjRs = new JSONObject(resultServer);
                String message = "";
                Boolean check = false;
                for (int i = 0; i < jsonObjRs.length(); i++) {
                    check = jsonObjRs.getBoolean("Success");
                    message = jsonObjRs.getString("Message");
                }
                if (check) {
                    final AlertDialog.Builder adb = new AlertDialog.Builder(ProductDetail.this);
                    adb.setMessage(p135+ productdetail_name_textView.getText().toString()+p140);//ดักว่าแน่ใจหรือไม่ที่จะออก
                    adb.setNegativeButton(p139, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(getApplication(), MyOrder.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    adb.setNeutralButton(p136, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    adb.show();
                } else {
                    Toast.makeText(getApplicationContext(), p137, Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(getApplicationContext(), p137, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), p137, Toast.LENGTH_LONG).show();
        }
    }
    private void TicketOrder() {
        SharedPreferences cookie = getSharedPreferences("UserInfo", 0);  //เชค user
        int UserId = cookie.getInt("sId", 0);
        readJson = new ReadJson();
        url = webservicepath+"TicketOrder/Add";
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("Id", UserId);
            String resultServer = readJson.getHttpPost(url, jsonObj);

            try {
                JSONObject jsonObjRs = new JSONObject(resultServer);
                int Id = 0;
                for (int i = 0; i < jsonObjRs.length(); i++) {
                    TicketOrder = jsonObjRs.getInt("Id");
                }
                if (TicketOrder > 0) {
                    SharedPreferences sp = getSharedPreferences("CartInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit(); // Create Session
                    editor.putInt("sId", TicketOrder);
                    editor.commit();
                } else {
                    Toast.makeText(getApplicationContext(), p138, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), p138, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), p138, Toast.LENGTH_LONG).show();
        }
    }
    private void getData() {
        String pp=null,tt = null;
        if (!Id.equals("0")) {
            if (!Imagedetail.equals("null")) {
                try {
                    Picasso.with(getApplication()).load(Imagedetail).into(productdetail_imageView);
                } catch (Exception e) {
                    productdetail_imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                productdetail_imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }
            readJson = new ReadJson();
            url = webservicepath+"GetProductDetailByProduct/"+Id;
            resultJson = "GetProductDetailByProductResult";
            String jsonResult = readJson.getHttpGet(url);
            String KeyName = null, KeyValue = null, Picture = null;
            try {
                JSONObject jsonObMain = new JSONObject(jsonResult);
                JSONArray data = jsonObMain.getJSONArray(resultJson);
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    KeyName = c.getString("KeyName");
                    KeyValue = c.getString("KeyValue");

                    if (KeyName.equals("Name")) //ถ้า keyname เท่ากับ name
                    {
                        productdetail_name_textView.setText(KeyValue);
                    } else if (KeyName.equals("Detail")) {
                        productdetail_detail_textView.setText(KeyValue);
                    }

                    else if (KeyName.equals("Price")) {
                        pp = KeyValue;
                    }
                    else if (KeyName.equals("PromotionPrice")) {
                        tt = KeyValue;
                    }
                    else if(tt.equals("-")){
                        productdetail_price_textView.setText(pp);
                    }else if(!tt.equals("-")){
                        pro_textView.setVisibility(View.VISIBLE);
                        productdetail_price_textView.setText(tt);
                    }
                    Price = productdetail_price_textView.getText().toString();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("error", e.toString());
            }
        }
    }
    private void getData2() {
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        String Language = cookieLanguage.getString("sName", "English");
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath+"GetProductById/" + Id;
        resultJson = "GetProductByIdResult";
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObMain = new JSONObject(jsonResult);
            JSONArray data = jsonObMain.getJSONArray(resultJson);

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);

                Id = c.getString("Id");
                price = c.getString("Price");
                proprice = c.getString("PromotionPrice");
                String urlimage = c.getString("Picture");


                if(proprice.equals("-")){
                    productdetail_price_textView.setText(price);
                }else if(!proprice.equals("-")){
                    pro_textView.setVisibility(View.VISIBLE);
                    productdetail_price_textView.setText(proprice);
                }

                Price = productdetail_price_textView.getText().toString();
                String x = c.getString("NameProduct"); //ภาษา
                try {
                    JSONObject  j = new JSONObject(x);
                    if(Language.equals("English")){
                        productdetail_name_textView.setText(j.getString("English"));
                    }
                    else if(Language.equals("Thai")){
                        productdetail_name_textView.setText( j.getString("Thai"));
                    }
                }
                catch (JSONException e){
                } //จบ2ภาษา

                String y = c.getString("DetailProduct"); //ภาษา
                try {
                    JSONObject  j = new JSONObject(y);
                    if(Language.equals("English")){
                        productdetail_detail_textView.setText(j.getString("English"));
                    }
                    else if(Language.equals("Thai")){
                        productdetail_detail_textView.setText( j.getString("Thai"));
                    }

                }
                catch (JSONException e){

                } //จบ2ภาษา


                if (!urlimage.equals("null")) {
                    try {
                        Picasso.with(getApplication())
                                .load(urlimage)
//                                .placeholder(R.drawable.logob) //ภาพเริ่มต้น
//                                .error(R.drawable.logob)
                                .into(productdetail_imageView);//เอารูปตัวไหน
                    } catch (Exception e) {

                    }
                }


            }
        } catch (JSONException e) {

            Log.e("error", e.toString());
        }
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(ProductDetail.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();//เปิดหมุนๆ
        }

        @Override
        protected Void doInBackground(Void... params) {
            getDataImg();//แสดงรูปภาพที่หมุน
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //this method will be running on UI thread
            pdLoading.dismiss();//ปิดหมุนๆ
            productdetail_gridView.setAdapter(new ImageAdapter(ProductDetail.this, MyArrList));
        }
    }
    public void getDataImg() {
//        ReadJSON readJson = new ReadJSON();
        url = webservicepath +"GetProductImageByProductId/"+Id;
        String jsonResult = readJson.getHttpGet(url);
        try {
            JSONObject jsonObjMain = new JSONObject(jsonResult);
            JSONArray data = jsonObjMain.getJSONArray("GetProductImageByProductIdResult");
            MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("KeyValue", c.getString("KeyValue"));
                MyArrList.add(map);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();


        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list) {
            // TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return MyArr.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.img_column, null);
            }

            // ColImage
            ImageView imageView = (ImageView) convertView.findViewById(R.id.ColPhoto);
            imageView.getLayoutParams().height = 1500;
            imageView.getLayoutParams().width = 1500;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String url = MyArr.get(position).get("KeyValue");

            if (!MyArr.get(position).get("KeyValue").equals("null")) {
                try {
                    //Picasso.with(context).load(url).into(imageView);
                    Picasso.with(context)
                            .load(url)
                            .placeholder(R.drawable.logo_bunchooo) //ภาพเริ่มต้น
                            .error(R.drawable.logo_bunchooo)
//                            .resize(imgWidth, imgHeight)//ปรับขนาด
//                            .centerCrop()//ตัดภาพ
                            .into(imageView);//เอารูปตัวไหน
                } catch (Exception e) {
                    imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                }
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            }
            return convertView;
        }
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
                    case 125://จำนวน
                        p125 = c.getString(Language);
                        Quantity_textView.setText(c.getString(Language));
                        break;
                    case 126://รายละเอียด
                        p126 = c.getString(Language);
                        detail_textView.setText(c.getString(Language));
                        break;
                    case 124://โปรโมชั่น
                        //p127 = c.getString(Language);
                        pro_textView.setText(c.getString(Language));
                        break;
                    case 123://บาท
                        p123 = c.getString(Language);
                        bath_textView.setText(c.getString(Language));
                        break;
                    case 127://สั่งซื้อ
                        productdetail_addtoorder_button.setText(c.getString(Language));
                        break;
                    case 135://สเพิ่มรายการ
                        p135 = c.getString(Language);
                        break;
                    case 136://สกลับ
                        p136 = c.getString(Language);
                        break;
                    case 137://เพิ่มรายการไม่สำเร็จ!!!
                        p137 = c.getString(Language);
                        break;
                    case 138://ไม่สามารถสร้าง ticket ได้!!!
                        p138 = c.getString(Language);
                        break;
                    case 139://เมนู
                        p139 = c.getString(Language);
                        break;
                    case 140://สำเร็จ
                        p140 = c.getString(Language);
                        break;
                    case 141://สำเร็จ
                        p141 = c.getString(Language);
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
