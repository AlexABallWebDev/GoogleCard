package net.greenrivertech.alexb.googlecard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CardActivity extends AppCompatActivity {

    private TextView userName;
    private TextView userEmail;
    private TextView userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userID = (TextView) findViewById(R.id.userID);


        Intent intent = getIntent();

        userName.setText(intent.getStringExtra(SignInActivity.USER_NAME_TAG));
        userEmail.setText(intent.getStringExtra(SignInActivity.USER_EMAIL_TAG));
        userID.setText(intent.getStringExtra(SignInActivity.USER_ID_TAG));

    }
}
