package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public abstract class StoriesListActivity
        extends AppCompatActivity
        implements StoriesFinderCallbacks
{
    protected enum STORIES_TYPE
    {
        USER_STORIES,
        PLACE_STORIES
    }

    protected ArrayList<Story> stories;
    protected StoriesRecyclerViewAdapter adapter;
    protected RecyclerView recyclerView;
    protected TextView network_error_view;
    protected TextView emptyView;
    protected ProgressBar loader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        stories = new ArrayList<>();
    }

    protected void setupRecyclerView(OnStoryClickable listener)
    {
        this.adapter = new StoriesRecyclerViewAdapter(listener, this.stories);
        recyclerView.setAdapter(adapter);
    }

    protected void setUpToolbar(String title)
    {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final TextView toolbarTitle = toolbar.findViewById(R.id.title);

        toolbarTitle.setText(title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void isEmptyListHandling(boolean isLoading)
    {

        loader.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(stories.isEmpty() && !isLoading ? View.VISIBLE : View.GONE);
        network_error_view.setVisibility(View.GONE);
        recyclerView.setVisibility(stories.isEmpty() && !isLoading ? View.GONE : View.VISIBLE);
    }

    private void noNetworkCOnnection()
    {
        network_error_view.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }


    protected void lookingForStories(StoriesListActivity.STORIES_TYPE type, String id)
    {
        final StoriesFinder storiesFinder = new StoriesFinder(this, this);
        if (type == STORIES_TYPE.PLACE_STORIES)
        {
            storiesFinder.start(id);
        }
        else
        {
            Log.i("ROBIN", "userid = " + FirebaseAuth.getInstance().getCurrentUser().getUid());
            storiesFinder.startUserStories(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }


    @Override
    public void onStoryFound(Story storie)
    {
        if (storie != null)
        {
            if (stories.contains(storie))
            {
                final int index = stories.indexOf(storie);
                final Story initialStory = stories.get(index);
                initialStory.clone(storie);

            }
            else
            {
                stories.add(storie);
            }
            adapter.notifyDataSetChanged();
            isEmptyListHandling(false);
        }

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

    @Override
    public void onNetworkError()
    {
        noNetworkCOnnection();
    }
}
