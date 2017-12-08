package crorg.node_konnector;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import crorg.node_konnector.GamePanel.GameCanvas;
import crorg.node_konnector.Shapes.Circle;

import crorg.node_konnector.Shapes.DrawPath;
import crorg.node_konnector.Shapes.Hexagon;
import crorg.node_konnector.Shapes.MyOvalShape;
import crorg.node_konnector.Shapes.MyPathShape;
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

    private ImageButton trashButton;

    private TextView numShapes;
    private TextView numBonds;

    private TextView gameStat;

    private GameCanvas game;
    private DrawPath drawShape;

    private int currentNodePlaying;
    private int currentLevelPlaying;
    private int highestScore;

    private ArrayList<Bond> userBonds_LIST;
    private ArrayList<Node> userNodes_LIST;
    private Structure answerStructure;    // the logic holding the answer for a given currentNodePlaying
    private File userBonds_FILE;
    private File userNodes_FILE;
    private File answerStructure_FILE;

    // primitive data only
    private File userScore_FILE;
    private File userLevel_FILE;

    private int lastLevelPlayed;

    private int highestLevel;

    /** Firecase set and retrieve variables */
    private DatabaseReference userData;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private int dragType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        highestScore = 0;
        dragType = 0;
        highestLevel = 1;
        currentLevelPlaying = 1;
        currentNodePlaying = 0;
        highestLevel = 1;

        // intent gets the currentNodePlaying selected
        Intent intent = getIntent();
        highestLevel = intent.getIntExtra(LevelSelectScreen.HIGHEST_LEVEL, highestLevel);

        currentLevelPlaying = intent.getIntExtra(LevelSelectScreen.LEVEL_MESSAGE, currentLevelPlaying);

        highestScore = intent.getIntExtra(LevelSelectScreen.HIGHEST_SCORE, highestScore);

//        highestScore = intent.getIntExtra(LevelSelectScreen.HIGHEST_SCORE, highestScore);

        Log.d("MESSAGE#", "What are you" + highestScore + " current" + currentLevelPlaying + " hight" + highestLevel );
        currentNodePlaying = currentLevelPlaying+1;

        // VERY IMPORTANT THING HERE - LOADING FROM FILE!!!!  //////////////////////////
        // setting up local storage for user's progres on a given currentNodePlaying...
        final String uBondsFile = "userProgressBonds123";
        final String uNodesFile = "userProgressNodes123";
        final String answerStructureFile = "answerStructure123";
        userBonds_FILE = new File(getFilesDir(), uBondsFile);
        userNodes_FILE = new File(getFilesDir(), uNodesFile);
        answerStructure_FILE = new File(getFilesDir(), answerStructureFile);
        userScore_FILE = new File(getFilesDir(), "userScore_File");
        userLevel_FILE = new File(getFilesDir(), "userLevel_File");

        userBonds_LIST = null;
        userNodes_LIST = null;
        answerStructure = null;
        //readFromFileSerial();
        answerStructure = new Structure(currentNodePlaying);

        // TEMPORARILY delete these...
        //userBonds_FILE.delete();
        //userNodes_FILE.delete();
        //answerStructure_FILE.delete();

        // IF everything checks out, then load info locally from file...

