package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Enemy extends GravityObject {
    private static Random random = new Random();
    private final SmartAnimation runa;
    private final AnimationGroup run;
    private int health = 3;

    public Enemy(PlatformGame game, float x,float y) {
        super(game);
        this.teleport(x,y);
        int layer = Enemy.random.nextInt(3);
        this.runa = game.assetManager.animation(this.destructor, "enemies.png").setAnimation(0.10f, new int[][]{ {layer,0}, {layer, 1} });
        this.run = this.createGroup(this.runa);
    }

    @Override
    protected void subupdate(float dt) {
        if(this.latestFlags.left) faceRight();
        if(this.latestFlags.right) faceLeft();

        float dx = 1;
        if(!isFacingRight())dx = -1;
        move(dx*dt*400, 0);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera cam) {
        subrender(this.run, batch, cam);
    }

    public void hurt() {
        this.health -= 1;
        this.flicker(1.0f);
        if( this.health <= 0 ) {
            this.removeMe = true;
        }
    }
}
