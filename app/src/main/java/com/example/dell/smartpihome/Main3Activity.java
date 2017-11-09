package com.example.dell.smartpihome;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
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

import CustomListAdapters.Song;
import Tools.CurrentDeviceState;
import Tools.MyMediaPlayer;
import fragment.AlarmFragment;
import fragment.BlindsFragment;
import fragment.CameraFragment;
import fragment.DoorFragment;
import fragment.HomeFragment;
import fragment.LightFragment;
import fragment.MusicPlayerFragment;

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PubNub pubnub;
    Switch light;
    Switch garage;
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

        //TODO: dodac pozostale kontrolki
        light = (Switch) findViewById(R.id.lightBtn);
        garage = (Switch)findViewById(R.id.garageBtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.smart_pi_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.smart_pi_home);
        getSupportActionBar().setLogo(R.drawable.smart_pi_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
            Fragment fragment = new MusicPlayerFragment();
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

    public Map prepareMessage(String type,int number,int direction,double time)
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

    // TODO: dopisac metode wykrywajaca jakie swiatlo wlaczyc i jaki pin przydzielic np poprzez schared prefrences
    public void LightClick(View view) {

        int pin = 0;
        ToggleButton currentButton=null;

//        ImageView livingRoom = (ImageView)findViewById(R.id.LivingRoomLightImg);
//        ImageView kitchen = (ImageView)findViewById(R.id.KitchenLightImg);
//        ImageView corridor = (ImageView)findViewById(R.id.CorridorLightImg);
//        ImageView garage = (ImageView)findViewById(R.id.GarageLightImg);

        if(view.getId()==R.id.lightBtn)
        {
            pin =3;
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
            pin =4;
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
            pin =27;
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
            pin =22;
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
        else if(view.getId()==R.id.fastLightBtn)
        {
            currentButton = (ToggleButton) findViewById(view.getId());
            if (currentButton.isChecked()==true)
            {
                tools.setFastLight(true);
            }
            else
            {
                tools.setFastLight(false);
            }
            if(currentButton.isChecked()==true)
            {
                publishMessage(prepareMessage("light",1,3));
                publishMessage(prepareMessage("light",1,4));
                publishMessage(prepareMessage("light",1,27));
                publishMessage(prepareMessage("light",1,22));
            }
            else
            {
                publishMessage(prepareMessage("light",0,3));
                publishMessage(prepareMessage("light",0,4));
                publishMessage(prepareMessage("light",0,27));
                publishMessage(prepareMessage("light",0,22));
            }
            lightButtonsUpdate();
        }

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
        ToggleButton fastLight = (ToggleButton) findViewById(R.id.fastLightBtn);
        if (fastLight.isChecked()) {
            livingRoom.setChecked(true);
            kitchenRoom.setChecked(true);
            corridorRoom.setChecked(true);
            garageRoom.setChecked(true);
            tools.setKitchenLight(true);
            tools.setLivingRoomLight(true);
            tools.setGarageLight(true);
            tools.setCorridorLight(true);
        } else {
            livingRoom.setChecked(false);
            kitchenRoom.setChecked(false);
            corridorRoom.setChecked(false);
            garageRoom.setChecked(false);
            tools.setKitchenLight(false);
            tools.setLivingRoomLight(false);
            tools.setGarageLight(false);
            tools.setCorridorLight(false);
        }
    }


    // TODO: zmienic garageDoor na uniwewrslana metode door i wykrywac o ktore drzwi nam chodzi
    public void doorClick(View view) {

        int pin = 0;
        ToggleButton currentButton=null;
        ToggleButton front;
        ToggleButton garage;


        if(view.getId()==R.id.garageDoorBtn)
        {
            pin =17;
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
            pin =10;
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
                publishMessage(prepareMessage("door",1,17));
                publishMessage(prepareMessage("door",1,10));
            }
            publishMessage(prepareMessage("door",1,pin));
        }
        else
        {
            if(currentButton.getId()==R.id.allDoorBtn)
            {
                publishMessage(prepareMessage("door",0,17));
                publishMessage(prepareMessage("door",0,10));
            }
            publishMessage(prepareMessage("door",0,pin));
        }
    }

    public void blindClick(View view)
    {
        ToggleButton currentButton=null;
        int number=0;
        if(view.getId()==R.id.livingRoomBlindBtn)
        {
            currentButton = (ToggleButton) findViewById(view.getId());
            number =1;
        }
        else if(view.getId()==R.id.kitchenBlindBtn)
        {
            currentButton = (ToggleButton) findViewById(view.getId());
            number=2;
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

    public void PlaySelectedSong(View view) {
        Song choosenSong;
        try {
            MusicListView = (ListView)view.findViewById(R.id.MusicListView);
            View parentRow = (View) view.getParent();
            //MusicListView = (ListView) parentRow.getParent();
            final int position = MusicListView.getPositionForView(parentRow);
            Toast.makeText(this, "Position "+position, Toast.LENGTH_SHORT).show();
//            choosenSong = player.getCurrentPlaylist().get(position);
//            player.setCurrentSong(choosenSong);
//            player.startPlaying();

        }
        catch (Exception e)
        {
            Toast.makeText(this, "Nie można odtworzyć wybrango utworu "+player.getCurrentSong().getPath().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

