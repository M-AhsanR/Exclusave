package com.exclusave;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.exclusave.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseClass {

    public static Dialog dialog;

    public static void showCustomLoader(Activity activity) {
        dialog = new Dialog(activity, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        dialog.setContentView(R.layout.custom_loader);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ImageView imageView = dialog.findViewById(R.id.loader);
        Animation pulse = AnimationUtils.loadAnimation(activity, R.anim.blow_animation);
        imageView.startAnimation(pulse);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    //  SharedPreferances initikization
    public static SharedPreferences sharedPreferances(Activity sharedaprefActivity){
        SharedPreferences sharedpreferences;
        String MyPREFERENCES = "MyPrefs";

        sharedpreferences = sharedaprefActivity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        return sharedpreferences;
    }

    // SharedPreferances Editor initilization
    public static SharedPreferences.Editor sharedPreferancesEditor(Activity sharedaprefActivity){
        SharedPreferences sharedpreferences;
        SharedPreferences.Editor mEditor;
        String MyPREFERENCES = "MyPrefs";

        sharedpreferences = sharedaprefActivity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();

        return mEditor;
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()){
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    public static class DrawableGradient extends GradientDrawable {
        public DrawableGradient(int[] colors, int cornerRadius) {
            super(Orientation.LEFT_RIGHT, colors);

            try {
                this.setShape(GradientDrawable.RECTANGLE);
                this.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                this.setCornerRadius(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        public DrawableGradient SetTransparency(int transparencyPercent) {
//            this.setAlpha(255 - ((255 * transparencyPercent) / 100));
//
//            return this;
//        }
    }

    public static class DrawableGradientForAccount extends GradientDrawable {
        public DrawableGradientForAccount(int[] colors, int cornerRadius) {
            super(Orientation.LEFT_RIGHT, colors);

            try {
                this.setShape(GradientDrawable.RECTANGLE);
                this.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                this.setCornerRadius(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        public DrawableGradient SetTransparency(int transparencyPercent) {
//            this.setAlpha(255 - ((255 * transparencyPercent) / 100));
//
//            return this;
//        }
    }

    public static String TimeStampToTime(String timestmp) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone(); /* debug: is it local time? */
        Log.d("Time zone: ", tz.getDisplayName()); /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        sdf.setTimeZone(tz); /* print your timestamp and double check it's the date you expect */
        long timestamp = Long.parseLong(timestmp);
        Log.e("checkingtime", timestmp);
        String localTime = sdf.format(new Date(timestamp * 1000));

        return localTime;
    }

    public static String TimeStampToDate(String timestmp) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone(); /* debug: is it local time? */
        Log.d("Time zone: ", tz.getDisplayName()); /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.YY");
        sdf.setTimeZone(tz); /* print your timestamp and double check it's the date you expect */
        long timestamp = Long.parseLong(timestmp);
        Log.e("checkingtime", timestmp);
        String localTime = sdf.format(new Date(timestamp * 1000));

        return localTime;
    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

    public static String checkLanguageFunction(Activity activity){
        Configuration configuration;
        configuration = activity.getResources().getConfiguration();
        if (configuration.getLayoutDirection() == 1) {
            return "AR";
        } else if (configuration.getLayoutDirection() == 0) {
            return "EN";
        }else
            return "null";
    }

}
