package com.exclusave.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.fxn.pix.Options;
import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;
import com.exclusave.Activities.LoginActivity;
import com.exclusave.Activities.SignUpTesting;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.exclusave.Fragments.FragmentSignUp_Page1.lastName;
import static com.exclusave.Fragments.FragmentSignUp_Page1.middleName;
import static com.fxn.pix.Pix.start;
import static com.exclusave.Fragments.FragmentSignUp_Page1.CityID;
import static com.exclusave.Fragments.FragmentSignUp_Page1.cardPosition;
import static com.exclusave.Fragments.FragmentSignUp_Page1.city;
import static com.exclusave.Fragments.FragmentSignUp_Page1.email;
import static com.exclusave.Fragments.FragmentSignUp_Page1.emailAddressString;
import static com.exclusave.Fragments.FragmentSignUp_Page1.firstName;
import static com.exclusave.Fragments.FragmentSignUp_Page1.firstnameString;
import static com.exclusave.Fragments.FragmentSignUp_Page1.lastNameString;
import static com.exclusave.Fragments.FragmentSignUp_Page1.middleNameString;
import static com.exclusave.Fragments.FragmentSignUp_Page1.phone;
import static com.exclusave.Fragments.FragmentSignUp_Page1.phoneNumberString;

public class FragmentSignUp_Page2 extends Fragment implements View.OnClickListener {

    //LinearLayout submit;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    Map<String, String> body = new HashMap<String, String>();
    public static TextView Ammount;
    public static TextView applyCode;
    TextView weblinks;
    static TextView uploadID;
    String f_name, m_name, l_name, mail, cell, mycity, card_id, user_id;
    CheckBox checkBox;
    ArrayList<String> returnValue = new ArrayList<>();
    Uri myUri;
    String ts;
    Bitmap bitmap;
    String paymentResultCode = "0";
    int codeRequest = 1234;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    ViewPager viewPager;
    Bitmap uploadIDUrl;
    public static ImageView paymentTab;
    public static EditText promocode;
    String CouponID = "";
    public static TextView payment_text;

    Options options;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_pager_itemtwo, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initilizeViews();
        weblinks.setMovementMethod(LinkMovementMethod.getInstance());

        if (!Ammount.getText().toString().isEmpty()) {
            String str = Ammount.getText().toString();
            String[] splited = str.split("\\s+");
            if (Float.parseFloat(splited[0]) == 0.0) {
                paymentTab.setVisibility(View.INVISIBLE);
                payment_text.setVisibility(View.INVISIBLE);
                //    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.youAreNotRequiredToPay), Toast.LENGTH_SHORT).show();
            }
        }

        sharedpreferences = BaseClass.sharedPreferances(getActivity());
        editor = BaseClass.sharedPreferancesEditor(getActivity());

        Long tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();

        Log.e("timestamp", ts);
        //     Log.e("timestamp", FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getIsCouponCodeRequired()+"");

