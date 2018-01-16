package groupe_9.com.fabellas;

import android.content.Intent;
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
import groupe_9.com.fabellas.widget.FabellasAppWidgetProvider;

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
            handleIntent(getIntent());
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

    private void handleIntent(Intent intent)
    {
        final Bundle bundle = intent.getExtras();

        if ((bundle.getString(FabellasAppWidgetProvider.APPWIDGET_PLACE_ID_EXTRA) != null))
        {
            this.title = (bundle.getString(FabellasAppWidgetProvider.APPWIDGET_TITLE_EXTRA));
        }
        else
        {
            final PlaceTag placeTag = (PlaceTag) bundle.getSerializable(MapActivity.PLACE_ID);
            loadPlaceData(placeTag.getTitle(), placeTag.getId());
        }
    }

    private void loadPlaceData(String title, String id)
    {
        this.title = title;
    }
}
