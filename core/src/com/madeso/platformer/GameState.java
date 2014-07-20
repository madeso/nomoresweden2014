package com.madeso.platformer;

public class GameState {

    public static final float WAIT = 0.5f;
    public static float murdering = 0.0f;
    public static float dt = 1.0f;

    public static boolean ctrlShooting = false;
    public static boolean ctrlLeft = false;
    public static boolean ctrlRight = false;
    public static boolean ctrlJump = false;

    public static void murder() {
        murdering = Math.max(1.0f, murdering+0.25f);
    }

    public static void update(float dt) {
        if( murdering > 0.0f ) {
            murdering -= dt;
        }
    }

    public static void entry() {
        dt = 1;
        murdering = 0;
    }
}
