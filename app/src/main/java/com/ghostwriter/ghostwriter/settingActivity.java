package com.ghostwriter.ghostwriter;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceActivity;

/**
 * Created by msi on 2017-05-12.
 */

public class settingActivity extends PreferenceActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        addPreferencesFromResource(R.xml.settings);
    }
}
