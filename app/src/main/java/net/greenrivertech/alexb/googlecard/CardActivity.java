package net.greenrivertech.alexb.googlecard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.MalformedURLException;
import java.net.URL;

public class CardActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;

    private TextView userName;
    private TextView userEmail;
    private TextView userID;
    private ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //get views
        userName = (TextView) findViewById(R.id.userName);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userID = (TextView) findViewById(R.id.userID);
        userPhoto = (ImageView) findViewById(R.id.userPhoto);


        //get user info from intent and set the views' text to display the info.
        Intent intent = getIntent();

        String userPhotoUriString = intent.getStringExtra(SignInActivity.USER_PHOTO_URL_TAG);

        userName.setText(intent.getStringExtra(SignInActivity.USER_NAME_TAG));
        userEmail.setText(intent.getStringExtra(SignInActivity.USER_EMAIL_TAG));
        userID.setText(intent.getStringExtra(SignInActivity.USER_ID_TAG));

        //if the photo was not null on the previous activity,
        //parse the URI.
        Uri userPhotoUri;

        if (!userPhotoUriString.equals(SignInActivity.NO_PHOTO_FOUND)) {

            userPhotoUri = Uri.parse(userPhotoUriString);
            /*
            Toast.makeText(this, userPhotoUriString, Toast.LENGTH_SHORT).show();

            try {
                URL url = new URL(userPhotoUriString);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                userPhoto.setImageBitmap(bmp);

                //userPhoto.setImage(userPhotoUri);
            }
            catch (Exception ex) {

            }*/

            //userPhoto.setImageURI(userPhotoUri);

        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customize sign-out button.
        Button signOutButton = (Button) findViewById(R.id.sign_out_button);
        if (signOutButton != null) {
            signOutButton.setOnClickListener(this);
        }

    }



    private void signOut() {
        //sign out of Google.
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);

        //go back to the sign in activity
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_out_button) {
            signOut();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
