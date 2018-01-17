package groupe_9.com.fabellas.bo;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by spyro on 17/01/2018.
 */

@IgnoreExtraProperties
public class Story implements Serializable
{

    public String UID;
    public String detail;
    public String placeId;
    public String title;
    public String userId;

    public Story()
    {

    }

    public Story(String UID, String detail, String placeId, String title, String userId)
    {
        this.UID = UID;
        this.detail = detail;
        this.placeId = placeId;
        this.title = title;
        this.userId = userId;
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
}