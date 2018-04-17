package com.example.user.restuarant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.restuarant.commonclass.GPSTracker;
import com.example.user.restuarant.commonclass.MultipartEntity;
import com.example.user.restuarant.commonclass.ReadJson;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
    private Button Date, Time, buttonBookbank, buttonPayment, confirmPayment;
    private ImageView imgGallery, imgCamera,  showPicture;
    private String d1, d2, t1, t2, mCurrentPhotoPath;
    private String latStr, lngStr;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private ReadJson readJson;
    private String url = "", resultJson;
    private String getDate, getTime;
    private ArrayList<HashMap<String, String>> MyArrListTotal = new ArrayList<>();
    private EditText edittextDetail, edittextMoney;
    private TextView textMoney;

    private int IdBank;
    String senddate, sendtime;
    private ListView chooseBookbank;
    private String Bank;
    private int TicketId = 0, Id_User = 0;
    private long Id = 0;
    ProgressDialog pDialog;
    String myFilePic;
    String ImageUrl = "-";
    MultipartEntity reqEntity;
    private Boolean chkUpImg = false;
    String ImageUrl_1 = null;
    String webservicepath,path_up_imgae,path_imgae;
    Bitmap rotatedBMP;
    GPSTracker gps;
    Uri uri;
    String  checkprice;
    DecimalFormat df = new DecimalFormat("#,###.00");
    Double price = 0.00, getCheckprice = 0.00;
    int d = 0,t=0;

    private String Id_Account="0";
    private String p214,p220,p219,p218,p217,p216,p215,p209,p221,p162,p107,p222;



    protected void onCreate(Bundle BusavedInstanceState) {
        super.onCreate(BusavedInstanceState);
        setContentView(R.layout.payment);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SharedPreferences cookie3 = getApplication().getSharedPreferences("DateTime", 0);//เรียกใช้ คุ้กกี้
        getDate = cookie3.getString("sDate", "null");
        getTime = cookie3.getString("sTime", "null");

        setTitle("แจ้งชำระเงิน");
        getWindow().setSoftInputMode( // hide keyboard
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            insertDummyContactWrapper();
        }
//        final Intent intent = getIntent();
        SharedPreferences totalprice = getSharedPreferences("Total", 0); //เรียกใช้ Cookie TicketId
        checkprice = totalprice.getString("sTotal", "");//รับค่าจากหน้าที่ส่งมาให้

        SharedPreferences Ticket = getSharedPreferences("CartInfo", 0); //เรียกใช้ Cookie TicketId
        TicketId = Ticket.getInt("sId", 0);

        SharedPreferences sp = getSharedPreferences("UserInfo", 0);//เรียกใช้ Cookie Id_User
        Id_User = sp.getInt("sId", 0);

        Date = (Button) findViewById(R.id.buttonDate);
        Time = (Button) findViewById(R.id.buttonTime);
        buttonBookbank = (Button) findViewById(R.id.buttonBookbank);
        imgGallery = (ImageView) findViewById(R.id.imgGallery);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        showPicture = (ImageView) findViewById(R.id.showPicture);
        buttonPayment = (Button) findViewById(R.id.buttonPayment);
        edittextMoney = (EditText) findViewById(R.id.edittextMoney);
        textMoney = (TextView) findViewById(R.id.textMoney);
        edittextDetail = (EditText) findViewById(R.id.edittextDetail);
        confirmPayment = (Button)findViewById(R.id.payment_button);

        webservicepath = getResources().getString(R.string.path_webservice);
        path_up_imgae = getResources().getString(R.string.path_up_image);
        //path_imgae = getResources().getString(R.string.path_imgae);


        getMassage();

        reqEntity = new MultipartEntity();
        gps = new GPSTracker(Payment.this);
        getData();
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show Date
                DialogFragment newFragment = new SelectDateIn();
                newFragment.show(getFragmentManager(), "DatePicker");
                senddate = Date.getText().toString();
                d =1; // เลือกวันที่แล้ว
            }
        });
        //โชว์จำนวนเงิน ****start****
        SharedPreferences pricetotal = getSharedPreferences("Total", 0);
        String stotalprice = pricetotal.getString("sTotal", "");
        textMoney.setText(stotalprice);
        //****End****
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show Time
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");
                sendtime = Time.getText().toString();
                t = 1; // เลือกเวลาแล้ว
            }
        });

        buttonBookbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show listview for select menu bookbank
                showDialog();
            }
        });



        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // to camera
                takePhoto();
            }
        });

        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //choose picture
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,PICK_FROM_GALLERY);
            }
        });

        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// to web service
                if(textMoney.getText().toString().isEmpty()) {
                    textMoney.setError(p214);
                    return;
                }else if(textMoney.getText().toString().equals("0")){
                    textMoney.setError(p214);
                    return;
                }
