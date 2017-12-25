package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import groupe_9.com.fabellas.fragments.PlaceStoriesFragment;

public class PlaceStoriesActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_stories);

        if ((getIntent() != null))
        {
            if ((getIntent().getExtras() != null))
            {
                final FragmentManager supportFragmentManager = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                final PlaceStoriesFragment placeStoriesFragment = new PlaceStoriesFragment();

                placeStoriesFragment.setArguments(getIntent().getExtras());

                fragmentTransaction.replace(R.id.container, placeStoriesFragment);
                fragmentTransaction.commit();


            }


        }
    }
}
