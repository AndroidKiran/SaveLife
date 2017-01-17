package com.donate.savelife.chats.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.donate.savelife.R;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.MessageBubbleDrawable;
import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageView extends LinearLayout {

    private final DateFormat timeFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
    private final Date date = new Date();
    private final Context context;
    private TextView body;
    private TextView time;
    private TextView name;

    private int layoutResId;
    private CircleImageView picture;

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        super.setOrientation(VERTICAL);
        this.context = context;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.layout
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            layoutResId = array.getResourceId(0, R.layout.merge_message_item_view);
            array.recycle();
        }
    }

    public void setTextBackground(MessageBubbleDrawable bubbleDrawable) {
        body.setBackground(bubbleDrawable);
    }

    @Override
    public void setOrientation(int orientation) {
//        throw new DeveloperError("This view only supports vertical orientation");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), layoutResId, this);
        this.picture = Views.findById(this, R.id.message_author_image);
        this.body = Views.findById(this, R.id.message_body);
        this.time = Views.findById(this, R.id.message_time);
        this.name = Views.findById(this, R.id.message_author_name);
    }

    public void display(final Message message, User user, Need need) {
        Glide.with(context).load(message.getAuthor().getPhotoUrl()).thumbnail(0.8f)
                .crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(picture);
        body.setText(message.getBody());
        time.setText(formattedTimeFrom(message.getTimestamp()));
        name.setText(message.getAuthor().getName());
        MessageBubbleDrawable bubbleDrawable = null;

        int messageColor;
        MessageBubbleDrawable.Gravity gravity;
        if (message.getUserId().equals(user.getId())) {
            gravity = MessageBubbleDrawable.Gravity.END;
            if (!message.getUserId().equals(need.getUserID())){
                messageColor = R.color.material_green;
            } else {
                messageColor = R.color.material_red;
            }
        } else {
            gravity = MessageBubbleDrawable.Gravity.START;
            if (!message.getUserId().equals(need.getUserID())){
                messageColor = R.color.material_dark_green;
            } else {
                messageColor = R.color.material_red;
            }
        }

        bubbleDrawable = new MessageBubbleDrawable(getContext(), messageColor, gravity);
        picture.setBorderColorResource(messageColor);
        setTextBackground(bubbleDrawable);


    }

    private String formattedTimeFrom(long timestamp) {
        date.setTime(timestamp);
        return timeFormat.format(date);
    }

}
