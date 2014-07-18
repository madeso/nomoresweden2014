package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
    static final int PIXELS_PER_METER = 10;
    static final float TILE_WIDTH = 10;
    private OrthogonalTiledMapRenderer renderer;
    private final String path;
    private OrthographicCamera cam;
    PlatformGame game;
    // Texture img;

    public GameScreen(PlatformGame game, String path) {
        this.game = game;

        this.cam = new OrthographicCamera();

        // img = new Texture("tiles.jpg");

        this.path = path;
    }

    public void postLoad() {
        TiledMap map = game.assetManager.get(path);
        // TiledMap map = new TmxMapLoader(new InternalFileHandleResolver()).load("level1.tmx");

        float unitScale = 1;
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        this.renderer.render();
        // game.batch.draw(img, 0, 0, TILE_WIDTH * 4, TILE_WIDTH * 4);
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
        // img.dispose();
        this.game.assetManager.unload(this.path);
    }

    public static void LoadWorld(PlatformGame game, String path) {
        game.assetManager.load(path, TiledMap.class);
        game.setScreen(new LoaderScreen(game, new GameScreen(game, path)));
    }
}
