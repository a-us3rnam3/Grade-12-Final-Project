package com.mygdx.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Where the core game loop and fun is found
 */
public class Game extends ScreenAdapter {

    MyGdxGame game;
    Boolean newGame;

    /**
     * creates Game using MyGdxGame as a base
     * @param game containts shared resources and provides function to switch screens
     * @param newGame informs if this is to be loaded from a save or starting fresh
     */
    public Game(MyGdxGame game, Boolean newGame) {
        this.game = game;
        this.newGame = newGame;
    }

    private OrthographicCamera cam;

    private Texture backgroundTexture;

    private Sprite characterSprite;
    private PathSprite teacherSprite;

    private Sprite cursorSprite = new Sprite();

    private ArrayList<Rectangle> walls;

    private Rectangle tempRectangle;
    private ArrayList<Rectangle> tempRectangleList = new ArrayList<Rectangle>();

    private boolean rectangleCreation;

    private Polygon sightlineTriangle;

    private float[] sightlineVerticies = new float[]{0,0,0,0,0,0};

    private boolean devWallDraw = false;

    private Stage testStage = new Stage();
    private Stage timerStage = new Stage();
    private Table testTable = new Table();
    private Table timerTable = new Table();
    
    private Skin uiSkin;
    private BetterLabel label;
    private Label timerLabel;

    private FileHandle file;
    private String text;
    private String[] wordsArray;
    private Array<String> settings;

    private float timeUsed;
    private float timelimit;
    private int timerMin;
    private int timerSec;
    private float secondTimer;

    Rectangle charLocation;

    private boolean hiding = false;

    private enum RoomType {
        PROGRAMMING,
        MANUFACTURING,
        DESIGN,
        HALLWAY,
    }

    private enum HallwayType {
        MANUFACTURING_HALLWAY,
        PROGRAMMING_DESIGN_HALLWAY,
        CROSSROAD_AREA,
        ROOM,
    }

    private enum HidingLocation{
        PROGRAMMING,
        MANUFACTURING,
        DESIGN,
        NOT_HIDING,
    }

    private enum Subteams{
        PROGRAMMING,
        MANUFACTURING,
        DESIGN,
    }

    RoomType roomIn;

    HallwayType hallwayIn;

    Subteams subteamChosen = Subteams.PROGRAMMING;

    HidingLocation hidingIn = HidingLocation.NOT_HIDING;

    Boolean inHallway;

    Rectangle electricalRoomRect = new Rectangle(36.84491f, 906.74146f, 850.5463f, 638.6559f); //sets up the area that an object is in
    Rectangle electricalViewRect = new Rectangle(887.6741f, 1094.01f, 279.20337f, 91.953125f);

    Rectangle designRoomRect = new Rectangle(39.024418f, 2173.3633f, 850.0001f, 581.25f);
    Rectangle designRoomViewRect = new Rectangle(885.89954f, 2270.2383f, 278.125f, 118.75f);

    Rectangle manuRoomRect = new Rectangle(1163.66f, 3170.321f, 1742.9188f, 956.57544f);
    Rectangle manuRoomViewRect = new Rectangle(2093.005f, 2786.3494f, 83.109375f, 387.84375f);

    Rectangle manuHallway = new Rectangle(43.036137f, 2786.419f, 3003.1033f, 359.2683f);
    Rectangle elecDesignHallway = new Rectangle(916.76526f, 30.086924f, 252.04614f, 2755.3484f);
    Rectangle[] manuHallwayPieces = { new Rectangle(42.198246f, 2783.984f, 874.7434f, 360.79688f),
            new Rectangle(918.89124f, 2785.6816f, 246.74768f, 359.81836f),
            new Rectangle(1166.2725f, 2785.685f, 1878.3586f, 361.7351f) };
    Rectangle[] elecDesHallwayPieces = { new Rectangle(919.84814f, 30.546062f, 245.01099f, 1065.1954f),
            new Rectangle(919.4762f, 1094.5354f, 246.61401f, 88.28125f),
            new Rectangle(919.2153f, 1182.8965f, 246.24335f, 1090.2849f),
            new Rectangle(919.6883f, 2273.018f, 245.31244f, 90.625f),
            new Rectangle(920.64496f, 2364.3933f, 244.53119f, 421.59033f), };

        Rectangle wireRect = new Rectangle(347.17676f, 1162.4961f, 105.0625f, 69.1875f);
        Rectangle wireSpoolrect = new Rectangle(472.13458f, 1200.0325f, 32.703125f, 32.703125f);
        Rectangle wireConnectionrect = new Rectangle(725.6663f, 942.6737f, 53.12506f, 56.25f);
        Rectangle elecTeacherDesk = new Rectangle(706.6007f, 919.7616f, 89.90637f, 98.46881f);
        Rectangle elecTeacherChair = new Rectangle(816.9347f, 930.11285f, 38.53125f, 85.625f);
        Rectangle elecComputerFront1 = new Rectangle(133.52905f, 1378.721f, 74.00006f, 86.0f);
        Rectangle elecComputerFront2 = new Rectangle(253.52914f, 1380.7208f, 70.00003f, 86.00012f);
        Rectangle elecComputerFront3 = new Rectangle(365.5292f, 1380.7208f, 72.00003f, 86.00012f);
        Rectangle elecComputerFront4 = new Rectangle(484.40424f, 1380.4385f, 73.90631f, 84.65637f);
        Rectangle elecComputerFront5 = new Rectangle(603.9981f, 1383.126f, 69.78265f, 81.31372f);
        Rectangle elecComputerSide1 = new Rectangle(59.4357f, 982.5787f, 39.84305f, 83.921936f);
        Rectangle elecComputerSide2 = new Rectangle(62.541443f, 1089.2751f, 36.84375f, 73.6875f);
        Rectangle elecComputerSide3 = new Rectangle(57.426773f, 1179.0851f, 42.984406f, 79.82825f);
        Rectangle elecComputerSide4 = new Rectangle(60.001495f, 1269.0809f, 38.89067f, 79.828125f);

