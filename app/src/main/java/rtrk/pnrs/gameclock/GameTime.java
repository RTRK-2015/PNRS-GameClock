package rtrk.pnrs.gameclock;


public class GameTime
{
    static
    {
        System.loadLibrary("dyngametime");
    }

    public static native long increaseTime(long time, long delta);
    public static native long decreaseTime(long time, long delta);
}
