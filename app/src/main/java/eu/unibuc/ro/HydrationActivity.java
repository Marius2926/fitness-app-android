package eu.unibuc.ro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.Date;

import eu.unibuc.ro.database.dao.IHydrationLevelDao;
import eu.unibuc.ro.database.models.HydrationLevel;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.services.HydrationService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

import javax.inject.Inject;

public class HydrationActivity extends AppCompatActivity {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    public final static String WATER_PROGRESS_KEY = "water_progress_key";
    @Inject
    IHydrationLevelDao hydrationLevelDao;
    Button btnGlass;
    ProgressBar pbWater;
    float currentProgress;
    TextView tvTest;
    TextView tvProgressLevel;
    Intent intent;
    User user;
    HydrationLevel hydrationLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerApplicationComponent.builder().roomModule(new RoomModule(this)).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydration);

        intent = getIntent();
        initComponents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private void initComponents() {
        btnGlass = findViewById(R.id.hydration_ib_glass);
        pbWater = findViewById(R.id.hydrate_pb_water);
        tvProgressLevel = findViewById(R.id.hydration_tv_progress_level);

        tvTest = findViewById(R.id.hydration_tv_quantity);

        user = (User) intent.getSerializableExtra(CURRENT_USER);

        getRegisteredDays();

        btnGlass.setOnClickListener(v -> {
            if (pbWater.getProgress() < 100) {
                setWaterProgress();
            }
            hydrationLevel.setLevelValue(currentProgress);

            new HydrationService.UpdateHydrationLevel(getApplicationContext()).execute(hydrationLevel);
        });

    }

    private void setWaterProgress() {
        currentProgress += 12.5;
        pbWater.setProgress((int) currentProgress);
        tvProgressLevel.setText(MessageFormat.format("{0}{1}", currentProgress, getResources().getString(R.string.number_water_Progress)));
    }

    private void resetWaterProgress() {
        currentProgress = 0;
        pbWater.setProgress(0);
        tvProgressLevel.setText(String.valueOf(getResources().getString(R.string.zero_water_progress)));
    }

    @Override
    public void onBackPressed() {
        if (pbWater.getProgress() >= 0) {
            intent.putExtra(WATER_PROGRESS_KEY, currentProgress);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void getRegisteredDays() {
        long time = new Date().getTime();
        Date date = new Date(time - time % (24 * 60 * 60 * 1000));

        mDisposable.add(hydrationLevelDao.getHydrationLevelByRegistrationDay(date, user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    hydrationLevel = result;
                    currentProgress = hydrationLevel.getLevelValue();
                    pbWater.setProgress((int) currentProgress);
                    tvProgressLevel.setText(MessageFormat.format("{0}{1}", currentProgress, getResources().getString(R.string.number_water_Progress)));
                }, error -> {
                    resetWaterProgress();
                    hydrationLevel = new HydrationLevel(0, new Date(), user.getId());
                    new HydrationService.InsertHydrationLevel(getApplicationContext()).execute(hydrationLevel);
                }));
    }
}