//        if (userBonds_LIST != null) {
//            if (userNodes_LIST != null) {
//                if (answerStructure != null) {
//                    if (currentNodePlaying == answerStructure.getNodes().size()) {
//                        Log.v("MESSAGE#45689", "DATA FROM FILES LOADED... recreating structure...");
//                        game.setNodesArrayList(userNodes_LIST);
//                        Log.v("MESSAGE#45689", "after set node");
//                        game.setBondArrayList(userBonds_LIST);
//                        Log.v("MESSAGE#45689", "after set bond");
//                        game.invalidate();
//                        Log.v("MESSAGE#45689", "after repaint");
//                    } else {
//                        Log.v("MESSAGE#45689", "currentNodePlaying not the same as answer");
//                        Log.v("MESSAGE#45689", "Creating new structure...");
//                        answerStructure = new Structure(currentNodePlaying);
//                    }
//                } else {
//                    Log.v("MESSAGE#45689", "answer structure is null!");
//                    Log.v("MESSAGE#45689", "Creating new structure...");
//                    answerStructure = new Structure(currentNodePlaying);
//                }
//            } else {
//                Log.v("MESSAGE#45689", "nodes list is null!");
//                Log.v("MESSAGE#45689", "Creating new structure...");
//                answerStructure = new Structure(currentNodePlaying);
//            }
//        } else {
//            Log.v("MESSAGE#45689", "bonds list is null!");
//            Log.v("MESSAGE#45689", "Creating new structure...");
//            answerStructure = new Structure(currentNodePlaying);
//        }


        ////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////

        gameStat = (TextView) findViewById(R.id.gameStat);
        gameStat.setText("Total Nodes:  " + currentNodePlaying);
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
                                    game.getShapeArrayList().add(new Circle(new MyOvalShape(), midX, midY));
                                    break;
                                case 2:
                                    game.getShapeArrayList().add(new Square(new MyPathShape(drawShape.drawSquare(), 100, 100), midX, midY));
                                    break;
                                case 3:
                                    game.getShapeArrayList().add(new Triangle(new MyPathShape(drawShape.drawTriangle(), 100, 100), midX, midY));
                                    break;
                                case 4:
                                    game.getShapeArrayList().add(new Hexagon(new MyPathShape(drawShape.drawHexagon(), 100, 100), midX, midY));
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

                                                        // check if current level playing is the highest level player get to.
                                                        if(currentLevelPlaying == highestLevel){
                                                            highestLevel++;
                                                        }

                                                        // WE NEED To seT the LOCAL COPY OF THEIR SCORE ALSO
                                                        if (isLoggedIn()) {
                                                            // WE NEED to compare firebase's values with local values - if they don't match,
                                                            // set both to the HIGHER of the two.  THEN up the score and currentNodePlaying as below...
                                                            highestScore += Math.pow(3, currentNodePlaying);
                                                            userData.child(currentUser.getUid()).child("Score").setValue(highestScore);
                                                            userData.child(currentUser.getUid()).child("Level").setValue(highestLevel);
                                                            // update currentNodePlaying and score LOCALLY also...
                                                        } else {
                                                            highestScore += Math.pow(3, currentNodePlaying);
                                                            nonSerialWriteToFile();
                                                            // up their current currentNodePlaying...
                                                            // SAVE this stuff LOCaLlY so that when they open the app again, the data remains
                                                        }
                                                        checkStructure.setEnabled(false);
                                                        singleButton.setEnabled(false);
                                                        doubleButton.setEnabled(false);
                                                        tripleButton.setEnabled(false);
                                                        trashButton.setEnabled(false);
                                                        circleImage.setEnabled(false);
                                                        squareImage.setEnabled(false);
                                                        triangleImage.setEnabled(false);
                                                        hexagonImage.setEnabled(false);
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


        database = FirebaseDatabase.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userData = database.getReference("USERS_TABLE");

