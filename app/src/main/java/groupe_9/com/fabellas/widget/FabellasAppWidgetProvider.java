package groupe_9.com.fabellas.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.StoriesListActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link FabellasAppWidgetConfigureActivity FabellasAppWidgetConfigureActivity}
 */
public class FabellasAppWidgetProvider extends AppWidgetProvider
{

    public static final String APPWIDGET_TITLE_EXTRA = "appWidgetTitleExtra";
    public static final String APPWIDGET_STORIE_EXTRA = "appWidgetTitleExtra";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        final String placeName = FabellasAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_global_layout);
        views.setTextViewText(R.id.appwidget_title, placeName);


        final Intent titleIntent = new Intent(context, StoriesListActivity.class);
        titleIntent.putExtra(FabellasAppWidgetProvider.APPWIDGET_TITLE_EXTRA, placeName);

        final PendingIntent titlePendingIntent = PendingIntent.getActivity(context, 0, titleIntent,  PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_title, titlePendingIntent);

        final Intent intent = new Intent(context, WidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.list, intent);


        // template to handle the click listener for each item
        final Intent clickIntentTemplate = new Intent(context, StoriesListActivity.class);
        final PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.list, clickPendingIntentTemplate);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds)
        {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds)
        {
            FabellasAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }
}

