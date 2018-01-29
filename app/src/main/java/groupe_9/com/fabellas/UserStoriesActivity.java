package groupe_9.com.fabellas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.firebase.Utils;
import groupe_9.com.fabellas.fragments.YesNoDialogFragment;
import groupe_9.com.fabellas.utils.OnStoryClickable;

/**
 * Created by thoma on 24/01/2018.
 */

public class UserStoriesActivity
        extends StoriesListActivity implements OnStoryClickable
{
    public static final String BUNDLE_STORY = "bundleStory";

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
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_STORY, story);
        YesNoDialogFragment yesNoDialogFragment = new YesNoDialogFragment();
        yesNoDialogFragment.setArguments(bundle);
        yesNoDialogFragment.show(getFragmentManager(), "YesNoDialog");

    }

    public void deleteClickedStory(Story story){
        final DatabaseReference mPlaceStoriesDatabaseReference =
                Utils.getDatabase().getReference("Places").child(story.getPlaceId()).child("stories");
        final DatabaseReference mUserStoriesDatabaseReference =
                Utils.getDatabase().getReference("Users").child(story.getUserId()).child("stories");

        mPlaceStoriesDatabaseReference.addListenerForSingleValueEvent(getStoriesValueListener(story.getUID()));
        mUserStoriesDatabaseReference.addListenerForSingleValueEvent(getStoriesValueListener(story.getUID()));

        Utils.getDatabase().getReference("Stories").child(story.getUID()).removeValue();
    }

    private ValueEventListener getStoriesValueListener(String storyId){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(storyId.equals(snapshot.getValue())){
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }


}
