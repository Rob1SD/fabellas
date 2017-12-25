package groupe_9.com.fabellas.bo;

import java.io.Serializable;

/**
 * Created by thoma on 25/12/2017.
 */

public class PlaceTag implements Serializable
{
    private String title;
    private String id;

    public PlaceTag(String title, String id)
    {
        this.title = title;
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
