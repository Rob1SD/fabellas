package groupe_9.com.fabellas.bo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by spyro on 17/01/2018.
 */

@IgnoreExtraProperties
public class Story {

    public String detail;
    public String placeId;
    public String title;
    public String userId;

    public Story() {

    }

    public Story(String detail, String placeId, String title, String userId) {
        this.detail = detail;
        this.placeId = placeId;
        this.title = title;
        this.userId = userId;
    }

}