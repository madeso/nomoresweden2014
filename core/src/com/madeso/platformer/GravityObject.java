package com.madeso.platformer;

public abstract class GravityObject extends WorldObject {
    float vy = 0;
    float mvy = 64*20;
    float gravity = -600;

    public GravityObject(PlatformGame game) {
        super(game);
    }
    @Override
    public void dispose() {
    }

    @Override
    boolean update(float dt) {
        boolean ret = super.update(dt);
        vy += gravity * dt;
        if( Math.abs(vy) > mvy ) {
            vy = Math.signum(vy) * mvy;
        }
        move(0, vy*dt);
        if( this.latestFlags.y() ) this.vy = 0;

        return ret;
    }

    public boolean jump(float strength) {
        if( this.latestFlags.down ) {
            forcejump(strength);
            return true;
        }
        return false;
    }

    protected void forcejump(float strength) {
        this.vy = strength;
        this.latestFlags.down = false;
    }
}
