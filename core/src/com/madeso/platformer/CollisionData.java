package com.madeso.platformer;

public class CollisionData {
    public CollisionFlags flags = new CollisionFlags();

    public CollisionData(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CollisionData(float x, float y, CollisionFlags flags) {
        this.x = x;
        this.y = y;
        this.flags = flags;
    }

    public float x;
    public float y;
}
