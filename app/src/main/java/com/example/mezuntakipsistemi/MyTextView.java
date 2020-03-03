package com.example.mezuntakipsistemi;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        if(!isInEditMode())
        {
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }
}
