package com.example.samsungproject.gameobjects;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.samsungproject.GameDisplay;

public abstract class Circle extends GameObject{

    protected float radius;
    protected Paint paint;

    public Circle(Context context, int color, float posX, float posY, float radius) {
        super(posX, posY);

        this.radius = radius;

        paint = new Paint();
        paint.setColor(color);
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(
                posX,
                posY,
                radius,
                paint);
    }
}
