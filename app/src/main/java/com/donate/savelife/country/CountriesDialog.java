package com.donate.savelife.country;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.country.OnCountryInteractionListener;
import com.donate.savelife.core.country.displayer.CountryDisplayer;
import com.donate.savelife.core.country.presenter.CountryPresenter;
import com.donate.savelife.firebase.Dependencies;


/**
 * Created by ravi on 01/10/16.
 */

public class CountriesDialog extends DialogFragment {

    private CountryPresenter countryPresenter;
    private OnCountryInteractionListener fragmentInteractionListener;

    public static CountriesDialog newInstance(FragmentManager fragmentManager){
        CountriesDialog countriesDialog = new CountriesDialog();
        countriesDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        countriesDialog.show(fragmentManager, "Countries Dialog");
        return countriesDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.countries_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CountryDisplayer countriesView = (CountryDisplayer) view.findViewById(R.id.countries_view);

        countryPresenter = new CountryPresenter(Dependencies.INSTANCE.getCountryService(),
                countriesView,
                fragmentInteractionListener,
                Dependencies.INSTANCE.getAnalytics(),
                Dependencies.INSTANCE.getErrorLogger());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (OnCountryInteractionListener) context;
    }

    @Override
    public void onDetach() {
        fragmentInteractionListener = null;
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        countryPresenter.startPresenting();
    }

    @Override
    public void onStop() {
        countryPresenter.stopPresenting();
        super.onStop();
    }
}
