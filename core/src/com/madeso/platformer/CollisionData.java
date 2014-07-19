package com.madeso.platformer;

public class CollisionData {
    public boolean collided = false;

    public CollisionData(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CollisionData(float x, float y, boolean collided) {
        this.x = x;
        this.y = y;
        this.collided = collided;
    }

    public float x;
    public float y;
}
