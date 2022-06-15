package eu.unibuc.ro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.services.EatenAlimentService;
import eu.unibuc.ro.database.services.HydrationService;

import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

public class AchievementsReportActivity extends AppCompatActivity {

    public static final String FILE_NAME = "achievementsReport.txt";
    private TextView tvCalories;
    private TextView tvImc;
    private TextView tvUserName;
    private TextView tvAverageCalories;
    private TextView tvAverageHydration;
    private TextView tvImcText;
    private TextView tvCaloriesText1;
    private TextView tvCaloriesText2;
    private TextView tvAchievementsSinceJoin;
    private TextView tvAverageCaloriesText;
    private TextView tvAverageCaloriesPerDay;
    private TextView tvAverageHydrationText;
    private TextView tvHydrationPercent;
    private Intent intent;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_report);
        intent = getIntent();
        initComponents();
    }

    private void initComponents() {
        tvUserName = findViewById(R.id.achievements_report_tv_user_name);
        tvCalories = findViewById(R.id.achievements_report_tv_calories_number_value);
        tvImc = findViewById(R.id.achievements_report_tv_imc_value);
        tvAverageCalories = findViewById(R.id.achievements_report_tv_average_calories_value);
        tvAverageHydration = findViewById(R.id.achievements_report_tv_hydration_average_value);
        tvImcText = findViewById(R.id.achievements_report_tv_your_imc_is);
        tvCaloriesText1 = findViewById(R.id.achievements_report_tv_calories_text_1);
        tvCaloriesText2 = findViewById(R.id.achievements_report_tv_calories_text_2);
        tvAchievementsSinceJoin = findViewById(R.id.achievements_report_tv_achievements_since_join);
        tvAverageCaloriesText = findViewById(R.id.achievements_tv_average_calories_text);
        tvAverageCaloriesPerDay = findViewById(R.id.achievements_report_tv_average_cal_per_day);
        tvAverageHydrationText = findViewById(R.id.achievements_report_tv_hydration_average_text);
        tvHydrationPercent = findViewById(R.id.achievements_report_tv_hydration_percent);
        Button btnDownload = findViewById(R.id.achievements_report_btn_download);
        Button btnChart = findViewById(R.id.achievements_report_btn_chart);


        if (intent.hasExtra(CURRENT_USER)) {
            user = (User) intent.getSerializableExtra(CURRENT_USER);
        }


        tvUserName.setText(user.getName());
        tvImc.setText(String.valueOf(Math.round(user.getImc())));
        tvCalories.setText(String.valueOf(Math.round(user.getCaloriesNumber())));

        new EatenAlimentService.GetCaloriesAverage(getApplicationContext()) {
            @Override
            protected void onPostExecute(Float avg) {
                super.onPostExecute(avg);
                if (avg != -1) {
                    tvAverageCalories.setText(String.valueOf(Math.round(avg)));
                }
            }
        }.execute(user.getId());

        new HydrationService.GetHydrationAverage(getApplicationContext()) {
            @Override
            protected void onPostExecute(Float avg) {
                super.onPostExecute(avg);
                if (avg != null) {
                    tvAverageHydration.setText(String.valueOf(Math.round(avg)));
                }
            }
        }.execute(user.getId());


        btnChart.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });

        btnDownload.setOnClickListener(v -> {

            String text = tvUserName.getText().toString() + " " + tvImcText.getText().toString()
                    + " " + tvImc.getText().toString() + " " + tvCaloriesText1.getText().toString()
                    + " " + tvCaloriesText2.getText().toString() + " " + tvCalories.getText().toString();
            FileOutputStream fos = null;

            try {
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(text.getBytes());

                fos.write("\n".getBytes());

                fos.write(tvAchievementsSinceJoin.getText().toString().getBytes());
                fos.write("\n".getBytes());

                String caloriesAvg = tvAverageCaloriesText.getText().toString() + " "
                        + tvAverageCalories.getText().toString() + " " + tvAverageCaloriesPerDay.getText().toString();
                fos.write(caloriesAvg.getBytes());
                fos.write("\n".getBytes());

                String hydrationAverage = tvAverageHydrationText.getText().toString() + " "
                        + tvAverageHydration.getText().toString() + " " + tvHydrationPercent.getText().toString();

                fos.write(hydrationAverage.getBytes());
                fos.write("\n".getBytes());

                Toast.makeText(getApplicationContext(), getString(R.string.save_file_text) + getFilesDir() + getString(R.string.save_file_path_separator) + FILE_NAME,
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
