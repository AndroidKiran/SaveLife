package com.donate.savelife.user.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.displayer.HerosDisplayer;

import java.util.ArrayList;

/**
 * Created by ravi on 04/09/16.
 */
public class HerosAdapter extends RecyclerView.Adapter<HeroViewHolder>{

    private final LayoutInflater inflater;
    private Users users;
    private HeroItemView heroItemView;
    private HerosDisplayer.HeroInteractionListener heroInteractionListener;

    public HerosAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        setHasStableIds(true);
        users = new Users(new ArrayList<User>());
    }

    public void setData(Users users){
        if (users.size() > 0){
            this.users = users;
            notifyItemRangeInserted(getItemCount(), users.size());
            heroInteractionListener.onContentLoaded();
        } else {
            heroInteractionListener.onEmpty();
        }

    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        heroItemView = (HeroItemView) inflater.inflate(R.layout.hero_item, parent, false);
        return new HeroViewHolder(heroItemView);
    }

    @Override
    public void onBindViewHolder(HeroViewHolder holder, int position) {
        holder.bind(users.getUserAt(position));
    }

    @Override
    public int getItemCount() {
        if (users == null){
            return 0;
        }
        return users.size();
    }

    public void attach(HerosDisplayer.HeroInteractionListener heroInteractionListener) {
        this.heroInteractionListener = heroInteractionListener;
    }

    public void detach(HerosDisplayer.HeroInteractionListener heroInteractionListener) {
        this.heroInteractionListener = heroInteractionListener;
    }
}
