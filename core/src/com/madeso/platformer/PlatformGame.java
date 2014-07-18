package com.madeso.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class PlatformGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public AssetManager assetManager;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        this.setScreen(new MainMenuScreen(this));
	}

	@Override
    public void render() {
        super.render(); //important!
    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.font.dispose();
        this.assetManager.dispose();
    }
}
