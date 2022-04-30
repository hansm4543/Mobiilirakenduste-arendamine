package com.example.settingutememorize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    private View parentview;
    private SwitchMaterial themeSwitch;
    private TextView themeTV, titleTV;

    private userSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        settings = (userSettings) getApplication();
        initWidgets();
        loadSharedPreferences();
        initSwitchListener();
        
    }

    private void initSwitchListener() {
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if(checked)
                    settings.setCustomTheme(userSettings.DARK_THEME);
                else
                    settings.setCustomTheme(userSettings.LIGHT_THEME);

                SharedPreferences.Editor editor = getSharedPreferences(userSettings.PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(userSettings.CUSTOM_THEME,settings.getCustomTheme());
                editor.apply();

                updateView();
            }

        });
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(userSettings.PREFERENCES,MODE_PRIVATE);
        String theme = sharedPreferences.getString(userSettings.CUSTOM_THEME,userSettings.LIGHT_THEME);
        settings.setCustomTheme(theme);
        updateView();
    }

    private void initWidgets() {
        themeTV = findViewById(R.id.themeTV);
        titleTV = findViewById(R.id.titleTV);
        themeSwitch = findViewById(R.id.themeSwitch);
        parentview = findViewById(R.id.parentView);
    }

    private void updateView() {
        final int black = ContextCompat.getColor(this, R.color.black);
        final int white = ContextCompat.getColor(this, R.color.white);

        if(settings.getCustomTheme().equals(userSettings.DARK_THEME)) {
            titleTV.setTextColor(white);
            themeTV.setTextColor(white);
            themeTV.setText("Dark");
            parentview.setBackgroundColor(black);
            themeSwitch.setChecked(true);
        }else {
            titleTV.setTextColor(black);
            themeTV.setTextColor(black);
            themeTV.setText("Light");
            parentview.setBackgroundColor(white);
            themeSwitch.setChecked(false);

        }
    }

}

