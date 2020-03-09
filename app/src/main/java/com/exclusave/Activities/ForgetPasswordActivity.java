package com.exclusave.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email;
    Button submit;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initilizeViews();
        BaseClass.hideKeyboard(email, ForgetPasswordActivity.this);
        sharedpreferences = BaseClass.sharedPreferances(ForgetPasswordActivity.this);
        editor = BaseClass.sharedPreferancesEditor(ForgetPasswordActivity.this);

    }

    private void initilizeViews() {
        email = findViewById(R.id.loginMail);
        submit = findViewById(R.id.submitBtn);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
                if (email.getText().toString().isEmpty() || email.getText().toString().startsWith(" ") || !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Animation shake = AnimationUtils.loadAnimation(ForgetPasswordActivity.this, R.anim.shake);
                    email.setText("");
                    email.startAnimation(shake);
                    Toast.makeText(ForgetPasswordActivity.this, this.getResources().getString(R.string.InvalidEmail), Toast.LENGTH_SHORT).show();
                } else
                    forgetPasswordApiCall();
                break;
        }
    }

    private void forgetPasswordApiCall() {

        String language = BaseClass.checkLanguageFunction(ForgetPasswordActivity.this);
        BaseClass.showCustomLoader(ForgetPasswordActivity.this);
        Map<String, String> body = new HashMap<String, String>();
        body.put("Query", email.getText().toString());
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
   //     body.put("Language",language);

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));


        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.FORGET_PASSWORD, ForgetPasswordActivity.this, body, header, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                BaseClass.dialog.dismiss();
                if (ERROR.isEmpty()) {
                    Log.e("GetCardsResp", result + " ");
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            finish();
                            Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
