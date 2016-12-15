package com.donate.savelife.user.view;

import android.support.v7.widget.RecyclerView;

import com.donate.savelife.core.user.data.model.User;


/**
 * Created by ravi on 04/09/16.
 */
public class HeroViewHolder extends RecyclerView.ViewHolder {

    private final HeroItemView itemView;

    public HeroViewHolder(HeroItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void bind(final User user) {
        itemView.display(user);
    }
}
