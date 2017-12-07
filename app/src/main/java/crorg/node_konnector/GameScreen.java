package crorg.node_konnector;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private ImageView triangleImage;

    private ImageView circleImage;

    private ImageView squareImage;

    private ImageView hexagonImage;

    private Button checkStructure;
    private int level;

    private ImageButton trashButton;

    private TextView numShapes;
    private TextView numBonds;

    private TextView gameStat;

    private GameCanvas game;
    private DrawPath drawShape;

    private int scores;

    private ArrayList<Bond> userBonds_LIST;
    private ArrayList<Node> userNodes_LIST;
    private Structure answerStructure;    // the logic holding the answer for a given level
    private File userBonds_FILE;
    private File userNodes_FILE;
    private File answerStructure_FILE;

    private int lastLevelPlayed;

    private DatabaseReference userData;

    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private int dragType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MESSAGE#45689", "Before content view...");
        setContentView(R.layout.activity_game_screen);
        Log.v("MESSAGE#45689", "AFTER content view");


        // intent gets the level selected
        Intent intent = getIntent();
        scores = 0;
        String message = intent.getStringExtra(LevelSelectScreen.LEVEL_MESSAGE);
        level = 0;


        if (isLoggedIn()) {
            database = FirebaseDatabase.getInstance();
            currentUser = FirebaseAuth.getInstance().getCurrentUser();

            userData = database.getReference("USERS_TABLE");
            myRef = database.getReference("USERS_TABLE").child(currentUser.getUid()).child("Score");

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    int value = dataSnapshot.getValue(Integer.class);
                    scores = value;
                    Log.v("TAG", "Score is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.v("TAG", "Failed to read value.", error.toException());
                }
            });
        }

        dragType = 0;


        ////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////
        level = 1 + Integer.parseInt(message);
// VERY IMPORTANT THING HERE - LOADING FROM FILE!!!!  //////////////////////////
// setting up local storage for user's progres on a given level...
        final String uBondsFile = "userProgressBonds123";
        final String uNodesFile = "userProgressNodes123";
        final String answerStructureFile = "answerStructure123";
        userBonds_FILE = new File(getFilesDir(), uBondsFile);
        userNodes_FILE = new File(getFilesDir(), uNodesFile);
        answerStructure_FILE = new File(getFilesDir(), answerStructureFile);
        userBonds_LIST = null;
        userNodes_LIST = null;
        answerStructure = null;
        readFromFileSerial();

        // IF everything checks out, then load info locally from file...

        if ((userBonds_LIST != null) && (userNodes_LIST != null)
                && (answerStructure != null) && (level == answerStructure.getNodes().size())) {
            game.setNodesArrayList(userNodes_LIST);
            game.setBondArrayList(userBonds_LIST);
        } else {
            // new game structure
            answerStructure = new Structure(level);
        }

//        if ((userBonds_LIST != null) && (userNodes_LIST != null)
//                && (answerStructure != null) && (level == answerStructure.getNodes().size())) {
//            game.setNodesArrayList(userNodes_LIST);
//            game.setBondArrayList(userBonds_LIST);
//        } else {
        // new game structure
        answerStructure = new Structure(level);
        // }

        ////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////

        gameStat = (TextView) findViewById(R.id.gameStat);
        gameStat.setText("Total Nodes:  " + level);
        gameStat.setTextColor(Color.rgb(255, 165, 00));
        gameStat.setTextSize(20f);


        // draw shape holds different shape to be show on canvas
        drawShape = new DrawPath();

        // display the game info
        answerStructure.displayStringDescriptionForPlayer();

        // associate game canvas
        game = (GameCanvas) findViewById(R.id.gameCanvas);

        numBonds = (TextView) findViewById(R.id.numBonds);
        numBonds.setText(answerStructure.printNumBonds());
        numBonds.setTextColor(Color.WHITE);
        numBonds.setTextSize(20f);

        numShapes = (TextView) findViewById(R.id.numShapes);
        numShapes.setText(answerStructure.printNumShapes());
        numShapes.setTextColor(Color.WHITE);
        numShapes.setTextSize(20f);

        //set the shape Recycler View to horizontal
