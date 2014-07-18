package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
    static final int WIDTH  = 1280;
    static final int HEIGHT = 768;
    static final int PIXELS_PER_METER = 10;
    static final float TILE_WIDTH = 10;
    private OrthographicCamera cam;
    PlatformGame game;

    Texture img;
    public GameScreen(PlatformGame game) {
        this.game = game;

        this.cam = new OrthographicCamera();

        img = new Texture("tiles.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        game.batch.draw(img, 0, 0, TILE_WIDTH * 4, TILE_WIDTH * 4);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        Vector3 oldpos = new Vector3(cam.position);
        cam.setToOrtho(false,
                width/PIXELS_PER_METER,
                height/PIXELS_PER_METER);
        cam.translate(oldpos.x-cam.position.x,oldpos.y-cam.position.y);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
