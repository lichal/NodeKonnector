package crorg.node_konnector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;

import crorg.node_konnector.LevelRecyclerView.LevelAdapter;
import crorg.node_konnector.LevelRecyclerView.LevelFragment;
import crorg.node_konnector.dummy.LevelContent;

public class LevelSelectScreen extends AppCompatActivity implements LevelFragment.OnListFragmentInteractionListener, Serializable {
    public static final String LEVEL_MESSAGE = "crorg.nodekonnector.LEVELMESSAGE";

    private int level;
    private RecyclerView levelView;
    private LevelAdapter adapter;
    // a comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        levelView = (RecyclerView) findViewById(R.id.levelList);

        // initialize level with 1
        level = 1;

        // retreive the level information
        Intent intent = getIntent();
        int levelMessage = intent.getIntExtra(StartUpScreen.LEVEL_NOW, 0);
        level = levelMessage;
    }

    @Override
    public void onListFragmentInteraction(LevelContent.LevelItem item) {
        Intent intent = new Intent(this, GameScreen.class);
        intent.putExtra(LEVEL_MESSAGE, item.id);
        startActivity(intent);
    }



}
