package com.donate.savelife.country;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.donate.savelife.R;
import com.donate.savelife.component.text.EditText;
import com.donate.savelife.firebase.Dependencies;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ravi on 17/03/17.
 */

public class CountryActivity extends AppCompatActivity{


    private EditText mCityNameET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        FirebaseDatabase firebaseDatabase = Dependencies.INSTANCE.getFirebaseDatabase();
        final DatabaseReference countryDB = firebaseDatabase.getReference("countries");

        mCityNameET = (EditText) findViewById(R.id.city_name);
        findViewById(R.id.add_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map map = new HashMap();
                map.put("name", mCityNameET.getText().toString());
                countryDB.child("IN").push().setValue(map);
            }
        });
    }
}
