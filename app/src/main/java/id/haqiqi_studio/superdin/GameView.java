package id.haqiqi_studio.superdin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import tyrantgit.explosionfield.ExplosionField;

/**
 * Created by sara on 2018/01/31.
 */

public class GameView extends View {
    GameSetting setting = new GameSetting();

    // Canvas
    private int canvasWidth;
    private int canvasHeight;

    MediaPlayer hitBlueBall;
    MediaPlayer hitBlackBall;
    MediaPlayer hitSnow;
    MediaPlayer getAward;

    // Bird
    //private Bitmap bird;
    private Bitmap bird[] = new Bitmap[2];
    private int birdX = 10;
    private int birdY;
    private int birdSpeed;

    // Blue Ball
    private int blueX;
    private int blueY;
    private int blueSpeed = 12;
    private Paint bluePaint = new Paint();

    // Black Ball
    private int blackX;
    private int blackY;
    private int blackSpeed = 20;
    private Bitmap blackPaint[] = new Bitmap[1];

    // Snow
    private int snowX;
    private int snowY;
    private int snowSpeed = 21;
    private Bitmap snow;


    // Soulmate
    private int ucilX;
    private int ucilY;
    private int ucilSpeed = 2;
    private Bitmap ucil;

    // Background
    private Bitmap bgImage[] = new Bitmap[3];

    //Flower
    private int flowerX;
    private int flowerY;
    private int flowerSpeed = 5;
    private Bitmap flower[] = new Bitmap[2];

    // Score
    private Paint scorePaint = new Paint();
    private int score;

    // Level
    private Paint levelPaint = new Paint();

    // Game State
    private Paint state = new Paint();

    // Life
    private Bitmap life[] = new Bitmap[2];
    private int life_count;

    // Status Check
    private boolean touch_flg = false;
    private boolean is_slow = false;

    private Rect rect;

    private int dWidth, dHeight;

    ExplosionField explos;

    Boolean is_show = true;


    //region GameView
    public GameView(Context context) {
        super(context);

        bgImage[0] = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bgImage[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bg_level2);
        bgImage[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bg_level3);

        ucil = BitmapFactory.decodeResource(getResources(), R.drawable.superucil);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rect = new Rect(0, 0, dWidth, dHeight);

        bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.superdin);
        bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.superdin1);

        flower[0] = BitmapFactory.decodeResource(getResources(), R.drawable.flower);
        flower[1] = BitmapFactory.decodeResource(getResources(), R.drawable.flower);

        snow = BitmapFactory.decodeResource(getResources(), R.drawable.snow);

        bluePaint.setColor(Color.BLUE);
        bluePaint.setAntiAlias(false);

