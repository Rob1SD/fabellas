package groupe_9.com.fabellas.firebase;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import groupe_9.com.fabellas.MapActivity;
import groupe_9.com.fabellas.bo.Story;

/**
 * Created by thoma on 19/01/2018.
 */

public class StoriesFinder
{
    private StoriesFinderCallbacks callbacks;
    private DatabaseReference mDatabaseReference;
    private Context context;

    public StoriesFinder(Context context, StoriesFinderCallbacks callbacks)
    {
        this.callbacks = callbacks;
        this.context = context;
    }

    public void start(String id)
    {
        mDatabaseReference = Utils.getDatabase().getReference("Places").child(id);

        callbacks.onStartSearching();

        mDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.exists())
                {
                    callbacks.onNoStorieFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.i(MapActivity.TAG, "onResearch cancelled");
            }
        });


        mDatabaseReference = Utils.getDatabase().getReference("Places").child(id).child("stories");

        mDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.exists())
                {
                    callbacks.onNoStorieFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        mDatabaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

                DatabaseReference mChildDatabaseReference = Utils.getDatabase().getReference("Stories").child(dataSnapshot.getValue().toString());
                mChildDatabaseReference.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (null == dataSnapshot.getValue())
                        {
                            mChildDatabaseReference.removeEventListener(this);
                            return;
                        }
                        final Story story = dataSnapshot.getValue(Story.class);
                        callbacks.onStoryFound(story);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.i("thomas", "onCancelled");
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                callbacks.onStoryRemoved(
                        new Story(dataSnapshot.getValue().toString(), null, null, null, null, null));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    public void startUserStories(String id)
    {
        mDatabaseReference = Utils.getDatabase().getReference("Users").child(id);
        callbacks.onStartSearching();

        mDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.exists())
                {
                    callbacks.onNoStorieFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });


        mDatabaseReference = Utils.getDatabase().getReference("Users").child(id).child("stories");

        mDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (!dataSnapshot.exists())
                {
                    callbacks.onNoStorieFound();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        mDatabaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                DatabaseReference mChildDatabaseReference = Utils.getDatabase().getReference("Stories").child(dataSnapshot.getValue().toString());
                mChildDatabaseReference.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (null == dataSnapshot.getValue())
                        {
                            mChildDatabaseReference.removeEventListener(this);
                            return;
                        }
                        final Story story = dataSnapshot.getValue(Story.class);
                        callbacks.onStoryFound(story);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.i("thomas", "onCancelled");
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {
                callbacks.onStoryRemoved(
                        new Story(dataSnapshot.getValue().toString(), null, null, null, null, null));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }

    private boolean isNetworkAvailable()
    {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
