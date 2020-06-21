package com.fit5046.m3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fit5046.m3.lib.Utils;

public class LoginActivity extends AppCompatActivity {

    private MainViewModel model;
    private boolean showPw = false;
    private EditText et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_pw = findViewById(R.id.et_password);
        model = new ViewModelProvider(this).get(MainViewModel.class);
        model.getUserId().observe(this, this::getHomePage);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this::clickLogin);

        Button btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this::clickSignup);

        Button btn_show_pw = findViewById(R.id.btn_show_pw);
        btn_show_pw.setOnClickListener(this::showPassword);
    }

    // Used when user clicks login button. credential info will be sent by viewmodel.
    private void clickLogin(View view) {
        EditText et_email = findViewById(R.id.et_email);
        String email = et_email.getText().toString();
        EditText et_password = findViewById(R.id.et_password);
        String password = et_password.getText().toString();

        if (email.length() < 5 || !Utils.checkEmailFormat(email)) {
            Toast.makeText(this, "Email not valid.", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() == 0) {
            Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_LONG).show();
            return;
        }

        model.login(email, password);
    }

    // Used when user clicks submit button. Switch to sign up page.
    private void clickSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    // when a valid user id is retrieved, switch to home page for the user.
    private void getHomePage(Integer id) {
        // id > 0: valid; id == 0: nothing happened; id < 0: login error.
        if (id < 0) {
            Toast.makeText(this, "Login failed.", Toast.LENGTH_LONG).show();
        } else if (id > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("uid", id);
            startActivity(intent);
        }
    }

    // This method is used to toggle the password visibility.
    // It has some problem. It can only toggle the password visibility once.
    private void showPassword(View view) {
        showPw = !showPw;
        if (showPw) {
            et_pw.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
        } else {
            et_pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

}
