package eu.unibuc.ro;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import eu.unibuc.ro.fragments.ChallengeFragment;
import eu.unibuc.ro.fragments.DiaryFragment;
import eu.unibuc.ro.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new DiaryFragment());
        initComponents();
    }

    private void initComponents() {

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_diary) {
                selectedFragment = new DiaryFragment();
            } else if (item.getItemId() == R.id.navigation_challenges) {
                selectedFragment = new ChallengeFragment();
            } else {
                selectedFragment = new ProfileFragment();
            }
            loadFragment(selectedFragment);
            return true;
        });
    }

    private void loadFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, frag)
                .commit();
    }
}
