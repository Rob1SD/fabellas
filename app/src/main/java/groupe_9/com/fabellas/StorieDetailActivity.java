package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.fragments.StorieDetailFragment;

public class StorieDetailActivity extends AppCompatActivity
{

    private Story storie;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storie_detail);


        final Bundle arguments = new Bundle();

        if (getIntent() != null && getIntent().getSerializableExtra(StorieDetailFragment.STORIE_EXTRA) != null)
        {
            this.storie = (Story) getIntent().getSerializableExtra(StorieDetailFragment.STORIE_EXTRA);
            this.title = storie.getTitle();
            arguments.putSerializable(StorieDetailFragment.STORIE_EXTRA, storie);
            final StorieDetailFragment fragment = new StorieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }


        setUpToolbar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
}
