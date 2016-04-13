package rtrk.pnrs.gameclock.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.adapters.StatAdapter;
import rtrk.pnrs.gameclock.data.Stat;
import rtrk.pnrs.gameclock.data.Stats;


public class StatsActivity
    extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        adapter = new StatAdapter(this);

        ListView listView = (ListView)findViewById(R.id.listStats);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        Stats stats = Stats.getStats(this);

        adapter.clear();

        for (Stat stat : stats.list)
            adapter.add(stat);
    }


    private StatAdapter adapter;
}
