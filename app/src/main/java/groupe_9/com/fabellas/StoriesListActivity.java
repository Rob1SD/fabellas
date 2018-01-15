package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import groupe_9.com.fabellas.adapters.SimpleItemRecyclerViewAdapter;
import groupe_9.com.fabellas.bo.PlaceTag;

public class StoriesListActivity extends AppCompatActivity
{
    private boolean isIntwoPanes;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_stories);

        if ((getIntent() != null))
        {
            if ((getIntent().getExtras() != null))
            {
                final Bundle extras = getIntent().getExtras();
                this.title = ((PlaceTag) extras.getSerializable(MapActivity.PLACE_ID)).getTitle();
            }
        }

        setUpToolbar();


        if (findViewById(R.id.item_detail_container) != null)
        {
            isIntwoPanes = true;
        }

        final View listView = findViewById(R.id.item_list);
        if (listView != null)
        {
            setupRecyclerView((RecyclerView) listView);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, isIntwoPanes));
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
