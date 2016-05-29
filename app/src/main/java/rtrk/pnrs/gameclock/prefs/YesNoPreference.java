package rtrk.pnrs.gameclock.prefs;


import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import rtrk.pnrs.gameclock.StatsProvider;


public class YesNoPreference
    extends DialogPreference
{
    public YesNoPreference(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }


    @Override
    protected void onDialogClosed(boolean result)
    {
        if (result)
            getContext().getContentResolver().delete(StatsProvider.CONTENT_URI, null, null);
    }
}
