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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import crorg.node_konnector.GamePanel.GameCanvas;
import crorg.node_konnector.ShapeRecyclerView.ShapeFragment;
import crorg.node_konnector.Shapes.Circle;

import crorg.node_konnector.Shapes.DrawPath;
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.Square;
import crorg.node_konnector.Shapes.Triangle;

import crorg.node_konnector.dummy.ShapeContent;

public class GameScreen extends AppCompatActivity implements
        ShapeFragment.OnListFragmentInteractionListener, Serializable {

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

    private DrawPath drawShape;

    DatabaseReference scoreData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        // intent gets the level selected
        Intent intent = getIntent();
        String message = intent.getStringExtra(LevelSelectScreen.LEVEL_MESSAGE);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("currentLevel");
//        myRef.setValue(message);


        // new game structure
        gameStruct = new Structure(2);

        drawShape = new DrawPath();

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
                game.getShapeArrayList().add(new Square(new PathShape(drawShape.drawSquare(), 100, 100), 200, 200));
                game.invalidate();
            }
        });

        triangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getShapeArrayList().add(new Triangle(new PathShape(drawShape.drawTriangle(), 100, 100), 400, 200));
                game.invalidate();
            }
        });

        hexagonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getShapeArrayList().add(new Hexagon(new PathShape(drawShape.drawHexagon(), 100, 100), 200, 400));
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
                        //boolean test = structure.matchesStructure(game.getShapeArrayList(), game.getBondArrayList());
                        boolean test = Structure.areStructuresSimilarEnough(game.getShapeArrayList(), game.getBondArrayList(), gameStruct.getNodes(), gameStruct.getBonds());
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

    public void testStructureList() {
        // when a fragment is touched, send that level integer to the next screen
        // the next screen will generate a structure
        // don't know where this goes, but...

        // pass this value from some screen;
        int levelNumber = 3;
        ArrayList<Structure> previousStructures = null;    // steal this from local storage/mobile
        boolean keepGoing = true;
        mainLoop:
        while (keepGoing) {
            Structure candidate = new Structure(levelNumber);
            if (previousStructures.size() > 0) {
                for (Structure previous : previousStructures) {
                    boolean matchFound = Structure.areStructuresSimilarEnough(candidate.getNodes(), candidate.getBonds(), previous.getNodes(), previous.getBonds());
                    if (matchFound) {
                        continue mainLoop;
                    }
                }
            }
            break;
        }
    }


    public ArrayList<Structure> getStorageArrayList() {




        return null;
    }


}
