//package com.exclusave.Activities;
//
//import android.util.Log;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//
//public class FirebaseIDService extends FirebaseInstanceIdService {
//    private static final String TAG = "FirebaseIDService";
//
//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//        sendRegistrationToServer(refreshedToken);
//    }
//
//    private void sendRegistrationToServer(String token) {
//
//    }
//}