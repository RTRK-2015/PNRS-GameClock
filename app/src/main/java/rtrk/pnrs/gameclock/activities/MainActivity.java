package rtrk.pnrs.gameclock.activities;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rtrk.pnrs.gameclock.Common.State;
import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.data.Preferences;
import rtrk.pnrs.gameclock.data.Stat;
import rtrk.pnrs.gameclock.data.Stats;
import rtrk.pnrs.gameclock.data.Time;
import rtrk.pnrs.gameclock.views.AnalogClockView;

import static rtrk.pnrs.gameclock.Common.attachOnClickListener;
import static rtrk.pnrs.gameclock.Common.modifyButtonState;


public class MainActivity
    extends AppCompatActivity
    implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Disable player controls
        Log.d(TAG, "Disabling player buttons");
        modifyWhiteControls(State.DISABLED);
        modifyBlackControls(State.DISABLED);

        // Attach listeners
        Log.d(TAG, "Attaching onClick to buttons");
        attachOnClickListener(this, "btnMain_", TAG);

        // Get resources
        resources = getResources();

        // Get preferences
        Log.d(TAG, "Getting preferences");
        stats = Stats.getStats(this);
        prefs = Preferences.getPreferences(this);

        setClockTimes(prefs);
    }


    @Override
    public void onPause()
    {
        super.onPause();

        Stats.putStats(this, stats);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        stats = Stats.getStats(this);
        prefs = Preferences.getPreferences(this);
        setClockTimes(prefs);
    }


    @Override
    public void onClick(View v)
    {
        if (isGameOver)
        {
            isGameOver = false;

            findViewById(R.id.layMain_White).setVisibility(View.VISIBLE);
            findViewById(R.id.layMain_Black).setVisibility(View.VISIBLE);
            findViewById(R.id.txtMain_White).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_Black).setVisibility(View.INVISIBLE);
        }


        switch (v.getId())
        {
        case R.id.btnMain_Start:
            Log.d(TAG, "Enabling white controls");
            modifyWhiteControls(State.ENABLED);
            modifySettingsControls(State.DISABLED);
            lastWhite = Time.fromLong(System.currentTimeMillis());
            lastBlack = Time.fromLong(System.currentTimeMillis());
            white = prefs.whiteTime;
            black = prefs.blackTime;
            isWhiteTurn = true;
            break;

        case R.id.btnMain_Setup:
            Log.d(TAG, "Entering setup");
            startActivity(new Intent(this, PreferencesActivity.class));
            break;

        case R.id.btnMain_Stats:
            Log.d(TAG, "Showing stats");
            startActivity(new Intent(this, StatsActivity.class));
            break;

        case R.id.btnMain_White:
            if (awaitingDraw)
            {
                awaitingDraw = false;

                findViewById(R.id.layMain_Black).setVisibility(View.VISIBLE);
                findViewById(R.id.txtMain_Black).setVisibility(View.INVISIBLE);
            }

            Log.d(TAG, "White passed the turn");
            awaitingDraw = false;
            modifyWhiteControls(State.DISABLED);
            modifyBlackControls(State.ENABLED);
            isWhiteTurn = false;
            lastBlack = Time.fromLong(System.currentTimeMillis());
            break;

        case R.id.btnMain_WhiteLose:
            Log.d(TAG, "White lost");
            whiteLose();
            break;

        case R.id.btnMain_WhiteDraw:
            modifyWhiteControls(State.DISABLED);

            if (awaitingDraw)
            {
                Log.d(TAG, "White confirms the draw");

                awaitingDraw = false;
                stats.list.add(new Stat(white, black, Stat.Won.DRAW));
                Stats.putStats(this, stats);

                isGameOver = true;

                modifySettingsControls(State.ENABLED);
            }
            else
            {
                Log.d(TAG, "White initiates a draw");
                awaitingDraw = true;

                modifyBlackControls(State.ENABLED);
                isWhiteTurn = false;
            }
            findViewById(R.id.layMain_White).setVisibility(View.INVISIBLE);

            TextView txtw = (TextView)findViewById(R.id.txtMain_White);
            txtw.setVisibility(View.VISIBLE);
            txtw.setText(resources.getString(R.string.draw));
            txtw.setTextColor(resources.getColor(R.color.draw));
            break;

        case R.id.btnMain_Black:
            if (awaitingDraw)
            {
                awaitingDraw = false;
                findViewById(R.id.layMain_White).setVisibility(View.VISIBLE);
                findViewById(R.id.txtMain_White).setVisibility(View.INVISIBLE);
            }

            Log.d(TAG, "Black passed the turn");
            awaitingDraw = false;
            modifyWhiteControls(State.ENABLED);
            modifyBlackControls(State.DISABLED);
            isWhiteTurn = true;
            lastWhite = Time.fromLong(System.currentTimeMillis());
            break;

        case R.id.btnMain_BlackLose:
            Log.d(TAG, "Black lost");
            blackLose();
            break;

        case R.id.btnMain_BlackDraw:
            modifyBlackControls(State.DISABLED);

            if (awaitingDraw)
            {
                Log.d(TAG, "Black confirms the draw");

                awaitingDraw = false;
                stats.list.add(new Stat(white, black, Stat.Won.DRAW));
                Stats.putStats(this, stats);

                isGameOver = true;

                modifySettingsControls(State.ENABLED);
            }
            else
            {
                Log.d(TAG, "Black initiates a draw");
                awaitingDraw = true;

                modifyWhiteControls(State.ENABLED);
                isWhiteTurn = true;
            }
            findViewById(R.id.layMain_Black).setVisibility(View.INVISIBLE);

            TextView txtb = (TextView)findViewById(R.id.txtMain_Black);
            txtb.setVisibility(View.VISIBLE);
            txtb.setText(resources.getString(R.string.draw));
            txtb.setTextColor(resources.getColor(R.color.draw));
            break;

        default:
            break;
        }
    }


    private void setClockTimes(Preferences prefs)
    {
        AnalogClockView white = (AnalogClockView)findViewById(R.id.btnMain_White);
        white.setH(prefs.whiteTime.h % 12);
        white.setM(prefs.whiteTime.m % 60);
        white.setS(prefs.whiteTime.s % 60);

        AnalogClockView black = (AnalogClockView)findViewById(R.id.btnMain_Black);
        black.setH(prefs.blackTime.h % 12);
        black.setM(prefs.blackTime.m % 60);
        black.setS(prefs.blackTime.s % 60);
    }


    private void modifyWhiteControls(State state)
    {
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
            state);
    }


    private void modifyBlackControls(State state)
    {
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
            state);
    }


    private void modifySettingsControls(State state)
    {
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_Settings),
            state);
    }


    private void whiteLose()
    {
        awaitingDraw = false;
        stats.list.add(new Stat(white, black, Stat.Won.BLACK));
        Stats.putStats(this, stats);

        modifyWhiteControls(State.DISABLED);
        modifyBlackControls(State.DISABLED);
        modifySettingsControls(State.ENABLED);

        findViewById(R.id.layMain_White).setVisibility(View.INVISIBLE);
        findViewById(R.id.layMain_Black).setVisibility(View.INVISIBLE);

        TextView txtw = (TextView)findViewById(R.id.txtMain_White);
        txtw.setVisibility(View.VISIBLE);
        txtw.setText(resources.getString(R.string.lose));
        txtw.setTextColor(resources.getColor(R.color.lose));

        TextView txtb = (TextView)findViewById(R.id.txtMain_Black);
        txtb.setVisibility(View.VISIBLE);
        txtb.setText(resources.getString(R.string.win));
        txtb.setTextColor(resources.getColor(R.color.win));

        isGameOver = true;
    }


    private void blackLose()
    {
        awaitingDraw = false;
        stats.list.add(new Stat(white, black, Stat.Won.WHITE));
        Stats.putStats(this, stats);

        modifyWhiteControls(State.DISABLED);
        modifyBlackControls(State.DISABLED);
        modifySettingsControls(State.ENABLED);

        findViewById(R.id.layMain_Black).setVisibility(View.INVISIBLE);
        findViewById(R.id.layMain_White).setVisibility(View.INVISIBLE);

        TextView txtb = (TextView)findViewById(R.id.txtMain_Black);
        txtb.setVisibility(View.VISIBLE);
        txtb.setText(resources.getString(R.string.lose));
        txtb.setTextColor(resources.getColor(R.color.lose));

        TextView txtw = (TextView)findViewById(R.id.txtMain_White);
        txtw.setVisibility(View.VISIBLE);
        txtw.setText(resources.getString(R.string.win));
        txtw.setTextColor(resources.getColor(R.color.win));

        isGameOver = true;
    }



    private static final String TAG = MainActivity.class.getSimpleName();
    private Stats stats;
    private Preferences prefs;
    private boolean awaitingDraw = false, isWhiteTurn = false;
    private boolean isGameOver = false;
    private Resources resources;

    private Time lastWhite, lastBlack, white, black;
}
