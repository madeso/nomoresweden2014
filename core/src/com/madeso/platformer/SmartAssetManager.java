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
    private List<PostLoader> assetsToLoad;

    public SmartAssetManager() {
        this.assetManager = new AssetManager();

        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetsToLoad = new ArrayList<PostLoader>();
    }
    
    public SmartTexture texture(Destructor destructor, String name) {
        SmartTexture texture = new SmartTexture(this.assetManager, name);
        assetsToLoad.add(texture);
        destructor.add(texture);
        return texture;
    }

    public void postLoad() {
        List<PostLoader> assets = new ArrayList<PostLoader>(assetsToLoad);
        this.assetsToLoad.clear();
        for(PostLoader asset: assets) {
            asset.postLoad();
        }
        assets.clear();
    }

    public boolean isFinished() {
        return this.assetsToLoad.isEmpty();
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

    public OrthoMap orthoMap(Destructor destructor, String path) {
        OrthoMap map = new OrthoMap(this.assetManager, path);
        assetsToLoad.add(map);
        destructor.add(map);
        return map;
    }

    public SmartRegion region(Destructor destructor, String name) {
        SmartRegion region = new SmartRegion(this.assetManager, name);
        assetsToLoad.add(region);
        return region;
    }

    public SmartAnimation animation(Destructor destructor, String name) {
        SmartAnimation animation = new SmartAnimation(this.assetManager, name);
        assetsToLoad.add(animation);
        destructor.add(animation);
        return animation;
    }

    public SmartSound sound(Destructor destructor, String name) {
        SmartSound sound = new SmartSound(this.assetManager, name);
        assetsToLoad.add(sound);
        destructor.add(sound);
        return sound;
    }
}
