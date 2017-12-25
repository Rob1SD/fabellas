package groupe_9.com.fabellas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class FabellasActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    protected void setUpToolbar(AppCompatActivity activity, String title, boolean showBackArrow)
    {
        final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        final TextView toolbarTitle = toolbar.findViewById(R.id.title);
        toolbarTitle.setText(title);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(showBackArrow);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(showBackArrow);
    }

}
