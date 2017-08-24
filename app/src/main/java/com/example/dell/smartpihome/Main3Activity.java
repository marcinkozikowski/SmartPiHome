package com.example.dell.smartpihome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import fragment.AlarmFragment;
import fragment.BlindsFragment;
import fragment.CameraFragment;
import fragment.DoorFragment;
import fragment.HomeFragment;
import fragment.LightFragment;
import fragment.StreamFragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;
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

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PubNub pubnub;
    Switch light;
    Switch garage;
    Switch alarm;
    Switch blind1;
    SeekBar seekBar1;
    TextView temperatureTextView;
    TextView humidityTextView;
    int lastBlindPosition=0;
    String temp = "24";
    String huminidity = "50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //TODO: dodac pozostale kontrolki
        light = (Switch) findViewById(R.id.lightBtn);
        garage = (Switch)findViewById(R.id.garageBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
                        pubnub.publish().channel("SmartPiHome").message(prepareMessage("temp",1,26)).async(new PNCallback<PNPublishResult>() {
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
                    try{
                        JsonObject msg = message.getMessage().getAsJsonObject();
                        if(msg.get("what").getAsString().equals("temp"))
                        {
                            temp = msg.get("pin").getAsString();
                            huminidity = msg.get("state").getAsString();

                            System.out.println("Temperature from Pi: " + temp);
                            System.out.println("Huminidity from Pi: "+huminidity);
                        }

                    } catch (Exception e)
                    {
                        System.out.println(e.toString());
                    }
                    // Message has been received on channel group stored in
                    // message.getChannel()
//                    JsonObject msg = null;
//                    if(message.getMessage().isJsonObject()) {
//                        msg = message.getMessage().getAsJsonObject();
//                        if(msg.has("what"))
//                        {
//                            String temp = msg.get("pin").getAsString();
//                            String humidity = msg.get("state").getAsString();
//
//                            System.out.println(temp);
//                            System.out.println(humidity);
//                        }
//
//                    }
                }
                else {
                    //System.out.print(message.toString());
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channels(Arrays.asList("SmartPiHome")).execute();
        publishMessage(prepareMessage("temp",1,26));

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

        FragmentTransaction fragmentTransaction1 = fragmentManager
                .beginTransaction();
        fragmentTransaction1.replace(R.id.mainFrame,
                fragment);
        Bundle bundle=new Bundle();
        bundle.putString("temp", temp);
        bundle.putString("huminidity",huminidity);

        fragment.setArguments(bundle);
        fragmentTransaction1.commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //TODO: obsluzyc menu ustawienia w ktorym bedzie mozna zmienic poszczegolne piny oswietlenia lub drzwi
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);
            Bundle bundle=new Bundle();
            bundle.putString("temp", temp);
            bundle.putString("huminidity",huminidity);

            fragment.setArguments(bundle);

            fragmentTransaction1.commit();

            publishMessage(prepareMessage("temp",1,26));



        } else if (id == R.id.nav_light) {
            Fragment fragment = new LightFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);
            fragmentTransaction1.commit();

        } else if (id == R.id.nav_blinds) {
            Fragment fragment = new BlindsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);
            fragmentTransaction1.commit();

        } else if (id == R.id.nav_door) {
            Fragment fragment = new DoorFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);
            fragmentTransaction1.commit();

        } else if (id == R.id.nav_alarm) {
            Fragment fragment = new AlarmFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);
            fragmentTransaction1.commit();

        } else if (id == R.id.nav_camera) {
            Fragment fragment = new CameraFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);
            fragmentTransaction1.commit();

        } else if (id==R.id.nav_stream)
        {
            Fragment fragment = new StreamFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);
            fragmentTransaction1.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public Map prepareMessage(String type,int state, int pin_number)
    {
        Map message = new HashMap();

        message.put("type",type);
        message.put("state",state);
        message.put("pin_number",pin_number);

        return message;
    }

    public void publishMessage(Map message)
    {
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

    // TODO: dopisac metode wykrywajaca jakie swiatlo wlaczyc i jaki pin przydzielic
    public void LightClick(View view) {

        light = (Switch) findViewById(R.id.lightBtn);
        int pin = 0;
        Switch currentButton=null;

        if(view.getId()==R.id.lightBtn)
        {
            pin =4;
            currentButton = (Switch)findViewById(view.getId());
        }
        else if(view.getId()==R.id.kitchenLightBtn)
        {
            pin =3;
            currentButton = (Switch)findViewById(view.getId());
        }
        else if(view.getId()==R.id.corridorLightBtn)
        {
            pin =3;
            currentButton = (Switch)findViewById(view.getId());
        }
                Map message = new HashMap();

        if(currentButton.isChecked()==true)
        {
            publishMessage(prepareMessage("light",1,pin));
        }
        else
        {
            publishMessage(prepareMessage("light",0,pin));
        }
    }

    public void garageDoorClick(View view) {
        garage = (Switch)findViewById(R.id.garageBtn);
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
}

