package groupe_9.com.fabellas.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import groupe_9.com.fabellas.DummyContent;
import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.StorieDetailActivity;
import groupe_9.com.fabellas.StoriesListActivity;
import groupe_9.com.fabellas.fragments.StorieDetailFragment;

/**
 * Created by thoma on 15/01/2018.
 */

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.StorieViewHolder>
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

    public SimpleItemRecyclerViewAdapter(StoriesListActivity parent,
                                         List<DummyContent.DummyItem> items,
                                         boolean isInTwoPane)
    {
        values = items;
        mParentActivity = parent;
        this.isInTwoPane = isInTwoPane;
    }

    @Override
    public StorieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        return new StorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StorieViewHolder holder, int position)
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

    class StorieViewHolder extends RecyclerView.ViewHolder
    {
        final TextView title;
        final TextView content;

        StorieViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.id_text);
            content = view.findViewById(R.id.content);
        }
    }
}