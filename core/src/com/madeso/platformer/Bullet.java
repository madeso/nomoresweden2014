package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Bullet extends WorldObject {
    private static final float FLASHTIME = 0.05f;
    private final SmartAnimation animation;
    private final AnimationGroup group;
    private final SmartAnimation another;

    private static Random random = new Random();

    public Bullet(PlatformGame game) {
        super(game);
        this.animation = game.assetManager.animation(this.destructor, "player.png").setAnimation(FLASHTIME, new int[][]{{4,0}, {4,2}}).setLooping(false);
        this.another = game.assetManager.animation(this.destructor, "player.png").setAnimation(FLASHTIME, new int[][]{{4,1}, {4,2}}).setLooping(false);
        this.group = this.createGroup(this.animation);
        this.animation.postLoad(); // hacky, preload a big bullet list instead...
        this.another.postLoad();
    }

    public Bullet setup(float x, float y, boolean right) {
        if( right ) faceRight();
        else faceLeft();
        float offset = 32;
        if( Bullet.random.nextBoolean() ) this.group.changeAnimation(this.another);
        else this.group.changeAnimation(this.animation);
        if( !right ) offset = -offset;
        this.teleport(x+offset, y);
        return this;
    }

    @Override
    protected void subupdate(float dt) {
        float dx = -1;
        if( isFacingRight() ) {
            dx = 1;
        }
        if( this.group.getTime() > FLASHTIME ) {
            move(dx * dt * 600, 0);
        }

        if( this.latestFlags.collided() ) {
            this.removeMe = true;
        }
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera cam) {
        this.subrender(this.group, batch, cam);
    }

    public void destroy() {
        this.removeMe = true;
    }
}
