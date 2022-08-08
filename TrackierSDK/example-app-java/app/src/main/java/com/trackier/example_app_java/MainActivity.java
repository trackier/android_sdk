package com.trackier.example_app_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.trackier.sdk.TrackierEvent;
import com.trackier.sdk.TrackierSDK;

public class MainActivity extends AppCompatActivity {

    Button eventTrack, eventTrackWithCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventTrack = findViewById(R.id.event_track);
        eventTrackWithCurrency = findViewById(R.id.event_curr_track);
        eventTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackierEvent event = new TrackierEvent(TrackierEvent.UPDATE);
                event.param1 = "Param Name";
//                event.c_code = "test_java";
//                event.discount = 5f;
//                TrackierSDK.setUserName("xyz");
//                TrackierSDK.setUserPhone("987654545");
                TrackierSDK.trackEvent(event);
                Log.d("TAG", "onClick: event_track ");
            }
        });
        eventTrackWithCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackierEvent event = new TrackierEvent(TrackierEvent.UPDATE);
                event.param1 = "Praram Name";
                event.revenue = 0.5;
                event.currency = "USD";
                TrackierSDK.trackEvent(event);
                Log.d("TAG", "onClick: eventTrackWithCurrency");
            }
        });
    }
}