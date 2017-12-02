package crorg.node_konnector;

import android.content.Intent;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import crorg.node_konnector.GamePanel.GameCanvas;
import crorg.node_konnector.ShapeRecyclerView.ShapeFragment;
import crorg.node_konnector.Shapes.Circle;
import crorg.node_konnector.Shapes.Triangle;
import crorg.node_konnector.dummy.ShapeContent;

public class GameScreen extends AppCompatActivity implements
        ShapeFragment.OnListFragmentInteractionListener {

    /* The shape recycler view holds shapes */
    private RecyclerView shapeRecyclerView;

    private ToggleButton bondButton;

    private ToggleButton triangleButton;

    private ToggleButton circleButton;

    private GameCanvas game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        game = (GameCanvas) findViewById(R.id.gameCanvas);

        //set the shape Recycler View to horizontal
        shapeRecyclerView = (RecyclerView)findViewById(R.id.shapeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        shapeRecyclerView.setLayoutManager(layoutManager);

        bondButton = (ToggleButton) findViewById(R.id.bondButton);
        triangleButton = (ToggleButton) findViewById(R.id.triangleButton);
        circleButton = (ToggleButton) findViewById(R.id.circleButton);

//        bondButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(bondButton.isChecked()){
//                    game.setBondingMode(true);
//                    game.invalidate();
//                }
//                else {
//                    game.setBondingMode(false);
//                    game.invalidate();
//                }
//            }
//        });

        bondButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.setBondingMode(true);
                    game.invalidate();
                }else{
                    game.setBondingMode(false);
                    game.invalidate();
                }
            }
        });

        triangleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.getShapeArrayList().add(new Triangle(new PathShape(drawTriangle(), 100, 100), 200, 200));
                    game.invalidate();
                }
            }
        });

        circleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.getShapeArrayList().add(new Circle(new OvalShape(), 500, 10));
                    game.invalidate();
                }
            }
        });

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainScreen.LEVEL_MESSAGE);
    }

    public Path drawTriangle() {
        Point p1 = new Point();
        p1.x = 50;
        p1.y = 0;

        Point p2 = null, p3 = null;

        p2 = new Point(p1.x - 50, p1.y + 100);
        p3 = new Point(p1.x + 50, p1.y + 100);

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }

    @Override
    public void onListFragmentInteraction(ShapeContent.ShapeItem item) {

    }
}
