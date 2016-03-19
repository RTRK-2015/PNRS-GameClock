package rtrk.pnrs.gameclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btnMain_Start:
			Log.d(TAG, "Enabling white controls");
			modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
				Common.ButtonState.ENABLED);
			modifyButtonState((ViewGroup)findViewById(R.id.layMain_Settings),
				Common.ButtonState.DISABLED);
			break;

        case R.id.btnMain_Setup:
            Log.d(TAG, "Entering setup");
            startActivity(new Intent(this, SetupActivity.class));
            break;

		case R.id.btnMain_White:
			Log.d(TAG, "White passed the turn");
			modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
				Common.ButtonState.ENABLED);
			modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
				Common.ButtonState.DISABLED);
			break;

		case R.id.btnMain_Black:
			Log.d(TAG, "Black passed the turn");
			modifyButtonState((ViewGroup)findViewById(R.id.layMain_White),
				Common.ButtonState.ENABLED);
			modifyButtonState((ViewGroup)findViewById(R.id.layMain_Black),
				Common.ButtonState.DISABLED);
			break;

		default:
			break;
		}
	}


	private static final String TAG = MainActivity.class.getSimpleName();
}
