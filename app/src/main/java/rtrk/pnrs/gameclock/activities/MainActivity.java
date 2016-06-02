package rtrk.pnrs.gameclock.activities;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rtrk.pnrs.gameclock.Common.State;
import rtrk.pnrs.gameclock.GameClockBinder;
import rtrk.pnrs.gameclock.GameClockService;
import rtrk.pnrs.gameclock.IGameClockBinder;
import rtrk.pnrs.gameclock.IGameTimeListener;
import rtrk.pnrs.gameclock.IGameTimeListener.Stub;
import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.StatDBHelper;
import rtrk.pnrs.gameclock.StatsProvider;
import rtrk.pnrs.gameclock.data.Preferences;
import rtrk.pnrs.gameclock.data.Time;
import rtrk.pnrs.gameclock.views.AnalogClockView;

import static rtrk.pnrs.gameclock.Common.attachOnClickListener;
import static rtrk.pnrs.gameclock.Common.modifyButtonState;


public class MainActivity
    extends AppCompatActivity
    implements View.OnClickListener, ServiceConnection
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
        prefs = Preferences.getPreferences(this);

        white = prefs.whiteTime;
        black = white;


        Intent intent = new Intent(this, GameClockService.class);

        if (!bindService(intent, this, BIND_AUTO_CREATE))
        {
            Log.e(TAG, "Binding failed");
        }

        resolver = getContentResolver();

        setClockTimes();
    }


    @Override
    public void onResume()
    {
        super.onResume();

        prefs = Preferences.getPreferences(this);
        setClockTimes();
    }


    @Override
    public void onDestroy()
    {
        if (service != null)
            unbindService(this);

        super.onDestroy();
    }


    @Override
    public void onClick(View v)
    {
        try
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
                white = prefs.whiteTime;
                black = white;
                service.start(white.toLong(), callback);
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
                service.turn();
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
                    isGameOver = true;
                    service.stop();
                    modifySettingsControls(State.ENABLED);

                    ContentValues values = new ContentValues();
                    values.put(StatDBHelper.ID, 0);
                    values.put(StatDBHelper.BLACK_TIME, service.getTime(GameClockBinder.BLACK_PLAYER_ID));
                    values.put(StatDBHelper.WHITE_TIME, service.getTime(GameClockBinder.WHITE_PLAYER_ID));
                    resolver.insert(StatsProvider.CONTENT_URI, values);
                }
                else
                {
                    Log.d(TAG, "White initiates a draw");
                    awaitingDraw = true;
                    service.turn();
                    modifyBlackControls(State.ENABLED);
                }
                findViewById(R.id.layMain_White).setVisibility(View.INVISIBLE);

                TextView txtw = (TextView) findViewById(R.id.txtMain_White);
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
                service.turn();
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
                    isGameOver = true;
                    service.stop();
                    modifySettingsControls(State.ENABLED);

                    ContentValues values = new ContentValues();
                    values.put(StatDBHelper.ID, 0);
                    values.put(StatDBHelper.BLACK_TIME, service.getTime(GameClockBinder.BLACK_PLAYER_ID));
                    values.put(StatDBHelper.WHITE_TIME, service.getTime(GameClockBinder.WHITE_PLAYER_ID));
                    resolver.insert(StatsProvider.CONTENT_URI, values);
                }
                else
                {
                    Log.d(TAG, "Black initiates a draw");
                    service.turn();
                    awaitingDraw = true;
                    modifyWhiteControls(State.ENABLED);
                }
                findViewById(R.id.layMain_Black).setVisibility(View.INVISIBLE);

                TextView txtb = (TextView) findViewById(R.id.txtMain_Black);
                txtb.setVisibility(View.VISIBLE);
                txtb.setText(resources.getString(R.string.draw));
                txtb.setTextColor(resources.getColor(R.color.draw));
                break;

            default:
                break;
            }
        }
        catch (RemoteException ex)
        {

        }
    }


    private void setClockTimes()
    {
        AnalogClockView whitev = (AnalogClockView)findViewById(R.id.btnMain_White);
        white.h -= 6;
        whitev.setTime(white);

        AnalogClockView blackv = (AnalogClockView)findViewById(R.id.btnMain_Black);
        blackv.setTime(white);
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

        try
        {
            service.stop();

            ContentValues values = new ContentValues();
            values.put(StatDBHelper.ID, GameClockBinder.BLACK_PLAYER_ID);
            values.put(StatDBHelper.BLACK_TIME, service.getTime(GameClockBinder.BLACK_PLAYER_ID));
            values.put(StatDBHelper.WHITE_TIME, service.getTime(GameClockBinder.WHITE_PLAYER_ID));
            resolver.insert(StatsProvider.CONTENT_URI, values);
        }
        catch (RemoteException ex)
        {

        }

        isGameOver = true;
    }


    private void blackLose()
    {
        awaitingDraw = false;

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

        try
        {
            service.stop();

            ContentValues values = new ContentValues();
            values.put(StatDBHelper.ID, GameClockBinder.WHITE_PLAYER_ID);
            values.put(StatDBHelper.BLACK_TIME, service.getTime(GameClockBinder.BLACK_PLAYER_ID));
            values.put(StatDBHelper.WHITE_TIME, service.getTime(GameClockBinder.WHITE_PLAYER_ID));
            resolver.insert(StatsProvider.CONTENT_URI, values);
        }
        catch (RemoteException ex)
        {

        }


        isGameOver = true;
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder)
    {
        service = IGameClockBinder.Stub.asInterface(iBinder);
    }


    @Override
    public void onServiceDisconnected(ComponentName componentName)
    {
        service = null;
    }



    private final IGameTimeListener.Stub callback = new Stub()
    {
        @Override
        public void onTimeChange(int player, long time) throws RemoteException
        {
            Time t = Time.fromLong(time);
            t.h -= 6;

            if (player == GameClockBinder.WHITE_PLAYER_ID)
            {
                AnalogClockView white = (AnalogClockView)findViewById(R.id.btnMain_White);
                white.setTime(t);
            }
            else
            {
                AnalogClockView black = (AnalogClockView)findViewById(R.id.btnMain_Black);
                black.setTime(t);
            }
        }


        @Override
        public void onTimesUp(int player) throws RemoteException
        {
            if (player == GameClockBinder.WHITE_PLAYER_ID)
                whiteLose();
            else
                blackLose();
        }
    };


    private static final String TAG = MainActivity.class.getSimpleName();
    private Preferences prefs;
    private boolean awaitingDraw = false;
    private boolean isGameOver = false;
    private Resources resources;
    private IGameClockBinder service;
    private Time white, black;
    private ContentResolver resolver;
}
