package crorg.node_konnector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.Serializable;

import crorg.node_konnector.LevelRecyclerView.LevelAdapter;
import crorg.node_konnector.LevelRecyclerView.LevelFragment;
import crorg.node_konnector.dummy.LevelContent;

public class LevelSelectScreen extends AppCompatActivity implements LevelFragment.OnListFragmentInteractionListener, Serializable {
    public static final String LEVEL_MESSAGE = "crorg.nodekonnector.LEVELMESSAGE";
    public static final String HIGHEST_SCORE = "crorg.nodekonnector.HIGHESTSCORE";
    public static final String HIGHEST_LEVEL = "crorg.nodekonnector.HIGHESTLEVEL";

    private int highestLevel;
    private int highestScore;
    // a comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LevelContent.ITEMS.clear();

        highestLevel = 1;
        highestScore = 0;
        // retreive the level information
        Intent intent = getIntent();
        highestLevel = intent.getIntExtra(StartUpScreen.LEVEL_NOW, highestLevel);
        highestScore = intent.getIntExtra(StartUpScreen.SCORE_NOW, highestScore);

        LevelContent.createList(highestLevel);
    }

    @Override
    public void onListFragmentInteraction(LevelContent.LevelItem item) {
        Intent intent = new Intent(this, GameScreen.class);

        intent.putExtra(HIGHEST_SCORE, highestScore);

        intent.putExtra(HIGHEST_LEVEL, highestLevel);

        int levelSelect = Integer.parseInt(item.id);
        intent.putExtra(LEVEL_MESSAGE, levelSelect);
        //Log.v("MESSAGE#45689", "BEFORE sending intent...ItemID: " + item.id);
        startActivity(intent);
        //Log.v("MESSAGE#45689", "AFER sending intent...");
    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.d("MESSAGE#", "PAUSE");
        finish();
    }

}
