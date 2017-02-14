package com.example.dell.smartpihome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    PubNub pubnub;
    Switch light;
    Switch garage;
    Switch alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        light = (Switch) findViewById(R.id.lightBtn);
        garage = (Switch) findViewById(R.id.garageBtn);
        alarm = (Switch) findViewById(R.id.alarmBtn);

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-0baf6e4c-c5ff-11e6-b2ab-0619f8945a4f");
        pnConfiguration.setPublishKey("pub-c-2902e976-c6d8-41bd-bebf-f3a14c238494");

        pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {


                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc

                    if (status.getCategory() == PNStatusCategory.PNConnectedCategory){
                        pubnub.publish().channel("awesomeChannel").message("hello!!").async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // Check whether request successfully completed or not.
                                if (!status.isError()) {

                                    // Message successfully published to specified channel.
                                }
                                // Request processing failed.
                                else {

                                    // Handle message publish error. Check 'category' property to find out possible issue
                                    // because of which request did fail.
                                    //
                                    // Request can be resent using: [status retry];
                                }
                            }
                        });
                    }
                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channels(Arrays.asList("awesomeChannel")).execute();
    }

    public void on_click(View view) {
        Map message = new HashMap();
        if(light.isChecked()==true)
        {
            message.put("type","light");
            message.put("state",1);
            message.put("pin_number",4);
        }
        else
        {
            message.put("type","light");
            message.put("state",0);
            message.put("pin_number",4);
        }
        pubnub.publish()
                .channel("SmartPiHome")
                .message(message)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            System.out.println(status);
                        } else {
                            System.out.println("Published!");
                        }
                    }
                    });
    }

    public void garageClick(View view) {
        Map message = new HashMap();
        if(garage.isChecked()==true)
        {
            message.put("type","door");
            message.put("state",1);
            message.put("pin_number",17);
        }
        else
        {
            message.put("type","door");
            message.put("state",0);
            message.put("pin_number",17);
        }
        pubnub.publish()
                .channel("SmartPiHome")
                .message(message)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            System.out.println(status);
                        } else {
                            System.out.println("Published!");
                        }
                    }
                });
    }

    public void alarmClick(View view) {
        Map message = new HashMap();
        if(alarm.isChecked()==true)
        {
            message.put("type","alarm");
            message.put("state",1);
            message.put("pin_number",11);
        }
        else
        {
            message.put("type","alarm");
            message.put("state",0);
            message.put("pin_number",11);
        }
        pubnub.publish()
                .channel("SmartPiHome")
                .message(message)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            System.out.println(status);
                        } else {
                            System.out.println("Published!");
                        }
                    }
                });
    }
}
