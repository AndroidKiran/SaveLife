package com.donate.savelife.intro;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.donate.savelife.R;

/**
 * Created by ravi on 24/12/16.
 */

public class IntroPageTransformer implements ViewPager.PageTransformer {

    private final Context context;
    private final AppCompatActivity appCompactActivity;

    public IntroPageTransformer(Context context) {
        this.context = context;
        this.appCompactActivity = (AppCompatActivity) context;
    }

    @Override
    public void transformPage(View page, float position) {

        int pageWidth = page.getWidth();
        float pageWidthTimesPosition = pageWidth * position;
        float absPosition = Math.abs(position);

        View title1 = page.findViewById(R.id.title1);
        View description1 = page.findViewById(R.id.description1);

        View title2 = page.findViewById(R.id.title2);
        View description2 = page.findViewById(R.id.description2);

        View title3 = page.findViewById(R.id.title3);
        View description3 = page.findViewById(R.id.description3);

        View icon1 = page.findViewById(R.id.icon1);
        View icon2 = page.findViewById(R.id.icon2);
        View icon3 = page.findViewById(R.id.sign_in);


        if (position <= -1.0f || position >= 1.0f) {
        } else if (position == 0.0f) {

        } else {

            translateX(icon1, -pageWidthTimesPosition);
            alphaAnim(icon1, absPosition);

            translateY(title1, -pageWidthTimesPosition);
            alphaAnim(title1, absPosition);

            translateY(description1, pageWidthTimesPosition);
            alphaAnim(description1, absPosition);

            translateX(icon2, -pageWidthTimesPosition);
            alphaAnim(icon2, absPosition);

            translateY(title2, -pageWidthTimesPosition);
            alphaAnim(title2, absPosition);

            translateY(description2, pageWidthTimesPosition);
            alphaAnim(description2, absPosition);

            translateX(icon3, -pageWidthTimesPosition);
            alphaAnim(icon3, absPosition);

            translateY(title3, -pageWidthTimesPosition);
            alphaAnim(title3, absPosition);

            translateY(description3, pageWidthTimesPosition);
            alphaAnim(description3, absPosition);
        }
    }

    public void alphaAnim(View view, float absPosition) {
        if (view != null) {
            view.setAlpha(1.0f - absPosition);
        }
    }

    private void translateY(View view, float pageWidthTimesPosition) {
        if (view != null) {
            view.setTranslationY(pageWidthTimesPosition / 2f);
        }
    }

    private void translateX(View view, float pageWidthTimesPosition) {
        if (view != null)
            view.setTranslationX(pageWidthTimesPosition / 4f);
    }

}
