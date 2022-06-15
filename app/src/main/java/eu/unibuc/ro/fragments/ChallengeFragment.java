package eu.unibuc.ro.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import eu.unibuc.ro.ChallengeDetailActivity;
import eu.unibuc.ro.R;
import eu.unibuc.ro.database.models.User;

import static eu.unibuc.ro.LoginActivity.USER_KEY;
import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

public class ChallengeFragment extends Fragment {

    public final static String CHALLENGE_TYPE = "challenge_type";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.challenge_fragment, container, false);
        initComponents(view);

        return view;
    }

    private void initComponents(View view) {
        User user = (User) requireActivity().getIntent().getSerializableExtra(USER_KEY);

        ImageButton btnNoCoffee = view.findViewById(R.id.challenges_ib_no_coffee);
        ImageButton btnNoFastFood = view.findViewById(R.id.challenges_ib_no_fast_food);
        ImageButton btnNoSmoking = view.findViewById(R.id.challenges_ib_no_smoking);
        ImageButton btnNoSoda = view.findViewById(R.id.challenges_ib_no_soda);
        ImageButton btnNoSweets = view.findViewById(R.id.challenges_ib_no_sweets);


        btnNoCoffee.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChallengeDetailActivity.class);
            intent.putExtra(CHALLENGE_TYPE, getString(R.string.challenge_detail_no_coffee));
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });

        btnNoFastFood.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChallengeDetailActivity.class);
            intent.putExtra(CHALLENGE_TYPE, getString(R.string.challenge_detail_no_fast_food));
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });

        btnNoSmoking.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChallengeDetailActivity.class);
            intent.putExtra(CHALLENGE_TYPE, getString(R.string.challenge_detail_no_smoking));
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });

        btnNoSoda.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChallengeDetailActivity.class);
            intent.putExtra(CHALLENGE_TYPE, getString(R.string.challenge_detail_no_soda));
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });

        btnNoSweets.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChallengeDetailActivity.class);
            intent.putExtra(CHALLENGE_TYPE, getString(R.string.challenge_detail_no_sweets));
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });
    }
}
