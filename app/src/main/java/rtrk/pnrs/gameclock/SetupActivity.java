package rtrk.pnrs.gameclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import static rtrk.pnrs.gameclock.Common.attachOnClickListener;


public class SetupActivity
    extends AppCompatActivity
    implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // Attach listeners
        Log.d(TAG, "Attaching onClick to buttons");
        attachOnClickListener(this, "btnSetup_", TAG);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        case R.id.btnSetup_Cancel:
            finish();
            break;

        case R.id.btnSetup_OK:
            finish();
            break;

        default:
            break;
        }
    }


    private static final String TAG = SetupActivity.class.getSimpleName();
}
