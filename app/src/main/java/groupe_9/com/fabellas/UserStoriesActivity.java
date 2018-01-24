package groupe_9.com.fabellas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import groupe_9.com.fabellas.adapters.StoriesRecyclerViewAdapter;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.firebase.StoriesFinder;
import groupe_9.com.fabellas.firebase.StoriesFinderCallbacks;
import groupe_9.com.fabellas.utils.OnStoryClickable;

/**
 * Created by thoma on 24/01/2018.
 */

public class UserStoriesActivity
        extends AppCompatActivity implements OnStoryClickable, StoriesFinderCallbacks
{
    private RecyclerView recyclerView;
    private TextView emptyView;
    private ProgressBar loader;
    private StoriesRecyclerViewAdapter adapter;
    private ArrayList<Story> stories;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stories);

        stories = new ArrayList<>();

        if (getIntent() == null || !MapActivity.USER_PROFIL_ACTION.equals(getIntent().getAction()))
        {
            finish();
        }

        setUpToolbar();

        recyclerView = findViewById(R.id.item_list);
        emptyView = findViewById(R.id.empty_view);
        loader = findViewById(R.id.loader);

        setupRecyclerView(recyclerView);

        lookingForStories();
    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        this.adapter = new StoriesRecyclerViewAdapter(this, this.stories);
        recyclerView.setAdapter(adapter);
    }

    private void setUpToolbar()
    {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = toolbar.findViewById(R.id.title);

        toolbarTitle.setText(R.string.user_stories_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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

    private void lookingForStories()
    {
        final StoriesFinder storiesFinder = new StoriesFinder(this);

        storiesFinder.startUserStories(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void isEmptyListHandling(boolean isLoading)
    {
        loader.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(stories.isEmpty() && !isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(stories.isEmpty() && !isLoading ? View.GONE : View.VISIBLE);
    }


    @Override
    public void clickedOnStory(Story story)
    {

    }

    @Override
    public void onStoryFound(Story storie)
    {
        stories.add(storie);
        adapter.notifyDataSetChanged();
        isEmptyListHandling(false);
    }

    @Override
    public void onStoryRemoved(Story storie)
    {
        stories.remove(storie);
        adapter.notifyDataSetChanged();
        isEmptyListHandling(false);
    }

    @Override
    public void onNoStorieFound()
    {
        isEmptyListHandling(false);
    }

    @Override
    public void onStartSearching()
    {
        isEmptyListHandling(true);
    }
}
