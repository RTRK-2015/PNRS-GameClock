package rtrk.pnrs.gameclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
	static final String TAG = MainActivity.class.getSimpleName();

	enum ButtonState
	{
		ENABLE,
		DISABLE
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Disable controls
		Log.i(TAG, "Disabling player buttons");
		modifyButtons((ViewGroup) findViewById(R.id.layBlack), ButtonState.DISABLE);
		modifyButtons((ViewGroup) findViewById(R.id.layWhite), ButtonState.DISABLE);

		// Attach listeners
		Log.i(TAG, "Attaching onClick listeners to buttons");
		attachOnClickListeners();
	}

	void modifyButtons(ViewGroup viewGroup, ButtonState state)
	{
		for (int i = 0; i < viewGroup.getChildCount(); i++)
		{
			View view = viewGroup.getChildAt(i);

			if (view instanceof Button)
				view.setEnabled(state == ButtonState.ENABLE);
			else if (view instanceof ViewGroup)
				modifyButtons((ViewGroup) view, state);
		}
	}


	void attachOnClickListeners()
	{
		try
		{
			for (Field f : R.id.class.getFields())
			{
				int modifiers = f.getModifiers();
				if (f.getName().startsWith("btn")
						&& Modifier.isPublic(modifiers)
						&& Modifier.isStatic(modifiers)
						&& Modifier.isFinal(modifiers))
				{
					findViewById(f.getInt(null)).setOnClickListener(this);
				}
			}
		}
		catch (IllegalAccessException ex)
		{
			Log.wtf(TAG, "Impossible");
			ex.printStackTrace();
		}
	}


	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.btnStart:
			Log.i(TAG, "Enabling white controls");
			modifyButtons((ViewGroup) findViewById(R.id.layWhite), ButtonState.ENABLE);
			modifyButtons((ViewGroup) findViewById(R.id.laySettings), ButtonState.DISABLE);
			break;

		default:
			break;
		}
	}
}
