package net.greenrivertech.alexb.googlecard;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    //tags for accessing the account information
    public final static String USER_NAME_TAG = "userName";
    public final static String USER_EMAIL_TAG = "userEmail";
    public final static String USER_ID_TAG = "userID";
    public final static String USER_PHOTO_URL_TAG = "userPhotoURL";

    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mStatusTextView = (TextView) findViewById(R.id.status);

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

        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        if (signInButton != null) {
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            signInButton.setScopes(gso.getScopeArray());
            signInButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

            //Start the next activity with the given account info
            startCardActivity(acct);

        } else {
            Toast.makeText(this, "Something went wrong handling signIn result", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCardActivity(GoogleSignInAccount acct) {
        //get information out of the account object
        String userName = acct.getDisplayName();
        String userEmail = acct.getEmail();
        String userID = acct.getId();
        //String userPhotoURL = acct.getPhotoUrl().toString();

        //attach the information to the intent
        Intent cardIntent = new Intent(this, CardActivity.class);

        cardIntent.putExtra(USER_NAME_TAG, userName);
        cardIntent.putExtra(USER_EMAIL_TAG, userEmail);
        cardIntent.putExtra(USER_ID_TAG, userID);
        //cardIntent.putExtra(USER_PHOTO_URL_TAG, userPhotoURL);

        startActivity(cardIntent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
    }
}
