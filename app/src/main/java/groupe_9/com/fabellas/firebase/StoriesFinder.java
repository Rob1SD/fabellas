package groupe_9.com.fabellas.firebase;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import groupe_9.com.fabellas.bo.Story;

/**
 * Created by thoma on 19/01/2018.
 */

public class StoriesFinder
{
    private StoriesFinderCallbacks callbacks;
    private DatabaseReference mDatabaseReference;

    public StoriesFinder(StoriesFinderCallbacks callbacks)
    {
        this.callbacks = callbacks;
    }

    public void start(String id)
    {
        //Log.i("ROBIN", "ID = " + id);
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
                DatabaseReference mChildDatabaseReference = Utils.getDatabase().getReference("Stories").child(dataSnapshot.getValue().toString());
                mChildDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        final Story story = dataSnapshot.getValue(Story.class);
                        callbacks.onStoryRemoved(story);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                    }
                });
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
                DatabaseReference mChildDatabaseReference = Utils.getDatabase().getReference("Stories").child(dataSnapshot.getValue().toString());
                mChildDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        final Story story = dataSnapshot.getValue(Story.class);
                        callbacks.onStoryRemoved(story);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                    }
                });
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
}
