package crorg.node_konnector;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.app.TaskStackBuilder;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static android.app.Notification.VISIBILITY_PUBLIC;


public class StartUpScreen extends AppCompatActivity implements Serializable, FacebookFragment.OnFragmentInteractionListener {
    public static final String LEVEL_NOW = "crorg.nodekonnector.LEVELNOW";
    public static final String SCORE_NOW = "crorg.nodekonnector.SCORENOW";

    /* The TextView for Press to Start */
    private TextView pressStart;
    private TextView loadingText;
    private TextView doneText;
    private File userProgress;  // local storage of user progress for the current Structure object
    private File structureAnswer;  // local storage of user progress for the current Structure object

    /** face book stuff */
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    /** Firebase user */
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference myLevel;

    private int currentLevel;
    private int currentScore;

    private boolean isrunning;
    private StartUpCanvas startUpCanvas;

    private File userScore_FILE;
    private File userLevel_FILE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        userScore_FILE = new File(getFilesDir(), "userScore_File");
        userLevel_FILE = new File(getFilesDir(), "userLevel_File");

        currentLevel = 1;
        currentScore = 0;
        isrunning = true;
        mAuth = FirebaseAuth.getInstance();

        startUpCanvas = (StartUpCanvas) findViewById(R.id.startUpCanvas);

        // instantiate firebase database
        database = FirebaseDatabase.getInstance();

        // set blink animation for the text view
        pressStart = (TextView)findViewById(R.id.startUpTxt);
        loadingText = (TextView) findViewById(R.id.loadingText);
        doneText = (TextView)findViewById(R.id.doneText);

        pressStart.setAnimation(manageBlinkEffect());
        loadingText.setAnimation(manageBlinkEffect());

        doneText.setTextSize(15f);
        doneText.setTextSize(Color.BLACK);

        loadingText.setTextSize(15f);
        loadingText.setTextColor(Color.BLACK);
//        loadingText.setVisibility(View.INVISIBLE);

        // facebook login button
        loginButton = (LoginButton) findViewById(R.id.login_button);

        // set the color of press start text
        pressStart.setTextColor(Color.LTGRAY);
        pressStart.setTextSize(15f);

        // setting up local storage for user
        userProgress = new File(getFilesDir(), "userProgress123");
        structureAnswer = new File(getFilesDir(), "structureAnswer123");

        // call back manager for face book
        callbackManager = CallbackManager.Factory.create();

