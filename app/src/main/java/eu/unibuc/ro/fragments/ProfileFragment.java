package eu.unibuc.ro.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import eu.unibuc.ro.AchievementsReportActivity;
import eu.unibuc.ro.ChallengeReportActivity;
import eu.unibuc.ro.LoginActivity;
import eu.unibuc.ro.R;
import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.services.UserService;

import static eu.unibuc.ro.LoginActivity.USER_KEY;
import static eu.unibuc.ro.fragments.DiaryFragment.CURRENT_USER;

public class ProfileFragment extends Fragment {

    public static final String DECIMAL_FORMAT = "##.##";
    private TextView tvImc;
    private TextView tvCaloriesNumber;
    private TextView tvName;
    private TextView tvHeight;
    private TextView tvWeight;
    private TextView tvEmail;
    private Spinner spnActivityLevel;
    private Intent intent;
    private User user;
    private AlertDialog alertDialog;
    private EditText etName;
    private EditText etHeight;
    private EditText etWeight;
    private EditText etEmail;
    private Spinner spnEditActivityLevel;
    private AlertDialog alertDialogDeleteAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        intent = requireActivity().getIntent();
        initComponents(view);

        return view;
    }

    private void initComponents(View view) {

        tvName = view.findViewById(R.id.profile_fragment_tv_name_value);
        tvEmail = view.findViewById(R.id.profile_fragment_tv_email_value);
        tvHeight = view.findViewById(R.id.profile_fragment_tv_height_value);
        tvWeight = view.findViewById(R.id.profile_fragment_tv_weight_value);
        spnActivityLevel = view.findViewById(R.id.profile_fragment_spn_activity_level);
        tvImc = view.findViewById(R.id.profile_fragment_tv_imc_value);
        tvCaloriesNumber = view.findViewById(R.id.profile_fragment_tv_calories_nr_value);
        ImageButton btnEdit = view.findViewById(R.id.profile_fragment_btn_edit);
        Button btnDeleteAccount = view.findViewById(R.id.profile_fragment_btn_delete_account);
        Button btnAchievementsReport = view.findViewById(R.id.profile_fragment_btn_progress_report);
        Button btnChallengeReport = view.findViewById(R.id.profile_fragment_btn_challenges_report);

        final ArrayAdapter<CharSequence> activityLevelAdapter = ArrayAdapter.createFromResource
                (getContext(), R.array.register_activity_level_array,
                        R.layout.support_simple_spinner_dropdown_item);
        spnActivityLevel.setAdapter(activityLevelAdapter);


        user = (User) intent.getSerializableExtra(USER_KEY);
        setUserInfo(user, activityLevelAdapter);


        alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.profile_fragment_ad_title_edit_personal_info));

        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        View editView = inflater.inflate(R.layout.edit_personal_info_view, null);
        alertDialog.setView(editView);

        etName = editView.findViewById(R.id.edit_personal_info_et_name);
        etEmail = editView.findViewById(R.id.edit_personal_info_et_email);
        etHeight = editView.findViewById(R.id.edit_personal_info_et_height);
        etWeight = editView.findViewById(R.id.edit_personal_info_et_weight);
        spnEditActivityLevel = editView.findViewById(R.id.edit_personal_info_spn_activity_level);
        spnEditActivityLevel.setAdapter(activityLevelAdapter);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.profile_fragment_ad_btn_save_changes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.setName(etName.getText().toString().trim());
                user.setEmail(etEmail.getText().toString().trim());
                user.setHeight(Float.parseFloat(etHeight.getText().toString().trim()));
                user.setWeight(Float.parseFloat(etWeight.getText().toString().trim()));
                user.setActivityLevel(spnEditActivityLevel.getSelectedItem().toString());

                new UserService.UpdateUser(getContext()) {
                    @Override
                    protected void onPostExecute(Integer rowsNr) {
                        super.onPostExecute(rowsNr);
                        if (rowsNr == 1) {
                            tvName.setText(etName.getText());
                            tvEmail.setText(etEmail.getText());
                            tvHeight.setText(etHeight.getText());
                            tvWeight.setText(etWeight.getText());
                            spnActivityLevel.setSelection(spnEditActivityLevel.getSelectedItemPosition());
                            Toast.makeText(getContext(), R.string.profile_fragment_update_success, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), R.string.profile_fragment_update_fail, Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute(user);
            }
        });

        btnEdit.setOnClickListener(v -> {
            etName.setText(tvName.getText());
            etEmail.setText(tvEmail.getText());
            etHeight.setText(tvHeight.getText());
            etWeight.setText(tvWeight.getText());
            spnEditActivityLevel.setSelection(spnActivityLevel.getSelectedItemPosition());
            alertDialog.show();
        });

        alertDialogDeleteAccount = new AlertDialog.Builder(getContext()).create();
        alertDialogDeleteAccount.setMessage(getString(R.string.profile_fragment_ad_tv_delete_message));
        alertDialogDeleteAccount.setTitle(getString(R.string.profile_fragment_ad_title_confirm_delete_account));

        alertDialogDeleteAccount.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.profile__fragment_ad_btn_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new UserService.DeleteUser(getContext()) {
                            @Override
                            protected void onPostExecute(Integer integer) {
                                super.onPostExecute(integer);
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }.execute(user);
                    }
                });

        alertDialogDeleteAccount.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.profile_fragment_ad_btn_no),
                (dialog, which) -> alertDialogDeleteAccount.cancel());

        btnDeleteAccount.setOnClickListener(v -> alertDialogDeleteAccount.show());

        btnAchievementsReport.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AchievementsReportActivity.class);
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });

        btnChallengeReport.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ChallengeReportActivity.class);
            intent.putExtra(CURRENT_USER, user);
            startActivity(intent);
        });
    }

    private void setUserInfo(User user, ArrayAdapter<CharSequence> activityLevelAdapter) {
        tvEmail.setText(user.getEmail());
        tvName.setText(user.getName());
        tvWeight.setText(String.valueOf(user.getWeight()));
        tvHeight.setText(String.valueOf(user.getHeight()));
        spnActivityLevel.setSelection(activityLevelAdapter.getPosition(user.getActivityLevel()));
        spnActivityLevel.setEnabled(false);
        tvImc.setText(new DecimalFormat(DECIMAL_FORMAT).format(user.getImc()));
        tvCaloriesNumber.setText(String.valueOf(Math.round(user.getCaloriesNumber())));
    }
}
