package rtrk.pnrs.gameclock.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import rtrk.pnrs.gameclock.R;


public class Stats
{
    public int whiteWins, blackWins, draws;

    public Stats(int whiteWins, int blackWins, int draws)
    {
        this.whiteWins = whiteWins;
        this.blackWins = blackWins;
        this.draws     = draws;
    }


    @NonNull
    public static Stats getStats(@NonNull Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(
            context.getString(R.string.prefStats_name), Context.MODE_PRIVATE);

        int whiteWins   = preferences.getInt(
            context.getString(R.string.prefStats_whiteWins), 0),
            blackWins   = preferences.getInt(
                context.getString(R.string.prefStats_blackWins), 0),
            draws       = preferences.getInt(
                context.getString(R.string.prefStats_draws), 0);

        return new Stats(whiteWins, blackWins, draws);
    }


    public static void putStats(@NonNull Context context, @NonNull Stats stats)
    {
        SharedPreferences preferences = context.getSharedPreferences(
            context.getString(R.string.prefStats_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(context.getString(R.string.prefStats_whiteWins),
            stats.whiteWins);
        editor.putInt(context.getString(R.string.prefStats_blackWins),
            stats.blackWins);
        editor.putInt(context.getString(R.string.prefStats_draws),
            stats.draws);
        editor.apply();
    }
}