        SignUpTesting.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str1 = Ammount.getText().toString();
                String[] splited1 = str1.split("\\s+");
                Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                if (SignUpTesting.btn_submit.getText().toString().equals(getResources().getString(R.string.next))){
                    if (firstnameString.equals("") || firstnameString.startsWith(" ")) {
                        viewPager.setCurrentItem(0);
                        firstName.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.EnterFirstName), Toast.LENGTH_LONG).show();
                    }else if (middleNameString.equals("") || middleNameString.startsWith(" ")) {
                        viewPager.setCurrentItem(0);
                        middleName.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.EnterMiddleName), Toast.LENGTH_LONG).show();
                    }else if (lastNameString.equals("") || lastNameString.startsWith(" ")) {
                        viewPager.setCurrentItem(0);
                        lastName.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.EnterLastName), Toast.LENGTH_LONG).show();
                    }else if (emailAddressString.equals("") || emailAddressString.startsWith(" ") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressString).matches()) {
                        viewPager.setCurrentItem(0);
                        email.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.InvalidEmail), Toast.LENGTH_LONG).show();
                    } else if (!phoneNumberString.startsWith("+966") && !phone.equals("")) {
                        viewPager.setCurrentItem(0);
                        phone.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.InvalidPhone), Toast.LENGTH_LONG).show();
                    } else if (CityID.isEmpty()) {
                        viewPager.setCurrentItem(0);
                        city.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.PleaseSelectYourCity), Toast.LENGTH_LONG).show();
                    }else {
                        SignUpTesting.viewPager.setCurrentItem(1);
                    }
                }else if (SignUpTesting.btn_submit.getText().toString().equals(getResources().getString(R.string.submit))){
                    if (firstnameString.equals("") || firstnameString.startsWith(" ")) {
                        viewPager.setCurrentItem(0);
                        firstName.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.EnterFirstName), Toast.LENGTH_LONG).show();
                    } else if (emailAddressString.equals("") || emailAddressString.startsWith(" ") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressString).matches()) {
                        viewPager.setCurrentItem(0);
                        email.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.InvalidEmail), Toast.LENGTH_LONG).show();
                    } else if (!phoneNumberString.startsWith("+966") && !phone.equals("")) {
                        viewPager.setCurrentItem(0);
                        phone.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.InvalidPhone), Toast.LENGTH_LONG).show();
                    } else if (CityID.isEmpty()) {
                        viewPager.setCurrentItem(0);
                        city.startAnimation(shake);
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.PleaseSelectYourCity), Toast.LENGTH_LONG).show();
                    } else if (uploadID.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.pleaseUploadID), Toast.LENGTH_SHORT).show();
                    } else if (!checkBox.isChecked()) {
                        checkBox.startAnimation(shake);
                    } else if (paymentResultCode.equals("100") || Float.parseFloat(splited1[0]) == 0.0) {
                        userSignUpApiCall();
                    } else {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.pleasepaytoregister), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser == true) {
            f_name = sharedpreferences.getString("FIRST_NAME", "");
            m_name = sharedpreferences.getString("MIDDLE_NAME", "");
            l_name = sharedpreferences.getString("LAST_NAME", "");
            cell = sharedpreferences.getString("PHONE", "");
            mail = sharedpreferences.getString("EMAIL", "");
            mycity = sharedpreferences.getString("CITY_ID", "");
            card_id = sharedpreferences.getString("CARD_ID", "");
            user_id = sharedpreferences.getString("USER_ID", "");
            SignUpTesting.btn_submit.setText(getResources().getString(R.string.submit));
        }
    }

    private void initilizeViews() {
        uploadID = getView().findViewById(R.id.uploadID);
        Ammount = getView().findViewById(R.id.amount);
        checkBox = getView().findViewById(R.id.checkBox);
        viewPager = getActivity().findViewById(R.id.contain);
        promocode = getView().findViewById(R.id.promoCode);
        paymentTab = getView().findViewById(R.id.payment);
        weblinks = getView().findViewById(R.id.weblinks);
        payment_text = getView().findViewById(R.id.paymenttext);

        paymentTab.setOnClickListener(this);
        uploadID.setOnClickListener(this);
        applyCode = getView().findViewById(R.id.applyCode);
        applyCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == SignUpTesting.submit) {
            //Toast.makeText(getActivity(),"clicked", Toast.LENGTH_SHORT).show();
        } else if (view == uploadID) {
            Log.e("cameraClick", "yes");
            returnValue.clear();
            start(getActivity(), 100);
        } else if (view == applyCode) {
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            if (promocode.getText().toString().isEmpty()) {
                promocode.startAnimation(shake);
            } else if (promocode.getText().toString().startsWith(" ")) {
                promocode.startAnimation(shake);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.checkEmptySpaces), Toast.LENGTH_LONG).show();
            } else {
                ApplyCoupon();
            }
        } else if (view == paymentTab) {

            String str = Ammount.getText().toString();
            String[] splited = str.split("\\s+");
            //  payment = Float.valueOf(Ammount.getText().toString());
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            if (firstnameString.equals("") || firstnameString.startsWith(" ")) {
                viewPager.setCurrentItem(0);
                firstName.startAnimation(shake);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.EnterFirstName), Toast.LENGTH_LONG).show();
            }else if (middleNameString.equals("") || middleNameString.startsWith(" ")) {
                viewPager.setCurrentItem(0);
                middleName.startAnimation(shake);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.EnterMiddleName), Toast.LENGTH_LONG).show();
            }else if (lastNameString.equals("") || lastNameString.startsWith(" ")) {
                viewPager.setCurrentItem(0);
                lastName.startAnimation(shake);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.EnterLastName), Toast.LENGTH_LONG).show();
            } else if (emailAddressString.equals("") || emailAddressString.startsWith(" ") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressString).matches()) {
                viewPager.setCurrentItem(0);
                email.startAnimation(shake);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.InvalidEmail), Toast.LENGTH_SHORT).show();
            } else if (!phoneNumberString.startsWith("+966") && !phone.equals("")) {
                viewPager.setCurrentItem(0);
                phone.startAnimation(shake);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.InvalidPhone), Toast.LENGTH_LONG).show();
            } else if (CityID.isEmpty()) {
                viewPager.setCurrentItem(0);
                city.setText("");
                city.startAnimation(shake);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.PleaseSelectYourCity), Toast.LENGTH_LONG).show();
            } else if (uploadID.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.pleaseUploadID), Toast.LENGTH_SHORT).show();
            } else if (Float.parseFloat(splited[0]) == 0.0) {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.youAreNotRequiredToPay), Toast.LENGTH_SHORT).show();
            } else if (!checkBox.isChecked()) {
                checkBox.startAnimation(shake);
            } else if (FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getIsCouponCodeRequired().equals("Yes")) {
                if (CouponID.equals("")) {
                    promocode.startAnimation(shake);
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.pleaseEnterPromoCode), Toast.LENGTH_LONG).show();
                } else {
                    payTabPayment();
                }
            } else if (FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getIsCouponCodeRequired().equals("No")) {
                payTabPayment();
            }
        }
    }

    private void payTabPayment() {
        Intent in = new Intent(getActivity(), PayTabActivity.class);
        in.putExtra(PaymentParams.MERCHANT_EMAIL, "a.bajnaid@exclusavecard.com");
        in.putExtra(PaymentParams.SECRET_KEY, "OfKvL2rbMaDs4a7GdaTQYD1HIupixPWm4HRPKj1wtFZyQ6e4h8UgLVYupnNXcKjhrb7bu8IYOIwEPKJ7SDnEzl8ZJWWVGgd7skjW");
        in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
        in.putExtra(PaymentParams.TRANSACTION_TITLE, "Test Paytabs android library");
//        in.putExtra(PaymentParams.AMOUNT, Double.parseDouble(Ammount.getText().toString()));
        in.putExtra(PaymentParams.AMOUNT, Double.parseDouble(FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getAmount()));

        in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
        in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, phoneNumberString);
        in.putExtra(PaymentParams.CUSTOMER_EMAIL, emailAddressString);
        in.putExtra(PaymentParams.ORDER_ID, ts);
        in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1");

