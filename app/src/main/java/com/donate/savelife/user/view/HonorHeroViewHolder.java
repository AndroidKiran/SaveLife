package com.donate.savelife.user.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donate.savelife.core.user.data.model.User;


/**
 * Created by ravi on 04/09/16.
 */
public class HonorHeroViewHolder extends RecyclerView.ViewHolder {

    private final HonorHeroItemView itemView;

    public HonorHeroViewHolder(HonorHeroItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void bind(final User user, final HonorSelectionListener honorSelectionListener) {
        itemView.display(user);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                honorSelectionListener.onHeroHonored(user);
            }
        });
    }


    interface HonorSelectionListener {

        void onHeroHonored(User user);

    }
}
