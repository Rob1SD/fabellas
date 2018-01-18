package groupe_9.com.fabellas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddStorieActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private TextView details;
    public static final String TITLE_VALUE = "TITLE_VALUE";
    public static final String DETAILS_VALUE = "DETAILS_VALUE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_storie);
        title = findViewById(R.id.storieTitle);
        details = findViewById(R.id.storieDetails);

        setUpToolbar();

        (findViewById(R.id.addStorieButton)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.addStorieButton)
        {
            Log.i(MapActivity.TAG, String.format("JE CLIQUE SUR LE BOUTTON VALIDER L'ANECDOTE de titre : " + title.getText().toString()));
            String titleText = title.getText().toString();
            String detailsText = details.getText().toString();
            if (titleText.length() == 0 || detailsText.length() == 0)
            {
                Toast.makeText(this,R.string.error_empty_story_title_or_details,Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(TITLE_VALUE, titleText);
            intent.putExtra(DETAILS_VALUE, detailsText);
            setResult(RESULT_OK,intent);
            finish();
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

        toolbarTitle.setText(getString(R.string.add_storie_title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
