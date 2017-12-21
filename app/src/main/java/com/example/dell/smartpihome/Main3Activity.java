package com.example.dell.smartpihome;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import Entities.LightScene;
import Tools.CurrentDeviceState;
import Tools.MyMediaPlayer;
import Fragments.AlarmFragment;
import Fragments.BlindsFragment;
import Fragments.CameraFragment;
import Fragments.DoorFragment;
import Fragments.HomeFragment;
import Fragments.LightFragment;
import Fragments.MusicPlayerFragment;

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PubNub pubnub;
    Switch light;
    Switch garage;
    Toolbar toolbar;
    SeekBar seekBar1;
    TextView temperatureTextView;
    TextView humidityTextView;
    int lastBlindPosition=0;
    String temp = "24";
    String huminidity = "50";
    public static CurrentDeviceState tools;
    public static MyMediaPlayer player = new MyMediaPlayer();
    ListView MusicListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        String menuFragment="";

        checkPinNumbersSettings();
        setActionBarIcon();

        //TODO: dodac pozostale kontrolki i stworzyc metode w ktorej wszystkie bede na poczatki inicjalizowane
        light = (Switch) findViewById(R.id.lightBtn);
        garage = (Switch)findViewById(R.id.garageBtn);


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
                        System.out.println(msg.toString());
                        if(msg.get("what").getAsString().equals("temp"))
                        {
                            temp = msg.get("pin").getAsString();
                            huminidity = msg.get("state").getAsString();

                            System.out.println("Temperature from Pi: " + temp);
                            System.out.println("Huminidity from Pi: "+huminidity);
                        }
                        else if(msg.get("what").getAsString().equals("motion")){
                            if(msg.get("isInMotion").getAsString().equals("0"))
                            {
                                tools.setMotionDetected(false);
                            }
                            else
                            {
                                tools.setMotionDetected(true);
                            }
                        }
                        else if(msg.get("what").getAsString().equals("notification")){
                            Calendar cal = Calendar.getInstance(); // get current time in a Calendar

                            int hour = cal.get(Calendar.HOUR_OF_DAY);
                            int minutes = cal.get(Calendar.MINUTE);
                            if(minutes<10)
                            {
                                addNotification(hour+" : 0"+minutes);
                            }
                            else
                            {
                                addNotification(hour+" : "+minutes);
                            }
                        }

                    } catch (Exception e)
                    {
                        System.out.println(e.toString());
                    }
                }
                else {
                    //System.out.print(message.toString());
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        if(getIntent().getStringExtra("Fragments")!=null)
        {
            menuFragment = getIntent().getStringExtra("Fragments");
        }

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

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (menuFragment != null) {

            if (menuFragment.equals("camera")) {
                CameraFragment favoritesFragment = new CameraFragment();
                fragmentTransaction.replace(R.id.mainFrame, favoritesFragment).commit();
            }
            else if(menuFragment.equals("alarm"))
            {
                AlarmFragment standardFragment = new AlarmFragment();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.mainFrame, standardFragment).commit();
            }
        }
    }

    private void setActionBarIcon()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.smart_pi_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.smart_pi_home);
        getSupportActionBar().setLogo(R.drawable.smart_pi_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void checkPinNumbersSettings()
    {
        SharedPreferences settings = getSharedPreferences("UserData", 0); // 0 - for private mode
        boolean hasSettings = settings.getBoolean("hasSettingSet", false);
        if(hasSettings==false)
        {
            Intent setting = new Intent(this, SettingsActivity.class);
            startActivity(setting);
            finish();
        }
        else if(hasSettings==true)
        {
            tools.setLivingRoomLightPin(settings.getInt("livingRoomLight",0));
            tools.setKitchenLightPin(settings.getInt("kitchenLight",0));
            tools.setCorridorLightPin(settings.getInt("corridorLight",0));
            tools.setGarageLightPin(settings.getInt("garageLight",0));
            tools.setFrontDoorPin(settings.getInt("frontDoor",0));
            tools.setGarageDoorPin(settings.getInt("garageDoor",0));
            tools.setLivingRoomBlindPin(settings.getInt("livingRoomBlind",0));
            tools.setKitchenBlindPin(settings.getInt("kitchenBlind",0));
        }
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

            FragmentTransaction fragmentTransaction1 = fragmentManager
                    .beginTransaction();
            fragmentTransaction1.replace(R.id.mainFrame,
                    fragment);

            publishMessage(prepareMessage("temp",1,26));

            Bundle bundle=new Bundle();
            bundle.putString("temp", temp);
            bundle.putString("huminidity",huminidity);

            fragment.setArguments(bundle);

            fragmentTransaction1.commit();




        } else if (id == R.id.nav_light) {
            Fragment fragment = new LightFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

//            FragmentTransaction fragmentTransaction1 = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction1.replace(R.id.mainFrame,
//                    fragment);
//            fragmentTransaction1.commit();

        } else if (id == R.id.nav_blinds) {
            Fragment fragment = new BlindsFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

//            FragmentTransaction fragmentTransaction1 = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction1.replace(R.id.mainFrame,
//                    fragment);
//            fragmentTransaction1.commit();

        } else if (id == R.id.nav_door) {
            Fragment fragment = new DoorFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

//            FragmentTransaction fragmentTransaction1 = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction1.replace(R.id.mainFrame,
//                    fragment);
//            fragmentTransaction1.commit();

        } else if (id == R.id.nav_alarm) {
            Fragment fragment = new AlarmFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

//            FragmentTransaction fragmentTransaction1 = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction1.replace(R.id.mainFrame,
//                    fragment);
//            fragmentTransaction1.commit();

        } else if (id == R.id.nav_camera) {
            Fragment fragment = new CameraFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

//            FragmentTransaction fragmentTransaction1 = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction1.replace(R.id.mainFrame,
//                    fragment);
//            fragmentTransaction1.commit();

        } else if (id==R.id.nav_stream)
        {
            Fragment fragment = new MusicPlayerFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainFrame,fragment).commit();

//            FragmentTransaction fragmentTransaction1 = fragmentManager
//                    .beginTransaction();
//            fragmentTransaction1.replace(R.id.mainFrame,
//                    fragment);
//            fragmentTransaction1.commit();
        }
        else if(id==R.id.nav_settings)
        {
            Intent setting = new Intent(this,SettingsActivity.class);
            startActivity(setting);
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

    public Map prepareMessage(String type,int number,int direction,int time)
    {
        Map message = new HashMap();

        message.put("type",type);
        message.put("number",number);
        message.put("direction",direction);
        message.put("time",time);

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

    public void LightClick(View view) {

        int pin = 0;
        ToggleButton currentButton=null;

//        ImageView livingRoom = (ImageView)findViewById(R.id.LivingRoomLightImg);
//        ImageView kitchen = (ImageView)findViewById(R.id.KitchenLightImg);
//        ImageView corridor = (ImageView)findViewById(R.id.CorridorLightImg);
//        ImageView garage = (ImageView)findViewById(R.id.GarageLightImg);

        if(view.getId()==R.id.lightBtn)
        {
            pin =tools.getLivingRoomLightPin();
            currentButton = (ToggleButton) findViewById(view.getId());
            if (currentButton.isChecked()==true)
            {
                tools.setLivingRoomLight(true);
            }
            else
            {
                tools.setLivingRoomLight(false);
            }
        }
        else if(view.getId()==R.id.kitchenLightBtn)
        {
            pin =tools.getKitchenLightPin();
            currentButton = (ToggleButton) findViewById(view.getId());
            if (currentButton.isChecked()==true)
            {
                tools.setKitchenLight(true);
            }
            else
            {
                tools.setKitchenLight(false);
            }
        }
        else if(view.getId()==R.id.CorridorLightBtn)
        {
            pin =tools.getCorridorLightPin();
            currentButton = (ToggleButton) findViewById(view.getId());
            if (currentButton.isChecked()==true)
            {
                tools.setCorridorLight(true);
            }
            else
            {
                tools.setCorridorLight(false);
            }
        }
        else if(view.getId()==R.id.GarageLightBtn)
        {
            pin =tools.getGarageLightPin();
            currentButton = (ToggleButton) findViewById(view.getId());
            if (currentButton.isChecked()==true)
            {
                tools.setGarageLight(true);
            }
            else
            {
                tools.setGarageLight(false);
            }
        }
//        else if(view.getId()==R.id.fastLightBtn)
//        {
//            currentButton = (ToggleButton) findViewById(view.getId());
//            if (currentButton.isChecked()==true)
//            {
//                tools.setFastLight(true);
//            }
//            else
//            {
//                tools.setFastLight(false);
//            }
//            if(currentButton.isChecked()==true)
//            {
//                publishMessage(prepareMessage("light",1,tools.getLivingRoomLightPin()));
//                publishMessage(prepareMessage("light",1,tools.getKitchenLightPin()));
//                publishMessage(prepareMessage("light",1,tools.getCorridorLightPin()));
//                publishMessage(prepareMessage("light",1,tools.getGarageLightPin()));
//            }
//            else
//            {
//                publishMessage(prepareMessage("light",0,tools.getLivingRoomLightPin()));
//                publishMessage(prepareMessage("light",0,tools.getKitchenLightPin()));
//                publishMessage(prepareMessage("light",0,tools.getCorridorLightPin()));
//                publishMessage(prepareMessage("light",0,tools.getGarageLightPin()));
//            }
//            lightButtonsUpdate();
//        }

        if(currentButton.isChecked()==true)
        {
            publishMessage(prepareMessage("light",1,pin));
        }
        else
        {
            publishMessage(prepareMessage("light",0,pin));
        }
    }

    public void lightButtonsUpdate() {
        ToggleButton livingRoom = (ToggleButton) findViewById(R.id.lightBtn);
        ToggleButton kitchenRoom = (ToggleButton) findViewById(R.id.kitchenLightBtn);
        ToggleButton corridorRoom = (ToggleButton) findViewById(R.id.CorridorLightBtn);
        ToggleButton garageRoom = (ToggleButton) findViewById(R.id.GarageLightBtn);
        //ToggleButton fastLight = (ToggleButton) findViewById(R.id.fastLightBtn);
//        if (fastLight.isChecked()) {
//            livingRoom.setChecked(true);
//            kitchenRoom.setChecked(true);
//            corridorRoom.setChecked(true);
//            garageRoom.setChecked(true);
//            tools.setKitchenLight(true);
//            tools.setLivingRoomLight(true);
//            tools.setGarageLight(true);
//            tools.setCorridorLight(true);
//        } else {
//            livingRoom.setChecked(false);
//            kitchenRoom.setChecked(false);
//            corridorRoom.setChecked(false);
//            garageRoom.setChecked(false);
//            tools.setKitchenLight(false);
//            tools.setLivingRoomLight(false);
//            tools.setGarageLight(false);
//            tools.setCorridorLight(false);
//        }
    }

    public void LightSceneClick(LightScene lc)
    {
        if(lc.isLivingRoom())
        {
            publishMessage(prepareMessage("light",1,tools.getLivingRoomLightPin()));
        }
        else
        {
            publishMessage(prepareMessage("light",0,tools.getLivingRoomLightPin()));
        }
        if(lc.isKitchen())
        {
            publishMessage(prepareMessage("light",1,tools.getKitchenLightPin()));
        }
        else
        {
            publishMessage(prepareMessage("light",0,tools.getKitchenLightPin()));
        }
        if(lc.isGarage())
        {
            publishMessage(prepareMessage("light",1,tools.getGarageLightPin()));
        }
        else
        {
            publishMessage(prepareMessage("light",0,tools.getGarageLightPin()));
        }
        if(lc.isCorridor())
        {
            publishMessage(prepareMessage("light",1,tools.getCorridorLightPin()));
        }
        else
        {
            publishMessage(prepareMessage("light",0,tools.getCorridorLightPin()));
        }
    }

    public void doorClick(View view) {

        int pin = 0;
        ToggleButton currentButton=null;
        ToggleButton front;
        ToggleButton garage;


        if(view.getId()==R.id.garageDoorBtn)
        {
            pin =tools.getGarageDoorPin();
            currentButton = (ToggleButton) findViewById(view.getId());
            if(currentButton.isChecked())
            {
                tools.setGarageDoor(true);
            }
            else
            {
                tools.setGarageDoor(false);
            }
        }
        else if(view.getId()==R.id.frontDoorBtn)
        {
            pin =tools.getFrontDoorPin();
            currentButton = (ToggleButton) findViewById(view.getId());
            if(currentButton.isChecked())
            {
                tools.setFrontDoor(true);
            }
            else
            {
                tools.setFrontDoor(false);
            }
        }
        else if(view.getId()==R.id.allDoorBtn)
        {
            garage = (ToggleButton)findViewById(R.id.garageDoorBtn);
            front = (ToggleButton)findViewById(R.id.frontDoorBtn);
            currentButton = (ToggleButton) findViewById(view.getId());
            if(currentButton.isChecked())
            {
                tools.setFrontDoor(true);
                tools.setGarageDoor(true);
                garage.setChecked(true);
                front.setChecked(true);
            }
            else
            {
                tools.setFrontDoor(false);
                tools.setGarageDoor(false);
                garage.setChecked(false);
                front.setChecked(false);
            }

        }

        if(currentButton.isChecked()==true)
        {
            if(currentButton.getId()==R.id.allDoorBtn)
            {
                publishMessage(prepareMessage("door",1,tools.getGarageDoorPin()));
                publishMessage(prepareMessage("door",1,tools.getFrontDoorPin()));
            }
            publishMessage(prepareMessage("door",1,pin));
        }
        else
        {
            if(currentButton.getId()==R.id.allDoorBtn)
            {
                publishMessage(prepareMessage("door",0,tools.getGarageDoorPin()));
                publishMessage(prepareMessage("door",0,tools.getFrontDoorPin()));
            }
            publishMessage(prepareMessage("door",0,pin));
        }
    }

    public void alarm_Click(View view) {
        if(tools.isAlarm())
        {
            tools.setAlarm(false);
        }
        else
        {
            tools.setAlarm(true);
        }
        if(tools.isAlarm()) {
            publishMessage(prepareMessage("alarm", 1, 11));
        }
        else if(tools.isAlarm()==false)
        {
            publishMessage(prepareMessage("alarm", 0, 11));
        }
    }

    public void BlindMove(int blindPin,int turns,int direction)
    {
        publishMessage(prepareMessage("blind",blindPin,direction,turns));
    }

    public void blindClick(View view)
    {
        ToggleButton currentButton=null;
        SeekBar livingRoom = (SeekBar)view.findViewById(R.id.blind1SeekBar);
        SeekBar kitchen = (SeekBar)view.findViewById(R.id.kitchenblind1SeekBar);
        int number=0;
        if(view.getId()==R.id.livingRoomBlindBtn)
        {
            currentButton = (ToggleButton) findViewById(view.getId());
            number =1;
            if(currentButton.isChecked())
            {
                tools.setLivingRoomBlind(true);
                tools.setLivingRoomBlindPosition(4);
            }
            else
            {
                tools.setLivingRoomBlind(false);
                tools.setLivingRoomBlindPosition(0);
            }
        }
        else if(view.getId()==R.id.kitchenBlindBtn)
        {
            currentButton = (ToggleButton) findViewById(view.getId());
            number=2;
            if(currentButton.isChecked())
            {
                tools.setKitchenBlind(true);
                tools.setKitchenBlindPosition(4);
            }
            else
            {
                tools.setKitchenBlind(false);
                tools.setKitchenBlindPosition(0);
            }
        }

        if(currentButton.isChecked()==true)
        {
            publishMessage(prepareMessage("blind",number,1,1));
        }
        else
        {
            publishMessage(prepareMessage("blind",number,0,1));
        }
    }

    public void rotateLoginTitle()
    {
        SharedPreferences settings = getSharedPreferences("UserData", 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);
        MenuItem login = (MenuItem)findViewById(R.id.nav_login);

        if(hasLoggedIn)
        {
            login.setTitle("Wyloguj");
        }
        else if(!hasLoggedIn)
        {
            login.setTitle("Wyloguj");
        }
    }

    public void addNotification(String time) {

        Intent alarmIntent = new Intent(this, Main3Activity.class);
        alarmIntent.putExtra("Fragments", "alarm");
        PendingIntent alarmPendingIntent = PendingIntent.getActivity(this, 0, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent cameraIntent = new Intent(this, Main3Activity.class);
        cameraIntent.putExtra("Fragments", "camera");
        PendingIntent cameraPendingIntent = PendingIntent.getActivity(this, 1, cameraIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.alarm_worning)
                        .setContentTitle("ALARM !! Wykryto intruza !!")
                        .setContentText("Ostatnio wykryto ruch o godzinie: "+time)
                        .setContentIntent(cameraPendingIntent)
                        .addAction(android.R.drawable.ic_menu_camera, "Kamery", cameraPendingIntent)
                        .addAction(android.R.drawable.ic_dialog_alert, "Alarm", alarmPendingIntent);




        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        manager.notify(1, notification);
    }

    public void LoginLogout_Click(MenuItem item) {
        SharedPreferences settings = getSharedPreferences("UserData", 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();

        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(hasLoggedIn)
        {
            editor.putBoolean("hasLoggedIn", false);
            editor.commit();
            finish();
        }
        else if(!hasLoggedIn) {
            Intent login = new Intent(this, Login_Activity.class);
            startActivity(login);
            finish();
        }
    }

    public void ExitApp_Click(MenuItem item) {
        finish();
    }

}

