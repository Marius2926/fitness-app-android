package eu.unibuc.ro.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import eu.unibuc.ro.DaggerApplicationComponent;
import eu.unibuc.ro.ExerciseActivity;
import eu.unibuc.ro.HydrationActivity;
import eu.unibuc.ro.NutritionActivity;
import eu.unibuc.ro.R;
import eu.unibuc.ro.RoomModule;
import eu.unibuc.ro.database.dao.IEatenAlimentsDao;
import eu.unibuc.ro.database.dao.IHydrationLevelDao;
import eu.unibuc.ro.database.models.EatenAliment;
import eu.unibuc.ro.database.models.HydrationLevel;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.services.EatenAlimentService;
import eu.unibuc.ro.database.services.HydrationService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


import static android.app.Activity.RESULT_OK;

import static eu.unibuc.ro.HydrationActivity.WATER_PROGRESS_KEY;
import static eu.unibuc.ro.LoginActivity.USER_KEY;
import static eu.unibuc.ro.NutritionActivity.EATEN_CALORIES_KEY;

import javax.inject.Inject;

public class DiaryFragment extends Fragment {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final int REQUEST_CODE_HYDRATION_LEVEL = 100;
    private static final int REQUEST_CODE_GET_EATEN_CALORIES = 101;
    public static final String CURRENT_USER = "current_user";
    private static final String SAVE_INSTANCE_WATER_PROGRESS = "save_instance_water_progress";
    private static final String SAVE_INSTANCE_TV_WATER_PROGRESS = "save_instance_tv_water_progress";
    private static final String SAVE_INSTANCE_EATEN_CAL_NUMBER = "save_instance_eaten_cal";
    private static final String SAVE_INSTANCE_REMAINING_CAL_NUMBER = "save_instance_remaining_cal";
    private ProgressBar pbCalories;
    private ProgressBar pbWater;
    private TextView tvWaterProgress;
    private TextView tvEatenCalories;
    private TextView tvRemainingCalories;
    private TextView tvGoalCalories;
    private Intent intent;
    private User user;
    private int eatenCalories;
    @Inject
    IEatenAlimentsDao alimentsDao;
    @Inject
    IHydrationLevelDao hydrationLevelDao;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        DaggerApplicationComponent.builder().roomModule(new RoomModule(context)).build().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.diary_fragment, container, false);
        intent = requireActivity().getIntent();
        initComponents(view);

        if (savedInstanceState != null) {
            pbWater.setProgress(savedInstanceState.getInt(SAVE_INSTANCE_WATER_PROGRESS));
            tvWaterProgress.setText(savedInstanceState.getCharSequence(SAVE_INSTANCE_TV_WATER_PROGRESS));
            tvEatenCalories.setText(savedInstanceState.getCharSequence(SAVE_INSTANCE_EATEN_CAL_NUMBER));
            tvRemainingCalories.setText(savedInstanceState.getCharSequence(SAVE_INSTANCE_REMAINING_CAL_NUMBER));
        } else {
            tvEatenCalories.setText("0");
            tvWaterProgress.setText(getResources().getString(R.string.zero_water_progress));
            pbWater.setProgress(0);
        }

        return view;
    }

    private void initComponents(View view) {

        ImageButton btnNutrition = view.findViewById(R.id.diary_fragment_ib_apple);
        ImageButton btnExercise = view.findViewById(R.id.diary_fragment_ib_runner);
        ImageButton btnHydrate = view.findViewById(R.id.diary_fragment_ib_water);
        pbCalories = view.findViewById(R.id.diary_progress_bar_calories);
        pbWater = view.findViewById(R.id.diary_progress_bar_water);
        tvWaterProgress = view.findViewById(R.id.diary_fragment_tv_progress_level);
        tvEatenCalories = view.findViewById(R.id.diary_tv_eaten_calories_value);
        tvGoalCalories = view.findViewById(R.id.diary_tv_goal_calories_value);
        tvRemainingCalories = view.findViewById(R.id.diary_fragment_tv_remaining_calories);

        user = (User) intent.getSerializableExtra(USER_KEY);
        tvGoalCalories.setText(String.valueOf(Math.round(user.getCaloriesNumber())));
        tvRemainingCalories.setText(String.valueOf(Math.round(user.getCaloriesNumber())));
        pbCalories.setProgress(0);

        long time = new Date().getTime();
        Date date = new Date(time - time % (24 * 60 * 60 * 1000));
        mDisposable.add(alimentsDao.getTodayEatenCalories(date, user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (Float sum) -> {
                            eatenCalories = Math.round(sum);
                            tvEatenCalories.setText(String.valueOf(eatenCalories));
                            tvGoalCalories.setText(String.valueOf(Math.round(user.getCaloriesNumber())));
                            int remainingCalories = calculateRemainingCalories(eatenCalories, Math.round(user.getCaloriesNumber()));
                            tvRemainingCalories.setText(String.valueOf(remainingCalories));
                            int calProgress = calculateCaloriesProgress(Math.round(user.getCaloriesNumber()), eatenCalories);
                            pbCalories.setProgress(calProgress);
                        }, Throwable::printStackTrace));

        mDisposable.add(hydrationLevelDao.getHydrationLevelByRegistrationDay(date, user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hydrationLevel -> {
                    pbWater.setProgress((int) hydrationLevel.getLevelValue());
                    tvWaterProgress.setText(MessageFormat.format("{0}{1}", hydrationLevel.getLevelValue(), getResources().getString(R.string.number_water_Progress)));
                }, Throwable::printStackTrace));


        btnNutrition.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NutritionActivity.class);
            intent.putExtra(CURRENT_USER, user);
            startActivityForResult(intent, REQUEST_CODE_GET_EATEN_CALORIES);
        });

        btnExercise.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ExerciseActivity.class);
            startActivity(intent);
        });

        btnHydrate.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), HydrationActivity.class);
            intent.putExtra(CURRENT_USER, user);
            startActivityForResult(intent, REQUEST_CODE_HYDRATION_LEVEL);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_HYDRATION_LEVEL && resultCode == RESULT_OK && data != null) {
            double progressWater = data.getFloatExtra(WATER_PROGRESS_KEY, 0);
            if (progressWater >= 0) {
                pbWater.setProgress((int) progressWater);
                tvWaterProgress.setText(MessageFormat.format("{0}{1}", String.valueOf(progressWater), getResources()
                        .getString(R.string.number_water_Progress)));
            }
        }

        if (requestCode == REQUEST_CODE_GET_EATEN_CALORIES && resultCode == RESULT_OK && data != null) {
            int eatenCaloriesNumber = data.getIntExtra(EATEN_CALORIES_KEY, 0);
            tvEatenCalories.setText(String.valueOf(eatenCaloriesNumber));
            int remainingCalories = calculateRemainingCalories(eatenCaloriesNumber, Math.round(user.getCaloriesNumber()));
            tvRemainingCalories.setText(String.valueOf(remainingCalories));
            pbCalories.setProgress(calculateCaloriesProgress(Math.round(user.getCaloriesNumber()), eatenCaloriesNumber));
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_WATER_PROGRESS, pbWater.getProgress());
        outState.putCharSequence(SAVE_INSTANCE_TV_WATER_PROGRESS, tvWaterProgress.getText());
        outState.putCharSequence(SAVE_INSTANCE_EATEN_CAL_NUMBER, tvEatenCalories.getText());
        outState.putCharSequence(SAVE_INSTANCE_REMAINING_CAL_NUMBER, tvRemainingCalories.getText());
    }

    private int calculateRemainingCalories(int eatenCalories, int goalCalories) {

        if (goalCalories > eatenCalories) {
            return goalCalories - eatenCalories;
        }

        return 0;
    }

    private int calculateCaloriesProgress(int goalCalories, int eatenCalories) {

        return (eatenCalories * 100) / goalCalories;
    }
}
