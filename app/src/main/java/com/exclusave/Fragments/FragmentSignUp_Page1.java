package com.exclusave.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.exclusave.Activities.SignUpTesting;
import com.exclusave.Adapters.CardTypePagerAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.CardsData;
import com.exclusave.models.CitiesData;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class FragmentSignUp_Page1 extends Fragment implements View.OnClickListener {

    ViewPager cardTypePager;
    CardTypePagerAdapter cardTypePagerAdapter;
    public static EditText cardNumber, firstName, middleName, lastName, email, phone;
    LinearLayout btn_next;
    static AutoCompleteTextView city;
    static String CityID = "", CardId, CardUserId;
    // LinearLayout submit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;
    public static ImageView left_btn, right_btn;
    //View selectcard_view;
    public static ArrayList<CardsData> cardsData = new ArrayList<>();
    ArrayList<CitiesData> citiesData = new ArrayList<>();
    ArrayList<String> citiesNames = new ArrayList<>();
    ImageView left, right;
    public static String cardPosition = "0";
    String currentPosition = "0";
    public static int cardsCount;
    public static String firstnameString = "", cardNumberString = "", middleNameString = "", lastNameString = "", emailAddressString = "",
            phoneNumberString = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_pager_itemone, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = BaseClass.sharedPreferances(getActivity());
        mEditor = BaseClass.sharedPreferancesEditor(getActivity());

        mEditor.putString("USER_ID", "").commit();
//        CardId = cardsData.get(0).getCardID();
        //"1";
        initializeViews();
        left_btn.setVisibility(View.GONE);
        right_btn.setVisibility(View.VISIBLE);
        SpannableString span1 = new SpannableString(getActivity().getResources().getString(R.string.IfAvailable));
        span1.setSpan(new AbsoluteSizeSpan(27), 0, span1.length(), SPAN_INCLUSIVE_INCLUSIVE);
        //     span1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.orange)), 0, products.get(i).getCurrency().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString span2 = new SpannableString(getString(R.string.cardNumber));
       // span2.setSpan(new AbsoluteSizeSpan(25), 0, span2.length(), SPAN_INCLUSIVE_INCLUSIVE);

        CharSequence finalText = TextUtils.concat(span2, " ", span1);

        cardNumber.setHint(finalText);
        cardNumber.setGravity(Gravity.CENTER_VERTICAL);

//        SignUpTesting.submit.setVisibility(View.GONE);
        getCards();
        getCities();
//        pageChangeListener();
        cityClickListener();
        TextChangeListenres();

    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()){
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    private void getCards() {

        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        Log.e("header", headers.toString());

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GETCARDS + "?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("GetCardsResp", result + " ");
                    try {
                        cardsData.clear();
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray cardsArray = jsonObject.getJSONArray("cards");

                            for (int i = 0; i < cardsArray.length(); i++) {
                                JSONObject cardsObject = cardsArray.getJSONObject(i);
                                String CardCode = cardsObject.getString("CardCode");
                                String CardID = cardsObject.getString("CardID");
                                String CardType = cardsObject.getString("CardType");
                                String CardImage = cardsObject.getString("CardImage");
                                String Title = cardsObject.getString("Title");
                                String Amount = cardsObject.getString("Amount");
                                String IsCouponCodeRequired = cardsObject.getString("IsCouponCodeRequired");
                                String CardLogoImage = cardsObject.getString("CardLogoImage");


                                cardsData.add(new CardsData(CardCode, CardID, CardType, CardImage, Title, Amount, IsCouponCodeRequired, CardLogoImage));
                                cardsCount = cardsArray.length();
                            }

                            cardTypePagerAdapter = new CardTypePagerAdapter(getActivity(), cardsData);
                            cardTypePager.setAdapter(cardTypePagerAdapter);
                            cardTypePagerAdapter.notifyDataSetChanged();
                            cardTypePager.setPageMargin(40);

                            pageChangeListener();

                            FragmentSignUp_Page2.Ammount.setText(FragmentSignUp_Page1.cardsData.get(Integer.parseInt(cardPosition)).getAmount() + " SAR");

                            left_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cardTypePager.setCurrentItem(Integer.parseInt(currentPosition) - 1);
                                }
                            });

                            right_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cardTypePager.setCurrentItem(Integer.parseInt(currentPosition) + 1);
                                }
                            });


