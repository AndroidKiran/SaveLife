package com.donate.savelife.user.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.donate.savelife.R;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.DividerItemDecoration;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.displayer.HonorHeroesDisplayer;

/**
 * Created by ravi on 22/11/16.
 */

public class HeroesHonorView extends CoordinatorLayout implements HonorHeroesDisplayer {

    private final HeroesHonorAdapter heroesHonorAdapter;
    private RecyclerView recyclerView;
    private HonorHeroesInteractionListener honorHeroesInteractionListener;

    public HeroesHonorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setFitsSystemWindows(true);
        heroesHonorAdapter = new HeroesHonorAdapter(LayoutInflater.from(context));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_heroes_honor_view, this);
        setRecyclerView();
    }

    void setRecyclerView(){
        recyclerView = Views.findById(this, R.id.recycler_view);
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.seperator_72);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(heroesHonorAdapter);
    }

    @Override
    public void attach(HonorHeroesInteractionListener honorHeroesInteractionListener) {
        this.honorHeroesInteractionListener = honorHeroesInteractionListener;
        heroesHonorAdapter.attach(honorHeroesInteractionListener);
    }

    @Override
    public void detach(HonorHeroesInteractionListener honorHeroesInteractionListener) {
        this.honorHeroesInteractionListener = honorHeroesInteractionListener;
        heroesHonorAdapter.detach(honorHeroesInteractionListener);
    }


    @Override
    public void display(Users users) {
        heroesHonorAdapter.setData(users);
    }

    @Override
    public Users getUsers() {
        return heroesHonorAdapter.getUsers();
    }

}
