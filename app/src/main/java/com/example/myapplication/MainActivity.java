package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText driverName, plateNumber;
    private Button button;
    RadioButton carFront, carMiddle, carBack;
    String carIndex = "1";
    //String video = "http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";
    String video = "";
    TelephonyManager telephonyManager;
    String deviceId;

    DeviceTokenService tokenService;

    String device_unique_id,IMEI;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        driverName = findViewById(R.id.name);
        plateNumber = findViewById(R.id.plate);
        button = (Button) findViewById(R.id.create);
        carFront = findViewById(R.id.front);
        carMiddle = findViewById(R.id.middle);
        carBack = findViewById(R.id.back);


        carFront.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                carIndex = "1";
                video = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4";
            }
        });
        carMiddle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                carIndex = "2";
                video = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
            }
        });
        carBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                carIndex = "3";
                video = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4";
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJson();
                navigate();
            }
        });
    }

    public void navigate() {
        Intent intent = new Intent(this, Car.class);
        intent.putExtra("PLATE_NUMBER", plateNumber.getText().toString());
        startActivity(intent);
    }

    public void createJson() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    //deviceId = tokenService.DeviceToken;
                    deviceId =getToken();;
                    URL url = new URL("http://54.159.121.22/api/v1/driver/create");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonCar = new JSONObject();
                    jsonCar.put("index", carIndex);
                    jsonCar.put("model", "LX675");
                    jsonCar.put("plateNumber", plateNumber.getText().toString());
                    jsonCar.put("video", video);

                    JSONObject jsonContact = new JSONObject();
                    jsonContact.put("phoneNumber", "01080507778");
                    jsonContact.put("address", "gangnam");
                    jsonContact.put("device", deviceId);

                    jsonParam.put("name", driverName.getText().toString());
                    jsonParam.put("licenseNumber", "AA112254");
                    jsonParam.put("car", jsonCar);
                    jsonParam.put("contact", jsonContact);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }


    public String getToken() {
        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("DEVICE TOKEN->", DeviceToken);
        return DeviceToken;
    }




}
