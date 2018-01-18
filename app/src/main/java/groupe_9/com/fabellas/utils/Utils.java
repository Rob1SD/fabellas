package groupe_9.com.fabellas.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by thoma on 18/01/2018.
 */

public class Utils
{
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase()
    {
        if (mDatabase == null)
        {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

}
