package com.donate.savelife.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.donate.savelife.R;
import com.donate.savelife.core.user.data.model.User;
import com.novoda.notils.caster.Views;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ravi on 03/09/16.
 */
public class HeroItemView extends RelativeLayout {

    private int layoutResId;
    private AppCompatTextView heroName;
    private AppCompatTextView heroAddress;
    private CircleImageView heroAvatar;

    public HeroItemView(Context context) {
        super(context);
    }

    public HeroItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_hero_item);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        this.heroName = Views.findById(this, R.id.hero_name);
        this.heroAddress = Views.findById(this, R.id.hero_address);
        this.heroAvatar = Views.findById(this, R.id.hero_avatar);
    }

    public void display(User user){
        heroName.setText(user.getName());
        heroAddress.setText(user.getCity());
        Picasso.with(getContext()).load(user.getPhotoUrl()).into(heroAvatar);
    }
}
