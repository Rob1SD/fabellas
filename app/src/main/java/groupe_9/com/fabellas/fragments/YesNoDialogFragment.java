package groupe_9.com.fabellas.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import groupe_9.com.fabellas.UserStoriesActivity;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.bo.User;

/**
 * Created by spyro on 29/01/2018.
 */

public class YesNoDialogFragment extends DialogFragment {


    public YesNoDialogFragment() {

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Etes-vous sûr de vouloir supprimer cette anecdote ?");
        //null should be your on click listener-
        alertDialogBuilder.setPositiveButton("OUI", (dialog, i) -> {
            UserStoriesActivity callingActivity = (UserStoriesActivity) getActivity();
            Bundle bundle = getArguments();
            Story clickedStory = (Story)bundle.getSerializable(UserStoriesActivity.BUNDLE_STORY);
            callingActivity.deleteClickedStory(clickedStory);
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton("NON", (dialog, which) -> dialog.dismiss());


        return alertDialogBuilder.create();
    }
}
