package rtrk.pnrs.gameclock.data;


import java.io.Serializable;

public class Time
    implements Serializable
{
    public int h, m, s, millisec;

    public Time(int h, int m, int s)
    {
        this(h, m, s, 0);
    }

    public Time(int h, int m, int s, int millisec)
    {
        this.h = h;
        this.m = m;
        this.s = s;
        this.millisec = millisec;
    }


    public long toLong()
    {
        return 1000 * ((long) h * 3600 + m * 60 + s) + millisec;
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
        return String.format("%d:%d:%d", h, m, s);
    }
}
