package rtrk.pnrs.gameclock.data;


import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class Stats
{
    public ArrayList<Stat> list;


    public Stats()
    {
        list = new ArrayList<>();
    }

    public static void putStats(Context context, Stats stats)
    {
        try
        {
            FileOutputStream fos =
                context.openFileOutput("stats", Context.MODE_PRIVATE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(stats.list);

            oos.close();
            fos.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    public static Stats getStats(Context context)
    {
        try
        {
            FileInputStream fis = context.openFileInput("stats");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Stats stats = new Stats();
            stats.list = (ArrayList<Stat>)ois.readObject();

            ois.close();
            fis.close();

            return stats;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return new Stats();
        }
    }
}
