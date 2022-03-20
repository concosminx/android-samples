package com.example.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomLayout extends RelativeLayout {

    private ImageView mThumbnail;
    private TextView mMainText;
    private TextView mSubText;

    public CustomLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CustomLayout (Context context, int thumbnailResId, String mainText, String subText) {
        super(context);
        init(context);
        mThumbnail.setImageResource(thumbnailResId);
        mMainText.setText(mainText);
        mSubText.setText(subText);
    }

    public void setMainText(String mainText) {
        mMainText.setText(mainText);
    }

    public void setSubText(String subText) {
        mSubText.setText(subText);
    }

    public void setThumbnail(Bitmap b) {
        mThumbnail.setImageBitmap(b);
    }

    public void setThumbnail(int thumbnailResId) {
        mThumbnail.setImageResource(thumbnailResId);
    }

    public void setThumbnail(Drawable d) {
        mThumbnail.setImageDrawable(d);
    }

    private void init(Context context) {
        setBackgroundColor(Color.YELLOW);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.author_box, this);

        mThumbnail = findViewById(R.id.thumbnail);
        mMainText = findViewById(R.id.main_text);
        mSubText = findViewById(R.id.sub_text);
    }
}
