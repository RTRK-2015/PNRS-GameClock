package rtrk.pnrs.gameclock.data;


import android.content.Context;
import android.content.SharedPreferences;

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
        SharedPreferences preferences = context.getSharedPreferences(
            context.getString(R.string.prefApp_name), Context.MODE_PRIVATE);

        int wh = preferences.getInt(
            context.getString(R.string.prefApp_whiteHour), 1);
        int wm = preferences.getInt(
            context.getString(R.string.prefApp_whiteMin), 0);
        int ws = preferences.getInt(
            context.getString(R.string.prefApp_whiteSec), 0);

        int bh = preferences.getInt(
            context.getString(R.string.prefApp_blackHour), 1);
        int bm = preferences.getInt(
            context.getString(R.string.prefApp_blackMin), 0);
        int bs = preferences.getInt(
            context.getString(R.string.prefApp_blackSec), 0);

        return new Preferences(new Time(wh, wm, ws), new Time(bh, bm, bs));
    }
}
