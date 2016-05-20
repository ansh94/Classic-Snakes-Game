package com.anshdeep.snake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Settings extends AppCompatActivity {

    private TextView titleLeft;
    private TextView titleRight;
    private TextView titleMiddle;

    private ImageView swipeButton;
    private ImageView musicButton;
    private ImageView homeButton;
    private Animation compileAnimation;

    private boolean isMusicOn;
    private boolean areButtonOn;
    private RelativeLayout settingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        settingsLayout = (RelativeLayout) findViewById(R.id.settings_layout);
        initSwipeButton();
        initMusicSwitch();
        initHomeButton();
        title();
    }

    private void initSwipeButton() {
        swipeButton = (ImageView) findViewById(R.id.swipe);
        compileAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_for_classic_button);
        compileAnimation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
                areButtonOn = preferences.getBoolean(GameSettings.USE_BUTTON_CONTROLS, true);
                if (areButtonOn) {
                    swipeButton.setImageResource(R.mipmap.buttons);
                } else {
                    swipeButton.setImageResource(R.mipmap.swipe);
                }
                swipeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeButton.setImageDrawable(null);
                        if (areButtonOn) {
                            areButtonOn = false;
                            swipeButton.setImageResource(R.mipmap.swipe);
                        } else {
                            areButtonOn = true;
                            swipeButton.setImageResource(R.mipmap.buttons);

                        }
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(GameSettings.USE_BUTTON_CONTROLS, areButtonOn);
                        editor.commit();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        swipeButton.startAnimation(compileAnimation);
    }

    private void initMusicSwitch() {
        musicButton = (ImageView) findViewById(R.id.music);
        compileAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_for_no_button);
        compileAnimation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
                isMusicOn = preferences.getBoolean(GameSettings.PLAY_MUSIC, true);
                if (isMusicOn) {
                    musicButton.setImageResource(R.mipmap.music_on);
                } else {
                    musicButton.setImageResource(R.mipmap.music_off);
                }
                musicButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        musicButton.setImageDrawable(null);
                        if (isMusicOn) {
                            isMusicOn = false;
                            musicButton.setImageResource(R.mipmap.music_off);
                        } else {
                            isMusicOn = true;
                            musicButton.setImageResource(R.mipmap.music_on);
                        }
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(GameSettings.PLAY_MUSIC, isMusicOn);
                        editor.commit();
                    }
                });


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        musicButton.startAnimation(compileAnimation);
    }

    private void initHomeButton() {
        homeButton = (ImageView) findViewById(R.id.home);
        compileAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_for_home_button);
        compileAnimation.setDuration(GameSettings.ANIMATION_SHOW_HOME_BUTTON_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                homeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        Animation animationHide = AnimationUtils.loadAnimation(Settings.this, R.anim.reverse_for_home_button);
                        animationHide.setDuration(GameSettings.ANIMATION_HIDE_HOME_BUTTON_DURATION);

                        swipeButton.setImageResource(R.mipmap.menu_options);
                        musicButton.setImageResource(R.mipmap.menu_options);

                        Animation animationsSwipe = AnimationUtils.loadAnimation(Settings.this, R.anim.reverse_for_classic_button);
                        animationsSwipe.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationMusic = AnimationUtils.loadAnimation(Settings.this, R.anim.reverse_for_no_button);
                        animationMusic.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationTitleLeft = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_title_left);
                        animationTitleLeft.setDuration(GameSettings.ANIMATION_SHOW_HOME_BUTTON_DURATION);

                        Animation animationTitleRight = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_title_right);
                        animationTitleRight.setDuration(GameSettings.ANIMATION_SHOW_HOME_BUTTON_DURATION);

                        Animation animationTitleMiddle = AnimationUtils.loadAnimation(Settings.this, R.anim.anim_for_title_middle);
                        animationTitleMiddle.setDuration(GameSettings.ANIMATION_SHOW_HOME_BUTTON_DURATION);

                        homeButton.startAnimation(animationHide);
                        swipeButton.startAnimation(animationsSwipe);
                        musicButton.startAnimation(animationMusic);
                        titleLeft.startAnimation(animationTitleLeft);
                        titleRight.startAnimation(animationTitleRight);
                        titleMiddle.startAnimation(animationTitleMiddle);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentMain = new Intent(Settings.this, MainMenu.class);
                                intentMain.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intentMain);

                            }
                        }, GameSettings.START_NEW_ACTIVITY_DURATION);
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        homeButton.startAnimation(compileAnimation);
    }

    private void title() {
        titleLeft = (TextView) findViewById(R.id.snake_left);
        titleMiddle = (TextView) findViewById(R.id.snake_middle);
        titleRight = (TextView) findViewById(R.id.snake_right);

        compileAnimation = AnimationUtils.loadAnimation(Settings.this, R.anim.back_anim_for_title_left);
        compileAnimation.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        titleLeft.startAnimation(compileAnimation);

        compileAnimation = AnimationUtils.loadAnimation(Settings.this, R.anim.back_anim_for_title_right);
        compileAnimation.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        titleRight.startAnimation(compileAnimation);

        compileAnimation = AnimationUtils.loadAnimation(Settings.this, R.anim.back_anim_for_title_middle);
        compileAnimation.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);
        compileAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        titleMiddle.startAnimation(compileAnimation);
    }

    @Override
    public void onBackPressed() {
    }
}
