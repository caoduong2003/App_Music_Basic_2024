package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {
    Button btnToLogin;
    TextView btnCancel;
    EditText userRegister, passRegister, rePassRegister;
    TextInputLayout userRegisterError, passRegisterError, rePassRegisterError;
    SQLiteAdapter sqLiteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_register);
        addController();
        sqLiteAdapter = new SQLiteAdapter(this);
        sqLiteAdapter.open();




        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRegister();

            }
        });



    }

    public void checkRegister() {
        String username = userRegister.getText().toString().trim();
        String password = passRegister.getText().toString().trim();
        String rePassword = rePassRegister.getText().toString().trim();
        boolean check = true;
        if (username.isEmpty()) {
            userRegisterError.setError("Username is required");
            check = false;
        } else {
            userRegisterError.setError(null);
        }
        if (password.isEmpty()) {
            passRegisterError.setError("Password is required");
            check = false;
        } else {
            passRegisterError.setError(null);
        }
        if (rePassword.isEmpty()) {
            rePassRegisterError.setError("Re-Password is required");
            check = false;
        } else {
            rePassRegisterError.setError(null);
        }
        if (!rePassword.equals(password)) {
            rePassRegisterError.setError("Re-Password is not match");
            check = false;
        } else {
            rePassRegisterError.setError(null);
        }
        if (check) {
            sqLiteAdapter.insertUser(username, password, "user");
            Toast toast = Toast.makeText(Register.this, "Register success", Toast.LENGTH_LONG);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(Register.this, R.anim.slide_out_left, R.anim.slide_in_right);
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent, options.toBundle());
        }
    }

    private void addController() {
        btnToLogin = (Button) findViewById(R.id.btnToLogin);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        userRegister = (EditText) findViewById(R.id.userRegister);
        passRegister = (EditText) findViewById(R.id.passRegister);
        rePassRegister = (EditText) findViewById(R.id.rePassRegister);
        userRegisterError = (TextInputLayout) findViewById(R.id.userRegisterError);
        passRegisterError = (TextInputLayout) findViewById(R.id.passRegisterError);
        rePassRegisterError = (TextInputLayout) findViewById(R.id.rePassRegisterError);


    }
}