package com.example.user.restuarant;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by PAK on 2015-08-26.
 */
public class TextViewKitithada extends TextView {
    public TextViewKitithada(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewKitithada(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewKitithada(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/psl244pro.ttf");
            setTypeface(tf);
        }
    }
}
