package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import android.media.MediaPlayer;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

public class Car extends AppCompatActivity {

    private static final String VIDEO_SAMPLE ="http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4";


    VideoView videoView;

    EditText editText;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        checkPermission();

        editText = findViewById(R.id.editText);
//        videoView = findViewById(R.id.videoView);
//        play = findViewById(R.id.play);

//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                playVideo();
//            }
//        });

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matches != null)
                    editText.setText(matches.get(0));
                voice();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        }) ;

        findViewById(R.id.button).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        editText.setHint("You will see the input here");
                        break;
                    case MotionEvent.ACTION_DOWN:
                        editText.setText("");
                        editText.setHint("Listening...");
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }
                return false;
            }
        });
    }

    private void voice() {
        Intent intent = getIntent();
        String plateNumber = intent.getStringExtra("PLATE_NUMBER");


        // ...

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://54.159.121.22/api/v1/driver/listen/" +plateNumber +"/" + editText.getText().toString();
        Log.d("URL", url);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //editText.setText("Response is: "+ response.toString());
                        editText.setText("");
                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    //JSONArray errors = data.getJSONArray("error");
                    //JSONObject jsonMessage = errors.getJSONObject(0);
                    String message = data.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                } catch (UnsupportedEncodingException errorr) {
                }

               // editText.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if(!(ContextCompat
                    .checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED)) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

//    private void playVideo() {
//        //videoView.setVideoPath(VIDEO_SAMPLE);
//        //videoView.start();
//        videoView.setVideoURI(Uri.parse(VIDEO_SAMPLE));
//        videoView.requestFocus();
//
//        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//            }
//        });
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                videoView.start();
//                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        MediaController mediaController = new MediaController(Car.this);
//                        videoView.setMediaController(mediaController);
//                        mediaController.setAnchorView(videoView);
//                    }
//                });
//            }
//        });
//
//        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                return false;
//            }
//        });
//    }






}