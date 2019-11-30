package com.example.myapplication;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;

public class DeviceTokenService {//extends FirebaseInstanceIdService {
    public String DeviceToken;
    //@Override
    public void  onTokenRefresh() {
        DeviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("DEVICE TOKEN", DeviceToken);
    }
}
