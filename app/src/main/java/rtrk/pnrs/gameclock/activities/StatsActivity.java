package rtrk.pnrs.gameclock.activities;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import rtrk.pnrs.gameclock.R;
import rtrk.pnrs.gameclock.StatDBHelper;
import rtrk.pnrs.gameclock.StatsProvider;
import rtrk.pnrs.gameclock.adapters.StatAdapter;
import rtrk.pnrs.gameclock.data.Stat;
import rtrk.pnrs.gameclock.data.Time;


public class StatsActivity
    extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        resolver = getContentResolver();
        adapter = new StatAdapter(this);

        ListView listView = (ListView)findViewById(R.id.listStats);
        listView.setAdapter(adapter);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.clear();

        Cursor cursor = resolver.query(StatsProvider.CONTENT_URI, null, null, null, null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex(StatDBHelper.ID));
            String white = cursor.getString(cursor.getColumnIndex(StatDBHelper.WHITE_TIME));
            String black = cursor.getString(cursor.getColumnIndex(StatDBHelper.BLACK_TIME));

            adapter.add(new Stat(Time.fromLong(Long.valueOf(white)),
                Time.fromLong(Long.valueOf(black)), id));
        }

        cursor.close();
    }


    private StatAdapter adapter;
    private ContentResolver resolver;
}
