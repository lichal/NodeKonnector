package crorg.node_konnector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.Serializable;

import crorg.node_konnector.LevelRecyclerView.LevelAdapter;
import crorg.node_konnector.LevelRecyclerView.LevelFragment;
import crorg.node_konnector.contents.LevelContent;

public class LevelSelectScreen extends AppCompatActivity implements LevelFragment.OnListFragmentInteractionListener, Serializable {
    public static final String LEVEL_MESSAGE = "crorg.nodekonnector.LEVELMESSAGE";
    public static final String HIGHEST_SCORE = "crorg.nodekonnector.HIGHESTSCORE";
    public static final String HIGHEST_LEVEL = "crorg.nodekonnector.HIGHESTLEVEL";

    public static final int RANKING_TRANS = 10023;

    private int highestLevel;
    private int highestScore;

    private ImageView lock;
    // a comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LevelContent.ITEMS.clear();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lock = (ImageView) findViewById(R.id.lock);

        highestLevel = 1;
        highestScore = 0;
        // retreive the level information
        Intent intent = getIntent();
        highestLevel = intent.getIntExtra(StartUpScreen.LEVEL_NOW, highestLevel);
        highestScore = intent.getIntExtra(StartUpScreen.SCORE_NOW, highestScore);

        LevelContent.createList(highestLevel+4);

        LevelAdapter.setLevel(highestLevel);
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
        finish();
    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.d("MESSAGE#", "PAUSE");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.rankingMenu) {
//            Intent intent = new Intent(LevelSelectScreen.this,
//                    RankingActivity.class);

//            startActivityForResult(intent, RANKING_TRANS );
//            return true;
        }
        return false;
    }

}
