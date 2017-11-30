package crorg.node_konnector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import crorg.node_konnector.GamePanel.GameCanvas;
import crorg.node_konnector.ShapeRecyclerView.ShapeFragment;
import crorg.node_konnector.dummy.ShapeContent;

public class GameScreen extends AppCompatActivity implements
        ShapeFragment.OnListFragmentInteractionListener {

    /* The shape recycler view holds shapes */
    private RecyclerView shapeRecyclerView;

//    private GameCanvas game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        //set the shape Recycler View to horizontal
        shapeRecyclerView = (RecyclerView)findViewById(R.id.shapeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        shapeRecyclerView.setLayoutManager(layoutManager);

//        game = (GameCanvas)findViewById(R.id.gameCanvas);
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainScreen.LEVEL_MESSAGE);

    }

    @Override
    public void onListFragmentInteraction(ShapeContent.ShapeItem item) {

    }
}
