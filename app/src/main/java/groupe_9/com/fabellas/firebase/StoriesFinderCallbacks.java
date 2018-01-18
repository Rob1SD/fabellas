package groupe_9.com.fabellas.firebase;

import groupe_9.com.fabellas.bo.Story;

/**
 * Created by thoma on 19/01/2018.
 */

public interface StoriesFinderCallbacks
{
    void onStartLooking();

    void onStorieFound(Story story);

    void onPlaceNotFound();

    void onStoryRemoved(Story story);
}
