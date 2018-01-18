package groupe_9.com.fabellas.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.firebase.StoriesFinder;
import groupe_9.com.fabellas.firebase.Utils;

/**
 * Created by thoma on 15/01/2018.
 */

public class AppWidgetAdapterFactory
        implements RemoteViewsService.RemoteViewsFactory
{
    private ArrayList<Story> stories;
    private String placeID;
    private int widgetID;
    private Context context;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mStoriesReference;

    private ValueEventListener addValueEventListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            final Story story = dataSnapshot.getValue(Story.class);
            stories.add(story);
            Log.i("thomasecalle", "Widget, onDataChanged, get story : " + story.getTitle());
            FabellasAppWidgetProvider.sendRefreshBroadcast(context);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            Log.i("thomas", "onCancelled");
        }
    };

    private ValueEventListener removeValueEventListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Story story = dataSnapshot.getValue(Story.class);
            stories.remove(story);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
        }
    };

    private ChildEventListener childEventListener = new ChildEventListener()
    {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s)
        {
            mStoriesReference = Utils.getDatabase().getReference("Stories").child(dataSnapshot.getValue().toString());
            mStoriesReference.addListenerForSingleValueEvent(addValueEventListener);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s)
        {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot)
        {
            mStoriesReference = Utils.getDatabase().getReference("Stories").child(dataSnapshot.getValue().toString());
            mStoriesReference.addListenerForSingleValueEvent(removeValueEventListener);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s)
        {
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
        }

    };


    public AppWidgetAdapterFactory(Context context, Intent intent)
    {
        this.context = context;
        placeID = intent.getStringExtra(FabellasAppWidgetProvider.APPWIDGET_PLACE_ID_EXTRA);
        widgetID = intent.getIntExtra(FabellasAppWidgetProvider.WIDGET_ID, 0);
    }

    @Override
    public void onCreate()
    {
        stories = new ArrayList<>();
        mDatabaseReference = Utils.getDatabase().getReference("Places").child(this.placeID).child("stories");
        mDatabaseReference.addChildEventListener(childEventListener);


    }

    @Override
    public void onDataSetChanged()
    {

    }

    @Override
    public void onDestroy()
    {
        if (stories != null)
        {
            stories = null;
        }
        if (mDatabaseReference != null)
        {
            if (mStoriesReference != null)
            {
                mStoriesReference.removeEventListener(addValueEventListener);
                mStoriesReference.removeEventListener(removeValueEventListener);
            }
            mDatabaseReference.removeEventListener(childEventListener);
        }

    }

    @Override
    public int getCount()
    {
        return stories.isEmpty() ? 0 : stories.size();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        if (position == AdapterView.INVALID_POSITION || stories == null || stories.get(position) == null)
        {
            return null;
        }

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.title, stories.get(position).getTitle());
        remoteViews.setTextViewText(R.id.details, stories.get(position).getDetail());

        // Next, set a fill-intent, which will be used to fill in the pending intent template
        // that is set on the collection view in StackWidgetProvider.
        final Bundle extras = new Bundle();
        extras.putString("thomas", stories.get(position).getUID());
        //fillInIntent.setData(Uri.parse("myapp://widget/id/" + widgetID));

        Intent fillInIntent = new Intent(FabellasAppWidgetProvider.INTENT_FROM_APPWIDGET_ITEM);
        fillInIntent.putExtra(FabellasAppWidgetProvider.STORIE, stories.get(position));
        fillInIntent.putExtra(FabellasAppWidgetProvider.WIDGET_ID, widgetID);

        remoteViews.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);

        return remoteViews;

    }

    @Override
    public RemoteViews getLoadingView()
    {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_loading_item);
        return remoteViews;
    }

    @Override
    public int getViewTypeCount()
    {
        return stories.isEmpty() ? 0 : 1;
    }

    @Override
    public long getItemId(int position)
    {
        return stories == null ? 0 : stories.get(position).hashCode();
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
