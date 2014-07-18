package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
    static final float UNITS_PER_METER = 2;
    private final OrthographicCamera fontCamera;
    private OrthographicCamera worldCamera;
    PlatformGame game;
    Dude dude;
    OrthoMap map;

    public GameScreen(PlatformGame game, String path) {
        this.game = game;

        this.worldCamera = new OrthographicCamera( 640, 480 );
        this.fontCamera = PlatformGame.CreateTextCamera();

        this.map = game.assetManager.orthoMap(path);

        this.dude = new Dude(game);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        movePlayer(dude, delta, 64);

        cameraTrack(dude, worldCamera);

        worldCamera.update();
        game.batch.setProjectionMatrix(worldCamera.combined);

        this.map.render(worldCamera);
        game.batch.begin();
        this.dude.render(game.batch, worldCamera);
        game.batch.end();

        this.fontCamera.update();
        this.game.batch.setProjectionMatrix(fontCamera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "gamestuff " + dude.toString(), 20, 20);
        game.batch.end();
    }

    private void cameraTrack(Dude dude, OrthographicCamera camera) {
        camera.position.x = dude.x;
        camera.position.y = dude.y;
    }

    private void movePlayer(Dude player, float delta, float speed) {
        float dx = 0;
        float dy = 0;

        if( IsDown(Input.Keys.LEFT, Input.Keys.A) ) {
            dx -= 1;
        }

        if( IsDown(Input.Keys.RIGHT, Input.Keys.D) ) {
            dx += 1;
        }

        if( IsDown(Input.Keys.UP, Input.Keys.W) ) {
            dy += 1;
        }

        if( IsDown(Input.Keys.DOWN, Input.Keys.S) ) {
            dy -= 1;
        }

        dx *= delta * speed;
        dy *= delta * speed;
        player.move(dx,dy);
    }

    private boolean IsDown(int a, int b) {
        return Gdx.input.isKeyPressed(a) || Gdx.input.isKeyPressed(b);
    }

    @Override
    public void resize(int width, int height) {
        Vector3 oldPos = new Vector3(worldCamera.position);
        worldCamera.setToOrtho(false,
                width/ UNITS_PER_METER,
                height / UNITS_PER_METER);
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
