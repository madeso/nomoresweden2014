package com.madeso.platformer;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

public class SmartAssetManager implements Disposable {
    private final AssetManager assetManager;
    private final List<PostLoader> assetsToLoad;

    public SmartAssetManager() {
        this.assetManager = new AssetManager();

        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetsToLoad = new ArrayList<PostLoader>();
    }
    
    public SmartTexture texture(String name) {
        SmartTexture texture = new SmartTexture(this.assetManager, name);
        assetsToLoad.add(texture);
        return texture;
    }

    public void postLoad() {
        for(PostLoader asset: assetsToLoad) {
            asset.postLoad();
        }
        assetsToLoad.clear();
    }

    public void onPostLoad(PostLoader pl) {
        assetsToLoad.add(pl);
    }

    public void finishLoading() {
        this.assetManager.finishLoading();
    }

    public boolean update() {
        return this.assetManager.update();
    }

    @Override
    public void dispose() {
        this.assetManager.dispose();
    }

    public float getProgress() {
        return this.assetManager.getProgress();
    }

    public OrthoMap orthoMap(String path) {
        OrthoMap map = new OrthoMap(this.assetManager, path);
        assetsToLoad.add(map);
        return map;
    }

    public SmartRegion region(String name) {
        SmartRegion region = new SmartRegion(this.assetManager, name);
        assetsToLoad.add(region);
        return region;
    }

    public SmartAnimation animation(String name) {
        SmartAnimation animation = new SmartAnimation(this.assetManager, name);
        assetsToLoad.add(animation);
        return animation;
    }
}
