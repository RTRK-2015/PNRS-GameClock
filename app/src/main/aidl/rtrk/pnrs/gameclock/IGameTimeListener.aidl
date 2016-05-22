package rtrk.pnrs.gameclock;



interface IGameTimeListener
{
    void onTimeChange(int player, long time);
    void onTimesUp(int player);
}
