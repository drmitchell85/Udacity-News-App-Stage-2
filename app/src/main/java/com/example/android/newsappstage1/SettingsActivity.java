package com.example.android.newsappstage1;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    /**
     * Step 1:
     * Build onCreate() and attach it to settings_activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    /**
     * Step 2:
     * Build the NewsPreferenceFragment
     * - implements Preference.OnPreferenceChangeListener
     */
    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        /**
         * Eventually add preference display keys
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference pageSize = findPreference(getString(R.string.settings_page_size_key));
            bindPreferencesSummaryToValue(pageSize);

            Preference category = findPreference(getString(R.string.settings_category_key));
            bindPreferencesSummaryToValue(category);

        }


        /**
         * Step 3:
         * This method will be called when the user has changed a preference, so inside of it
         * we should add whatever action we wnt to happen after this change. We want the UI to
         * display the preference summary
         * - Requires two if statements
         */

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            preference.setSummary(stringValue);
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if(prefIndex >=0){
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        /**
         * Step 4:
         * create a bindPreferenceSummaryToValue() method, which takes in a Preference
         * parameter then use setOnPreferenceChangeListener to set the current PreferenceFragment
         * instance to listen for changes to the preference we pass in using:
         *  - preference.setOnPreferenceChangeListener(this);
         *  - also read the current value of the preference stored and display it
         */
        private void bindPreferencesSummaryToValue(Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
