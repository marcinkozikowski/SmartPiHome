package com.example.dell.smartpihome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Activity extends AppCompatActivity {

    EditText SubscribeKey;
    EditText PublishKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        InitControls();
        SharedPreferences settings = getSharedPreferences("UserData", 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        if(hasLoggedIn)
        {
            Intent mainMenu = new Intent(this, Main3Activity.class);
            startActivity(mainMenu);
            finish();
        }
    }

    private void InitControls()
    {
        SubscribeKey = (EditText) findViewById(R.id.SubscribeKey_EditText);
        PublishKey = (EditText) findViewById(R.id.PublishKey_EditText);
    }

    public void LogIn(View view) {
        try {
            SharedPreferences settings = getSharedPreferences("UserData", 0); // 0 - for private mode
            SharedPreferences.Editor editor = settings.edit();
            if(PublishKey.getText().length()>32 && SubscribeKey.getText().length()>32)
            {
                editor.putBoolean("hasLoggedIn", true);
                editor.putString("publishKey",PublishKey.getText().toString());
                editor.putString("subscribeKey", SubscribeKey.getText().toString());
                editor.commit();
                Toast.makeText(this, "Poprawnie zalogowano uzytkownika ", Toast.LENGTH_SHORT).show();
                Intent mainMenu = new Intent(this, Main3Activity.class);
                startActivity(mainMenu);
                finish();
            }
            else {
                Toast.makeText(this, "Wprowadzone dane sa nieprawidlowe. ", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Nie mozna zalogowac, sprobuj ponownie", Toast.LENGTH_LONG).show();
        }
    }
}
