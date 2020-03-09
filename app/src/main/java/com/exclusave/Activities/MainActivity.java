package com.exclusave.Activities;

import android.content.ComponentCallbacks2;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.DialogInterface;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.android.volley.Request;
import com.fxn.pix.Options;
import com.makeramen.roundedimageview.RoundedImageView;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.Fragments.NotificationsFragment;
import com.exclusave.models.BranchesList_Data;
import com.exclusave.models.CitiesData;
import com.exclusave.models.InterestsData;
import com.exclusave.models.StoresDataForDropDown;
import com.exclusave.BuildConfig;
import com.soundcloud.android.crop.Crop;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.exclusave.Fragments.Branches_Fragment;
import com.exclusave.Fragments.MyAccountFragment;
import com.exclusave.Fragments.Select_Product_Fragment;
import com.exclusave.R;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, ComponentCallbacks2 {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    ArrayList<String> returnValue = new ArrayList<>();
    Uri myUri;
    Bitmap bitmap;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    ImageView nav_cover;

    String StoreId;
    String BranchStoreId;
    NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrarLayout;
    BottomNavigationView bottomNavigationView;
    static ImageView filter, cam_image, ProfilePic, filterheader;
    static TextView fragment_title;
    TextView reg_btn, login_btn;
    TextView change_lang;
    FrameLayout frameLayout;
    Select_Product_Fragment select_pro_frag = new Select_Product_Fragment();
    Branches_Fragment branches_fragment = new Branches_Fragment();
    MyAccountFragment myAccountFragment = new MyAccountFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();
    String id;
    CircleImageView completeprofilePic;
    LinearLayout linearLayout_buttons;
    RelativeLayout relativeLayout_profile_image;
    Fragment active = select_pro_frag;
    Options options;
    int reqCode = 0;
    EditText dob, company_name, position;
    ArrayList<BranchesList_Data> branches_list = new ArrayList<>();
    ArrayList<String> branchesNames = new ArrayList<>();
    ArrayList<CitiesData> citiesData = new ArrayList<>();
    ArrayList<String> citiesNames = new ArrayList<>();
    ArrayList<String> storeName = new ArrayList<>();
    ArrayList<StoresDataForDropDown> storesPagerData = new ArrayList<>();
    AutoCompleteTextView gender, interest;
    String Gender;
    Map<String, String> body = new HashMap<String, String>();
    Map<String, String> saveTransbody = new HashMap<String, String>();
    ArrayList<InterestsData> interestsData = new ArrayList<>();
    ArrayList<String> interestsNames = new ArrayList<>();
    String InterestID;
    static Dialog updatedialog;
    static MaterialProgressBar materialProgressBar;
    TextView uploadid;
    String StoreCoverImage = "", StoreTitle = "", StoreID = "", TransactionID = "", Rating = "1";
    EditText beforeBill;
    EditText afterBill;
    TextView saved;
    Dialog confirmBillDialog;
    Dialog rateYourExpeirenceDialog;
    Dialog complaintDialog;
    Dialog raisacomplain;
    ColorStateList iconsColorStates;
    ColorStateList progressColorStates;
    AutoCompleteTextView select_city, complaintitle;
    ImageView settings;
    int selectedItem;
    Dialog merchantCodeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        Configuration configuration = getApplicationContext().getResources().getConfiguration();

        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            iconsColorStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            Color.parseColor("#FFFFFF"),
                            Color.parseColor(sharedpreferences.getString("FontColor", ""))
                    });

            progressColorStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{
                            Color.parseColor(sharedpreferences.getString("FontColor", "")),
                            Color.parseColor("#FFFFFF")
                    });
        }

        navigationView = findViewById(R.id.nav_view);

        initilizeViews();
        navHeaderView();
//        getStoresApiCall();

        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            getStoresApiCall();
            linearLayout_buttons.setVisibility(View.GONE);
            relativeLayout_profile_image.setVisibility(View.VISIBLE);
            navigationView.inflateMenu(R.menu.navigation_drawer_login_items);
            if (!sharedpreferences.getString("Info", "").equals("Submited")) {
                if (!sharedpreferences.getString("ProfileCompletedPercentage", "").equals("100")) {
                    updateMissingDataPopup();
                }
            }
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            linearLayout_buttons.setVisibility(View.VISIBLE);
            relativeLayout_profile_image.setVisibility(View.GONE);
            navigationView.inflateMenu(R.menu.navigation_drawer_viewusage_items);
        }

        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("CardBackgroundImage", "")).into(nav_cover);
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            nav_cover.setBackground(this.getResources().getDrawable(R.drawable.ic_header_cover));
        }

        mDrarLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrarLayout, R.string.open, R.string.close);
        mDrarLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            bottomNavigationView.setItemIconTintList(iconsColorStates);
        }

        frameLayout = (FrameLayout) findViewById(R.id.fragmentContainer);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_menu:
                        fragment_title.setText("");
                        mDrarLayout.openDrawer(GravityCompat.START);
                        selectedItem = bottomNavigationView.getSelectedItemId();
                        Log.e("selectedItem", " " + selectedItem);
                        return true;

                    case R.id.bottom_location:
                        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
                            filter.setVisibility(View.GONE);
                            filterheader.setVisibility(View.GONE);
                            fragment_title.setText("");
                            setFragment(branches_fragment);
                            active = branches_fragment;
                            selectedItem = bottomNavigationView.getSelectedItemId();
                            Log.e("selectedItem", " " + selectedItem);
                            return true;
                        } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                            Toast.makeText(MainActivity.this, getApplicationContext().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    case R.id.bottom_profile:
                        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
                            filter.setVisibility(View.GONE);
                            filterheader.setVisibility(View.GONE);
                            fragment_title.setText(getApplicationContext().getResources().getString(R.string.myAccount));
                            setFragment(myAccountFragment);
                            active = myAccountFragment;
                            selectedItem = bottomNavigationView.getSelectedItemId();
                            Log.e("selectedItem", " " + selectedItem);
                            return true;
                        } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                            Toast.makeText(MainActivity.this, getApplicationContext().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    case R.id.bottom_bell:
                        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
                            filter.setVisibility(View.GONE);
                            filterheader.setVisibility(View.GONE);
                            setFragment(notificationsFragment);
                            active = notificationsFragment;
                            fragment_title.setText(getApplicationContext().getResources().getString(R.string.inbox));
                            selectedItem = bottomNavigationView.getSelectedItemId();
                            Log.e("selectedItem", " " + selectedItem);
                            return true;
                        } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                            Toast.makeText(MainActivity.this, getApplicationContext().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    case R.id.bottom_centreBtn:
                        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                            Toast.makeText(MainActivity.this, getApplicationContext().getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                            return false;
                        } else if (!sharedpreferences.getString("ProfileCompletedPercentage", "").equals("100")) {
                            Log.e("percent", sharedpreferences.getString("ProfileCompletedPercentage", ""));
                            updateMissingDataPopup();
//                            merchantCodeDialog();
                            return false;
                        } else {
                            merchantCodeDialog();
                            return true;
                        }
                    default:
                        return false;
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, notificationsFragment, "4").hide(notificationsFragment);
        fragmentTransaction.add(R.id.fragmentContainer, myAccountFragment, "3").hide(myAccountFragment);
        fragmentTransaction.add(R.id.fragmentContainer, branches_fragment, "2").hide(branches_fragment);
        fragmentTransaction.add(R.id.fragmentContainer, select_pro_frag, "1");
        fragmentTransaction.commit();


        new Thread(new Runnable() {

            @Override
            public void run() {
                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.nav_drawer_home:
                                filter.setVisibility(View.VISIBLE);
                                filterheader.setVisibility(View.VISIBLE);
                                setFragment(select_pro_frag);
                                active = select_pro_frag;
                                mDrarLayout.closeDrawers();
//                                break;
                                return true;
                            case R.id.nav_drawer_logout:
                                logoutFunction();
//                                break;
                                return true;
                            case R.id.nav_drawer_favorites:
                                startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
                                return true;
                            case R.id.nav_drawer_wishlist:
                                Toast.makeText(MainActivity.this,  getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                                return false;
                            case R.id.nav_drawer_contact_us:
                                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                                return true;
                            case R.id.nav_drawer_contactus:
                                startActivity(new Intent(MainActivity.this, ContactUsActivity.class));
                                return true;
                            case R.id.nav_drawer_exclusavemap:
                                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                                mDrarLayout.closeDrawers();
                                return true;

                            default:
                                return false;
                        }
//                        return true;
                    }
                });
            }

        }).start();
    }


    private void merchantCodeDialog() {
        merchantCodeDialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        merchantCodeDialog.setContentView(R.layout.merchant_code_dialog);
        Button applyCode = merchantCodeDialog.findViewById(R.id.applyCode);
        Button compain = merchantCodeDialog.findViewById(R.id.complain);
        EditText storecode = merchantCodeDialog.findViewById(R.id.storeCode);
        RoundedImageView cardImg = merchantCodeDialog.findViewById(R.id.cardImg);
        TextView userName = merchantCodeDialog.findViewById(R.id.username);
        TextView companyName = merchantCodeDialog.findViewById(R.id.companyName);

        applyCode.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        if(sharedpreferences.getString("IsFamilyMember", "").equals("0")){
            userName.setText(sharedpreferences.getString("FullName", ""));
            companyName.setText(sharedpreferences.getString("CompanyName", ""));
        }else if(sharedpreferences.getString("IsFamilyMember", "").equals("1")){
            userName.setText(sharedpreferences.getString("FamilyMemberFirstName", "") + " " + sharedpreferences.getString("FamilyMemberMiddleName", "") + " " + sharedpreferences.getString("FamilyMemberLastName", ""));
            companyName.setText(sharedpreferences.getString("CompanyName", ""));
        }

        Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("CardImage", "")).into(cardImg);

        merchantCodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        merchantCodeDialog.setCancelable(false);
        merchantCodeDialog.show();
        applyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storecode.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.pleaseEnterStoreCode), Toast.LENGTH_SHORT).show();
                } else {
                    applyStoreCode(storecode.getText().toString());
                }