//        blackPaint.setColor(Color.BLACK);
//        blackPaint.setAntiAlias(false);

        blackPaint[0] = BitmapFactory.decodeResource(getResources(), R.drawable.peluru);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(32);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        state.setColor(Color.BLACK);
        state.setTextSize(32);
        state.setTypeface(Typeface.DEFAULT_BOLD);
        state.setAntiAlias(true);

        levelPaint.setColor(Color.DKGRAY);
        levelPaint.setTextSize(32);
        levelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        levelPaint.setTextAlign(Paint.Align.CENTER);
        levelPaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_g);

        hitBlackBall = MediaPlayer.create(context, R.raw.brukk);
        hitBlueBall = MediaPlayer.create(context, R.raw.point);
        hitSnow = MediaPlayer.create(context, R.raw.hit_snow);
        getAward = MediaPlayer.create(context, R.raw.award);

        // First position.
        birdY = 500;
        score = 0;
        life_count = 3;
    }
    //endregion

    @Override
    protected void onDraw(Canvas canvas) {

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        changeState(canvas, rect);

        //region Bird Speed
        int minBirdY = bird[0].getHeight() - 30;
        int maxBirdY = canvasHeight - bird[0].getHeight();

        birdY += birdSpeed;
        if (birdY < minBirdY) birdY = minBirdY;
        if (birdY > maxBirdY) birdY = maxBirdY;
        birdSpeed += 2;
        //endregion

        //region Touch Flag
        if (touch_flg) {
            // Flap wings.
            canvas.drawBitmap(bird[1], birdX, birdY, null);
            touch_flg = false;
        } else {
            canvas.drawBitmap(bird[0], birdX, birdY, null);
        }
        //endregion

        //region Snow
        if (!is_slow) {
            showSnow(canvas, snowX, snowY);
        }

        if (hitCheck(snowX, snowY)) {
            is_slow = true;

            snowX = -100;
            hitSnow.start();

            blackSpeed = 4;
            blueSpeed = 12;

        }
        if (score % 110 == 0) {
            is_slow = false;
            blackSpeed = 20;
            blueSpeed = 12;
        }


        //endregion

        //region Blue Ball
        blueX -= blueSpeed;
        if (hitCheck(blueX, blueY)) {
            hitBlueBall.start();
            score += 10;
            checkPoint();
            blueX = -100;
        }
        if (blueX < 0) {
            blueX = canvasWidth + 20;
            blueY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
        }
        //canvas.drawCircle(blueX, blueY, 10, bluePaint);lu
        showFlower(canvas, blueX, blueY);
        //endregion

        //region Black Ball
        blackX -= blackSpeed;
        if (hitCheck(blackX, blackY)) {
            hitBlackBall.start();
            blackX = -100;
            life_count--;
        }

        if (blackX < 0) {
            blackX = canvasWidth + 200;
            blackY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
        }
        canvas.drawBitmap(blackPaint[0], blackX, blackY, null);
        //endregion

        showSnow(canvas, maxBirdY, minBirdY);

        showUcil(canvas, maxBirdY, minBirdY);

        checkLife(canvas, life_count);
        // Score
        canvas.drawText("Score : " + score, 15, 60, scorePaint);

        if (if_over()) {
            canvas.drawText("Game Over", canvasWidth / 2 - 30, canvasHeight / 2, state);
            birdSpeed = 0;
            blackSpeed = 0;
            blueSpeed = 0;
            flowerSpeed = 0;
            birdY = -100;

            setting.setBestScore(getContext(), "score", score);
        }

        // Life
        //region Life
        for (int i = 0; i < 3; i++) {
            int x = (int) (560 + life[0].getWidth() * 1.0 * i);
            int y = 30;

            if (i < life_count) {
                canvas.drawBitmap(life[0], x, y, null);
            } else {
                canvas.drawBitmap(life[1], x, y, null);
            }
        }
        //endregion
    }

    void checkPoint() {
        if (score % 100 == 0) {
            getAward.start();
        }
    }

    //region Check Life
    void checkLife(Canvas canvas, int _life) {
        if (_life == 3) {
            // Game Over
            //Log.v("Message", "Game Over");
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);
        } else if (_life == 2) {
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);
        } else if (_life == 1) {
            canvas.drawBitmap(life[0], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);
        } else {
            //canvas.drawText("Game Over", canvasWidth / 2 - 40, canvasHeight / 2, state);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 4 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 - 10), 20, null);
            canvas.drawBitmap(life[1], canvasWidth / 2 + (canvasWidth / 3 + 30), 20, null);



        }
    }

    boolean if_over() {
        if (life_count <= 0) {
            return true;
        }
        else {
            return false;
        }
    }
    //endregion

    void showUcil(Canvas canvas, int x, int y) {
        if (score % 310 == 0 && score != 0) {
            canvas.drawBitmap(ucil, x, y, null);
        }
    }

    void showSnow(Canvas canvas, int maxBirdY, int minBirdY) {

        if (score % 70 == 0 && score != 0 && !is_slow) {
            is_show = true;
            if (is_show) {
                is_show = false;
                snowX -= snowSpeed;
                if (snowX < 0) {
                    snowX = canvasWidth + 200;
                    snowY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY)) + minBirdY;
                }

                canvas.drawBitmap(snow, snowX, snowY, null);
            }

        }
    }

    void showFlower(Canvas canvas, int x, int y) {
        canvas.drawBitmap(flower[0], x, y, null);
    }

    //region Change State
    void changeState(Canvas canvas, Rect rect) {
        if (score >= 200 && score <= 300) {
            canvas.drawBitmap(bgImage[1], null, rect, null);
            canvas.drawText("Lv. 2", canvasWidth / 2, 60, levelPaint);
        } else if (score > 300) {
            canvas.drawBitmap(bgImage[2], null, rect, null);
            canvas.drawText("Lv. 3", canvasWidth / 2, 60, levelPaint);
        } else {
            canvas.drawBitmap(bgImage[0], null, rect, null);
            canvas.drawText("Lv. 1", canvasWidth / 2, 60, levelPaint);
        }
    }
    //endregion

    //region Hit Check
    public boolean hitCheck(int x, int y) {
        if (birdX < x && x < (birdX + bird[0].getWidth()) &&
                birdY < y && y < (birdY + bird[0].getHeight())) {
            return true;
        }
        return false;
    }
    //endregion

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch_flg = true;
            birdSpeed = -20;
        }
        return true;
    }
}
