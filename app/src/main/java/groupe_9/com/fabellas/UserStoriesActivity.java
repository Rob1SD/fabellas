package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.utils.OnStoryClickable;

/**
 * Created by thoma on 24/01/2018.
 */

public class UserStoriesActivity
        extends StoriesListActivity implements OnStoryClickable
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stories);

        if (getIntent() == null || !MapActivity.USER_PROFIL_ACTION.equals(getIntent().getAction()))
        {
            finish();
        }

        setUpToolbar(getString(R.string.user_stories_title));

        recyclerView = findViewById(R.id.item_list);
        emptyView = findViewById(R.id.empty_view);
        loader = findViewById(R.id.loader);

        setupRecyclerView(this);

        lookingForStories(STORIES_TYPE.USER_STORIES, FirebaseAuth.getInstance().getCurrentUser().getUid());
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

    @Override
    public void clickedOnStory(Story story)
    {

    }
}
