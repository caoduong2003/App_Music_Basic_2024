package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Login extends AppCompatActivity {

    public String DATABASE_NAME = "db_music";
    public String DB_SUFFIX_PATH = "/databases/";
    SQLiteAdapter sqLiteAdapter;

    Button btnLogin;
    TextView btnRegister, btnForgotPass;
    EditText userLogin, passLogin;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout userLoginError, passLoginError;
    CheckBox checkBoxRemember;

    SharedPreferences sharedPreferences; // luu trang thai dang nhap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        boolean isLoggedIn  = sharedPreferences.getBoolean("Login", false);
        if (isLoggedIn ) {
            Intent intent = new Intent(Login.this, List_Music.class);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.form_login);
        proccessCopy();
        addController();
//        userLogin.setText(sharedPreferences.getString("username", ""));
//        passLogin.setText(sharedPreferences.getString("password", ""));
//        checkBoxRemember.setChecked(sharedPreferences.getBoolean("checked", false));


//        sqLiteAdapter = new SQLiteAdapter(this);
//        sqLiteAdapter.open();
//        Cursor cursor = sqLiteAdapter.getAllUsers();
//
//        StringBuffer buffer = new StringBuffer();
//
//        while (cursor.moveToNext()) {
//            int id = Integer.parseInt(cursor.getString(0));
//            String name = cursor.getString(1);
//            String pass = cursor.getString(2);
//            String role = cursor.getString(3);
//            User user = new User(id, name, pass, role);
//            buffer.append("\n" + user.toString());
//        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Login.this, R.anim.slide_out_left, R.anim.slide_in_right);
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent, options.toBundle());


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetValidation();
                CheckLogin();
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);


            }
        });

    }

    private void checkRemember() {
        if (checkBoxRemember.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", userLogin.getText().toString().trim());
            editor.putString("password", passLogin.getText().toString().trim());
            editor.putBoolean("Login", true);
            editor.apply();
        }
    }

    public void CheckLogin() {
        String username = userLogin.getText().toString().trim();
        String password = passLogin.getText().toString().trim();
        SQLiteDatabase database = null;
        try {
            database = SQLiteDatabase.openDatabase(getDatabasePath(), null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});



            while (cursor.moveToNext()) {
                if (username.equals(cursor.getString(1)) && password.equals(cursor.getString(2))) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, List_Music.class);
                    startActivity(intent);
                    checkRemember();
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
                }


            }
        } finally {
            if (database != null) {
                database.close();
            }

        }


    }


    public String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_SUFFIX_PATH + DATABASE_NAME;
    }

    private void proccessCopy() {
        try {
            File file = getDatabasePath(DATABASE_NAME);
            if (!file.exists()) {
                copyDatabaseFromAssets();
            }
//            Toast.makeText(this, "Copy Database Successful", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyDatabaseFromAssets() {
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File file = new File(getApplicationInfo().dataDir + DB_SUFFIX_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    public void addController() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        userLogin = (EditText) findViewById(R.id.userLogin);
        passLogin = (EditText) findViewById(R.id.passLogin);
        userLoginError = (TextInputLayout) findViewById(R.id.userLoginError);
        passLoginError = (TextInputLayout) findViewById(R.id.passLoginError);
        checkBoxRemember = (CheckBox) findViewById(R.id.checkBoxRemember);
        btnForgotPass = (TextView) findViewById(R.id.btnForgotPass);
    }

    public void SetValidation() {
        if (userLogin.getText().toString().isEmpty()) {
            userLoginError.setError(getResources().getString(R.string.StrLoginError));
            isEmailValid = false;
        } else {
            isEmailValid = true;
            userLoginError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (passLogin.getText().toString().isEmpty()) {
            passLoginError.setError(getResources().getString(R.string.StrPassError));
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
            passLoginError.setErrorEnabled(false);
        }

//        if (isEmailValid && isPasswordValid) {
//            Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
//        }

    }
}