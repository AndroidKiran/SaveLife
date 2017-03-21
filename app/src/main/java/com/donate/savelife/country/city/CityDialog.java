package com.donate.savelife.country.city;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donate.savelife.R;
import com.donate.savelife.core.country.OnCityInteractionListener;
import com.donate.savelife.core.country.displayer.CityDisplayer;
import com.donate.savelife.core.country.presenter.CityPresenter;
import com.donate.savelife.firebase.Dependencies;

/**
 * Created by ravi on 17/03/17.
 */

public class CityDialog extends DialogFragment {

    private OnCityInteractionListener fragmentInteractionListener;
    private CityPresenter cityPresenter;

    public static CityDialog newInstance(FragmentManager fragmentManager){
        CityDialog cityDialog = new CityDialog();
        cityDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        cityDialog.show(fragmentManager, "Cities Dialog");
        return cityDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cities_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CityDisplayer citiesView = (CityDisplayer) view.findViewById(R.id.cities_view);

        cityPresenter = new CityPresenter(Dependencies.INSTANCE.getCountryService(),
                citiesView,
                fragmentInteractionListener,
                Dependencies.INSTANCE.getAnalytics(),
                Dependencies.INSTANCE.getErrorLogger(),
                "IN");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (OnCityInteractionListener) context;
    }

    @Override
    public void onDetach() {
        fragmentInteractionListener = null;
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        cityPresenter.startPresenting();
    }

    @Override
    public void onStop() {
        cityPresenter.stopPresenting();
        super.onStop();
    }
}
