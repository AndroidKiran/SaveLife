package com.donate.savelife.user.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.displayer.HonorHeroesDisplayer;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by ravi on 04/09/16.
 */
public class HeroesHonorAdapter extends RecyclerView.Adapter<HonorHeroViewHolder> {
    private final int LIFE_INCREAMENT_VALUE = 1;
    private final LayoutInflater inflater;
    private Users users;
    private HonorHeroItemView HonorHeroItemView;
    private HonorHeroesDisplayer.HonorHeroesInteractionListener honorHeroesInteractionListener;

    public HeroesHonorAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        users = new Users(new ArrayList<User>());
        setHasStableIds(true);
    }

    public void setData(Users users) {
        if (users.size() > 0) {
            this.users = users;
            notifyItemRangeInserted(getItemCount(), users.size());
        }
    }

    public void remove(User user) {
        ListIterator<User> userListIterator = users.getUsers().listIterator();
        while (userListIterator.hasNext()) {
            User user1 = userListIterator.next();
            if (user1.equals(user)) {
                remove(user);
                break;
            }
        }
    }

    @Override
    public HonorHeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HonorHeroItemView = (HonorHeroItemView) inflater.inflate(R.layout.honor_item, parent, false);
        return new HonorHeroViewHolder(HonorHeroItemView);
    }

    @Override
    public void onBindViewHolder(HonorHeroViewHolder holder, int position) {
        holder.bind(users.getUserAt(position), honorSelectionListener);
    }

    @Override
    public int getItemCount() {
        if (users == null) {
            return 0;
        }
        return users.size();
    }

    public Users getUsers() {
        return users;
    }

    public void attach(HonorHeroesDisplayer.HonorHeroesInteractionListener honorHeroesInteractionListener) {
        this.honorHeroesInteractionListener = honorHeroesInteractionListener;
    }

    public void detach(HonorHeroesDisplayer.HonorHeroesInteractionListener honorHeroesInteractionListener) {
        this.honorHeroesInteractionListener = honorHeroesInteractionListener;
    }


    private final HonorHeroViewHolder.HonorSelectionListener honorSelectionListener = new HonorHeroViewHolder.HonorSelectionListener() {
        @Override
        public void onHeroHonored(User user) {
            honorHeroesInteractionListener.onHeroesHonored(user, LIFE_INCREAMENT_VALUE);
        }
    };
}