//        if (isLoggedIn()) {
//
//
//
//
//
//            // Read from the database
//            userData.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.child(currentUser.getUid()).child("Level").exists()){
//                        int value = dataSnapshot.child(currentUser.getUid()).child("Level").getValue(Integer.class);
//                        currentNodePlaying = value;
//                    } else{
//                        userData.child(currentUser.getUid()).child("Level").setValue(currentNodePlaying);
//                    }
//                    if(dataSnapshot.child(currentUser.getUid()).child("Score").exists()){
//                        int value = dataSnapshot.child(currentUser.getUid()).child("Score").getValue(Integer.class);
//                        highestScore = value;
//                    } else{
//                        userData.child(currentUser.getUid()).child("Score").setValue(highestScore);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.v("TAG", "Failed to read value.", error.toException());
//                }
//            });
//        }
    }


    // use these to save the state of the game
    public void nonSerialWriteToFile() {
        String textToDisplay = "hello, boyo!";
        FileOutputStream outputStream;

        // save user score...
        try {
            outputStream = openFileOutput(userScore_FILE.getName(), Context.MODE_PRIVATE);
            outputStream.write(highestScore);
            Log.d("MESSAGE#", "DO I GET SCORE SAVE?" + highestScore);
            outputStream.close();
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // save user currentNodePlaying
        try {
            outputStream = openFileOutput(userLevel_FILE.getName(), Context.MODE_PRIVATE);
            outputStream.write(highestLevel);
            Log.d("MESSAGE#", "DO I GET SCORE SAVE?" + highestLevel);
            outputStream.close();
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    // use these to save the current state of the currentNodePlaying...
    public void writeToFileSerial() {
        // write player bonds to file...
        try {
            FileOutputStream fos = openFileOutput(userBonds_FILE.getName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game.getBondArrayList());
            fos.close();
            oos.close();
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
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
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
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
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // use these to save the current state of the currentNodePlaying...
    public void readFromFileSerial() {
        // get user nodes...
        try {
            FileInputStream fis = openFileInput(userNodes_FILE.getName());
            ObjectInputStream ois = new ObjectInputStream(fis);
            userNodes_LIST = (ArrayList<Node>) ois.readObject();
            ois.close();
            fis.close();
            Log.v("MESSAGE#45689", "User nodes loaded successfully!");
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (StreamCorruptedException ff) {
            Log.v("MESSAGE#45689", "StreamCorruptedException: " + ff.getMessage());
        } catch (ClassNotFoundException ff) {
            Log.v("MESSAGE#45689", "ClassNotFoundException: " + ff.getMessage());
        } catch (OptionalDataException ff) {
            Log.v("MESSAGE#45689", "OptionalDataException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
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
            Log.v("MESSAGE#45689", "User bonds loaded successfully!");
        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (StreamCorruptedException ff) {
            Log.v("MESSAGE#45689", "StreamCorruptedException: " + ff.getMessage());
        } catch (ClassNotFoundException ff) {
            Log.v("MESSAGE#45689", "ClassNotFoundException: " + ff.getMessage());
        } catch (OptionalDataException ff) {
            Log.v("MESSAGE#45689", "OptionalDataException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
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
            Log.v("MESSAGE#45689", "Answer structure from file loaded successfully! Details: " + answerStructure.printNumBonds());

        } catch (FileNotFoundException ff) {
            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
        } catch (SecurityException ff) {
            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
        } catch (InvalidClassException ff) {
            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
        } catch (StreamCorruptedException ff) {
            Log.v("MESSAGE#45689", "StreamCorruptedException: " + ff.getMessage());
        } catch (ClassNotFoundException ff) {
            Log.v("MESSAGE#45689", "ClassNotFoundException: " + ff.getMessage());
        } catch (OptionalDataException ff) {
            Log.v("MESSAGE#45689", "OptionalDataException: " + ff.getMessage());
        } catch (NullPointerException ff) {
            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
        } catch (NotSerializableException ff) {
            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
        }catch (IOException ff) {
            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public boolean nonSerialReadFromFile() {
//        FileInputStream inputStream;
////        String s = "";
////        try {
////            inputStream = openFileInput(userBonds_FILE.getName());
////            int nextByte = 0;
////            while (nextByte != -1) {
////                nextByte = inputStream.read();
////                s += (char) nextByte;
////            }
////            inputStream.close();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//        // get user currentNodePlaying
//        try {
//            inputStream = openFileInput(userLevel_FILE.getName());
//            currentNodePlaying = inputStream.read();
//            inputStream.close();
//        } catch (FileNotFoundException ff) {
//            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
//        } catch (SecurityException ff) {
//            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
//        } catch (InvalidClassException ff) {
//            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
//        } catch (NullPointerException ff) {
//            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
//        } catch (NotSerializableException ff) {
//            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
//        }catch (IOException ff) {
//            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // get user highestScore
//        try {
//            inputStream = openFileInput(userScore_FILE.getName());
//            highestScore = inputStream.read();
//            inputStream.close();
//            return true;
//        } catch (FileNotFoundException ff) {
//            Log.v("MESSAGE#45689", "FileNotFoundException: " + ff.getMessage());
//        } catch (SecurityException ff) {
//            Log.v("MESSAGE#45689", "SecurityException: " + ff.getMessage());
//        } catch (InvalidClassException ff) {
//            Log.v("MESSAGE#45689", "InvalidClassException: " + ff.getMessage());
//        } catch (NullPointerException ff) {
//            Log.v("MESSAGE#45689", "NullPointerException: " + ff.getMessage());
//        } catch (NotSerializableException ff) {
//            Log.v("MESSAGE#45689", "NotSerializableException: " + ff.getMessage());
//        }catch (IOException ff) {
//            Log.v("MESSAGE#45689", "IOException: " + ff.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }


//    public void testStructureList() {
//        // when a fragment is touched, send that currentNodePlaying integer to the next screen
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
