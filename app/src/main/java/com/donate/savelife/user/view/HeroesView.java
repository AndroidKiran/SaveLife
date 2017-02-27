package com.donate.savelife.user.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.donate.savelife.R;
import com.donate.savelife.apputils.Views;
import com.donate.savelife.component.DividerItemDecoration;
import com.donate.savelife.component.MultiStateView;
import com.donate.savelife.component.text.TextView;
import com.donate.savelife.core.user.data.model.Users;
import com.donate.savelife.core.user.displayer.HerosDisplayer;

/**
 * Created by ravi on 22/11/16.
 */

public class HeroesView extends LinearLayout implements HerosDisplayer {

    private final HeroesAdapter heroesAdapter;
    private RecyclerView recyclerView;
    private MultiStateView multiView;
    private HeroInteractionListener heroInteractionListener;
    private TextView emptyViewTxt;
    private AppCompatImageView emptyViewIcon;

    public HeroesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        heroesAdapter = new HeroesAdapter(LayoutInflater.from(context));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_needs_view, this);
        initControls();
        setRecyclerView();
    }

    void initControls(){
        multiView = Views.findById(this, R.id.multi_view);
        emptyViewTxt = (TextView) multiView.findViewById(R.id.txt_empty);
        emptyViewIcon = (AppCompatImageView) multiView.findViewById(R.id.img_empty);
    }


    void setRecyclerView(){
        recyclerView = Views.findById(this, R.id.recycler_view);
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.seperator_72);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(heroesAdapter);
    }

    @Override
    public void attach(HeroInteractionListener heroInteractionListener) {
        this.heroInteractionListener = heroInteractionListener;
        heroesAdapter.attach(heroInteractionListener);
    }

    @Override
    public void detach(HeroInteractionListener heroInteractionListener) {
        this.heroInteractionListener = heroInteractionListener;
        heroesAdapter.detach(heroInteractionListener);
    }


    @Override
    public void display(Users users) {
        heroesAdapter.setData(users);
    }


    @Override
    public void displayLoading() {
        multiView.setViewState(MultiStateView.VIEW_STATE_LOADING);
    }

    @Override
    public void displayContent() {
        multiView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void displayError() {
        multiView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void displayEmpty() {
        emptyViewTxt.setText(getContext().getString(R.string.str_heros_empty_state));
        emptyViewIcon.setVisibility(GONE);
        multiView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

}
