package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    //Test Commit & Push - To Make sure git is connected to submit the REPO link in the moodle.
    //Added Assets to the drwable folder

    // Android debug variables
    final static String TAG="DINO-RAINBOWS";

    // screen size
    int screenHeight;
    int screenWidth;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    //
    Player player;
    Item poop;
    Item rainbow;
    Item candy;

    int lives = 3;
    int score = 0;


    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------

    // represent the TOP LEFT CORNER OF THE GRAPHIC

    // ----------------------------
    // ## GAME STATS
    // ----------------------------


    public GameEngine(Context context, int w, int h) {
        super(context);

        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;

        this.printScreenInfo();

        this.player = new Player(getContext(), 1500, 500);
        this.poop = new Item(getContext(), 100, 200);
        this.rainbow = new Item(getContext(), 100, 400);
        this.candy = new Item(getContext(), 100, 600);
    }



    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the right side of screen
    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location


    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    public void updatePositions() {
//

        // MAKE ENEMY MOVE
        // - enemy moves right forever
        // - when enemy touches LEFT wall, respawn on RIGHT SIDE
        this.poop.setxPosition(this.poop.getxPosition()+25);

        // MOVE THE HITBOX (recalcluate the position of the hitbox)
        this.poop.updateHitbox();

        if (this.poop.getxPosition() >= 1500) {
            // restart the enemy in the starting position
            this.poop.setxPosition(20);
            this.poop.setyPosition(200);
            this.poop.updateHitbox();
        }

        this.rainbow.setxPosition(this.rainbow.getxPosition()+30);
        // MOVE THE HITBOX (recalcluate the position of the hitbox)
        this.rainbow.updateHitbox();

        if (this.rainbow.getxPosition() >= 1500) {
            // restart the enemy in the starting position
            this.rainbow.setxPosition(20);
            this.rainbow.setyPosition(400);
            this.rainbow.updateHitbox();
        }

        this.candy.setxPosition(this.candy.getxPosition()+20);
        // MOVE THE HITBOX (recalcluate the position of the hitbox)
        this.candy.updateHitbox();

        if (this.candy.getxPosition() >= 1500) {
            // restart the enemy in the starting position
            this.candy.setxPosition(20);
            this.candy.setyPosition(600);
            this.candy.updateHitbox();
        }


        // @TODO:  Check collisions between poop and player
        if (this.player.getHitbox().intersect(this.poop.getHitbox()) == true) {
            // the enemy and player are colliding
            Log.d(TAG, "++++++ENEMY AND PLAYER COLLIDING!");

            // @TODO: What do you want to do next?

            // RESTART THE PLAYER IN ORIGINAL POSITION
            // -------
            // 1. Restart the player
            // 2. Restart the player's hitbox
            this.poop.setxPosition(20);
            this.poop.setyPosition(200);
            this.poop.updateHitbox();

            // decrease the lives
            lives = lives - 1;

        }

        // @TODO:  Check collisions between rainbow and player
        if (this.player.getHitbox().intersect(this.rainbow.getHitbox()) == true) {
            // the enemy and player are colliding
            Log.d(TAG, "++++++RAINBOW AND PLAYER COLLIDING!");
            this.rainbow.setxPosition(20);
            this.rainbow.setyPosition(400);
            this.rainbow.updateHitbox();

            // @TODO: What do you want to do next?

            // Add Score
            score = score + 1;

        }

        // @TODO:  Check collisions between candy and player
        if (this.player.getHitbox().intersect(this.candy.getHitbox()) == true) {
            // the enemy and player are colliding
            Log.d(TAG, "++++++CANDY AND PLAYER COLLIDING!");
            this.candy.setxPosition(20);
            this.candy.setyPosition(600);
            this.candy.updateHitbox();

            // @TODO: What do you want to do next?

            // Add Score
            score = score + 1;

        }
    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);

            paintbrush.setColor(Color.BLACK);
            this.canvas.drawRect(550,1500, 950, 1550, paintbrush);
            paintbrush.setColor(Color.BLACK);

            // DRAW THE PLAYER HITBOX
            // ------------------------
            // 1. change the paintbrush settings so we can see the hitbox
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            // draw player graphic on screen
            canvas.drawBitmap(player.getImage(), player.getxPosition(), player.getyPosition(), paintbrush);
            // draw the player's hitbox
            canvas.drawRect(player.getHitbox(), paintbrush);

            // draw the enemy graphic on the screen
            canvas.drawBitmap(poop.getImage(), poop.getxPosition(), poop.getyPosition(), paintbrush);
            // 2. draw the enemy's hitbox
            canvas.drawRect(poop.getHitbox(), paintbrush);

            // draw the rainbow graphic on the screen
            canvas.drawBitmap(rainbow.getImage(), rainbow.getxPosition(), rainbow.getyPosition(), paintbrush);
            // 2. draw the rainbows hitbox
            canvas.drawRect(rainbow.getHitbox(), paintbrush);

            // draw the candy graphic on the screen
            canvas.drawBitmap(candy.getImage(), candy.getxPosition(), candy.getyPosition(), paintbrush);
            // 2. draw the candy hitbox
            canvas.drawRect(candy.getHitbox(), paintbrush);

            paintbrush.setColor(Color.BLUE);
            paintbrush.setTextSize(60);
            canvas.drawText("Lives: " + lives,
                    1100,
                    800,
                    paintbrush
            );
            canvas.drawText("Score: " + score,
                    800,
                    500,
                    paintbrush
            );

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(120);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------


    String fingerAction = "";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?

        if (userAction == MotionEvent.ACTION_DOWN) {
            fingerAction = "mousedown";
            this.player.setyPosition(this.player.getyPosition()+25);
        }
        else if (userAction == MotionEvent.ACTION_UP) {
            fingerAction = "mouseup";
            this.player.setyPosition(this.player.getyPosition()-25);
        }
        return true;
    }
}