//                merchantCodeDialog.dismiss();
//                BaseClass.dialog.dismiss();
//                confirmBillDialog();
            }
        });
        compain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
                merchantCodeDialog.dismiss();
                raisacomplaintDialog();
            }
        });

        merchantCodeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                mDrarLayout.closeDrawers();
            }
        });

        merchantCodeDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dialog.cancel();
                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
                    setFragment(select_pro_frag);
                    active = select_pro_frag;
                    filter.setVisibility(View.VISIBLE);
                    filterheader.setVisibility(View.VISIBLE);
                    mDrarLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

    private void confirmBillDialog() {
        confirmBillDialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        confirmBillDialog.setContentView(R.layout.confirm_bill_dialog);
        Button submitBill = confirmBillDialog.findViewById(R.id.submitBill);
        uploadid = confirmBillDialog.findViewById(R.id.uploadid);
        RoundedImageView storeImg = confirmBillDialog.findViewById(R.id.storeImg);
        TextView storeName = confirmBillDialog.findViewById(R.id.storeName);
        beforeBill = confirmBillDialog.findViewById(R.id.billBefore);
        afterBill = confirmBillDialog.findViewById(R.id.billAfter);
        saved = confirmBillDialog.findViewById(R.id.saved);

        beforeBill.setFilters(new InputFilter[]{new BaseClass.DecimalDigitsInputFilter(6, 2)});
        afterBill.setFilters(new InputFilter[]{new BaseClass.DecimalDigitsInputFilter(6, 2)});

        submitBill.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));
//        Button compain = merchantCodeDialog.findViewById(R.id.complain);
        Picasso.get().load(Constants.URL.IMG_URL + StoreCoverImage).into(storeImg);
        storeName.setText(StoreTitle);
        confirmBillDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmBillDialog.setCancelable(false);
        confirmBillDialog.show();

        beforeBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (beforeBill.getText().toString().startsWith(".")) {
                    beforeBill.setError(getResources().getString(R.string.startwithdoterrormessage));
                    beforeBill.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        afterBill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (afterBill.getText().toString().startsWith(".")) {
                    afterBill.setError(getResources().getString(R.string.startwithdoterrormessage));
                    afterBill.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        submitBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                if (beforeBill.getText().toString().isEmpty()) {
                    beforeBill.startAnimation(shake);
                } else if (afterBill.getText().toString().isEmpty()) {
                    afterBill.setAnimation(shake);
                } else {
                    saveTransaction(StoreID, beforeBill.getText().toString(), afterBill.getText().toString());
                    BaseClass.dialog.dismiss();
                }
//                confirmBillDialog.dismiss();
//                rateYourExpeirenceDialog();
            }
        });
        uploadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqCode = 3333;
                options = Options.init()
                        .setRequestCode(100)
                        .setCount(1);
                Log.e("cameraClick", "yes");
                returnValue.clear();
                Pix.start(MainActivity.this,                    //Activity or Fragment Instance
                        options);
            }
        });

        confirmBillDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dialog.cancel();
                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
                    setFragment(select_pro_frag);
                    active = select_pro_frag;
                    filter.setVisibility(View.VISIBLE);
                    filterheader.setVisibility(View.VISIBLE);
                    mDrarLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

    private void rateYourExpeirenceDialog() {
        final String[] title = {" "};
        rateYourExpeirenceDialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        rateYourExpeirenceDialog.setContentView(R.layout.rate_experience_dialog);
        ChipCloud chipCloud = rateYourExpeirenceDialog.findViewById(R.id.chipcloud);
        Button ratingDone = rateYourExpeirenceDialog.findViewById(R.id.ratingDone);
        Button skipRating = rateYourExpeirenceDialog.findViewById(R.id.skipRating);
        Button complain = rateYourExpeirenceDialog.findViewById(R.id.complain);
        RatingBar ratingBar = rateYourExpeirenceDialog.findViewById(R.id.ratingBar);
        EditText comment = rateYourExpeirenceDialog.findViewById(R.id.comment);

        ratingDone.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        rateYourExpeirenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rateYourExpeirenceDialog.setCancelable(false);
        rateYourExpeirenceDialog.show();

        String list[] = {"EXCELLENT SERVICS", "RECOMMEND THIS PLACE", "ABOVE AND BEYOND", "OTHER"};

        chipCloud.addChips(list);
        chipCloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                title[0] = list[index];
                Log.e("SelectedTitle", title[0]);
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Rating = String.valueOf(rating);
            }
        });

        ratingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate(comment.getText().toString(), title[0]);
                BaseClass.dialog.dismiss();
