package groupe_9.com.fabellas.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import groupe_9.com.fabellas.bo.PlaceItem;
import groupe_9.com.fabellas.bo.PlaceTag;

/**
 * Created by thoma on 03/02/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "FABELLAS";
    public static final int DB_VERSION = 1;


    private static final String DB_CREATE_TABLE_PLACE =
            "CREATE TABLE  "
                    + PlaceItem.TABLE_NAME + " ( "
                    + PlaceItem.COL_ID + " VARCHAR PRIMARY KEY, "
                    + PlaceItem.COL_TITLE + " VARCHAR, "
                    + PlaceItem.COL_LATITUDE + " REAL, "
                    + PlaceItem.COL_LONGITUDE + " REAL)";


    public DatabaseHelper(Context context, String name, int version)
    {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(DatabaseHelper.DB_CREATE_TABLE_PLACE);
    }

    /*
        Drop older table if existed
        Create tables again
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + PlaceItem.TABLE_NAME);


        onCreate(database);
    }

    public void addPlace(PlaceItem item)
    {
        if (!alreadyExist(item))
        {
            final SQLiteDatabase database = this.getWritableDatabase();

            final ContentValues values = new ContentValues();
            values.put(PlaceItem.COL_ID, item.getID());
            values.put(PlaceItem.COL_TITLE, item.getTitle());
            values.put(PlaceItem.COL_LATITUDE, item.getLatitude());
            values.put(PlaceItem.COL_LONGITUDE, item.getLongitude());


            database.insert(PlaceItem.TABLE_NAME, null, values);
            database.close();
        }

    }

    private boolean alreadyExist(PlaceItem item)
    {
        final SQLiteDatabase database = this.getReadableDatabase();

        final Cursor cursor = database.query(PlaceItem.TABLE_NAME, new String[]{PlaceItem.COL_ID}, PlaceItem.COL_ID + "=?",
                new String[]{String.valueOf(item.getID())}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
        {
            return true;
        }
        cursor.close();
        database.close();
        return false;

    }

    public ArrayList<PlaceItem> getAllPlaces()
    {
        final ArrayList<PlaceItem> list = new ArrayList<>();

        final String selectQuery = "SELECT  * FROM " + PlaceItem.TABLE_NAME;

        final SQLiteDatabase database = this.getWritableDatabase();

        final Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                final String id = cursor.getString(0);
                final String title = cursor.getString(1);
                final PlaceTag placeTag = new PlaceTag(title, id);

                final double latitude = Double.parseDouble(cursor.getString(2));
                final double longitude = Double.parseDouble(cursor.getString(3));


                final PlaceItem place = new PlaceItem(placeTag, latitude, longitude);

                list.add(place);

            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return list;
    }
}
