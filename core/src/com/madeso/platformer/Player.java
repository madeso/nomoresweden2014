package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends GravityObject {

    private final SmartAnimation headHappy;
    private final SmartAnimation bodyJump;
    private final SmartAnimation bodyFall;
    private float airtime = 0.0f;

    private final SmartAnimation bodyIdle;
    private final SmartAnimation headIdle;
    private final SmartAnimation gunIdle;
    private final AnimationGroup body;
    private final AnimationGroup head;
    private final AnimationGroup gun;
    private final SmartAnimation bodyRun;

    public Player(PlatformGame game) {
        super(game);

        String path = "player.png";
        this.bodyIdle = game.assetManager.animation(this.destructor, path).setAnimation(0.5f, new int[][]{ {0,0} });
        this.bodyRun = game.assetManager.animation(this.destructor, path).setAnimation(0.10f, new int[][]{ {0,1}, {0,2}, {0,3}, {0,2} });
        this.bodyJump = game.assetManager.animation(this.destructor, path).setAnimation(0.1f, new int[][]{ {3,0},{3,1} });
        this.bodyFall = game.assetManager.animation(this.destructor, path).setAnimation(0.1f, new int[][]{ {3,2},{3,3} });

        this.headIdle = game.assetManager.animation(this.destructor, path).setAnimation(0.5f, new int[][]{ {1,0}, {1,1} });
        this.headHappy = game.assetManager.animation(this.destructor, path).setAnimation(0.5f, new int[][]{ {1,2} });

        this.gunIdle = game.assetManager.animation(this.destructor, path).setAnimation(0.5f, new int[][]{ {2,0} });

        this.body = this.createGroup(this.bodyIdle);
        this.head = this.createGroup(this.headIdle);
        this.gun = this.createGroup(this.gunIdle);
    }

    @Override
    protected void subupdate(float dt) {
        float dx = 0;
        float speed = 64 * 3;

        boolean moving = false;

        if( IsDown(Input.Keys.LEFT, Input.Keys.A) ) {
            dx -= 1;
            moving = true;
        }

        if( IsDown(Input.Keys.RIGHT, Input.Keys.D) ) {
            dx += 1;
            moving = true;
        }

        if( IsDown(Input.Keys.UP, Input.Keys.W) ) {
            this.jump(100);
        }

        if( this.latestFlags.down ) {
            if (moving) {
                body.changeAnimation(bodyRun);
            } else {
                body.changeAnimation(bodyIdle);
            }
        }
        else {
            if( this.vy > 0 ) {
                body.changeAnimation(bodyJump);
            }
            else {
                body.changeAnimation(bodyFall);
            }
        }

        if( this.latestFlags.down ) {
            this.airtime = 0;
        }
        else {
            this.airtime += dt;
        }

        if( this.airtime > 0.5f ) {
            this.head.changeAnimation(headHappy);
        }
        else {
            this.head.changeAnimation(headIdle);
        }

        dx *= dt * speed;
        this.move(dx,0);
    }

    private boolean IsDown(int a, int b) {
        return Gdx.input.isKeyPressed(a) || Gdx.input.isKeyPressed(b);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera cam) {
        subrender(this.body, batch, cam);
        subrender(this.head, batch, cam);
        subrender(this.gun, batch, cam);
    }
}
