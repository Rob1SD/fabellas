package groupe_9.com.fabellas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.bo.Story;

public class StorieDetailFragment extends Fragment
{
    public static final String STORIE_EXTRA = "storieExtra";

    private Story storie;

    public StorieDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().getSerializable(STORIE_EXTRA) != null)
        {
            storie = (Story) getArguments().getSerializable(STORIE_EXTRA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.storie_detail, container, false);

        if (storie != null)
        {
            ((TextView) rootView.findViewById(R.id.storie_detail)).setText(storie.detail);
        }

        return rootView;
    }
}
