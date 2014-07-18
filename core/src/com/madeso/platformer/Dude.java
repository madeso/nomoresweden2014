package com.madeso.platformer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Dude implements Disposable {
    private final PlatformGame game;
    private final SmartTexture img;

    float x = 0;
    float y = 0;

    public Dude(PlatformGame game) {
        this.game = game;
        this.img = game.assetManager.texture("player.png");
    }

    @Override
    public void dispose() {
        this.img.dispose();
    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    @Override
    public String toString() {
        return Float.toString(x) + "," + Float.toString(y);
    }

    public void render(SpriteBatch batch, OrthographicCamera cam) {
        float size = 64f;
        batch.draw(this.img.texture, x, y, size, size);
    }
}
