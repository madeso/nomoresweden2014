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
    private OrthogonalTiledMapRenderer renderer;
    private final String path;
    private OrthographicCamera worldCamera;
    PlatformGame game;
    // Texture img;

    public GameScreen(PlatformGame game, String path) {
        this.game = game;

        this.worldCamera = new OrthographicCamera();
        this.fontCamera = PlatformGame.CreateTextCamera();

        this.path = path;
    }

    public void postLoad() {
        TiledMap map = game.assetManager.get(path);

        float unitScale = 1;
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        worldCamera.update();
        game.batch.setProjectionMatrix(worldCamera.combined);
        game.batch.begin();
        this.renderer.render();
        game.batch.end();

        this.fontCamera.update();
        this.game.batch.setProjectionMatrix(fontCamera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "gamestuff", 10, 10);
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
        // img.dispose();
        this.game.assetManager.unload(this.path);
    }

    public static void LoadWorld(PlatformGame game, String path) {
        game.assetManager.load(path, TiledMap.class);
        game.setScreen(new LoaderScreen(game, new GameScreen(game, path)));
    }
}
