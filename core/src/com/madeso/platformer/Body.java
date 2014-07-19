package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;


public class Body extends GenericBody {
    private final SmartSound sounddeath;

    public Body(PlatformGame game, int layer) {
        super(game);

        this.dying = game.assetManager.animation(this.destructor, "enemies.png").setAnimation(0.10f, new int[][]{ {layer,2} });
        this.dead = game.assetManager.animation(this.destructor, "enemies.png").setAnimation(0.10f, new int[][]{ {layer,3} });
        this.anim = this.createGroup(this.dying);
        this.sounddeath = game.assetManager.sound(this.destructor, "enemydies.wav");
    }

    public void setup(float x, float y, boolean right, boolean direction) {
        super.setup(x,y,right, direction);
        this.sounddeath.play();
        GameState.murder();
    }
}
