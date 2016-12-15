package com.donate.savelife.requirements.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.donate.savelife.R;
import com.donate.savelife.component.DividerItemDecoration;
import com.donate.savelife.component.MultiStateView;
import com.donate.savelife.component.paginate.Paginate;
import com.donate.savelife.core.requirement.displayer.NeedsDisplayer;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.novoda.notils.caster.Views;

/**
 * Created by ravi on 22/11/16.
 */

public class NeedsView extends LinearLayout implements NeedsDisplayer {

    private final NeedsAdapter needsAdapter;
    private Need lastNeedItem;
    private RecyclerView recyclerView;
    private NeedInteractionListener needInteractionListener;
    private Paginate paginate;
    private boolean isloading;
    private MultiStateView multiView;

    public NeedsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        needsAdapter = new NeedsAdapter(LayoutInflater.from(context));
        lastNeedItem = new Need();
        lastNeedItem.setId("");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_needs_view, this);
        initControls();
        setRecyclerView();
        setPagination(callbacks);
    }

    void initControls(){
        multiView = Views.findById(this, R.id.multi_view);
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

    private void setPagination(Paginate.Callbacks callbacks) {
        if (paginate != null) {
            paginate.unbind();
        }
        paginate = Paginate.with(recyclerView, callbacks)
                .setLoadingTriggerThreshold(3)
                .addLoadingListItem(true)
                .build();
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

    Paginate.Callbacks callbacks = new Paginate.Callbacks() {
        @Override
        public void onLoadMore(int direction) {
            if (needsAdapter.getItemCount() != 0 && !isloading) {
                Need lastNeed = needsAdapter.getLastItem();
                if (!lastNeedItem.getId().equals(lastNeed.getId()) && direction == Paginate.SCROLL_UP) {
                    lastNeedItem = lastNeed;
                    needInteractionListener.onLoadMore(lastNeed);
                    isloading = true;
                } else {
                    lastNeedItem = lastNeed;
                    isloading = false;
//                    paginate.setHasMoreDataToLoad(false);
                }
            }
        }

        @Override
        public boolean isLoading() {
            return isloading;
        }

        @Override
        public boolean hasLoadedAllItems() {
            if (needsAdapter.getItemCount() != 0){
                return lastNeedItem.getId().equals(needsAdapter.getLastItem().getId());
            }
            return false;
        }
    };

    @Override
    public void display(Needs needs) {
        needsAdapter.setData(needs);
        isloading = false;
    }

    @Override
    public void displayMore(Needs needs) {
        needsAdapter.setMoreData(needs);
        isloading = false;
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
        multiView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public Needs getNeeds() {
        return needsAdapter.getNeeds();
    }

    @Override
    public Need getlastNeedItem() {
        return lastNeedItem;
    }

    @Override
    public void setLastNeedItem(Need lastNeedItem) {
        this.lastNeedItem = lastNeedItem;
    }
}
