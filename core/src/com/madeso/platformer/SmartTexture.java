package com.madeso.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class SmartTexture implements IAsset {
    private final AssetManager assetManager;
    private final String name;
    public Texture texture;

    public SmartTexture(AssetManager assetManager, String name) {
        this.assetManager = assetManager;
        this.name = name;
        this.assetManager.load(name, Texture.class);
    }

    @Override
    public void postLoad() {
        this.texture = assetManager.get(name);
    }

    @Override
    public void dispose() {
        assetManager.unload(name);
    }
}
