package groupe_9.com.fabellas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import groupe_9.com.fabellas.adapters.StoriesRecyclerViewAdapter;
import groupe_9.com.fabellas.bo.PlaceTag;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.firebase.StoriesFinder;
import groupe_9.com.fabellas.firebase.StoriesFinderCallbacks;
import groupe_9.com.fabellas.firebase.Utils;
import groupe_9.com.fabellas.fragments.StorieDetailFragment;
import groupe_9.com.fabellas.utils.OnStoryClickable;
import groupe_9.com.fabellas.widget.FabellasAppWidgetConfigureActivity;
import groupe_9.com.fabellas.widget.FabellasAppWidgetProvider;

public class StoriesListActivity
        extends AppCompatActivity
        implements OnStoryClickable, View.OnClickListener, StoriesFinderCallbacks
{
    private boolean isIntwoPanes;
    private String title;
    private String id;
    private DatabaseReference mDatabaseReference;
    private ArrayList<Story> stories;
    private StoriesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar loader;
    public static final int REQUEST_CODE_FOR_ADD_STORIE_ACTIVITY = 1;
    private boolean isFromWidget = false;

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

        final ImageView iconImageView = findViewById(R.id.floating_button);
        iconImageView.setOnClickListener(this);

        iconImageView.setVisibility(FirebaseAuth.getInstance().getCurrentUser().isAnonymous() ? View.GONE : View.VISIBLE);


        setUpToolbar();

        if (findViewById(R.id.item_detail_container) != null)
        {
            isIntwoPanes = true;
        }

        recyclerView = findViewById(R.id.item_list);
        emptyView = findViewById(R.id.empty_view);
        loader = findViewById(R.id.loader);


        setupRecyclerView(recyclerView);

        final StoriesFinder storiesFinder = new StoriesFinder(this);

        mDatabaseReference = storiesFinder.startToSearch(id);
    }

    private void isEmptyListHandling(boolean isLoading)
    {
        loader.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(stories.isEmpty() && !isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(stories.isEmpty() && !isLoading ? View.GONE : View.VISIBLE);
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
                if (isFromWidget)
                {
                    startActivity(new Intent(this, MapActivity.class));
                    finish();
                }
                else
                {
                    onBackPressed();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpToolbar()
    {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = toolbar.findViewById(R.id.title);

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
                    isFromWidget = true;
                    placeTag = (PlaceTag) bundle.getSerializable(MapActivity.PLACE);
                    this.title = placeTag.getTitle();
                    this.id = placeTag.getId();
                    break;
                case FabellasAppWidgetProvider.INTENT_FROM_APPWIDGET_ITEM:
                    isFromWidget = true;
                    final Story storie = (Story) intent.getSerializableExtra(FabellasAppWidgetProvider.STORIE);
                    final int widgetID = intent.getIntExtra(FabellasAppWidgetProvider.WIDGET_ID, 0);
                    this.id = storie.getPlaceId();
                    this.title = FabellasAppWidgetConfigureActivity.loadTitlePref(this, widgetID);
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
        DatabaseReference StoryDatabaseReference = Utils.getDatabase().getReference("Stories").push();
        StoryDatabaseReference.setValue(new Story(StoryDatabaseReference.getKey(), details, id, title, userUid));
        mDatabaseReference.push().setValue(StoryDatabaseReference.getKey());
        Utils.getDatabase().getReference("Users").child(userUid).child("stories").push().setValue(StoryDatabaseReference.getKey());

        FabellasAppWidgetProvider.sendRefreshBroadcast(this);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }

    @Override
    public void onStartLooking()
    {
        isEmptyListHandling(true);
    }

    @Override
    public void onStorieFound(Story story)
    {
        stories.add(story);
        adapter.notifyDataSetChanged();
        isEmptyListHandling(false);
    }

    @Override
    public void onPlaceNotFound()
    {
        isEmptyListHandling(false);
    }

    @Override
    public void onStoryRemoved(Story story)
    {
        stories.add(story);
        adapter.notifyDataSetChanged();
        isEmptyListHandling(false);
    }
}
