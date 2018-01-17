package groupe_9.com.fabellas.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.StorieDetailActivity;
import groupe_9.com.fabellas.StoriesListActivity;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.fragments.StorieDetailFragment;

/**
 * Created by thoma on 15/01/2018.
 */

public class StoriesRecyclerViewAdapter
        extends RecyclerView.Adapter<StoriesRecyclerViewAdapter.StorieViewHolder>
{

    private final StoriesListActivity mParentActivity;
    private final ArrayList<Story> stories;
    private final boolean isInTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            final Story storie = (Story) view.getTag();
            if (isInTwoPane)
            {
                final Bundle arguments = new Bundle();
                arguments.putSerializable(StorieDetailFragment.STORIE_EXTRA, storie);

                final StorieDetailFragment fragment = new StorieDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.item_detail_container, fragment)
                        .commit();
            }
            else
            {
                final Context context = view.getContext();
                final Intent intent = new Intent(context, StorieDetailActivity.class);
                intent.putExtra(StorieDetailFragment.STORIE_EXTRA, storie);

                context.startActivity(intent);
            }
        }
    };

    public StoriesRecyclerViewAdapter(StoriesListActivity parent,
                                      ArrayList<Story> stories,
                                      boolean isInTwoPane)
    {
        this.stories = stories;
        mParentActivity = parent;
        this.isInTwoPane = isInTwoPane;
    }

    @Override
    public StorieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_content, parent, false);
        return new StorieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StorieViewHolder holder, int position)
    {
        holder.title.setText(stories.get(position).title);
        holder.content.setText(stories.get(position).detail);

        holder.itemView.setTag(stories.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount()
    {
        return stories.size();
    }

    class StorieViewHolder extends RecyclerView.ViewHolder
    {
        final TextView title;
        final TextView content;

        StorieViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            content = view.findViewById(R.id.details);
        }
    }
}