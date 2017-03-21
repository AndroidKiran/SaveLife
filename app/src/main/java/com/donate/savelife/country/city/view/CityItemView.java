package com.donate.savelife.country.city.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.donate.savelife.R;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.core.country.model.City;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ravi on 01/10/16.
 */

public class CityItemView extends RelativeLayout {

    private int layoutResId;
    private CircleImageView flag;
    private AppCompatTextView name;

    public CityItemView(Context context) {
        super(context);
    }

    public CityItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_country_item);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        this.flag = Views.findById(this, R.id.country_flag);
        this.name = Views.findById(this, R.id.country_name);
    }

    public void display(City city){
        flag.setImageResource(R.mipmap.in_flag);
        name.setText(city.getName());
    }
}
