package com.madeso.platformer;

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
    SmartAnimation getAnimation() {
        return animation;
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

    public void jump(float strength) {
        if( this.latestFlags.down ) {
            this.vy = 500;
            this.latestFlags.down = false;
        }
    }
}
