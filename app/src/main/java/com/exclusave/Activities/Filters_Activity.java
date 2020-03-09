package com.exclusave.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.exclusave.Adapters.CategoriesFilterAdapter;
import com.exclusave.Adapters.CitiesFilterAdapter;
import com.exclusave.Adapters.SubCategoriesFilterAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.CitiesData;
import com.exclusave.models.InterestsData;
import com.exclusave.models.SubCategoriesData;
import com.exclusave.BuildConfig;
import com.exclusave.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Filters_Activity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<InterestsData> categoriesData = new ArrayList<>();
    ArrayList<CitiesData> citiesData = new ArrayList<>();
    ArrayList<SubCategoriesData> subCategoriesData = new ArrayList<>();
    RecyclerView categoriesRecycler, citiesRecycler, subcategoriesRecycler;
    AutoCompleteTextView citiestext, categoriestext, subcategories;
    LinearLayout cities_linear, categories_linear, subcategories_linear;
    Dialog citiesDialog, categoriesDialog, subcategoriesDialog;
    String categoryID;
    TextView clearAll;
    Button citiesDone, categoriesDone, subcategoriesDone, azBtn, discountBtn, popularBtn, applyfilterBtn;
    String sortBy = "A : Z";

    public static String publiccityID = "";
    public static String publiccategoryID = "";
    public static String publicsubCategoryID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters_);

        sharedPreferences = BaseClass.sharedPreferances(Filters_Activity.this);
        editor = BaseClass.sharedPreferancesEditor(Filters_Activity.this);

        CategoriesFilterAdapter.categories.clear();
        CategoriesFilterAdapter.categoryIds.clear();
        CitiesFilterAdapter.cities.clear();
        CitiesFilterAdapter.cityIds.clear();

        initializeViews();
        getCities();
        getCategories();

        applyfilterBtn.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedPreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedPreferences.getString("PackageColorLower", "")), Color.parseColor(sharedPreferences.getString("PackageColorUpper", ""))}, 8));


    }

    @Override
    public void onClick(View v) {
        if (v == cities_linear) {
            citiesDialog();
        } else if (v == categories_linear) {
            categoriesDialog();
        } else if (v == azBtn) {
            sortBy(azBtn, discountBtn, popularBtn);
        } else if (v == discountBtn) {
            sortBy(discountBtn, azBtn, popularBtn);
        } else if (v == popularBtn) {
            sortBy(popularBtn, azBtn, discountBtn);
        } else if (v == clearAll) {
            citiestext.setText("");
            categoriestext.setText("");
            sortBy(azBtn, discountBtn, popularBtn);
        } else if (v == subcategories_linear) {
            if (categoriestext.getText().toString().isEmpty()) {
                Toast.makeText(Filters_Activity.this, this.getResources().getString(R.string.selectCategoryFirst), Toast.LENGTH_SHORT).show();
            } else {
                subCategoriesDialogFunction();
            }
        } else if (v == applyfilterBtn) {
            if (citiestext.getText().toString().isEmpty() && categoriestext.getText().toString().isEmpty() && subcategories.getText().toString().isEmpty()) {
                Toast.makeText(Filters_Activity.this, this.getResources().getString(R.string.applyAFilterToContinue), Toast.LENGTH_SHORT).show();
            } else {
                publiccityID = android.text.TextUtils.join(",", CitiesFilterAdapter.cityIds);
                publiccategoryID = android.text.TextUtils.join(",", CategoriesFilterAdapter.categoryIds);
                publicsubCategoryID = android.text.TextUtils.join(",", SubCategoriesFilterAdapter.subcategoryIds);
                Intent intent = new Intent(Filters_Activity.this, Filters_Result_Activity.class);
                intent.putExtra("SORTTYPE", sortBy);
                startActivity(intent);
                finish();
            }
        }
    }

    public void initializeViews() {
        citiesDialog = new Dialog(Filters_Activity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        citiesDialog.setContentView(R.layout.city_filter_dialog);
        citiesRecycler = citiesDialog.findViewById(R.id.citiesRecycler);
        citiesDone = citiesDialog.findViewById(R.id.citiesDone);

        categoriesDialog = new Dialog(Filters_Activity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        categoriesDialog.setContentView(R.layout.category_filter_dialog);
        categoriesRecycler = categoriesDialog.findViewById(R.id.categoriesRecycler);
        categoriesDone = categoriesDialog.findViewById(R.id.categoriesDone);

        subcategoriesDialog = new Dialog(Filters_Activity.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        subcategoriesDialog.setContentView(R.layout.subcategories_filter_dialog);
        subcategoriesRecycler = subcategoriesDialog.findViewById(R.id.subcategoriesRecycler);
        subcategoriesDone = subcategoriesDialog.findViewById(R.id.subcategoriesDone);

        citiestext = findViewById(R.id.cities);
        categoriestext = findViewById(R.id.categories);
        azBtn = findViewById(R.id.azBtn);
        discountBtn = findViewById(R.id.discountBtn);
        popularBtn = findViewById(R.id.popularBtn);
        applyfilterBtn = findViewById(R.id.applyFiltersBtn);
        clearAll = findViewById(R.id.clearAll);
        subcategories = findViewById(R.id.subcategories);

        cities_linear = findViewById(R.id.cities_click);
        categories_linear = findViewById(R.id.categories_click);
        subcategories_linear = findViewById(R.id.subcategories_click);

        Log.e("colors", sharedPreferences.getString("PackageColorUpper", ""));
        String upper = sharedPreferences.getString("PackageColorUpper", "");
        String lower = sharedPreferences.getString("PackageColorLower", "");
        applyfilterBtn.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedPreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedPreferences.getString("PackageColorLower", "")), Color.parseColor(sharedPreferences.getString("PackageColorUpper", ""))}, 8));

        subcategories_linear.setOnClickListener(this);
        categories_linear.setOnClickListener(this);
        cities_linear.setOnClickListener(this);
        citiestext.setOnClickListener(this);
        categoriestext.setOnClickListener(this);
        discountBtn.setOnClickListener(this);
        azBtn.setOnClickListener(this);
        popularBtn.setOnClickListener(this);
        applyfilterBtn.setOnClickListener(this);
        clearAll.setOnClickListener(this);
        subcategories.setOnClickListener(this);

    }

    private void getCities() {

        String language = BaseClass.checkLanguageFunction(Filters_Activity.this);
        BaseClass.showCustomLoader(Filters_Activity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.CITIES + "?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&OS=" + Build.VERSION.RELEASE + "&Language=" + language, Filters_Activity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetCitiesResponse", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {

                            BaseClass.dialog.dismiss();
                            JSONArray citiesArray = jsonObject.getJSONArray("cities");
                            for (int i = 0; i < citiesArray.length(); i++) {
                                JSONObject jsonObject1 = citiesArray.getJSONObject(i);

                                String CityID = jsonObject1.getString("CityID");
                                String CityLat = jsonObject1.getString("CityLat");
                                String CityLong = jsonObject1.getString("CityLong");
                                String CityPlaceID = jsonObject1.getString("CityPlaceID");
                                String Title = jsonObject1.getString("Title");
                                citiesData.add(new CitiesData(CityID, CityLat, CityLong, CityPlaceID, Title));
                            }

                            citiesRecycler.setLayoutManager(new LinearLayoutManager(Filters_Activity.this));
                            CitiesFilterAdapter citiesFilterAdapter = new CitiesFilterAdapter(Filters_Activity.this, citiesData, new CitiesFilterAdapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            citiesRecycler.setAdapter(citiesFilterAdapter);


                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(Filters_Activity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(Filters_Activity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getCategories() {

        String language = BaseClass.checkLanguageFunction(Filters_Activity.this);
//        BaseClass.showCustomLoader(Filters_Activity.this);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "categories?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.CATEGORIES + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&Language=" + language;
        }

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, Filters_Activity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetCategoriesResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            categoriesData.clear();
                            BaseClass.dialog.dismiss();
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
                                categoriesData.add(new InterestsData(BackgroundImage, CategoryID, Image, Title, SortOrder, GoldImage, "no"));
                            }

                            categoriesRecycler.setLayoutManager(new LinearLayoutManager(Filters_Activity.this));
                            CategoriesFilterAdapter categoriesFilterAdapter = new CategoriesFilterAdapter(Filters_Activity.this, categoriesData, new CategoriesFilterAdapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            categoriesRecycler.setAdapter(categoriesFilterAdapter);

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(Filters_Activity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(Filters_Activity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getSubCategories() {
        String language = BaseClass.checkLanguageFunction(Filters_Activity.this);
        categoryID = android.text.TextUtils.join(",", CategoriesFilterAdapter.categoryIds);
        Log.e("Cate", "cat id " + categoryID);
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedPreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedPreferences.getString("isLoggedIn", "").equals("0")) {
            URL = Constants.URL.BASE_URL + "subCategories?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&ParentID=" + categoryID + "&Language=" + language;
        } else if (sharedPreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.SUBCATOGARIES + sharedPreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&ParentID=" + categoryID + "&Language=" + language;
        }

        Log.e("body", categoryID);

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, Filters_Activity.this, body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("CategoriesResp", result);
                    try {
                        subCategoriesData.clear();
                        subcategories.setText("");
                        SubCategoriesFilterAdapter.subcategories.clear();
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            BaseClass.dialog.dismiss();
                            JSONArray subcatArray = jsonObject.getJSONArray("categories");
                            for (int i = 0; i < subcatArray.length(); i++) {
                                JSONObject categoriesObj = subcatArray.getJSONObject(i);

                                String BackgroundImage = categoriesObj.getString("BackgroundImage");
                                String CategoryID = categoriesObj.getString("CategoryID");
                                String Image = categoriesObj.getString("Image");
                                String Title = categoriesObj.getString("Title");
                                subCategoriesData.add(new SubCategoriesData(BackgroundImage, CategoryID, Image, Title));
                            }

                            subcategoriesRecycler.setLayoutManager(new LinearLayoutManager(Filters_Activity.this));
                            SubCategoriesFilterAdapter subcategoriesFilterAdapter = new SubCategoriesFilterAdapter(Filters_Activity.this, subCategoriesData, new SubCategoriesFilterAdapter.CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {

                                }
                            });
                            subcategoriesRecycler.setAdapter(subcategoriesFilterAdapter);

                        } else {
                            BaseClass.dialog.dismiss();
                            Toast.makeText(Filters_Activity.this, String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    BaseClass.dialog.dismiss();
                    Toast.makeText(Filters_Activity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void citiesDialog() {

        citiesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        citiesDialog.show();

        citiesDone.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedPreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedPreferences.getString("PackageColorLower", "")), Color.parseColor(sharedPreferences.getString("PackageColorUpper", ""))}, 8));

        citiesDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.text.TextUtils.join(",", CitiesFilterAdapter.cities);
                citiestext.setText(android.text.TextUtils.join(",", CitiesFilterAdapter.cities));
                citiesDialog.dismiss();
            }
        });
    }

    private void categoriesDialog() {

        categoriesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        categoriesDialog.show();

        categoriesDone.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedPreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedPreferences.getString("PackageColorLower", "")), Color.parseColor(sharedPreferences.getString("PackageColorUpper", ""))}, 8));

        categoriesDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Cate", "pressed");
                android.text.TextUtils.join(",", CitiesFilterAdapter.cities);
                categoriestext.setText(android.text.TextUtils.join(",", CategoriesFilterAdapter.categories));
                Log.e("Cate", android.text.TextUtils.join(",", CategoriesFilterAdapter.categories));
                getSubCategories();
                categoriesDialog.dismiss();
            }
        });
    }

    private void subCategoriesDialogFunction() {

        subcategoriesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        subcategoriesDialog.show();

        subcategoriesDone.setBackgroundDrawable(new BaseClass.DrawableGradient(new int[]{Color.parseColor(sharedPreferences.getString("PackageColorUpper", "")), Color.parseColor(sharedPreferences.getString("PackageColorLower", "")), Color.parseColor(sharedPreferences.getString("PackageColorUpper", ""))}, 8));

        subcategoriesDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subcategories.setText(android.text.TextUtils.join(",", SubCategoriesFilterAdapter.subcategories));
                subcategoriesDialog.dismiss();
            }
        });
    }

    private void sortBy(Button a, Button b, Button c) {
        a.setBackground(this.getResources().getDrawable(R.drawable.selected_btn));
        b.setBackground(this.getResources().getDrawable(R.drawable.unselected_btn));
        c.setBackground(this.getResources().getDrawable(R.drawable.unselected_btn));

        sortBy = a.getText().toString();
    }
}
