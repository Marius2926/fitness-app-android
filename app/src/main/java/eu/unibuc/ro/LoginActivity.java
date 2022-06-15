package eu.unibuc.ro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import eu.unibuc.ro.database.models.User;
import eu.unibuc.ro.database.services.UserService;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_KEY = "user_key";
    EditText etEmail;
    EditText etPassword;
    Button btnSignIn;
    TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
    }

    private void initComponents() {
        etEmail = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.login);
        tvRegister = findViewById(R.id.register);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    new UserService.GetUserByEmail(getApplicationContext()) {
                        @Override
                        protected void onPostExecute(User user) {
                            super.onPostExecute(user);
                            if (user == null) {
                                Toast.makeText(getApplicationContext(), R.string.login_inexistent_email, Toast.LENGTH_LONG).show();
                            } else if (!etPassword.getText().toString().trim().equals(user.getPassword())) {
                                Toast.makeText(getApplicationContext(), R.string.login_incorrect_password, Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra(USER_KEY, user);
                                startActivity(intent);
                            }
                        }
                    }.execute(etEmail.getText().toString().trim());
                }
            }
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateFields() {
        if (etEmail.getText() == null || etEmail.getText().toString().trim().isEmpty()
                || !etEmail.getText().toString().contains("@") || !etEmail.getText().toString().contains(".com")) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_email, Toast.LENGTH_LONG).show();
            return false;
        }

        if (etPassword.getText() == null || etPassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.register_invalid_password, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
