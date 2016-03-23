package rtrk.pnrs.gameclock.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

import rtrk.pnrs.gameclock.R;


public class PreferencesFragment
    extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        getPreferenceScreen().getSharedPreferences()
                             .registerOnSharedPreferenceChangeListener(this);
        initSummary(getPreferenceScreen());
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
        String key)
    {
        Preference pref = findPreference(key);

        if (pref instanceof EditTextPreference)
            pref.setSummary(((EditTextPreference)pref).getText());
    }


    private void initSummary(Preference pref)
    {
        if (pref instanceof PreferenceGroup)
        {
            PreferenceGroup pg = (PreferenceGroup)pref;
            for (int i = 0; i < pg.getPreferenceCount(); i++)
                initSummary(pg.getPreference(i));
        }
        else if (pref instanceof EditTextPreference)
        {
            pref.setSummary(((EditTextPreference)pref).getText());
        }
    }
}
