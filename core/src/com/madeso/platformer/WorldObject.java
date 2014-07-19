package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import javafx.animation.Animation;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldObject implements Disposable, Moveable {
    protected final PlatformGame game;

    private float x = 0;
    private float y = 0;

    private float targetX = 0;
    private float targetY = 0;
    private float suggestedX = 0;
    private float suggestedY = 0;
    private boolean drawSuggested = false;
    private boolean collideWithWorld = true;
    protected boolean removeMe = false;
    private List<AnimationGroup> groups = new ArrayList<AnimationGroup>();

    protected Destructor destructor = new Destructor();

    protected CollisionFlags latestFlags = new CollisionFlags();
    private boolean facingRight = false;

    public WorldObject(PlatformGame game) {
        this.game = game;
    }

    @Override
    public void dispose() {
        destructor.dispose();
    }

    public void teleport(float x, float y) {
        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
    }

    public void move(float dx, float dy) {
        targetX += dx;
        targetY += dy;
    }

    protected abstract void subupdate(float dt);

   boolean update(float dt) {
       for(AnimationGroup g : groups) {
           g.update(dt);
       }
       subupdate(dt);
       return this.removeMe;
   }

    protected AnimationGroup createGroup(SmartAnimation animation) {
        AnimationGroup g = new AnimationGroup(animation);
        this.groups.add(g);
        return g;
    }

    @Override
    public String toString() {
        return Float.toString(suggestedX) + "," + Float.toString(suggestedY);
    }

    public abstract void render(SpriteBatch batch, OrthographicCamera cam);

    public void subrender(AnimationGroup animation, SpriteBatch batch, OrthographicCamera cam) {
        float size = 64f;

        if( animation == null) throw new NullPointerException("animation is null");
        if( animation.getAnimation()==null) throw new NullPointerException("get anim is null");

        TextureRegion reg = animation.getAnimation().getKeyFrame(animation.getTime(), animation.isLooping());

        if( this.facingRight ) {
            batch.draw(reg, x, y, size, size);
        }
        else {
            batch.draw(reg, x+size, y, -size, size);
        }


        if( this.drawSuggested ) {
            batch.setColor(1, 0, 0, 0.5f);
            batch.draw(reg, suggestedX, suggestedY, size, size);
            batch.setColor(1, 1, 1, 1);
        }
    }

    @Override
    public void applyMovement(OrthoMap map) {
        CollisionData cd = map.basic(x, y, targetX, targetY, 64, 64);
        suggestedX = cd.x;
        suggestedY = cd.y;
        this.latestFlags = cd.flags;

        // update position
        if( this.collideWithWorld ) {
            this.x = suggestedX;
            this.y = suggestedY;
        }
        else {
            this.x = targetX;
            this.y = targetY;
        }

        // update movement code
        this.targetX = this.x;
        this.targetY = this.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void faceLeft() {
        this.facingRight = false;
    }

    public void faceRight() {
        this.facingRight = true;
    }

    public boolean isFacingRight() {
        return this.facingRight;
    }

    public Rectangle getRect() {
        return new Rectangle(this.x, this.y, 64, 64);
    }
}
