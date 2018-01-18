package groupe_9.com.fabellas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import groupe_9.com.fabellas.adapters.StoriesRecyclerViewAdapter;
import groupe_9.com.fabellas.bo.PlaceTag;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.widget.FabellasAppWidgetProvider;

public class StoriesListActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isIntwoPanes;
    private String title;
    private String id;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Story> stories;
    private StoriesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_stories);

        stories = new ArrayList<>();

        if ((getIntent() != null))
        {
            handleIntent(getIntent());
        }

        setUpToolbar();

        if (findViewById(R.id.item_detail_container) != null)
        {
            isIntwoPanes = true;
        }

        recyclerView = findViewById(R.id.item_list);
        emptyView = findViewById(R.id.empty_view);


        setupRecyclerView(recyclerView);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Places").child(this.id).child("stories");
        mDatabaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                DatabaseReference mChildDatabaseReference = FirebaseDatabase.getInstance().getReference("Stories").child(dataSnapshot.getValue().toString());
                mChildDatabaseReference.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Story story = dataSnapshot.getValue(Story.class);
                        stories.add(story);
                        adapter.notifyDataSetChanged();
                        isEmptyListHandling();
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
                        FirebaseDatabase.getInstance().getReference("Stories").child(dataSnapshot.getValue().toString());
                mChildDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Story story = dataSnapshot.getValue(Story.class);
                        stories.remove(story);
                        adapter.notifyDataSetChanged();
                        isEmptyListHandling();
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

        isEmptyListHandling();
        (findViewById(R.id.icon)).setOnClickListener(this);


    }

    private void isEmptyListHandling()
    {
        emptyView.setVisibility(stories.isEmpty() ? ImageView.VISIBLE : View.GONE);
        recyclerView.setVisibility(stories.isEmpty() ? ImageView.GONE : View.VISIBLE);
    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        this.adapter = new StoriesRecyclerViewAdapter(this, this.stories, isIntwoPanes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar()
    {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = toolbar.findViewById(R.id.title);

        final ImageView iconImageView = findViewById(R.id.icon);
        iconImageView.setImageResource(R.drawable.ic_add);
        toolbarTitle.setText(this.title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void handleIntent(Intent intent)
    {
        final Bundle bundle = intent.getExtras();

        if ((bundle.getString(FabellasAppWidgetProvider.APPWIDGET_PLACE_ID_EXTRA) != null))
        {
            this.title = (bundle.getString(FabellasAppWidgetProvider.APPWIDGET_TITLE_EXTRA));
        }
        else
        {
            final PlaceTag placeTag = (PlaceTag) bundle.getSerializable(MapActivity.PLACE_ID);
            loadPlaceData(placeTag.getTitle(), placeTag.getId());
        }
    }

    private void loadPlaceData(String title, String id)
    {
        this.title = title;
        this.id = id;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.icon)
        {
            Log.i(MapActivity.TAG, String.format("PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS PLS "));
            Intent intent = new Intent(this,AddStorieActivity.class);
            startActivityForResult(intent, 1);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = data.getStringExtra(AddStorieActivity.TITLE_VALUE);
        String details = data.getStringExtra(AddStorieActivity.DETAILS_VALUE);
        Toast.makeText(this,"title :  => " + title + "\nDetails : " + details,Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
