package com.donate.savelife.user.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donate.savelife.R;
import com.donate.savelife.apputils.RoundedBackgroundSpan;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.core.user.data.model.User;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by ravi on 03/09/16.
 */
public class HeroItemView extends RelativeLayout {

    private int layoutResId;
    private AppCompatTextView heroName;
    private AppCompatTextView liveCount;
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
        this.liveCount = Views.findById(this, R.id.live_count);
        this.heroAvatar = Views.findById(this, R.id.hero_avatar);
    }

    public void display(final User user) {
        heroName.setText(user.getName());
        String liveCountStr = getResources().getQuantityString(R.plurals.heros_lives,user.getLifeCount(),user.getLifeCount());
        Glide.with(getContext()).load(user.getPhotoUrl()).thumbnail(0.8f)
                .crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(heroAvatar);
        setTags(liveCountStr);
    }

    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }


    public void setTags(String tag) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(tag);
        RoundedBackgroundSpan tagSpan = new RoundedBackgroundSpan(ContextCompat.getColor(getContext(),R.color.material_gold), ContextCompat.getColor(getContext(),R.color.material_white));
        stringBuilder.setSpan(tagSpan, 10, tag.indexOf("l") - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        liveCount.setText(stringBuilder);
    }

}
