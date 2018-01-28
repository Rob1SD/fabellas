package groupe_9.com.fabellas.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.utils.OnStoryClickable;

/**
 * Created by thoma on 15/01/2018.
 */

public class StoriesRecyclerViewAdapter
        extends RecyclerView.Adapter<StoriesRecyclerViewAdapter.StorieViewHolder>
{

    private final OnStoryClickable listener;
    private final ArrayList<Story> stories;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            final Story storie = (Story) view.getTag();
            listener.clickedOnStory(storie);
        }
    };

    public StoriesRecyclerViewAdapter(OnStoryClickable listener, ArrayList<Story> stories)
    {
        this.stories = stories;
        this.listener = listener;
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
        final Story story = stories.get(position);
        holder.title.setText(story.getTitle());
        holder.content.setText(story.getDetail());
        holder.ratingBar.setRating(story.getRate() == null ? 0f : story.getRate());


        holder.itemView.setTag(story);
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
        final RatingBar ratingBar;

        StorieViewHolder(View view)
        {
            super(view);
            title = view.findViewById(R.id.title);
            content = view.findViewById(R.id.details);
            ratingBar = view.findViewById(R.id.ratingBar);
        }
    }
}