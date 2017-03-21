package com.donate.savelife.country.city.view;

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
import com.donate.savelife.core.country.displayer.CityDisplayer;
import com.donate.savelife.core.country.model.Cities;

/**
 * Created by ravi on 01/10/16.
 */

public class CitiesView extends LinearLayout implements CityDisplayer {

    private final CityAdapter cityAdapter;
    private RecyclerView recyclerView;
    private MultiStateView multiView;
    private TextView emptyViewTxt;
    private AppCompatImageView emptyViewIcon;
    private CityInteractionListener cityInteractionListener;

    public CitiesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        cityAdapter = new CityAdapter(LayoutInflater.from(getContext()), getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_cities_view, this);
        initControls();
        setRecyclerView();
        setAdpater();
    }

    private void initControls(){
        multiView = Views.findById(this,R.id.multi_view);
        emptyViewTxt = (TextView) multiView.findViewById(R.id.txt_empty);
        emptyViewIcon = (AppCompatImageView) multiView.findViewById(R.id.img_empty);
    }

    private void setRecyclerView() {
        recyclerView = Views.findById(this, R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.seperator_72);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setAdpater(){
        recyclerView.setAdapter(cityAdapter);
    }


    @Override
    public void attach(CityInteractionListener cityInteractionListener) {
        this.cityInteractionListener = cityInteractionListener;
        cityAdapter.attach(cityInteractionListener);
    }

    @Override
    public void detach(CityInteractionListener cityInteractionListener) {
        this.cityInteractionListener = null;
        cityAdapter.detach(cityInteractionListener);
    }

    @Override
    public void display(Cities cities) {
        cityAdapter.setData(cities);
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
        emptyViewTxt.setText(getContext().getString(R.string.str_no_city_found));
        emptyViewIcon.setImageResource(R.drawable.ic_assistant_photo_24dp);
        multiView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }
}
