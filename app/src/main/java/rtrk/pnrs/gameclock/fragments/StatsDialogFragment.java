package rtrk.pnrs.gameclock.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import org.jetbrains.annotations.NotNull;

import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.data.Stats;


public class StatsDialogFragment
    extends AppCompatDialogFragment
{
    @Override
    @NotNull
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Stats stats = Stats.getStats(getActivity());

        String[] strings =
            {
                "White wins: " + stats.whiteWins,
                "Black wins: " + stats.blackWins,
                "Draws: " + stats.draws
            };

        builder.setTitle(R.string.txtStats_Title).setItems(strings,
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });


        return builder.create();
    }
}
