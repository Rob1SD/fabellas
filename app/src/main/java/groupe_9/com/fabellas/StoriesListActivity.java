package groupe_9.com.fabellas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
import groupe_9.com.fabellas.fragments.StorieDetailFragment;
import groupe_9.com.fabellas.utils.OnStoryClickable;
import groupe_9.com.fabellas.widget.FabellasAppWidgetProvider;

public class StoriesListActivity
        extends AppCompatActivity
        implements OnStoryClickable, View.OnClickListener
{
    private boolean isIntwoPanes;
    private String title;
    private String id;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Story> stories;
    private StoriesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    public static final int REQUEST_CODE_FOR_ADD_STORIE_ACTIVITY = 1;

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

        final ImageView iconImageView = findViewById(R.id.floating_button);
        iconImageView.setOnClickListener(this);

        toolbarTitle.setText(this.title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void handleIntent(@NonNull Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        PlaceTag placeTag = null;

        if (intent.getAction() != null)
        {
            switch (intent.getAction())
            {
                case FabellasAppWidgetProvider.INTENT_FROM_APPWIDGET_TITLE:
                    placeTag = (PlaceTag) bundle.getSerializable(MapActivity.PLACE);
                    this.title = placeTag.getTitle();
                    this.id = placeTag.getId();
                    break;
                case FabellasAppWidgetProvider.INTENT_FROM_APPWIDGET_ITEM:
                    final Story storie = (Story) intent.getSerializableExtra(MapActivity.PLACE);
                    final String title = intent.getStringExtra(FabellasAppWidgetProvider.APPWIDGET_PLACE_NAME);
                    this.id = storie.getPlaceId();
                    this.title = title;
                    clickedOnStory(storie);
                    break;

                case MapActivity.INTENT_FROM_MAP_ACTIVITY:
                    placeTag = (PlaceTag) bundle.getSerializable(MapActivity.PLACE);
                    loadPlaceData(placeTag.getTitle(), placeTag.getId());
                    break;
            }
        }
    }

    private void loadPlaceData(String title, String id)
    {
        this.title = title;
        this.id = id;
    }

    @Override
    public void clickedOnStory(Story storie)
    {
        if (isIntwoPanes)
        {
            final Bundle arguments = new Bundle();
            arguments.putSerializable(StorieDetailFragment.STORIE_EXTRA, storie);

            final StorieDetailFragment fragment = new StorieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment).commit();
        }
        else
        {
            final Intent intent = new Intent(this, StorieDetailActivity.class);
            intent.putExtra(StorieDetailFragment.STORIE_EXTRA, storie);

            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.floating_button)
        {
            if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
            {
                Intent intent = new Intent(this, AddStorieActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOR_ADD_STORIE_ACTIVITY);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_FOR_ADD_STORIE_ACTIVITY == requestCode && RESULT_OK == resultCode)
        {
            String title = data.getStringExtra(AddStorieActivity.TITLE_VALUE);
            String details = data.getStringExtra(AddStorieActivity.DETAILS_VALUE);
            addNewStory(title, details);
        }
    }

    private void addNewStory(String title, String details)
    {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference StoryDatabaseReference = FirebaseDatabase.getInstance().getReference("Stories").push();
        StoryDatabaseReference.setValue(new Story(StoryDatabaseReference.getKey(), details, id, title, userUid));
        mDatabaseReference.push().setValue(StoryDatabaseReference.getKey());
        FirebaseDatabase.getInstance().getReference("Users").child(userUid).child("stories").push().setValue(StoryDatabaseReference.getKey());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }
}
