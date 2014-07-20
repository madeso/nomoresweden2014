package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Button {
    private final SmartTexture white;
    private final SmartTexture dark;
    private float x = 0;
    private float y = 0;
    private float width = 64;
    private float height = 64;

    private boolean down = false;

    public Button(PlatformGame game, Destructor destructor, String path) {
        this.white = game.assetManager.texture(destructor, path + "w.png");
        this.dark = game.assetManager.texture(destructor, path + "d.png");
    }

    public void setup(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(SpriteBatch batch) {
        SmartTexture t = this.dark;
        if( this.down ) t = this.white;
        batch.draw(t.texture, x, y, width, height);
    }

    Rectangle getRect() {
        return new Rectangle(x,y,width, height);
    }

    public boolean isDown() {
        return this.down;
    }

    private boolean calculateIsDown(OrthographicCamera fontCamera) {
        int maxi = 4;

        Rectangle rect = getRect();
        for(int i=0; i<maxi; ++i) {
            if( Gdx.input.isTouched(i) ) {
                float x = Gdx.input.getX(i);
                float y = Gdx.input.getY(i);

                Vector3 temp = fontCamera.unproject(new Vector3(x, y, 0));
                x = temp.x;
                y = temp.y;

                if( rect.contains(x,y)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void update(OrthographicCamera fontCamera) {
        this.down = calculateIsDown(fontCamera);
    }
}
