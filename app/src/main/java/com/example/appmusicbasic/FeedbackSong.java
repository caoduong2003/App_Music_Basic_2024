package com.example.appmusicbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

public class FeedbackSong extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText edt_fullname, edt_email, edt_comment;
    com.google.android.material.button.MaterialButton btn_send;
    RatingBar rating_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_song);
        addController();
        String username = getIntent().getStringExtra("username");
        edt_fullname.setText(username);


        btn_send.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                String fullname = edt_fullname.getText().toString();
                String email = edt_email.getText().toString();
                String comment = edt_comment.getText().toString();
                float rating = rating_bar.getRating();


                // Kiểm tra xem các trường nhập liệu có rỗng không
                if (fullname.isEmpty() || email.isEmpty() || comment.isEmpty()) {
                    // Hiển thị thông báo nếu có trường nào đó rỗng
                    Toast.makeText(FeedbackSong.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(email)) {
                    // Kiểm tra định dạng email
                    Toast.makeText(FeedbackSong.this, "Địa chỉ email không hợp lệ", Toast.LENGTH_LONG).show();
                } else {
                    // Nếu tất cả thông tin hợp lệ, hiển thị thông báo cảm ơn
                    Toast.makeText(FeedbackSong.this, "Cảm ơn bạn đã phản hồi", Toast.LENGTH_LONG).show();

                    finish();

                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void addController() {
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_email = findViewById(R.id.edt_email);
        edt_comment = findViewById(R.id.edt_comment);
        btn_send = findViewById(R.id.btn_send);
        rating_bar = findViewById(R.id.rating_bar);
    }
}