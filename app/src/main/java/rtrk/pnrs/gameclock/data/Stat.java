package rtrk.pnrs.gameclock.data;


import java.io.Serializable;

public class Stat
    implements Serializable
{
    public int won;
    public Time blackLeft, whiteLeft;


    public Stat(Time whiteLeft, Time blackLeft, int won)
    {
        this.blackLeft = blackLeft;
        this.whiteLeft = whiteLeft;
        this.won = won;
    }
}
