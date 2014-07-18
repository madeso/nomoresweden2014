package com.madeso.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class OrthoMap implements IAsset {
    private final AssetManager assetManager;
    private final String path;
    private OrthogonalTiledMapRenderer renderer;

    public OrthoMap(AssetManager assetManager, String path) {
        this.assetManager = assetManager;
        this.path = path;

        this.assetManager.load(path, TiledMap.class);
    }

    @Override
    public void postLoad() {
        TiledMap map = this.assetManager.get(path);
        float unitScale = 1;
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void dispose() {
        this.renderer.dispose();
        this.assetManager.unload(path);
    }

    public void render() {
        this.renderer.render();
    }
}
