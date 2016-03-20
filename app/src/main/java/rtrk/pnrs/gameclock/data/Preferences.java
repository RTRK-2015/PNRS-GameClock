package rtrk.pnrs.gameclock.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import rtrk.pnrs.gameclock.R;

public class Preferences
{
    public Time whiteTime, blackTime;

    public Preferences(Time whiteTime, Time blackTime)
    {
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
    }


    public static Preferences getPreferences(Context context)
    {
        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(context);


        // Store everything as a string. What could go wrong?
        int wh = trulyGetInt(context, R.string.prefApp_whiteHour,
            R.integer.defaultHour);
        int wm = trulyGetInt(context, R.string.prefApp_whiteMin,
            R.integer.defaultMin);
        int ws = trulyGetInt(context, R.string.prefApp_whiteSec,
            R.integer.defaultSec);

        int bh = trulyGetInt(context, R.string.prefApp_blackHour,
            R.integer.defaultHour);
        int bm = trulyGetInt(context, R.string.prefApp_blackMin,
            R.integer.defaultMin);
        int bs = trulyGetInt(context, R.string.prefApp_blackSec,
            R.integer.defaultSec);

        return new Preferences(new Time(wh, wm, ws), new Time(bh, bm, bs));
    }


    private static int trulyGetInt(Context context, int key, int defaultValueKey)
    {
        // Seal of quality
        SharedPreferences prefs =
            PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();

        return Integer.parseInt(
            prefs.getString(context.getString(key),
            Integer.toString(resources.getInteger(defaultValueKey))));
    }
}
