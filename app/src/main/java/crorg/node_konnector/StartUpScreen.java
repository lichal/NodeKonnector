package crorg.node_konnector;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import java.util.logging.Level;

import android.app.TaskStackBuilder;


public class StartUpScreen extends AppCompatActivity implements Serializable {

    /* The TextView for Press to Start */
    private TextView pressStart;
    private File userProgress;  // local storage of user progress for the current Structure object
    private File structureAnswer;  // local storage of user progress for the current Structure object
    private Button serialLoad;
    private Button serialSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        // set blink animation for the text view
        pressStart = (TextView)findViewById(R.id.startUpTxt);
        pressStart.setAnimation(manageBlinkEffect());
        serialLoad = (Button)findViewById(R.id.serialLoad);
        serialSave = (Button)findViewById(R.id.serialSave);

        // setting up local storage for user
        userProgress = new File(getFilesDir(), "userProgress123");

        structureAnswer = new File(getFilesDir(), "structureAnswer123");





        // this needs to consult the saved file for highest level achieved by the player before it


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
//        blinkAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
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
                // Setting a bew intent for screen transition
                Intent intent = new Intent(this, LevelSelectScreen.class);
                // Transition to level screen
                startActivity(intent);
                // End start up activity
//                finish();
        }
        return true;
    }
































    public void notifyTest(View view) {
        // The id of the channel.
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this);

                        mBuilder.setSmallIcon(R.drawable.ic_stat_name);
                        mBuilder.setContentTitle("My notification");
                        mBuilder.setContentText("Hello World!");
                    mBuilder.setChannel(CHANNEL_ID);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, GameScreen.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(LevelSelectScreen.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// mNotificationId is a unique integer your app uses to identify the
// notification. For example, to cancel the notification, you can pass its ID
// number to NotificationManager.cancel().
        int mNotificationId = 456;
        mNotificationManager.notify(mNotificationId, mBuilder.build());




    }



    public void testAngles() {
        
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
                    serialLoad.setText("Nodes: " + loaded.getNodes().size());
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
















}
