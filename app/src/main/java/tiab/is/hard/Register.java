package tiab.is.hard;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.io.File;
import java.util.Objects;
import tiab.is.hard.BigBoyPageTabs.BigBoyPage;
import tiab.is.hard.SQL.SQL;
import tiab.is.hard.SQL.SQLVariables;

public class Register extends AppCompatActivity {

    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    Button btn_login;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;
    String userer;
    String photo;
    File profilpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_login.setOnClickListener(v -> SignInGoogle());

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }

    public void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else
                        updateUI(null);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException ignored) {}
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            photo = String.valueOf(user.getPhotoUrl());
            //Picasso.with(Register.this).load(photo).into(image);

            userer = Objects.requireNonNull(user.getEmail()).replace(".", "").replace("@", "");
            profilpic = new File(Environment.getExternalStorageDirectory() + File.separator  + "Android/data/tiab.is.hard/files/random/" + userer + ".png");
            if(!profilpic.exists())
                downloadMeSenpai(this, userer + ".png", "random", photo);
            else
                getout();
        }
    }

    public void dontloginClicked(View view) {
        getout();
    }

    public void nevershowagainClicked(View view) {
        BigBoyPage.SELECTED_TABLE = "SETTINGS";
        SQLVariables.mydb = new SQL(this);
        SQLVariables.resulter = SQLVariables.mydb.getAllDate();
        SQLVariables.resulter.moveToFirst();
        SQLVariables.mydb.updateSetting(SQLVariables.resulter.getString(0), "never_show_register_page_again", "yes");
        getout();
    }

    private void getout(){
        progressBar.setVisibility(View.INVISIBLE);
        Intent start = new Intent(Register.this, BigBoyPage.class);
        startActivity(start);
        ActivityCompat.finishAffinity(Register.this);
    }


    public void downloadMeSenpai(final Context context, String DISPLAY, String destinationDirectory, String url) {
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, DISPLAY);
        final long downloadID = downloadmanager.enqueue(request);

        BroadcastReceiver onComplete=new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadID == id) {
                    getout();
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
