package com.example.samsungproject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import com.example.samsungproject.gamepanel.Joystick;
import com.example.samsungproject.gamepanel.Perfomance;

import java.util.ArrayList;
import java.util.List;


public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Context context;
    private Joystick joystick; // Джойстик
    private Player player; // Игрок
    Paint paint;
    public static Resources res;
    GameLoop gameLoop;
    float hs, ws;//ширина и высота области рисования
    boolean isFirstDraw = true;
    GameMap gameMap;
    private List<Spell> spellList = new ArrayList<Spell>();
    private int numberOfSpellsToCast = 0;
    private float touchX, touchY;
    private Perfomance perfomance;
    //private GameDisplay gameDisplay;

    public Game(Context context) {
        super(context);
        getHolder().addCallback(this);
        res = getResources();

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        perfomance = new Perfomance(context, gameLoop);
        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            SurfaceHolder surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        gameLoop.setRunning(false);
        try {
            gameLoop.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(isFirstDraw){
            hs = getHeight();
            ws = getWidth();
            joystick = new Joystick(275, 700, 140, 80);
            player = new Player(context,ws / 2, (hs/2)+120, joystick);
            isFirstDraw = false;
        }

        //gameMap.draw(canvas); // Рисовать карту

        player.draw(canvas); // Рисовать игрока

        for (Spell spell: spellList) {
            spell.draw(canvas);
        }
        joystick.draw(canvas); // Рисовать джойстик
        perfomance.draw(canvas);
        update();
    }

    public void update(){
        player.update();
        joystick.update();

        if (player.isJump) player.jump();

        while (numberOfSpellsToCast > 0){
            spellList.add(new Spell(getContext(), player, touchX, touchY));
            numberOfSpellsToCast--;
        }

        for (Spell spell: spellList) {
            spell.update();
        }
        //gameDisplay.update();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)) {
            if (player.jumpIsPressed(event.getX(1), event.getY(1))) player.isJump = true; // Player's jump
            if ((joystick.getIsPressed()) && (!player.isJump)) {
                touchX = event.getX(1);
                touchY = event.getY(1);
                numberOfSpellsToCast++;
            }
        }
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (player.jumpIsPressed(event.getX(), event.getY())) player.isJump = true;

                if ((joystick.getIsPressed())){
                    touchX = event.getX();
                    touchY = event.getY();
                    numberOfSpellsToCast++;
                }
                else if (joystick.isPressed(event.getX(), event.getY())){
                    joystick.setIsPressed(true);
                }
                else if (!player.isJump){
                    touchX = event.getX();
                    touchY = event.getY();
                    numberOfSpellsToCast++;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()){
                    joystick.setActuator(event.getX(), event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
        }
        return true;
    }

    public void pause() {
        gameLoop.stopLoop();
    }
}