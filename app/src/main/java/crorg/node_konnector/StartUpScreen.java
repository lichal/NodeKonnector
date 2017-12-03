package crorg.node_konnector;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class StartUpScreen extends AppCompatActivity {

    /* The TextView for Press to Start */
    private TextView pressStart;
    private File userProgress;  // local storage of user progress for the current Structure object
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


















































    public void saveSerialTest(View view) {
        try {
            Structure test = new Structure(4);
            FileOutputStream fos = new FileOutputStream(userProgress);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(test);
            os.close();
            System.out.println("Succeeded in saving serial!");
        } catch (FileNotFoundException e1) {
            System.out.println("Failed to write - FileNotFound");
        } catch (InvalidClassException e2) {
            System.out.println("Failed to write - InvalidClassException");
         } catch (NotSerializableException e3) {
            System.out.println("Failed to write - NotSerializableException");
        } catch (IOException e4) {
            System.out.println("Failed to write - IOException");
        }
    }


    public void loadSerialTest(View view) {
        System.out.println("Got into method!!!");
        // Now open its contents...
        try {
            FileInputStream fis = new FileInputStream(userProgress);
            try {
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    Structure loaded = (Structure) ois.readObject();
                    //serialLoad.setText("Nodes: " + loaded.getNodes().size());
                    ois.close();
                } catch (IOException i) {
                    System.out.println("IOException i");
                } catch (ClassNotFoundException c) {
                    System.out.println("ClassNotFoundException i");
                } catch (Exception e) {
                    System.out.println("Exception i");
                }
            } catch (IOException e) {
                System.out.println("IOException #2");
            } catch (Exception e) {
                System.out.println("Exception #2");
            }
        } catch (FileNotFoundException e) {
        }


    }
















}
