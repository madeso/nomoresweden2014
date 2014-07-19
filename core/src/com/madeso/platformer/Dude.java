package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Dude implements Disposable, Moveable {
    private final PlatformGame game;
    private final SmartTexture img;

    private float x = 0;
    private float y = 0;

    private float tx = 0;
    private float ty = 0;
    private float sx = 0;
    private float sy = 0;

    public Dude(PlatformGame game) {
        this.game = game;
        this.img = game.assetManager.texture("player.png");
    }

    @Override
    public void dispose() {
        this.img.dispose();
    }

    public void teleport(float x, float y) {
        this.x = x;
        this.y = y;
        this.tx = x;
        this.ty = y;
    }

    public void move(float dx, float dy) {
        tx += dx;
        ty += dy;
    }

    @Override
    public String toString() {
        return Float.toString(sx) + "," + Float.toString(sy);
    }

    public void render(SpriteBatch batch, OrthographicCamera cam) {
        float size = 64f;
        batch.draw(this.img.texture, x, y, size, size);

        batch.setColor(1, 0, 0, 0.5f);
        batch.draw(this.img.texture, sx, sy, size, size);

        batch.setColor(1, 1, 1, 1);
    }

    @Override
    public void applyMovement(OrthoMap map) {
        CollisionData cd = map.basic(x,y,tx,ty,64,64);
        sx = cd.x;
        sy = cd.y;

        // update position
        this.x = tx;
        this.y = ty;

        // update movement code
        this.tx = this.x;
        this.ty = this.y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
