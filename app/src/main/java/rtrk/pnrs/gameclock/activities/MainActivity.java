package rtrk.pnrs.gameclock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import rtrk.pnrs.gameclock.Common;
import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.data.Preferences;
import rtrk.pnrs.gameclock.data.Stats;
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

        // Disable controls
        Log.d(TAG, "Disabling player buttons");
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
            Common.ButtonState.DISABLED);
        modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
            Common.ButtonState.DISABLED);

        // Attach listeners
        Log.d(TAG, "Attaching onClick to buttons");
        attachOnClickListener(this, "btnMain_", TAG);
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
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.btnMain_Start:
            Log.d(TAG, "Enabling white controls");
            white.run();
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
                Common.ButtonState.ENABLED);
            modifyButtonState(
                (ViewGroup)findViewById(R.id.layMain_Settings),
                Common.ButtonState.DISABLED);
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
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
                Common.ButtonState.ENABLED);
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
                Common.ButtonState.DISABLED);
            break;

        case R.id.btnMain_WhiteLose:
            Log.d(TAG, "White lost");
            awaitingDraw = false;
            stats.blackWins += 1;
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
                Common.ButtonState.DISABLED);
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
                Common.ButtonState.DISABLED);
            modifyButtonState(
                (ViewGroup)findViewById(R.id.layMain_Settings),
                Common.ButtonState.ENABLED);
            break;

        case R.id.btnMain_WhiteDraw:
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
                Common.ButtonState.DISABLED);

            if (awaitingDraw)
            {
                Log.d(TAG, "White confirms the draw");
                awaitingDraw = false;
                stats.draws += 1;

                modifyButtonState(
                    (ViewGroup)findViewById(R.id.layMain_Settings),
                    Common.ButtonState.ENABLED);
            }
            else
            {
                Log.d(TAG, "White initiates a draw");
                awaitingDraw = true;

                modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
                    Common.ButtonState.ENABLED);
            }
            break;

        case R.id.btnMain_Black:
            Log.d(TAG, "Black passed the turn");
            awaitingDraw = false;
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
                Common.ButtonState.ENABLED);
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
                Common.ButtonState.DISABLED);
            break;

        case R.id.btnMain_BlackLose:
            Log.d(TAG, "Black lost");
            awaitingDraw = false;
            stats.whiteWins += 1;
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
                Common.ButtonState.DISABLED);
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
                Common.ButtonState.DISABLED);
            modifyButtonState(
                (ViewGroup)findViewById(R.id.layMain_Settings),
                Common.ButtonState.ENABLED);
            break;

        case R.id.btnMain_BlackDraw:
            modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
                Common.ButtonState.DISABLED);

            if (awaitingDraw)
            {
                Log.d(TAG, "Black confirms the draw");
                awaitingDraw = false;
                stats.draws += 1;

                modifyButtonState(
                    (ViewGroup)findViewById(R.id.layMain_Settings),
                    Common.ButtonState.ENABLED);
            }
            else
            {
                Log.d(TAG, "Black initiates a draw");
                awaitingDraw = true;

                modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
                    Common.ButtonState.ENABLED);
            }
            break;

        default:
            break;
        }
    }


    private static final String TAG = MainActivity.class.getSimpleName();
    private Stats stats;
    private Preferences prefs;
    private boolean awaitingDraw = false;

    private Handler handler = new Handler();
    private Runnable white = new Runnable()
    {
        @Override
        public void run()
        {
            handler.postDelayed(white, 500);
        }
    };
    private Runnable black = new Runnable()
    {
        @Override
        public void run()
        {
            handler.postDelayed(black, 500);
        }
    };
}
