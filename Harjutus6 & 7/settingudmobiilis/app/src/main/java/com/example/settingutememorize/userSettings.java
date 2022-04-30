package com.example.settingutememorize;

import android.app.Application;

public class userSettings extends Application {

    public  static final String PREFERENCES ="preferences";

    public  static final String CUSTOM_THEME ="customTheme";
    public  static final String LIGHT_THEME ="LightTheme";
    public  static final String DARK_THEME ="darkTheme";

    private String customTheme;


    public String getCustomTheme() {
        return customTheme;
    }

    public void setCustomTheme(String customTheme) {
        this.customTheme = customTheme;
    }


}
