package com.madeso.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SmartRegion extends SmartTexture {
    public TextureRegion[][] regions;
    int width = 64;
    int height = 64;

    public SmartRegion(AssetManager assetManager, String name) {
        super(assetManager, name);
    }

    public SmartRegion setTileSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public void postLoad() {
        super.postLoad();
        this.regions = TextureRegion.split(this.texture, width, height);
    }
}
