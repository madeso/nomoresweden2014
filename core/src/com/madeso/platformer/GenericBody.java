package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;


public class GenericBody extends GravityObject {
    protected SmartAnimation dying;
    protected SmartAnimation dead;
    protected AnimationGroup anim;
    private float dx = 0;
    private static Random random = new Random();

    public GenericBody(PlatformGame game) {
        super(game);
    }

    public void setup(float x, float y, boolean right, boolean direction) {
        if( right ) faceRight();
        else faceLeft();
        this.teleport(x,y);
        forcejump(150.0f + random.nextFloat() * 100.0f);
        dx = 1.0f;
        if( !direction ) dx = -1;

        dx *= 0.5f + (random.nextFloat() / 0.5f);
    }

    @Override
    protected void subupdate(float dt) {
        if( this.latestFlags.down ) {
            dx = 0;
            this.anim.changeAnimation(this.dead);
        }
        if( this.latestFlags.x() ) dx = dx*0.25f;

        move(dx*dt*300, 0);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera cam) {
        this.subrender(anim, batch, cam);
    }
}