        // ask for the email and public profile permissions
        loginButton.setReadPermissions("email", "public_profile");

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.v("MESSAGE#", "LOGGED IN");
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                        System.out.println("Success cancel");
                        // App code
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });



        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user clicks on facebook logout
                    updateLogout();
                    Log.v("MESSAGE#", "over here");
                }
                if(currentAccessToken != null){
                    updateLogin();
                    Log.v("MESSAGE#", "over here in");
                }
            }
        };

        checkLoginStatus();
    }

    private void checkLoginStatus(){
        if(AccessToken.getCurrentAccessToken() !=null) {
            Log.v("MESSAGE#", "TOKEN");
            updateLogin();
        }else{
            Log.v("MESSAGE#", "NoTOKEN");
            updateLogout();
        }
    }

    private void updateLogout(){
        // case user is not login, we will use the local copy of the score and levels.
        currentUser = null;
        if(levelReadFromFile()) {
            Log.d("MESSAGE###", "LOADED" + currentLevel);
        }else{
            currentLevel = 1;
        }
        if(scoreReadFromFile()){
//            Log.d("MESSAGE###", "score read" + currentScore);
        }else{
            currentScore = 0;
        }
    }

    private void updateLogin(){
        // retreive level information from firebase
        if(isLoggedIn()) {
            // firebase authentication
            currentUser = mAuth.getCurrentUser();
        }
        myLevel = database.getReference("USERS_TABLE");
        myLevel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // checks if the level data exist or not
                if(dataSnapshot.child(currentUser.getUid()).child("Level").exists()){
                    int levelValue = dataSnapshot.child(currentUser.getUid()).child("Level").getValue(Integer.class);
                    currentLevel = levelValue;
                } else{
                    currentLevel = 1;
                    myLevel.child(currentUser.getUid()).child("Level").setValue(1);
                }
                // checks if the score data exist or not
                if(dataSnapshot.child(currentUser.getUid()).child("Score").exists()){
                    int scoreValue = dataSnapshot.child(currentUser.getUid()).child("Score").getValue(Integer.class);
                    currentScore = scoreValue;
                } else{
                    currentScore = 0;
                    myLevel.child(currentUser.getUid()).child("Score").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public boolean levelReadFromFile() {
        FileInputStream inputStream;
        // get user level
        try {
            inputStream = openFileInput(userLevel_FILE.getName());
            currentLevel = inputStream.read();
            inputStream.close();
            return true;
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        } catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean scoreReadFromFile() {
        FileInputStream inputStream;
        // get user level
        try {
            inputStream = openFileInput(userScore_FILE.getName());
            currentScore = inputStream.read();
            inputStream.close();
            return true;
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        } catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        if(isLoggedIn()){
            currentUser = mAuth.getCurrentUser();
            updateLogin();
        }else {
            FirebaseAuth.getInstance().signOut();
            updateLogout();
            Log.d("MESSAGE", "HERE IN SIGNOUT START");
            currentUser = null;
        }
    }

    public void onResume(){
        super.onResume();
        if(isLoggedIn()){
            currentUser = mAuth.getCurrentUser();
            updateLogin();
        }else {
            FirebaseAuth.getInstance().signOut();
            updateLogout();
            Log.d("MESSAGE", "HERE IN SIGNOUT START");
            currentUser = null;
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("MESSAGE#", "handleFacebookAccessToken:" + token);

        loadingText.setTextColor(Color.LTGRAY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        ///right here make some fading out
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadingText.setTextColor(Color.BLACK);
                    doneText.setTextColor(Color.LTGRAY);
                    // Sign in success, update UI with the signed-in user's information
                    ////remove the fading out
                    Log.v("MESSAGE#", "signInWithCredential:success");
                    currentUser = mAuth.getCurrentUser();
                    updateLogin();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Animation fade;
                    fade = new AlphaAnimation(1, 0);
                    fade.setDuration(750);
                    fade.setFillAfter(true);
                    doneText.setAnimation(fade);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*******************************************************************
     * This method performs a blinking animation for press to start text
     *
     * @return blinkAnimation, the blink animation effect
     ******************************************************************/
    private Animation manageBlinkEffect(){
        Animation blinkAnimation;
        // Change alpha from fully visible to mostly invisible
        blinkAnimation= new AlphaAnimation(1, 0.2f);
        // blink duration
        blinkAnimation.setDuration(750);
        // Repeat animation infinitely
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        return blinkAnimation;
    }

    /*******************************************************************
     * The touch event method, determine what to do on touch event
     *
     * @param event the user touch event
     * @return true for touch event occur
     ******************************************************************/
    // this method needs to consult the highest level achieved by the player in order to generate levels

    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            // Case touch down
            case MotionEvent.ACTION_DOWN:
                checkLoginStatus();
                Log.d("MESSAGE#", "level" + currentLevel);
                Log.d("MESSAGE#", "score" + currentScore);
                // Setting a bew intent for screen transition
                Intent intent = new Intent(this, LevelSelectScreen.class);
                intent.putExtra(LEVEL_NOW, currentLevel);
                intent.putExtra(SCORE_NOW, currentScore);
                Log.d("MESSAGE#", "why are you" + currentScore);
                // Transition to level screen
                startActivity(intent);
        }
        return true;
    }

    public void notifyUserOfSurpassingFriendScore(View view) {
        // The id of the channel.
        String CHANNEL_ID = "nodeKonnector_channel_0156";
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
        mBuilder.setContentTitle("NEW SCORE!");
        //mBuilder.setContentText("A friend of yours has just BEATEN your high score!");
        //mBuilder.setContentTitle("HIGH SCORE BEATEN");
        mBuilder.setContentText("You just beat your old score!");
//        mBuilder.setChannel(CHANNEL_ID);
        mBuilder.setTicker("NODES!");   // what does this do?
        mBuilder.setVibrate(new long[]{0, 100, 100, 100, 100, 100, 100, 1500, 1000, 100, 100, 100, 100, 100, 100, 1500});
        mBuilder.setColor(0xff00ffff);  // color for app name title in notifications drawer
        mBuilder.setLights(0xffff00ff, 1000, 500);  // sets flashing lights pattern when phone is locked
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(2);
        mBuilder.setVisibility(VISIBILITY_PUBLIC);
        //mBuilder.setOngoing(true);  // this makes it impossible to swipe away

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, StartUpScreen.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(StartUpScreen.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int mNotificationId = 456;
        mNotificationManager.notify(mNotificationId, mBuilder.build());
    }

    // this needs to save the user's current progress as well as the current
    // structure answer
    // call this method whenever you want to save a file to drive...
    public void saveSerialTest(View view) {
        try {
            Structure answer = new Structure(4);
            FileOutputStream fos = new FileOutputStream(userProgress);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(answer);
            os.close();
            System.out.println("Succeeded in saving serial!");
        } catch (FileNotFoundException e1) {
            System.out.println("Failed to write - FileNotFound");
            System.out.println("Message: " + e1.getMessage());
        } catch (InvalidClassException e2) {
            System.out.println("Failed to write - InvalidClassException");
            System.out.println("Message: " + e2.getMessage());
         } catch (NotSerializableException e3) {
            System.out.println("Failed to write - NotSerializableException");
            System.out.println("Message: " + e3.getMessage());
        } catch (IOException e4) {
            System.out.println("Failed to write - IOException");
            System.out.println("Message: " + e4.getMessage());
        }
    }


    // call this method to retrieve stuff
    public void loadSerialTest(View view) {
        System.out.println("Got into method!!!");
        // Now open its contents...
        try {
            FileInputStream fis = new FileInputStream(userProgress);
            try {
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    Structure loaded = (Structure) ois.readObject();
//                    serialLoad.setText("Nodes: " + loaded.getNodes().size());
                    ois.close();
                } catch (ClassNotFoundException i) {
                    System.out.println("Read object fail: ClassNotFoundException");
                    System.out.println("Culprit: " + i.getMessage());
                } catch (InvalidClassException i2) {
                    System.out.println("Read object fail: InvalidClassException");
                    System.out.println("Culprit: " + i2.getMessage());
                } catch (StreamCorruptedException i3) {
                    System.out.println("Read object fail: StreamCorruptedException");
                    System.out.println("Culprit: " + i3.getMessage());
                } catch (OptionalDataException i4) {
                    System.out.println("Read object fail: OptionalDataException");
                    System.out.println("Culprit: " + i4.getMessage());
                } catch (IOException i5) {
                    System.out.println("Read object fail: IOException");
                    System.out.println("Culprit: " + i5.getMessage());
                }
            } catch (IOException e) {
                System.out.println("ObjectOutputStream open fail: IOException");
                System.out.println("Culprit: " + e.getMessage());
            } catch (Exception e2) {
                System.out.println("ObjectOutputStream open fail: Exception");
                System.out.println("Culprit: " + e2.getMessage());
            }
        } catch (FileNotFoundException eZ) {
            System.out.println("FileInputStream fail: FileNotFoundException");
            System.out.println("Culprit: " + eZ.getMessage());
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
