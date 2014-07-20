package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
    static final float UNITS_PER_METER = 1f;
    private final OrthographicCamera fontCamera;
    private OrthographicCamera worldCamera;
    PlatformGame game;
    WorldObject dude;
    OrthoMap map;
    GameWorld moveables = new GameWorld();
    Music music;
    Destructor destructor = new Destructor();

    public GameScreen(final PlatformGame game, int id) {
        this.game = game;

        this.worldCamera = new OrthographicCamera( 640, 480 );
        this.fontCamera = PlatformGame.CreateTextCamera();

        String levelpath = "level" + Integer.toString(id) + ".tmx";
        String musicpath = "music" + Integer.toString(id) + ".ogg";

        this.map = game.assetManager.orthoMap(destructor, levelpath);

        Player p = new Player(this.moveables, game);
        // p.teleport(70,70);
        this.moveables.spawn(p);

        this.dude = p;

        // ninja
        this.map.registerCreator("4", new OrthoMap.ObjectCreator() {
            @Override
            public void create(OrthoMap map, float x, float y, MapObject properties) {
                moveables.spawn(new Enemy(moveables, game, x, y, 0));
            }
        });

        // agent
        this.map.registerCreator("8", new OrthoMap.ObjectCreator() {
            @Override
            public void create(OrthoMap map, float x, float y, MapObject properties) {
                moveables.spawn(new Enemy(moveables, game, x, y, 1));
            }
        });

        // suicidal
        this.map.registerCreator("12", new OrthoMap.ObjectCreator() {
            @Override
            public void create(OrthoMap map, float x, float y, MapObject properties) {
                moveables.spawn(new Enemy(moveables, game, x, y, 2));
            }
        });

        // end
        this.map.registerCreator("15", new OrthoMap.ObjectCreator() {
            @Override
            public void create(OrthoMap map, float x, float y, MapObject properties) {
                moveables.spawn(new Trigger(game, properties, x,y));
            }
        });

        // player
        this.map.registerCreator("16", new OrthoMap.ObjectCreator() {
            @Override
            public void create(OrthoMap map, float x, float y, MapObject properties) {
                dude.teleport(x,y);
            }
        });

        music = Gdx.audio.newMusic(Gdx.files.internal(musicpath));
        music.setLooping(true);
        music.play();
    }

    private Integer nextLevel = null;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.moveables.update(delta * GameState.dt, this.map);

        if( GameState.dt > 0.9f && Gdx.input.isTouched() && dude.getClass() == PlayerBody.class ) {
            game.setScreen(new MainMenuScreen(game));
            this.dispose();
            return;
        }

        this.moveables.overlap(Bullet.class, Enemy.class, new GameWorld.Collision() {
            @Override
            public void collide(WorldObject left, WorldObject right) {
                Bullet bullet = (Bullet)left;
                Enemy enemy = (Enemy) right;
                bullet.destroy();
                enemy.hurt(bullet.isFacingRight());
            }
        });

        this.moveables.overlap(Player.class, Enemy.class, new GameWorld.Collision() {
            @Override
            public void collide(WorldObject left, WorldObject right) {
                Player player = (Player)left;
                Enemy other = (Enemy) right;
                PlayerBody b = player.kill(other.isFacingRight());
                if( b != null ) {
                    dude = b;
                }
                GameState.dt = 0.2f;
            }
        });

        final GameScreen self = this;
        this.moveables.overlap(Player.class, Trigger.class, new GameWorld.Collision() {
            @Override
            public void collide(WorldObject left, WorldObject right) {
                Player player = (Player)left;
                Trigger other = (Trigger) right;
                other.trigger();
                self.nextLevel = new Integer(other.nextLevel);
            }
        });

        cameraTrack(dude, worldCamera);

        worldCamera.update();
        game.batch.setProjectionMatrix(worldCamera.combined);

        this.map.render(worldCamera);
        game.batch.begin();
        this.moveables.render(game.batch, worldCamera);
        game.batch.end();

        this.fontCamera.update();
        this.game.batch.setProjectionMatrix(fontCamera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "gamestuff", 20, 20);
        game.batch.end();


        if( nextLevel != null ) {
            int next = nextLevel.intValue();
            if( next < 0 ) {
                this.game.setScreen(new GameCompletedScreen(game));
            }
            else {
                this.music.stop();
                GameScreen.LoadWorld(game, next);
            }
            this.dispose();
        }
    }

    private void cameraTrack(WorldObject dude, OrthographicCamera camera) {
        if( dude != null ) {
            camera.position.x = NiceValue(dude.getX());
            camera.position.y = NiceValue(dude.getY());
        }
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
        this.moveables.dispose();
        music.stop();
        music.dispose();
    }

    public static void LoadWorld(PlatformGame game, int id) {
        game.setScreen(new LoaderScreen(game, new GameScreen(game, id)));
    }
}
