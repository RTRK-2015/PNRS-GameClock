package rtrk.pnrs.gameclock;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


public class StatsProvider
    extends ContentProvider
{
    public static final String AUTHORITY = "rtrk.pnrs.gameclock";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
        StatDBHelper.TABLE);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.rtrk.pnrs.gameclock.stat";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.rtrk.pnrs.gameclock.stat";


    public StatsProvider()
    {
    }


    private int delete(String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int deleted = db.delete(StatDBHelper.TABLE, selection, selectionArgs);
        db.close();

        return deleted;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        int m = matcher.match(uri);
        int deleted = 0;

        if (m == STAT)
            deleted = delete(selection, selectionArgs);
        else if (m == STAT_ITEM)
            deleted = delete("_id = ?", new String[] { uri.getLastPathSegment() });

        if (deleted > 0)
        {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return deleted;
    }


    @Override
    public String getType(Uri uri)
    {
        int m = matcher.match(uri);

        if (m == STAT)
            return CONTENT_TYPE;
        else if (m == STAT_ITEM)
            return CONTENT_ITEM_TYPE;
        else
            return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(StatDBHelper.TABLE, null, values);
        mHelper.close();

        if (id != -1)
        {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return Uri.withAppendedPath(uri, Long.toString(id));
    }


    @Override
    public boolean onCreate()
    {
        mHelper = new StatDBHelper(getContext());
        return true;
    }


    private Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        return db.query(StatDBHelper.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {
        int m = matcher.match(uri);

        if (m == STAT)
            return query(projection, selection, selectionArgs, sortOrder);
        else if (m == STAT_ITEM)
            return query(projection, "_id = ?", new String[] { uri.getLastPathSegment() }, null);
        else
            return null;
    }


    private int update(ContentValues values, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updated = db.update(StatDBHelper.TABLE, values, selection, selectionArgs);
        mHelper.close();

        return updated;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs)
    {
        int m = matcher.match(uri);
        int updated = 0;

        if (m == STAT)
            updated = update(values, selection, selectionArgs);
        else if (m == STAT_ITEM)
            updated = update(values, "_id = ?", new String[] { uri.getLastPathSegment() });

        if (updated > 1)
        {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.notifyChange(uri, null);
        }

        return updated;
    }


    private StatDBHelper mHelper;
    private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int STAT = 1;
    private static final int STAT_ITEM = 2;

    static
    {
        matcher.addURI(AUTHORITY, StatDBHelper.TABLE, STAT);
        matcher.addURI(AUTHORITY, StatDBHelper.TABLE + "/#", STAT_ITEM);
    }
}