        Rectangle desComputersFront = new Rectangle(130.68817f,2635.7385f,671.3844f,91.73926f);
        Rectangle desComputersLeft = new Rectangle(53.92829f,2252.7346f,61.859375f,386.20312f);
        Rectangle desComputersBack = new Rectangle(140.54156f,2179.5798f,536.0914f,73.5625f);
        Rectangle desComputerMiddleSpec = new Rectangle(307.22766f,2475.1687f,75.35953f,33.953125f);
        Rectangle desComputerBackSpec = new Rectangle(709.1736f,2177.916f,87.76575f,74.921875f);
        Rectangle desComputerRightSpec = new Rectangle(816.3156f,2547.2117f,65.84381f,93.40625f);
        Rectangle desComputerRight2 = new Rectangle(816.36456f,2455.9995f,62.890625f,77.984375f);
        Rectangle desComputersMiddleFront = new Rectangle(305.00043f,2383.3171f,176.24649f,81.73755f);
        Rectangle desComputerMiddleBack = new Rectangle(407.1723f,2477.8262f,71.520325f,28.097168f);
        Rectangle desComputerTeacher = new Rectangle(228.37152f,2411.4143f,48.531647f,86.84619f);
        
        Rectangle weldingArea = new Rectangle(1161.2478f,3176.5137f,610.5939f,521.875f);
        Rectangle longTable = new Rectangle(1951.8184f,3409.9114f,80.89075f,318.34375f);
        Rectangle picture = new Rectangle(1781.13f,3240.2844f,26.625f,47.70337f);
        Rectangle teacherArea = new Rectangle(1921.5046f,3174.3748f,153.094f,158.64087f);
        Rectangle bumpers = new Rectangle(2279.2183f,3172.901f,161.71875f,85.9375f);
        Rectangle lathes = new Rectangle(2484.9666f,3403.4397f,269.73853f,345.3838f);
        Rectangle footshear = new Rectangle(2209.681f,3759.0881f,86.6875f,143.7185f);

        Rectangle elecMember = new Rectangle(348.08627f,1070.0099f,47.65628f,50.0f);

        Rectangle designCloset = new Rectangle(496.9907f,2319.7f,191.25021f,40.0f);
        Rectangle manuCloset = new Rectangle(1396.5314f,3871.0215f,735.4376f,146.0625f);
        Rectangle elecCloset = new Rectangle(750.40625f, 1414.7618f, 82.06262f, 121.51575f);

    Rectangle vent = new Rectangle(2686.0603f,3035.7346f,76.500244f,46.750244f);
    Rectangle wireConnection = new Rectangle(538.93634f,1134.6736f,143.00012f,115.37512f);
    Rectangle reactionTest = new Rectangle(713.4106f,2476.2263f,50.78125f,51.5625f);

    private boolean reactionPass;
    private boolean ventAccess;
    private boolean reactionAttempt;
    private boolean connectWireAttempt;

    private boolean wasSeen = false;

    Vector2 mouseLoc;
    Vector3 interactMouseLocation;