//                else if (TextUtils.isEmpty(edittextDetail.getText().toString())) {
//                    edittextDetail.setError("กรุณาใส่รายละเอียด");
//                    return;
//                }
                else if(Id_Account.equals("0")){
                    Toast.makeText(getApplicationContext(),p215,Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(d==0){
                    Toast.makeText(getApplicationContext(),p216,Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(t==0){
                    Toast.makeText(getApplicationContext(),p217,Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    String Nowprice = textMoney.getText().toString();
                    price = Double.parseDouble(Nowprice);
                    getCheckprice = Double.parseDouble(checkprice);
                    if (!(price.equals(getCheckprice))){
                        textMoney.setError(p218);
                        return;
                    }else {
                        if(imgUp >0){ // ใส่ภาพมา
                            reqEntity = new MultipartEntity();
                            try {
                                sendPhoto(rotatedBMP);
                                if(chkUpImg){
                                    if(price.equals(getCheckprice)){
                                        ConFirmpayment();

                                    }else {
                                        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Payment.this);
                                        adb.setMessage(p162);
                                        adb.setNegativeButton(p107, new AlertDialog.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int arg1) {
                                            }
                                        });
                                        adb.show();
                                    }
                                }

                            } catch (Exception e) {

                                final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Payment.this);
                                adb.setMessage(p162);
                                adb.setNegativeButton(p107, new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int arg1) {
                                    }
                                });
                                adb.show();

                            }
                        }
                        else { // ไม่ใส่ภาพ
                            final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Payment.this);
                            adb.setMessage(p219);
                            adb.setNegativeButton(p107, new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {

                                }
                            });
                            adb.show();
                        }
                    }
                }

            }
        });

        new AsyncCaller().execute();

    }

    int imgUp = 0;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: " + this);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            setPic();
        }

        if (requestCode == PICK_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            File finalFile = new File(getRealPathFromURI(uri));
            mCurrentPhotoPath = finalFile.getPath();
            setPic();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void takePhoto() {
/*		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(intent, 0);*/
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        Log.i(TAG, "photo path = " + mCurrentPhotoPath);
        return image;
    }

    public void showDialog() { // create Alertdialog

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.showboookbank, null);
        ListView chooseBookbank = (ListView) dialogView.findViewById(R.id.chooseBookbank);
        chooseBookbank.setAdapter(new Adapter(getApplication(), MyArrListTotal));

        builder.setView(dialogView);
        builder.setTitle(p209);
        final AlertDialog dialog = builder.show();
        chooseBookbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buttonBookbank.setText(p222 + MyArrListTotal.get(position).get("Bank") + MyArrListTotal.get(position).get("Idname"));
                //buttonBookbank.setText(p222 + MyArrListTotal.get(position).get("Bank"));
                Id_Account = MyArrListTotal.get(position).get("Id");
                Bank = buttonBookbank.getText().toString();
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("ValidFragment")
    public  class SelectDateIn extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);  //date is dateSetListener as per your code in question
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime()); //ไม่เอาวันอนาคต
            datePickerDialog.show();
            return datePickerDialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            d1 = String.valueOf(day);//แปลง int เป็น string
            if (d1.length()==1){
                d1 = "0"+day;
            }
            month = month + 1;
            d2 = String.valueOf(month);//แปลง int เป็น string
            if (String.valueOf(month).length()==1){
                d2 = "0"+month;
            }
            Date.setText(d1+"-"+d2+"-"+year);
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
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            t1 = String.valueOf(hourOfDay);//แปลง int เป็น string
            t2 = String.valueOf(minute);//แปลง int เป็น string
            if (t1.length() == 1) {
                t1 = "0" + hourOfDay;
            }
            if (t2.length() == 1) {
                t2 = "0" + minute;
            }
            Time.setText(t1 + ":" + t2);
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = showPicture.getWidth();
        int targetH = showPicture.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        try {
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

            ExifInterface exif = new ExifInterface(mCurrentPhotoPath);
            Matrix mtx = new Matrix();

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    mtx.postRotate(90);
                    break;
            }

            // Rotating Bitmap
            rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);
            if (rotatedBMP != bitmap)
                bitmap.recycle();
            showPicture.setImageBitmap(rotatedBMP);
            imgUp=2;
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private void getData() {//ดึงชื่อธนาคาร
        HashMap<String, String> map;
        readJson = new ReadJson();
        url = webservicepath + "GetAllAccount";
        resultJson = "GetAllAccountResult";
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
                map.put("Bank", c.getString("Bank"));
                map.put("Id", c.getString("Id"));
                map.put("Idname", c.getString("Idname"));
                map.put("Name", c.getString("Name"));
                map.put("Sector", c.getString("Sector"));
                map.put("Status", c.getString("Status"));
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
                convertView = inflater.inflate(R.layout.bookbank, null);

            }
            TextView showBookBank = (TextView) convertView.findViewById(R.id.showBookBank);
            TextView showSector = (TextView) convertView.findViewById(R.id.showSector);
            TextView textviewId = (TextView) convertView.findViewById(R.id.textviewId);

            showBookBank.setText("ธนาคาร" + MyArr.get(position).get("Bank"));
            showSector.setText("สาขา " + MyArr.get(position).get("Idname"));
            textviewId.setText(MyArr.get(position).get("Id"));

            return convertView;
        }
    }

    private boolean ConFirmpayment() {
        boolean chk = false;
        readJson = new ReadJson();
        url = webservicepath + "ConFirmpayment/Add";
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("Detail", edittextDetail.getText().toString());
            jsonObj.put("Price", textMoney.getText().toString());
            jsonObj.put("TicketOrder", TicketId);
            jsonObj.put("UrlImage", ImageUrl);
            jsonObj.put("Id_Account", Id_Account);
            jsonObj.put("Id_user", Id_User);
            jsonObj.put("Date",  Date.getText().toString());
            jsonObj.put("Time",  Time.getText().toString());

            String resultServer = readJson.getHttpPost(url, jsonObj);
            try {
                JSONObject jsonObjRs = new JSONObject(resultServer);
                String message = "";
                Boolean check = false;

                Id = jsonObjRs.getLong("Id");
                if (Id != 0) {
                    chk = true;
                    SharedPreferences sp = getSharedPreferences("Payment", 0);//สร้างคุกกี้ ชื่อ""
                    SharedPreferences.Editor editor = sp.edit(); // Create Session
                    editor.putLong("sId", Id);//ค่าที่จะเก็บค่าที่ต้องการ
                    editor.commit();
                    final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Payment.this);
                    adb.setMessage(p220);
                    adb.setNegativeButton("OK", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            finish();
                        }
                    });
                    adb.show();

                }


            } catch (JSONException e) {

            }
        } catch (JSONException e) {
            //Log.e(TAG, "JSONException: " + e);
        }
        return chk;
    }

    private class UploadTask extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Payment.this);
            pDialog.setMessage(Html.fromHtml("Uploading ..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Bitmap... bitmaps) {
            if (bitmaps[0] == null)
                return null;
            setProgress(0);

            Bitmap Mybitmap = bitmaps[0];
            Bitmap bitmap = Bitmap.createScaledBitmap(Mybitmap, (int) (Mybitmap.getWidth() * 0.5), (int) (Mybitmap.getHeight() * 0.5), true); // resize image
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
            InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream

            DefaultHttpClient httpclient = new DefaultHttpClient();
            try {
                HttpPost httppost = new HttpPost(path_up_imgae); // server
                String NamePic = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                myFilePic = TicketId + "_" + NamePic;
                ImageUrl =  myFilePic + ".jpg";
                reqEntity.addPart("myFile", myFilePic + ".jpg", in);

                httppost.setEntity(reqEntity);

                Log.i(TAG, "request " + httppost.getRequestLine());
                HttpResponse response = null;
                try {
                    response = httpclient.execute(httppost);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    chkUpImg = false;
                    if (response != null)
                        chkUpImg = true; /// ภาพลง เซิฟเวอร์
                    Log.i(TAG, "response " + response.getStatusLine().toString());
                } finally {

                }
            } finally {

            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            pDialog.dismiss();
            super.onPostExecute(result);
            if (chkUpImg) {

                if (ConFirmpayment()) {
                    new AsyncCaller().execute();
                    Toast.makeText(getApplicationContext(), p220, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Payment.this);
                    adb.setMessage(p221);
                    adb.setNegativeButton("ตกลง", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {

                        }
                    });
                    adb.show();
                }
            }
            else {
                final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(Payment.this);
                adb.setMessage(p221);
                adb.setNegativeButton("ตกลง", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                adb.show();
            }
        }
    }

    private void sendPhoto(Bitmap bitmap) throws Exception {
        new UploadTask().execute(bitmap);
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(Payment.this);

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
            //this method will be running on UI thread
            pdLoading.dismiss();
            if (ImageUrl_1 != null) {
                //Picasso.with(getApplicationContext()).load("http://services.totiti.net/snapsmeswcfservice/images/"+ImageUrl_1).into(showPicture);
            } else {
                Picasso.with(getApplicationContext()).load(R.drawable.logo_bunchooo).into(showPicture);

            }

        }
    }

    ////////////////////

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        java.util.List<String> permissionsNeeded = new ArrayList<String>();
        final java.util.List<String> permissionsList = new ArrayList<String>();

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE EXTERNAL STORAGE");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ EXTERNAL STORAGE");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        insertDummyContact();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(java.util.List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        ) {
                    // All Permissions Granted
                    insertDummyContact();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Payment.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private static final String TAG = "Contacts";

    private void insertDummyContact() {
        // Two operations are needed to insert a new contact.
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

        // First, set up a new raw contact.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // Next, set the name for the contact.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        "__DUMMY CONTACT from runtime permissions sample");
        operations.add(op.build());

    }


    private void getMassage() {
        TextView showTranfer = (TextView)findViewById(R.id.showTranfer);
        TextView showDateTimeTranfer = (TextView)findViewById(R.id.showDateTimeTranfer);
        TextView showMoney = (TextView)findViewById(R.id.showMoney);
        TextView showBaht = (TextView)findViewById(R.id.showBaht);
        TextView detail = (TextView)findViewById(R.id.detail);
        TextView picture = (TextView)findViewById(R.id.picture);
        readJson = new ReadJson();
        //เรียกใช้คุกกี้ภาษา
        SharedPreferences cookieLanguage = getSharedPreferences("Language", 0);//เรียกใช้ คุ้กกี้
        String Language = cookieLanguage.getString("sName", "English");
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
                    case 208://บัญชีที่โอนเงิน
                        showTranfer.setText(c.getString(Language));
                        break;
                    case 209://-เลือกบัญชีธนาคาร-
                        p209 = c.getString(Language);
                        buttonBookbank.setText(c.getString(Language));
                        break;
                    case 210://วันเวลาที่โอน
                        showDateTimeTranfer.setText(c.getString(Language));
                        break;
                    case 211://จำนวนเงิน
                        showMoney.setText(c.getString(Language));
                        break;
                    case 126://รายละเอียด
                        detail.setText(c.getString(Language));
                        break;
                    case 213://แจ้งชำระเงิน
                        buttonPayment.setText(c.getString(Language));
                        break;
                    case 114://วันที่
                        Date.setText(c.getString(Language));
                        break;
                    case 115://เวลา
                        Time.setText(c.getString(Language));
                        break;
                    case 123://บาท
                        showBaht.setText(c.getString(Language));
                        break;
                    case 214://กรุณาใส่จำนวนเงิน
                        p214 = c.getString(Language);
                        break;
                    case 220://แจ้งชำระเงินเสร็จสมบูรณ์
                        p220 = c.getString(Language);
                        break;
                    case 219://กรุณาแนบหลักฐานการโอนเงิน
                        p219 = c.getString(Language);
                        break;
                    case 218://จำนวนเงินไม่ถูกต้อง
                        p218 = c.getString(Language);
                        break;
                    case 217://กรุณาเลือกเวลาโอนเงิน
                        p217 = c.getString(Language);
                        break;
                    case 216://กรุณาเลือกวันที่โอนเงิน
                        p216 = c.getString(Language);
                        break;
                    case 215://กรุณาเลือกบัญชีธนาคาร
                        p215 = c.getString(Language);
                        break;
                    case 221://กรุณาเลือกบัญชีธนาคาร
                        p221 = c.getString(Language);
                        break;
                    case 162://ผิดพลาด!!!
                        p162 = c.getString(Language);
                        break;
                    case 107://ผิดพลาด!!!
                        p107 = c.getString(Language);
                        break;
                    case 222://ธนาคาร
                        p222 = c.getString(Language);
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