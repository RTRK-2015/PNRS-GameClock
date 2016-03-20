package rtrk.pnrs.gameclock.data;


public class Time
{
    public int hour, min, sec;

    public Time(int hour, int min, int sec)
    {
        this.hour = hour;
        this.min  = min;
        this.sec  = sec;
    }


    public long toLong()
    {
        return hour * 3600 + min * 60 + sec;
    }


    public static Time fromLong(long time)
    {
        int h = (int)(time / 3600);
        time  %= 3600;

        int m = (int)(time / 60);
        time  %= 60;

        return new Time(h, m, (int)time);
    }


    @Override
    public String toString()
    {
        return String.format("%d:%d:%d", hour, min, sec);
    }
}
