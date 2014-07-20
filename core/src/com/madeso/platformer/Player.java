package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player extends GravityObject {

    private static final float GUNTIMER = 0.1f;
    private final SmartAnimation headHappy;
    private final SmartAnimation bodyJump;
    private final SmartAnimation bodyFall;
    private final SmartSound soundJump;
    private final SmartSound soundThud;
    private final SmartSound soundHardThud;
    private final SmartSound soundSoftThud;
    private final GameWorld world;
    private final SmartAnimation headMurder;
    private PlayerBody pbody;
    private float airtime = 0.0f;

    private final SmartAnimation bodyIdle;
    private final SmartAnimation headIdle;
    private final SmartAnimation gunIdle;
    private final AnimationGroup body;
    private final AnimationGroup head;
    private final AnimationGroup gun;
    private final SmartAnimation bodyRun;
    private float gcd = 0.0f;
    private final SmartSound soundGun;

    @Override
    public void dispose() {
        super.dispose();
        if( this.pbody != null )
            this.pbody.dispose();
    }

    public Player(GameWorld world, PlatformGame game) {
        super(game);
        this.world = world;
        this.pbody = new PlayerBody(game);

        this.soundJump = game.assetManager.sound(this.destructor, "jump.wav");
        this.soundGun = game.assetManager.sound(this.destructor, "gun.wav");
        this.soundThud = game.assetManager.sound(this.destructor, "thud.wav");
        this.soundHardThud = game.assetManager.sound(this.destructor, "hardthud.wav");
        this.soundSoftThud = game.assetManager.sound(this.destructor, "softthud.wav");

        String path = "player.png";
        this.bodyIdle = game.assetManager.animation(this.destructor, path).setAnimation(0.5f, new int[][]{ {0,0} });
        this.bodyRun = game.assetManager.animation(this.destructor, path).setAnimation(0.10f, new int[][]{ {0,1}, {0,2}, {0,3}, {0,2} });
        this.bodyJump = game.assetManager.animation(this.destructor, path).setAnimation(0.1f, new int[][]{ {3,0},{3,1} });
        this.bodyFall = game.assetManager.animation(this.destructor, path).setAnimation(0.1f, new int[][]{ {3,2},{3,3} });

        this.headIdle = game.assetManager.animation(this.destructor, path).setAnimation(0.5f, new int[][]{ {1,0} });
        this.headHappy = game.assetManager.animation(this.destructor, path).setAnimation(0.4f, new int[][]{ {1,2} });
        this.headMurder = game.assetManager.animation(this.destructor, path).setAnimation(0.2f, new int[][]{ {1,3} });
        // this.headPickup = game.assetManager.animation(this.destructor, path).setAnimation(0.2f, new int[][]{ {1,1} });

        this.gunIdle = game.assetManager.animation(this.destructor, path).setAnimation(0.5f, new int[][]{ {2,0} });

        this.body = this.createGroup(this.bodyIdle);
        this.head = this.createGroup(this.headIdle);
        this.gun = this.createGroup(this.gunIdle);
    }

    boolean lastdown = true;
    float lastvy = 0;

    @Override
    protected void subupdate(float dt) {
        float dx = 0;
        float speed = 64 * 3;

        GameState.update(dt);

        if( this.latestFlags.down && this.lastdown == false ) {
            if( this.lastvy < -600 ) {
                this.soundHardThud.play();
            }
            else if( this.lastvy < -300 ) {
                this.soundThud.play();
            }
            else if( this.lastvy < -90 ) {
                this.soundSoftThud.play();
            }
        }

        boolean moving = false;

        if( IsDown(Input.Keys.LEFT, Input.Keys.A) ) {
            dx -= 1;
            moving = true;
            this.faceLeft();
        }

        if( IsDown(Input.Keys.RIGHT, Input.Keys.D) ) {
            dx += 1;
            moving = true;
            this.faceRight();
        }

        if( IsDown(Input.Keys.UP, Input.Keys.W) ) {
            if ( this.jump(500) ) {
                this.soundJump.play();
            }
        }

        if( IsDown(Input.Keys.X, Input.Keys.SPACE)) {
            if( gcd <= 0 ) {
                this.soundGun.play();
                gcd += GUNTIMER;
                this.world.spawn(new Bullet(this.game).setup(this.getX(), this.getY(), this.isFacingRight()));
            }
        }

        if( gcd > 0 ) {
            gcd -= dt;
        }
        else {
            gcd = 0;
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

        if( GameState.murdering > 0 ) {
            this.head.changeAnimation(headMurder);
        }
        else {
            if (this.airtime > 0.5f) {
                this.head.changeAnimation(headHappy);
            } else {
                this.head.changeAnimation(headIdle);
            }
        }

        dx *= dt * speed;
        this.move(dx,0);

        this.lastdown = this.latestFlags.down;
        this.lastvy = this.vy;
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

    public PlayerBody kill(boolean other) {
        this.removeMe = true;
        if( this.pbody != null ) {
            world.add(this.pbody);
            this.pbody.setup(this.getX(), this.getY(), this.isFacingRight(), other);
            PlayerBody ret = this.pbody;
            this.pbody = null;
            return ret;
        }
        else return null;
    }
}
