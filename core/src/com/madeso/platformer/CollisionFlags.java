package com.madeso.platformer;

public class CollisionFlags {
    public boolean down = false;
    public boolean up = false;
    public boolean right = false;
    public boolean left = false;

    public boolean collided() {
        return x() || y();
    }

    public boolean y() {
        return up || down;
    }

    public boolean x() {
        return left || right;
    }
}
