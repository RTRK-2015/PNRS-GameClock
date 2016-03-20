package rtrk.pnrs.gameclock.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import rtrk.pnrs.gameclock.R;


public class PreferencesFragment
    extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
