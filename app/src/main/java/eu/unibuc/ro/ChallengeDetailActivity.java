package eu.unibuc.ro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import eu.unibuc.ro.database.dao.IChallengeDao;
import eu.unibuc.ro.database.models.Challenge;
import eu.unibuc.ro.database.models.User;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static eu.unibuc.ro.fragments.ChallengeFragment.CHALLENGE_TYPE;
import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

import javax.inject.Inject;

public class ChallengeDetailActivity extends AppCompatActivity {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private Chronometer chronometer;
    private Intent intent;
    private String type;
    private Challenge challenge;
    private User user;
    private TextView tvDays;
    private Button btnStartChallenge;
    @Inject
    IChallengeDao challengeDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerApplicationComponent.builder().roomModule(new RoomModule(this)).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);
        intent = getIntent();
        initComponents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private void initComponents() {
        TextView tvTitle = findViewById(R.id.challenge_detail_tv_title);
        btnStartChallenge = findViewById(R.id.challenge_detail_btn_start_challenge);
        TextView tvStopChallenge = findViewById(R.id.challenge_detail_tv_give_up);
        tvDays = findViewById(R.id.challenge_detail_tv_days);
        chronometer = findViewById(R.id.challenge_detail_chronometer);

        user = (User) intent.getSerializableExtra(CURRENT_USER);

        if (intent.hasExtra(CHALLENGE_TYPE)) {
            type = intent.getStringExtra(CHALLENGE_TYPE);
            switch (type) {
                case "noCoffee": {
                    tvTitle.setText(getString(R.string.challenge_detail_title_no_coffee));
                    break;
                }
                case "noFastFood": {
                    tvTitle.setText(getString(R.string.challenge_detail_title_no_fast_food));
                    break;
                }
                case "noSmoking": {
                    tvTitle.setText(getString(R.string.challenge_detail_title_no_smoking));
                    break;
                }
                case "noSoda": {
                    tvTitle.setText(getString(R.string.challenge_detail_title_no_soda));
                    break;
                }
                case "noSweets": {
                    tvTitle.setText(getString(R.string.challenge_detail_title_no_sweets));
                    break;
                }
            }

            setChronometer();

            btnStartChallenge.setOnClickListener(v -> {
                Date currentDate = new Date();
                challenge = new Challenge(type, currentDate, null, user.getId());
                mDisposable.add(challengeDao.insertChallenge(challenge)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    Toast.makeText(getApplicationContext(), getString(R.string.challenge_saved), Toast.LENGTH_LONG).show();
                                    setChronometer();
                                },
                                throwable -> Toast.makeText(getApplicationContext(), getString(R.string.insert_not_saved), Toast.LENGTH_LONG).show()));
            });

            tvStopChallenge.setOnClickListener(v -> {
                mDisposable.add(challengeDao.getChallengeByNameAndDateEnd(type, user.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(challenge -> {
                            challenge.setDateEnd(new Date());
                            mDisposable.add(challengeDao.updateChallenge(challenge)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        chronometer.stop();
                                        btnStartChallenge.setEnabled(true);
                                        Toast.makeText(getApplicationContext(), getString(R.string.challenge_aborted), Toast.LENGTH_LONG).show();
                                    }));
                        }, Throwable::printStackTrace));
            });

        }

    }

    private void setChronometer() {
        mDisposable.add(challengeDao.getChallengeByNameAndDateEnd(type, user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(challenge -> {
                    btnStartChallenge.setEnabled(false);
                    Date startDateTime = challenge.getDateStart();
                    Date currentDateTime = new Date();
                    long different = currentDateTime.getTime() - startDateTime.getTime();

                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;

                    long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;

                    long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;

                    long elapsedSeconds = different / secondsInMilli;

                    tvDays.setText(elapsedDays > 1 ? elapsedDays + getString(R.string.time_day) : elapsedDays + getString(R.string.time_dayy));
                    tvDays.setVisibility(elapsedDays > 0 ? View.VISIBLE : View.GONE);


                    int elapsedTimeMilliseconds = (int) (elapsedHours * 60 * 60 * 1000
                            + elapsedMinutes * 60 * 1000
                            + elapsedSeconds * 1000);
                    chronometer.setBase(SystemClock.elapsedRealtime() - elapsedTimeMilliseconds);
                    chronometer.start();
                }, Throwable::printStackTrace));
    }
}
