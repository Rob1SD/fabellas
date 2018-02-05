package groupe_9.com.fabellas.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import groupe_9.com.fabellas.ConnectionActivity;
import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.firebase.Utils;
import groupe_9.com.fabellas.utils.OnDetailStoryRemoval;

public class StorieDetailFragment extends Fragment implements View.OnClickListener
{
    public static final String STORIE_EXTRA = "storieExtra";

    private RatingBar ratingBar;
    private TextView detail;
    private Button validate;
    private ProgressDialog progressDialog;

    private Story storie;

    private DatabaseReference mStoriesMyNotationDatabaseReference;
    private DatabaseReference mStoriesRateDatabaseReference;
    private DatabaseReference mStorieDatabaseReference;

    public StorieDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().getSerializable(STORIE_EXTRA) == null)
        {
            onDestroy();
        }

        storie = (Story) getArguments().getSerializable(STORIE_EXTRA);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.storie_detail, container, false);

        if (storie != null)
        {
            detail = rootView.findViewById(R.id.storie_detail);
            ratingBar = rootView.findViewById(R.id.ratingBar);
            validate = rootView.findViewById(R.id.validate);

            detail.setText(storie.getDetail());

            validate.setOnClickListener(this);

            mStorieDatabaseReference = Utils.getDatabase().getReference("Stories").child(storie.getUID());
            mStorieDatabaseReference.addValueEventListener(getNotationExistenceListener());

            mStoriesRateDatabaseReference = Utils.getDatabase().getReference("Stories")
                    .child(storie.getUID()).child("rate");


            mStoriesRateDatabaseReference.addValueEventListener(getStoryNotationListener());

            if (FirebaseAuth.getInstance().getCurrentUser() == null)
            {
                final Intent intent = new Intent(getContext(), ConnectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
            else
            {
                if (getUserPossibilityToRate())
                {
                    mStoriesMyNotationDatabaseReference = Utils.getDatabase().getReference("Stories")
                            .child(storie.getUID()).child("notations")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mStoriesMyNotationDatabaseReference.addValueEventListener(getMyNotationListener());
                    changeNotationBarStatus(true);
                }
                else
                {
                    changeNotationBarStatus(false);
                }
            }


        }
        return rootView;
    }

    private void changeNotationBarStatus(boolean status)
    {
        validate.setVisibility(status ? View.VISIBLE : View.GONE);
        ratingBar.setIsIndicator(!status);
    }

    private boolean getUserPossibilityToRate()
    {
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
        {
            return false;
        }

        if (storie.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            return false;
        }
        return true;
    }

    private ValueEventListener getMyNotationListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                changeNotationBarStatus(null == dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        };
    }

    private ValueEventListener getNotationExistenceListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot != null)
                {
                    if (dataSnapshot.getValue(Story.class) == null)
                    {
                        handleStoryRemoval();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
    }

    private void handleStoryRemoval()
    {
        ((OnDetailStoryRemoval) getActivity()).onActualDetailStoryRemove();
    }

    private ValueEventListener getStoryNotationListener()
    {
        return new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (null != dataSnapshot.getValue())
                {
                    ratingBar.setRating(dataSnapshot.getValue(float.class));
                }
                else
                {
                    ratingBar.setRating(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };
    }

    @Override
    public void onClick(View view)
    {
        addRate();
    }

    private void addRate()
    {
        progressDialog.setMessage(getString(R.string.adding_rate_message, ratingBar.getRating()));

        progressDialog.show();

        mStoriesMyNotationDatabaseReference.setValue(ratingBar.getRating());

        final DatabaseReference mStoriesNotationDatabaseReference =
                Utils.getDatabase().getReference("Stories").child(storie.getUID()).child("notations");
        mStoriesNotationDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                float totalNotation = 0;
                Map<String, Float> notationMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, Float>>()
                {
                });
                for (Float f : notationMap.values())
                {
                    totalNotation += f;
                }
                totalNotation /= notationMap.size();
                Utils.getDatabase().getReference("Stories").child(storie.getUID()).child("rate").setValue(totalNotation);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
            }
        });
    }
}
