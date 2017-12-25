package groupe_9.com.fabellas.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import groupe_9.com.fabellas.MapActivity;
import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.bo.PlaceTag;

public class PlaceStoriesFragment extends Fragment
{
    private String placeID;
    private String placeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_place_stories, container, false);

        final Bundle bundle = getArguments();
        if (bundle != null)
        {
            if (bundle.getSerializable(MapActivity.PLACE_ID) != null)
            {
                this.placeID = ((PlaceTag) bundle.getSerializable(MapActivity.PLACE_ID)).getId();
                this.placeName = ((PlaceTag) bundle.getSerializable(MapActivity.PLACE_ID)).getTitle();
                ((TextView) view.findViewById(R.id.name)).setText(this.placeName);
                ((TextView) view.findViewById(R.id.id)).setText(this.placeID);
            }

        }

        return view;
    }

}
