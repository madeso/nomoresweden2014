package com.madeso.platformer;

public class GameState {

    public static float murdering = 0.0f;
    public static float dt = 1.0f;

    public static void murder() {
        murdering += 0.5f;
    }

    public static void update(float dt) {
        if( murdering > 0.0f ) {
            murdering -= dt;
        }
    }
}
