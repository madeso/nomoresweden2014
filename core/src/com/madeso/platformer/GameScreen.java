package com.madeso.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
    static final float UNITS_PER_METER = 1f;
    private static final float SMOOTH_TIME = 0.25f;
    private static final float BTNSIZE = 32.0f;
    private static final float BTNX = 10;
    private static final float BTNY = 10;
    private static final float BTNSPACE = 5;
    private static final float TEXTWIDTH = PlatformGame.TEXTWIDTH;
    private final OrthographicCamera fontCamera;
    private final Button btnLeft;
    private final Button btnRight;
    private final Button btnJump;
    private final Button btnShoot;
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

        this.btnLeft = new Button(game, this.destructor, "buttons/left");
        this.btnRight = new Button(game, this.destructor, "buttons/right");
        this.btnJump = new Button(game, this.destructor, "buttons/jump");
        this.btnShoot = new Button(game, this.destructor, "buttons/shoot");

        btnLeft.setup(BTNX, BTNY, BTNSIZE, BTNSIZE );
        btnRight.setup(BTNX + BTNSPACE + BTNSIZE, BTNY, BTNSIZE, BTNSIZE );
        btnJump.setup(BTNX + (BTNSPACE + BTNSIZE) / 2, BTNY + BTNSPACE + BTNSIZE, BTNSIZE, BTNSIZE);

        btnShoot.setup(TEXTWIDTH - (BTNSIZE + BTNX), BTNY, BTNSIZE, BTNSIZE);

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
                cx = x;
                cy = y;
                dude.faceRight();
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

        this.fontCamera.update();

        btnLeft.update(this.fontCamera);
        btnRight.update(this.fontCamera);
        btnJump.update(this.fontCamera);
        btnShoot.update(this.fontCamera);

        updateControls();

        this.moveables.update(delta * GameState.dt, this.map);

        this.music.setVolume(GameState.dt);

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
        if( this.dude.getClass() == PlayerBody.class && GameState.dt > 0.9f  ) {
            game.font.draw(game.batch, "Touch to restart!", 10, 10);
        }
        btnLeft.draw(game.batch);
        btnRight.draw(game.batch);
        btnJump.draw(game.batch);
        btnShoot.draw(game.batch);
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

    private void updateControls() {
        GameState.ctrlShooting = D(Input.Keys.X) || D(Input.Keys.SPACE) || btnShoot.isDown() ;
        GameState.ctrlLeft = D(Input.Keys.LEFT) || D(Input.Keys.A) || btnLeft.isDown();
        GameState.ctrlRight = D(Input.Keys.RIGHT) || D(Input.Keys.D) || btnRight.isDown();
        GameState.ctrlJump = D(Input.Keys.UP) || D(Input.Keys.W) || btnJump.isDown();
    }

    private boolean D(int a) {
        return Gdx.input.isKeyPressed(a);
    }

    float cx = 0;
    float cy = 0;

    private void cameraTrack(WorldObject dude, OrthographicCamera camera) {
        if( dude != null ) {
            float dx = 200;
            if( dude.isFacingRight() == false ) dx *= -1;

            cx = Smooth(cx, SMOOTH_TIME, dude.getX() + dx);
            cy = Smooth(cy, SMOOTH_TIME, dude.getY());

            camera.position.x = NiceValue(cx);
            camera.position.y = NiceValue(cy);
        }
    }

    private float Smooth(float now, float smoothTime, float next) {
        float dt = Gdx.graphics.getDeltaTime();

        float distance = next - now;
        float speed = distance / smoothTime;
        return now + speed * dt;
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
        GameState.entry();
    }
}
