package crorg.node_konnector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
<<<<<<< HEAD
=======
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.Square;
import crorg.node_konnector.Shapes.Triangle;
>>>>>>> bf3554a3c0280afdd50d99c36c98b56500ee11f8
import crorg.node_konnector.dummy.ShapeContent;

public class GameScreen extends AppCompatActivity implements
        ShapeFragment.OnListFragmentInteractionListener {

    /* The shape recycler view holds shapes */
    private RecyclerView shapeRecyclerView;

    private ToggleButton bondButton;

    private Button triangleButton;

    private Button circleButton;

    private Button squareButton;

    private Button hexagonButton;

    private Button checkStructure;

    private TextView gameStatus;

    private GameCanvas game;

    private File userProgress;  // local storage of user progress

    private Structure gameStruct;    // the logic holding the answer for a given level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        // intent gets the level selected
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainScreen.LEVEL_MESSAGE);

        // new game structure
        gameStruct = new Structure(2);

        // display the game info
        gameStruct.displayStringDescriptionForPlayer();

        // associate game canvas
        game = (GameCanvas) findViewById(R.id.gameCanvas);

        //set the shape Recycler View to horizontal
//        shapeRecyclerView = (RecyclerView)findViewById(R.id.shapeRecyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        shapeRecyclerView.setLayoutManager(layoutManager);

        // associate buttons
        bondButton = (ToggleButton) findViewById(R.id.bondButton);
        checkStructure = (Button) findViewById(R.id.checkStructure);
        circleButton = (Button) findViewById(R.id.circleBtn);
        squareButton = (Button) findViewById(R.id.squareBtn);
        triangleButton = (Button) findViewById(R.id.triangleBtn);
        hexagonButton = (Button) findViewById(R.id.hexagonBtn);
        gameStatus = (TextView) findViewById(R.id.gameStatus);

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

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getShapeArrayList().add(new Circle(new OvalShape(), 500, 10));
                game.invalidate();
            }
        });

        squareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getShapeArrayList().add(new Square(new PathShape(drawSquare(), 100, 100), 200, 200));
                game.invalidate();
            }
        });

        triangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getShapeArrayList().add(new Triangle(new PathShape(drawTriangle(), 100, 100), 400, 200));
                game.invalidate();
            }
        });

        hexagonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getShapeArrayList().add(new Hexagon(new PathShape(drawHexagon(), 100, 100), 200, 400));
                game.invalidate();
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
<<<<<<< HEAD
                        //boolean test = structure.matchesStructure(game.getShapeArrayList(), game.getBondArrayList());
                        boolean test = Structure.areStructuresSimilarEnough(game.getShapeArrayList(), game.getBondArrayList(), structure.getNodes(), structure.getBonds());
=======
                        boolean test = gameStruct.matchesStructure(game.getShapeArrayList(), game.getBondArrayList());
>>>>>>> bf3554a3c0280afdd50d99c36c98b56500ee11f8
                        if (test){
                            gameStatus.setText("pass level!");
                        }else if (!test){
                            gameStatus.setText("failed!");
                        }
                    }
                }

            }
        });

        // setting up local storage for user
        String filename = "userProgress123";
        userProgress = new File(getFilesDir(), filename);
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

    private Path drawHexagon() {
        Point midPoint = new Point();
        midPoint.x = 50;
        midPoint.y = 50;

        Point p1 = null, p2 = null, p3 = null, p4 = null, p5 = null, p6 = null;

        p1 = new Point(midPoint.x-50, midPoint.y);
        p2 = new Point(midPoint.x-25, midPoint.y+50);
        p3 = new Point(midPoint.x+25, midPoint.y+50);
        p4 = new Point(midPoint.x+50, midPoint.y);
        p5 = new Point(midPoint.x+25, midPoint.y-50);
        p6 = new Point(midPoint.x-25, midPoint.y-50);

        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.lineTo(p4.x, p4.y);
        path.lineTo(p5.x, p5.y);
        path.lineTo(p6.x, p6.y);
        path.lineTo(p1.x, p1.y);

        return path;
    }

    private Path drawSquare(){
        Point p1 = new Point();
        p1.x = 0;
        p1.y = 0;

        Point p2, p3, p4;

        p2 = new Point(p1.x+100, p1.y);
        p3 = new Point(p1.x + 100, p2.y + 100);
        p4 = new Point(p1.x, p1.y + 100);

        Path rectangle = new Path();
        rectangle.moveTo(p1.x, p1.y);
        rectangle.lineTo(p2.x,p2.y);
        rectangle.lineTo(p3.x, p3.y);
        rectangle.lineTo(p4.x, p4.y);
        rectangle.lineTo(p1.x, p1.y);

        return rectangle;
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
