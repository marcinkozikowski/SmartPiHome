package com.example.dell.smartpihome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.dell.smartpihome.Main3Activity.tools;

public class SettingsActivity extends AppCompatActivity {

    EditText livingRoomLight;
    EditText kitchenLight;
    EditText corridorLight;
    EditText garageLight;
    EditText livingRoomBlind;
    EditText kitchenBlind;
    EditText frontDoor;
    EditText garageDoor;
    EditText ipAddr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InitControls();
    }

    private void InitControls() {
        livingRoomLight = (EditText) findViewById(R.id.LivingRoomLightSettingsET);
        kitchenLight = (EditText) findViewById(R.id.KitchenSettingsET);
        corridorLight = (EditText) findViewById(R.id.CorridorLightSettingsET);
        garageLight = (EditText) findViewById(R.id.GarageLightSettingsET);
        livingRoomBlind = (EditText) findViewById(R.id.LivingRoomBlindSettingsET);
        kitchenBlind = (EditText) findViewById(R.id.KitchenBlindSettingsET);
        frontDoor = (EditText) findViewById(R.id.FrontDoorSettingsET);
        garageDoor = (EditText) findViewById(R.id.GarageSettingsET);
        ipAddr = (EditText) findViewById(R.id.IpSettingsET);


        SharedPreferences settings = getSharedPreferences("UserData", 0); // 0 - for private mode
        boolean set = settings.getBoolean("hasSettingSet", false);
        try{
        if (set) {
            livingRoomLight.setText(Integer.toString(tools.getLivingRoomLightPin()));
            kitchenLight.setText(Integer.toString(tools.getKitchenLightPin()));
            corridorLight.setText(Integer.toString(tools.getCorridorLightPin()));
            garageLight.setText(Integer.toString(tools.getGarageLightPin()));
            livingRoomBlind.setText(Integer.toString(tools.getLivingRoomBlindPin()));
            kitchenBlind.setText(Integer.toString(tools.getKitchenBlindPin()));
            frontDoor.setText(Integer.toString(tools.getFrontDoorPin()));
            garageDoor.setText(Integer.toString(tools.getGarageDoorPin()));
            ipAddr.setText(tools.getIp());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Nie można odczytac ustawien, sprawdź dane", Toast.LENGTH_LONG).show();
        }

        }

    public void SaveSettings_Click(View view) {
        try {
            SharedPreferences settings = getSharedPreferences("UserData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = settings.edit();
                editor.putInt("livingRoomLight",Integer.parseInt(livingRoomLight.getText().toString()));
            tools.setLivingRoomLightPin(Integer.parseInt(livingRoomLight.getText().toString()));
                editor.putInt("kitchenLight",Integer.parseInt(kitchenLight.getText().toString()));
            tools.setKitchenLightPin(Integer.parseInt(kitchenLight.getText().toString()));
                editor.putInt("corridorLight",Integer.parseInt(corridorLight.getText().toString()));
            tools.setCorridorLightPin(Integer.parseInt(corridorLight.getText().toString()));
                editor.putInt("garageLight",Integer.parseInt(garageLight.getText().toString()));
            tools.setGarageLightPin(Integer.parseInt(garageLight.getText().toString()));
                editor.putInt("frontDoor",Integer.parseInt(frontDoor.getText().toString()));
            tools.setFrontDoorPin(Integer.parseInt(frontDoor.getText().toString()));
                editor.putInt("garageDoor",Integer.parseInt(garageDoor.getText().toString()));
            tools.setGarageDoorPin(Integer.parseInt(garageDoor.getText().toString()));
                editor.putInt("livingRoomBlind",Integer.parseInt(livingRoomBlind.getText().toString()));
            tools.setLivingRoomBlindPin(Integer.parseInt(livingRoomBlind.getText().toString()));
                editor.putInt("kitchenBlind",Integer.parseInt(kitchenBlind.getText().toString()));
            tools.setKitchenBlindPin(Integer.parseInt(kitchenBlind.getText().toString()));
                editor.putString("ip",ipAddr.getText().toString());
            tools.setIp(ipAddr.getText().toString());
                editor.putBoolean("hasSettingSet", true);
                editor.commit();
                Toast.makeText(this, "Poprawnie zapisano nowe ustawienia ", Toast.LENGTH_SHORT).show();
                Intent mainMenu = new Intent(this, Main3Activity.class);
                startActivity(mainMenu);
                finish();

        } catch (Exception e) {
            Toast.makeText(this, "Nie można zapisać wprowadzonych zmian, sprawdź dane", Toast.LENGTH_LONG).show();
        }

    }
}
