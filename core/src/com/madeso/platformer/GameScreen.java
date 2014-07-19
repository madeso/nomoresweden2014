package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    static final float UNITS_PER_METER = 1f;
    private final OrthographicCamera fontCamera;
    private OrthographicCamera worldCamera;
    PlatformGame game;
    Player dude;
    OrthoMap map;
    List<WorldObject> moveables;
    Music music;
    Destructor destructor = new Destructor();

    public GameScreen(PlatformGame game, int id) {
        this.game = game;

        this.worldCamera = new OrthographicCamera( 640, 480 );
        this.fontCamera = PlatformGame.CreateTextCamera();

        this.moveables = new ArrayList<WorldObject>();

        String levelpath = "level" + Integer.toString(id) + ".tmx";
        String musicpath = "music" + Integer.toString(id) + ".ogg";

        this.map = game.assetManager.orthoMap(destructor, levelpath);

        this.dude = new Player(game);
        this.dude.teleport(70,70);
        this.moveables.add(this.dude);

        music = Gdx.audio.newMusic(Gdx.files.internal(musicpath));
        music.setLooping(true);
        // music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for (WorldObject m : moveables) {
            m.update(delta);
        }
        for (Moveable m : moveables) {
            m.applyMovement(this.map);
        }

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

    private void cameraTrack(WorldObject dude, OrthographicCamera camera) {
        camera.position.x = NiceValue(dude.getX());
        camera.position.y = NiceValue(dude.getY());
    }

    private float NiceValue(float x) {
        // hides the issue with ugly lines appearing between the tiles
        // shamelessly stolen from: http://www.reddit.com/r/gamedev/comments/1euvrs/libgdx_tiledmap_linesgaps_between_tiles/
        float smooth = 5.0f;
        return MathUtils.round(smooth * x) / smooth;
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
        this.destructor.dispose();
        for (WorldObject m : moveables) {
            m.dispose();
        }
        music.stop();
        music.dispose();
    }

    public static void LoadWorld(PlatformGame game, int id) {
        game.setScreen(new LoaderScreen(game, new GameScreen(game, id)));
    }
}
