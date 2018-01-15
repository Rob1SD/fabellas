package groupe_9.com.fabellas.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by thoma on 15/01/2018.
 */

public class WidgetRemoteViewsService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new AppWidgetAdapterFactory(getApplicationContext(), intent);
    }
}
