package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import groupe_9.com.fabellas.bo.PlaceTag;
import groupe_9.com.fabellas.fragments.PlaceStoriesFragment;

public class PlaceStoriesActivity
        extends AppCompatActivity
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
                this.title = ((PlaceTag) extras.getSerializable(MapActivity.PLACE)).getTitle();
                final FragmentManager supportFragmentManager = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                final PlaceStoriesFragment placeStoriesFragment = new PlaceStoriesFragment();

                placeStoriesFragment.setArguments(extras);

                fragmentTransaction.replace(R.id.container, placeStoriesFragment);
                fragmentTransaction.commit();


            }
        }

        setUpToolbar();
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
}
