package crorg.node_konnector;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import crorg.node_konnector.GamePanel.MyGLSurfaceView;
import crorg.node_konnector.ShapeRecyclerView.ShapeFragment;
import crorg.node_konnector.dummy.ShapeContent;

public class GameScreen extends AppCompatActivity implements
        ShapeFragment.OnListFragmentInteractionListener {

    /* The shape recycler view holds shapes */
    private RecyclerView shapeRecyclerView;

    private MyGLSurfaceView myGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        //set the shape Recycler View to horizontal
        shapeRecyclerView = (RecyclerView)findViewById(R.id.shapeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        shapeRecyclerView.setLayoutManager(layoutManager);


        myGLSurfaceView = (MyGLSurfaceView)this.findViewById(R.id.gamePanel);
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainScreen.LEVEL_MESSAGE);


    }

    @Override
    public void onListFragmentInteraction(ShapeContent.ShapeItem item) {

    }
}
