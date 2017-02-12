package com.donate.savelife.requirements.view;

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
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;

/**
 * Created by ravi on 22/11/16.
 */

public class NeedsView extends LinearLayout implements NeedsDisplayer {

    private final NeedsAdapter needsAdapter;
    private RecyclerView recyclerView;
    private NeedInteractionListener needInteractionListener;
    private MultiStateView multiView;
    private TextView emptyViewTxt;
    private AppCompatImageView emptyViewIcon;

    public NeedsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        needsAdapter = new NeedsAdapter(LayoutInflater.from(context));
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
        recyclerView.setAdapter(needsAdapter);
    }

    @Override
    public void attach(NeedInteractionListener needInteractionListener) {
        this.needInteractionListener = needInteractionListener;
        needsAdapter.attach(needInteractionListener);
    }

    @Override
    public void detach(NeedInteractionListener needInteractionListener) {
        this.needInteractionListener = needInteractionListener;
        needsAdapter.detach(needInteractionListener);
    }

    @Override
    public void display(Needs needs, User owner) {
        needsAdapter.setData(needs, owner);
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
        emptyViewIcon.setVisibility(GONE);
        emptyViewTxt.setText(getContext().getString(R.string.str_needs_empty_state));
        multiView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public Needs getNeeds() {
        return needsAdapter.getNeeds();
    }
}
