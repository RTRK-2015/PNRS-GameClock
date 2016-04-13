package rtrk.pnrs.gameclock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import rtrk.pnrs.gameclock.Common;
import rtrk.pnrs.gameclock.Common.ButtonState;
import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.data.Preferences;
import rtrk.pnrs.gameclock.data.Stat;
import rtrk.pnrs.gameclock.data.Stats;
import rtrk.pnrs.gameclock.data.Time;

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
        modifyWhiteControls(ButtonState.DISABLED);
        modifyBlackControls(ButtonState.DISABLED);

        // Attach listeners
        Log.d(TAG, "Attaching onClick to buttons");
        attachOnClickListener(this, "btnMain_", TAG);

        // Get preferences
        Log.d(TAG, "Getting preferences");
        stats = Stats.getStats(this);
        prefs = Preferences.getPreferences(this);
    }


    @Override
    public void onPause()
    {
        super.onPause();

        stopTimer();
        Stats.putStats(this, stats);
    }


    @Override
    public void onResume()
    {
        super.onResume();

        stats = Stats.getStats(this);
        prefs = Preferences.getPreferences(this);
    }


    @Override
    public void onClick(View v)
    {
        if (isGameOver)
        {
            isGameOver = false;

            findViewById(R.id.layMain_White).setVisibility(View.VISIBLE);
            findViewById(R.id.layMain_Black).setVisibility(View.VISIBLE);
            findViewById(R.id.txtMain_WhiteDraw).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_BlackDraw).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_WhiteLose).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_BlackLose).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_WhiteWin).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_BlackWin).setVisibility(View.INVISIBLE);
        }


        switch (v.getId())
        {
        case R.id.btnMain_Start:
            Log.d(TAG, "Enabling white controls");
            resetMainButtonsText();
            modifyWhiteControls(ButtonState.ENABLED);
            modifySettingsControls(ButtonState.DISABLED);
            lastWhite = Time.fromLong(System.currentTimeMillis());
            lastBlack = Time.fromLong(System.currentTimeMillis());
            white = prefs.whiteTime;
            black = prefs.blackTime;
            isWhiteTurn = true;
            timer.run();
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
                findViewById(R.id.txtMain_BlackDraw).setVisibility(View.INVISIBLE);
            }

            Log.d(TAG, "White passed the turn");
            awaitingDraw = false;
            modifyWhiteControls(ButtonState.DISABLED);
            modifyBlackControls(ButtonState.ENABLED);
            isWhiteTurn = false;
            lastBlack = Time.fromLong(System.currentTimeMillis());
            break;

        case R.id.btnMain_WhiteLose:
            Log.d(TAG, "White lost");
            whiteLose();
            break;

        case R.id.btnMain_WhiteDraw:
            modifyWhiteControls(ButtonState.DISABLED);

            if (awaitingDraw)
            {
                Log.d(TAG, "White confirms the draw");
                stopTimer();

                awaitingDraw = false;
                stats.list.add(new Stat(white, black, Stat.Won.DRAW));
                Stats.putStats(this, stats);

                isGameOver = true;

                modifySettingsControls(ButtonState.ENABLED);
                resetMainButtonsText();
            }
            else
            {
                Log.d(TAG, "White initiates a draw");
                awaitingDraw = true;

                modifyBlackControls(ButtonState.ENABLED);
                isWhiteTurn = false;
            }
            findViewById(R.id.layMain_White).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_WhiteDraw).setVisibility(View.VISIBLE);
            break;

        case R.id.btnMain_Black:
            if (awaitingDraw)
            {
                awaitingDraw = false;
                findViewById(R.id.layMain_White).setVisibility(View.VISIBLE);
                findViewById(R.id.txtMain_WhiteDraw).setVisibility(View.INVISIBLE);
            }

            Log.d(TAG, "Black passed the turn");
            awaitingDraw = false;
            modifyWhiteControls(ButtonState.ENABLED);
            modifyBlackControls(ButtonState.DISABLED);
            isWhiteTurn = true;
            lastWhite = Time.fromLong(System.currentTimeMillis());
            break;

        case R.id.btnMain_BlackLose:
            Log.d(TAG, "Black lost");
            blackLose();
            break;

        case R.id.btnMain_BlackDraw:
            modifyBlackControls(ButtonState.DISABLED);

            if (awaitingDraw)
            {
                Log.d(TAG, "Black confirms the draw");
                stopTimer();

                awaitingDraw = false;
                stats.list.add(new Stat(white, black, Stat.Won.DRAW));
                Stats.putStats(this, stats);

                isGameOver = true;

                modifySettingsControls(ButtonState.ENABLED);
                resetMainButtonsText();
            }
            else
            {
                Log.d(TAG, "Black initiates a draw");
                awaitingDraw = true;

                modifyWhiteControls(ButtonState.ENABLED);
                isWhiteTurn = true;
            }
            findViewById(R.id.layMain_Black).setVisibility(View.INVISIBLE);
            findViewById(R.id.txtMain_BlackDraw).setVisibility(View.VISIBLE);
            break;

        default:
            break;
        }
    }


    private void modifyWhiteControls(Common.ButtonState state)
    {
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
            state);
    }


    private void modifyBlackControls(Common.ButtonState state)
    {
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
            state);
    }


    private void modifySettingsControls(Common.ButtonState state)
    {
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_Settings),
            state);
    }


    private void resetMainButtonsText()
    {
        ((Button)findViewById(R.id.btnMain_Black)).setText(
            getString(R.string.btnMain_Black));

        ((Button)findViewById(R.id.btnMain_White)).setText(
            getString(R.string.btnMain_White));
    }


    private void whiteLose()
    {
        stopTimer();

        awaitingDraw = false;
        stats.list.add(new Stat(white, black, Stat.Won.BLACK));
        Stats.putStats(this, stats);

        modifyWhiteControls(ButtonState.DISABLED);
        modifyBlackControls(ButtonState.DISABLED);
        modifySettingsControls(ButtonState.ENABLED);

        findViewById(R.id.layMain_White).setVisibility(View.INVISIBLE);
        findViewById(R.id.layMain_Black).setVisibility(View.INVISIBLE);
        findViewById(R.id.txtMain_WhiteLose).setVisibility(View.VISIBLE);
        findViewById(R.id.txtMain_BlackWin).setVisibility(View.VISIBLE);
        isGameOver = true;

        resetMainButtonsText();
    }


    private void blackLose()
    {
        stopTimer();

        awaitingDraw = false;
        stats.list.add(new Stat(white, black, Stat.Won.WHITE));
        Stats.putStats(this, stats);

        modifyWhiteControls(ButtonState.DISABLED);
        modifyBlackControls(ButtonState.DISABLED);
        modifySettingsControls(ButtonState.ENABLED);

        findViewById(R.id.layMain_Black).setVisibility(View.INVISIBLE);
        findViewById(R.id.layMain_White).setVisibility(View.INVISIBLE);
        findViewById(R.id.txtMain_BlackLose).setVisibility(View.VISIBLE);
        findViewById(R.id.txtMain_WhiteWin).setVisibility(View.VISIBLE);
        isGameOver = true;

        resetMainButtonsText();
    }


    private void stopTimer()
    {
        handler.removeCallbacks(timer);
    }


    private static final String TAG = MainActivity.class.getSimpleName();
    private Stats stats;
    private Preferences prefs;
    private boolean awaitingDraw = false, isWhiteTurn = false;
    private boolean isGameOver = false;

    private Time lastWhite, lastBlack, white, black;
    private Handler handler = new Handler();
    private Runnable timer = new Runnable()
    {
        @Override
        public void run()
        {
            long current = System.currentTimeMillis();

            if (isWhiteTurn)
            {
                long elapsed = current - lastWhite.toLong();

                white = Time.fromLong(white.toLong() - elapsed);
                lastWhite = Time.fromLong(current);

                if (white.toLong() <= 0)
                    whiteLose();

                ((Button)findViewById(R.id.btnMain_White)).setText(
                    getString(R.string.btnMain_White) + "\n" + white);
            }
            else
            {
                long elapsed = current - lastBlack.toLong();

                black = Time.fromLong(black.toLong() - elapsed);
                lastBlack = Time.fromLong(current);

                if (black.toLong() <= 0)
                    blackLose();

                ((Button)findViewById(R.id.btnMain_Black)).setText(
                    getString(R.string.btnMain_Black) + "\n" + black);
            }

            handler.postDelayed(timer, 200);
        }
    };
}
