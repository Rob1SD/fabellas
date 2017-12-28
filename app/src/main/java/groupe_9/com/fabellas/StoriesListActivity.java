package groupe_9.com.fabellas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import groupe_9.com.fabellas.bo.PlaceTag;
import groupe_9.com.fabellas.fragments.StorieDetailFragment;

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

        final View recyclerView = findViewById(R.id.item_list);
        if (recyclerView != null)
        {
            setupRecyclerView((RecyclerView) recyclerView);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, isIntwoPanes));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
    {

        private final StoriesListActivity mParentActivity;
        private final List<DummyContent.DummyItem> values;
        private final boolean isInTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (isInTwoPane)
                {
                    final Bundle arguments = new Bundle();
                    arguments.putString(StorieDetailFragment.ARG_ITEM_ID, item.id);
                    final StorieDetailFragment fragment = new StorieDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                }
                else
                {
                    final Context context = view.getContext();
                    final Intent intent = new Intent(context, StorieDetailActivity.class);
                    intent.putExtra(StorieDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(StoriesListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean isInTwoPane)
        {
            values = items;
            mParentActivity = parent;
            this.isInTwoPane = isInTwoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            holder.title.setText(values.get(position).id);
            holder.content.setText(values.get(position).content);

            holder.itemView.setTag(values.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount()
        {
            return values.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView title;
            final TextView content;

            ViewHolder(View view)
            {
                super(view);
                title = view.findViewById(R.id.id_text);
                content = view.findViewById(R.id.content);
            }
        }
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
