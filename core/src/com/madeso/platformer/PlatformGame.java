package com.madeso.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlatformGame extends Game {
    public static final float TEXTWIDTH = 800;
    private static final float TEXTHEIGHT = 480;
    public SpriteBatch batch;
    public BitmapFont font;
    public SmartAssetManager assetManager;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.assetManager = new SmartAssetManager();
        this.setScreen(new MainMenuScreen(this));
        // GameScreen.LoadWorld(this, 1);
	}

	@Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.font.dispose();
        this.assetManager.dispose();
    }

    public static OrthographicCamera CreateTextCamera() {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, TEXTWIDTH, TEXTHEIGHT);
        return camera;
    }
}
