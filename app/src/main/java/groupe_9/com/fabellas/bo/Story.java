package groupe_9.com.fabellas.bo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by spyro on 17/01/2018.
 */

@IgnoreExtraProperties
public class Story implements Serializable
{

    private String UID;
    private String detail;
    private String placeId;
    private String title;
    private String userId;
    private float rate;

    public Story()
    {

    }

    public Story(String UID, String detail, String placeId, String title, String userId, float rate)
    {
        this.UID = UID;
        this.detail = detail;
        this.placeId = placeId;
        this.title = title;
        this.userId = userId;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Story)
        {
            return ((Story) obj).UID.equals(UID);
        }
        return false;
    }

    public String getUID()
    {
        return UID;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

    public String getPlaceId()
    {
        return placeId;
    }

    public void setPlaceId(String placeId)
    {
        this.placeId = placeId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public float getRate()
    {
        return rate;
    }

    public void setRate(float rate)
    {
        this.rate = rate;
    }

    public void clone(Story other)
    {
        if (other == null) return;
        this.setRate(other.getRate());
        this.setTitle(other.getTitle());
        this.setDetail(other.getDetail());
    }
}