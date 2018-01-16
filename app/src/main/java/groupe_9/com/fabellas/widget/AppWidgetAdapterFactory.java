package groupe_9.com.fabellas.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import groupe_9.com.fabellas.DummyContent;
import groupe_9.com.fabellas.R;

/**
 * Created by thoma on 15/01/2018.
 */

public class AppWidgetAdapterFactory
        implements RemoteViewsService.RemoteViewsFactory
{
    private List<DummyContent.DummyItem> list;
    private Context context;

    public AppWidgetAdapterFactory(Context context, Intent intent)
    {
        this.context = context;
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void onDataSetChanged()
    {
        if (list != null)
        {
            list.clear();
        }

        list = DummyContent.ITEMS;
    }

    @Override
    public void onDestroy()
    {
        if (list != null)
        {
            list = null;
        }
    }

    @Override
    public int getCount()
    {
        return list == null ? 0 : list.size();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        if (position == AdapterView.INVALID_POSITION || list == null || list.get(position) == null)
        {
            return null;
        }

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        remoteViews.setTextViewText(R.id.title, list.get(position).content);
        remoteViews.setTextViewText(R.id.details, list.get(position).details);

        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra(FabellasAppWidgetProvider.APPWIDGET_PLACE_ID_EXTRA, list.get(position).content);

        remoteViews.setOnClickFillInIntent(R.id.widget_item_container, fillInIntent);

        return remoteViews;

    }

    @Override
    public RemoteViews getLoadingView()
    {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_loading_item);
        return remoteViews;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public long getItemId(int i)
    {
        return list == null ? 0 : list.get(i).hashCode();
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }
}
