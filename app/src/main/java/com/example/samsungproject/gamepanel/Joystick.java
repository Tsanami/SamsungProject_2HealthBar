package com.example.samsungproject.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick {
    private  int outerCircleCenterPositionX;
    private  int outerCircleCenterPositionY;
    private  int innerCircleCenterPositionX;
    private  int innerCircleCenterPositionY;
    private  int outerCircleRaius;
    private  int innerCircleRaius;
    private  Paint outerCirclePaint;
    private  Paint innerCirclePaint;
    private double joystickCenterToTouchDistance;
    private boolean isPressed;
    private double actuatorX;
    private double actuatorY;

    public Joystick(int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius){


        outerCircleCenterPositionX = centerPositionX;
        outerCircleCenterPositionY = centerPositionY;
        innerCircleCenterPositionX = centerPositionX;
        innerCircleCenterPositionY = centerPositionY;


        // Radius' of circles
        this.outerCircleRaius = outerCircleRadius;
        this.innerCircleRaius = innerCircleRadius;


        // paint circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // paint circles
        innerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.BLUE);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCircleCenterPositionX, outerCircleCenterPositionY, outerCircleRaius, outerCirclePaint);
        canvas.drawCircle(innerCircleCenterPositionX, innerCircleCenterPositionY, innerCircleRaius, innerCirclePaint);
    }

    public boolean isPressed(double tX, double tY) {
        joystickCenterToTouchDistance = Math.sqrt( Math.pow(outerCircleCenterPositionX - tX, 2) + Math.pow(outerCircleCenterPositionY - tY, 2));
        return joystickCenterToTouchDistance < outerCircleRaius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double tX, double tY) {
        double deltaX = tX - outerCircleCenterPositionX;
        double deltaY = tY - outerCircleCenterPositionY;
        double deltaDistance = Math.sqrt( Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if (deltaDistance < outerCircleRaius){
            actuatorX = deltaX/outerCircleRaius;
            actuatorY = deltaY/outerCircleRaius;
        }else{
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }

    public void resetActuator() {
        actuatorX = 0.0;
        actuatorY = 0.0;
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX = (int) (outerCircleCenterPositionX + actuatorX*outerCircleRaius);
        innerCircleCenterPositionY = (int) (outerCircleCenterPositionY + actuatorY*outerCircleRaius);
    }

    public double getActuatorY() {
        return actuatorY;
    }

    public double getActuatorX() {
        return actuatorX;
    }
}
