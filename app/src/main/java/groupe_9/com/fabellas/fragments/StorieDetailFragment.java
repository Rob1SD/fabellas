package groupe_9.com.fabellas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import groupe_9.com.fabellas.DummyContent;
import groupe_9.com.fabellas.R;

public class StorieDetailFragment extends Fragment
{
    public static final String ARG_ITEM_ID = "item_id";

    private DummyContent.DummyItem storie;

    public StorieDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            storie = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.storie_detail, container, false);

        if (storie != null)
        {
            ((TextView) rootView.findViewById(R.id.storie_detail)).setText(storie.details);
        }

        return rootView;
    }
}
