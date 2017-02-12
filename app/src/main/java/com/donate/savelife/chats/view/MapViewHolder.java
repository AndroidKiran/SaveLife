package com.donate.savelife.chats.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.donate.savelife.core.chats.model.Message;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.user.data.model.User;

class MapViewHolder extends RecyclerView.ViewHolder {

    private final MapView mapView;

    public MapViewHolder(MapView mapView) {
        super(mapView);
        this.mapView = mapView;
    }

    public void bind(final Message message, User needOwner, Need need, final OnChatSelectionListener onChatSelectionListener) {
        mapView.display(message, needOwner, need);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChatSelectionListener.onChatSelected(message);
            }
        });

        mapView.getUserAvatar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChatSelectionListener.onProfilePicSelected(message);
            }
        });
    }
}
