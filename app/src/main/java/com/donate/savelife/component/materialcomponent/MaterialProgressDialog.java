package com.donate.savelife.component.materialcomponent;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.donate.savelife.R;


/**
 * Created by ravi on 09/09/16.
 */
public class MaterialProgressDialog extends ProgressDialog {
    // Default background for the progress spinner
    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    private Context context;
    private MaterialProgressDrawable indeterminateDrawable;
    private int mDefaultColor;
    private int mColor;

    public MaterialProgressDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDefaultColor = ContextCompat.getColor(context, R.color.material_green);

        indeterminateDrawable = new MaterialProgressDrawable(getContext(), findViewById(android.R.id.progress));
        indeterminateDrawable.setBackgroundColor(CIRCLE_BG_LIGHT);
        indeterminateDrawable.setAlpha(255);
        indeterminateDrawable.updateSizes(MaterialProgressDrawable.XLARGE);
        indeterminateDrawable.setColorSchemeColors(getColor());
        indeterminateDrawable.start();
        setIndeterminateDrawable(indeterminateDrawable);
    }

    public void setColor(int color){
        mColor = color;
    }

    public int getColor(){
        if(mColor == 0){
            return mDefaultColor;
        }
        return mColor;
    }
}
