package groupe_9.com.fabellas.fragments;

import android.app.ProgressDialog;
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

import groupe_9.com.fabellas.R;
import groupe_9.com.fabellas.bo.Story;
import groupe_9.com.fabellas.firebase.Utils;

public class StorieDetailFragment extends Fragment implements View.OnClickListener
{
    public static final String STORIE_EXTRA = "storieExtra";

    private RatingBar ratingBar;
    private TextView detail;
    private Button validate;
    private ProgressDialog progressDialog;

    private Story storie;

    private DatabaseReference mStoriesMyNotationDatabaseReference;

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

        mStoriesMyNotationDatabaseReference = Utils.getDatabase().getReference("Stories").child(storie.getUID()).child("notations").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mStoriesMyNotationDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    validate.setVisibility(View.VISIBLE);
                    ratingBar.setIsIndicator(false);
                }
                else{
                    validate.setVisibility(View.GONE);
                    ratingBar.setIsIndicator(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
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


            validate.setOnClickListener(this);

            final boolean userRatePossibility =
                    !(FirebaseAuth.getInstance().getCurrentUser().isAnonymous() ||
                            storie.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()));

            validate.setVisibility(userRatePossibility ? View.VISIBLE : View.GONE);
            detail.setText(storie.getDetail());
            ratingBar.setIsIndicator(!userRatePossibility);
            ratingBar.setRating(storie.getRate());

        }

        return rootView;
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
        mStoriesNotationDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float totalNotation = 0;
                Map<String, Float> notationMap = dataSnapshot.getValue(new GenericTypeIndicator<Map<String, Float>>(){});
                for (Float f: notationMap.values()) {
                    totalNotation += f;
                }
                totalNotation /= notationMap.size();
                Utils.getDatabase().getReference("Stories").child(storie.getUID()).child("rate").setValue(totalNotation);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        //mDatabaseReference.child(user.getUid()).setValue(new User(user.getUid(), user.getPhoneNumber(), null));

        /*
            Ici, JEAN, tu peux faire tes appels pour
                calculer la nouvelle moyenne
                mettre à jour la note de la story
                ajouter cette nouvelle note dans la liste des notes de la story

                Lorsque tu sera sûr, dans tes listeners, que la note a bien été prise en compte
                tu pourra appeler "progressDialog.dismiss();" à l'endroit !
                ça enlevera le loader

                puisque tu auras alors calculer la nouvelle moyenne, tu peux d'ailleurs la mettre à jour dans la vue
                en faisant
                - ratingBar.setRating(laNouvelleNote);

                Attention : Après ce commentaire, j'ai fais un Fake Timer pour dismiss le loader après 3 secondes
                ça ne sert qu'à ne pas bloquer la page le temps que tu mettes les listener
                Une fois que tu auras fais le "dismiss" la ou il faut, tu pourra senlever les lignes après ce commentaire :)

         */
    }
}
