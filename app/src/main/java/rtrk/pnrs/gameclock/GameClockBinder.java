package rtrk.pnrs.gameclock;


import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import rtrk.pnrs.gameclock.data.Time;


public class GameClockBinder
    extends IGameClockBinder.Stub
{
    public static final int WHITE_PLAYER_ID = 1;
    public static final int BLACK_PLAYER_ID = 2;


    @Override
    public void start(long time, IGameTimeListener listener) throws RemoteException
    {
        whiteTime = Time.fromLong(time);
        blackTime = Time.fromLong(time);
        this.listener = listener;
        handler = new Handler(Looper.getMainLooper());
        whitesTurn = true;

        caller.run();
    }


    @Override
    public void stop() throws RemoteException
    {
        handler.removeCallbacks(caller);
        handler = null;
    }


    @Override
    public void turn() throws RemoteException
    {
        synchronized (mutex)
        {
            whitesTurn = !whitesTurn;
        }
    }


    @Override
    public long getTime(int player) throws RemoteException
    {
        if (player == WHITE_PLAYER_ID)
            return whiteTime.toLong();
        else if (player == BLACK_PLAYER_ID)
            return blackTime.toLong();
        else
            return -1;
    }


    private Time whiteTime, blackTime;
    private IGameTimeListener listener;
    private Handler handler;
    private boolean whitesTurn;

    private final Object mutex = new Object();
    private final Runnable caller = new Runnable()
    {
        @Override
        public void run()
        {
            try
            {
                synchronized (mutex)
                {
                    if (whitesTurn)
                    {
                        if (whiteTime.toLong() <= 0)
                        {
                            listener.onTimesUp(WHITE_PLAYER_ID);
                        }
                        else
                        {
                            whiteTime = Time.fromLong(whiteTime.toLong() - second.toLong());
                            listener.onTimeChange(WHITE_PLAYER_ID, whiteTime.toLong());
                        }
                    }
                    else
                    {
                        if (blackTime.toLong() <= 0)
                        {
                            listener.onTimesUp(BLACK_PLAYER_ID);
                        }
                        else
                        {
                            blackTime = Time.fromLong(blackTime.toLong() - second.toLong());
                            listener.onTimeChange(BLACK_PLAYER_ID, blackTime.toLong());
                        }
                    }
                }

                handler.postDelayed(caller, 1000);
            }
            catch (RemoteException ex)
            {

            }
        }


        private final Time second = new Time(0, 0, 1);
    };
}
