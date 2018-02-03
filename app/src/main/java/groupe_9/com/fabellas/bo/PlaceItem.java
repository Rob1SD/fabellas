package groupe_9.com.fabellas.bo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by thoma on 30/01/2018.
 */

public class PlaceItem implements ClusterItem
{
    private final double latitude;
    private final double longitude;
    private PlaceTag placeTag;


    public static final String TABLE_NAME = "Place";
    public static final String COL_ID = "ID";
    public static final String COL_LATITUDE = "LATITUDE";
    public static final String COL_LONGITUDE = "LONGITUDE";
    public static final String COL_TITLE = "TITLE";


    public PlaceItem(PlaceTag placeTag, double latitude, double longitude)
    {
        this.placeTag = placeTag;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public LatLng getPosition()
    {
        return new LatLng(latitude, longitude);
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

    @Override
    public String toString()
    {
        return getTitle() + " (latitude : " + getLatitude() + ", longitude : " + getLongitude() + ")";
    }

    public PlaceTag getPlaceTag()
    {
        return placeTag;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public String getID()
    {
        return placeTag.getId();
    }
}