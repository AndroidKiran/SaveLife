package com.donate.savelife.apputils;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.donate.savelife.R;
import com.donate.savelife.component.materialcomponent.MaterialProgressDialog;

/**
 * Created by ravi on 09/09/16.
 */
public class DialogUtils {

    public static void showMaterialProgressDialog(MaterialProgressDialog progressDialog, String title, String message, Context context) {

        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        int dividerId = progressDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = progressDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(ActivityCompat.getColor(context, R.color.bg_grey));
        }
    }


    public static AlertDialog showRequirementDialog(Context context, View dialogView){
        AlertDialog alertDialogBuilder = new AlertDialog.Builder(context, R.style.TransperentDialog)
                .setView(dialogView)
                .create();
        return alertDialogBuilder;
    }


    public static AlertDialog showDialog(Context context, View dialogView){
        AlertDialog alertDialogBuilder = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        return alertDialogBuilder;
    }


}
