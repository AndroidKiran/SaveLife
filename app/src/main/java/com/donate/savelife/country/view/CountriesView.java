package com.donate.savelife.country.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.donate.savelife.R;
import com.donate.savelife.component.DividerItemDecoration;
import com.donate.savelife.component.MultiStateView;
import com.donate.savelife.component.text.ClearableEditText;
import com.donate.savelife.core.country.displayer.CountriesDisplayer;
import com.donate.savelife.core.country.model.Countries;
import com.novoda.notils.caster.Views;

/**
 * Created by ravi on 01/10/16.
 */

public class CountriesView extends LinearLayout implements CountriesDisplayer {

    private CountryAdapter countryAdapter;
    private RecyclerView recyclerView;
    private CountryInteractionListener countryInteractionListener;
    private ClearableEditText searchView;
    private MultiStateView multiView;

    public CountriesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        countryAdapter = new CountryAdapter(LayoutInflater.from(getContext()), getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_countries_view, this);
        initControls();
        setRecyclerView();
        setAdpater();
    }

    private void initControls(){
        searchView = Views.findById(this, R.id.search_view);
        multiView = Views.findById(this,R.id.multi_view);
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
        recyclerView.setAdapter(countryAdapter);
    }


    @Override
    public void attach(CountryInteractionListener countryInteractionListener) {
        this.countryInteractionListener = countryInteractionListener;
        countryAdapter.attach(countryInteractionListener);
        searchView.addTextChangedListener(textWatcher);
    }

    @Override
    public void detach(CountryInteractionListener countryInteractionListener) {
        this.countryInteractionListener = null;
        countryAdapter.detach(countryInteractionListener);
        searchView.addTextChangedListener(null);
    }

    @Override
    public void display(Countries countries) {
        countryAdapter.setData(countries);
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


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            countryAdapter.onCountryFilter(editable.toString());
        }
    };
}
