package groupe_9.com.fabellas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.fragments.StorieDetailFragment;
import groupe_9.com.fabellas.utils.OnDetailStoryRemoval;

public class StorieDetailActivity
        extends AppCompatActivity
        implements OnDetailStoryRemoval
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

    @Override
    public void onActualDetailStoryRemove()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.story_removal_dialog_title);
        builder.setMessage(R.string.story_removal_dialog_message);
        builder.setCancelable(false);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });

        builder.create().show();
    }
}
