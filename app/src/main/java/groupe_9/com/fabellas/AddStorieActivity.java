package groupe_9.com.fabellas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

        (findViewById(R.id.addStorieButton)).setOnClickListener(this);
    }



    public void onClick(View v)
    {
        if (v.getId() == R.id.addStorieButton)
        {
            Log.i(MapActivity.TAG, String.format("JE CLIQUE SUR LE BOUTTON VALIDER L'ANECDOTE de titre : " + title.getText().toString()));
            Intent intent = new Intent();
            intent.putExtra(TITLE_VALUE, title.getText().toString());
            intent.putExtra(DETAILS_VALUE, details.getText().toString());
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