//                rateYourExpeirenceDialog.dismiss();
            }
        });
        skipRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
                rateYourExpeirenceDialog.dismiss();
//                Dialog regsuccess = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                regsuccess.setCancelable(false);
//                regsuccess.setContentView(R.layout.redeem_successfull_dialog);
//                regsuccess.show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        regsuccess.dismiss();
//                        BaseClass.dialog.dismiss();
////                        rateYourExpeirenceDialog.dismiss();
//                        bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                        setFragment(select_pro_frag);
//                        active = select_pro_frag;
//                        mDrarLayout.closeDrawers();
//                    }
//                }, 1000);
            }
        });
        complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateYourExpeirenceDialog.dismiss();
                complaintDialog();
//                DynamicToast.makeSuccess(LoginActivity.this, "Mmm, working on it!", 3).show();
            }
        });

        rateYourExpeirenceDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dialog.cancel();
                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
                    setFragment(select_pro_frag);
                    active = select_pro_frag;
                    filter.setVisibility(View.VISIBLE);
                    filterheader.setVisibility(View.VISIBLE);
                    mDrarLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

    private void raisacomplaintDialog() {
        final String[] title = {""};
        raisacomplain = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        raisacomplain.setContentView(R.layout.raisacomlaint_dialog);
        complaintitle = raisacomplain.findViewById(R.id.complain_title);
        select_city = raisacomplain.findViewById(R.id.city_selection);
        ChipCloud complain_cloud = raisacomplain.findViewById(R.id.complain_chipcloud);
        Button complainsubmit = raisacomplain.findViewById(R.id.complainsubmit);
        EditText complain_comment = raisacomplain.findViewById(R.id.complain_comment);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, storeName);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        complaintitle.setAdapter(adapter);

        selectstoresClickListener();
        selectbranchesClickListener();

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, branchesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        select_city.setAdapter(adapter1);

        complainsubmit.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        raisacomplain.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        raisacomplain.setCancelable(false);
        raisacomplain.show();

        String list[] = {"VERY BAD SERVICE", "NOT RECOMMENDED", "HIGH PRICES", "WORST SERVICE"};
        complain_cloud.addChips(list);
        complain_cloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                title[0] = list[index];
                Log.e("SelectedTitle", title[0]);
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

        complainsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complaintitle.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.selectstore), Toast.LENGTH_LONG).show();
                }
