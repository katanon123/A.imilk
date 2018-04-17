package com.example.user.restuarant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.restuarant.commonclass.BitmapUrl;
import com.example.user.restuarant.commonclass.TouchImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by kanya hosakul on 7/25/2016.
 */
public class ShowFullSizePhoto {

    ImageView image;
    public  void ShowPhoto(final String UrlImageFull, final Context context) {
        //final AlertDialog.Builder imageDialog = new AlertDialog.Builder(context);
        final  Dialog imageDialog =new Dialog(context,android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View layout = inflater.inflate(R.layout.image_dialog,(ViewGroup)((Activity) context).findViewById(R.id.layout_root));

        image = (ImageView)layout.findViewById(R.id.user_fullimage);


        Picasso.with(context)
                .load(UrlImageFull)
                .error(R.drawable.logo_bunchooo)
                .into(image);



        BitmapUrl bm =  new BitmapUrl();
        Bitmap img = bm.loadBitmap(UrlImageFull);

        TouchImageView user_fullimage = new TouchImageView(context);
        user_fullimage.setImageBitmap(img);
//        user_fullimage.setMaxZoom(4f);
        Activity a = (Activity) context;
        imageDialog.setContentView(user_fullimage);
        imageDialog.show();


   }
}
