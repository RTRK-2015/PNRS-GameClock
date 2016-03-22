package rtrk.pnrs.gameclock.data;


public class Time
{
    public int hour, min, sec, millisec;

    public Time(int hour, int min, int sec)
    {
        this(hour, min, sec, 0);
    }

    public Time(int hour, int min, int sec, int millisec)
    {
        this.hour     = hour;
        this.min      = min;
        this.sec      = sec;
        this.millisec = millisec;
    }


    public long toLong()
    {
        return 1000 * (hour * 3600 + min * 60 + sec) + millisec;
    }


    public static Time fromLong(long time)
    {
        int h = (int)(time / 3600 / 1000);
        time  %= 3600 * 1000;

        int m = (int)(time / 60 / 1000);
        time  %= 60 * 1000;

        int s = (int)(time / 1000);
        time  %= 1000;

        return new Time(h, m, s, (int)time);
    }


    @Override
    public String toString()
    {
        return String.format("%d:%d:%d", hour, min, sec);
    }
}
