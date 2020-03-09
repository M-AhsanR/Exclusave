package com.exclusave.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.fxn.pix.Pix;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.CardsData;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import static com.fxn.pix.Pix.start;
import static com.exclusave.Activities.SignUpActivity.citiesData;
import static com.exclusave.Activities.SignUpActivity.citiesNames;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp_ViewPager_Adapter extends PagerAdapter implements View.OnClickListener {
    private Context context;
    private LayoutInflater layoutInflater;
    TextView submit_btn, uploadID, amount;
    ArrayList<String> arrayList;
    LinearLayout page1, page2, submit;
    ViewPager cardTypePager;
    EditText cardNumber, firstName, middleName, lastName, email, phone, promoCode, password, confirmPassword;
    ImageView payment;
    CheckBox checkBox;
    AutoCompleteTextView city;
    private static String CityID = "1";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<String> returnValue = new ArrayList<>();
    ArrayList<CardsData> cardsData;

    private static String firstnameString = "", cardNumberString = "", middleNameString = "", lastNameString = "", emailAddressString = "",
            phoneNumberString = "";

    public SignUp_ViewPager_Adapter(Context context, ArrayList<CardsData> cardsData) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.cardsData = cardsData;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
//        notifyDataSetChanged();
//        getCards();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View view = this.layoutInflater.inflate(R.layout.signup_pager_itemone, container, false);

        page1 = view.findViewById(R.id.page1);
        page2 = view.findViewById(R.id.page2);
        submit = view.findViewById(R.id.submit);
        cardTypePager = view.findViewById(R.id.cardTypeViewPager);
        cardNumber = view.findViewById(R.id.cardNumber);
        firstName = view.findViewById(R.id.firstName);
        middleName = view.findViewById(R.id.middleName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);
        city = view.findViewById(R.id.city);
        payment = view.findViewById(R.id.payment);
        uploadID = view.findViewById(R.id.uploadID);
        promoCode = view.findViewById(R.id.promoCode);
        amount = view.findViewById(R.id.amount);
        checkBox = view.findViewById(R.id.checkBox);
//        password = view.findViewById(R.id.password);
//        confirmPassword = view.findViewById(R.id.confirmPassword);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, citiesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        city.setAdapter(adapter);

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                hideKeyboard(city);
                city.showDropDown();
            }
        });

        city.setTextIsSelectable(true);
        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    CityID = citiesData.get(position).getCityID();

//                    mEditor.putString("CityID", CityID).commit();
//                    mEditor.putString("City", citiesDataArrayList.get(position).getTitle()).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TextChangeListenres();

        SpannableString ss1 = new SpannableString(context.getResources().getString(R.string.ihaveReadAndAgreed));