//        shapeRecyclerView = (RecyclerView)findViewById(R.id.shapeRecyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        shapeRecyclerView.setLayoutManager(layoutManager);

        // associate buttons
        singleButton = (ToggleButton) findViewById(R.id.bondButton);
        doubleButton = (ToggleButton) findViewById(R.id.doubleButton);
        tripleButton = (ToggleButton) findViewById(R.id.tripleButton);
        checkStructure = (Button) findViewById(R.id.checkStructure);
        circleImage = (ImageView) findViewById(R.id.circleView);
        squareImage = (ImageView) findViewById(R.id.squareView);
        triangleImage = (ImageView) findViewById(R.id.triangleView);
        hexagonImage = (ImageView) findViewById(R.id.hexagonView);

        trashButton = (ImageButton) findViewById(R.id.trashBtn);


        singleButton.setText("Single");
        doubleButton.setText("Double");
        tripleButton.setText("Triple");

        singleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    game.setBondingMode(true, 1);
                    doubleButton.setEnabled(false);
                    tripleButton.setEnabled(false);
                    circleImage.setEnabled(false);
                    squareImage.setEnabled(false);
                    triangleImage.setEnabled(false);
                    hexagonImage.setEnabled(false);
                    trashButton.setEnabled(false);
                    game.invalidate();
                } else {
                    game.setBondingMode(false, 0);
                    doubleButton.setEnabled(true);
                    tripleButton.setEnabled(true);
                    circleImage.setEnabled(true);
                    squareImage.setEnabled(true);
                    triangleImage.setEnabled(true);
                    hexagonImage.setEnabled(true);
                    trashButton.setEnabled(true);
                    game.invalidate();
                }
                singleButton.setText("Single");
                doubleButton.setText("Double");
                tripleButton.setText("Triple");
            }
        });

        doubleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    game.setBondingMode(true, 2);
                    singleButton.setEnabled(false);
                    tripleButton.setEnabled(false);
                    circleImage.setEnabled(false);
                    squareImage.setEnabled(false);
                    triangleImage.setEnabled(false);
                    hexagonImage.setEnabled(false);
                    trashButton.setEnabled(false);
                    game.invalidate();
                } else {
                    game.setBondingMode(false, 0);
                    singleButton.setEnabled(true);
                    tripleButton.setEnabled(true);
                    circleImage.setEnabled(true);
                    squareImage.setEnabled(true);
                    triangleImage.setEnabled(true);
                    hexagonImage.setEnabled(true);
                    trashButton.setEnabled(true);
                    game.invalidate();
                }
                singleButton.setText("Single");
                doubleButton.setText("Double");
                tripleButton.setText("Triple");
            }
        });

        tripleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    game.setBondingMode(true, 3);
                    singleButton.setEnabled(false);
                    doubleButton.setEnabled(false);
                    circleImage.setEnabled(false);
                    squareImage.setEnabled(false);
                    triangleImage.setEnabled(false);
                    hexagonImage.setEnabled(false);
                    trashButton.setEnabled(false);
                    game.invalidate();
                } else {
                    game.setBondingMode(false, 0);
                    singleButton.setEnabled(true);
                    doubleButton.setEnabled(true);
                    circleImage.setEnabled(true);
                    squareImage.setEnabled(true);
                    triangleImage.setEnabled(true);
                    hexagonImage.setEnabled(true);
                    trashButton.setEnabled(true);
                    game.invalidate();
                }
                singleButton.setText("Single");
                doubleButton.setText("Double");
                tripleButton.setText("Triple");
            }
        });


        circleImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 1;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(circleImage);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });

        squareImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 2;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(squareImage);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });

        triangleImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 3;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(triangleImage);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });

        hexagonImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ClipData data = ClipData.newPlainText("", "");
                dragType = 4;
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(hexagonImage);
                view.startDrag(data, shadow, null, 0);
                return true;
            }
        });

        game.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                int x = (int) event.getX();
                int y = (int) event.getY();
                int midX = x - game.getShapeWidth() / 2;
                int midY = y - game.getShapeWidth() / 2;
                switch (action) {
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
                                    game.getShapeArrayList().add(new Circle(new OvalShape(), midX, midY));
                                    break;
                                case 2:
                                    game.getShapeArrayList().add(new Square(new PathShape(drawShape.drawSquare(), 100, 100), midX, midY));
                                    break;
                                case 3:
                                    game.getShapeArrayList().add(new Triangle(new PathShape(drawShape.drawTriangle(), 100, 100), midX, midY));
                                    break;
                                case 4:
                                    game.getShapeArrayList().add(new Hexagon(new PathShape(drawShape.drawHexagon(), 100, 100), midX, midY));
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

        trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.deleteSelectedNode();
            }
        });


        // also need to verify number of konnections...
        checkStructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Node> allFriendKonnections = new ArrayList<Node>();
                if (game.getShapeArrayList().size() > 0) {
                    int numPlayerKonnectedNodes = Structure.countAllNodeRelatives(game.getShapeArrayList().get(0), allFriendKonnections);
                    // Is the player's structure intact?
                    if (numPlayerKonnectedNodes == game.getShapeArrayList().size()) {
                        // does the player's number of nodes match the answer num of nodes?
                        if (numPlayerKonnectedNodes == answerStructure.getNodes().size()) {
                            // Right number of bonds?...
                            if (game.getBondArrayList().size() == answerStructure.getBonds().size()) {
                                // Now check the BOND configs...
                                if (Structure.areBondsSimilar(game.getBondArrayList(), answerStructure.getBonds())) {
                                    // check All Konnections...
                                    if (Structure.checkAllCircleKonnections(game.getShapeArrayList())) {
                                        if (Structure.checkAllSquareKonnections(game.getShapeArrayList())) {
                                            if (Structure.checkAllTriangleKonnections(game.getShapeArrayList())) {
                                                if (Structure.checkAllHexagonKonnections(game.getShapeArrayList())) {
                                                    if (Structure.areShapeConfigsSimilar(game.getShapeArrayList(), answerStructure.getNodes())) {
                                                        gameStat.setText("Congratz!");

                                                        // if passed, delete local files...
                                                        answerStructure_FILE.delete();
                                                        userBonds_FILE.delete();
                                                        userNodes_FILE.delete();


                                                        // WE NEED To seT the LOCAL COPY OF THEIR SCORE ALSO
                                                        if (isLoggedIn()) {
                                                            // WE NEED to compare firebase's values with local values - if they don't match,
                                                            // set both to the HIGHER of the two.  THEN up the score and level as below...
                                                            scores += Math.pow(3, level);
                                                            userData.child(currentUser.getUid()).child("Score").setValue(scores);
                                                            userData.child(currentUser.getUid()).child("Level").setValue(level);
                                                            // update level and score LOCALLY also...
                                                        } else {
                                                            scores += Math.pow(3, level);
                                                            // up their current level...
                                                            // SAVE this stuff LOCaLlY so that when they open the app again, the data remains
                                                        }
                                                    } else {
                                                        gameStat.setText("Oops! Shape AMOUNTS don't match!");
                                                    }
                                                } else {
                                                    gameStat.setText("HEXAGONS must have exactly 4 Konnections!");
                                                }
                                            } else {
                                                gameStat.setText("TRIANGLES must have exactly 3 Konnections!");
                                            }
                                        } else {
                                            gameStat.setText("SQUARES must have exactly 2 Konnections!");
                                        }
                                    } else {
                                        gameStat.setText("CIRCLES must have exactly 1 Konnection!");
                                    }
                                } else {
                                    gameStat.setText("Oops! Bond TYPES don't match!");
                                }
                            } else {
                                gameStat.setText("Oops! Wrong number of BONDS!");
                            }
                        } else {
                            gameStat.setText("WRONG number of Nodes!");
                        }
                    } else {
                        gameStat.setText("Oops! All nodes must be Konnected to the SAME structure!");
                    }
                }

            }
        });
    }


    // use these to save the state of the game
    public void writeToFile(View view) {
        String textToDisplay = "hello, boyo!";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(userBonds_FILE.getName(), Context.MODE_PRIVATE);
            outputStream.write(textToDisplay.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // use these to save the current state of the level...
    public void writeToFileSerial() {
        // write player bonds to file...
        try {
            FileOutputStream fos = openFileOutput(userBonds_FILE.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game.getBondArrayList());
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // write player nodes to file...
        try {
            FileOutputStream fos = openFileOutput(userNodes_FILE.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game.getShapeArrayList());
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // write answer to file
        try {
            FileOutputStream fos = openFileOutput(answerStructure_FILE.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(answerStructure);
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // use these to save the current state of the level...
    public void readFromFileSerial() {
        // get user nodes...
        try {
            FileInputStream fis = openFileInput(userNodes_FILE.getName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            userNodes_LIST = (ArrayList<Node>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get user bonds...
        try {
            FileInputStream fis = openFileInput(userBonds_FILE.getName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            userBonds_LIST = (ArrayList<Bond>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get answer structure
        try {
            FileInputStream fis = openFileInput(answerStructure_FILE.getName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            answerStructure = (Structure) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void readFromFileThing(View view) {
        FileInputStream inputStream;
        String s = "";
        try {
            inputStream = openFileInput(userBonds_FILE.getName());
            int nextByte = 0;
            while (nextByte != -1) {
                nextByte = inputStream.read();
                s += (char) nextByte;
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void testStructureList() {
//        // when a fragment is touched, send that level integer to the next screen
//        // the next screen will generate a structure
//        // don't know where this goes, but...
//
//        // pass this value from some screen;
//        int levelNumber = 3;
//        ArrayList<Structure> previousStructures = null;    // steal this from local storage/mobile
//        boolean keepGoing = true;
//        mainLoop:
//        while (keepGoing) {
//            Structure candidate = new Structure(levelNumber);
//            if (previousStructures.size() > 0) {
//                for (Structure previous : previousStructures) {
//                    boolean matchFound = Structure.areShapeConfigsSimilar(candidate.getNodes(), candidate.getBonds(), previous.getNodes(), previous.getBonds());
//                    if (matchFound) {
//                        continue mainLoop;
//                    }
//                }
//            }
//            break;
//        }
//    }


    public ArrayList<Structure> getStorageArrayList() {
        return null;
    }

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }


}
