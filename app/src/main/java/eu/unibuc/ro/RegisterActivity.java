package eu.unibuc.ro;

import static eu.unibuc.ro.LoginActivity.USER_KEY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.services.UserService;

public class RegisterActivity extends AppCompatActivity {
    EditText etName;
    EditText etAge;
    EditText etHeight;
    EditText etWeight;
    RadioGroup rgGender;
    Spinner spnGoal;
    Spinner spnActivityLevel;
    Button btnSave;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
    }

    private void initComponents() {
        etName = findViewById(R.id.register_et_name);
        etAge = findViewById(R.id.register_et_age);
        etHeight = findViewById(R.id.register_et_height);
        etWeight = findViewById(R.id.register_et_weight);
        rgGender = findViewById(R.id.register_rg_gender);
        spnGoal = findViewById(R.id.register_sp_goal);
        spnActivityLevel = findViewById(R.id.register_sp_activity_level);
        btnSave = findViewById(R.id.register_bt_save);
        etEmail = findViewById(R.id.register_et_email);
        etPassword = findViewById(R.id.register_et_password);
        etConfirmPassword = findViewById(R.id.register_et_confirm_password);


        ArrayAdapter<CharSequence> goalAdapter =
                ArrayAdapter.createFromResource(getApplicationContext(), R.array.register_goals_array,
                        R.layout.support_simple_spinner_dropdown_item);
        spnGoal.setAdapter(goalAdapter);

        ArrayAdapter<CharSequence> activityLevelAdapter =
                ArrayAdapter.createFromResource(getApplicationContext(), R.array.register_activity_level_array,
                        R.layout.support_simple_spinner_dropdown_item);
        spnActivityLevel.setAdapter(activityLevelAdapter);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    new UserService.GetUserByEmail(getApplicationContext()) {
                        @Override
                        protected void onPostExecute(User user) {
                            super.onPostExecute(user);
                            if (user != null) {
                                Toast.makeText(getApplicationContext(), R.string.register_toast_account_exists, Toast.LENGTH_LONG).show();
                            } else {
                                RadioButton selectedGender = findViewById(rgGender.getCheckedRadioButtonId());
                                User usr = new User(etName.getText().toString(), Integer.parseInt(etAge.getText().toString()),
                                        Float.valueOf(etHeight.getText().toString()), Float.valueOf(etWeight.getText().toString()),
                                        selectedGender.getText().toString(),
                                        spnGoal.getSelectedItem().toString(),
                                        spnActivityLevel.getSelectedItem().toString(),
                                        etEmail.getText().toString().trim(),
                                        etPassword.getText().toString().trim());
                                new UserService.InsertUser(getApplicationContext()) {
                                    @Override
                                    protected void onPostExecute(User insertedUser) {
                                        super.onPostExecute(insertedUser);
                                        if (insertedUser != null) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra(USER_KEY, insertedUser);
                                            startActivity(intent);
                                        }
                                    }
                                }.execute(usr);
                            }
                        }
                    }.execute(etEmail.getText().toString().trim());

                }
            }
        });
    }

    private boolean validateFields() {
        if (etName.getText() == null || etName.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_name, Toast.LENGTH_LONG).show();
            return false;
        }

        if (etAge.getText() == null || etAge.getText().toString().trim().isEmpty()
                || Integer.parseInt(etAge.getText().toString()) <= 0) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_age, Toast.LENGTH_LONG).show();
            return false;
        }

        if (etHeight.getText() == null || etHeight.getText().toString().trim().isEmpty()
                || Integer.parseInt(etHeight.getText().toString()) <= 0) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_height, Toast.LENGTH_LONG).show();
            return false;
        }

        if (etWeight.getText() == null || etWeight.getText().toString().trim().isEmpty()
                || Integer.parseInt(etWeight.getText().toString()) <= 0) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_weight, Toast.LENGTH_LONG).show();
            return false;
        }

        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), R.string.register_no_gender, Toast.LENGTH_LONG).show();
            return false;
        }

        if (etEmail.getText() == null || etEmail.getText().toString().trim().isEmpty()
                || !etEmail.getText().toString().contains("@") || !etEmail.getText().toString().contains(".com")) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_email, Toast.LENGTH_LONG).show();
            return false;
        }

        if (etPassword.getText() == null || etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_password, Toast.LENGTH_LONG).show();
            return false;
        } else if (etPassword.getText().toString().length() < 8) {
            Toast.makeText(getApplicationContext(), R.string.register_passwd_8_characters, Toast.LENGTH_LONG).show();
            return false;
        }
        if (etConfirmPassword.getText() == null || etConfirmPassword.getText().toString().trim().isEmpty()
                || !etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_confirm_password, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}



