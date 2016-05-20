package com.anshdeep.snake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class ClassicSnake extends AppCompatActivity {

    private boolean playMusic;
    private MediaPlayer musicPlayer;

    private RelativeLayout classicSnakeLayout;
    private boolean isInitialized;

    private GestureDetector gestureDetector;
    private boolean isPaused;

    private boolean isGoingLeft = false;
    private boolean isGoingRight = false;
    private boolean isGoingUp = false;
    private boolean isGoingDown = false;

    private boolean clickLeft;
    private boolean clickRight;
    private boolean clickUp;
    private boolean clickDown;


    private ImageView btnRight,btnLeft,btnDown,btnUp;

    private boolean useButtons;
    private int playerScore;

    private boolean gameOver = false;

    private ArrayList<ImageView> parts;

    private int screenHeight,screenWidth;

    private ArrayList<ImageView> points;
    private boolean isCollide = false;
    private Handler myHandler;
    private ImageView head;

    private TextView textScore;

    private int speedX = 17;
    private int speedY = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_classic_snake);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        musicOnOff();
        classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
        classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake);
        classicSnakeLayout.setPaddingRelative(GameSettings.LAYOUT_PADDING,GameSettings.LAYOUT_PADDING,GameSettings.LAYOUT_PADDING,GameSettings.LAYOUT_PADDING);
        textScore = (TextView) findViewById(R.id.score);
        isInitialized = false;
    }

    private void musicOnOff(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
        playMusic = preferences.getBoolean(GameSettings.PLAY_MUSIC,true);
        musicPlayer = MediaPlayer.create(ClassicSnake.this,R.raw.music);
        if(playMusic){
            musicPlayer.setLooping(true);
            musicPlayer.start();
        }
        else {
            musicPlayer.stop();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector.onTouchEvent(event)){
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        musicPlayer.release();
    }

    private void onSwipeRight(){
        if(isGoingRight == false && isGoingLeft == false){
            isGoingRight = true;
            isGoingLeft = false;
            isGoingUp = false;
            isGoingDown = false;
        }
    }

    private void onSwipeLeft(){
        if(isGoingLeft == false && isGoingRight == false){
            isGoingLeft = true;
            isGoingRight= false;
            isGoingUp = false;
            isGoingDown = false;
        }
    }

    private void onSwipeDown(){
        if(isGoingDown == false && isGoingUp == false){
            isGoingDown = true;
            isGoingLeft = false;
            isGoingUp = false;
            isGoingRight = false;
        }
    }

    private void onSwipeUp(){
        if(isGoingUp == false && isGoingDown == false){
            isGoingUp = true;
            isGoingLeft = false;
            isGoingRight = false;
            isGoingDown = false;
        }
    }

    private void clickRight(){
        if(clickRight == false && clickLeft==false){
            clickRight = true;
            clickLeft = false;
            clickUp = false;
            clickDown = false;
        }
    }

    private void clickLeft(){
        if(clickLeft == false && clickRight==false){
            clickLeft = true;
            clickRight = false;
            clickUp = false;
            clickDown = false;
        }
    }

    private void clickDown(){
        if(clickDown == false && clickUp == false){
            clickDown = true;
            clickLeft = false;
            clickUp = false;
            clickRight = false;
        }
    }

    private void clickUp(){
        if(clickUp == false && clickDown==false){
            clickUp = true;
            clickLeft = false;
            clickRight = false;
            clickDown = false;
        }
    }

    private void buttonsDirectionInit(){
        btnRight = (ImageView) findViewById(R.id.btn_right);
        btnLeft = (ImageView) findViewById(R.id.btn_left);
        btnDown = (ImageView) findViewById(R.id.btn_down);
        btnUp = (ImageView) findViewById(R.id.btn_up);

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRight();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLeft();
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDown();
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickUp();
            }
        });

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(GameSettings.PREFS_NAME,Context.MODE_PRIVATE);
        useButtons = preferences.getBoolean(GameSettings.USE_BUTTON_CONTROLS,true);
        if(useButtons){
            btnRight.setVisibility(View.VISIBLE);
            btnLeft.setVisibility(View.VISIBLE);
            btnDown.setVisibility(View.VISIBLE);
            btnUp.setVisibility(View.VISIBLE);
        }
        else{
            btnRight.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.INVISIBLE);
            btnDown.setVisibility(View.INVISIBLE);
            btnUp.setVisibility(View.INVISIBLE);
        }
    }

    private void shake(){
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
        shake.setDuration(GameSettings.SHAKE_DURATION);
        classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
        classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake);
        classicSnakeLayout.startAnimation(shake);
    }

    private void fadeAnim(){
        if(playerScore % GameSettings.POINTS_ANIMATION == 0){
            Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
            classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
            classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake_change);
            classicSnakeLayout.startAnimation(fadeIn);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation fadeOut = AnimationUtils.loadAnimation(ClassicSnake.this,R.anim.fade_out);
                    classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
                    classicSnakeLayout.setBackgroundResource(R.mipmap.background_for_snake);
                    classicSnakeLayout.startAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void collide(){
        gameOver = true;
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(GameSettings.PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(GameSettings.PLAYER_SCORE,playerScore);
        editor.commit();
        Intent intentScore = new Intent(ClassicSnake.this,ClassicScore.class);
        intentScore.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentScore);
    }

    private void checkBitten(){
        ImageView snakeHead = parts.get(0);
        ImageView snakeTile = new ImageView(this);

        for (int i=1;i<parts.size();i++){
            snakeTile = parts.get(i);
            if(snakeHead.getX() == snakeTile.getX() && snakeHead.getY() == snakeTile.getY()){
                collide();
                break;
            }
        }
    }

    private void addTail(){
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.head);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(((screenWidth * 20)/450),((screenHeight*30)/450));
        imageView.setLayoutParams(layoutParams);
        classicSnakeLayout.addView(imageView);
        parts.add(imageView);
    }

    private void setNewPoint(){
        Random random = new Random();
        ImageView newPoint = new ImageView(ClassicSnake.this);
        float x = random.nextFloat() * (screenWidth - newPoint.getWidth());
        float y = random.nextFloat() * (screenHeight - newPoint.getHeight());
        newPoint.setImageResource(R.mipmap.food);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(((screenWidth * 20)/450),((screenHeight * 30)/450));
        newPoint.setLayoutParams(layoutParams);
        newPoint.setX(x);
        newPoint.setY(y);
        isCollide = false;
        classicSnakeLayout.addView(newPoint);
        points.add(points.size(),newPoint);
    }

    private void setFoodPoints(){
        for (int i=0;i<GameSettings.FOOD_POINTS;i++){
            Random rand = new Random();

            ImageView foodItem = new ImageView(this);
            float x = rand.nextFloat() * (screenWidth - foodItem.getWidth());
            float y = rand.nextFloat() * (screenHeight - foodItem.getHeight());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(((screenWidth * 20)/450),((screenHeight * 30)/450));
            foodItem.setImageResource(R.mipmap.food);
            foodItem.setLayoutParams(layoutParams);
            foodItem.setX(x);
            foodItem.setY(y);
            classicSnakeLayout.addView(foodItem);
            points.add(i,foodItem);
        }
    }

    private void update(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!gameOver && !isPaused){
                    try{
                        Thread.sleep(GameSettings.GAME_THREAD);
                        myHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                float left = head.getX() - head.getWidth();
                                float top = head.getY() - head.getHeight();
                                float right = head.getX() + head.getWidth();
                                float bottom = head.getY() + head.getHeight();

                                for (int i=0;i<points.size();i++){
                                    if(!isCollide){
                                        ImageView p = points.get(i);
                                        float left1 = p.getX() - p.getWidth();
                                        float top1 = p.getY() - p.getHeight();
                                        float right1 = p.getX() + p.getWidth();
                                        float bottom1 = p.getY() + p.getHeight();

                                        //player
                                        Rect rc1 = new Rect();
                                        rc1.set((int) left,(int) top,(int) right,(int) bottom);

                                        //food item
                                        Rect rc2 = new Rect();
                                        rc2.set((int) left1,(int) top1,(int) right1,(int) bottom1);

                                        p.getHitRect(rc2);
                                        if(Rect.intersects(rc1,rc2)){
                                            classicSnakeLayout.removeView(p);
                                            points.remove(i);
                                            playerScore++;
                                            isCollide = true;
                                            textScore.setText("Score: " + playerScore);
                                            setNewPoint();
                                            addTail();
                                            shake();
                                            fadeAnim();
                                        }
                                        checkBitten();
                                    }
                                }
                                isCollide = false;
                                if(isGoingRight || clickRight){
                                    for (int i=parts.size()-1;i>=0;i--){
                                        ImageView imageView = parts.get(i);
                                        if(i>0){
                                            ImageView imageView2 = parts.get(i-1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        }
                                        else{
                                            imageView.setX(imageView.getX() + speedX);
                                            if(imageView.getX()+imageView.getWidth()>= screenWidth){
                                                imageView.setX(screenWidth - imageView.getWidth() / 2 );
                                                collide();
                                            }
                                        }
                                    }
                                }
                                else if(isGoingLeft || clickLeft){
                                    for (int i=parts.size()-1;i>=0;i--){
                                        ImageView imageView = parts.get(i);
                                        if(i>0){ //body
                                            ImageView imageView2 = parts.get(i-1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        }
                                        else{ //head
                                            imageView.setX(imageView.getX() - speedX);
                                            if(imageView.getX() <=0){
                                                imageView.setX(0);
                                                collide();
                                            }
                                        }
                                    }
                                }
                                else if(isGoingDown || clickDown){
                                    for (int i=parts.size()-1;i>=0;i--){
                                        ImageView imageView = parts.get(i);
                                        if(i>0){ //body
                                            ImageView imageView2 = parts.get(i-1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        }
                                        else{ //head
                                            imageView.setY(imageView.getY() + speedY);
                                            if(imageView.getY() + imageView.getHeight() >=screenHeight){
                                                imageView.setY(screenHeight - imageView.getHeight() / 2);
                                                collide();
                                            }
                                        }
                                    }
                                }

                                else if(isGoingUp || clickUp){
                                    for (int i=parts.size()-1;i>=0;i--){
                                        ImageView imageView = parts.get(i);
                                        if(i>0){ //body
                                            ImageView imageView2 = parts.get(i-1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        }
                                        else{ //head
                                            imageView.setY(imageView.getY() - speedY);
                                            if(imageView.getY() <=0){
                                                imageView.setY(0);
                                                collide();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                    catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

    public class SwipeGestureDirector extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            if(!useButtons){
                try{
                    float diffX = e2.getX() - e1.getX();
                    float diffY = e2.getY() - e1.getY();
                    if(Math.abs(diffX)>Math.abs(diffY)){
                        //Horizontal swipe
                        if(Math.abs(diffX) > GameSettings.SWIPE_THRESH_HOLD && Math.abs(velocityX) > GameSettings.SWIPE_VELOCITY_THRESH_HOLD){
                            if(diffX > 0){
                                onSwipeRight();
                            }
                            else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    } else if(Math.abs(diffY) > GameSettings.SWIPE_THRESH_HOLD && Math.abs(velocityY)> GameSettings.SWIPE_VELOCITY_THRESH_HOLD){
                        //Vertical swipe
                        if(diffY > 0 ){
                            onSwipeDown();
                        }
                        else {
                            onSwipeUp();
                        }
                        result = true;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return result;
            }
            return result;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(!isInitialized){
            isInitialized = true;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
            myHandler = new Handler();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            gestureDetector = new GestureDetector(null,new SwipeGestureDirector());
            head = new ImageView(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(((screenWidth * 20) / 450),((screenHeight * 30) / 450));
            head.setImageResource(R.mipmap.head);
            head.setLayoutParams(layoutParams);
            head.setX(screenWidth / 2 - head.getWidth());
            head.setY(screenHeight / 2 - head.getHeight());
            classicSnakeLayout.addView(head);


            parts = new ArrayList<ImageView>();
            points = new ArrayList<ImageView>();
            parts.add(0,head);


            layoutParams.setMargins(GameSettings.LAYOUT_MARGIN,GameSettings.LAYOUT_MARGIN,GameSettings.LAYOUT_MARGIN,GameSettings.LAYOUT_MARGIN);
            setFoodPoints();
            buttonsDirectionInit();
            if(hasFocus){
                isPaused = false;
                update();
            }
            super.onWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
