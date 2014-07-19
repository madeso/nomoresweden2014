package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Dude extends WorldObject {
    private final SmartAnimation animation;
    float vy = 0;
    float mvy = 64*20;
    float gravity = -600;

    public Dude(PlatformGame game) {
        super(game);

        this.animation = game.assetManager.animation("player.png").setAnimation(0.5f, new int[][]{ {0,0}, {0,1} });
    }
    @Override
    public void dispose() {
        this.animation.dispose();
    }

    @Override
    void update(float dt) {
        super.update(dt);
        vy += gravity * dt;
        if( Math.abs(vy) > mvy ) {
            vy = Math.signum(vy) * mvy;
        }
        move(0, vy*dt);
        if( this.latestFlags.y() ) this.vy = 0;
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera cam) {
        this.subrender(this.animation, batch, cam);
    }

    public void jump(float strength) {
        if( this.latestFlags.down ) {
            this.vy = 500;
            this.latestFlags.down = false;
        }
    }
}
