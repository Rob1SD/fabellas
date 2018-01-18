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
    private ValueEventListener isPlaceInDatabaseListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            if (dataSnapshot.exists())
            {
                databaseReference = Utils.getDatabase().getReference("Places").child(placeID).child("stories");

                databaseReference.addChildEventListener(new ChildEventListener()
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
                                Story story = dataSnapshot.getValue(Story.class);
                                storiesFinderCallbacks.onStorieFound(story);
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
                        DatabaseReference mChildDatabaseReference =
                                Utils.getDatabase().getReference("Stories").child(dataSnapshot.getValue().toString());
                        mChildDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                final Story story = dataSnapshot.getValue(Story.class);
                                storiesFinderCallbacks.onStoryRemoved(story);
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
                        Log.i("thomasecalle", "onChildMoved");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.i("thomasecalle", "onCancelled");
                    }
                });
            }
            else
            {
                Log.i("thomasecalle", "Il n'y a pas de places ayant cet ID !!");
                storiesFinderCallbacks.onPlaceNotFound();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {

        }
    };
    private DatabaseReference databaseReference;
    private StoriesFinderCallbacks storiesFinderCallbacks;
    private String placeID;

    public StoriesFinder(StoriesFinderCallbacks storiesFinderCallbacks)
    {
        this.storiesFinderCallbacks = storiesFinderCallbacks;
    }

    public DatabaseReference startToSearch(String placeID)
    {
        this.placeID = placeID;

        databaseReference = Utils.getDatabase().getReference("Places").child(placeID);

        storiesFinderCallbacks.onStartLooking();
        databaseReference.addValueEventListener(isPlaceInDatabaseListener);

        return this.databaseReference;
    }
}