    /**
     * called when this becomes the current screen for game, also initiates more variables
     */
    @Override
    public void show() {
        try{
            uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        }catch (Exception e){
            game.setScreen(new ErrorScreen(game));
            return;
        }
        timerLabel = new Label(null, uiSkin);
        label = new BetterLabel(null, uiSkin);

        secondTimer = 0f;
        timeUsed = 0f;
        timelimit = 601f;

        reactionPass = false;
        ventAccess = false;

        try{
        backgroundTexture = new Texture("fullMap.png");

        characterSprite = new Sprite(new Texture("design.jpg"));
        characterSprite.setSize(50, 50);

        teacherSprite = new PathSprite(new Sprite(new Texture("teacher.png")), getSetPath());
        teacherSprite.setSize(100, 100);
        teacherSprite.setOriginCenter();
        }catch (Exception e){
            game.setScreen(new ErrorScreen(game));
            return;
        }

        cursorSprite = new Sprite();
        cursorSprite.setPosition(0, 0);
        cursorSprite.setSize(0.1f, 0.1f);

        cam = new OrthographicCamera(1000, 1000 * (game.getWorldHeight() / game.getWorldWidth()));
        cam.zoom = 10f;
        cam.position.set(characterSprite.getX() + characterSprite.getWidth() / 2,
                characterSprite.getY() + characterSprite.getHeight() / 2, 0);
        cam.update();

        walls = new ArrayList<Rectangle>();
        walls.add(new Rectangle(916.2988f, 0.7812501f, 253.90625f, 30.468746f));// hallways
        walls.add(new Rectangle(885.8301f, 30.468746f, 31.276794f, 1065.2198f));
        walls.add(new Rectangle(889.2984f, 1234.6805f, 28.916687f, 1038.7233f));
        walls.add(new Rectangle(889.7165f, 2414.5122f, 27.61914f, 371.3037f));
        walls.add(new Rectangle(40.29691f, 2755.4502f, 854.9955f, 30.617188f));
        walls.add(new Rectangle(10.3815775f, 2785.9827f, 32.812504f, 258.59375f));
        walls.add(new Rectangle(43.56616f, 3079.6665f, 2052.1555f, 92.04907f));
        walls.add(new Rectangle(2175.5522f, 3082.9487f, 869.4431f, 87.60327f));
        walls.add(new Rectangle(1164.8378f, 26.061474f, 35.9375f, 2760.5005f));
        walls.add(new Rectangle(1201.5565f, 2759.9377f, 1846.0856f, 26.5625f));
        walls.add(new Rectangle(3046.0796f, 2786.5002f, 25.78125f, 310.71338f));// hallways
        walls.add(new Rectangle(38.274796f, 886.6751f, 847.7296f, 25.500061f));// electrical
        walls.add(new Rectangle(39.62848f, 914.3002f, 88.18803f, 491.2621f));
        walls.add(new Rectangle(123.98297f, 1377.9733f, 587.47394f, 23.375122f));
        walls.add(new Rectangle(689.65466f, 1402.4109f, 23.375f, 165.75024f));
        walls.add(new Rectangle(709.84216f, 1460.697f, 180.62537f, 30.812622f));
        walls.add(new Rectangle(219.94307f, 1132.7699f, 546.62366f, 120.90662f));
        walls.add(new Rectangle(709.42596f, 912.6455f, 176.39105f, 111.79712f)); // electrical
        walls.add(new Rectangle(126.40935f, 2630.953f, 704.8563f, 26.25f)); // design
        walls.add(new Rectangle(807.88055f, 2442.4316f, 96.09375f, 24.21875f));
        walls.add(new Rectangle(808.92535f, 2464.3284f, 28.90625f, 178.90625f));
        walls.add(new Rectangle(100.68678f, 2257.1921f, 36.265945f, 391.45605f));
        walls.add(new Rectangle(130.99673f, 2224.0273f, 761.28796f, 37.609375f));
        walls.add(new Rectangle(220.96933f, 2391.4607f, 472.48068f, 124.21875f)); // design
        walls.add(new Rectangle(1921.0376f, 3168.9172f, 158.64087f, 163.07837f));
        walls.add(new Rectangle(1755.3787f, 3174.9749f, 19.96875f, 527.7737f)); // manu walls
        walls.add(new Rectangle(1162.3143f, 3680.938f, 597.95386f, 18.859375f));
        walls.add(new Rectangle(1144.7678f, 3703.1255f, 17.75f, 242.95337f));
        walls.add(new Rectangle(1165.6481f, 3966.116f, 231.85974f, 36.609375f));
        walls.add(new Rectangle(1397.8103f, 3916.3335f, 735.6953f, 97.625244f));
        walls.add(new Rectangle(2134.229f, 3973.3616f, 265.14087f, 22.1875f));
        walls.add(new Rectangle(2398.632f, 3926.0325f, 134.23462f, 72.10962f));
        walls.add(new Rectangle(2534.7964f, 3973.3242f, 370.53174f, 29.055176f));
        walls.add(new Rectangle(2221.6829f, 3815.8442f, 72.109375f, 80.98462f));
        walls.add(new Rectangle(2906.6973f, 3172.061f, 19.96875f, 808.62085f));
        walls.add(new Rectangle(1778.0216f, 3170.5796f, 33.59375f, 154.6875f)); // manu walls
        walls.add(new Rectangle(2274.3574f, 3171.4133f, 5.46875f, 113.28125f)); // cabinet climb
        walls.add(new Rectangle(2279.045f, 3280.007f, 61.71875f, 4.6875f));
        walls.add(new Rectangle(2336.8574f, 3285.4758f, 4.6875f, 32.03125f));
        walls.add(new Rectangle(2402.7017f, 3284.3867f, 5.46875f, 28.90625f));
        walls.add(new Rectangle(2403.483f, 3280.4805f, 40.625f, 4.6875f));
        walls.add(new Rectangle(2440.983f, 3168.7617f, 5.46875f, 115.625f)); // cabinet climb
        walls.add(new Rectangle(1951.3835f, 3458.9287f, 84.28137f, 272.21924f)); // tables
        walls.add(new Rectangle(2279.6558f, 3610.7573f, 130.7815f, 78.468994f));
        walls.add(new Rectangle(2281.65f, 3451.557f, 129.81274f, 83.312744f)); // tables
        walls.add(new Rectangle(2092.3154f, 3606.649f, 132.8125f, 82.8125f)); // stool table
        walls.add(new Rectangle(2107.583f, 3690.811f, 32.03125f, 21.09375f));
        walls.add(new Rectangle(2175.7896f, 3691.2625f, 33.59375f, 18.75f)); // stool table
        walls.add(new Rectangle(2093.2437f, 3452.4097f, 6.25f, 80.46875f)); // climbable table 1
        walls.add(new Rectangle(2102.6187f, 3526.6284f, 110.15625f, 6.25f));
        walls.add(new Rectangle(2216.6812f, 3452.4097f, 7.8125f, 82.8125f));
        walls.add(new Rectangle(2111.4954f, 3420.4458f, 4.6875f, 19.53125f));
        walls.add(new Rectangle(2200.5579f, 3422.0083f, 4.6875f, 19.53125f)); // climbable table 1
        walls.add(new Rectangle(2495.7354f, 3640.6575f, 91.40625f, 102.34375f)); // lathe
        walls.add(new Rectangle(2650.7053f, 3643.7825f, 92.1875f, 101.5625f));
        walls.add(new Rectangle(2490.309f, 3458.1746f, 94.53125f, 96.09375f));
        walls.add(new Rectangle(2652.5078f, 3458.9558f, 89.84375f, 101.5625f)); // lathe

        rectangleCreation = false;

        tempRectangle = new Rectangle();

        sightlineTriangle = new Polygon();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                cam.zoom += 0.03 * amountY;
                return false;
            }
        });

        // testTable.setDebug(true);
        // timerTable.setDebug(true);

        label.setPosition(0, 0);
        label.setWrap(true);

        timerLabel.setPosition(0,0);

        testTable.add(label).width(700);
        testStage.addActor(testTable);

        timerTable.add(timerLabel);
        timerStage.addActor(timerTable);
        
        testTable.setPosition(0, 0);
        testTable.setSize(900, 300);

        
        timerTable.setSize(200, 100);
        timerTable.setPosition(10, 890-timerTable.getHeight());

        label.setFontScale(2f);

        settings = new Array<String>();

        try {
            if (newGame == true) {
                file = Gdx.files.internal("NewSetting.txt");
            } else {
                file = Gdx.files.internal("SavedSetting.txt");
            }
            text = file.readString();
            wordsArray = text.split("\\r?\\n");
            for (String word : wordsArray) {
                settings.add(word);
            }
            characterSprite.setPosition(Float.valueOf(settings.get(0)), Float.valueOf(settings.get(1)));
            teacherSprite.setPosition(Float.valueOf(settings.get(2)), Float.valueOf(settings.get(3)));
            teacherSprite.setWaypoint(Integer.valueOf(settings.get(4)));
            timeUsed = Float.valueOf(settings.get(5));
            reactionPass = Boolean.valueOf(settings.get(6));
            ventAccess = Boolean.valueOf(settings.get(7));
            reactionAttempt = Boolean.valueOf(settings.get(8));
            connectWireAttempt = Boolean.valueOf(settings.get(9));
            hiding = Boolean.valueOf(settings.get(10));
            subteamChosen = Game.Subteams.valueOf(settings.get(11));

        } catch (Exception e) {
            e.printStackTrace();
            game.setScreen(new ErrorScreen(game));
            return;
        }

    }

    @Override
    public void render(float delta) {
        timeUsed += Gdx.graphics.getDeltaTime();
        timerMin = (int) ((timelimit - timeUsed) / 60);
        timerSec = (int) ((timelimit - timeUsed) % 60);
        // System.out.println(timerMin + ":" + timerSec);
        inputs();
        if (!hiding){
            sightlineLogic();
        }else{
            sightlineVerticies = new float[]{0,0,0,0,0,0,}; //had to ensure the sightline would dissapear when hiding
        }

        if (timerSec < 10){
            timerLabel.setText(timerMin+":0"+timerSec+"\n" + subteamChosen.toString());
        }else {
            timerLabel.setText(timerMin+":"+timerSec+"\n" + subteamChosen.toString());
        }
        
        game.batch.setProjectionMatrix(cam.combined);

        ScreenUtils.clear(0, 0, 0, 1);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);

        cam.position.set(characterSprite.getX() + characterSprite.getWidth() / 2,
                characterSprite.getY() + characterSprite.getHeight() / 2, 0);
        cam.update();

        game.batch.end();
        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0);
        teacherSprite.draw(game.batch);
        if (!hiding){
            // System.out.println("not hiding");
            characterSprite.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(cam.combined);

        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setProjectionMatrix(cam.combined);
        game.shapeRenderer.setColor(Color.GRAY);

        if (roomIn.equals(RoomType.PROGRAMMING)) { //checks what room you are in
            if (!hallwayIn.equals(HallwayType.PROGRAMMING_DESIGN_HALLWAY)) { //checks which hallway you're in
                drawRectFromRect(elecDesHallwayPieces[0]); //these numbers represent the pieces of the hallway from bottom to top
                drawRectFromRect(elecDesHallwayPieces[2]);
                drawRectFromRect(elecDesHallwayPieces[3]);
                drawRectFromRect(elecDesHallwayPieces[4]);
            }
        }
        if (roomIn.equals(RoomType.DESIGN)) {
            if (!hallwayIn.equals(HallwayType.PROGRAMMING_DESIGN_HALLWAY)) {
                drawRectFromRect(elecDesHallwayPieces[0]);
                drawRectFromRect(elecDesHallwayPieces[1]);
                drawRectFromRect(elecDesHallwayPieces[2]);
                drawRectFromRect(elecDesHallwayPieces[4]);
                drawRectFromRect(manuHallway);
            }
        }
        if (!hallwayIn.equals(HallwayType.CROSSROAD_AREA)) { //crossroad area when at the intersection
            if (hallwayIn.equals(HallwayType.PROGRAMMING_DESIGN_HALLWAY)) {
                drawRectFromRect(manuHallwayPieces[0]);
                drawRectFromRect(manuHallwayPieces[2]);
            }
            if (hallwayIn.equals(HallwayType.MANUFACTURING_HALLWAY)) {
                drawRectFromRect(elecDesignHallway);
            }
        }

        if (!roomIn.equals(RoomType.PROGRAMMING)) { //checks if NOT in a room
            drawRectFromRect(electricalRoomRect);
        }
        if (!roomIn.equals(RoomType.DESIGN)) {
            drawRectFromRect(designRoomRect);
        }
        if (!roomIn.equals(RoomType.MANUFACTURING)) {
            drawRectFromRect(manuRoomRect);
        }
        if (!roomIn.equals(RoomType.MANUFACTURING) && !hallwayIn.equals(HallwayType.MANUFACTURING_HALLWAY)
                && !hallwayIn.equals(HallwayType.CROSSROAD_AREA)) {
            drawRectFromRect(manuRoomViewRect);
        }
        if (!roomIn.equals(RoomType.PROGRAMMING) && !hallwayIn.equals(HallwayType.PROGRAMMING_DESIGN_HALLWAY)
                && !hallwayIn.equals(HallwayType.CROSSROAD_AREA)) {
            drawRectFromRect(electricalViewRect);
        }
        if (!roomIn.equals(RoomType.DESIGN) && !hallwayIn.equals(HallwayType.PROGRAMMING_DESIGN_HALLWAY)
                && !hallwayIn.equals(HallwayType.CROSSROAD_AREA)) {
            drawRectFromRect(designRoomViewRect);
        }

        if (devWallDraw) {
            for (int i = 0; i < walls.size(); i++) {
                drawRectFromRect(walls.get(i));
            }
        }
        game.shapeRenderer.setColor(Color.FIREBRICK);
        for (int i = 0; i < tempRectangleList.size(); i++) {
            game.shapeRenderer.rect(tempRectangleList.get(i).x, tempRectangleList.get(i).y,
                    tempRectangleList.get(i).width,
                    tempRectangleList.get(i).height);
        }
        game.shapeRenderer.setColor(Color.GOLD);
        game.shapeRenderer.rect(tempRectangle.x, tempRectangle.y, tempRectangle.width, tempRectangle.height); // DEV
                                                                                                              // MODE
        // RECTANGLE
        // DRAWING
        // game.shapeRenderer.triangle(sightlineVerticies[0], sightlineVerticies[1], sightlineVerticies[2],
                // sightlineVerticies[3],
                // sightlineVerticies[4], sightlineVerticies[5]);

        game.shapeRenderer.end();

        testStage.draw();
        timerStage.draw();

        if (timelimit - timeUsed < 0) {
            game.setScreen(new Lose(game));
        }
        // System.out.println(game.mousePositionInWorld(new Vector3(), cam));

        // System.out.println(reactionPass + " "  + reactionAttempt);
    }
    
    /**
     * efficiency method to allow for the writing of a rectangle once as opposed to 4 times when drawing rectangles
     * @param inputRectangle the rectangle that will be drawn
     */
    private void drawRectFromRect(Rectangle inputRectangle) {
        game.shapeRenderer.rect(inputRectangle.getX(), inputRectangle.getY(), inputRectangle.getWidth(),
                inputRectangle.getHeight());
    }

    /**
     * resets the viewport size when resizing the window
     * 
     */
    @Override
    public void resize(int width, int height) {
        cam.viewportWidth = 1000f;
        cam.viewportHeight = 1000f * height / width;
        cam.update();
        testStage.getViewport().update(width, height, true);
    }

    /**
     * takes in all data from keyboard and mouse
     */
    private void inputs() {
        float speed = 120f;
        float delta = Gdx.graphics.getDeltaTime();
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            Vector3 mouseClickedTeleport = new Vector3(game.mousePositionInWorld(cam));
            characterSprite.setPosition(mouseClickedTeleport.x, mouseClickedTeleport.y);
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            dialogue();
            if (reactionTest.contains(game.vector3Converter(game.mousePositionInWorld(cam))) && (!reactionAttempt)) {
                saveSettings();
                game.setScreen(new ReactionTest(game));
            } else if (wireConnection.contains(game.vector3Converter(game.mousePositionInWorld(cam))) && (!connectWireAttempt)) {
                saveSettings();
                game.setScreen(new WireConnection(game));
            } else if (vent.contains(game.vector3Converter(game.mousePositionInWorld(cam)))) {
                if (ventAccess){
                    saveSettings();
                    game.setScreen(new Vent(game));
                } else {
                    label.setText("This vent could probably be opened with a screwdriver");
                }

                
            } else if (reactionTest.contains(game.vector3Converter(game.mousePositionInWorld(cam))) && reactionAttempt && reactionPass) {
                label.setText("A vent that is right outside of manufacturing leads outside, a screw driver might be helpful");
            } else if (wireConnection.contains(game.vector3Converter(game.mousePositionInWorld(cam))) && connectWireAttempt && ventAccess) {
                label.setText("Oh, eletrical team just gave me a screw driver!");
            }

        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (hiding){
                hiding = false;
            }
            characterSprite.translateX(speed * delta); //allows for movement at a constant rate, even if the time between frames is inconsistant

            if (wallLogic()) { //moved the char backwards if hitting any wall
                characterSprite.translateX(-speed * delta);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (hiding){
                hiding = false;
            }
            characterSprite.translateX(-speed * delta);
            if (wallLogic()) {
                characterSprite.translateX(speed * delta);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (hiding){
                hiding = false;
            }
            characterSprite.translateY(speed * delta);

            if (wallLogic()) {
                characterSprite.translateY(-speed * delta);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {

            if (hiding){
                hiding = false;
            }
            characterSprite.translateY(-speed * delta);

            if (wallLogic()) {
                characterSprite.translateY(speed * delta);
            }
        }
        if (!hidingIn.equals(HidingLocation.NOT_HIDING) && hiding == false){//a way to see if the char shouldnt be in hiding, but is still considered to be, allowing for a one off trigger of the text below
            hidingIn = HidingLocation.NOT_HIDING;
            label.setText("You have left hiding");
        }

        // if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {//dev tools for drawing rectangles
        //     if (rectangleCreation) {
        //         System.out.println("turning off rect mode");
        //         rectangleCreation = false;
        //     } else {
        //         System.out.println("turning on rect mode");
        //         rectangleCreation = true;
        //     }
        // }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			game.setScreen(new PauseScreen(game, new Game(game, false)));
            saveSettings();
		}

        if (rectangleCreation) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Vector3 mouseClickedTemp = new Vector3(game.mousePositionInWorld(cam));
                tempRectangle.setX((float) mouseClickedTemp.x);
                tempRectangle.setY((float) mouseClickedTemp.y);
            } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                Vector3 mouseClickedTemp = new Vector3(game.mousePositionInWorld(cam));
                tempRectangle.setSize(mouseClickedTemp.x - tempRectangle.x, mouseClickedTemp.y - tempRectangle.y);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
                tempRectangleList.add(tempRectangle);
                tempRectangle = new Rectangle();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                System.out.println(tempRectangleList);
                // System.out.println(tempRectangle.x + ", " + tempRectangle.y + ", " +
                // tempRectangle.width + ", "
                // + tempRectangle.height);
                for (int i = 0; i < tempRectangleList.size(); i++) {
                    System.out.println("walls.add(new Rectangle(" + tempRectangleList.get(i).x + "f,"
                            + tempRectangleList.get(i).y + "f," + tempRectangleList.get(i).width + "f,"
                            + tempRectangleList.get(i).height + "f));");
                    // walls.add(new Rectangle(100, 100, 100, 200));
                }
            }
        } else {
            tempRectangle = new Rectangle();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.zoom -= 0.02;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.zoom += 0.02;
        }


        charLocation = characterSprite.getBoundingRectangle();
        if (electricalRoomRect.overlaps(charLocation) || electricalViewRect.overlaps(charLocation)) {//checks if the rectangle that represents each room has the character sprite box within it
            roomIn = RoomType.PROGRAMMING;
        } else if (designRoomRect.overlaps(charLocation) || designRoomViewRect.overlaps(charLocation)) {
            roomIn = RoomType.DESIGN;
        } else if (manuRoomRect.overlaps(charLocation) || manuRoomViewRect.overlaps(charLocation)) {
            roomIn = RoomType.MANUFACTURING;
        } else {
            roomIn = RoomType.HALLWAY;
        }

        if (manuHallwayPieces[1].overlaps(charLocation)) {
            hallwayIn = HallwayType.CROSSROAD_AREA;
        } else if (manuHallway.overlaps(charLocation)) {
            hallwayIn = HallwayType.MANUFACTURING_HALLWAY;
        }

        else if (elecDesignHallway.overlaps(charLocation)) {
            hallwayIn = HallwayType.PROGRAMMING_DESIGN_HALLWAY;
        } else {
            hallwayIn = HallwayType.ROOM;
        }

        cam.zoom = MathUtils.clamp(cam.zoom, 0.5f, 2f);

        
    }

    private void dialogue() {
        if (hiding){
            hiding = false;
        }
        interactMouseLocation = game.mousePositionInWorld(cam);
        mouseLoc = new Vector2(interactMouseLocation.x, interactMouseLocation.y);

        if (roomIn.equals(RoomType.PROGRAMMING)){
        if (wireRect.contains(mouseLoc)) {//checks if the rectangle representing an object contains the mouse when the click was made
            label.setText("A HUGE mess of wires within a box, looks like electrical hasn't made much progress");
        } 
        else if (wireSpoolrect.contains(mouseLoc)) {
            label.setText("The singular red spool of wire");
        }  
        else if (elecTeacherDesk.contains(mouseLoc)) {
            label.setText("It's just a teacher's desk, probably filled with papers of trip logistics and how much funding we need");
        } 
        else if (elecTeacherChair.contains(mouseLoc)) {
            label.setText("Just a chair, nothing special about it");
        } 
        else if (elecComputerFront1.contains(mouseLoc)) {
            label.setText("A computer on it's lock screen");
        } 
        else if (elecComputerFront2.contains(mouseLoc)) {
            label.setText("A computer, but the lock screen has a famous actor's face on them");
        } 
        else if (elecComputerFront3.contains(mouseLoc)) {
            label.setText("A computer, it has someones Tetris high score on it, I guess they weren't working too hard");
        }
        else if (elecComputerFront4.contains(mouseLoc)) {
            label.setText("A computer, it's a huge list of tasks electrical needs to do, looks like they've been struggling with wiring the robot for a while");
        } 
        else if (elecComputerFront5.contains(mouseLoc)) {
            label.setText("A computer on it's lock screen");
        } 
        else if (elecComputerSide1.contains(mouseLoc)) {
            label.setText("A computer with a list of tools within the room, oh, a screwdriver could be useful");
        }
        else if (elecComputerSide2.contains(mouseLoc)) {
            label.setText("A computer on a google sheet of motors to buy");
        }
        else if (elecComputerSide3.contains(mouseLoc)) {
            label.setText("A computer on it's home screen");
        }
        else if (elecComputerSide4.contains(mouseLoc)) {
            label.setText("A computer with instructions on taking apart and rebuilding a motor");
        }
        else if (didIClickOn(elecMember)){
            if (!connectWireAttempt){
                label.setText("hey, could you help us with wiring this? Maybe you'll have better luck than us");
            }else{
                label.setText("Wow, thanks so much, here's a screwdriver for your \"escape plans\", also feel free to use our closet");
            }
            
        }
        else {
            label.setText(null);
        }
        }

        if (roomIn.equals(RoomType.DESIGN)){
            
            if (desComputersFront.contains(mouseLoc)){
                label.setText("A bunch of computers in the front of the room, all open to different pages of inventor");
            }
            else if (desComputersLeft.contains(mouseLoc)){
                label.setText("Many computers all referencing different measurements and restructions from the challenge");
            }
            else if (desComputersBack.contains(mouseLoc)){
                label.setText("The whole back row of computers, all making lovely renders of our robot for the judges manual");
            }
            else if (desComputerMiddleSpec.contains(mouseLoc)){
                label.setText("This computer has a google doc named \"Mr. White Lore\" and its... 192 PAGES LONG???");
            }
            else if (desComputerBackSpec.contains(mouseLoc)){
                label.setText("Why is there an inventor part open using metric?");
            }
            else if (didIClickOn(desComputerRightSpec)){
                label.setText("A pdf of blueprints is open on the computer. Why do all of these blueprints have a number of holes in each part on them? I wonder what poor intern was assigned that...");
            }
            else if (didIClickOn(desComputerRight2)){
                label.setText("This computer is loading something");
                if (reactionPass){
                    label.setText("This computer was loading before, but now it's showing blueprints of the school. Theres a vent outside of manufacturing that leads outside??!?!");
                }
            }
            else if(didIClickOn(desComputersMiddleFront)){
                label.setText("Computers on their home screen");
            }
            else if (didIClickOn(desComputerMiddleBack)){
                label.setText("Theres a vent somewhere? huh, that'd be useful to escape");
            }
            else if (didIClickOn(desComputerTeacher)){
                label.setText("The teacher's computer, definitely shouldn't touch that");
            }
            else{
                label.setText(null);
            }
        }

        if (roomIn.equals(RoomType.MANUFACTURING)){
            if (didIClickOn(weldingArea)){
                label.setText("Lots of sound noise and flashing lights from the welding area, I best not set foot in there");
            }
            else if (didIClickOn(longTable)){
                label.setText("A long table. Why is there nothing on it?");
            }
            else if (didIClickOn(picture)){
                label.setText("A picture of the people that started the team, people say that they see their eyes move sometimes");
            }
            else if (didIClickOn(teacherArea)){
                label.setText("This is where THE TEACHER sits, many students are found waiting, forming a forever spanning line to ask questions that take forever to be answered. There are also hair ties and a bin of hoodie strings");
            }
            else if (didIClickOn(bumpers)){
                label.setText("The bumpers that go on the robot, they are so high that students regularly ask for help to reach them");
            }
            else if (didIClickOn(lathes)){
                label.setText("These pieces of heavy machinery is what makes the tubes that go on the robot, as well as most other round pieces");
            }
            else if (didIClickOn(footshear)){
                label.setText("The footshear is what cuts huge sheets of metal, if the peice is too thick, multiple people will jump on the standing area to get it to cut");
            }
            else{
                label.setText(null);
            }
        }

        if (roomIn.equals(RoomType.HALLWAY)){
                label.setText(null);
        }
        if (elecCloset.contains(mouseLoc)) {
            label.setText("Electrical's closet, you could probably hide in there, but you prob shouldnt walk in without permission");
            if (ventAccess){
                label.setText("You are now hidden from sight after walking into the electrical closet \n [IF YOU MOVE OR CLICK, YOU WILL BE VISIBLE]");
                characterSprite.setX(765.316f);
                characterSprite.setY(1408.894f);
                hiding = true;
                hidingIn = HidingLocation.PROGRAMMING;
            }
            
        }
        
        else if (designCloset.contains(mouseLoc)){
            label.setText("You are now hidden from sight after cramming yourself into the tiny design room closet \n [IF YOU MOVE OR CLICK, YOU WILL BE VISIBLE]");
            characterSprite.setX(593.05524f);
            characterSprite.setY(2340.437f);
            hiding = true;
            hidingIn = HidingLocation.DESIGN;
        }
        else if (manuCloset.contains(mouseLoc)){
            label.setText("You are now hidden from sight after making yourself confortable with the slew of parts within the manufacturing storage closets \n [IF YOU MOVE OR CLICK, YOU WILL BE VISIBLE]");
            characterSprite.setX(1785.4348f);
            characterSprite.setY(3864.5461f);
            hiding = true;
            hidingIn = HidingLocation.MANUFACTURING;
        }
        // else if (INSERT_RECT_HERE.contains(mouseLoc)){
        // label.setText("");
        // }
        

        if (wireConnectionrect.contains(mouseLoc)) {
            pause();
        }
    }

    /**
     * Called to see if the sprite is intersecting with a wall
     * 
     * @return if the player character is hitting a wall
     */
    private boolean wallLogic() {
        Rectangle rectangleHitbox = new Rectangle(characterSprite.getBoundingRectangle());
        for (int i = 0; i < walls.size(); i++) {
            if (rectangleHitbox.overlaps(walls.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * easier way to see if the mouse clicked within a rectangle
     * @param thing the rectangle representing the thing that you may have clicked on
     * @return if you did or didnt click that thing
     */
    private boolean didIClickOn(Rectangle thing){
        if (thing.contains(mouseLoc)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Called to check if the player hitbox intersects with the pathsprite's
     * sightline
     * 
     */
    private void sightlineLogic() {
        if (teacherSprite.isWaiting()) {
            if (wasSeen){
                label.setText("Looks like the teacher is taking a break");
                wasSeen = false;
                secondTimer = 0;
            }
            return;
        }

        if (subteamChosen.toString().equals(roomIn.toString())){
            if (wasSeen){
                System.out.println("escaped by being in your room");
                label.setText("Whew, You're where you're supposed to be, can't be in trouble for that");
                wasSeen = false;
                secondTimer = 0;
            }
            return;
            
        } 
        
        float sightLength = 200;
        float sightWidth = 100;
        float spriteWidth = teacherSprite.getWidth() / 2;
        float spriteAngle = teacherSprite.getRotation() * MathUtils.degRad;
        float xLoc = teacherSprite.getX();
        float yLoc = teacherSprite.getY();
        float yInc1 = spriteWidth * (float) Math.sin(spriteAngle);
        float xInc1 = spriteWidth * (float) Math.cos(spriteAngle);
        float yInc2 = (sightLength + spriteWidth) * (float) Math.sin(spriteAngle)
                + (sightWidth * (float) Math.cos(spriteAngle));
        float xInc2 = (sightLength + spriteWidth) * (float) Math.cos(spriteAngle)
                - (sightWidth * (float) Math.sin(spriteAngle));
        float yInc3 = (sightLength + spriteWidth) * (float) Math.sin(spriteAngle)
                - (sightWidth * (float) Math.cos(spriteAngle));
        float xInc3 = (sightLength + spriteWidth) * (float) Math.cos(spriteAngle)
                + (sightWidth * (float) Math.sin(spriteAngle));
        sightlineVerticies = new float[] { xLoc + xInc1, yLoc + yInc1, xLoc + xInc2, yLoc + yInc2, xLoc + xInc3,
                yLoc + yInc3 };
        sightlineTriangle.setVertices(sightlineVerticies);
        Polygon hitBoxPolygon = new Polygon();
        Rectangle hitBoxRectangle = new Rectangle(characterSprite.getBoundingRectangle());
        hitBoxPolygon.setVertices(new float[] { hitBoxRectangle.x, hitBoxRectangle.y, hitBoxRectangle.x,
                hitBoxRectangle.y + hitBoxRectangle.height, hitBoxRectangle.x + hitBoxRectangle.width,
                hitBoxRectangle.y + hitBoxRectangle.height, hitBoxRectangle.x + hitBoxRectangle.width,
                hitBoxRectangle.y });

        if (Intersector.overlapConvexPolygons(sightlineTriangle, hitBoxPolygon)){
            if (teacherSprite.getBoundingRectangle().overlaps(hitBoxRectangle)){
                teacherSprite.setSpeed(0);
                secondTimer += Gdx.graphics.getDeltaTime()*8;
            } else{
                teacherSprite.setSpeed(120);
            }
            label.setText("WOAH, THE TEACHER CAN SEE YOU RIGHT NOW, RUN! [IF YOU ARE SEEN FOR TOO LONG, YOU WILL BE CAUGHT]");
            wasSeen = true;
            secondTimer+=Gdx.graphics.getDeltaTime();
            // System.out.println(secondTimer);
            if (secondTimer >= 5f) {
                game.setScreen(new Lose(game));
            }           
        }else{
            teacherSprite.setSpeed(250f);
            if (wasSeen){
                label.setText("Whew, just barely managed to escape");
                wasSeen = false;
                secondTimer = 0;
            }
        }

    }

    // creating a path for the set path sprite
    private Array<Vector2> getSetPath() {
        Array<Vector2> path = new Array<Vector2>();
        path.add(new Vector2(1850, 3785));
        path.add(new Vector2(2150, 3785));
        path.add(new Vector2(2150, 3710));
        path.add(new Vector2(2425, 3710));
        path.add(new Vector2(2425, 3325));
        path.add(new Vector2(2105, 3325));
        path.add(new Vector2(2105, 2910));
        path.add(new Vector2(1035, 2910));
        path.add(new Vector2(1035, 2295));
        path.add(new Vector2(715, 2295));
        path.add(new Vector2(715, 2265));
        path.add(new Vector2(145, 2265));
        path.add(new Vector2(145, 2515));
        path.add(new Vector2(715, 2515));
        path.add(new Vector2(715, 2295));
        path.add(new Vector2(1035, 2295));
        path.add(new Vector2(1035, 1110));
        path.add(new Vector2(790, 1110));
        path.add(new Vector2(790, 1035));
        path.add(new Vector2(155, 1035));
        path.add(new Vector2(155, 1265));
        path.add(new Vector2(790, 1265));
        path.add(new Vector2(790, 1110));
        path.add(new Vector2(1035, 1110));
        path.add(new Vector2(1035, 2910));
        path.add(new Vector2(2105, 2910));
        path.add(new Vector2(2105, 3335));
        path.add(new Vector2(1850, 3335));
        path.add(new Vector2(1850, 3215));
        path.add(new Vector2(1851, 3215));
        return path;
    }

    /**
     * saves settings when ending game or starting a minigame
     */
    private void saveSettings() {
        try{
            file = Gdx.files.local("SavedSetting.txt");
            file.writeString(characterSprite.getX() + "\n" + characterSprite.getY() + "\n" + teacherSprite.getX() + "\n" + teacherSprite.getY() + "\n" + teacherSprite.getWaypoint() + "\n" + timeUsed + "\n" + reactionPass + "\n" + ventAccess + "\n" + reactionAttempt + "\n" + connectWireAttempt + "\n" + hiding + "\n" + subteamChosen, false);           
        } catch(Exception e) {
            e.printStackTrace();
            game.setScreen(new ErrorScreen(game));
            return;
        }   
    }

}