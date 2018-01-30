package groupe_9.com.fabellas.bo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by thoma on 30/01/2018.
 */

public class PlaceItem implements ClusterItem
{
    private final LatLng mPosition;
    private PlaceTag placeTag;

    public PlaceItem(PlaceTag placeTag, double lat, double lng)
    {
        this.placeTag = placeTag;
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition()
    {
        return mPosition;
    }

    @Override
    public String getTitle()
    {
        return placeTag.getTitle();
    }

    @Override
    public String getSnippet()
    {
        return null;
    }

    public PlaceTag getPlaceTag()
    {
        return placeTag;
    }
}