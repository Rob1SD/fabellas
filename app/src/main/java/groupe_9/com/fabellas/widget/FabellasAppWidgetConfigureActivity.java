package groupe_9.com.fabellas.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import groupe_9.com.fabellas.MapActivity;
import groupe_9.com.fabellas.R;

/**
 * The configuration screen for the {@link FabellasAppWidgetProvider FabellasAppWidgetProvider} AppWidget.
 */
public class FabellasAppWidgetConfigureActivity
        extends Activity
        implements View.OnClickListener
{

    private static final String PREFS_NAME = "groupe_9.com.fabellas.widget.FabellasAppWidgetProvider";
    private static final String PREFS_ID = "groupe_9.com.fabellas.widget.FabellasAppWidgetProvider.ID";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case MapActivity.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    final Place place = PlaceAutocomplete.getPlace(this, data);

                    //Toast.makeText(FabellasAppWidgetConfigureActivity.this, "Choose place : " + place.getName(), Toast.LENGTH_SHORT).show();

                    Log.i(MapActivity.TAG, "Place Searched on widget configure activity: " + place.getName());

                    saveTitlePref(this, mAppWidgetId, place.getName().toString());
                    saveIDPref(this, mAppWidgetId, place.getName().toString());

                    // It is the responsibility of the configuration activity to update the app widget
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                    FabellasAppWidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId);

                    // Make sure we pass back the original appWidgetId
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                    finish();

                }
                else if (resultCode == PlaceAutocomplete.RESULT_ERROR)
                {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    Log.i(MapActivity.TAG, status.getStatusMessage());
                }
                else if (resultCode == RESULT_CANCELED)
                {
                    Log.i(MapActivity.TAG, "Search cancelled by user");

                }
        }

    }

    public FabellasAppWidgetConfigureActivity()
    {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Write the ID of the place
    static void saveIDPref(Context context, int appWidgetId, String id)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_ID, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, id);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null)
        {
            return titleValue;
        }
        else
        {
            return context.getString(R.string.appwidget_text);
        }
    }

    static String loadIDPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_ID, 0);
        String idValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (idValue != null)
        {
            return idValue;
        }
        else
        {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    static void deleteIDPref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_ID, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_configure_layout);
        findViewById(R.id.add_button).setOnClickListener(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
        {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            finish();
            return;
        }

    }

    @Override
    public void onClick(View view)
    {
        try
        {
            final Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(null)
                    .build(FabellasAppWidgetConfigureActivity.this);
            startActivityForResult(intent, MapActivity.PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }
        catch (GooglePlayServicesRepairableException e)
        {
            Toast.makeText(FabellasAppWidgetConfigureActivity.this, "An error occured", Toast.LENGTH_SHORT).show();

        }
        catch (GooglePlayServicesNotAvailableException e)
        {
            Toast.makeText(FabellasAppWidgetConfigureActivity.this, "Google services not availables", Toast.LENGTH_SHORT).show();
        }
            /*
            final Context context = FabellasAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            FabellasAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
            */
    }
}

