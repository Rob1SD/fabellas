package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import groupe_9.com.fabellas.bo.PlaceTag;
import groupe_9.com.fabellas.fragments.PlaceStoriesFragment;

public class PlaceStoriesActivity extends FabellasActivity
{

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_stories);

        title = "Error";

        if ((getIntent() != null))
        {
            if ((getIntent().getExtras() != null))
            {
                final Bundle extras = getIntent().getExtras();
                this.title = ((PlaceTag) extras.getSerializable(MapActivity.PLACE_ID)).getTitle();
                final FragmentManager supportFragmentManager = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                final PlaceStoriesFragment placeStoriesFragment = new PlaceStoriesFragment();

                placeStoriesFragment.setArguments(extras);

                fragmentTransaction.replace(R.id.container, placeStoriesFragment);
                fragmentTransaction.commit();


            }
        }

        setUpToolbar(this, title, true);
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
}
