package rtrk.pnrs.gameclock.data;


import java.io.Serializable;

public class Stat
    implements Serializable
{
    public Stat(Time whiteLeft, Time blackLeft, Won won)
    {
        this.blackLeft = blackLeft;
        this.whiteLeft = whiteLeft;
        this.won = won;
    }

    public Won getWon()
    {
        return won;
    }

    public Time getBlackLeft()
    {
        return blackLeft;
    }

    public Time getWhiteLeft()
    {
        return whiteLeft;
    }


    public enum Won
    {
        BLACK,
        WHITE,
        DRAW
    }

    private Won won;
    private Time blackLeft, whiteLeft;
}
