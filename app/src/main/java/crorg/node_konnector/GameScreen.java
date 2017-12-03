package crorg.node_konnector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

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

    private Button checkStructure;

    private TextView gameStatus;

    private GameCanvas game;

    private File userProgress;  // local storage of user progress

    private Structure structure;    // the logic holding the answer for a given level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        structure = new Structure(3);
        game = (GameCanvas) findViewById(R.id.gameCanvas);

        //set the shape Recycler View to horizontal
        shapeRecyclerView = (RecyclerView)findViewById(R.id.shapeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        shapeRecyclerView.setLayoutManager(layoutManager);

        bondButton = (ToggleButton) findViewById(R.id.bondButton);
        triangleButton = (ToggleButton) findViewById(R.id.triangleButton);
        circleButton = (ToggleButton) findViewById(R.id.circleButton);

        checkStructure = (Button) findViewById(R.id.checkStructure);

        gameStatus = (TextView) findViewById(R.id.gameStatus);

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

        checkStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList <Node> allFriendKonnections = new ArrayList<Node>();
                if(game.getShapeArrayList().size() > 0) {
                    int number = Structure.countAllNodeRelatives(game.getShapeArrayList().get(0), allFriendKonnections);
                    if(number != game.getShapeArrayList().size()){
                        gameStatus.setText("Not intact!");
                    }else{
                        gameStatus.setText("Intact");
                    }

                }
            }
        });

        // setting up local storage for user
        String filename = "userProgress123";
        userProgress = new File(getFilesDir(), filename);
        structure = new Structure(5);


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

    // use these to save the state of the game
    public void writeToFile(View view) {
        String textToDisplay = "hello, boyo!";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(userProgress.getName(), Context.MODE_PRIVATE);
            outputStream.write(textToDisplay.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readFromFileThing(View view) {
        FileInputStream inputStream;
        String s = "";
        try {
            inputStream = openFileInput(userProgress.getName());
            int nextByte = 0;
            while (nextByte != -1) {
                nextByte = inputStream.read();
                s += (char) nextByte;
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // change button
//        TextView fileStuffs = (TextView) findViewById(R.id.fileStuffs);
//        fileStuffs.setText(s);
    }





}
