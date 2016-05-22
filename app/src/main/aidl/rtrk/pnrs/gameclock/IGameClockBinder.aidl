// IGameClock.aidl
package rtrk.pnrs.gameclock;


import rtrk.pnrs.gameclock.IGameTimeListener;


interface IGameClockBinder
{
    void start(long time, in IGameTimeListener listener);
    void stop();
    void turn();
    long getTime(int player);
}
