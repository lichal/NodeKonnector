package crorg.node_konnector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class StartUpScreen extends AppCompatActivity {

    /* The TextView for Press to Start */
    private TextView pressStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        // set blink animation for the text view
        pressStart = (TextView)findViewById(R.id.startUpTxt);
        pressStart.setAnimation(manageBlinkEffect());

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
}
