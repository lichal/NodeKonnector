package crorg.node_konnector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import crorg.node_konnector.LevelRecyclerView.LevelFragment;
import crorg.node_konnector.dummy.LevelContent;

public class LevelSelectScreen extends AppCompatActivity implements LevelFragment.OnListFragmentInteractionListener, Serializable {
    public static final String LEVEL_MESSAGE = "crorg.nodekonnector.LEVELMESSAGE";

    // a comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onListFragmentInteraction(LevelContent.LevelItem item) {
        Intent intent = new Intent(this, GameScreen.class);
        intent.putExtra(LEVEL_MESSAGE, item.id);
        startActivity(intent);
    }



}
