package com.example.android_game;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    public static final String LONGITUDE = "LONGITUDE";
    public static final String LATITUDE = "LATITUDE";
    final static int ANIM_DURATION = 4400;
    public static String GAME_TYPE = "GAME_TYPE";
    public static String PLAYER_NAME = "PLAYER_NAME";
    public static String SPEED = "SPEED";
    private Player player;
    private int lifeState;
    private ImageView[] panel_IMG_life;
    private ImageView[][] boardMatrix;
    private int[][] values;
    private ImageView panel_IMG_left;
    private ImageView panel_IMG_right;
    private ImageView panel_IMG_background;
    private LinearLayout panel_Layout_endGameButtons;
    private TextView panel_LBL_score;
    private ImageView panel_IMG_newGame;
    private ImageView panel_IMG_finishGame;
    private ImageView splash_IMG_logo;
    private TextView panel_lbl_gameOver;
    private int score;
    private int playerPos;
    private int gameSpeed;
    private TextView panel_LBL_distance;
    private int distance;
    private float mobileRotation;
    private LinearLayout panel_game;
    private MovementSensors sensors;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ohado", "initSensor: onCreate game ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initGameView();

        if (player.getGameType() == 1) {
            initSensors();

        }
        initLiseners();
        addBackground(); // true for game background . false for game over


    }

    private void initSensors() {
        delateButtons();
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors = new MovementSensors()
                .setActivity(this)
                .setSensorManager(sm)
                .setAccSensor(sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (player.getGameType() == 1)
            sensors.registerLisener(accSensorEventListener, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void delateButtons() {
        panel_IMG_left.setVisibility(View.GONE);
        panel_IMG_right.setVisibility(View.GONE);
    }

    private SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            mobileRotation = event.values[0];
            updatePlayerPos(getDiverPos(mobileRotation));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

    private int getDiverPos(float x) {
//
//        if (x >= 0.8 ) {
//            if(playerPos > 0)
//                return playerPos - 1 ;
//            else
//                return playerPos ;
//        }
//
//        if (x < 0.8 && x >= -0.8) {
//           return playerPos ;
//        }
//
//        if (x <= -0.8) {
//            if(playerPos < boardMatrix[0].length - 1 )
//                return playerPos + 1 ;
//            else
//                return playerPos;
//
//        }


        if (x > 2.5) {
            return 0;
        }
        if (x <= 2.5 && x >= 1) {
            return 1;
        }
        if (x < 1 && x >= -1) {
            return 2;
        }

        if (x < -1 && x >= -2.5) {
            return 3;
        }
        if (x < -2.5) {
            return 4;
        }

        return -1; // will never read this line ..

    }


    private void addBackground() {

        Glide
                .with(this)
                .load(R.drawable.sky)
                .centerCrop()
                .into(panel_IMG_background);
    }

    private void initLiseners() {
        initLefButtonLisener();
        initRightButtonLisener();

    }

    private void initRightButtonLisener() {
        panel_IMG_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("ppt", "mat: " + boardMatrix[0].length + "  " + boardMatrix.length);
                if (playerPos + 1 < boardMatrix[0].length && lifeState >= 0) {
                    updatePlayerPos(playerPos + 1);
                }

            }
        });
    }


    private void updatePlayerPos(int newPos) {

        values[0][playerPos] = 0;
        boardMatrix[0][playerPos].setVisibility(View.INVISIBLE);
        playerPos = newPos;
        boardMatrix[0][playerPos].setVisibility(View.VISIBLE);
        boardMatrix[0][playerPos].setImageResource(R.drawable.diver);
        handleScore(values[0][playerPos]);
        values[0][playerPos] = 3;
    }

    private void initLefButtonLisener() {

        panel_IMG_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (playerPos - 1 >= 0 && lifeState >= 0) {
                    updatePlayerPos(playerPos - 1);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (lifeState >= 0)
            startTicker();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTicker();
    }

    private void stopTicker() {
        timer.cancel();
    }

    private void startTicker() {
        timer = new Timer();
        timer.schedule(createTimerTask(),  0, gameSpeed);


    }

    private void initGameView() {
        player = getParameterFromBundle();
        Log.d("ohado", "init : ");
        panel_IMG_life = new ImageView[]{
                findViewById(R.id.panel_IMG_life_0),
                findViewById(R.id.panel_IMG_life_1),
                findViewById(R.id.panel_IMG_life_2),
        };


        boardMatrix = new ImageView[][]{
                {findViewById(R.id.panel_IMG_00), findViewById(R.id.panel_IMG_01), findViewById(R.id.panel_IMG_02), findViewById(R.id.panel_IMG_03), findViewById(R.id.panel_IMG_04)},
                {findViewById(R.id.panel_IMG_10), findViewById(R.id.panel_IMG_11), findViewById(R.id.panel_IMG_12), findViewById(R.id.panel_IMG_13), findViewById(R.id.panel_IMG_14)},
                {findViewById(R.id.panel_IMG_20), findViewById(R.id.panel_IMG_21), findViewById(R.id.panel_IMG_22), findViewById(R.id.panel_IMG_23), findViewById(R.id.panel_IMG_24)},
                {findViewById(R.id.panel_IMG_30), findViewById(R.id.panel_IMG_31), findViewById(R.id.panel_IMG_32), findViewById(R.id.panel_IMG_33), findViewById(R.id.panel_IMG_34)},
                {findViewById(R.id.panel_IMG_40), findViewById(R.id.panel_IMG_41), findViewById(R.id.panel_IMG_42), findViewById(R.id.panel_IMG_43), findViewById(R.id.panel_IMG_44)},
                {findViewById(R.id.panel_IMG_50), findViewById(R.id.panel_IMG_51), findViewById(R.id.panel_IMG_52), findViewById(R.id.panel_IMG_53), findViewById(R.id.panel_IMG_54)},
                {findViewById(R.id.panel_IMG_60), findViewById(R.id.panel_IMG_61), findViewById(R.id.panel_IMG_62), findViewById(R.id.panel_IMG_63), findViewById(R.id.panel_IMG_64)},
                {findViewById(R.id.panel_IMG_70), findViewById(R.id.panel_IMG_71), findViewById(R.id.panel_IMG_72), findViewById(R.id.panel_IMG_73), findViewById(R.id.panel_IMG_74)},
                {findViewById(R.id.panel_IMG_80), findViewById(R.id.panel_IMG_81), findViewById(R.id.panel_IMG_82), findViewById(R.id.panel_IMG_83), findViewById(R.id.panel_IMG_84)},
        };

        values = new int[boardMatrix.length][boardMatrix[0].length];
        initValusMat();
        score = 0;
        gameSpeed = getGameSpeed(player.getSpeed());
        timer = new Timer();
        splash_IMG_logo = findViewById(R.id.splash_IMG_logo);
        panel_LBL_score = findViewById(R.id.panel_LBL_score);
        panel_IMG_left = findViewById(R.id.panel_IMG_left);
        panel_IMG_right = findViewById(R.id.panel_IMG_right);
        panel_Layout_endGameButtons = findViewById(R.id.panel_Layout_endGameButtons);
        panel_IMG_background = findViewById(R.id.panel_IMG_background);
        panel_IMG_newGame = findViewById(R.id.panel_IMG_newGame);
        panel_IMG_finishGame = findViewById(R.id.panel_IMG_finishGame);
        panel_LBL_distance = findViewById(R.id.panel_LBL_distance);
        panel_lbl_gameOver = findViewById(R.id.panel_lbl_gameOver);
        panel_game = findViewById(R.id.panel_game);
        lifeState = 2;
        playerPos = 1;
        distance = 0;
        boardMatrix[0][1].setVisibility(View.VISIBLE);
        boardMatrix[0][1].setImageResource(R.drawable.diver);
        panel_LBL_score.setText("" + score);


    }

    private int getGameSpeed(int speed) {
        if (speed == 0) {

            return 600;
        } else if (speed == 1) {
            return 380;
        } else {
            return 230;
        }
    }

    private Player getParameterFromBundle() {
        Log.d("ohado", "getParameterFromBundle: ");
        Bundle extras = getIntent().getBundleExtra("Bundle");
        Player player =  new Player()
                .setName(extras.getString(GameActivity.PLAYER_NAME))
                .setGameType(extras.getInt(GameActivity.GAME_TYPE))
                .setSpeed(extras.getInt(GameActivity.SPEED))
                .setLon(extras.getDouble(GameActivity.LONGITUDE))
                .setLat(extras.getDouble(GameActivity.LATITUDE));

        Log.d("ohadsa", "onSuccess: " + player.getLat()   + "  " + player.getLon() );
        return player;
    }

    private void initValusMat() {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] = 0;
            }
        }
    }

    public TimerTask createTimerTask() {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateState();
                    }
                });
            }
        };

        return timerTask;
    }

    public void updateState() {
        panel_LBL_distance.setText(" " + (++distance) + " m ");
        for (int i = 0; i < values.length - 1; i++) { //
            for (int j = 0; j < values[0].length; j++) {
                values[i][j] = values[i + 1][j];
            }
        }

        randomFirstRow();
        sync_ImgMat_valsMat();
        handleScore(values[0][playerPos]);
        if (lifeState >= 0) {
            boardMatrix[0][playerPos].setVisibility(View.VISIBLE);
            boardMatrix[0][playerPos].setImageResource(R.drawable.diver);
        }

    }


    private void handleScore(int i) {
        if (i == 1) {
            panel_IMG_life[lifeState].setVisibility(View.GONE);
            lifeState--;
            if (lifeState < 0)
                endGame();
            MediaPlayer song = MediaPlayer.create(this, R.raw.crush_sound);
            song.start();
            vibrate_init();
        } else if (i == 2) {
            MediaPlayer song = MediaPlayer.create(this, R.raw.collect_sound);
            song.start();
            addScoreBySpeed();

        }

    }

    private void addScoreBySpeed() {
        int speed = player.getSpeed();
        if (speed == 0)
            score += 10;
        else if (speed == 1)
            score += 40;
        else
            score += 100;

        panel_LBL_score.setText("" + score);
    }

    private void endGame() {
        stopTicker();
        panel_game.setVisibility(View.GONE);
        panel_lbl_gameOver.setVisibility(View.VISIBLE);
        player.setScore(score);
        boolean enteredTopTen = player.saveRecordToFile();
        showViewSlideDown(splash_IMG_logo, enteredTopTen);

    }

    public void showViewSlideDown(final View v, boolean enteredTopTen) {
        v.setVisibility(View.VISIBLE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        v.setY(-height / 2);
        v.setScaleY(0.0f);
        v.setScaleX(0.0f);
        v.animate()
                .scaleY(1.0f)
                .scaleX(1.0f)
                .translationY(0)
                .setDuration(GameActivity.ANIM_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (enteredTopTen) {
                            panel_lbl_gameOver.setText("congratulation you entered top10 !\n\n\ngame over");
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationDone();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void animationDone() {
        openRecordActivity();
    }

    private void openRecordActivity() {

        Intent intent = new Intent(this, RecordsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle bundle = new Bundle();
        bundle.putBoolean(RecordsActivity.ENTRY_EXIST, false);
        intent.putExtra("Bundle", bundle);
        startActivity(intent);
        finish();
    }


    private void sync_ImgMat_valsMat() {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                ImageView tmp = boardMatrix[i][j];
                if (values[i][j] == 0) {
                    tmp.setVisibility(View.INVISIBLE);
                } else if (values[i][j] == 1) {
                    tmp.setVisibility(View.VISIBLE);
                    tmp.setImageResource(R.drawable.cloud);
                } else if (values[i][j] == 2) {
                    tmp.setVisibility(View.VISIBLE);
                    tmp.setImageResource(R.drawable.parachute);
                } else if (values[i][j] == 3) {
                    tmp.setImageResource(R.drawable.diver);
                }
            }
        }
    }

    public void randomFirstRow() {
        for (int i = 0; i < values[0].length; i++) {
            values[values.length - 1][i] = 0;
        }
        int randomPlace = (int) (Math.random() * values[0].length);
        int randomValue = (int) ((Math.random() * 3) - 0.5);
        values[values.length - 1][randomPlace] = randomValue;
    }

    public void vibrate_init() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

}
