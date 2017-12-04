package crorg.node_konnector;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import crorg.node_konnector.GamePanel.GameCanvas;
import crorg.node_konnector.Shapes.Circle;

import crorg.node_konnector.Shapes.DrawPath;
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.Square;
import crorg.node_konnector.Shapes.Triangle;

public class GameScreen extends AppCompatActivity implements Serializable {

    private ToggleButton singleButton;

    private ToggleButton doubleButton;

    private ToggleButton tripleButton;

    private Button triangleButton;

    private Button circleButton;

    private Button squareButton;

    private Button hexagonButton;

    private Button checkStructure;

    private TextView numShapes;
    private TextView numBonds;

    private TextView gameStat;

    private GameCanvas game;

    private File userProgress;  // local storage of user progress

    private Structure gameStruct;    // the logic holding the answer for a given level

    private DrawPath drawShape;

    DatabaseReference scoreData;

    private int dragType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        // intent gets the level selected
        Intent intent = getIntent();
        String message = intent.getStringExtra(LevelSelectScreen.LEVEL_MESSAGE);

        dragType = 0;
        int m = Integer.parseInt(message) + 1;

        gameStat = (TextView) findViewById(R.id.gameStat);
        gameStat.setText("Node " + (m));
        gameStat.setTextColor(Color.WHITE);
        gameStat.setTextSize(20f);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("currentLevel");
//        myRef.setValue(message);

        // new game structure
        gameStruct = new Structure(Integer.parseInt(message)+1);

        // draw shape holds different shape to be show on canvas
        drawShape = new DrawPath();

        // display the game info
        gameStruct.displayStringDescriptionForPlayer();

        // associate game canvas
        game = (GameCanvas) findViewById(R.id.gameCanvas);

        numBonds = (TextView) findViewById(R.id.numBonds);
        numBonds.setText(gameStruct.printNumBonds());
        numBonds.setTextColor(Color.WHITE);
        numBonds.setTextSize(20f);

        numShapes = (TextView) findViewById(R.id.numShapes);
        numShapes.setText(gameStruct.printNumShapes());
        numShapes.setTextColor(Color.WHITE);
        numShapes.setTextSize(20f);

        //set the shape Recycler View to horizontal
//        shapeRecyclerView = (RecyclerView)findViewById(R.id.shapeRecyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        shapeRecyclerView.setLayoutManager(layoutManager);

        // associate buttons
        singleButton = (ToggleButton) findViewById(R.id.bondButton);
        doubleButton = (ToggleButton) findViewById(R.id.doubleButton) ;
        tripleButton = (ToggleButton) findViewById(R.id.tripleButton);
        checkStructure = (Button) findViewById(R.id.checkStructure);
        circleButton = (Button) findViewById(R.id.circleBtn);
        squareButton = (Button) findViewById(R.id.squareBtn);
        triangleButton = (Button) findViewById(R.id.triangleBtn);
        hexagonButton = (Button) findViewById(R.id.hexagonBtn);

        singleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.setBondingMode(true, 1);
                    game.invalidate();
                }else{
                    game.setBondingMode(false, 0);
                    game.invalidate();
                }
            }
        });

        doubleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.setBondingMode(true, 2);
                    game.invalidate();
                }else{
                    game.setBondingMode(false, 0);
                    game.invalidate();
                }
            }
        });

        tripleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    game.setBondingMode(true, 3);
                    game.invalidate();
                }else{
                    game.setBondingMode(false, 0);
                    game.invalidate();
                }
            }
        });


        circleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 1;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(circleButton);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });
        circleButton.setOnDragListener(new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch(action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;

                    case DragEvent.ACTION_DROP:
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return true;
            }
        });

        squareButton.setOnDragListener(new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch(action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;

                    case DragEvent.ACTION_DROP:
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return true;
            }
        });

        squareButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 2;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(circleButton);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });

        triangleButton.setOnDragListener(new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch(action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;

                    case DragEvent.ACTION_DROP:
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return true;
            }
        });

        triangleButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 3;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(circleButton);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });

        hexagonButton.setOnDragListener(new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch(action) {

                    case DragEvent.ACTION_DRAG_STARTED:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;

                    case DragEvent.ACTION_DROP:
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return true;
            }
        });

        hexagonButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 4;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(circleButton);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });

        game.setOnDragListener(new View.OnDragListener(){
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                int x = (int)event.getX();
                int y = (int)event.getY();

                int placeX = x - game.getShapeWidth()/2;

                int placeY = y - game.getShapeWidth()/2;

                switch(action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;

                    case DragEvent.ACTION_DROP:
                        if (y < game.getHeight()) {
                            switch (dragType) {
                                case 1:
                                    game.getShapeArrayList().add(new Circle(new OvalShape(), placeX, placeY));
                                    break;
                                case 2:
                                    game.getShapeArrayList().add(new Square(new PathShape(drawShape.drawSquare(), 100, 100), placeX, placeY));
                                    break;
                                case 3:
                                    game.getShapeArrayList().add(new Triangle(new PathShape(drawShape.drawTriangle(), 100, 100), placeX, placeY));
                                    break;
                                case 4:
                                    game.getShapeArrayList().add(new Hexagon(new PathShape(drawShape.drawHexagon(), 100, 100), placeX, placeY));
                                    break;
                                default:
                                    break;
                            }
                            game.invalidate();
                        }
                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:

                        return true;

                    default:
                        break;
                }
                return true;
            }
        });

//        circleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                game.getShapeArrayList().add(new Circle(new OvalShape(), 500, 10));
//                game.invalidate();
//            }
//        });
//
//        squareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                game.getShapeArrayList().add(new Square(new PathShape(drawShape.drawSquare(), 100, 100), 200, 200));
//                game.invalidate();
//            }
//        });
//
//        triangleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                game.getShapeArrayList().add(new Triangle(new PathShape(drawShape.drawTriangle(), 100, 100), 400, 200));
//                game.invalidate();
//            }
//        });
//
//        hexagonButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                game.getShapeArrayList().add(new Hexagon(new PathShape(drawShape.drawHexagon(), 100, 100), 200, 400));
//                game.invalidate();
//            }
//        });


        checkStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList <Node> allFriendKonnections = new ArrayList<Node>();
                if(game.getShapeArrayList().size() > 0) {
                    int number = Structure.countAllNodeRelatives(game.getShapeArrayList().get(0), allFriendKonnections);
                    if(number != game.getShapeArrayList().size()){

                    }else{
                        //boolean test = structure.matchesStructure(game.getShapeArrayList(), game.getBondArrayList());
                        boolean test = Structure.areStructuresSimilarEnough(game.getShapeArrayList(), game.getBondArrayList(), gameStruct.getNodes(), gameStruct.getBonds());
                        if (test){
                            gameStat.setText("Level Passed");
                            gameStat.setTextColor(Color.WHITE);
                            gameStat.setTextSize(20f);
                        }else if (!test){

                        }
                    }
                }

            }
        });

        // setting up local storage for user
        String filename = "userProgress123";
        userProgress = new File(getFilesDir(), filename);
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