//                else if (select_city.getText().toString().isEmpty()) {
//                    Toast.makeText(MainActivity.this, getResources().getString(R.string.selectbranch), Toast.LENGTH_LONG).show();
//                }
                else if (complain_comment.getText().toString().equals("") && title[0].equals("")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.riseAComplaint), Toast.LENGTH_LONG).show();
                } else {
                    submitComplainDirect(complain_comment.getText().toString(), title[0]);
                }
                BaseClass.dialog.dismiss();
            }
        });

        raisacomplain.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                raisacomplain.dismiss();
                bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
                setFragment(select_pro_frag);
                active = select_pro_frag;
                mDrarLayout.closeDrawers();
            }
        });

        raisacomplain.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dialog.cancel();
                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
                    setFragment(select_pro_frag);
                    active = select_pro_frag;
                    filter.setVisibility(View.VISIBLE);
                    filterheader.setVisibility(View.VISIBLE);
                    mDrarLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

    private void submitComplainDirect(String comment, String reason) {
        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        BaseClass.showCustomLoader(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("RatingType", "Complain");
        body.put("StoreID", BranchStoreId);
        body.put("Reason", reason);
        body.put("Comment", comment);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));

        Log.e("directComplainBody", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("DirectComplainResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            raisacomplain.dismiss();

                            Toast.makeText(MainActivity.this, getResources().getString(R.string.complainsuccess), Toast.LENGTH_LONG).show();

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectstoresClickListener() {
        complaintitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                hideKeyboard(complaintitle);
                complaintitle.showDropDown();
            }
        });

        complaintitle.setTextIsSelectable(true);
        complaintitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (!storesPagerData.get(position).getStoreID().isEmpty()) {
                        StoreId = storesPagerData.get(position).getStoreID();
                        BranchStoreId = storesPagerData.get(position).getStoreID();
                        branchesNames.clear();
                        branchesListApiCall();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void selectbranchesClickListener() {
        select_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                hideKeyboard(select_city);
                select_city.showDropDown();
            }
        });

        select_city.setTextIsSelectable(true);
        select_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    if (!branches_list.get(position).getStoreID().isEmpty()) {
                        BranchStoreId = branches_list.get(position).getStoreID();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isAcceptingText()){
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    private void complaintDialog() {
        final String[] title = {" "};
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            tags.add("Tag" + i);
        }
        complaintDialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        complaintDialog.setContentView(R.layout.comlaint_dialog);
        ChipCloud cloud = complaintDialog.findViewById(R.id.chipcloud);
        Button complainDone = complaintDialog.findViewById(R.id.complainDone);
        EditText comment = complaintDialog.findViewById(R.id.comment);

        complainDone.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        complaintDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        complaintDialog.setCancelable(false);
        complaintDialog.show();

        String list[] = {"VERY BAD SERVICE", "NOT RECOMMENDED", "HIGH PRICES", "WORST SERVICE"};

        cloud.addChips(list);

        cloud.setChipListener(new ChipListener() {
            @Override
            public void chipSelected(int index) {
                title[0] = list[index];
                Log.e("SelectedTitle", title[0]);
            }

            @Override
            public void chipDeselected(int index) {

            }
        });

        complainDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complain(comment.getText().toString(), title[0]);
                BaseClass.dialog.dismiss();
            }
        });

        complaintDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        event.getAction() == KeyEvent.ACTION_UP &&
                        !event.isCanceled()) {
                    dialog.cancel();
                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
                    setFragment(select_pro_frag);
                    active = select_pro_frag;
                    mDrarLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

    private void branchesListApiCall() {
        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "stores?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&CardID=1" + "&OrderBy=DESC" + "&Language=" + language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.BRANCHES + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OrderBy=DESC" + "&ParentID=" + StoreId;
            Log.e("URL", URL);
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetBranchesResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i = 0; i < storearray.length(); i++) {

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryBackgroundImage = object.getString("CategoryBackgroundImage");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String ContactPersonName = object.getString("ContactPersonName");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String Description = object.getString("Description");
                                String Distance = object.getString("Distance");
                                String DistrictID = object.getString("DistrictID");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Email = object.getString("Email");
                                String FacebookLink = object.getString("FacebookLink");
                                String FirstName = object.getString("FirstName");
                                String GoldImage = object.getString("GoldImage");
                                String Hide = object.getString("Hide");
                                String Image = object.getString("Image");
                                String InstagramLink = object.getString("InstagramLink");
                                String IsActive = object.getString("IsActive");
                                String IsSponsored = object.getString("IsSponsored");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String OrangeImage = object.getString("OrangeImage");
                                String ParentID = object.getString("ParentID");
                                String SortOrder = object.getString("SortOrder");
                                String SponsoredSortOrder = object.getString("SponsoredSortOrder");
                                String SponsorshipExpiresAt = object.getString("SponsorshipExpiresAt");
                                String StoreCode = object.getString("StoreCode");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String StoreType = object.getString("StoreType");
                                String SubCategoryID = object.getString("SubCategoryID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String TwitterLink = object.getString("TwitterLink");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String UserID = object.getString("UserID");
                                String WebsiteLink = object.getString("WebsiteLink");

                                branchesNames.add(Title);
                                branches_list.add(new BranchesList_Data(Address, CategoryID, CategoryTitle, CityID, CityTitle, CoverImage, CreatedAt, CreatedBy, DistrictID,
                                        DistrictTitle, Email, FacebookLink, FirstName, Hide, Image, InstagramLink, IsActive, LastName, Latitude, Longitude,
                                        MiddleName, Mobile, ParentID, SortOrder, StoreID, StoreTextID, SystemLanguageID, Title, TwitterLink, UpdatedAt, UpdatedBy, WebsiteLink));

                            }

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navHeaderView() {

        View view = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        reg_btn = view.findViewById(R.id.nav_btn_register);
        change_lang = view.findViewById(R.id.change_lan);
        login_btn = view.findViewById(R.id.nav_btn_login);
        linearLayout_buttons = view.findViewById(R.id.nav_linear_buttons);
        relativeLayout_profile_image = view.findViewById(R.id.nav_relative_image);
        cam_image = view.findViewById(R.id.logoBtn);
        ProfilePic = view.findViewById(R.id.profilePic);
        materialProgressBar = view.findViewById(R.id.materialProgress);
        nav_cover = view.findViewById(R.id.nav_cover);
        settings = view.findViewById(R.id.settings);
        settings.setOnClickListener(this);

        Configuration configuration;
        configuration = getResources().getConfiguration();
        if (configuration.getLayoutDirection() == 1) {
            change_lang.setText("English");
        } else if (configuration.getLayoutDirection() == 0) {
            change_lang.setText("عربى");
        }

        materialProgressBar.setProgress(Integer.parseInt(sharedpreferences.getString("ProfileCompletedPercentage", "0")));
        materialProgressBar.setSupportProgressTintList(progressColorStates);
        if (sharedpreferences.getString("IsFamilyMember", "").equals("1")) {
            Log.e("image", "family" + "           " + Constants.URL.IMG_URL + sharedpreferences.getString("FamilyMemberImage", ""));
            Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("FamilyMemberImage", "")).error(R.drawable.user).into(ProfilePic);
        } else {
            Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).error(R.drawable.user).into(ProfilePic);
            Log.e("image", "personal" + "           " + Constants.URL.IMG_URL + sharedpreferences.getString("Image", ""));
        }

        reg_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        change_lang.setOnClickListener(this);
        cam_image.setOnClickListener(this);

        if (sharedpreferences.getString("IsFamilyMember", "").equals("1")) {
            if (!sharedpreferences.getString("Image", "").equals("")) {
                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("FamilyMemberImage", "")).error(R.drawable.user).into(ProfilePic);
            }
        } else if (sharedpreferences.getString("IsFamilyMember", "").equals("0")) {
            if (!sharedpreferences.getString("Image", "").equals("")) {
                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).error(R.drawable.user).into(ProfilePic);
            }
        }

    }

    private void initilizeViews() {
        bottomNavigationView = findViewById(R.id.navigation);
        filter = findViewById(R.id.filter);
        filterheader = findViewById(R.id.filterheader);
        fragment_title = findViewById(R.id.fragmenttitle);

        filterheader.setOnClickListener(this);
        filter.setOnClickListener(this);

        if(sharedpreferences.getString("isLoggedIn", "").equals("1")){
            filter.setVisibility(View.VISIBLE);
            filterheader.setVisibility(View.VISIBLE);
        }else if(sharedpreferences.getString("isLoggedIn", "").equals("0")){
            filter.setVisibility(View.GONE);
            filterheader.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.areYouSureYouWantToExit))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(active).show(fragment).commit();
    }

    private void updateMissingDataPopup() {
        updatedialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        updatedialog.setContentView(R.layout.update_missingdata_dialog);
        Button skip = updatedialog.findViewById(R.id.skip_btn);
        Button submit = updatedialog.findViewById(R.id.submitBtn);
        dob = updatedialog.findViewById(R.id.date_of_birth);
        company_name = updatedialog.findViewById(R.id.companyName);
        position = updatedialog.findViewById(R.id.position);
        completeprofilePic = updatedialog.findViewById(R.id.completeprofilePic);
        gender = updatedialog.findViewById(R.id.gender);
        interest = updatedialog.findViewById(R.id.interestType);

        submit.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));
        // getCities();

        String GENDER[] = {getResources().getString(R.string.male), getResources().getString(R.string.female)};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, GENDER);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        gender.setAdapter(adapter);

        cityClickListener();
        getInterests();
        interestClickListener();
        ImageView imageuploadbtn = updatedialog.findViewById(R.id.profilePicBtn);
        updatedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updatedialog.show();

        dob.setText(sharedpreferences.getString("DateOfBirth", ""));
        company_name.setText(sharedpreferences.getString("CompanyName", ""));
        position.setText(sharedpreferences.getString("Position", ""));
        if (!sharedpreferences.getString("Gender", "").equals("")) {
            gender.setHint(sharedpreferences.getString("Gender", ""));
            gender.setHintTextColor(getResources().getColor(R.color.black));
        }
        if (!sharedpreferences.getString("InterestTitle", "").equals("")) {
            interest.setHint(sharedpreferences.getString("InterestTitle", ""));
            interest.setHintTextColor(getResources().getColor(R.color.black));
        }

        InterestID = sharedpreferences.getString("Interest", "");

//        if (!sharedpreferences.getString("Image", "").equals("")) {
            Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).error(R.drawable.user).into(completeprofilePic);
//        }


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        imageuploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reqCode = 1111;
                options = Options.init()
                        .setRequestCode(100)
                        .setCount(1);
                Log.e("cameraClick", "yes");
                returnValue.clear();
                Pix.start(MainActivity.this,                    //Activity or Fragment Instance
                        options);
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putString("Info", "Submited").commit();
                completeProfilleApiCall();
                // DynamicToast.makeSuccess(MainActivity.this, "Mmm, working on it!", 3).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.filter:
                if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.PleaseLogin), Toast.LENGTH_LONG).show();
                    break;
                } else {
                    Intent intent = new Intent(MainActivity.this, SearchMarchents.class);
                    startActivity(intent);
                    break;
                }
            case R.id.nav_btn_register:
                startActivity(new Intent(MainActivity.this, SignUpTesting.class));
                mDrarLayout.closeDrawers();
//                finish();
                break;
            case R.id.nav_btn_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                mDrarLayout.closeDrawers();
