package eu.unibuc.ro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.unibuc.ro.database.dao.IChallengeDao;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.utils.ChallengeAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

import javax.inject.Inject;

public class ChallengeReportActivity extends AppCompatActivity {

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private static final String FILE_NAME = "challengeReport.txt";
    private ListView lvChallenges;
    private Intent intent;
    @Inject
    public IChallengeDao challengeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerApplicationComponent.builder().roomModule(new RoomModule(this)).build().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_report);
        intent = getIntent();
        initComponents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.clear();
    }

    private List<String> generateFileContent() {
        View view = null;
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < lvChallenges.getCount(); i++) {
            view = lvChallenges.getAdapter().getView(i, view, lvChallenges);

            String firstTextPart = ((TextView) view
                    .findViewById(R.id.challenge_lv_row_tv_congratulations)).getText().toString();
            String challengeType = ((TextView) view
                    .findViewById(R.id.challenge_lv_row_tv_eaten_drunk_or_smoked)).getText().toString();
            String period = ((TextView) view
                    .findViewById(R.id.challenge_tv_row_time_period)).getText().toString();

            String line = firstTextPart + " " + challengeType + " " + period;
            strings.add(line);
        }
        return strings;
    }

    private void saveFile() {
        List<String> fileContent = this.generateFileContent();

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);

            for (int i = 0; i < fileContent.size(); i++) {
                fos.write(fileContent.get(i).getBytes());
                fos.write("\n".getBytes());
            }

            Toast.makeText(getApplicationContext(), getString(R.string.save_file_text)
                            + getFilesDir() + getString(R.string.save_file_path_separator) + FILE_NAME,
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
    }

    private void initComponents() {
        lvChallenges = findViewById(R.id.challenge_report_lv_challenges);
        Button btnDownload = findViewById(R.id.challenge_report_btn_download);
        Button btnShareFile = findViewById(R.id.challenge_report_btn_share);

        User user = (User) intent.getSerializableExtra(CURRENT_USER);

        mDisposable.add(challengeDao.getUserChallenges(user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(challenges -> {
                    if (challenges != null) {
                        ChallengeAdapter challengeAdapter = new ChallengeAdapter(getApplicationContext(), challenges,
                                getLayoutInflater());
                        lvChallenges.setAdapter(challengeAdapter);
                    }
                }));

        btnShareFile.setOnClickListener(v -> {
            this.saveFile();
            File file = getFileStreamPath(FILE_NAME);
            Uri uri = FileProvider.getUriForFile(this, "eu.unibuc.ro.provider", file);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.setType("text/*");

            Intent chooser = Intent.createChooser(sharingIntent, "Share File");
            List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                this.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivity(chooser);
        });

        btnDownload.setOnClickListener(v -> this.saveFile());
    }

}
