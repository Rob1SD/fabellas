package groupe_9.com.fabellas.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import groupe_9.com.fabellas.MapActivity;
import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.StoriesListActivity;
import groupe_9.com.fabellas.bo.PlaceTag;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link FabellasAppWidgetConfigureActivity FabellasAppWidgetConfigureActivity}
 */
public class FabellasAppWidgetProvider extends AppWidgetProvider
{
    public static final String APPWIDGET_PLACE_ID_EXTRA = "appWidgetPlaceIdExtra";
    public static final String APPWIDGET_PLACE_NAME = "appWidgetPlaceName";
    public static final String WIDGET_ID = "widgetID";
    public static final String INTENT_FROM_APPWIDGET_TITLE = "intentFromAppWidgetAction";
    public static final String INTENT_FROM_APPWIDGET_ITEM = "intentFromAppWidgetItemAction";
    public static final String STORIE = "storie";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
    {
        final String placeName = FabellasAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        final String placeID = FabellasAppWidgetConfigureActivity.loadIDPref(context, appWidgetId);

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_global_layout);
        views.setTextViewText(R.id.appwidget_title, placeName);

        final PendingIntent titlePendingIntent = clickOnTitleIntent(context, placeName, placeID, appWidgetId);
        views.setOnClickPendingIntent(R.id.appwidget_title, titlePendingIntent);

        final Intent intent = new Intent(context, WidgetRemoteViewsService.class);
        intent.putExtra(APPWIDGET_PLACE_ID_EXTRA, placeID);
        intent.putExtra(WIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.list, intent);


        // template to handle the click listener for each item
        //clickIntent.putExtra(APPWIDGET_PLACE_NAME, placeName);
        //clickIntent.setData(Uri.parse("myapp://widget/id/#togetituniqie" + appWidgetId));
        Intent clickIntentTemplate = new Intent(context, StoriesListActivity.class);
        //clickIntentTemplate.putExtra(APPWIDGET_PLACE_NAME, placeName);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
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
            FabellasAppWidgetConfigureActivity.deleteIDPref(context, appWidgetId);
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

    public static void sendRefreshBroadcast(Context context)
    {
        final Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, FabellasAppWidgetProvider.class));
        context.sendBroadcast(intent);
    }


    @Override
    public void onReceive(final Context context, Intent intent)
    {
        final String action = intent.getAction();

        switch (action)
        {
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                final ComponentName cn = new ComponentName(context, FabellasAppWidgetProvider.class);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(cn), R.id.list);
                break;
        }
        super.onReceive(context, intent);
    }

    private static PendingIntent clickOnTitleIntent(Context context, String placeName, String placeID, int appWidgetId)
    {
        final Intent titleIntent = new Intent(context, StoriesListActivity.class);
        titleIntent.setAction(INTENT_FROM_APPWIDGET_TITLE);
        final Bundle bundle = new Bundle();
        bundle.putSerializable(MapActivity.PLACE, new PlaceTag(placeName, placeID));

        titleIntent.putExtras(bundle);
        titleIntent.setData(Uri.withAppendedPath(Uri.parse("myapp://widget/id/#togetituniqie" + appWidgetId), String.valueOf(appWidgetId)));

        return PendingIntent.getActivity(context, 0, titleIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

