package com.donate.savelife.requirements.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.donate.savelife.R;
import com.donate.savelife.core.requirement.model.Need;
import com.novoda.notils.caster.Views;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ravi on 03/09/16.
 */
public class NeedItemView extends RelativeLayout {

    private int layoutResId;
    private AppCompatTextView bloodGroup;
    private AppCompatTextView requirementAddress;
    private CircleImageView userAvatar;

    public NeedItemView(Context context) {
        super(context);
    }

    public NeedItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_need_item);
            array.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        this.bloodGroup = Views.findById(this, R.id.blood_group);
        this.requirementAddress = Views.findById(this, R.id.user_address);
        this.userAvatar = Views.findById(this, R.id.user_avatar);
    }

    public void display(Need need){
        bloodGroup.setText(String.format(getResources().getString(R.string.str_blood_required_msg), need.getBloodGroup()));
        requirementAddress.setText(String.format(getResources().getString(R.string.str_address_msg), need.getAddress(), need.getCity(),need.getCountryName(getContext())));
        Picasso.with(getContext()).load(need.getUser().getPhotoUrl()).into(userAvatar);
    }
}