//        ss1.setSpan(new RelativeSizeSpan(2f), 0,5, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 30, 48, 0);// set color
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 57, 71, 0);// set color
        checkBox.setText(ss1);

        if (position == 0) {
            page1.setVisibility(View.VISIBLE);
            page2.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            CardTypePagerAdapter cardTypePagerAdapter = new CardTypePagerAdapter(context, cardsData);
            cardTypePager.setAdapter(cardTypePagerAdapter);
            cardTypePager.setPageMargin(40);
            cardTypePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    Log.e("currentItem", cardsData.get(position).getCardType());
                    if (cardsData.get(position).getCardType().equals("Company")) {
                        cardNumber.setVisibility(View.VISIBLE);
                        Log.e("visibility", "visible");

                    } else if (cardsData.get(position).getCardType().equals("Exclusave")) {
                        cardNumber.setVisibility(View.INVISIBLE);
                        Log.e("visibility", "gone");
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        } else if (position == 1) {
            submit.setVisibility(View.VISIBLE);
            page1.setVisibility(View.GONE);
            page2.setVisibility(View.VISIBLE);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                firstnameString = s.toString();
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void Register() {
//        Log.e("firstName", firstnameString);
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        if (firstnameString.equals("") || firstnameString.startsWith(" ")) {
            firstName.setText("");
            firstName.startAnimation(shake);
            Toast.makeText(context, context.getResources().getString(R.string.EnterFirstName), Toast.LENGTH_LONG).show();
        } else if (emailAddressString.equals("") || emailAddressString.startsWith(" ") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressString).matches()) {
            email.setText("");
            email.startAnimation(shake);
            Toast.makeText(context, context.getResources().getString(R.string.InvalidEmail), Toast.LENGTH_LONG).show();
        } else if (!phoneNumberString.startsWith("+966") && !phone.equals("")) {
            phone.setText("");
            phone.startAnimation(shake);
            Toast.makeText(context, context.getResources().getString(R.string.InvalidPhone), Toast.LENGTH_LONG).show();
        } else if (CityID.isEmpty()) {
            city.setText("");
            city.startAnimation(shake);
            Toast.makeText(context, context.getResources().getString(R.string.PleaseSelectYourCity), Toast.LENGTH_LONG).show();
        } else if (password.getText().toString().isEmpty() || password.getText().toString().startsWith(" ")) {
            password.setText("");
            password.startAnimation(shake);
            Toast.makeText(context, context.getResources().getString(R.string.pleaseEnterPassword), Toast.LENGTH_LONG).show();
        } else if (confirmPassword.getText().toString().isEmpty() || confirmPassword.getText().toString().startsWith(" ")) {
            confirmPassword.setText("");
            confirmPassword.startAnimation(shake);
            Toast.makeText(context, context.getResources().getString(R.string.pleaseEnterConfirmPassword), Toast.LENGTH_LONG).show();
        } else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            Toast.makeText(context, context.getResources().getString(R.string.passwordsDontMatch), Toast.LENGTH_LONG).show();
        } else if (!checkBox.isChecked()) {
            checkBox.startAnimation(shake);
        } else {
            signUpAPI();
//        }
        }

    }

    private void TextChangeListenres() {
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                firstnameString = s.toString();
            }
        });

        cardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cardNumberString = s.toString();
                if (cardNumberString.length() == 12) {
                    getUserDetails();
                }

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailAddressString = s.toString();
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumberString = s.toString();
            }
        });

        middleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                middleNameString = s.toString();
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                lastNameString = s.toString();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadID:
                returnValue.clear();
                Pix.start((FragmentActivity) context, 1234);
        }
    }

    private void getUserDetails() {
        String language = BaseClass.checkLanguageFunction((Activity) context);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.getUserDetail + "?CardNumber=" + cardNumberString + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&Language="+language, context, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                Log.e("UserDetailsResponse", result + " ");
                if (ERROR.isEmpty()) {
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONObject user_info = jsonObject.getJSONObject("user_info");
//
                            String AuthToken = user_info.getString("AuthToken");
                            String CardID = user_info.getString("CardID");
                            String CardNumber = user_info.getString("CardNumber");
                            String CityID = user_info.getString("CityID");
                            String CityTitle = user_info.getString("CityTitle");
                            String Email = user_info.getString("Email");
                            String FirstName = user_info.getString("FirstName");
                            String Gender = user_info.getString("Gender");
                            String Image = user_info.getString("Image");
                            String IsEmailVerified = user_info.getString("IsEmailVerified");
                            String IsMobileVerified = user_info.getString("IsMobileVerified");
                            String LastName = user_info.getString("LastName");
                            String MiddleName = user_info.getString("MiddleName");
                            String Mobile = user_info.getString("Mobile");

                            firstName.setText("Rizwan");
                            firstName.setEnabled(false);
                            middleName.setText(MiddleName);
                            middleName.setEnabled(false);
                            lastName.setText(LastName);
                            lastName.setEnabled(false);
                            email.setText(Email);
                            email.setEnabled(false);
                            phone.setText(Mobile);
                            phone.setEnabled(false);
                            city.setText(CityTitle);
                            city.setEnabled(false);


//                            mEditor.putString("CategoriesCount", String.valueOf(UserCategoriesCount)).commit();
//                            mEditor.putString("UserFollowedBooths", String.valueOf(UserFollowedBooths)).commit();
//                            mEditor.putString("IsEmailVerified", IsEmailVerified).commit();
//                            mEditor.putString("IsMobileVerified", IsMobileVerified).commit();

                        } else {
//                            DynamicToast.makeError(context, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                    Toast.makeText(SplashScreen.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()){
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    private void signUpAPI() {
        String language = BaseClass.checkLanguageFunction((Activity) context);
        BaseClass.showCustomLoader((Activity) context);

        Map<String, String> body = new HashMap<String, String>();
        body.put("FirstName", firstnameString);
        body.put("MiddleName", middleNameString);
        body.put("LastName", lastNameString);
        body.put("CardNumber", "");
        body.put("Email", emailAddressString);
        body.put("Mobile", phoneNumberString);
        body.put("CityID", "6");
        body.put("IDImage", "");
        body.put("Password", password.getText().toString());
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language",language);



        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));
        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.SignUp, context, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("sendFeedbackResp", result);
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        String message = jsonObject.getString("message");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
//                            String message = jsonObject.getString("message");
//                            JSONObject user_info = jsonObject.getJSONObject("user_info");
                            final Dialog dialog = new Dialog(context, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                            dialog.setContentView(R.layout.registeration_success_dialog);
                            ImageView cancel = dialog.findViewById(R.id.cancel);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(ContactUsActivity.this, MainActivity.class));
//                            finish();
                        } else {
                            BaseClass.dialog.dismiss();
//                            String message = jsonObject.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
    
}