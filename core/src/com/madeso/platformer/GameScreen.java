package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
    static final int PIXELS_PER_METER = 10;
    static final float TILE_WIDTH = 10;
    private final OrthographicCamera fontCamera;
    private OrthographicCamera worldCamera;
    PlatformGame game;
    Dude dude;
    OrthoMap map;

    public GameScreen(PlatformGame game, String path) {
        this.game = game;

        this.worldCamera = new OrthographicCamera();
        this.fontCamera = PlatformGame.CreateTextCamera();

        this.map = game.assetManager.orthoMap(path);

        this.dude = new Dude(game);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldCamera.update();
        game.batch.setProjectionMatrix(worldCamera.combined);
        game.batch.begin();
        this.map.render();
        game.batch.end();

        this.fontCamera.update();
        this.game.batch.setProjectionMatrix(fontCamera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "gamestuff", 20, 20);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        Vector3 oldPos = new Vector3(worldCamera.position);
        worldCamera.setToOrtho(false,
                width/PIXELS_PER_METER,
                height/PIXELS_PER_METER);
        worldCamera.translate(oldPos.x - worldCamera.position.x, oldPos.y - worldCamera.position.y);
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
        this.map.dispose();
        dude.dispose();
    }

    public static void LoadWorld(PlatformGame game, String path) {
        game.setScreen(new LoaderScreen(game, new GameScreen(game, path)));
    }
}