//                finish();
                break;
            case R.id.logoBtn:
                Button btn_no, btn_yes;
                TextView dialog_alert_text;

                Dialog dialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                dialog.setContentView(R.layout.areyousureloader);
                // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                btn_no = dialog.findViewById(R.id.btn_no);
                btn_yes = dialog.findViewById(R.id.btn_yes);

                dialog_alert_text = dialog.findViewById(R.id.dialog_alert_text);

                btn_yes.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

                dialog_alert_text.setText(getResources().getString(R.string.change_profile_string));

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sharedpreferences.getString("FamilyMemberFirstName", "").equals("") || sharedpreferences.getString("FamilyMemberGender", "").equals("")
                                || sharedpreferences.getString("FamilyMemberMobile", "").equals("") || sharedpreferences.getString("FamilyMemberImage", "").equals("")) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.pleaseCompleteFamily), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                            changeAccountUser();
                        }

                    }
                });
                break;
            case R.id.change_lan:
                Configuration configuration;
                configuration = getResources().getConfiguration();
                if (configuration.getLayoutDirection() == 1) {
                    change_lang.setText("English");
                } else if (configuration.getLayoutDirection() == 0) {
                    change_lang.setText("عربى");
                }
                if (change_lang.getText().toString().equals("عربى")) {
                    updateLanguageApiCall("English", "AR", "ar");
                } else if (change_lang.getText().toString().equals("English")) {
                    updateLanguageApiCall("عربى", "EN", "en");
                }
                break;
            case R.id.filterheader:
                if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
                    startActivity(new Intent(MainActivity.this, Filters_Activity.class));
                    break;
                } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.settings:
                if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
                    startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
                    break;
                } else if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.PleaseLogin), Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }*/

        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            for (int a = 0; a < returnValue.size(); a++) {
                File imgFile = new File(returnValue.get(a));
//                Uri myUri = Uri.parse(returnValue.get(a));
                myUri = Uri.fromFile(new File(returnValue.get(a)));
                Log.e("getdata", "---->" + myUri);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Log.e("getdata", "---->" + myBitmap);
//                    ProfilePic.setImageBitmap(myBitmap);
                }
                beginCrop(myUri);

            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(MainActivity.this, options);
                } else {
                    Toast.makeText(MainActivity.this, "Approve permissions to open Camera", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            if (reqCode == 2222) {
                ProfilePic.setImageBitmap(null);
                ProfilePic.setImageURI(Crop.getOutput(result));
            }
            if (reqCode == 1111) {
                completeprofilePic.setImageBitmap(null);
                completeprofilePic.setImageURI(Crop.getOutput(result));
            }
            if (reqCode == 3333) {
                uploadid.setText("UPLOADED SUCCESSFULLY");
            }
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
                bitmapArrayList.add(bitmap);
                Log.e("chekingBit", bitmap.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void datePicker() {
        //  String currentDate = DateFormat.getDateInstance().format(new Date().getDay());

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dpd.setYearRange(1900, now.getWeekYear() - 18);
        }
        // dpd.setMaxDate(now);
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        dob.setText(date);
    }

    private void logoutFunction() {

        Button btn_no, btn_yes;
        TextView dialog_alert_text;

        Dialog dialog = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        dialog.setContentView(R.layout.areyousureloader);
        // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        btn_no = dialog.findViewById(R.id.btn_no);
        btn_yes = dialog.findViewById(R.id.btn_yes);
        dialog_alert_text = dialog.findViewById(R.id.dialog_alert_text);

        btn_yes.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedpreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedpreferences.getString("PackageColorLower", "")), Color.parseColor(sharedpreferences.getString("PackageColorUpper", ""))}, 8));

        dialog_alert_text.setText(getResources().getString(R.string.logout_string));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String language = BaseClass.checkLanguageFunction(MainActivity.this);

                BaseClass.showCustomLoader(MainActivity.this);

                Map<String, String> body = new HashMap<String, String>();

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

                ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LOGOUT + sharedpreferences.getString("UserID", " ") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language, MainActivity.this, body, headers, new ServerCallback() {
                    @Override
                    public void onSuccess(String result, String ERROR) {

                        if (ERROR.isEmpty()) {
                            Log.e("LogoutResponse", result);
                            try {

                                JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                String message = jsonObject.getString("message");
                                int status = jsonObject.getInt("status");
                                if (status == 200) {
                                    mEditor.clear();
                                    mEditor.putString("LOGIN_STATUS", "NO").commit();
                                    mEditor.commit();
                                    BaseClass.dialog.dismiss();

//                                    mEditor.clear().commit();
                                    Intent i = getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
//                                    DynamicToast.makeSuccess(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    BaseClass.dialog.dismiss();
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                BaseClass.dialog.dismiss();
                                e.printStackTrace();
                            }
                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getCities() {

        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CITIES + "?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OS=" + Build.VERSION.RELEASE + "&Language=" + language, MainActivity.this, body, headers, new ServerCallback() {
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

/*                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, GENDER);
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            city.setAdapter(adapter);*/

                        } else {
                            Toast.makeText(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cityClickListener() {
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("drawableClicked", "Checked");
                BaseClass.hideKeyboard(gender, MainActivity.this);
                gender.showDropDown();
            }
        });

        gender.setTextIsSelectable(true);
        gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {

//                    mEditor.putString("CityID", CityID).commit();
//                    mEditor.putString("City", citiesDataArrayList.get(position).getTitle()).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void completeProfilleApiCall() {

        String language = BaseClass.checkLanguageFunction(MainActivity.this);

        BaseClass.showCustomLoader(MainActivity.this);

        if (bitmapArrayList.size() > 0) {
            imgToStringFunction(bitmap, 1111);
        }
        body.put("DateOfBirth", dob.getText().toString());
        if (!InterestID.equals("")) {
            body.put("Interest", InterestID);
        }
        if (!company_name.getText().toString().isEmpty()) {
            body.put("CompanyName", company_name.getText().toString());
        }
        if (!position.getText().toString().isEmpty()) {
            body.put("Position", position.getText().toString());
        }
        if (gender.getHint().toString().equals(this.getResources().getString(R.string.male)) || gender.getHint().toString().equals(this.getResources().getString(R.string.female))) {
            body.put("Gender", gender.getHint().toString());
        }
        if (gender.getText().toString().equals(this.getResources().getString(R.string.male)) || gender.getText().toString().equals(this.getResources().getString(R.string.female))) {
            body.put("Gender", gender.getText().toString());
        }
        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("UpdateProfileResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONObject user_info = jsonObject.getJSONObject("user_info");

                            String Cityid = user_info.getString("CityID");
                            String CityTitle = user_info.getString("CityTitle");
                            String Email = user_info.getString("Email");
                            String FirstName = user_info.getString("FirstName");
                            String Image = user_info.getString("Image");
                            String LastName = user_info.getString("LastName");
                            String MiddleName = user_info.getString("MiddleName");
                            String Mobile = user_info.getString("Mobile");
                            String DateOfBirth = user_info.getString("DateOfBirth");
                            String Interest = user_info.getString("Interest");
                            String CompanyName = user_info.getString("CompanyName");
                            String Position = user_info.getString("Position");
                            String Gender = user_info.getString("Gender");
                            String ProfileCompletedPercentage = user_info.getString("ProfileCompletedPercentage");
                            String InterestTitle = user_info.getString("InterestTitle");
                            String CardID = user_info.getString("CardID");
                            String CouponID = user_info.getString("CouponID");
                            String CardBackgroundImage = user_info.getString("CardBackgroundImage");
                            String CardImage = user_info.getString("CardImage");
                            String FamilyMemberDob = user_info.getString("FamilyMemberDob");
                            String FamilyMemberFirstName = user_info.getString("FamilyMemberFirstName");
                            String FamilyMemberGender = user_info.getString("FamilyMemberGender");
                            String FamilyMemberLastName = user_info.getString("FamilyMemberLastName");
                            String FamilyMemberMiddleName = user_info.getString("FamilyMemberMiddleName");
                            String FamilyMemberMobile = user_info.getString("FamilyMemberMobile");
                            String IDImage = user_info.getString("IDImage");
                            String PackageColorLower = user_info.getString("PackageColorLower");
                            String PackageColorUpper = user_info.getString("PackageColorUpper");
                            String FamilyMemberImage = user_info.getString("FamilyMemberImage");
                            String IsFamilyMember = user_info.getString("IsFamilyMember");
                            String TotalSaving = user_info.getString("TotalSaving");
                            String FontColor = user_info.getString("FontColor");

                            mEditor.putString("FullName", FirstName + " " + MiddleName + " " + LastName);
                            mEditor.putString("FirstName", FirstName);
                            mEditor.putString("MiddleName", MiddleName);
                            mEditor.putString("LastName", LastName);
                            mEditor.putString("Email", Email);
                            mEditor.putString("Image", Image);
                            mEditor.putString("Mobile", Mobile);
                            mEditor.putString("CityTitle", CityTitle);
                            mEditor.putString("DateOfBirth", DateOfBirth);
                            mEditor.putString("Interest", Interest);
                            mEditor.putString("CompanyName", CompanyName);
                            mEditor.putString("Position", Position);
                            mEditor.putString("CityID", Cityid);
                            mEditor.putString("Gender", Gender);
                            mEditor.putString("ProfileCompletedPercentage", ProfileCompletedPercentage);
                            mEditor.putString("InterestTitle", InterestTitle);
                            mEditor.putString("CardID", CardID);
                            mEditor.putString("CouponID", CouponID);
                            mEditor.putString("CardImage", CardImage);
                            mEditor.putString("CardBackgroundImage", CardBackgroundImage);
                            mEditor.putString("FamilyMemberDob", FamilyMemberDob);
                            mEditor.putString("FamilyMemberFirstName", FamilyMemberFirstName);
                            mEditor.putString("FamilyMemberGender", FamilyMemberGender);
                            mEditor.putString("FamilyMemberLastName", FamilyMemberLastName);
                            mEditor.putString("FamilyMemberMiddleName", FamilyMemberMiddleName);
                            mEditor.putString("FamilyMemberMobile", FamilyMemberMobile);
                            mEditor.putString("IDImage", IDImage);
                            mEditor.putString("PackageColorLower", PackageColorLower);
                            mEditor.putString("PackageColorUpper", PackageColorUpper);
                            mEditor.putString("FamilyMemberImage", FamilyMemberImage);
                            mEditor.putString("IsFamilyMember", IsFamilyMember);
                            mEditor.putString("TotalSaving", TotalSaving);
                            mEditor.putString("FontColor", FontColor);
                            mEditor.commit();

                            materialProgressBar.setProgress(Integer.parseInt(sharedpreferences.getString("ProfileCompletedPercentage", "")));
                            materialProgressBar.setSupportProgressTintList(progressColorStates);

                            if (sharedpreferences.getString("IsFamilyMember", "").equals("1")) {
                                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("FamilyMemberImage", "")).error(R.drawable.user).into(ProfilePic);
                            } else {
                                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).error(R.drawable.user).into(ProfilePic);
                            }

                            updatedialog.dismiss();

                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void imgToStringFunction(Bitmap bitmap, int requestcoDE) {

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
                if (requestcoDE == 3333) {
                    saveTransbody.put("Receipt", Base64.encodeToString(data, Base64.DEFAULT));
                } else if (requestcoDE == 1111) {
                    body.put("Image", Base64.encodeToString(data, Base64.DEFAULT));
                }
            }
        }
    }

    private void getInterests() {
        String language = BaseClass.checkLanguageFunction(MainActivity.this);

        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CATEGORIES + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetInterestsResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray citiesArray = jsonObject.getJSONArray("categories");
                            for (int i = 0; i < citiesArray.length(); i++) {
                                JSONObject categoriesObj = citiesArray.getJSONObject(i);
//
                                String BackgroundImage = categoriesObj.getString("BackgroundImage");
                                String CategoryID = categoriesObj.getString("CategoryID");
                                String Image = categoriesObj.getString("Image");
                                String Title = categoriesObj.getString("Title");
                                String SortOrder = categoriesObj.getString("SortOrder");
                                String GoldImage = categoriesObj.getString("GoldImage");
                                interestsNames.add(Title);
                                interestsData.add(new InterestsData(BackgroundImage, CategoryID, Image, Title, SortOrder, GoldImage, "no"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, interestsNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            interest.setAdapter(adapter);

                        } else {
                            Toast.makeText(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void interestClickListener() {
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseClass.hideKeyboard(interest, MainActivity.this);
                interest.showDropDown();
            }
        });

        interest.setTextIsSelectable(true);
        interest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try {
                    InterestID = interestsData.get(position).getCategoryID();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedpreferences.getString("IsFamilyMember", "").equals("1")) {
            if (!sharedpreferences.getString("Image", "").equals("")) {
                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("FamilyMemberImage", "")).error(R.drawable.user).into(ProfilePic);
            }
        } else if (sharedpreferences.getString("IsFamilyMember", "").equals("0")) {
            if (!sharedpreferences.getString("Image", "").equals("")) {
                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).error(R.drawable.user).into(ProfilePic);
            }
        }
    }

    private void getStoresApiCall() {
        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
//            URL = Constants.URL.BASE_URL + "store?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Langujage=" + language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.STORE + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language;
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetStoresResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONArray storearray = jsonObject.getJSONArray("stores");

                            for (int i = 0; i < storearray.length(); i++) {

                                JSONObject object = storearray.getJSONObject(i);

                                String Address = object.getString("Address");
                                String CategoryID = object.getString("CategoryID");
                                String CategoryTitle = object.getString("CategoryTitle");
                                String CityID = object.getString("CityID");
                                String CityTitle = object.getString("CityTitle");
                                String CoverImage = object.getString("CoverImage");
                                String CreatedAt = object.getString("CreatedAt");
                                String CreatedBy = object.getString("CreatedBy");
                                String Distance = object.getString("Distance");
                                String DistrictID = object.getString("DistrictID");
                                String DistrictTitle = object.getString("DistrictTitle");
                                String Hide = object.getString("Hide");
                                String Email = object.getString("Email");
                                String FirstName = object.getString("FirstName");
                                String Image = object.getString("Image");
                                String IsActive = object.getString("IsActive");
                                String LastName = object.getString("LastName");
                                String Latitude = object.getString("Latitude");
                                String Longitude = object.getString("Longitude");
                                String MiddleName = object.getString("MiddleName");
                                String Mobile = object.getString("Mobile");
                                String ParentID = object.getString("ParentID");
                                String PromotionID = object.getString("PromotionID");
                                String SortOrder = object.getString("SortOrder");
                                String StoreID = object.getString("StoreID");
                                String StoreTextID = object.getString("StoreTextID");
                                String SystemLanguageID = object.getString("SystemLanguageID");
                                String Title = object.getString("Title");
                                String UpdatedAt = object.getString("UpdatedAt");
                                String UpdatedBy = object.getString("UpdatedBy");
                                String CategoryImage = object.getString("CategoryImage");
                                String UserID = object.getString("UserID");

                                storeName.add(Title);
                                storesPagerData.add(new StoresDataForDropDown(StoreID, Title));
                                mEditor.putString("StoreID", StoreID).commit();
                            }

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void applyStoreCode(String storecode) {
        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        BaseClass.showCustomLoader(MainActivity.this);

        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("StoreCode", storecode);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.APPLYCODE, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("StoreCodeResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            merchantCodeDialog.dismiss();

                            JSONObject jsonObject1 = jsonObject.getJSONObject("store");
                            String Address = jsonObject1.getString("Address");
                            String CategoryID = jsonObject1.getString("CategoryID");
                            String CategoryTitle = jsonObject1.getString("CategoryTitle");
                            String CityID = jsonObject1.getString("CityID");
                            String CityTitle = jsonObject1.getString("CityTitle");
                            StoreCoverImage = jsonObject1.getString("CoverImage");
                            String CreatedAt = jsonObject1.getString("CreatedAt");
                            String CreatedBy = jsonObject1.getString("CreatedBy");
                            String DistrictID = jsonObject1.getString("DistrictID");
                            String Hide = jsonObject1.getString("Hide");
                            String Image = jsonObject1.getString("Image");
                            String IsActive = jsonObject1.getString("IsActive");
                            String Latitude = jsonObject1.getString("Latitude");
                            String Longitude = jsonObject1.getString("Longitude");
                            String SortOrder = jsonObject1.getString("SortOrder");
                            StoreID = jsonObject1.getString("StoreID");
                            String StoreTextID = jsonObject1.getString("StoreTextID");
                            String SystemLanguageID = jsonObject1.getString("SystemLanguageID");
                            StoreTitle = jsonObject1.getString("Title");
                            String UpdatedAt = jsonObject1.getString("UpdatedAt");
                            String UpdatedBy = jsonObject1.getString("UpdatedBy");

                            confirmBillDialog();


                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
//                            merchantCodeDialog.dismiss();
                            bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
                            setFragment(select_pro_frag);
                            active = select_pro_frag;
                            mDrarLayout.closeDrawers();
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void calculate() {
        if (beforeBill.getText().toString().isEmpty()) {
            saved.setText("= " + afterBill.getText().toString() + "SAR");
        } else if (afterBill.getText().toString().isEmpty()) {
            saved.setText("= " + beforeBill.getText().toString() + "SAR");
        } else if (Float.parseFloat(beforeBill.getText().toString()) < Float.parseFloat(afterBill.getText().toString())) {
            saved.setText("= " + (Float.parseFloat(beforeBill.getText().toString()) - Float.parseFloat(afterBill.getText().toString())) + " SAR");
            saved.setTextColor(Color.RED);
        } else {
            saved.setText("= " + (Float.parseFloat(beforeBill.getText().toString()) - Float.parseFloat(afterBill.getText().toString())) + " SAR");
            saved.setTextColor(getResources().getColor(R.color.green));
        }

    }

    private void saveTransaction(String storeID, String billBefore, String billAfter) {

        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

//        BaseClass.showCustomLoader(MainActivity.this);

        imgToStringFunction(bitmap, 3333);
        saveTransbody.put("UserID", sharedpreferences.getString("UserID", ""));
        saveTransbody.put("StoreID", storeID);
        saveTransbody.put("BillBefore", billBefore);
        saveTransbody.put("BillAfter", billAfter);
        saveTransbody.put("CreatedAt", ts);
        saveTransbody.put("IsFamilyMember", sharedpreferences.getString("IsFamilyMember", ""));
        saveTransbody.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        saveTransbody.put("Language", language);

        Log.e("save", saveTransbody.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveTransaction, MainActivity.this, saveTransbody, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("SaveTransactionResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            confirmBillDialog.dismiss();
                            rateYourExpeirenceDialog();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("transaction_detail");
                            String BillAfter = jsonObject1.getString("BillAfter");
                            String BillBefore = jsonObject1.getString("BillBefore");
                            String CreatedAt = jsonObject1.getString("CreatedAt");
                            String Discount = jsonObject1.getString("Discount");
                            String Receipt = jsonObject1.getString("Receipt");
                            String StoreID = jsonObject1.getString("StoreID");
                            TransactionID = jsonObject1.getString("TransactionID");
                            String UserID = jsonObject1.getString("UserID");

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void rate(String comment, String reason) {

        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

//        BaseClass.showCustomLoader(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("Rating", Rating);
        body.put("RatingType", "Rating");
        body.put("TransactionID", TransactionID);
        body.put("Reason", reason);
        body.put("Comment", comment);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", "");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("RateResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            JSONObject jsonObject1 = jsonObject.getJSONObject("rating_detail");
                            String Comment = jsonObject1.getString("Comment");
                            String Rating = jsonObject1.getString("Rating");
                            String RatingID = jsonObject1.getString("RatingID");
                            String RatingType = jsonObject1.getString("RatingType");
                            String Reason = jsonObject1.getString("Reason");
                            String TransactionID = jsonObject1.getString("TransactionID");

//                            Dialog regsuccess = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                            regsuccess.setCancelable(false);
//                            regsuccess.setContentView(R.layout.redeem_successfull_dialog);
//                            rateYourExpeirenceDialog.dismiss();
//                            regsuccess.show();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    regsuccess.dismiss();
//                                    BaseClass.dialog.dismiss();
//                                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                                    setFragment(select_pro_frag);
//                                    active = select_pro_frag;
//                                    mDrarLayout.closeDrawers();
//                                }
//                            }, 1000);

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void complain(String comment, String reason) {

        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        BaseClass.showCustomLoader(MainActivity.this);
        Map<String, String> body = new HashMap<String, String>();

        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("RatingType", "Complain");
        body.put("TransactionID", TransactionID);
        body.put("Reason", reason);
        body.put("Comment", comment);
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", "");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.saveRating, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("ComplainResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            complaintDialog.dismiss();

//                            Dialog regsuccess = new Dialog(MainActivity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
//                            regsuccess.setCancelable(false);
//                            regsuccess.setContentView(R.layout.redeem_successfull_dialog);
//                            regsuccess.show();
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    regsuccess.dismiss();
//                                    bottomNavigationView.setSelectedItemId(R.id.bottom_menu);
//                                    setFragment(select_pro_frag);
//                                    active = select_pro_frag;
//                                    mDrarLayout.closeDrawers();
//                                }
//                            }, 1000);
//                            JSONObject jsonObject1 = jsonObject.getJSONObject("rating_detail");
//                            String Comment = jsonObject1.getString("Comment");
//                            String Rating = jsonObject1.getString("Rating");
//                            String RatingID = jsonObject1.getString("RatingID");
//                            String RatingType = jsonObject1.getString("RatingType");
//                            String Reason = jsonObject1.getString("Reason");
//                            String TransactionID = jsonObject1.getString("TransactionID");

                        } else {
                            BaseClass.dialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void changeAccountUser() {
        String language = BaseClass.checkLanguageFunction(MainActivity.this);
        BaseClass.showCustomLoader(MainActivity.this);

        if (sharedpreferences.getString("IsFamilyMember", "").equals("1")) {
            body.put("IsFamilyMember", "0");
        } else if (sharedpreferences.getString("IsFamilyMember", "").equals("0")) {
            body.put("IsFamilyMember", "1");
        }
        body.put("UserID", sharedpreferences.getString("UserID", " "));
        body.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        body.put("Language", language);

        Log.e("body", body.toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, MainActivity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {

                if (ERROR.isEmpty()) {
                    Log.e("UpdateProfileResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String message = jsonObject.getString("message");
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            JSONObject user_info = jsonObject.getJSONObject("user_info");

                            String Cityid = user_info.getString("CityID");
                            String CityTitle = user_info.getString("CityTitle");
                            String Email = user_info.getString("Email");
                            String FirstName = user_info.getString("FirstName");
                            String Image = user_info.getString("Image");
                            String LastName = user_info.getString("LastName");
                            String MiddleName = user_info.getString("MiddleName");
                            String Mobile = user_info.getString("Mobile");
                            String DateOfBirth = user_info.getString("DateOfBirth");
                            String Interest = user_info.getString("Interest");
                            String CompanyName = user_info.getString("CompanyName");
                            String Position = user_info.getString("Position");
                            String Gender = user_info.getString("Gender");
                            String ProfileCompletedPercentage = user_info.getString("ProfileCompletedPercentage");
                            String InterestTitle = user_info.getString("InterestTitle");
                            String CardID = user_info.getString("CardID");
                            String CouponID = user_info.getString("CouponID");
                            String CardBackgroundImage = user_info.getString("CardBackgroundImage");
                            String CardImage = user_info.getString("CardImage");
                            String FamilyMemberDob = user_info.getString("FamilyMemberDob");
                            String FamilyMemberFirstName = user_info.getString("FamilyMemberFirstName");
                            String FamilyMemberGender = user_info.getString("FamilyMemberGender");
                            String FamilyMemberLastName = user_info.getString("FamilyMemberLastName");
                            String FamilyMemberMiddleName = user_info.getString("FamilyMemberMiddleName");
                            String FamilyMemberMobile = user_info.getString("FamilyMemberMobile");
                            String IDImage = user_info.getString("IDImage");
                            String PackageColorLower = user_info.getString("PackageColorLower");
                            String PackageColorUpper = user_info.getString("PackageColorUpper");
                            String FamilyMemberImage = user_info.getString("FamilyMemberImage");
                            String IsFamilyMember = user_info.getString("IsFamilyMember");
                            String TotalSaving = user_info.getString("TotalSaving");
                            String FontColor = user_info.getString("FontColor");

                            mEditor.putString("FullName", FirstName + " " + MiddleName + " " + LastName);
                            mEditor.putString("FirstName", FirstName);
                            mEditor.putString("MiddleName", MiddleName);
                            mEditor.putString("LastName", LastName);
                            mEditor.putString("Email", Email);
                            mEditor.putString("Image", Image);
                            mEditor.putString("Mobile", Mobile);
                            mEditor.putString("CityTitle", CityTitle);
                            mEditor.putString("DateOfBirth", DateOfBirth);
                            mEditor.putString("Interest", Interest);
                            mEditor.putString("CompanyName", CompanyName);
                            mEditor.putString("Position", Position);
                            mEditor.putString("CityID", Cityid);
                            mEditor.putString("Gender", Gender);
                            mEditor.putString("ProfileCompletedPercentage", ProfileCompletedPercentage);
                            mEditor.putString("InterestTitle", InterestTitle);
                            mEditor.putString("CardID", CardID);
                            mEditor.putString("CouponID", CouponID);
                            mEditor.putString("CardImage", CardImage);
                            mEditor.putString("CardBackgroundImage", CardBackgroundImage);
                            mEditor.putString("FamilyMemberDob", FamilyMemberDob);
                            mEditor.putString("FamilyMemberFirstName", FamilyMemberFirstName);
                            mEditor.putString("FamilyMemberGender", FamilyMemberGender);
                            mEditor.putString("FamilyMemberLastName", FamilyMemberLastName);
                            mEditor.putString("FamilyMemberMiddleName", FamilyMemberMiddleName);
                            mEditor.putString("FamilyMemberMobile", FamilyMemberMobile);
                            mEditor.putString("IDImage", IDImage);
                            mEditor.putString("PackageColorLower", PackageColorLower);
                            mEditor.putString("PackageColorUpper", PackageColorUpper);
                            mEditor.putString("FamilyMemberImage", FamilyMemberImage);
                            mEditor.putString("IsFamilyMember", IsFamilyMember);
                            mEditor.putString("TotalSaving", TotalSaving);
                            mEditor.putString("FontColor", FontColor);
                            mEditor.commit();

                            Log.e("familyMemberLog", sharedpreferences.getString("IsFamilyMember", ""));

                            materialProgressBar.setProgress(Integer.parseInt(sharedpreferences.getString("ProfileCompletedPercentage", "")));
                            materialProgressBar.setSupportProgressTintList(progressColorStates);

                            if (sharedpreferences.getString("IsFamilyMember", "").equals("1")) {
                                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("FamilyMemberImage", "")).error(R.drawable.user).into(ProfilePic);
                            } else {
                                Picasso.get().load(Constants.URL.IMG_URL + sharedpreferences.getString("Image", "")).error(R.drawable.user).into(ProfilePic);
                            }


                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            BaseClass.dialog.dismiss();

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLanguageApiCall(String language, String value, String code) {

        if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {

            BaseClass.showCustomLoader(MainActivity.this);

            Map<String, String> bodyy = new HashMap<String, String>();
            bodyy.put("UserID", sharedpreferences.getString("UserID", " "));
            bodyy.put("Lang", value);
            bodyy.put("AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
            Log.e("body", bodyy.toString());

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

            ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATELANGUAGE, MainActivity.this, bodyy, headers, new ServerCallback() {
                @Override
                public void onSuccess(String result, String ERROR) {

                    if (ERROR.isEmpty()) {
                        Log.e("UpdateLanguaheResp", result);
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(result));
                            String message = jsonObject.getString("message");
                            int status = jsonObject.getInt("status");
                            if (status == 200) {
                                Configuration configuration;
                                configuration = getResources().getConfiguration();

                                change_lang.setText(language);
                                mEditor.putString("language", code);
                                mEditor.commit();
                                String languageToLoad = code;
                                Locale locale = new Locale(languageToLoad);
                                Locale.setDefault(locale);
                                configuration.locale = locale;
                                configuration = getResources().getConfiguration();
                                configuration.setLayoutDirection(new Locale(code));
                                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

                                //    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                BaseClass.dialog.dismiss();

                                Intent i = new Intent(MainActivity.this, SplashActivity.class);
                                startActivity(i);
                                finishAffinity();
                            } else {
                                BaseClass.dialog.dismiss();
                                Toast.makeText(MainActivity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            BaseClass.dialog.dismiss();
                            e.printStackTrace();
                        }
                    } else {
                        BaseClass.dialog.dismiss();
                        Toast.makeText(MainActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {

            Configuration configuration;
            configuration = getResources().getConfiguration();

            change_lang.setText(language);
            mEditor.putString("language", code);
            mEditor.commit();
            String languageToLoad = code;
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            configuration.locale = locale;
            configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale(code));
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

            //    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            BaseClass.dialog.dismiss();

            Intent i = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(i);
            finishAffinity();
        }
    }
}
