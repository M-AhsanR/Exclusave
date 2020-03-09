package com.exclusave.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.exclusave.Activities.ChatActivity;
import com.exclusave.Adapters.NotificationsAdapter;
import com.exclusave.ApiStructure.ApiModelClass;
import com.exclusave.ApiStructure.Constants;
import com.exclusave.ApiStructure.ServerCallback;
import com.exclusave.BaseClass;
import com.exclusave.models.Notifications_Data;
import com.exclusave.BuildConfig;
import com.exclusave.R;
import com.google.android.gms.nearby.connection.Connections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView notificatiolnsEmptyMessage;
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor mEditor;
    public static final String MyPREFERENCES = "MyPrefs";
    RecyclerView notificationRecycler;
    private String mParam1;
    private String mParam2;
    ArrayList<Notifications_Data> notifications_dataList = new ArrayList<>();
    String BookingByFamilyMember;
    String BookingDate;
    String BookingTime;
    String NoOfGuests;
    String BranchTitle;
    String FirstName;
    String Image;
    String IsApproved;
    String StoreTitle;
    String CategoryTitle;
    String MiddleName;
    String LastName;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mEditor = sharedpreferences.edit();
        initilizeViews(view);

        if(sharedpreferences.getString("isLoggedIn", "").equals("1")){
            notificationsApiCall("no");
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notificationsApiCall("yes");
            }
        });

    }

    private void initilizeViews(View view) {
        notificationRecycler = view.findViewById(R.id.notificationsRecycler);
        notificatiolnsEmptyMessage = view.findViewById(R.id.notifications_empty_message);
        swipeRefreshLayout = view.findViewById(R.id.notification_swipRefresh);
    }


    public void notificationsApiCall(String fromrefresh){
//        BaseClass.showCustomLoader(getActivity());
        String language = BaseClass.checkLanguageFunction(getActivity());
        Map<String, String> body = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Verifytoken", sharedpreferences.getString("ApiToken", " "));

        String URL = "";
        if (sharedpreferences.getString("isLoggedIn", "").equals("0")) {
//            URL = Constants.URL.BASE_URL + "getNotifications?AndroidAppVersion=" + BuildConfig.VERSION_CODE + "&RoleID=2"+"&CommonID="+sharedpreferences.getString("UserID", "")+"&Language="+language;
        } else if (sharedpreferences.getString("isLoggedIn", "").equals("1")) {
            URL = Constants.URL.GETNOTIFICATIONS + sharedpreferences.getString("UserID", "") + "&AndroidAppVersion=" + BuildConfig.VERSION_CODE+"&RoleID=2"+"&CommonID="+sharedpreferences.getString("UserID", "")+"&Language="+language;
        }

        Log.e("url", URL);
        Log.e("urlToken", sharedpreferences.getString("ApiToken", " "));

        ApiModelClass.GetApiResponse(Request.Method.GET, URL, getActivity(), body, headers, new ServerCallback() {
            @Override
            public void onSuccess(String result, String ERROR) {
                if (ERROR.isEmpty()) {
                    Log.e("GetNotificationsResp", result);
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            notifications_dataList.clear();
                            if (fromrefresh.equals("yes")){
                                swipeRefreshLayout.setRefreshing(false);
                            }
//                            BaseClass.dialog.dismiss();
                            JSONArray notificationsArray = jsonObject.getJSONArray("notifications");
                            for (int i = 0; i < notificationsArray.length(); i++) {
                                JSONObject notificationsObj = notificationsArray.getJSONObject(i);
//
                                String BookingID = notificationsObj.getString("BookingID");
                                String CreatedAt = notificationsObj.getString("CreatedAt");
                                String Description = notificationsObj.getString("NotificationText");
                                String NotificationFrom = notificationsObj.getString("NotificationFrom");
                                String NotificationID = notificationsObj.getString("NotificationID");
                                String NotificationTo = notificationsObj.getString("NotificationTo");
                                String NotificationType = notificationsObj.getString("NotificationType");
                                String RoleID = notificationsObj.getString("RoleID");
                                String Title = notificationsObj.getString("Title");
                                String ChatID = notificationsObj.getString("ChatID");

                                if (notificationsObj.has("BookingData")) {
                                    JSONObject bookingDataOnj = notificationsObj.getJSONObject("BookingData");
                                    BookingByFamilyMember = bookingDataOnj.getString("BookingByFamilyMember");
                                    BookingDate = bookingDataOnj.getString("BookingDate");
                                    BookingTime = bookingDataOnj.getString("BookingTime");
                                    NoOfGuests = bookingDataOnj.getString("NoOfGuests");
                                    BranchTitle = bookingDataOnj.getString("Title");
                                    IsApproved = bookingDataOnj.getString("IsApproved");

                                    JSONObject fromStoreData = notificationsObj.getJSONObject("FromStoreData");
                                    FirstName = fromStoreData.getString("FirstName");
                                    Image = fromStoreData.getString("Image");
                                    StoreTitle = fromStoreData.getString("Title");
                                    CategoryTitle = fromStoreData.getString("CategoryTitle");
                                    MiddleName = fromStoreData.getString("MiddleName");
                                    LastName = fromStoreData.getString("LastName");
                                }

                                notifications_dataList.add(new Notifications_Data(BookingID, CreatedAt, Description, NotificationFrom,NotificationID,NotificationTo,NotificationType,
                                        RoleID,Title, ChatID, BookingByFamilyMember, BookingDate, BookingTime, NoOfGuests, BranchTitle,IsApproved, FirstName, Image, StoreTitle, CategoryTitle, MiddleName, LastName));
                            }

                            if (notifications_dataList.size() == 0){
                                swipeRefreshLayout.setVisibility(View.GONE);
                                notificationRecycler.setVisibility(View.GONE);
                                notificatiolnsEmptyMessage.setVisibility(View.VISIBLE);
                            }

                            notificationRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            NotificationsAdapter adapter = new NotificationsAdapter(getActivity(), notifications_dataList, new NotificationsAdapter.CRLCallbacks() {
                                @Override
                                public void onItemClick(int position) {
//                                    Log.e("NotiID", notifications_dataList.get(position).getChatID());
//                                    if(!notifications_dataList.get(position).getChatID().equals("0")){
//                                        Log.e("NotiID", notifications_dataList.get(position).getChatID());
//                                        mEditor.putString("ReceiverID", notifications_dataList.get(position).getNotificationFrom()).commit();
//                                        startActivity(new Intent(getActivity(), ChatActivity.class));
//                                    }
                                }
                            });
                            notificationRecycler.setAdapter(adapter);

                        } else {
//                            BaseClass.dialog.dismiss();
                            Toast.makeText(getActivity(), String.valueOf(status), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
//                        BaseClass.dialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
//                    BaseClass.dialog.dismiss();
                    Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
