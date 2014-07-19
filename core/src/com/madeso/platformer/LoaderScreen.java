package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class LoaderScreen implements Screen {
    private final PlatformGame game;
    private final OrthographicCamera camera;
    private final GameScreen gs;
    private boolean loaded = false;

    public LoaderScreen(PlatformGame game, GameScreen gs) {
        this.gs = gs;
        this.game = game;
        this.camera = PlatformGame.CreateTextCamera();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.camera.update();
        this.game.batch.setProjectionMatrix(camera.combined);

        if( this.loaded )
        {
            game.batch.begin();
            game.font.draw(game.batch, "Welcome to Platform Game!!! ", 100, 150);
            game.font.draw(game.batch, "Loading done. Touch to play.", 100, 100);
            game.batch.end();

            if (true) { // Gdx.input.isTouched()) {
                game.assetManager.finishLoading();
                game.assetManager.postLoad();
                game.setScreen(this.gs);
                this.dispose();
            }
        }
        else {
            if (game.assetManager.update()) {
                game.assetManager.finishLoading();
                game.assetManager.postLoad();

                if( game.assetManager.isFinished() ) {
                    this.loaded = true;
                }
            }

            // display loading information
            float progress = game.assetManager.getProgress();

            game.batch.begin();
            game.font.draw(game.batch, "Welcome to Platform Game!!! ", 100, 150);
            game.font.draw(game.batch, "Loading: " + Float.toString(progress), 100, 100);
            game.batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
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
    }
}
