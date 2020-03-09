package com.exclusave.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Book_Now_Activity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button send_request;
    TextView date_of_visit, time_prefer;
    int currentday,currentmonth,currentyear,selectedDAY,selectedMONTH,selectedYear;
    EditText specialRequest, noOfGuests;
    String bookingDate = "", bookingTime = "";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__now_);

        sharedPreferences = BaseClass.sharedPreferances(Book_Now_Activity.this);
        editor = BaseClass.sharedPreferancesEditor(Book_Now_Activity.this);

        initilizeViews();
    }

    private void initilizeViews() {
        send_request = findViewById(R.id.send_req);
        date_of_visit = findViewById(R.id.date_of_visit);
        time_prefer = findViewById(R.id.time_prefered);
        noOfGuests = findViewById(R.id.noOfGuests);
        specialRequest = findViewById(R.id.specialRequest);

        send_request.setBackgroundDrawable( new BaseClass.DrawableGradient(new int[] {Color.parseColor(sharedPreferences.getString("PackageColorUpper","")), Color.parseColor(sharedPreferences.getString("PackageColorLower","")), Color.parseColor(sharedPreferences.getString("PackageColorUpper",""))}, 8));


        send_request.setOnClickListener(this);
        date_of_visit.setOnClickListener(this);
        time_prefer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == send_request){
            bookNowApi();
        }else if (view == date_of_visit){
            datePicker();
        }else if (view == time_prefer){
            if (date_of_visit.getText().toString().isEmpty()){
                Toast.makeText(Book_Now_Activity.this,getResources().getString(R.string.pleaseSelectDate), Toast.LENGTH_SHORT).show();
            }else {
                timePicker();
            }
        }
    }

    private void timePicker(){

        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                Book_Now_Activity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        int chrs = now.get(Calendar.HOUR_OF_DAY);
        int cmin = now.get(Calendar.MINUTE);
        int csec = now.get(Calendar.SECOND);

        tpd.setThemeDark(true);
        tpd.vibrate(true);
        tpd.dismissOnPause(true);
        tpd.enableSeconds(false);
        tpd.setAccentColor(Color.parseColor("#222222"));
        tpd.setTimeInterval(1, 5, 10);
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");
        if (selectedDAY == currentday) {
            tpd.setMinTime(chrs, cmin, csec);
        }
    }

    private void datePicker() {

        time_prefer.setText("");
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                Book_Now_Activity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        currentday = now.get(Calendar.DAY_OF_MONTH);
        currentmonth = now.get(Calendar.MONTH);
        currentyear = now.get(Calendar.YEAR);

        dpd.setMinDate(now);
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        selectedDAY = dayOfMonth;
        selectedMONTH = monthOfYear;
        selectedYear = year;
        date_of_visit.setText(date);

        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        bookingDate = String.valueOf(calendar.getTime().getTime() / 1000);
        Log.e("BookingDate", bookingDate);
//        bookingDate =  calendar.getTime() / 1000
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        time_prefer.setText(twelveHrsConverter(hourOfDay,minute));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Log.e("preferTime", String.valueOf(calendar.getTimeInMillis()));
        bookingTime = String.valueOf(calendar.getTime().getTime()/1000);
        Log.e("BookingTime", bookingTime);
//        bookingTime = calendar.getTime() / 1000

    }

    private  String twelveHrsConverter(int hrs,int mins){
        String convertedtime = "";
        final String time = hrs+":"+mins;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(time);
            convertedtime = new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return convertedtime;
    }

    private void bookNowApi() {

        String language = BaseClass.checkLanguageFunction(Book_Now_Activity.this);
        Animation shake = AnimationUtils.loadAnimation(Book_Now_Activity.this, R.anim.shake);
        if(date_of_visit.getText().toString().isEmpty() || date_of_visit.getText().toString().startsWith(" ")){
            date_of_visit.startAnimation(shake);
            Toast.makeText(Book_Now_Activity.this, getResources().getString(R.string.pleaseSelectDate), Toast.LENGTH_SHORT).show();
        }else if(time_prefer.getText().toString().isEmpty() || time_prefer.getText().toString().startsWith(" ")){
            time_prefer.startAnimation(shake);
            Toast.makeText(Book_Now_Activity.this, getResources().getString(R.string.pleaseSelectTime), Toast.LENGTH_SHORT).show();
        }else if(noOfGuests.getText().toString().isEmpty() || noOfGuests.getText().toString().startsWith(" ")){
            noOfGuests.startAnimation(shake);
            Toast.makeText(Book_Now_Activity.this, getResources().getString(R.string.pleaseSelectGuests), Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(noOfGuests.getText().toString()) <= 0){
            noOfGuests.startAnimation(shake);
            Toast.makeText(Book_Now_Activity.this, getResources().getString(R.string.pleaseSelectGuests), Toast.LENGTH_SHORT).show();
        } else{
            BaseClass.showCustomLoader(Book_Now_Activity.this);
            Map<String, String> body = new HashMap<String, String>();
            body.put("UserID", sharedPreferences.getString("UserID", ""));
            body.put("StoreID", sharedPreferences.getString("StoreID", ""));
            body.put("PromotionID", sharedPreferences.getString("PromotionID", ""));
            body.put("BookingDate", bookingDate);
            body.put("BookingTime", bookingTime);
            body.put("NoOfGuests", noOfGuests.getText().toString());
            body.put("SpecialRequest", specialRequest.getText().toString());
            body.put("IsFamilyMember", sharedPreferences.getString("IsFamilyMember", ""));
            body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
//            body.put("Language",language);

            HashMap<String, String> header = new HashMap<String, String>();
            header.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));


            ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.CREATEBOOKING, Book_Now_Activity.this, body, header, new ServerCallback() {
                @Override
                public void onSuccess(String result, String ERROR) {
                    BaseClass.dialog.dismiss();
                    if (ERROR.isEmpty()) {
                        Log.e("BookingResp", result + " ");
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(result));
                            String message = jsonObject.getString("message");
                            int status = jsonObject.getInt("status");
                            if (status == 200) {
                                finish();
                                Toast.makeText(Book_Now_Activity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Book_Now_Activity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(Book_Now_Activity.this, ERROR, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
