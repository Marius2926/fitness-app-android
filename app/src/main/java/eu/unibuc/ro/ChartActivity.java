package eu.unibuc.ro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.unibuc.ro.database.models.HydrationLevel;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.services.HydrationService;
import eu.unibuc.ro.database.utils.ChartDraw;

import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

public class ChartActivity extends AppCompatActivity {

    private Intent intent;
    private Map<String, Float> chartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        intent = getIntent();
        initComponents();
    }

    private void initComponents() {

        User user = (User) intent.getSerializableExtra(CURRENT_USER);
        chartData = new LinkedHashMap<>();
        new HydrationService.GetChartData(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<HydrationLevel> hydrationLevels) {
                super.onPostExecute(hydrationLevels);
                if (hydrationLevels != null && hydrationLevels.size() != 0) {
                    for (HydrationLevel hydrationLevel : hydrationLevels) {
                        chartData.put(hydrationLevel.getRegistrationDay().toString(), hydrationLevel.getLevelValue());
                    }
                    ChartDraw draw = new ChartDraw(getApplicationContext(), chartData);
                    setContentView(draw);
                }
            }
        }.execute(user.getId());

    }
}
