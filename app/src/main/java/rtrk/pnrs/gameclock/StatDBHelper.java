package rtrk.pnrs.gameclock;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collection;

import rtrk.pnrs.gameclock.data.Stat;
import rtrk.pnrs.gameclock.data.Time;


public class StatDBHelper
    extends SQLiteOpenHelper
{
    public static final String
        TABLE = "Stats",
        ID = "id",
        WHITE_TIME = "whiteTime",
        BLACK_TIME = "blackTime";


    public StatDBHelper(Context context)
    {
        super(context, DATABASE, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String sql =
            String.format("CREATE TABLE %s", TABLE) +
            String.format("( %s INTEGER CHECK(\"id\" in (0, 1, 2))", ID) +
            String.format(", %s TEXT", WHITE_TIME) +
            String.format(", %s TEXT", BLACK_TIME) +
            ");";

        sqLiteDatabase.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }


    public void insert(Stat stat)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, stat.won);
        values.put(WHITE_TIME, Long.toString(stat.whiteLeft.toLong()));
        values.put(BLACK_TIME, Long.toString(stat.blackLeft.toLong()));

        db.insert(TABLE, null, values);
        close();
    }


    public Collection<Stat> readStats()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        ArrayList<Stat> list = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            list.add(createStat(cursor));

        cursor.close();
        close();

        return list;
    }


    public void clear()
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(ID, null, null);

        close();
    }


    private Stat createStat(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(ID));
        String white = cursor.getString(cursor.getColumnIndex(WHITE_TIME));
        String black = cursor.getString(cursor.getColumnIndex(BLACK_TIME));

        return new Stat(Time.fromLong(Long.valueOf(white)),
            Time.fromLong(Long.valueOf(black)), id);
    }


    private static final String DATABASE = "Stats.db";
}
