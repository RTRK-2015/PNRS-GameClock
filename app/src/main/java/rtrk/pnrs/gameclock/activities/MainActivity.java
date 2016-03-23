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
import rtrk.pnrs.gameclock.data.Stats;
import rtrk.pnrs.gameclock.data.Time;
import rtrk.pnrs.gameclock.fragments.StatsDialogFragment;

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
        switch (v.getId())
        {
        case R.id.btnMain_Start:
            Log.d(TAG, "Enabling white controls");
            resetMainButtonsText();
            modifyWhiteControls(ButtonState.ENABLED);
            modifySettingsControls(ButtonState.DISABLED);
            lastWhite = Time.fromLong(System.currentTimeMillis());
            lastBlack = null;
            white = prefs.whiteTime;
            black = prefs.blackTime;
            isWhiteTurn = true;
            timeRunnable.run();
            break;

        case R.id.btnMain_Setup:
            Log.d(TAG, "Entering setup");
            startActivity(new Intent(this, PreferencesActivity.class));
            break;

        case R.id.btnMain_Stats:
            Log.d(TAG, "Showing stats");
            new StatsDialogFragment().show(getSupportFragmentManager(),
                "stats");
            break;

        case R.id.btnMain_White:
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
                awaitingDraw = false;
                stats.draws += 1;

                modifySettingsControls(ButtonState.ENABLED);
                resetMainButtonsText();
                stopTimer();
            }
            else
            {
                Log.d(TAG, "White initiates a draw");
                awaitingDraw = true;

                modifyBlackControls(ButtonState.ENABLED);
                isWhiteTurn = false;
            }
            break;

        case R.id.btnMain_Black:
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
                awaitingDraw = false;
                stats.draws += 1;

                modifySettingsControls(ButtonState.ENABLED);
                resetMainButtonsText();
                stopTimer();
            }
            else
            {
                Log.d(TAG, "Black initiates a draw");
                awaitingDraw = true;

                modifyWhiteControls(ButtonState.ENABLED);
                isWhiteTurn = true;
            }
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
        awaitingDraw = false;
        stats.blackWins += 1;
        stopTimer();
        modifyWhiteControls(ButtonState.DISABLED);
        modifyBlackControls(ButtonState.DISABLED);
        modifySettingsControls(ButtonState.ENABLED);
        resetMainButtonsText();
        Stats.putStats(this, stats);
    }


    private void blackLose()
    {
        awaitingDraw = false;
        stats.whiteWins += 1;
        modifyWhiteControls(ButtonState.DISABLED);
        modifyBlackControls(ButtonState.DISABLED);
        modifySettingsControls(ButtonState.ENABLED);
        resetMainButtonsText();
        stopTimer();
        Stats.putStats(this, stats);
    }


    private void stopTimer()
    {
        handler.removeCallbacks(timeRunnable);
    }


    private static final String TAG = MainActivity.class.getSimpleName();
    private Stats stats;
    private Preferences prefs;
    private boolean awaitingDraw = false, isWhiteTurn = false;

    private Time lastWhite, lastBlack, white, black;
    private Handler handler = new Handler();
    private Runnable timeRunnable = new Runnable()
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

            handler.postDelayed(timeRunnable, 200);
        }
    };
}