//Billing Address
        in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_BILLING, "Manama");
        in.putExtra(PaymentParams.STATE_BILLING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "+966"); //Put Country Phone code if Postal code not available '00973'

//Shipping Address
        in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
        in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
        in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
        in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
        in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'
//Payment Page Style
        in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");
        in.putExtra(PaymentParams.THEME, PaymentParams.THEME_LIGHT);

//Tokenization
        in.putExtra(PaymentParams.IS_TOKENIZATION, true);
        startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
    }

    // @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
            paymentResultCode = data.getStringExtra(PaymentParams.RESPONSE_CODE);
            if (paymentResultCode.equals("100")) {
                paymentTab.setClickable(false);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.paymentDoneSuccessfully), Toast.LENGTH_LONG);
                //userSignUpApiCall();
                //SignUpTesting.submit.setClickable(false);
                // SignUpTesting.submit.setBackgroundColor(R.color.deselected_color);
            }
            Log.e("Tag", paymentResultCode);
            Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
            }
        }
    }

    private void userSignUpApiCall() {

        BaseClass.showCustomLoader(getActivity());
        String language = BaseClass.checkLanguageFunction(getActivity());
        imgToStringFunction(uploadIDUrl);
        body.put("FirstName", firstnameString);
        body.put("MiddleName", middleNameString);
        body.put("LastName", lastNameString);
        body.put("Mobile", phoneNumberString);
        body.put("Email", emailAddressString);
        body.put("CityID", CityID);
        body.put("CouponID", CouponID);
        body.put("Language",language);
//        body.put("Amount", Ammount.getText().toString());
        body.put("Amount", FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getAmount());
        body.put("CardID", FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getCardID());
        if (!user_id.equals(null)) {
            body.put("UserID", sharedpreferences.getString("USER_ID", ""));
        }
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));


        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.SIGNUP, getActivity(), body, header, new ServerCallback() {
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
                            Dialog regsuccess = new Dialog(getActivity(), android.R.style.ThemeOverlay_Material_Dialog_Alert);
                            regsuccess.setCancelable(false);
                            regsuccess.setContentView(R.layout.registeration_success_dialog);
                            ImageView cancel = regsuccess.findViewById(R.id.cancel);

                            regsuccess.show();
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().finish();
                                    regsuccess.dismiss();
                                }
                            });

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        BaseClass.dialog.dismiss();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setTextViewText(String value, Bitmap url) {
        uploadID.setText(value);
        uploadIDUrl = url;
        Log.e("ImageUploaded", value + "  " + url);
    }

    private Map<String, String> imgToStringFunction(Bitmap bitmap) {

        if (bitmapArrayList.size() > 0) {
            Log.e("sizee", bitmapArrayList.size() + "");
            for (int j = 0; j < bitmapArrayList.size(); j++) {
                bitmap = bitmapArrayList.get(j);
                Log.e("bitmap", bitmap + " ");
                ByteArrayOutputStream bos = new ByteArrayOutputStream(); // Compressed
                ByteArrayOutputStream original_image = new ByteArrayOutputStream(); // Original
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); // Compressed
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, original_image); // Original
                byte[] data = bos.toByteArray(); // Compressed
                byte[] original_data = original_image.toByteArray(); // Original
                // images[0] = Base64.encodeToString(data, Base64.DEFAULT);
                // images[1] = Base64.encodeToString(original_data, Base64.DEFAULT);
                body.put("IDImage", Base64.encodeToString(data, Base64.DEFAULT));

            }
        }
        return body;
    }

    private void ApplyCoupon() {

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        Map<String, String> body = new HashMap<String, String>();
        body.put("CouponCode", promocode.getText().toString());
        body.put("Date", ts);
        body.put("Amount", FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getAmount());
        body.put("CardID", FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getCardID());
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));

        Log.e("CardID", FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getCardID());
        Log.e("GetApply", body.toString());

        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));


        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.ApplyCoupon, getActivity(), body, header, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("GetApplyCouponResp", result + " ");
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            JSONObject coupon = jsonObject.getJSONObject("coupon");
                            String DiscountAppliedFactor = coupon.getString("DiscountAppliedFactor");
                            String DiscountAvailed = coupon.getString("DiscountAvailed");
                            String DiscountedAmount = coupon.getString("DiscountedAmount");
                            CouponID = coupon.getString("CouponID");

//                            if(!(Integer.parseInt(DiscountedAmount) > 0)){
//                                paymentTab.setVisibility(View.GONE);
//                            }
                            Ammount.setText(DiscountedAmount + " SAR");
                            String str = Ammount.getText().toString();
                            String[] splited = str.split("\\s+");
                            if (Float.parseFloat(splited[0]) == 0.0) {
                                paymentTab.setVisibility(View.INVISIBLE);
                                payment_text.setVisibility(View.INVISIBLE);
                            //    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.youAreNotRequiredToPay), Toast.LENGTH_SHORT).show();
                            }
                            promocode.setEnabled(false);
                            applyCode.setEnabled(false);
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
