package rtrk.pnrs.gameclock;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;


public class GameClockService
    extends Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        if (binder == null)
            binder = new GameClockBinder();

        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent)
    {
        try
        {
            binder.stop();
        }
        catch (RemoteException e)
        {

        }

        binder = null;
        return super.onUnbind(intent);
    }


    private GameClockBinder binder;
}
