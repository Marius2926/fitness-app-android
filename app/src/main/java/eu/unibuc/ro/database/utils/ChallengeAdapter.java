package eu.unibuc.ro.database.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;

import eu.unibuc.ro.R;
import eu.unibuc.ro.database.models.Challenge;


public class ChallengeAdapter extends ArrayAdapter<Challenge> {

    public ChallengeAdapter(@NonNull Context context, List<Challenge> challenges, LayoutInflater inflater) {
        super(context, R.layout.challenge_report_lv_row, challenges);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.challenge_report_lv_row, parent, false);
        }
        Challenge challenge = getItem(position);
        assert challenge != null;

        TextView tvChallengeType = currentItemView.findViewById(R.id.challenge_lv_row_tv_eaten_drunk_or_smoked);
        TextView tvPeriod = currentItemView.findViewById(R.id.challenge_tv_row_time_period);

        switch (challenge.getName()) {
            case "noCoffee": {
                tvChallengeType.setText(getContext().getResources().getString(R.string.challenge_lv_row_no_coffee));
                break;
            }
            case "noFastFood": {
                tvChallengeType.setText(getContext().getResources().getString(R.string.challenge_tv_row_no_fastFood));
                break;
            }
            case "noSmoking": {
                tvChallengeType.setText(getContext().getResources().getString(R.string.challenge_row_lv_no_smoking));
                break;
            }
            case "noSoda": {
                tvChallengeType.setText(getContext().getResources().getString(R.string.challenge_row_lv_no_soda));
                break;
            }
            case "noSweets": {
                tvChallengeType.setText(getContext().getResources().getString(R.string.challenge_row_lv_no_sweets));
                break;
            }
        }

        Date d1 = challenge.getDateStart();
        Date d2 = challenge.getDateEnd();

        //in milliseconds
        long diff = d2.getTime() - d1.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String result = (diffDays != 0 ? diffDays + getContext().getResources().getString(R.string.challenge_dapter_days) : "") + " "
                + (diffHours != 0 ? diffHours + getContext().getResources().getString(R.string.challenge_adapter_hours) : "") + " "
                + diffMinutes + " " + getContext().getResources().getString(R.string.challenge_adapter_minutes) + " "
                + diffSeconds + " " + getContext().getResources().getString(R.string.challenge_adapter_seconds);

        tvPeriod.setText(result);
        return currentItemView;
    }
}
