package rtrk.pnrs.gameclock;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class Common
{
    public enum ButtonState
    {
        ENABLED,
        DISABLED
    }


    public static void modifyButtonState(ViewGroup viewGroup, ButtonState state)
    {
        if (viewGroup == null || state == null)
            return;

        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View view = viewGroup.getChildAt(i);

            if (view instanceof Button)
                view.setEnabled(state == ButtonState.ENABLED);
            else if (view instanceof ViewGroup)
                modifyButtonState((ViewGroup)view, state);
        }
    }


    public static <T extends AppCompatActivity & View.OnClickListener>
    void attachOnClickListener(T activity, String prefix, String TAG)
    {
        for (Field f : R.id.class.getFields())
        {
            int modifiers = f.getModifiers();
            if (f.getName().startsWith(prefix)
                && Modifier.isPublic(modifiers)
                && Modifier.isStatic(modifiers)
                && Modifier.isFinal(modifiers)
                && f.getType().equals(int.class))
            {
                try
                {
                    activity.findViewById(f.getInt(null))
                             .setOnClickListener(activity);
                }
                catch (IllegalAccessException|NullPointerException ex)
                {
                    Log.wtf(TAG, "Impossible: '" + f.getName() +
                        "' is declared public static final, but inaccessible");
                    ex.printStackTrace();
                }
            }
        }
    }


    private Common() { }
}
