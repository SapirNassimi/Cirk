package com.example.cirk;

import android.graphics.Color;
import android.graphics.Point;

import java.util.Random;

public class Circle {
    private Point borders;
    private Point location;
    private int color;
    private int radius;

    public Circle(Point borders) {
        this.borders = borders;
        this.location = new Point();

        generateRandomCircle();
    }

    public Circle(Point borders, Point location, int color, int radius) {
        this.borders = borders;
        this.location = location;
        this.color = color;
        this.radius = radius;
    }


    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }


    public void generateRandomCircle() {
        Random random = new Random();

        radius = 50 + random.nextInt(150 - 50 + 1);

        int randX = radius + random.nextInt(borders.x - (2 * radius));
        int randY = 160 + radius + random.nextInt(borders.y - (2 * radius) - 160);

        location.set(randX, randY);

        color = Color.argb(255,
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));
    }
}