//                            Log.e("cardId", cardsData.get(Integer.parseInt(cardPosition)).getCardID());
//                            SignUp_ViewPager_Adapter adapter = new SignUp_ViewPager_Adapter(SignUpTesting.this, cardsData);
//                            reg_viewPager.setAdapter(adapter);
//                            reg_viewPager.setPageMargin(40);
//
//                            indicatorPager.setViewPager(reg_viewPager);

                        } else {
                            Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
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

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        //INSERT CUSTOM CODE HERE

        SignUpTesting.btn_submit.setText(getResources().getString(R.string.next));


    }

    private void getCities() {

        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CITIES + "?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OS=" + Build.VERSION.RELEASE + "&Language=" + language, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetCitiesResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray citiesArray = jsonObject.getJSONArray("cities");
                            for (int i = 0; i < citiesArray.length(); i++) {
                                JSONObject jsonObject1 = citiesArray.getJSONObject(i);

                                String Cityid = jsonObject1.getString("CityID");
                                String CityLat = jsonObject1.getString("CityLat");
                                String CityLong = jsonObject1.getString("CityLong");
                                String CityPlaceID = jsonObject1.getString("CityPlaceID");
                                String Title = jsonObject1.getString("Title");
                                citiesNames.add(Title);
                                citiesData.add(new CitiesData(Cityid, CityLat, CityLong, CityPlaceID, Title));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_list_item_1, citiesNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            city.setAdapter(adapter);

                        } else {
                            Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
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

    private void initializeViews() {
        cardTypePager = getView().findViewById(R.id.cardTypeViewPager);
        cardNumber = getView().findViewById(R.id.cardNumber);
        firstName = getView().findViewById(R.id.firstName);
        middleName = getView().findViewById(R.id.middleName);
        lastName = getView().findViewById(R.id.lastName);
        email = getView().findViewById(R.id.email);
        phone = getView().findViewById(R.id.phone);
        city = getView().findViewById(R.id.city);
        left_btn = getView().findViewById(R.id.left);
        right_btn = getView().findViewById(R.id.right);
        btn_next = getView().findViewById(R.id.btn_next);

//        submit = getActivity().findViewById(R.id.submit);
    }

    private void pageChangeListener() {
        cardTypePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = String.valueOf(position);

                if (position == 0) {
                    FragmentSignUp_Page1.left_btn.setVisibility(View.GONE);
                    FragmentSignUp_Page1.right_btn.setVisibility(View.VISIBLE);
                } else if (position == FragmentSignUp_Page1.cardsCount - 1) {
                    FragmentSignUp_Page1.right_btn.setVisibility(View.GONE);
                    FragmentSignUp_Page1.left_btn.setVisibility(View.VISIBLE);
                } else {
                    FragmentSignUp_Page1.right_btn.setVisibility(View.VISIBLE);
                    FragmentSignUp_Page1.left_btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void cityClickListener() {
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                hideKeyboard(city);
                city.showDropDown();
            }
        });

        city.setTextIsSelectable(true);
        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (!citiesData.get(position).getCityID().isEmpty()) {
                        CityID = citiesData.get(position).getCityID();
                    }

//                    mEditor.putString("CityID", CityID).commit();
//                    mEditor.putString("City", citiesDataArrayList.get(position).getTitle()).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                if (cardNumberString.length() == 6) {
                    //  getUserDetails();
                }

            }
        });

        cardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                getUserDetails();
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


        phone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().startsWith("+966")){
                    phone.setText("+966");
                }
            }
        });
    }

    private void getUserDetails() {
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.getUserDetail + "?CardNumber=" + cardNumberString + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language, getActivity(), body, headers, new ServerCallback() {
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
                            String UserID = user_info.getString("UserID");
                            String AuthToken = user_info.getString("AuthToken");
                            String CardID = user_info.getString("CardID");
                            String CardNumber = user_info.getString("CardNumber");
                            String Cityid = user_info.getString("CityID");
                            String CityTitle = user_info.getString("CityTitle");
                            String Email = user_info.getString("Email");
                            String FirstName = user_info.getString("FirstName");
                            String Image = user_info.getString("Image");
                            String LastName = user_info.getString("LastName");
                            String MiddleName = user_info.getString("MiddleName");
                            String Mobile = user_info.getString("Mobile");

                            if (!CardID.equals(cardsData.get(Integer.parseInt(cardPosition)).getCardID())) {
                                for (int i = 0; i < cardsData.size(); i++) {
                                    if (CardID.equals(cardsData.get(i).getCardID())) {
                                        cardTypePager.setCurrentItem(i);
                                    }
                                }
                            }

                            CardUserId = UserID;
                            CityID = String.valueOf(Cityid);
                            firstName.setText(FirstName);
                            firstName.setEnabled(false);
                            middleName.setText(MiddleName);
                            middleName.setEnabled(false);
                            lastName.setText(LastName);
                            lastName.setEnabled(false);
                            email.setText(Email);
                            email.setEnabled(false);
                            phone.setText(Mobile);
                            phone.setEnabled(false);
                            cardNumber.setText(CardNumber);

                            mEditor.putString("USER_ID", CardUserId).commit();


                        } else {
                            firstName.setText("");
                            firstName.setEnabled(true);
                            middleName.setText("");
                            middleName.setEnabled(true);
                            lastName.setText("");
                            lastName.setEnabled(true);
                            email.setText("");
                            email.setEnabled(true);
                            phone.setText("");
                            phone.setEnabled(true);

                            String message = jsonObject.getString("message");
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

    @Override
    public void onClick(View view) {

        /*if (view == left_btn){
            int tab = cardTypePager.getCurrentItem();
            if (tab > 0) {
                tab--;
                cardTypePager.setCurrentItem(tab);
            } else if (tab == 0) {
                cardTypePager.setCurrentItem(tab);
            }
        }else if (view == right_btn){
            int tab = cardTypePager.getCurrentItem();
            tab++;
            cardTypePager.setCurrentItem(tab);
        }*/
    }


}
