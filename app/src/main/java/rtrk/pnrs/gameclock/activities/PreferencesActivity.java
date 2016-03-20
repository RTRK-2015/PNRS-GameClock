package rtrk.pnrs.gameclock.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.fragments.PreferencesFragment;


public class PreferencesActivity
    extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, new PreferencesFragment())
                            .commit();
    }
}
