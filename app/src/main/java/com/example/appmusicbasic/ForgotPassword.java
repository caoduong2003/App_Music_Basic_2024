package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPassword extends AppCompatActivity {

    EditText userForgot, passForgot;
    TextInputLayout userForgotError, passForgotError;
    TextView btnCancelForgot;
    Button btnGetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        addCotroller();

        btnCancelForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassByName();
            }
        });
    }

    private void forgotPassByName() {
        String username = userForgot.getText().toString().trim();
        SQLiteDatabase database = null;
        try {
//            database = SQLiteDatabase.openDatabase(getDatabasePath(), null, SQLiteDatabase.OPEN_READWRITE);
            database = openOrCreateDatabase("db_music", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT password FROM users WHERE username = ?", new String[]{username});
            if (cursor.moveToFirst()) {
                // Người dùng tồn tại, lấy mật khẩu và hiển thị nó trong EditText
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                passForgot.setText(password);
            } else {
                // Người dùng không tồn tại, hiển thị thông báo
                Toast.makeText(this, "Username does not exist", Toast.LENGTH_LONG).show();
            }
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public void addCotroller() {
        userForgot = findViewById(R.id.userForgot);
        passForgot = findViewById(R.id.passForgot);
        userForgotError = findViewById(R.id.userForgotError);
        passForgotError = findViewById(R.id.passForgotError);
        btnCancelForgot = findViewById(R.id.btnCancelForgot);
        btnGetPassword = findViewById(R.id.btnGetPassword);
    }
}